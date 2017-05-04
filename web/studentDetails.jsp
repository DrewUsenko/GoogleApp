<%@ page errorPage="jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.fileupload.*,
         org.apache.commons.fileupload.servlet.ServletFileUpload,
         org.apache.commons.fileupload.disk.DiskFileItemFactory,
         org.apache.commons.io.FilenameUtils, java.util.*,
         java.util.zip.*, org.hibernate.*, org.hibernate.criterion.*,
         java.io.*, slGal.LiveEdu.ORM.*, slGal.LiveEdu.DB.HibernateUtil, java.lang.Exception" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LiveEdu - Студенты</title>
    </head>
    <body>

        <%
            if (request.getParameter("deleteStudId") != null) {
                int id = Integer.parseInt(request.getParameter("deleteStudId"));
                Student.DeleteById(id);
                response.addHeader("course", "4");
                response.addHeader("group", "5");
                response.addHeader("spec", "401");
                response.sendRedirect("students.jsp");
            }

            if (request.getParameter("editStudId") != null) {
                String stIdStr = request.getParameter("editStudId");
                int stId = Integer.parseInt(stIdStr);
                Student student = Student.getById(stId);
        %>
        <form action="students.jsp" method="post">
            <table>
                <tr>
                    <td>Фамилия</td>
                    <td><input type="text" name="lname" maxlength="50" value="<%=student.getLastname()%>"/></td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td><input type="text" name="fname" maxlength="50" value="<%=student.getFirstname()%>"/></td>
                </tr>
                <tr>
                    <td>Отчество</td>
                    <td><input type="text" name="patronymic" maxlength="50" value="<%=student.getPatronymic()%>"/></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" maxlength="50" value="<%=student.getEmail()%>"/></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="text" name="password" maxlength="50" value="<%=student.getPass()%>"/></td>
                </tr>
                <tr>
                    <td>Факультет</td>
                    <td><input type="text" name="faculty" maxlength="50" value="<%=student.getFaculty()%>"/></td>
                </tr>
                <tr>
                    <td>Курс</td>
                    <td><input type="text" name="course" maxlength="50" value="<%=student.getCourse()%>"/></td>
                </tr>
                <tr>
                    <td>Группа</td>
                    <td><input type="text" name="groupa" maxlength="50" value="<%=student.getGroupa()%>"/></td>
                </tr>
                <tr>
                    <td>Специальность</td>
                    <td><input type="text" name="spec" maxlength="50" value="<%=student.getSpec()%>"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <button type="submit" name="updateStudentId" value="<%=student.getPer_id()%>">Сохранить</button>
                        <input type="reset" value="Отмена" />
                    </td>
                </tr>
            </table>
        </form>
        <%}%>
    </body>
</html>
