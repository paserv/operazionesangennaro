var result;
var rootURL;
var jsonObj= [];
var numSelected;
var counter;

$(function() {
	$('#from').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
	$('#to').datetimepicker({dateFormat: "dd-mm-yy", timeFormat: "HH:mm:ss"});
});



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
	numSelected = 0;
	counter = 0;
	$('#transmission :selected').each(function(i, selected) {
		numSelected++;
	});

	getServices(rootURL);

	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#transmission').keypress(function(e){
	if(e.which == 13) {
		getServices(rootURL);
		e.preventDefault();
		return false;
	}
});

function getServices(rootURL) {
	$('#transmission :selected').each(function(i, selected) {
		$.ajax({
			type: 'GET',
			url: rootURL + "time/" + $('#monitoredEntity').val() + "/" + $(selected).val() + "/" + $('#from').val() + "/" + $('#to').val(),
			dataType: "json",
			success: function(data) {
				counter++;
				if (numSelected == 1) {
					result = renderData(data);
					createChart(result);
				} else {
					if ($('#groupingunit').val() != 'none') {
						jsonObj[i] = {
								name: $(selected).text(),
								data: renderData(data),
								dataGrouping : {
									units : [
									         [$('#groupingunit').val(),
									          [1] ]

									         ],
									         approximation : $('#approssimazione').val(),
									         forced : "true",


								}
						};
					} else {
						jsonObj[i] = {
								name: $(selected).text(),
								data: renderData(data),
								dataGrouping : {
									units : [
									         ['day',
									          [1] ]

									         ],
									         approximation : 'sum',
									         forced : "true",


								}
						};
					}
					
				}
				if (numSelected > 1 && counter == numSelected) {
					createMultipleChart();
				}
			}
		});
	});


}


function renderData(res) {
	var temp = [];
	$.each(res, function() {
		$.each(this, function(key, value) {
			var year = this.axis.substring(6,10);
			var month = this.axis.substring(3,5);
			var day = this.axis.substring(0,2);
			var hour = this.axis.substring(11,13);
			var min = this.axis.substring(14,16);           
			temp.push([new Date(year, --month, day, (++hour) + 1, min).getTime(), parseInt(this.ordinate)]);
		});
	});
	return temp;
}

function createChart(res) { 
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
				type: $('#graphType').val(),
				name: $('#transmission').val(),
				data: res,
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
				type: $('#graphType').val(),
				name: $('#transmission').val(),
				data: res,

			}]
		});
	}


}



function createMultipleChart() {
	$('#container').highcharts('StockChart', {
		chart: {
		},

		rangeSelector: {
			selected: 4
		},

		yAxis: {
			plotLines: [{
				value: 0,
				width: 2,
				color: 'silver'
			}]
		},
		series: jsonObj
	});
}

function render() {
	if (jsonObj.lenght != 0) {
		if (numSelected == 1) {
			if (result != undefined) {
				createChart(result);
			}
		} else if (numSelected != undefined && counter == numSelected) {
			createMultipleChart();
		}
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


function iterateExample(seriesOptions) {
	$.each(seriesOptions, function() {
		var graphData = this.data;
		$.each(graphData, function() {
			$.each(this, function(key, value) {
				var year = this.axis.substring(6,10);
				var month = this.axis.substring(3,5);
				var day = this.axis.substring(0,2);
				var hour = this.axis.substring(11,13);
				var min = this.axis.substring(14,16);  
			});
		});
	});
}