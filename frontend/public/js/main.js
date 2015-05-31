jQuery(document).ready(function() {
  $.ajax({
    url: '/filters',
    success: function(result) {
      $('#filters').html(result);
      $('.ui.form').removeClass('loading');
      $('.ui.dropdown').dropdown();
      $('.datepicker').pickadate({
        formatSubmit: 'dd/mm/yyyy'
      });
    }
  });
});