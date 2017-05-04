
<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@page import="java.io.File"%>
<%@page errorPage="WEB-INF/jsp/ErrorPage.jsp"%>
<%@page import="slGal.LiveEdu.TeacherServletControler"%>
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
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
        <link rel="stylesheet" type="text/css" href="css/style.css" >
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
        <div class="container">
        <div id="menu">
            <a href="index.jsp">Главная</a> | <b>Преподаватели</b> | <a href="StudentServletControler">Студенты</a>
            <br/>
            <a href="UploadServlet?<%=UploadServlet.PARAMERET_ACTION%>=<%=UploadServlet.ACTION_UPLOAD_TEACHER_CSV%>">Добавить преподавателей из CSV</a>
        </div>
        
        <div class="row">
        <div class="col-md-12">
            <h1 class="title">Фильтр преподаватели</h1>
        <form class="form-top" action="TeacherServletControler">
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
                            <input class="btn btn-danger" type="submit" value="Фильтр" />
                        </div>
                    </div>
                </div>
            </form>
       <div class="row col-md-6">
    <div class="col-md-4">
        <form action="TeacherServletControler" method="GET">
                    <input class="btn btn-primary" type="submit" value="Новый преподаватель" />
                        <input class="btn btn-primary" type="hidden" name="action" value="<%=TeacherServletControler.ACTION_ADD_NEW_TEACHER%>">       
        </form>
    </div>
                            </div>    
                </div> 
        </div>
    <form action="TeacherServletControler" method="post" name="resultsForm" id="resultsForm">
        <input type="hidden" name="<%= TeacherConst.PARAMETER_FACULTY%>" value="${pf}">
        <input type="hidden" name="<%= TeacherConst.PARAMETER_DEPARTMENT%>" value="${pd}">
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
                    <td>Фамилие</td>
                    <td>Имя</td>
                    <td>Email</td>
<!--                    <td>Пар</td>-->
                    <td>Факультет</td>
                    <td>Кафедра</td>
                    <td>msdnaa</td>
                    <td>Office365</td>
                </tr>
            </thead>

            <c:forEach var="teacher" items="${requestScope[teacherConst.ATTRIBUTE_TEACHERS]}">
                <tr>
                    <td><input type="checkbox" name="check" value="${teacher.id}"></td>
                    <td>${teacher.id}</td>
                    <td>${teacher.lastname}</td>
                    <td>${teacher.firstname}</td>
                    <td>${teacher.patronymic}</td>
                    <td>${teacher.lastnameEn}</td>
                    <td>${teacher.firstnameEn}</td>
                    <td>${teacher.email}</td>
<!--                    <td>${teacher.password}</td>-->
                    <td>${teacher.faculty}</td>
                    <td>${teacher.department}</td>
                    <td>${teacher.msdnaa}</td>
                    <td>${teacher.office365}</td>
                </tr>
            </c:forEach>
            <div class="buttons">
                <div class="buttons col-md-12">
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_PDF%>" title="Создать PDF">Создать PDF</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_MSDN_IMPORT%>" title="Сгенерировать MSDN IMPORT">Сгенерировать MSDN IMPORT</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_OFFICE365_IMPORT%>" title="Сгенерировать Office365 IMPORT">Сгенерировать Office365 IMPORT</button>
                </div>
<!--                <div class="buttons col-md-12">
                    <button type="submit" class=" btn btn-info" name="action" title="Создать PDF">Заблокировать доступ к акауту</button>
                    <button type="submit" class="btn btn-info" name="action" title="Сгенерировать MSDN IMPORT">Создать почту Gmail</button>
                    <button type="submit" class="btn btn-info" name="action" title="Сгенерировать GMAIL IMPORT">Удалить почту Gmail</button>
                    <button type="submit" class="btn btn-info" name="action" title="Сгенерировать Office365 IMPORT">Редактировать пользователя</button>
                </div>   -->
                    
                <div class="buttons col-md-12">
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_CLEAR_FIO_AND_EMAIL%>" title="Очистить ФИО">Очистить ФИО</button>
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_CLEAR_EMAIL%>" title="Очиститить EMAIL">Очиститить EMAIL</button>
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_FIO%>" title="Сгенерировать ФИО">Сгенерировать ФИО</button>
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_EMAIL%>" title="Создать EMAIL">Создать EMAIL</button>
                    <button type="submit" class=" btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_GENERATE_PASSWORD%>" title="Перегенерировать password">Перегенерировать password</button>
                </div>
                <div class="buttons col-md-12">
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_SET_MSDN%>" title="Установить MSDN">Установить MSDN</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_CLEAR_MSDN%>" title="Clear MSDN">Clear MSDN</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_SET_OFFICE365%>" title="Установить OFFICE365">Установить OFFICE365</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_CLEAR_OFFICE365%>" title="Clear OFFICE365">Clear OFFICE365</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_SET_DISMISS%>" title="Уволить">Уволить</button>
                    <button type="submit" class="btn btn-info" name="action" value="<%=TeacherServletControler.ACTION_CLEAR_DISMISS%>" title="Востановить">Востановить</button>
                </div>

            </div>
            
            <c:remove var="ATTRIBUTE_TEACHERS" scope="request"/>
    </form>
    <a href="#" onclick="if (markAllRows('resultsForm'))
                    return false;">Отметить все</a>
    <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                    return false;">Снять выделени</a>


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
    </div>
</body>
</html>

