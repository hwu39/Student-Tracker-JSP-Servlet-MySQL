<%@ page import="java.util.*, com.web.jdbc.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<title>Student Tracker</title>
<link rel="stylesheet" type="text/css" href="META-INF/css/styles.css">
</head>

<body>

	<%
		List<Student> studentList = (List<Student>) request.getAttribute("student_list");
	%>
	<div id="wrapper">
		<div id="header">
			<h2>UMBC</h2>
		</div>
	</div>

	<div id="container">
		<div id="content">

			<!-- add button  -->
			<input type="button" value="Add Student"
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button">

			<!-- add search form -->
			<form action="StudentControllerServlet" method="GET">
				<input type="hidden" name="command" value="SEARCH" />
				Search student: <input type="text" name="searchName" /> 
				<input type="submit" value="Search" class="add-student-button" />
			</form>

			<table border="1">
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>

				<c:forEach var="element" items="${ student_list }">
					<!-- set up link for each student -->
					<c:url var="link" value="StudentControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="studentID" value="${element.id }" />
					</c:url>

					<!-- set up link for deleting a student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="studentID" value="${element.id }" />
					</c:url>

					<tr>
						<td>${ element.firstName }</td>
						<td>${ element.lastName }</td>
						<td>${ element.email }</td>
						<td><a href="${link}">Update</a> | <a href="${deleteLink}"
							onclick="if (!(confirm('Confirm Delete:'))) return false">Delete</a>
						</td>
					</tr>
				</c:forEach>

			</table>
		</div>
	</div>
</body>

</html>