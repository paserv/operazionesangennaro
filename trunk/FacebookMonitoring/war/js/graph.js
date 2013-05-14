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
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: function(data) {
            renderList(data);
        }
	});
}


function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#searchList li').remove();
	$.each(list, function(index, data) {
		$('#searchList').append('<li>' + data.ballaro.likeCount +'</li>');
	});
}