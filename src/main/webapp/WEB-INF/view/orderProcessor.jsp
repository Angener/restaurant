<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/processor.tld" prefix="processor"%>

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
    <div class="row">
  <div class="col-1-1"> 


<processor:process key="${key}" order="${orderDto}" dishId="${dishId}">
<c:remove var="orderDto" scope="session"/>
<c:redirect url="/menu"/>
</processor:process>



<table>
  <tr>
    <th><fmt:message key="orderProcessor.table.name" bundle="${loc}"/></th>
    <th><fmt:message key="orderProcessor.table.price" bundle="${loc}"/></th>
    <th><fmt:message key="orderProcessor.table.quantity" bundle="${loc}"/></th>
    <th><fmt:message key="orderProcessor.table.amount" bundle="${loc}"/></th>
  </tr>
  <c:forEach var="preOrder" items="${orderDto.dishes}">

      <tr>
        <td><p><c:out value="${preOrder.name}"/></p></td>
        <td><p class="price" align="center"><c:out value="${preOrder.price}"/></p></td>
        <td><p class="price" align="center"><c:out value="${preOrder.quantity}"/></p></td>
        <td><p class="price" align="center"><c:out value="${preOrder.amount}"/></p></td>
        <td>
          <form action="controller?command=USE_TLD_TAG&key=deleteDish&dishId=${preOrder.id}&form=ORDER_PROCESSOR" method="post" >



            <button type="submit">
              <fmt:message key="orderProcessor.button.delete" bundle="${loc}"/>
            </button>
          </form> 
        </td>
      </li>
  </c:forEach>
  <tr>
    <td><strong><p><fmt:message key="orderProcessor.totalAmount" bundle="${loc}"/></p></strong></td>
    <td><strong><p class="price"><c:out value="${orderDto.totalAmount}"/></p></strong></td>
  </tr>
  </table>
  <form action="controller?command=CREATE_ORDER" method="post" >
      <button type="submit">
          <fmt:message key="orderProcessor.button.checkout" bundle="${loc}"/>
      </button>
  </form>
  <form action="/menu" method="post" >
      <button type="submit">
          <fmt:message key="orderProcessor.button.menu" bundle="${loc}"/>
      </button>
  </form>
  <form action="controller?command=USE_TLD_TAG&key=cancelOrder&form=ORDER_PROCESSOR" method="post">   
      <button type="submit">
          <fmt:message key="orderProcessor.button.cancel" bundle="${loc}"/>
      </button>
  </form>

    










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


  