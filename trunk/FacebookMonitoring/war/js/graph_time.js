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
	//Se è già presente un grafico eliminalo
	if ($('#container').highcharts() != null) {
		$('#container').highcharts().destroy();
	}

	//Prelevo il rootURL dei servizi
	if ($('#debug').is(":checked"))	{
		rootURL = "http://localhost:8888/rest/resource/";
	} else {
		rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
	}

	//Calcolo il numero di trasmissioni selezionate
	numSelected = 0;
	counter = 0;
	jsonObj= [];
	$('#transmission :selected').each(function(i, selected) {
		numSelected++;
	});

	//invoco i servizi
	getServices(rootURL);

	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#transmission').keypress(function(e){
	if(e.which == 13) {
		//Se è già presente un grafico eliminalo
		if ($('#container').highcharts() != null) {
			$('#container').highcharts().destroy();
		}

		//Prelevo il rootURL dei servizi
		if ($('#debug').is(":checked"))	{
			rootURL = "http://localhost:8888/rest/resource/";
		} else {
			rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
		}

		//Calcolo il numero di trasmissioni selezionate
		numSelected = 0;
		counter = 0;
		jsonObj= [];
		$('#transmission :selected').each(function(i, selected) {
			numSelected++;
		});

		//invoco i servizi
		getServices(rootURL);

		return false;
	}
});



function getServices(rootURL) {
	//Per ogni trasmissione selezionata invoco il servizio che restituisce i dati da graficare
	$('#transmission :selected').each(function(i, selected) {
		$.ajax({
			type: 'GET',
			url: rootURL + "time/" + $('#monitoredEntity').val() + "/" + $(selected).val() + "/" + $('#from').val() + "/" + $('#to').val(),
			dataType: "json",
			success: function(data) {
				//Contatore che indica quante volte è stato invocato il servizio
				counter++;

				if ($('#groupingunit').val() != 'none') {
					jsonObj[i] = {
							name: $(selected).text(),
							data: buildData(data),
							dataGrouping : {
								units : [
								         [$('#groupingunit').val(),
								          [1] ]

								         ],
								         approximation : $('#approssimazione').val(),
								         forced : "true",
							}
					};

					//Se non è specificato un grouping per i dati
				} else {
					jsonObj[i] = {
							name: $(selected).text(),
							data: buildData(data),
					};
				}

				// se il numero di volte che ho invocato il servizio è pari al numero di trasmissioni selezionate allora crea il grafico
				if (counter == numSelected) {
					createChart();
				}
			}
		});
	});


}

//costruisce l'array json con le coppie data-valore
function buildData(res) {
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


function createChart() {
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
		series: jsonObj,

	});
}


function reRender() {
	if (jsonObj.lenght != 0) {
		if (numSelected != undefined && counter == numSelected) {
			if ($('#groupingunit').val() != 'none') {
				$.each(jsonObj, function(index, value) {
					jsonObj[index] = {
							name: value.name,
							data: value.data,
							dataGrouping : {
								units : [
								         [$('#groupingunit').val(),
								          [1] ]

								         ],
								         approximation : $('#approssimazione').val(),
								         forced : "true",
							},
					};
				});

			} else {
				jsonObj[index] = {
						name: value.name,
						data: value.data,
				};
			}
			createChart();
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