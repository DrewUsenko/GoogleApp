<%@ page contentType="text/csv; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="slGal.LiveEdu.StudentServletControler" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<c:set var="csvImport" value="<%= request.getAttribute(StudentServletControler.ATTRIBUTE_CSV_IMPORT)%>" />
<c:forEach var="line" items="${csvImport}"><c:out value="${line}" />
</c:forEach>