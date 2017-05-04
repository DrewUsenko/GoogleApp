<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="slGal.LiveEdu.StudentServletControler" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<c:set var="csvImport" value="<%= request.getAttribute(StudentServletControler.ATTRIBUTE_CSV_IMPORT)%>" />    
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CSV Import User</title>
    </head>
    <body>
        <c:forEach var="line" items="${csvImport}"><c:out value="${line}" /></c:forEach>
    </body>
</html>
