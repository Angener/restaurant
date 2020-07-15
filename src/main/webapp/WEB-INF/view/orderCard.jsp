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
      <div class="row">
      <div class="col-1-1"> 

        <c:if test="${role eq 'customer'}">

          <c:choose>
          <c:when test = "${order.isApproved() eq false}">
            <form action="controller?command=CANCEL_ORDER&orderId=${order.id}" method="post" >
            <button type="submit">
              <fmt:message key="orderCard.button.cancel" bundle="${loc}"/>
            </button>
            </form>
          </c:when>
          <c:when test = "${order.isBilled() eq true && order.isPaid() eq false}">
            <form action="controller?command=GET_CUSTOMER_FORM&form=BILL" method="post" >
            <button type="submit">
              <fmt:message key="orderCard.button.pay" bundle="${loc}"/>
            </button>
            </form>
          </c:when>
        </c:choose>

        </c:if>


      
      <c:if test="${role eq 'admin'}">
      <c:choose>
      <c:when test = "${order.isApproved() eq false}">
      <form action="controller?command=CHANGE_ORDER_STATUS&changeableStatus=APPROVED" method="post" >
          <button type="submit">
            <fmt:message key="orderCard.button.approve" bundle="${loc}"/>
          </button>
        </form>
      </c:when>
      <c:when test = "${order.isApproved() eq true && order.isPassed() eq false}">
      <form action="controller?command=CHANGE_ORDER_STATUS&changeableStatus=PROCESSING" method="post" >
          <button type="submit">
            <fmt:message key="orderCard.button.pass" bundle="${loc}"/>
          </button>
        </form>
      </c:when>
      <c:when test = "${order.isPassed() eq true && order.isCooked() eq false}">
      <form action="controller?command=CHANGE_ORDER_STATUS&changeableStatus=COOKED" method="post" >
          <button type="submit">
            <fmt:message key="orderCard.button.cook" bundle="${loc}"/>
          </button>
        </form>
      </c:when>
      <c:when test = "${order.isCooked() eq true && order.isBilled() eq false}">
      <form action="controller?command=CHANGE_ORDER_STATUS&changeableStatus=PENDING_PAYMENT" method="post" >
          <button type="submit">
            <fmt:message key="orderCard.button.bill" bundle="${loc}"/>
          </button>
        </form>
      </c:when>
    </c:choose>
      </c:if>
</div>


    
    </div>


    <div class="row">
      <div class="col-1-3">
    </div>



  

 

  <div class="col-2-3">
     <c:out value="${message}"/>
     <table>
       <tr>
        <td>
          <fmt:message key="orderCard.table.orderId" bundle="${loc}"/>
        </td>
        <td>
          <c:out value="${order.id}"/>
        </td>
       </tr>
        <tr>
        <td>
          <fmt:message key="orderCard.table.date" bundle="${loc}"/>
        </td>
        <td>
          <fmt:formatDate value="${order.orderDate}" type="both" dateStyle="long" /> 
        </td>
       </tr>
       <tr>
        <td>
          <fmt:message key="orderCard.table.amount" bundle="${loc}"/>
        </td>
        <td>
          <c:out value="${order.totalAmount}"/>
        </td>
       </tr>
        <tr>
        <td>
          <fmt:message key="orderCard.table.status" bundle="${loc}"/>
        </td>
        <td>
          <fmt:message key="${order.status.status}" bundle="${loc}"/>
        </td>
       </tr>
     </table>




<c:set var="position" value="0"/>
<table>
  <tr>
    <th><fmt:message key="orderCard.table.position" bundle="${loc}"/></th>
    <th><fmt:message key="orderCard.table.name" bundle="${loc}"/></th>
    <th><fmt:message key="orderCard.table.quantity" bundle="${loc}"/></th>
  </tr>
  <c:forEach var="dish" items="${order.dishes}">
  <c:set var="position" value="${position + 1}"/>
      <tr>
        <td><p><c:out value="${position}"/></p></td>
        <td><p><c:out value="${dish.name}"/></p></td>
        <td><p class="price" align="center"><c:out value="${dish.quantity}"/></p></td>
      </tr>
  </c:forEach>
  </table>





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