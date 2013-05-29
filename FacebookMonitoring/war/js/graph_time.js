



function render(rootURL) {
	$.ajax({
		type: 'GET',
		url: rootURL + "time/" + $('#monitoredEntity').val() + "/" + $('#transmission').val() + "/" + $('#from').val() + "/" + $('#to').val(),
		dataType: "json", // data type of response
		//provare success: return data
		success: function(data) {
            renderStockGraph(data);
        }
	});
}



function renderStockGraph(data) { 
	var jsonObj= [];
	
	$.each(data, function() {
		$.each(this, function(key, value) {
			var year = this.axis.substring(6,10);
			var month = this.axis.substring(3,5);
			var day = this.axis.substring(0,2);
			var hour = this.axis.substring(11,13);
			var min = this.axis.substring(14,16);			
			jsonObj.push([new Date(year, --month, day, (++hour) + 1, min).getTime(), parseInt(this.ordinate)]);
		});
	});
	
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},
		
        title: {
            text: 'Andamento ' + $('#monitoredEntity').val()
        },
        
        yAxis: {
            title: {
                text: $('#monitoredEntity').val()
            }
        },
        
        series: [{
            name: $('#transmission').val(),
            data: jsonObj
        }]
    });
}


/*
 * VARIE
 */

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#searchList li').remove();
	$.each(data, function() {
		$.each(this, function(key, value) {
			$('#searchList').append('<li>(' + this.axis +'): ' + this.ordinate + '</li>');
		});
	});
}