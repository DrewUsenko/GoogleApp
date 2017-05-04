<%-- 
    Document   : ErrorPage
    Created on : Sep 23, 2011, 12:02:21 PM
    Author     : Admin
--%>
<%@ page isErrorPage="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error JSP Page</title>
    </head>
    <body>
        <table>
            <TR>
                <TD>Your request to the following URI: ${pageContext.errorData.requestURI} 
                    has failed. </TD>
            </TR>  
            <TR>
                <TD>Status code: ${pageContext.errorData.statusCode}</TD>
            </TR>
            <TR>
                <TD>Exception: ${pageContext.errorData.throwable}</TD>
            </TR>
            <TR>
                <TD>Message: ${pageContext.errorData.throwable.message}</TD>
            </TR>
            <TR> 
                <c:forEach items="${pageContext.errorData.throwable.stackTrace}" var="elem">
                    <td> <c:out value="${elem}" /> </td>
                </c:forEach>
           
            </TR>
        </table>
    </body>
</html>
