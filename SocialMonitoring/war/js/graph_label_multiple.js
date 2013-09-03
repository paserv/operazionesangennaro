$(function() {
	$('#from').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
	$('#to').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
});

function render(rootURL) {
    $.ajax({
        type: 'GET',
        url: rootURL + "label/" + $('#monitoredEntity').val() + "/" + $('#transmission').val() + "/" + $('#from').val() + "/" + $('#to').val() + "/" + $('#limit').val(),
        dataType: "json", // data type of response
        //provare success: return data
        success: function(data) {
            renderHighGraph(data);
        }
    });
}



function renderHighGraph(data) { 
    
    var categ = [];
    var values = [];
    
    $.each(data, function() {
        $.each(this, function(key, value) {
            categ.push(this.axis);
            values.push(parseInt(this.ordinate));
        });
    });
    
    $('#container').highcharts({
        chart: {
            type: $('#graphType').val()
        },
        title: {
            text: 'Word Frequency'
        },
        xAxis: {
            categories: categ
        },
        yAxis: {
            title: {
                text: 'Comments and Posts'
            }
        },
        series: [{
            name: $('#transmission').val(),
            data: values
        }]
    });
}