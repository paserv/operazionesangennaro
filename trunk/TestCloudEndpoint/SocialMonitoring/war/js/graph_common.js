// The root URL for the RESTful services
//var rootURL = "http://localhost:8888/rest/resource/";
//var rootURL = "http://01-monitorfacebookpages.appspot.com/rest/resource/";
var rootURL;
//Register listeners
$('#btnSearch').click(function() {
	
	if ($('#container').highcharts() != null) {
		$('#container').highcharts().destroy();
	}
	if ($('#local').val() == 'local') {
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


