(function () {
  $(document).ready(function () {
    page.init();
  });
  var page = {
    init: function () {
      page.initStyling();
      page.initEvents();
    },
    initStyling: function () {
      page.getDate();
    },
    initEvents: function () {
      $('.noLikey').on('click', function (event) {
        page.getDate();
      });
    },
    loadTmpl: function ($el, data, template) {
      var tmpl = _.template(template);
      $el.html(tmpl(data));
    },
    getDate: function () {
      $.ajax({
        url: '/random',
        method: 'GET',
        success: function (data) {
          window.dateData = JSON.parse(data);
          page.loadTmpl($('.dateDiv'), dateData, $('#dateTmpl').html());
        }
      });
    }
  };
})();
