<%-- 
    Document   : teacherCSVFile
    Created on : Oct 2, 2015, 11:29:06 AM
    Author     : Andrey
--%>

<%@page import="slGal.LiveEdu.UploadServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pTypeOfAction" value="<%=request.getParameter(UploadServlet.PARAMERET_ACTION)%>" scope="page"/>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Uploading Form</title>
        <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
        <script src="http://gregpike.net/demos/bootstrap-file-input/bootstrap.file-input.js"></script>

    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#file').bootstrapFileInput();
            });
        </script>

        <div class="container">               

            <nav class="navbar navbar-default navbar-static">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".js-navbar">
                            <span class="sr-only">Toggle navigation</span>
                        </button>
                        <a class="navbar-brand" href="#" style="padding:15px 15px;">LiveEdu</a>
                    </div>
                    <div class="collapse navbar-collapse js-navbar">
                        <ul class="nav navbar-nav">
                            <li><a href="index.jsp">Главная</a></li>
                            <li><a href="TeacherServletControler">Преподаватели</a></li>
                            <li><a href="StudentServletControler">Студенты</a></li> 
                            <li><a href="">Сервисы Преподаватели</a></li>  
                            <li><a href="StudentLDAPControler">Сервисы Студенты</a></li>  
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="active">
                                <a href="UploadServlet?<%=UploadServlet.PARAMERET_ACTION%>=<%=UploadServlet.ACTION_UPLOAD_STUDENT_CSV%>">Добавить студентов из CSV</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
                                
            <div class="col-md-6">
                <form action="UploadServlet" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="<%=UploadServlet.PARAMERET_ACTION%>" value="${pTypeOfAction}" >
                    <label for="<%=UploadServlet.PARAMETER_FILE%>" >Виберите Файл</label><br>
                    <input id="file" type="file" name="<%=UploadServlet.PARAMETER_FILE%>" data-filename-placement="inside" /><br>
                    <input type="submit" class="btn btn-success" style="display:block; margin-top:10px;" value="Upload File" />                
                </form>
            </div>
        </div>
    </body>
</html>

