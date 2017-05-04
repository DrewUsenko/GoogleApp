
<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@ page errorPage="\WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, slGal.LiveEdu.ORM.*,
         slGal.LiveEdu.StudentServletControler" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%--<jsp:useBean id="listStudent" class="slGal.LiveEdu.ORM.Student" /> 
<c:set var="items" value="${listStudent.}" scope="request" />--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--<c:set var="as" value="<%= request.getAttribute(StudentServletControler.ATTRIBUTE_SPECIALITY)%>" />--%>
<%--<c:set var="ag" value="<%= request.getAttribute(StudentServletControler.ATTRIBUTE_GROUP)%>" />--%>
<c:set var="pCourseStart" value="<%= request.getParameter(StudentServletControler.PARAMETER_COURSE_START)%>" />
<c:set var="pCourseEnd" value="<%= request.getParameter(StudentServletControler.PARAMETER_COURSE_END)%>" />
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
            <title>JSP Page</title>
            <!--<script type="text/javascript" src="scripts/jquery-1.6.2.min.js"></script>-->
            <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
            <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
            <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
            <script type="text/javascript" src="scripts/jquery-ui-1.8.16.custom.min.js"></script>      
            <script type="text/javascript" src="scripts/myFunction.js"></script>
            <link rel="stylesheet" type="text/css" href="css/style.css" >

            <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" >
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
            <div id="menu">
                <a href="index.jsp">Главная</a> | <a href="TeacherServletControler">Преподаватели</a> | <b>Студенты</b>
                <br/>
                <a href="UploadServlet?<%=UploadServlet.PARAMERET_ACTION%>=<%=UploadServlet.ACTION_UPLOAD_STUDENT_CSV%>">Добавить студентов из CSV</a>
            </div>

            <div class="row">
            <div class="col-md-12">
            <h1 class="title">Фильтр по студентам</h1>
<!--            <h1 class="title">Изменение данных</h1>-->
            <form class="form-top" action="StudentServletControler">
            <input type="hidden" name="action" value="<%=StudentServletControler.ACTION_FILTER%>" >    
            <div id="filter">
                <div class="col-md-6 teacher-list">
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
                        <label for="${teacherConst.PARAMETER_FACULTY}">Факультет</label>                     
                        <select class="form-control" data-live-search="true" name="<%=StudentServletControler.PARAMETER_FACULTY%>">
                            <option selected></option>
                            <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">   
                                <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
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
                        <input class="btn btn-danger" type="submit" value="Фильтр" />
                    </div>
                </div>
            
                </div>
            </form>
                        </div> 
        </div>
            
        <c:if test="${ATTRIBUTE_PERSON != null}">
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
                                           }"></td>                 
                    <td>id</td>
                    <td>Фамилие</td>
                    <td>Имя</td>
                    <td>Отчество</td>
                    <td>Фамилие Eng</td>
                    <td>Имя Eng</td>
                    <td>Отчество Eng</td>
                    <td>Email</td>
<!--                    <td>Курс</td>-->
                    <td>Группа</td>
                    <td>Специальность</td>
<!--                    <td>Заблокирован</td>-->
                    <td>msdnaa</td>
<!--                    <td>В процессе</td>-->
                    <td>office365</td>
                </tr>
            </thead>
                    <c:forEach var="student" items="${ATTRIBUTE_PERSON}">
                        <tr>
                            <td><input type="checkbox" name="check" value="${student.id}"></td>                        
                            <td><c:out value="${student.id}" default="null" /> </td>
                            <td><c:out value="${student.lastname}" default="null" /></td>
                            <td><c:out value="${student.firstname}" default="null"/></td>
                            <td><c:out value="${student.patronymic}" default="null" /></td>
<%--                            <td><c:out value="${student.ukrainian}" default="null" /></td>--%>
                            <td><c:out value="${student.lastnameEn}" default="null" /></td>
                            <td><c:out value="${student.firstnameEn}" default="null"/></td>
                            <td><c:out value="${student.patronymicEn}" default="null" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${student.pdf}">
                                        <c:set var="pathPdf" value="LiveEdu/export/Student/${student.faculty}/${student.spec}/${student.course}/${student.groupa}/pdf/${student.firstname}_${student.lastname}.pdf" />
                                        <a href='${pathPdf}' >
                                            <c:out value="${student.email}" default="null"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${student.email}" default="null"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
<!--                            <td><c:out value="${student.password}" default="null" /></td>-->
<!--                            <td><c:out value="${student.course}" default="null" /></td>-->
                            <td><c:out value="${student.groupa}" default="null"/></td>
                            <td><c:out value="${student.spec}" default="null"/></td>
                            <td><c:out value="${student.msdnaa}" default="null"/></td>
                            <td><c:out value="${student.office365}" default="null"/></td>
<%--                            <td><a href="EditStudent?id=${student.ID}">Edit</a></td>--%>
                        </tr>
                    </c:forEach>
                        <div class="buttons">
                            <div class="buttons col-md-12">     
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_PDF%>" title="Создать PDF">Создать PDF</button>
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_MSDN_IMPORT%>" title="Сгенерировать MSDN IMPORT">Сгенерировать MSDN IMPORT</button>                        
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_OFFICE365_IMPORT%>" title="Сгенерировать Office365 IMPORT">Сгенерировать Office365 IMPORT</button>
                            </div>                            
                            <div class="buttons col-md-12">
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_CLEAR_FIO_AND_EMAIL%>" title="Сгенерировать ФИО">Очистить ФИО</button>  
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_FIO%>" title="Сгенерировать ФИО">Сгенерировать ФИО</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_CLEAR_EMAIL%>" title="Создать EMAIL">Очиститить EMAIL</button>       
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_EMAIL%>" title="Создать EMAIL">Создать EMAIL</button>
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_GENERATE_PASSWORD%>" title="Перегенерировать password">Перегенерировать password</button>
                            </div>
<!--                            <div class="buttons col-md-12">
                                <button type="submit" class=" btn btn-info" name="action" title="Создать PDF">Заблокировать доступ к акауту</button>
                                <button type="submit" class="btn btn-info" name="action" title="Сгенерировать MSDN IMPORT">Создать почту Gmail</button>
                                <button type="submit" class="btn btn-info" name="action" title="Сгенерировать GMAIL IMPORT">Удалить почту Gmail</button>
                                <button type="submit" class="btn btn-info" name="action" title="Сгенерировать Office365 IMPORT">Редактировать пользователя</button>
                            </div>                            -->
                            <div class="buttons col-md-12">                                                   
                                   
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_SET_MSDN%>" title="Установить MSDN">Установить MSDN</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_CLEAR_MSDN%>" title="Снять MSDN">Снять MSDN</button>                                   
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_SET_OFFICE365%>" title="Установить OFFICE365">Установить OFFICE</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentServletControler.ACTION_CLEAR_OFFICE365%>" title="Clear OFFICE365">Clear OFFICE365</button> 
                            </div>
                        </div>
                
            </form>
        </c:if>
        <a href="#" onclick="if (markAllRows('resultsForm'))
                                    return false;">Отметить все</a>
        <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                                    return false;">Снять выделени</a>
        <c:remove var="ATTRIBUTE_STUDENT" scope="request"/>
        </div>
    </body>    
</html>
