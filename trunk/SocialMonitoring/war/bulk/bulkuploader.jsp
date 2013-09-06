<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Bulk Uploader CSV to DataStore</title>
</head>

<body>
	<h1 align="center">Bulk Uploader CSV to DataStore</h1>


	<form name="bulk" action="bulkupload" method="post">
		<table width="80%" align="center">
			<tr>
				<td style="font-weight: bold;">Paste CSV</td>
			</tr>
			<tr>
				<td colspan="8"><textarea name="data"
						style="width: 100%; height: 400px"></textarea></td>
			</tr>
			<tr>
				<td><br></td>
			</tr>
			<tr>
				<td style="font-weight: bold;">Set delimiter:</td>
				<td style="font-weight: bold;"><input type="text"
					name="delimiter"><br></td>
				<td style="font-weight: bold;">Set Table Group:</td>
				<td style="font-weight: bold;"><input type="text" name="group"><br></td>
				<td style="font-weight: bold;">Set Entity:</td>
				<td style="font-weight: bold;"><input type="text" name="entity"><br></td>
				<td style="font-weight: bold;"><br></td>
				<td style="font-weight: bold;"><input type="submit"
					value="Upload to DS"><br></td>

			</tr>
		</table>
	</form>
	
	<br>
	<br>
	<br>
	
	<form method="post" action="data">
		<table width="80%" align="center">
	    Tabella
	    <input type="text" id="tabella" name="tabella" />
	    
	    Operazione
	    <select id="operazione" name="operazione" >
	  		<option value="select">Select</option>
	  		<option value="delete">Delete</option>
	  		<option value="test">Test</option>
	 	</select>
	    
	    <button id="getData">Get Data</button>
	    </table>
	</form>
</body>
</html>
