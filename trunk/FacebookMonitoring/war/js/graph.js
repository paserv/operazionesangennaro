// The root URL for the RESTful services
var rootURL = "http://localhost:8888/rest/resource/";

//Register listeners
$('#btnSearch').click(function() {
	render($('#transmission').val(), $('#graphType').val());
	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#transmission').keypress(function(e){
	if(e.which == 13) {
		render($('#transmission').val(),$('#graphType').val());
		e.preventDefault();
		return false;
    }
});

function render(transmission, graphType) {
	if (transmission == '')
		renderAll(graphType);
	else
		renderTransmission(transmission, graphType);
}



function renderTransmission(transmission, graphType) {
	$.ajax({
		type: 'GET',
		url: rootURL + transmission,
		dataType: "json", // data type of response
		//provare success: return data
		success: function(data) {
            renderList(data);
            renderGraph(data, graphType);
        }
	});
}

//per ora è la stessa di renderTransmission
function renderAll(graphType) {
	$.ajax({
		type: 'GET',
		url: rootURL + 'ballaro',
		dataType: "json", // data type of response
		//provare success: return data
		success: function(data) {
            renderList(data);
            renderGraph(data, graphType);
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
			$('#searchList').append('<li>(' + this.date +'): ' + this.likeCount + '</li>');
		});
	});
}


function renderGraph(data, graphType) { 
    
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
