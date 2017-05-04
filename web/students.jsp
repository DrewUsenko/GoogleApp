<%@ page errorPage="WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,
         java.util.zip.*, org.hibernate.*, org.hibernate.criterion.*,
         java.io.*, slGal.LiveEdu.*, slGal.LiveEdu.ORM.*, slGal.LiveEdu.DB.HibernateUtil, java.lang.Exception" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%--
<jsp:useBean id="cstudent" class="slGal.LiveEdu.ORM.Student"></jsp:useBean>
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LiveEdu - Студенты</title>
        
        <style>
            table{
                border: 1px solid black;
                border-collapse: collapse;
            }

            .studentsTable thead{
                background-color: #405481;
                color: white;
                font-weight: bold;
                font-size: 12pt;
            }

            .studentsTable td{
                border-top: 1px solid gray;
                border-bottom: 1px solid gray;
                height: 35px;
            }

            .studentsTable tbody tr:hover {
                background-color: #cad3e7;
                border-top: 2px solid black;
                border-bottom: 2px solid black;
            }

            tr.odd { background: #d6e0f7; }
            tr.even { background: #ecf1fb; }
        </style>

    </head>
    <%
        String strRootPathSite = application.getRealPath(File.separator) + "LiveEdu/";
    %>

    <body>
        <div id="menu"><a href="index.html">Главная</a> | <b>Студенты</b> | <a href="import.html">Импорт данных</a></div>
        <br>
        <div id="body" style="width: 100%; position: relative;">
            <div id="table" style="width: 75%;height: 100%; position: absolute; left: 0px;top: 0px;">
                <form action="studentDetails.jsp" method="post">
                    <table class="studentsTable" width="100%" style="font-size: 11pt">
                        <thead>
                            <tr><td>№</td><td>Фамилия</td><td>Имя</td><td>Отчество</td><td>Email</td><td>Password</td><td>Курс</td><td>Группа</td><td>Спец.</td><td colspan="2">Опции</td></tr>
                        </thead>

                        <%
                            if (request.getParameter("updateStudentId") != null) {
                                String fname = Student.GetEncStr(request.
                                        getParameter("fname"));
                                String lname = Student.GetEncStr(request.
                                        getParameter("lname"));
                                String patronymic = Student.GetEncStr(request.
                                        getParameter("patronymic"));
                                String email = request.getParameter("email");
                                String pass = request.getParameter("password");
                                String faculty = request.getParameter("faculty");
                                String grupa = request.getParameter("groupa");
                                int course = Integer.parseInt(request.getParameter("course"));
                                String spec = request.getParameter("spec");
                                int id = Integer.parseInt(request.getParameter("updateStudentId"));
                                Student.UpdateById(id, fname, lname, patronymic, email, pass, faculty, grupa, course, spec);
                            }
                        %>

                        <%
                            // нажата кнопочка фас
                            if (request.getParameter("show") != null) {
                        %>
                        <%--
                           <p>getAuthType()<%=request.getAuthType()%></p>
                           <p>getCharacterEncoding()<%=request.getCharacterEncoding()%></p>
                           <p>getContentType()<%=request.getContentType()%></p>
                           <p>getContextPath()<%=request.getContextPath()%></p>
                        --%>
                        <%
                            Session ses = null;

                            ses = HibernateUtil.currentSession();

                            List students = new ArrayList<Student>();

                            //kurs
                            //int kurs=5;
                            Criteria cr = ses.createCriteria(Student.class);

                            String str = request.getParameter("course");
                            if (!str.equals("")) {
                                if (str.equals("null")) {
                                    cr = cr.add(Restrictions.isNull("course"));
                                } else {
                                    int course = Integer.parseInt(str);
                                    cr = cr.add(Restrictions.eq("course", course));
                                }
                            }

                            str = request.getParameter("spec");
                            if (!str.equals("")) {
                                if (str.equals("null")) {
                                    cr = cr.add(Restrictions.isNull("spec"));
                                } else {
                                   // int spec = Integer.parseInt(str);
                                    cr = cr.add(Restrictions.eq("spec", str));
                                }
                            }

                            str = request.getParameter("groupa");
                            if (!str.equals("")) {
                                cr = cr.add(Restrictions.eq("groupa", str));
                            }
                            /*
                             * str = request.getParameter("lname"); if
                             * (!str.equals("")) { cr =
                             * cr.add(Restrictions.eq("lastname", str)); }
                             *
                             * str = request.getParameter("fname"); if
                             * (!str.equals("")) { cr =
                             * cr.add(Restrictions.eq("firstname", str)); }
                             */
                            str = request.getParameter("noemail");
                            if (str != null) {
                                if (str.equals("on")) {
                                    cr = cr.add(Restrictions.isNull("liveemail"));
                                }
                            }

                            students = cr.list();
                            HibernateUtil.closeSession();

                            /*
                             * List students1 = new ArrayList<Student>();
                             * students1=ses.createCriteria(Student.class)
                             * .add(Restrictions.eq("kurs", 5))
                             * .add(Restrictions.eq("spec", 401))
                             * .add(Restrictions.eq("groupa", "4")) .list();
                             */
                            //students=ses.createCriteria(Student.class).list();
                            int i = 0;
                            for (Object object : students) //for(Student student:students)
                            {
                                Student student = (Student) object;
                        %><tr>
                            <td><%=student.getPer_id()%></td>
                            <td><%=student.getLastname()%></td>
                            <td><%=student.getFirstname()%></td>
                            <td><%=student.getPatronymic()%></td>
                            <td><%=student.getEmail()%></td>
                            <td><%=student.getPass()%></td>
                            <td><%=student.getKurs()%></td>
                            <td><%=student.getGroupa()%></td>
                            <td><%=student.getSpec()%></td>
                        </tr><%
                                i++;
                            }
                        } else // нажата кнопочка generate
                        if (request.getParameter("generate") != null) {
                            Session ses = HibernateUtil.currentSession();

                            Transaction tx = null;

                            List students = new ArrayList<Student>();
                            
                            Criteria cr = ses.createCriteria(Student.class);

                            String str = request.getParameter("kurs");
                            if (!str.equals("")) {
                                if (str.equals("null")) {
                                    cr = cr.add(Restrictions.isNull("kurs"));
                                } else {
                                    int kurs = Integer.parseInt(str);
                                    cr = cr.add(Restrictions.eq("kurs", kurs));
                                }
                            }
                            str = request.getParameter("spec");
                            if (!str.equals("")) {
                                if (str.equals("null")) {
                                    cr = cr.add(Restrictions.isNull("spec"));
                                } else {
                                    //int spec = Integer.parseInt(str);
                                    cr = cr.add(Restrictions.eq("spec", str));
                                }
                            }
                            
                            str = request.getParameter("groupa");
                            if (!str.equals("")) {
                                cr = cr.add(Restrictions.eq("groupa", str));
                            }

                            students = cr.list();

                            //String pathExportDir = "." +  request.getContextPath() + "/export/";


                            String pathExportDir = strRootPathSite + "export/";
                            // создаем несколько директорий
                            (new File(pathExportDir)).mkdirs();
                            LiveManager.CreateFile(pathExportDir + "Students.csv", students);

                            for (Object object : students) {
                                Student student = (Student) object;
                                String root = pathExportDir
                                        + String.valueOf(student.getFaculty()) + "/"
                                        + String.valueOf(student.getSpec()) + "/"
//                                        + String.valueOf(student.getSpec()) + "-"
//                                        + String.valueOf(student.getKurs()) + "-"
                                        + String.valueOf(student.getGroupa());

                                File file = new File(root);
                                file.mkdirs();
                                //PDFManager.setResurce("."+request.getContextPath()+"/");
                                PDFManager.setResurce(strRootPathSite + "/res/");
                                try {
                                    PDFManager.createPDF_New(student, root, (student.
                                            getMsdnaa() == true) ? true : false);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                /*
                                 * tx=ses.beginTransaction(); try {
                                 * student=(Student)object; student.Generate();
                                 * student.SaveOrUpdate(ses); tx.commit(); }
                                 * catch(Exception exp) { tx.rollback();
                                 * =(Student)object; //try another name }
                                 */

                        %><tr><td><%=student.getPer_id()%></td><td><%=student.getLastname()%></td><td><%=student.getFirstname()%></td><td><%=student.getPatronymic()%></td><td><%=student.getEmail()%></td><td><%=student.getPass()%></td><td><%=student.getKurs()%></td><td><%=student.getGroupa()%></td><td><%=student.getSpec()%></td></tr><%
                                }

                                // LiveManager.CreateFile("./export/Students", students);
                                //try generate file for groups
//                                List myList = ses.createSQLQuery("SELECT distinct faculty,spec,kurs,groupa FROM persons where kurs<6").
//                                        list();
//                                for (Object obj : myList) {
//                                    Object[] o = (Object[]) obj;
//
//                                    int kurs = 0, spec = 0;
//                                    String faculty = "", groupa = "";
//
//                                    faculty = (String) o[0];
//                                    spec = (Integer) o[1];
//                                    kurs = (Integer) o[2];
//                                    groupa = (String) o[3];
//
//                                    List stud = null;
//                                    stud = ses.createCriteria(Student.class).add(Restrictions.
//                                            eq("faculty", faculty)).add(Restrictions.
//                                            eq("kurs", kurs)).add(Restrictions.
//                                            eq("spec", spec)).add(Restrictions.
//                                            eq("groupa", groupa)).list();
//                                    /*
//                                     * String root = "./LiveEdu/export/" +
//                                     * String.valueOf(faculty) + "/" +
//                                     * String.valueOf(spec);
//                                     */
//
//                                    String root = strRootPathSite
//                                            + "/export/"
//                                            + String.valueOf(faculty) + "/"
//                                            + String.valueOf(spec);
//
//                                    String pth = String.valueOf(spec) + "-"
//                                            + String.valueOf(kurs) + "-"
//                                            + groupa;
//                                    String root2 = root + "/" + pth;
//
//                                    String fileName = root2 + "/" + pth + ".csv";
//                                    LiveManager.CreateFile(fileName, stud);
//                                }

                            }
                            if (request.getParameter("generate_email_and_password") != null) {
                                Session ses = HibernateUtil.currentSession();
                                Transaction tx = null;
                                List students = new ArrayList<Student>();

                                Criteria cr = ses.createCriteria(Student.class);
                                //students = ses.createCriteria(Student.class).add(Restrictions.isNull("liveemail")).add(Restrictions.isNull("pass")).list();

                                String str = request.getParameter("kurs");
                                if (!str.equals("")) {
                                    if (str.equals("null")) {
                                        cr = cr.add(Restrictions.isNull("kurs"));
                                    } else {
                                        int kurs = Integer.parseInt(str);
                                        cr = cr.add(Restrictions.eq("kurs", kurs));
                                    }
                                }

                                str = request.getParameter("spec");
                                if (!str.equals("")) {
                                    if (str.equals("null")) {
                                        cr = cr.add(Restrictions.isNull("spec"));
                                    } else {
                                       // int spec = Integer.parseInt(str);
                                        cr = cr.add(Restrictions.eq("spec", str));
                                    }
                                }

                                str = request.getParameter("groupa");
                                if (!str.equals("")) {
                                    cr = cr.add(Restrictions.eq("groupa", str));
                                }

                                students = cr.add(Restrictions.isNull("liveemail")).
                                        add(Restrictions.isNull("pass")).list();
                                Student.nameFileAlphabetic = strRootPathSite + "/res/alphabetic.ini";
                                for (Object object : students) {
                                    Student student = (Student) object;
                                    tx = ses.beginTransaction();
                                    try {
                                        //student = (Student) object;
                                        student.generateYear();
                                        ses.update(student);
                                        //student.SaveOrUpdate(ses);
                                        tx.commit();
                                    } catch (Exception exp) {
                                        tx.rollback();
                                        student.clearMailAndPassword();
                                        try {
                                            tx = ses.beginTransaction();
                                            student = (Student) object;
                                            student.generateYear();
                                            student.SaveOrUpdate(ses);
                                            tx.commit();
                                        } catch (Exception ex) {
                                            tx.rollback();
                        %>
                        <%=ex.toString()%>
                        <%//try another name
                            }
                        } finally {
                        %>
                        <tr><td><%=student.getPer_id()%>
                            </td><td><%=student.getLastname()%>
                            </td><td><%=student.getFirstname()%>
                            </td><td><%=student.getPatronymic()%>
                            </td><td><%=student.getEmail()%>
                            </td><td><%=student.getPass()%>
                            </td><td><%=student.getKurs()%>
                            </td><td><%=student.getGroupa()%>
                            </td><td><%=student.getSpec()%></td></tr>
                            <%
                                        }
                                    }
                                }
                            %>
                    </table>
                </form>
            </div>
            <div id="filter" style="width: 25%;height: 100%; position: absolute; right: 0px;top: 0px; background-color: silver; ">
                <br/>
                <form action="students.jsp" method="post">
                    <table width="100%">
                        <caption><b>Критерии поиска</b></caption>
                        <tr><td align="right" style="padding-right: 10px;">Курс</td><td>

                                <select name="kurs" style="width: 100px;">
                                    <option></option>    
                                    <%
                                        List<Integer> cources = Student.
                                                getAllCources();
                                        for (Integer object : cources) {
                                            if (request.getParameter("kurs") != null && request.
                                                    getParameter("kurs").equals(object)) {
                                    %> <option selected > <%= object%></option><%
                                    } else {
                                    %> <option> <%= object%></option><%
                                            }
                                        }
                                    %>

                                    <%--  
                                    <option></option>
                                    <c:forEach var="course" items="${Student.getAllCources()}" >
                                        <option>${cousre}</option>
                                    </c:forEach>
                                    --%>
                                </select>
                            </td></tr>
                        <tr><td align="right" style="padding-right: 10px;">Група</td><td>

                                <select name="groupa" style="width: 100px;">
                                    <option selected>
                                    </option>
                                    <%
                                        List<String> groups = Student.
                                                getAllGroups();
                                        for (String group : groups) {
                                    %>
                                    <option>
                                        <%= group%></option>
                                        <%}%>
                                </select>

                            </td></tr>
                        <tr><td align="right" style="padding-right: 10px;">Специальность</td><td>

                                <select name="spec" style="width: 100px;">
                                    <option>
                                    </option>
                                    <%
                                        List specs = Student.getAllSpecs();
                                        for (Object object : specs) {
                                    %>
                                    <option>
                                        <%=object%></option>
                                        <%}%>
                                </select>

                            </td></tr>
                        <tr><td colspan="2" align="center" style="background-color: #405481; color: white;">Персональные данные</td></tr>
                        <tr><td align="right" style="padding-right: 10px;">Фамилия</td><td><input type="text" name="lname" maxlength="50" value="<%=Student.GetEncStr(request.getParameter("lname"))%>"/></td></tr>
                        <tr><td align="right" style="padding-right: 10px;">Имя</td><td><input type="text" name="fname" maxlength="50" value="<%=Student.GetEncStr(request.getParameter("fname"))%>"/></td></tr>
                        <tr><td></td><td align="left"><input type="checkbox" name="noemail"/>Пустой email</td></tr>
                        <tr><td align="right" colspan="2" style="padding-right: 10px;"><input type="submit" name="show" value="Фасс!"><input type="reset" value="Отмена"></td></tr>
                        <tr><td align="center" colspan="2"><b>Работа с данными</b></td></tr>
                        <tr><td align="center" colspan="2"><input type="submit" name="generate_email_and_password" value="Сгенирировать мыло и пароль!"></td></tr>
                        <tr><td align="center" colspan="2"><input type="submit" name="generate" value="Сгенирировать PDF"></td></tr>
                    </table>
                </form>
            </div>
        </div>


    </body>
</html>
