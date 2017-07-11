
<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@page import="java.io.File"%>
<%@page errorPage="WEB-INF/jsp/ErrorPage.jsp"%>
<%@page import="slGal.LiveEdu.TeacherLDAPController"%>
<%@page import="slGal.LiveEdu.TeacherConst"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="teacherConst" class="slGal.LiveEdu.TeacherConst" scope="session" />
<c:set var="pd" value="<%= request.getParameter(TeacherConst.PARAMETER_DEPARTMENT)%>" />
<c:set var="pf" value="<%= request.getParameter(TeacherConst.PARAMETER_FACULTY)%>" />
<c:set var="pDismiss" value="<%= request.getParameter(TeacherConst.PARAMETER_DISMISS)%>" />
<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Teacher filter</title>
        <script type="text/javascript" src="scripts/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="scripts/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript" src="scripts/myFunction.js"></script>
        <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" >
        <link rel="stylesheet" type="text/css" href="css/style.css" >
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Latest compiled JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <%
        String strRootPathSite = application.getRealPath(File.separator) + "LiveEdu/";
        String faculty = "";
        String department = "";
        String spec = "";
        if (request.getParameter("faculty") != null) {
            faculty = request.getParameter("faculty").toString();
        }
        if (request.getParameter("department") != null) {
            department = request.getParameter("department").toString();
        }
    %>
    <body>
        <script type="text/javascript">
            $(document).ready(function () {
                $.getJSON('GroupFilter?spec=401', function (data) {
                    alert(
                            'Cources: ' + data.success + '\n'
                            + 'Success: ' + data.success + '\n'
                            + 'Message: ' + data.message + '\n'
                            + 'Param: ' + data.param);
                });
            });
        </script>
        <script>
            $("input#checkAll")
                    .change(function () {
                        if ($('#checkAll').is(':checked')) {
                            $('#checkAll').attr('checked', 'checked');
                            if (markAllRows('resultsForm'))
                                return false;
                        } else {
                            $('#checkAll').removeAttr('checked');
                            if (unMarkAllRows('resultsForm'))
                                return false;
                        }
                    })
                    .change();
        </script>
        <div class="container">             
            <nav class="navbar navbar-default navbar-static">
                <div class="container-fluid">
                    <div class="navbar-header">                            
                        <a class="navbar-brand" href="#">LiveEdu</a>
                    </div>
                    <div class="collapse navbar-collapse js-navbar">
                        <ul class="nav navbar-nav">
                            <li><a href="index.jsp">Главная</a></li>
                            <li><a href="TeacherServletControler">Преподаватели</a></li>
                            <li><a href="StudentServletControler">Студенты</a></li> 
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">LDAP
                                    <span class="caret"></span></a>
                                <ul class="dropdown-menu active">
                                    <li><a href="TeacherLDAPController">Преподаватели</a></li>
                                    <li><a href="StudentLDAPControler">Студенты</a></li>
                                </ul>
                            </li>                                
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                        </ul>
                    </div>
                </div>
            </nav>    

            <div class="row">
                <div class="col-md-12">
                    <h1 class="title">Фильтр преподаватели</h1>
                    <form class="form-top" action="TeacherLDAPController">
                        <input type="hidden" name="action" value="${teacherConst.ACTION_FILTER}" >
                        <div id="filter">
                            <div class="col-md-6 teacher-list">
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Факультет</label>
                                    <select class="form-control" data-live-search="true" name="${teacherConst.PARAMETER_FACULTY}">
                                        <option selected></option>
                                        <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">
                                            <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Кафедра</label>
                                    <select class="form-control" data-live-search="true" name="${teacherConst.PARAMETER_DEPARTMENT}">
                                        <option selected></option>
                                        <c:forEach var="department" items="${requestScope.ATTRIBUTE_DEPARTMENT_LIST}">

                                            <option<c:if test="${department eq pd}"> selected</c:if>>${department}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-12 filter-button">
                                    <input class="btn btn-success" type="submit" value="Фильтр" />
                                </div>
                            </div>
                        </div>
                    </form>
                </div> 
            </div>
            <c:if test="${requestScope[teacherConst.ATTRIBUTE_TEACHERS] != null}">
                <form action="TeacherLDAPController" method="post" name="resultsForm" id="resultsForm">
                    <input type="hidden" name="<%= TeacherConst.PARAMETER_FACULTY%>" value="${pf}">
                    <input type="hidden" name="<%= TeacherConst.PARAMETER_DEPARTMENT%>" value="${pd}">
                    <table width="100%" class="table table-striped table-hover teacher-filer-list">
                        <thead>
                            <tr>
                                <td><input id="checkAll" type="checkbox" name="checkAll" value="${teacher.idStuff}"
                                           onchange="
                                                   if ($('#checkAll').is(':checked')) {
                                                       $('#checkAll').attr('checked', 'checked');
                                                       if (markAllRows('resultsForm'))
                                                           return false;
                                                   } else {
                                                       $('#checkAll').removeAttr('checked');
                                                       if (unMarkAllRows('resultsForm'))
                                                           return false;
                                                   }"></td>                                            
                                <td>id</td>
                                <td>Имя</td>
                                <td>Фамилие</td>                                
                                <td>Имя Eng</td>
                                <td>Фамилие Eng</td>
                                <td>Email</td>       
                                <td>Exist</td>  
                                <td>Moodle</td>
                                <td>GitLab</td>
                            </tr>
                        </thead>

                        <c:forEach var="teacher" items="${requestScope[teacherConst.ATTRIBUTE_TEACHERS]}">
                            <tr>
                                <td><input type="checkbox" name="check" value="${teacher.idStuff}"></td>                        
                                <td><c:out value="${teacher.idStuff}" default="null" /> </td>
                                <td><c:out value="${teacher.personInf.firstname}" default="null" /></td>
                                <td><c:out value="${teacher.personInf.lastname}" default="null"/></td>
                                <td><c:out value="${teacher.personInf.firstnameEn}" default="null" /></td>
                                <td><c:out value="${teacher.personInf.lastnameEn}" default="null"/></td>
                                <td><c:out value="${teacher.personInf.emailCorporate}" default="null" /></td>
                                <td><c:out value="${teacher.personInf.exist}" default="null"/></td>
                                <td><c:out value="${teacher.personInf.moodle}" default="null"/></td>
                                <td><c:out value="${teacher.personInf.gitlab}" default="null"/></td> 
                            </tr>
                        </c:forEach>
                        <div class="buttons">

                            <div class="buttons col-md-12">   
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherLDAPController.ACTION_CREATE_ACCOUNT%>" title="Создать аккаунт">Создать аккаунт</button> 
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=TeacherLDAPController.ACTION_DELETE_ACCOUNT%>" title="Удалить аккаунт">Удалить аккаунт</button> 
                                </div>
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherLDAPController.ACTION_SET_MOODLE%>" title="Подключить Moodle">Подключить Moodle</button>                                   
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=TeacherLDAPController.ACTION_DOWN_MOODLE%>" title="отключить Moodle">Отключить Moodle</button> 
                                </div>
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherLDAPController.ACTION_SET_GITLAB%>" title="Подключить GitLab">Подключить GitLab</button> 
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=TeacherLDAPController.ACTION_DOWN_GITLAB%>" title="отключить GitLab">Отключить GitLab</button> 
                                </div>

                                <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherLDAPController.ACTION_MAKE_PDF%>" title="Clear OFFICE365">Сгенерировать PDF</button> 
                            </div> 
                                                          

                        </div>
                        <a href="#" onclick="if (markAllRows('resultsForm'))
                                    return false;">Отметить все</a>
                        <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                                    return false;">Снять выделени</a>

                </form>
            </c:if>
            <c:remove var="ATTRIBUTE_TEACHERS" scope="request"/>

        </div>
    </body>
</html>

