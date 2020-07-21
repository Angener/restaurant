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
					<div class="col-1-1"> 
						<form action="controller?command=CHANGE_ORDER_STATUS&changeableStatus=COMPLETED" method="post" >
							<button type="submit">
								<fmt:message key="orderCard.button.pay" bundle="${loc}"/>
							</button>
						</form>        
					</div>
				</div>
				<div class="row">
					<div class="col-1-3"></div>
					<h1><fmt:message key="bill.bill" bundle="${loc}"/></h1>
					<div class="col-2-3">
						<c:if test="${messages != null}">
							<c:forEach var="message" items="${messages}">
								<fmt:message key="${message}" bundle="${loc}"/>
							</c:forEach>
						</c:if>
						<table>
							<tr>
								<td>
									<fmt:message key="bill.table.order" bundle="${loc}"/>
								</td>
								<td>
									<c:out value="${order.id}"/>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="bill.table.date" bundle="${loc}"/>
								</td>
								<td>
									<fmt:formatDate value="${order.orderDate}" type="both" dateStyle="long" /> 
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="bill.table.user" bundle="${loc}"/>
								</td>
								<td>
									<c:out value="${user.username}"/>
								</td>
							</tr>   
						</table>
						<c:set var="position" value="0"/>
						<table>
							<tr>
								<th><fmt:message key="bill.table.position" bundle="${loc}"/></th>
								<th><fmt:message key="bill.table.dishName" bundle="${loc}"/></th>
								<th><fmt:message key="bill.table.price" bundle="${loc}"/></th>
								<th><fmt:message key="bill.table.quantity" bundle="${loc}"/></th>
								<th><fmt:message key="bill.table.amount" bundle="${loc}"/></th>
							</tr>
							<c:forEach var="dish" items="${order.dishes}">
								<c:set var="position" value="${position + 1}"/>
								<tr>
									<td><p><c:out value="${position}"/></p></td>
									<td><p><c:out value="${dish.name}"/></p></td>
									<td><p class="price" align="right"><c:out value="${dish.price}"/></p></td>
									<td><p align="center"><c:out value="${dish.quantity}"/></p></td>
									<td><p class="price" align="right"><c:out value="${dish.amount}"/></p></td>
								</tr>
							</c:forEach>
							<tr>
								<td><td><td></td></td></td>
								<td><strong><p><fmt:message key="bill.table.totalAmount" bundle="${loc}"/></p></strong></td>
								<td><strong><p class="price"><c:out value="${order.totalAmount}"/></p></strong></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
