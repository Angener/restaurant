<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html>
  <%@ include file="/WEB-INF/view/chunk/head.jsp"%>
  <body>
    <%@ include file="/WEB-INF/view/chunk/header.jsp"%>
    <div class="main">
      <div class="container">
        <div class="col-1-1">
          <form name="anyName" action="controller?command=ADD_DISH" method="post" >
            <fmt:message key="menuRegister.title" bundle="${loc}"/><br/>
            <p><textarea required type="text" name="name" placeholder=<fmt:message key="menuRegister.dishName" bundle="${loc}"/>></textarea></p>
            <p><textarea required type="text" name="category" placeholder=<fmt:message key="menuRegister.dishCategory" bundle="${loc}"/>></textarea></p>
            <p><textarea required type="text" name="description" placeholder=<fmt:message key="menuRegister.dishDescription" bundle="${loc}"/>></textarea></p>
            <p><input required type="text" name="price" placeholder=<fmt:message key="menuRegister.dishPrice" bundle="${loc}"/>></p>
            <input type="submit" value=<fmt:message key="menuRegister.button.add" bundle="${loc}"/>>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
