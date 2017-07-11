

<%@ page errorPage="\WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, slGal.LiveEdu.ORM.*,
         slGal.LiveEdu.StudentLDAPControler" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="pg" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_GROUP)%>" />
<c:set var="ps" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_SPESIALITY)%>" />
<c:set var="pf" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_FACULTY)%>" />
<c:set var="pu" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_UKRAINIAN)%>" />
<c:set var="pl" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_LDAP)%>" />
<c:if test="${pu eq null}"><c:set var="pu" value="true" /></c:if>
<c:set var="pe" value="<%= request.getParameter(StudentLDAPControler.PARAMETER_EMAIL)%>" />
<c:if test="${pe eq null}"><c:set var="pe" value="true" /></c:if>

    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Студенты LDAP</title>
            <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
            <script type="text/javascript" src="scripts/jquery-ui-1.8.16.custom.min.js"></script>      
            <script type="text/javascript" src="scripts/myFunction.js"></script>
            <link rel="stylesheet" type="text/css" href="css/style.css" >
            <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" >
            <!-- Latest compiled and minified CSS -->
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
            <!-- jQuery library -->
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
            <!-- Latest compiled JavaScript -->
            <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        </head>
        <body>

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
                                <li class="dropdown active">
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">LDAP
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
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
                        <h1 class="title">Фильтр по студентам</h1>
                        <form class="form-top" action="StudentLDAPControler">
                            <input type="hidden" name="action" value="<%=StudentLDAPControler.ACTION_FILTER%>" >    
                        <div id="filter">
                            <div class="col-md-6 teacher-list">
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Факультет</label>                     
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_FACULTY%>">
                                        <option selected></option>
                                        <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">   
                                            <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Специальность</label>
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_SPESIALITY%>">
                                        <option selected></option>
                                        <c:forEach var="speciality" items="${requestScope.ATTRIBUTE_SPECIALITY_LIST}">
                                            <option<c:if test="${speciality eq ps}"> selected</c:if>>${speciality}</option>
                                        </c:forEach>          
                                    </select>
                                </div>                                  
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Группа</label>                                                  
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_GROUP%>" style="width: 150px;">
                                        <option selected></option>
                                        <c:forEach var="group" items="${requestScope.ATTRIBUTE_GROUP_LIST}">
                                            <option<c:if test="${group eq pg}"> selected</c:if>>${group}</option>
                                        </c:forEach>          
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Украинец</label>                           
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_UKRAINIAN%>" style="width: 150px;">                                
                                        <option<c:if test="${pu eq true}"> selected</c:if>>true</option>
                                        <option<c:if test="${pu eq false}"> selected</c:if>>false</option>
                                        </select>                        
                                    </div>    
                                         <!--<div class="col-md-4">
                                        <label for="${teacherConst.PARAMETER_FACULTY}">Сервис</label>                           
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_LDAP%>" style="width: 150px;">                                
                                        <option<c:if test="${pl eq true}"> selected</c:if>>Default</option>
                                        <option<c:if test="${pl eq false}"> selected</c:if>>GitLab</option>
                                        <option<c:if test="${pl eq false}"> selected</c:if>>Moodle</option>
                                        <option<c:if test="${pu eq false}"> selected</c:if>>GitLab+Moodle</option>
                                        </select>                        
                                    </div> -->  
                                    <div class="col-md-12 filter-button">
                                        <input class="btn btn-success" type="submit" value="Фильтр" />
                                    </div>                                    
                                </div>

                            </div>
                        </form>
                    </div> 
                </div>

            <c:if test="${ATTRIBUTE_STUDENT != null}">
                <form action="StudentLDAPControler" method="post"  name="resultsForm" id="resultsForm">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_SPESIALITY%>" value="${ps}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_FACULTY%>" value="${pf}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_GROUP%>" value="${pg}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_UKRAINIAN%>" value="${pu}">
                    <table width="100%" class="table table-striped table-hover teacher-filer-list"> 
                        <thead>
                            <tr>
                                <td><input id="checkAll" type="checkbox" name="checkAll" value="${teacher.id}"
                                           onchange="
                                                   if ($('#checkAll').is(':checked')) {
                                                       $('#checkAll').attr('checked', 'checked');
                                                       if (markAllRows('resultsForm'))
                                                           return false;
                                                   } else {
                                                       $('#checkAll').removeAttr('checked');
                                                       if (unMarkAllRows('resultsForm'))
                                                           return false;
                                                   }">
                                </td>                 
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
                        <c:forEach var="student" items="${ATTRIBUTE_STUDENT}">
                            <tr>
                                <td><input type="checkbox" name="check" value="${student.idStudent}"></td>                        
                                <td><c:out value="${student.idStudent}" default="null" /> </td>
                                <td><c:out value="${student.personInf.firstname}" default="null" /></td>
                                <td><c:out value="${student.personInf.lastname}" default="null"/></td>
                                <td><c:out value="${student.personInf.firstnameEn}" default="null" /></td>
                                <td><c:out value="${student.personInf.lastnameEn}" default="null"/></td>
                                <td><c:out value="${student.personInf.emailCorporate}" default="null" /></td>
                                <td><c:out value="${student.personInf.exist}" default="null"/></td>
                                <td><c:out value="${student.personInf.moodle}" default="null"/></td>
                                <td><c:out value="${student.personInf.gitlab}" default="null"/></td>                                
                            </tr>
                        </c:forEach>
                        <div class="buttons">

                            <div class="buttons col-md-12">   
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CREATE_ACCOUNT%>" title="Создать аккаунт">Создать аккаунт</button> 
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=StudentLDAPControler.ACTION_DELETE_ACCOUNT%>" title="Удалить аккаунт">Удалить аккаунт</button> 
                                </div>
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_SET_MOODLE%>" title="Подключить Moodle">Подключить Moodle</button>                                   
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=StudentLDAPControler.ACTION_DOWN_MOODLE%>" title="отключить Moodle">Отключить Moodle</button> 
                                </div>
                                <div class="btn-group-vertical">
                                    <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_SET_GITLAB%>" title="Подключить GitLab">Подключить GitLab</button> 
                                    <button type="submit" class=" btn btn-warning" name="action" value="<%=StudentLDAPControler.ACTION_DOWN_GITLAB%>" title="отключить GitLab">Отключить GitLab</button> 
                                </div>

                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_MAKE_PDF%>" title="Clear OFFICE365">Сгенерировать PDF</button> 
                            </div>                            
                        </div>

                        <a href="#" onclick="if (markAllRows('resultsForm'))
                                    return false;">Отметить все</a>
                        <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                                    return false;">Снять выделени</a>

                </form>
            </c:if>

            <c:remove var="ATTRIBUTE_STUDENT" scope="request"/>
        </div>
    </body>    
</html>
