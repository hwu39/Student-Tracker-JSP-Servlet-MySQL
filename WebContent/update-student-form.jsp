<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Update Student</title>
	<link rel="stylesheet" type="text/css" href="META-INF/css/styles.css">
	<link rel="stylesheet" type="text/css" href="META-INF/css/add-student-style.css">
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>UMBC</h2>
		</div>
	</div>
	
	<div id="container">
		<h2>Update Student</h2>
		
		<form action="StudentControllerServlet" method="GET">
			<input type="hidden" name="command" value="UPDATE"> 
			
			<input type="hidden" name="studentID" value="${the_student.id }"> 
			
			<table>
				<tbody>
					<tr>
						<td><label>First Name:</label></td>
						<td><input type="text" name="firstname" value="${the_student.firstName }"></td>
					</tr>
					
					<tr>
						<td><label>Last Name:</label></td>
						<td><input type="text" name="lastname" value="${the_student.lastName }"></td>
					</tr>
					
					<tr>
						<td><label>Email:</label></td>
						<td><input type="text" name="email" value="${the_student.email }"></td>
					</tr>
					
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="SAVE" class="save"></td>
					</tr>
				</tbody>
			
			</table>
		</form>
	
		<div> 
			<a href="StudentControllerServlet">Back</a>
		</div>
	</div>

</body>

</html>