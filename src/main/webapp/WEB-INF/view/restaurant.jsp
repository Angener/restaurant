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
    <a><strong><fmt:message key="main.siteName" bundle="${loc}"/></strong></a>
    <nav>
      <ul>
        <li><a href="">Главная</a></li>
        <li><a href="">О нас</a></li>
        <li><a href="">Контакты</a></li>
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








	<c:choose>
		<c:when test="${user != null}">
		<form action="controller?command=CHANGE_LOCALE" method="post">
            <button type="submit" name="locale" value="ru">
            <image src="${request.getContextPath}/resources/image/RU.png">
            </button>
          </form>

		</c:when>
		<c:otherwise>
		<form action="controller?command=CHANGE_LOCALE" method="post">
            <button type="submit" name="locale" value="ru">
            <image src="${request.getContextPath}/resources/image/RU.png">
            </button>
              <button type="submit" name="locale" value="en">
               <image src="${request.getContextPath}/resources/image/US.png">
              </button>
           </form>
		</c:otherwise>



	</c:choose> 


	






</body>
</html>