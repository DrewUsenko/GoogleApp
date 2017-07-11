
<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@ page errorPage="\WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, slGal.LiveEdu.ORM.*,
         slGal.LiveEdu.StudentServletControler" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="pg" value="<%= request.getParameter(StudentServletControler.PARAMETER_GROUP)%>" />
<c:set var="ps" value="<%= request.getParameter(StudentServletControler.PARAMETER_SPESIALITY)%>" />
<c:set var="pf" value="<%= request.getParameter(StudentServletControler.PARAMETER_FACULTY)%>" />
<c:set var="pu" value="<%= request.getParameter(StudentServletControler.PARAMETER_UKRAINIAN)%>" />
<c:if test="${pu eq null}"><c:set var="pu" value="true" /></c:if>
<c:set var="pe" value="<%= request.getParameter(StudentServletControler.PARAMETER_EMAIL)%>" />
<c:if test="${pe eq null}"><c:set var="pe" value="true" /></c:if>

    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Студенты</title>
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
                                <li class="active"><a href="StudentServletControler">Студенты</a></li> 
                                <li class="dropdown">
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">LDAP
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="TeacherLDAPController">Преподаватели</a></li>
                                        <li><a href="StudentLDAPControler">Студенты</a></li>
                                    </ul>
                                </li>                                
                            </ul>
                            <ul class="nav navbar-nav navbar-right">
                                <li>
                                    <a href="UploadServlet?<%=UploadServlet.PARAMERET_ACTION%>=<%=UploadServlet.ACTION_UPLOAD_STUDENT_CSV%>">Добавить студентов из CSV</a>
                            </li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                        </ul>
                    </div>
                </div>
            </nav>

            <div class="row">
                <div class="col-md-12">
                    <h1 class="title">Фильтр по студентам</h1>
                    <form class="form-top" action="StudentServletControler">
                        <input type="hidden" name="action" value="<%=StudentServletControler.ACTION_FILTER%>" >    
                        <div id="filter">
                            <div class="col-md-6 teacher-list">
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Факультет</label>                     
                                    <select class="form-control" data-live-search="true" name="<%=StudentServletControler.PARAMETER_FACULTY%>">
                                        <option selected></option>
                                        <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">   
                                            <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Специальность</label>
                                    <select class="form-control" data-live-search="true" name="<%=StudentServletControler.PARAMETER_SPESIALITY%>">
                                        <option selected></option>
                                        <c:forEach var="speciality" items="${requestScope.ATTRIBUTE_SPECIALITY_LIST}">
                                            <option<c:if test="${speciality eq ps}"> selected</c:if>>${speciality}</option>
                                        </c:forEach>          
                                    </select>
                                </div>                                  
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Группа</label>                                                  
                                    <select class="form-control" data-live-search="true" name="<%=StudentServletControler.PARAMETER_GROUP%>" style="width: 150px;">
                                        <option selected></option>
                                        <c:forEach var="group" items="${requestScope.ATTRIBUTE_GROUP_LIST}">
                                            <option<c:if test="${group eq pg}"> selected</c:if>>${group}</option>
                                        </c:forEach>          
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Украинец</label>                           
                                    <select class="form-control" data-live-search="true" name="<%=StudentServletControler.PARAMETER_UKRAINIAN%>" style="width: 150px;">                                
                                        <option<c:if test="${pu eq true}"> selected</c:if>>true</option>
                                        <option<c:if test="${pu eq false}"> selected</c:if>>false</option>
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

            <c:if test="${ATTRIBUTE_STUDENT != null}">
                <form action="StudentServletControler" method="post"  name="resultsForm" id="resultsForm">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_COURSE_START%>" value="${pCourseStart}">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_COURSE_END%>" value="${pCourseEnd}">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_SPESIALITY%>" value="${ps}">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_FACULTY%>" value="${pf}">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_GROUP%>" value="${pg}">
                    <input type="hidden" name="<%= StudentServletControler.PARAMETER_UKRAINIAN%>" value="${pu}">
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
                                <td>Отчество</td>
                                <td>Email</td>
                                <td>Email_1</td>
                                <td>Украинец</td>
                                <td>ИИН</td>
                                <td>Группа</td>
                                <td>Специальность</td>
                                <td>Факультет</td>                                
                            </tr>
                        </thead>
                        <c:forEach var="student" items="${ATTRIBUTE_STUDENT}">
                            <tr>
                                <td><input type="checkbox" name="check" value="${student.idStudent}"></td>                        
                                <td><c:out value="${student.idStudent}" default="null" /> </td>
                                <td><c:out value="${student.personInf.firstname}" default="null" /></td>
                                <td><c:out value="${student.personInf.lastname}" default="null"/></td>
                                <td><c:out value="${student.personInf.patronymic}" default="null" /></td> 
                                <td><c:out value="${student.personInf.emailCorporate}" default="null" /></td>
                                <td><c:out value="${student.personInf.emailPersonal}" default="null" /></td>
                                <td><c:out value="${student.ukrainian}" default="null" /></td>
                                <td><c:out value="${student.personInf.iin}" default="null" /></td>
                                <td><c:out value="${student.group}" default="null"/></td>
                                <td><c:out value="${student.specInf.spec}" default="null"/></td>
                                <td><c:out value="${student.specInf.facultyInf.faculty}" default="null"/></td>                                
                            </tr>
                        </c:forEach>
                        <div class="buttons">
                            <div class="buttons col-md-12">
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_CHANGE%>" title="Изменить">Изменить</button>  
                            </div>
                        </div>
                        <a href="#" onclick="if (markAllRows('resultsForm'))
                                    return false;">Отметить все</a>
                        <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                                    return false;">Снять выделение</a>
                </form>
            </c:if>

            <c:remove var="ATTRIBUTE_STUDENT" scope="request"/>
        </div>
    </body>    
</html>
