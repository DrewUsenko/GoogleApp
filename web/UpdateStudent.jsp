<%-- 
    Document   : UpdateJsp
    Created on : Nov 13, 2011, 5:11:02 PM
    Author     : mlch
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="slGal.LiveEdu.ORM.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <style>
            input[type=text] {
                width: 250px
            }


        </style>

    </head>
    <body>
        <form style="margin: 0px auto;width: 400px;" action="EditStudent" method="post">
            <h1 align="center">Студент</h1>
            <%

                Student student = (Student) request.getAttribute("student");
            %>
            <table width="100%">
                <tr>
                    <td>Имя</td>
                    <td>
                        <input type="text" name="firstName" value="<%=student.getFirstname()%>" /></td>
                </tr>
                <tr>
                    <td>Фамилия</td>
                    <td><input type="text" name="lastName" value="<%=student.getLastname()%>" /></td>
                </tr>
                <tr>
                    <td>Отчество</td>
                    <td><input type="text" name="patronymic" value="<%=student.getPatronymic()%>" /></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="liveEmail" value="<%=student.getEmail()%>" /></td>
                </tr>
                <tr>
                    <td>Пароль</td>
                    <td><input type="text" name="password" value="<%=student.getPassword() %>" /></td>
                </tr>
                <tr>
                    <td>Факультет</td>
                    <td><input type="text" name="faculty" value="<%=student.getFaculty() %>" /></td>
                </tr>
                <tr>
                    <td>Группа</td>
                    <td><input type="text" name="group" value="<%=student.getGroupa() %>" /></td>
                </tr>
                <tr>
                    <td>Курс</td>
                    <td><input type="text" name="cource" value="<%=student.getCourse()%>" /></td>
                </tr>
                <tr>
                    <td>Специальность</td>
                    <td><input type="text" name="spec" value="<%=student.getSpec() %>" /></td>
                </tr>
                <tr>
                    <td><input type="hidden" name="id" value="<%=student.getId()%>" /></td>
                    <td>
                        <input type="submit" value="Обновить" />
                    </td>
                </tr>
            </table>

        </form>  
    </body>
</html>
