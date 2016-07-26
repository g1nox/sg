  $(document).ready(function(){
    $('#timeSpanFrom, #timeSpanTo').datepicker( {
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'mm/yy',
        onClose: function(dateText, inst) { 
          var month=$("#ui-datepicker-div .ui-datepicker-month :selected").val();
          var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
          $(this).datepicker('setDate', new Date(year, month, 1));
        },
        beforeShow : function(input, inst) {
          var tmp = $(this).val().split('/');
          $(this).datepicker('option','defaultDate',new Date(tmp[1],tmp[0]-1,1));
          $(this).datepicker('setDate', new Date(tmp[1], tmp[0]-1, 1));
        }
    });
});

