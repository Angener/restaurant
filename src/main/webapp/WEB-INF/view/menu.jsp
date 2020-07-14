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
                <c:when test="${role eq 'customer' && report.orders != null}">
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
    <div class="row">
      <div class="col-1-3">
      	<form action="controller?command=GET_MENU&category=All" method="post" >
      		<button type="submit">
            <fmt:message key="menu.all" bundle="${loc}"/>
            </button>
     	</form>
     	<form action="controller?command=GET_MENU&category=Cakes" method="post" >
      		<button type="submit">
            <fmt:message key="menu.cakes" bundle="${loc}"/>
            </button>
     	</form>
     	<form action="controller?command=GET_MENU&category=Sweets" method="post" >
      		<button type="submit">
            <fmt:message key="menu.sweets" bundle="${loc}"/>
            </button>
     	</form>
     	<form action="controller?command=GET_MENU&category=Cookies" method="post" >
      		<button type="submit">
            <fmt:message key="menu.cookies" bundle="${loc}"/>
            </button>
     	</form>
     	<c:if test= "${role == 'admin'}">
      	<form action="controller?command=GET_ADMIN_FORM&form=MENU_REGISTER" method="post" >
      		<button type="submit">
            <fmt:message key="menu.add" bundle="${loc}"/>
            </button>
     	</form>
	     </c:if>
    	</div>



	<div class="col-2-3">
		<c:out value="${message}"/>

     
<table>
	<c:forEach var="dish" items="${menu.dishes}">
			<tr>
				<td>
					<div class="card">
						<c:forEach var="pic" items="${dish.images}">
  							<p><img src="${pic}" alt="${dish.name}" style="width:30%; margin-right: 10px" align="left"></p>
  						</c:forEach>
  						<h1><c:out value="${dish.name}"/></h1>
						<p class="price"><c:out value="${dish.price}"/></p>
  						<c:out value="${dish.description}"/>
  						<c:choose>
    						<c:when test="${role == 'admin'}">
    							<form action="controller?command=GET_MENU&category=All" method="post">
      								<button type="submit">
           								<fmt:message key="menu.button.changeMenu" bundle="${loc}"/>
           							</button>
     							</form>
    						</c:when>
    						<c:when test="${role == 'customer'}">
    							<form action="controller?command=ADD_TO_ORDER" method="post">
    								<input type="hidden" name="dishId" value="${dish.id}">
    								<input type="hidden" name="dishName" value="${dish.name}">
    								<input type="hidden" name="dishQuantity" value="1">
    								<input type="hidden" name="dishPrice" value="${dish.price}">
      								<button type="submit">
           								<fmt:message key="menu.button.addToOrder" bundle="${loc}"/>
           							</button>
     							</form>


     							<c:if test= "${orderDto != null}">
      								<form action="controller?command=GET_CUSTOMER_FORM&form=ORDER_PROCESSOR" method="post" >
      									<button type="submit">
           									<fmt:message key="menu.button.processOrder" bundle="${loc}"/>
           								</button>
     								</form>
	    						</c:if>


    						</c:when>
    						<c:otherwise>
     							<form action="/authorization" method="post" >
      								<button type="submit">
           								<fmt:message key="menu.button.addToOrder" bundle="${loc}"/>
           							</button>
     							</form>
    						</c:otherwise>
  						</c:choose>
  					</div>
				</td>
			</tr>
	</c:forEach>
</table>


<%-- PAGINATION  --%>
 <c:if test="${quantityOfPages > 1}">
	<%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="controller?command=GET_MENU&category=${category}&page=${currentPage - 1}">Previous</a></td>
    </c:if>
 
    <%--For displaying Page numbers. 
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${quantityOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="controller?command=GET_MENU&category=${category}&page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>
     
    <%--For displaying Next link --%>
    <c:if test="${currentPage lt quantityOfPages}">
        <td><a href="controller?command=GET_MENU&category=${category}&page=${currentPage + 1}">Next</a></td>
    </c:if>
</c:if>


























      </div>
    </div>
    <div class="row">
      <div class="col-1-2"> </div>
      <div class="col-1-2"></div>
    </div>
    <div class="row">
      <div class="col-1-4"></div>
      <div class="col-1-4"></div>
      <div class="col-1-2"></div>
    </div>
  </div>
</div>






</body>
</html>





