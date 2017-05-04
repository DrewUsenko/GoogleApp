<%-- 
    Document   : validate
    Created on : Dec 30, 2015, 9:59:39 PM
    Author     : Andrey
--%>

<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <%-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> --%>
        <!-- Подключаем jQuery UI CSS -->
        <%--<jsp:include page="/css/jquery-ui-1.8.16.custom.css"/> --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui-1.8.16.custom.css" type="text/css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/google_app_hneu.css" type="text/css" />
        <!--Подключаем сначала jQuery затем уже jQuery UI! -->
        <script src="${pageContext.request.contextPath}/scripts/jquery-1.6.2.min.js"></script>
        <script src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom.min.js"></script>                
        <title>${ATTRIBUTE_CSV_UPLOAD_BEAN.titleOfPage}</title>
    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function(){
                $.getJSON('GroupFilter?spec=401', function(data) {
                    alert(
                    'Cources: ' + data.success + '\n'
                        +'Success: ' + data.success + '\n'
                        + 'Message: ' + data.message + '\n'
                        + 'Param: ' + data.param);
                });
            });
        </script>
        <div class="ViewLogContent">
            <div class="userImportPreviewLabel">
                Verification results:
            </div>
            <div class="usersImportedLabel">
                <span id="UsersCount"> ${ATTRIBUTE_CSV_UPLOAD_BEAN.numberValideRecord} users passed verification</span>
            </div>
            <div class="requiredFieldsLabel">
                <span id="labelRequired" class="requiredField o365-err-c">* Required fields</span>
            </div>

            <div class="UserTableContent">
                <table class="tableImportVerify"> 
                    <thead>
                        <tr>
                            <th>Verification</th>                        
                            <c:forEach var="headerName" items="${ATTRIBUTE_CSV_UPLOAD_BEAN.headOfTable}" varStatus="i">                                                
                                <th><c:out value="${headerName}" default="" /> </th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${ATTRIBUTE_CSV_UPLOAD_BEAN.bodyOfTable}" varStatus="i">
                            <tr>
                                <td>
                                    <c:choose> 
                                        <%-- <c:when test="${not paramErrorStudentCsv[i.index].isEmpty()}"> --%>
                                        <c:when test="${not empty ATTRIBUTE_CSV_UPLOAD_BEAN.errorOfRecords[i.index]}">    
                                            <c:forEach var="mess" items="${ATTRIBUTE_CSV_UPLOAD_BEAN.errorOfRecords[i.index]}">
                                                <c:out value="${mess}" default="Valide" /> <br/>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="Valide" />
                                        </c:otherwise>
                                    </c:choose>                                
                                </td>
                                <td><c:out value="${student.lastName}" default="null" /></td>
                                <td><c:out value="${student.firstName}" default="null"/></td>
                                <td><c:out value="${student.patronymic}" default="null" /></td>
                                <%-- <td><c:out value="${student.lastnameEn}" default="" /></td>
                                <td><c:out value="${student.firstnameEn}" default="null"/></td>
                                <td><c:out value="${student.patronymicEn}" default="null" /></td> --%>
                                <td><c:out value="${student.ukrainian}" default="null" /></td>                           
                                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${student.dateLiveReg}"/></td>
                                <td><c:out value="${student.course}" default="null"/></td>
                                <td><c:out value="${student.dismiss}" default="null"/></td>
                                <td><c:out value="${student.extramuralStudent}" default="null"/></td>
                                <td><c:out value="${student.groupa}" default="null"/></td>
                                <td><c:out value="${student.facultyFull}" default="null"/></td> 
                                <td><c:out value="${student.faculty}" default="null"/></td> 
                                <td><c:out value="${student.specialityName}" default="null"/></td> 
                                <td><c:out value="${student.specialityCode}" default="null"/></td>
                                <td><c:out value="${student.specialityNumber}" default="null"/></td>
                                <td><c:out value="${student.card}" default="null"/></td> 
                                <td><c:out value="${student.edbo}" default="null"/></td> 
                          </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div>
            <form action="UploadServlet" method="GET">
                <button type="submit" name="<%=UploadServlet.PARAMERET_ACTION %>" value="<%=UploadServlet.ACTION_REJECT_CSV_DATA%>">Вернуться назад</button>
                <button type="submit" name="<%=UploadServlet.PARAMERET_ACTION %>" value="<%=UploadServlet.ACTION_ACCEPT_CSV_DATA%>">Продолжить</button>  
                <button type="submit" name="<%=UploadServlet.PARAMERET_ACTION %>" value="<%=UploadServlet.ACTION_ACCEPT_GO_BACK%>">Завершить</button>
            </form>
        </div>
            
    </body>
    
</html>
