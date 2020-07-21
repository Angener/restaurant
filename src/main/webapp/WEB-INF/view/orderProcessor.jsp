<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/processor.tld" prefix="processor"%>


<!DOCTYPE html>
<html>
  <%@ include file="/WEB-INF/view/chunk/head.jsp"%>
  <body>
    <%@ include file="/WEB-INF/view/chunk/header.jsp"%>
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
                </tr>
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
      </div>
    </div>
  </body>
</html>


  