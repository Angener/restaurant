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
            <%@include file="/WEB-INF/view/chunk/message.jsp"%>
            <table>
              <c:if test="${report.orders.size() > 0}">
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
                  <td><p align="center"> <fmt:formatDate value="${order.orderDate}" type="both" dateStyle="long"/></p></td>
                  <td><p align="center"><c:out value="${order.totalAmount}"/></p></td>
                  <td><p align="center"><fmt:message key="${order.status.status}" bundle="${loc}"/></p></td>
                  <td>
                    <form action="controller?command=GET_ORDER&orderId=${order.id}&form=ORDER_CARD" method="post" >
                      <button type="submit">
                        <fmt:message key="orders.table.button.view" bundle="${loc}"/>
                      </button>
                    </form> 
                  </td>
                </tr>
              </c:forEach>
              </table>
              <%-- PAGINATION  --%>
              <c:if test="${quantityOfOrderPages > 1}">
                <c:if test="${currentReportPage != 1}">
                  <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${currentReportPage - 1}">Previous</a></td>
                </c:if> 
                <table border="1" cellpadding="5" cellspacing="5">
                  <tr>
                  <c:forEach begin="1" end="${quantityOfOrderPages}" var="i">
                    <c:choose>
                      <c:when test="${currentReportPage eq i}">
                        <td>${i}</td>
                      </c:when>
                      <c:otherwise>
                        <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${i}">${i}</a></td>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                  </tr>
                </table>
            <c:if test="${currentReportPage lt quantityOfOrderPages}">
            <td><a href="controller?command=GET_REPORT&reportType=${reportType}&page=${currentReportPage + 1}">Next</a></td>
            </c:if>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>