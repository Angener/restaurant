<c:if test="${messages != null}">
	<c:forEach var="message" items="${messages}">
		<fmt:message key="${message}" bundle="${loc}"/>
	</c:forEach>
</c:if>