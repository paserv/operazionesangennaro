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
		url: rootURL + $('#monitoredEntity').val() + "/" + $('#transmission').val() + "/" + $('#from').val() + "/" + $('#to').val(),
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
	
	var categ = [];
	var values = [];
	
	$.each(data, function() {
		$.each(this, function(key, value) {
			//categ.push(new Date(this.axisDate).getTime());
			//values.push(parseInt(this.ordinate));
			jsonObj.push([new Date(this.axis).getTime(), parseInt(this.ordinate)]);
		});
	});
	
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},
		
        title: {
            text: 'Andamento likes'
        },
        
        yAxis: {
            title: {
                text: 'likes'
            }
        },
        
        series: [{
            name: $('#transmission').val(),
            data: jsonObj
        }]
    });
}



function renderHighGraph(data, graphType) { 
    
	var categ = [];
	var values = [];
	
	$.each(data, function() {
		$.each(this, function(key, value) {
			categ.push(this.date);
			values.push(parseInt(this.likeCount));
		});
	});
	
	$('#container').highcharts({
        chart: {
            type: graphType
        },
        title: {
            text: 'Andamento likes'
        },
        xAxis: {
            categories: categ
        },
        yAxis: {
            title: {
                text: 'likes'
            }
        },
        series: [{
            name: $('#transmission').val(),
            data: values
        }]
    });
}
