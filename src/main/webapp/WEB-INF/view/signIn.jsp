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
        <div class="col-1-2">       
          <form name="anyName" action="controller?command=SIGN_IN" method="post" >
            <fmt:message key="signIn.signInMessage" bundle="${loc}"/><br/>
            <input required type="text" name="Username" placeholder=<fmt:message key="signIn.form.username" bundle="${loc}"/>>
            <input required type="password" name="Password" placeholder=<fmt:message key="signIn.form.password" bundle="${loc}"/>>
            <input type="submit" value=<fmt:message key="signIn.signInMessage" bundle="${loc}"/>>
          </form>
        </div>
        <div class="col-1-2">
          <form name="anyName" action="controller?command=SIGN_UP" method="post" >
            <fmt:message key="signIn.signUpMessage" bundle="${loc}"/><br/>
            <input required type="text" name="Username" placeholder=<fmt:message key="signIn.form.username" bundle="${loc}"/>>
            <input required type="password" name="Password" placeholder=<fmt:message key="signIn.form.password" bundle="${loc}"/>>
            <input required type="text" name="Email" placeholder=<fmt:message key="signIn.form.email" bundle="${loc}"/>>
            <input required type="text" name="Phone" placeholder=<fmt:message key="signIn.form.mobile" bundle="${loc}"/>>
            <input type="submit" value=<fmt:message key="signIn.signUpMessage" bundle="${loc}"/>>
          </form>
        </div>
        <%@include file="/WEB-INF/view/chunk/message.jsp"%>
      </div>
    </div>
  </body>
</html>