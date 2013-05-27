// The root URL for the RESTful services
//var rootURL = "http://localhost:8888/rest/resource/";
//var rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
var rootURL;
//Register listeners
$('#btnSearch').click(function() {
	
	if ($('#local').val() == 'local') {
		rootURL = "http://localhost:8888/rest/resource/";
	} else {
		rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
	}
	
	render($('#transmission').val());
	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#transmission').keypress(function(e){
	if(e.which == 13) {
		render($('#transmission').val());
		e.preventDefault();
		return false;
    }
});

function render(transmission) {
	//controllo sui campi da inserire
	if (transmission == '')
		renderAll();
	else
		renderTransmission();
}



function renderTransmission() {
	$.ajax({
		type: 'GET',
		url: rootURL + "time/" + $('#monitoredEntity').val() + "/" + $('#transmission').val() + "/" + $('#from').val() + "/" + $('#to').val(),
		dataType: "json", // data type of response
		//provare success: return data
		success: function(data) {
            renderList(data);
            renderStockGraph(data);
        }
	});
}

//per ora è la stessa di renderTransmission
function renderAll() {
	$.ajax({
		type: 'GET',
		url: rootURL + 'ballaro',
		dataType: "json", // data type of response
		//provare success: return data
		success: function(data) {
            renderList(data);
            renderStockGraph(data);
        }
	});
}



/*
 * RENDERING FUNCTIONS
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

