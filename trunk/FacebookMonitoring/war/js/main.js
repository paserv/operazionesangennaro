// The root URL for the RESTful services
var rootURL = "http://localhost:8888/rest/resource/ballaro";

//Register listeners
$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

//Trigger search when pressing 'Return' on search key input field
$('#searchKey').keypress(function(e){
	if(e.which == 13) {
		search($('#searchKey').val());
		e.preventDefault();
		return false;
    }
});

function search(searchKey) {
	if (searchKey == '')
		findAll();
	else
		findByName(searchKey);
}

function findAll() {
	alert('findAll');
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: renderlist
	});
}


function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	alert('renderList');
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#searchList li').remove();
	$.each(list, function(index, data) {
		alert(data);
		$('#searchList').append('<li><a href="#" data-identity="' + data.id + '">'+data.name+'</a></li>');
	});
}