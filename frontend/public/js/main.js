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

  $.ajax({
    url: '/comments',
    success: function(result) {
      $('#results').html(result);
    }
  });

});

$(document).on('click', '#btnRun', function(e) {
  var
      topic = $('#topic').val(),
      spam = $('#spam').val(),
      sentiment = $('#sentiment').val()
  ;
  var params = {};
  if (topic != '') {
    params['topic'] = topic;
  }
  if (spam != '') {
    params['spam'] = spam;
  }
  if (sentiment != '') {
    params['sentiment'] = sentiment;
  }
  $.ajax({
    url: '/comments',
    data: params,
    success: function(result) {
      $('#results').html(result);
    }
  });
});