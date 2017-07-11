<%-- 
    Document   : AddTeacher
    Created on : Sep 28, 2015, 1:35:01 PM
    Author     : Andrey
--%>
<%@page import="slGal.LiveEdu.UploadServlet"%>
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
                            <li class="active"><a href="AddTeacher.jsp">Новый преподаватель</a></li>
                            <li><a href="UploadServlet?<%=UploadServlet.PARAMERET_ACTION%>=<%=UploadServlet.ACTION_UPLOAD_TEACHER_CSV%>">Добавить преподавателей из CSV</a></li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                        </ul>
                    </div>
                </div>
            </nav>    

            <div class="form-group">
                <label for="usr">Имя</label>
                <input type="text" class="form-control" id="usr">
            </div>
            <div class="form-group">
                <label for="pwd">Фамилия</label>
                <input class="form-control" id="pwd">
            </div>

            <div class="form-group">
                <label for="pwd">Отчество</label>
                <input class="form-control" id="pwd">
            </div>

            <div class="form-group">
                <label for="pwd">ИИН</label>
                <input class="form-control" id="pwd">
            </div>

            <div class="form-group">
                <label for="pwd">Пароль</label>
                <input type="password" class="form-control" id="pwd">
            </div>

            <div class="form-group">
                <label for="sel1">Факультет:</label>
                <select class="form-control" id="sel1">
                    <option>ЕІ</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                </select>
            </div>

            <div class="form-group">
                <label for="sel1">Кафедра:</label>
                <select class="form-control" id="sel1">
                    <option>ІС</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                </select>
            </div>

            <button type="button" class="btn btn-primary">Добавить</button>
        </div>
    </body>
</html>
