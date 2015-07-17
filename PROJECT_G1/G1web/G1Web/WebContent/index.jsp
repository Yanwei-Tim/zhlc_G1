<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Spring mvc</title>
</head>
<body>
<h1>用户登录</h1>
	<form action="user/login.html">
		账户：<input type="text" name="userName" value="" /><br/>
		密码：<input type="password" name="passWord" value="" /><br/><br/>
		<input type="submit" value="登录" />   
	</form>
</body>
</html>