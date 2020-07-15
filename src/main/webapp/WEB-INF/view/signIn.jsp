<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <fmt:setLocale value="${locale}" scope="session" />
  <fmt:setBundle basename="locale" var="loc" />
  <link rel="stylesheet" href="${request.getContextPath}/resources/css/style.css">
  <title>
    <fmt:message key="main.siteName" bundle="${loc}"/>
  </title>  
</head>
<body>
   <header>
      <div class="container">
      <a href="/" class="logo"><fmt:message key="main.siteName" bundle="${loc}"/></a>
        <nav>
            <ul>
              <li><a href="/restaurant"><fmt:message key="main.homeButton" bundle="${loc}"/></a></li>
              <li><a href="controller?command=GET_MENU&category=All"><fmt:message key="main.menu" bundle="${loc}"/></a></li>
              <c:choose>
                <c:when test="${role eq 'admin'}">
                  <li><a href="/orders"><fmt:message key="main.orders" bundle="${loc}"/></a></li>
                </c:when>
                <c:when test="${role eq 'customer' && report.orders.size() != 0}">
                  <li><a href="controller?command=GET_REPORT&reportType=actual_user_orders"><fmt:message key="main.orders" bundle="${loc}"/></a></li>
                </c:when>
              </c:choose>
              <li>
                <c:choose>
                <c:when test="${user != null}">
                  <li><a href="controller?command=SIGN_OUT"><fmt:message key="main.signOut" bundle="${loc}"/></a></li>
                </c:when>
                <c:otherwise>
                  <a href="/authorization"><fmt:message key="main.signIn" bundle="${loc}"/></a>
                </c:otherwise>
              </c:choose>
              </li>
              <li>
                <form action="controller?command=CHANGE_LOCALE" method="post">
                  <input type="hidden" name="path" value="${getContextPath}">
                    <button type="submit" name="locale" value="ru">
                      <image src="${request.getContextPath}/resources/image/RU.png">
                    </button>
                    <button type="submit" name="locale" value="en">
                      <image src="${request.getContextPath}/resources/image/US.png">
                    </button>
                </form>
              </li>
            </ul>
        </nav>
      </div>
  </header>



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
      <c:out value="${message}"/>
    </div>
  </div>
</body>
</html>