<%@ page errorPage="jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%-- 
<%@ page import="org.apache.commons.fileupload.*,
         org.apache.commons.fileupload.servlet.ServletFileUpload,
         org.apache.commons.fileupload.disk.DiskFileItemFactory,
         org.apache.commons.io.FilenameUtils" %>
--%>

<%@ page  import="java.util.*, java.util.zip.*, org.hibernate.*, org.hibernate.criterion.*,
         java.io.*, slGal.LiveEdu.ORM.*, slGal.LiveEdu.DB.HibernateUtil, java.lang.Exception" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="scripts/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="scripts/jquery-ui-1.8.16.custom.min.js"></script>

        <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" >
    </head>
    <%
        String strRootPathSite = application.getRealPath(File.separator) + "LiveEdu/";
        String cource = "";
        String group = "";
        String spec = "";
        if (request.getParameter("cource") != null) {
            cource = request.getParameter("cource").toString();
        }
        if (request.getParameter("group") != null) {
            group = request.getParameter("group").toString();
        }
        if (request.getParameter("spec") != null) {
            spec = request.getParameter("spec").toString();
        }
    %>
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
        <h1>Hello World!</h1>
        <form action="Filter">
            <div id="filter">
                <table>
                    <tr>
                        <td>Специальность</td>
                        <td><input type="text" name="spec" value="<%=spec%>"/></td>
                    </tr>
                    <tr>
                        <td>Курс</td>
                        <td><input type="text" name="cource" value="<%=cource%>"/></td>
                    </tr>
                    <tr>
                        <td>Группа</td>
                        <td><input type="text" name="group" value="<%=group%>" /></td>
                    </tr>

                    <tr>
                        <td>&nbsp;</td>
                        <td><input type="submit" value="Фильтр" /></td>
                    </tr>
                </table>
            </div>
        </form>
        <form action="StudentsList.jsp" method="post">
            <table width="100%">
                <%
                    List students = new ArrayList<Student>();
                    students = (ArrayList<Student>) request.getAttribute("students");
                    for (Object object : students)
                    {
                        Student student = (Student) object;
                %><tr>
                    <td><%=student.getPer_id()%></td>
                    <td><%=student.getLastname()%></td>
                    <td><%=student.getFirstname()%></td>
                    <td><%=student.getPatronymic()%></td>
                    <td><%=student.getEmail()%></td>
                    <td><%=student.getPass()%></td>
                    <td><%=student.getCourse()%></td>
                    <td><%=student.getGroupa()%></td>
                    <td><%=student.getSpec()%></td>
                    <td><%=student.getMsdnaa()%></td>
                </tr>
            </table>
        </form>
    </body>
</html>
