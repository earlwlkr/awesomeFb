var startDate, endDate;
jQuery(document).ready(function() {
  $.ajax({
    url: '/filters',
    success: function(result) {
      $('#filters').html(result);
      $('.ui.form').removeClass('loading');
      $('.ui.dropdown').dropdown();
      startDate = $('#startDate').pickadate({
        formatSubmit: 'dd/mm/yyyy'
      }).pickadate('picker');
      endDate = $('#endDate').pickadate({
        formatSubmit: 'dd/mm/yyyy'
      }).pickadate('picker');
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
      sentiment = $('#sentiment').val(),
      start = startDate.get('select', 'mm dd yyyy'),
      end = endDate.get('select', 'mm dd yyyy')
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
  params['start'] = start;
  params['end'] = end;
  $.ajax({
    url: '/comments',
    data: params,
    success: function(result) {
      $('#results').html(result);
    }
  });
});