<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style><%@include file="/WEB-INF/css/Login.css"%></style>
</head>
<body>
<h2 class="h2style">Login Form</h2>
<form class="formstyling" action="/home">
<input placeholder="Name" class="inputstyling" type="text" name="name"/>
<input placeholder="Email" class="inputstyling" type="text" name="email"/>
<button type="submit">Login<button/>
</form>
</body>
</html>