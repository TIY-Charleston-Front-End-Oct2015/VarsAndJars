package com.theironyard;

import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;

public class Main {

    public static void insertFood(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO food VALUES (NULL, ?)");
        stmt.setString(1, name);
        stmt.execute();
    }

    public static void insertDrink(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO drinks VALUES (NULL, ?)");
        stmt.setString(1, name);
        stmt.execute();
    }

    public static void insertAlbum(Connection conn, String name, String artist, String genre) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO albums VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, artist);
        stmt.setString(3, genre);
        stmt.execute();
    }

    public static Date generateDate(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        Food food = null;
        Drink drink = null;
        Album album = null;

        ResultSet resultsFood = stmt.executeQuery("SELECT * FROM food ORDER BY RAND() LIMIT 1");
        if (resultsFood.next()) {
            food = new Food();
            food.id = resultsFood.getInt("id");
            food.name = resultsFood.getString("name");
        }

        ResultSet resultsDrink = stmt.executeQuery("SELECT * FROM drinks ORDER BY RAND() LIMIT 1");
        if (resultsDrink.next()) {
            drink = new Drink();
            drink.id = resultsDrink.getInt("id");
            drink.name = resultsDrink.getString("name");
        }

        ResultSet resultsAlbum = stmt.executeQuery("SELECT * FROM albums ORDER BY RAND() LIMIT 1");
        if (resultsAlbum.next()) {
            album = new Album();
            album.id = resultsAlbum.getInt("id");
            album.name = resultsAlbum.getString("name");
            album.artist = resultsAlbum.getString("artist");
            album.genre = resultsAlbum.getString("genre");
        }

        Date date = new Date();
        date.food = food;
        date.drink = drink;
        date.album = album;

        return date;
    }

    public static void main(String[] args) throws SQLException {
	    Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS food (id IDENTITY, name VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS drinks (id IDENTITY, name VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS albums (id IDENTITY, name VARCHAR, artist VARCHAR, genre VARCHAR)");

        /*
        insertFood(conn, "Pizza");
        insertFood(conn, "Mac and Cheese");
        insertFood(conn, "Steak");

        insertDrink(conn, "Wine");
        insertDrink(conn, "Bourbon");
        insertDrink(conn, "Beer");

        insertAlbum(conn, "1989", "Taylor Swift", "Pop");
        insertAlbum(conn, "White", "The Beatles", "Classic Rock");
        insertAlbum(conn, "What's Goin' On", "Marvin Gay", "R&B");
        */

        Spark.externalStaticFileLocation("client");
        Spark.init();

        Spark.get(
                "/random",
                ((request, response) -> {
                    Date date = generateDate(conn);
                    JsonSerializer serializer = new JsonSerializer();
                    return serializer.serialize(date);
                })
        );
    }
}
