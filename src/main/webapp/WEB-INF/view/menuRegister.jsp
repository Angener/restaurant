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
      <div class="col-1-1">
        <form name="anyName" action="controller?command=ADD_DISH" method="post" >
          <fmt:message key="menuRegister.title" bundle="${loc}"/><br/>
          <p><textarea required type="text" name="name" placeholder=<fmt:message key="menuRegister.dishName" bundle="${loc}"/>></textarea></p>
          <p><textarea required type="text" name="category" placeholder=<fmt:message key="menuRegister.dishCategory" bundle="${loc}"/>></textarea></p>
          <p><textarea required type="text" name="description" placeholder=<fmt:message key="menuRegister.dishDescription" bundle="${loc}"/>></textarea></p>
          <p><input required type="text" name="price" placeholder=<fmt:message key="menuRegister.dishPrice" bundle="${loc}"/>></p>
          <input type="submit" value=<fmt:message key="menuRegister.button.add" bundle="${loc}"/>>
    	</div>
        <c:out value="${error}"/>
  </div>
</div>
</body>
</html>





