<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>WikiList | Login Page</title>
</head>
<body>
	<div>
	<form id="loginForm" action="j_spring_security_check" method="post">
		Username: <input type="text" name="j_username"/>
		Password: <input type="password" name="j_password"/>
		<button type="submit">Login</button>
	</form>
	</div>
</body>
</html>