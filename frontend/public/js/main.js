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

      $('.error.message').hide();
      $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
      });
    }
  });
  $('a.tabular.menu.item').tab();

  $.ajax({
    url: '/stats',
    success: function(result) {
      $('#stats').html(result);
    }
  });

  $.ajax({
    url: '/comments',
    success: function(result) {
      $('#results').html(result);
      $('table').tablesort();
      $('thead th.date').data('sortBy', function(th, td, tablesort) {
        return new Date(td.text());
      });
    }
  });

});

$(document).on('click', '#btnRun', function(e) {
  var limit = $('#limit').val();
  if (limit < 1) {
    $('.error.message').show();
    $('#limit').parent().addClass('error');
    return;
  }
  $('.error.message').hide();
  $('#limit').parent().removeClass('error');
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
  params['limit'] = limit;

  $.ajax({
    url: '/comments',
    data: params,
    success: function(result) {
      $('#results').html(result);
      $('table').tablesort();
      $('thead th.date').data('sortBy', function(th, td, tablesort) {
        return new Date(td.text());
      });
    }
  });
});