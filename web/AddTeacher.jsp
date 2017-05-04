<%-- 
    Document   : AddTeacher
    Created on : Sep 28, 2015, 1:35:01 PM
    Author     : Andrey
--%>
<%@page import="slGal.LiveEdu.TeacherConst"%>
<%@ page errorPage="WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- --%>
<c:set var="pd" value="<%= request.getParameter(TeacherConst.PARAMETER_DEPARTMENT)%>" />
<c:set var="pf" value="<%= request.getParameter(TeacherConst.PARAMETER_FACULTY)%>" />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Новый преподаватель</title>
    </head>
    <body>
        <form action="TeacherServletControler" method="POST">
            <input type="hidden" name="action" value="${teacherConst.ACTION_FILTER}" >    
            <div id="filter">
                <table>
                    <tr>
                        <td>Имя</td>
                        <td><input type="text" name="firstName" value="" /></td>
                    </tr>
                    <tr>
                    <td>Фамилия</td>
                        <td><input type="text" name="lastName" value="" /></td>
                    </tr>
                    <tr>
                        <td>Отчество</td>
                        <td><input type="text" name="patronymic" value="" /></td>
                    </tr>
                    <tr>
                        <td>Пароль</td>
                        <td><input type="text" name="password" value="" /></td>
                    </tr>
                    <tr>
                        <td>Факультет</td>
                        <td>
                            <select name="${teacherConst.PARAMETER_FACULTY}" style="width: 100px;">
                                <option selected></option>
                                <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">
<!--                                    <option>${facult}</option>-->
                                    <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Кафедра</td>
                        <td>
                            <select name="${teacherConst.PARAMETER_DEPARTMENT}" style="width: 100px;">
                                <option selected></option>
                                <c:forEach var="department" items="${requestScope.ATTRIBUTE_DEPARTMENT_LIST}">
                                    <option<c:if test="${department eq pd}"> selected</c:if>>${department}</option>                                    
                                </c:forEach>
                            </select>
                        </td>
                    </tr>                   
                </table>
            </div>
        </form> 
    </body>
</html>
