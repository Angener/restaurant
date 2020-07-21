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
						<%@include file="/WEB-INF/view/chunk/message.jsp"%>   
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
											<form action="controller?command=EDIT_MENU&form=MENU_EDITOR&dishId=${dish.id}" method="post">							
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
							<c:if test="${currentMenuPage != 1}">
								<td><a href="controller?command=GET_MENU&category=${category}&page=${currentMenuPage - 1}">Previous</a></td>
							</c:if>
							<table border="1" cellpadding="5" cellspacing="5">
								<tr>
									<c:forEach begin="1" end="${quantityOfPages}" var="i">
										<c:choose>
											<c:when test="${currentMenuPage eq i}">
												<td>${i}</td>
											</c:when>
											<c:otherwise>
												<td><a href="controller?command=GET_MENU&category=${category}&page=${i}">${i}</a></td>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</tr>
							</table>
							<c:if test="${currentMenuPage lt quantityOfPages}">
								<td><a href="controller?command=GET_MENU&category=${category}&page=${currentMenuPage + 1}">Next</a></td>
							</c:if>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
