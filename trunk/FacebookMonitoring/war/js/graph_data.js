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

function renderAll() {
	alert("render all non implementata");
}

function renderTransmission() {
	$.ajax({
		type: 'GET',
		url: rootURL + "data/" + $('#monitoredEntity').val() + "/" + $('#transmission').val() + "/" + $('#from').val() + "/" + $('#to').val(),
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
