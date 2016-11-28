<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Patient</title>
<style>
table{
border:1px solid #000;
}
table tr th, table tr td{
text-aling:left;
padding:4px;
border:1px solid #ACACAC;
}
body{
padding:10px;
}
</style>
</head>
<body>
<ul>
<li>
	Go to <a href="/Hospital/createPatient.html">Create Patient</a>	
</li>
<li>
	Go to <a href="/Hospital/PatientManagement">Search Patient Details</a>.
</li>
</ul>
<form action="/Hospital/PatientManagement" method="get">
<br><br>
Patient Name:<input type="text" name="searchName" value="${searchName}" /> <button type="Submit">Search</button>
<br><br>
</form>
<c:if test="${patientList != null}">
	<table>
		<tr>
			<th>P.ID</th>
			<th>Full Name</th>
			<th>Age</th>
			<th>DOB</th>
			<th>Gender</th>
			<th>Phone</th>
			<th>Info</th>
		</tr>
		
	<c:forEach items="${patientList}" var="patient">
		<tr>
			<td>${patient.recNum}</td>
			<td>${patient.fname} ${patient.lname}</td>
			<td>${patient.age}</td>
			<td>${patient.dob}</td>
			<td>${patient.gender}</td>
			<td>${patient.phone}</td>
			<td>${patient.text}</td>
		</tr>

		
	</c:forEach>
	</table>
</c:if>
</body>
</html>