<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<html>
<head>

</head>

<body>
<form action="Controller" method="post">
<input type="hidden" name="local" value="ru" />
<input type="submit" value=<fmt:message key="local.button.ru" bundle="${loc}"/>><br />
</form>
<form action="Controller" method="post">
<input type="hidden" name="local" value="en" />
<input type="submit" value=<fmt:message key="local.button.en" bundle="${loc}"/>><br/>

</form>
</body>
</html>