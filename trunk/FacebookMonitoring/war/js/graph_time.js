var result;

$(function() {
	$('#from').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
	$('#to').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
});


var rootURL;
//Register listeners
$('#btnSearch').click(function() {
	
	if ($('#container').highcharts() != null) {
		$('#container').highcharts().destroy();
	}
	
	if ($('#debug').is(":checked"))	{
		rootURL = "http://localhost:8888/rest/resource/";
	} else {
		rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
	}
	
	render(rootURL);
	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#transmission').keypress(function(e){
	if(e.which == 13) {
		render(rootURL);
		e.preventDefault();
		return false;
  }
});

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
    result = data;
	
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
    
    var groupingunit = $('#groupingunit').val();
    var approssimazione = $('#approssimazione').val();
    if (groupingunit != 'none') {
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
    	            data: jsonObj,
    	            dataGrouping : {
    					units : [
    						[groupingunit, // unit name
    						[1] ]
    					
    					],
    					approximation : approssimazione,
    					forced : "true",
    					
    					
    				},
    	            
    	        }]
    	    });
    } else {
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
	            data: jsonObj,
	            
	        }]
	    });
    }
    
 
}


function groupGraph() {
	if (result != null) {
	renderStockGraph(result);
	}
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