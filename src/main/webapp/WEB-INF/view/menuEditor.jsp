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
      <%@include file="/WEB-INF/view/chunk/message.jsp"%>
        <div class="col-1-1">
          <fmt:message key="menuEditor.form.title" bundle="${loc}"/><br/>
          <form action="controller?command=UPDATE_DISH&changeableField=DISH_NAME" method="post">
            <p><textarea required rows="3" cols="45" name="value" placeholder=<fmt:message key="menuEditor.form.placeholder.dishName" bundle="${loc}"/>>
              <c:out value="${dish.name}"/>
            </textarea></p>
            <button type="submit">
              <fmt:message key="menuEditor.button.save" bundle="${loc}"/>
            </button>
          </form>
          <form action="controller?command=UPDATE_DISH&changeableField=CATEGORY"  method="post">
            <p>
              <select reqired name="value">
                <option selected value="${dish.category}"><c:out value="${dish.category}"/></option>
                <c:forEach var="cat" items="${categories}">
                  <c:if test="${cat != dish.category}">
                    <option value="${cat}"><c:out value="${cat}"/></option>
                  </c:if>
                </c:forEach>
              </select>
              <button type="submit">
                <fmt:message key="menuEditor.button.save" bundle="${loc}"/>
              </button>
            </p>
          </form>
          <form method="post" action="controller?command=UPDATE_DISH&changeableField=DESCRIPTION">
            <p><textarea reqired rows="10" cols="45" name="value" placeholder=<fmt:message key="menuEditor.form.placeholder.description" bundle="${loc}"/>> <c:out value="${dish.description}"/></textarea></p>
            <button type="submit">
              <fmt:message key="menuEditor.button.save" bundle="${loc}"/>
            </button>
          </form>        
          <form method="post" action="controller?command=UPDATE_DISH&changeableField=PRICE">
            <p>
              <input required name="value" type="text" value="${dish.price}" placeholder=<fmt:message key="menuEditor.form.placeholder.price" bundle="${loc}"/>>
              <button type="submit">
                <fmt:message key="menuEditor.button.save" bundle="${loc}"/>
              </button>
            </p>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
