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
        <tr>
          <c:if test = "${role == 'admin'}">
            <form action="controller?command=GET_REPORT&reportType=incomplete_orders" method="post" >
              <button type="submit">
                <fmt:message key="orders.report.type.incomplete" bundle="${loc}"/>
              </button>
            </form>
        <form action="controller?command=GET_REPORT&reportType=unapproved_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.unapproved" bundle="${loc}"/>
            </button>
        </form>
        <form action="controller?command=GET_REPORT&reportType=unsent_to_kitchen_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.unsentToKitchen" bundle="${loc}"/>
            </button>
        </form>
         <form action="controller?command=GET_REPORT&reportType=uncooked_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.uncooked" bundle="${loc}"/>
            </button>
        </form>
        <form action="controller?command=GET_REPORT&reportType=not_billed_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.notBilled" bundle="${loc}"/>
            </button>
        </form>
        <form action="controller?command=GET_REPORT&reportType=completed_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.completed" bundle="${loc}"/>
            </button>
        </form>
        <form action="controller?command=GET_REPORT&reportType=all_orders" method="post" >
          <button type="submit">
            <fmt:message key="orders.report.type.all" bundle="${loc}"/>
            </button>
        </form>    
    </c:if>
    </tr>
    </div>


  <div class="col-2-3">
     <c:out value="${message}"/>

<table>
  <c:if test="${report.orders.size() != 0}">
    <tr>
      <th><fmt:message key="orders.table.column.orderId" bundle="${loc}"/></th>
      <th><fmt:message key="orders.table.column.date" bundle="${loc}"/></th>
      <th><fmt:message key="orders.table.column.totalAmount" bundle="${loc}"/></th>
      <th><fmt:message key="orders.table.column.status" bundle="${loc}"/></th>
    </tr>
  </c:if>
  <c:forEach var="order" items="${report.orders}">
      <tr>
        <td><p align="center"><c:out value="${order.id}"/></p></td>
        <td><p align="center"><c:out value="${order.orderDate}"/></p></td>
        <td><p align="center"><c:out value="${order.totalAmount}"/></p></td>
        <td><p align="center"><fmt:message key="${order.status.status}" bundle="${loc}"/></p></td>
        <td>
          <form action="" method="post" >
            <button type="submit">
              <fmt:message key="orders.table.button.view" bundle="${loc}"/>
            </button>
          </form> 
        </td>
      </li>
    </tr>
  </c:forEach>
</table>



<%-- PAGINATION  --%>
 <c:if test="${quantityOfOrderPages > 1}">
  <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${currentPage - 1}">Previous</a></td>
    </c:if>
 
    <%--For displaying Page numbers. 
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${quantityOfOrderPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt quantityOfOrderPages}">
        <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${currentPage + 1}">Next</a></td>
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