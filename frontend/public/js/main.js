var startDate, endDate;

function loadMainPage() {
  $.ajax({
    url: '/filters',
    success: function(result) {
      $('#filters').html(result);
      $('.ui.form').removeClass('loading');
      $('.ui.dropdown').dropdown();
      startDate = $('#startDate').pickadate({
        formatSubmit: 'dd/mm/yyyy',
        selectYears: true,
        selectMonths: true
      }).pickadate('picker');
      endDate = $('#endDate').pickadate({
        formatSubmit: 'dd/mm/yyyy',
        selectYears: true,
        selectMonths: true
      }).pickadate('picker');

      $('.error.message').hide();
      $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
      });
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
}

function loadStatsPage() {
  $.ajax({
    url: '/stats',
    success: function(result) {
      $('#stats').html(result);
    }
  });
}

jQuery(document).ready(function() {
  $('.menu .item').tab();
  loadMainPage();

  $('#tabMain').click(function() {
    loadMainPage();
  });
  $('#tabStats').click(function() {
    console.log($(this));
    loadStatsPage();
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