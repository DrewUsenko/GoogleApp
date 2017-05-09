/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import edu.hneu.googleapp.utill.StringExt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hneu.googleapp.parser.CsvWriterGmail;
import net.hneu.googleapp.parser.CsvWriterMsdn;
import net.hneu.googleapp.parser.CsvWriterOffice365;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.*;
import slGal.LiveEdu.ORM.*;

/*import slGal.LiveEdu.ORM.Person;
import slGal.LiveEdu.ORM.Student;
import slGal.LiveEdu.ORM.Teacher;
import java.io.*;
import slGal.LiveEdu.DB.DB_0;*/

/**
 *
 * @author Andrey
 */
public class StudentServletControler extends PersonServletControler {
    public static final String ACTION_CLEAR_MSDN = "ACTION_CLEAR_MSDN";
    public static final String ACTION_CREATE_GMAIL = "ACTION_CREATE_GMAIL";
    public static final String ACTION_GENERATE_MSDN_IMPORT = "ACTION_GENERATE_MSDN_IMPORT";
    public static final String ACTION_GENERATE_OFFICE365_IMPORT = "ACTION_GENERATE_OFFICE365_IMPORT";

    public static final String ACTION_GENERATE_PASSWORD = "ACTION_GENERATE_PASSWORD";

    public static final String ACTION_GENERATE_PDF = "ACTION_GENERATE_PDF";
    public static final String ACTION_SET_MSDN = "ACTION_SET_MSDN";
    public static final String ACTION_SET_OFFICE365 = "ACTION_SET_OFFICE365";
    public static final String ACTION_CLEAR_OFFICE365 = "ACTION_CLEAR_OFFICE365";
    public static final String ATTRIBUTE_CSV_IMPORT = "ATTRIBUTE_CSV_IMPORT";
    //
    public static final String ATTRIBUTE_FACULTY = "ATTRIBUTE_FACULTY";
    public static final String ATTRIBUTE_FACULTY_LIST = "ATTRIBUTE_FACULTY_LIST";
    public static final String ATTRIBUTE_GROUP = "ATTRIBUTE_GROUP";
    //
    public static final String ATTRIBUTE_GROUP_LIST = "ATTRIBUTE_GROUP_LIST";
    public static final String ATTRIBUTE_SPECIALITY = "ATTRIBUTE_SPECIALITY";
    public static final String ATTRIBUTE_SPECIALITY_LIST = "ATTRIBUTE_SPECIALITY_LIST";
    public static final String ATTRIBUTE_COURSE_LIST = "ATTRIBUTE_COURSE_LIST";

    //
    public static final String ATTRIBUTE_STUDENT = PersonServletControler.ATTRIBUTE_PERSON;
    public static final String ATTRIBUTE_YEAR = "ATTRIBUTE_YEAR";
    public static final String PARAMETER_EMAIL = "paramEmail";
    //
    public static final String PARAMETER_FACULTY = "paramFaculty";
    public static final String PARAMETER_COURSE_START = "paramCourseStart";
    public static final String PARAMETER_COURSE_END = "paramCourseEnd";
    public static final String PARAMETER_GROUP = "paramGroup";
    public static final String PARAMETER_SPESIALITY = "paramSpeciality";
    public static final String PARAMETER_UKRAINIAN = "paramUkranianian";
    public static final String PARAMETER_YEAR = "year";

    private static Path ROOT_PATH_SITE;

    private void bachClearEmail(HttpServletRequest request)
            throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.in(DB.Student.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        Person.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                student.getPersonInf().setEmailCorporate(null);
                student.getPersonInf().setPassCorporate(null);
                student.getPersonInf().setPdf(Boolean.FALSE);
                ses.update(student);
                tx.commit();
            } catch (Exception exp) {
                tx.rollback();
                System.out.println(exp.toString());
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(PersonServletControler.ATTRIBUTE_PERSON, listStudent);
    }

    private void bachClearFioAndEmail(HttpServletRequest request)
            throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        
        List<StudentInf> listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.in(DB.Student.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        Person.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                student.getPersonInf().setLastnameEn(null);
                student.getPersonInf().setFirstnameEn(null);
                student.getPersonInf().setPatronymicEn(null);               
                student.getPersonInf().setEmailCorporate(null);
                student.getPersonInf().setPassCorporate(null);
                student.getPersonInf().setPdf(Boolean.FALSE);

                ses.update(student);
                tx.commit();
            } catch (Exception exp) {
                tx.rollback();
                System.out.println(exp.toString());
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachGenerateFioEn(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.isNull(DB.Person.COLUM_FIRST_NAME_EN))
                .add(Restrictions.isNull(DB.Person.COLUM_LAST_NAME_EN))
                .add(Restrictions.isNull(DB.Person.COLUM_EMAIL_CORPORATE))
                .add(Restrictions.isNull(DB.Person.COLUM_PASS_CORPORATE))                   
                .add(Restrictions.in(DB.Student.COLUM_ID, StringExt.toInt(arrayID)))
                .list();      

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        PersonInf persone = new PersonInf();
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                persone.translateFIO();
                ses.update(student);
                tx.commit();
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachGenerateFioEn", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();

        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void batchGeneratePassword(HttpServletRequest request) throws HibernateException {
         String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<Person> personList = ses.createCriteria(Student.class)
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
                        
            for (Person person : personList) {
                Transaction tx = ses.beginTransaction();
                try {
                    person.clearPassword();
                    person.generatePassword();
                    
                    person.setPdf(Boolean.FALSE);
                   
                    ses.update(person);                    
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "MEss", exp);
                    
                    ses =  HibernateUtil.currentSession();                    
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(StudentServletControler.ATTRIBUTE_STUDENT, personList);
        }
    }

    private void bachGmailImport(HttpServletRequest request) throws HibernateException {
        /*  String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        
        Session ses = HibernateUtil.currentSession();

        List<Person> listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        
        
        List<String> msdnInport = CsvWriterGmail.readWithCsvBeanReader(listStudent);
        
        String[][] create = new String[msdnInport.size()-1][4];
        for (int i = 0; i < msdnInport.size()-1; i++) {
            String info[] = new String[17];
            info = msdnInport.get(i+1).toString().split(",");
            for (int j = 0; j < 4; j++)
            {
                create[i][j] = info[j];
            }
	}
        
        try {
            ApiGoogle.CreateUser(ROOT_PATH_SITE, create, listStudent.size());
        }catch(GoogleJsonResponseException ex) {
            
            Logger.getLogger(StudentServletControler.class.getName()).log(Level.SEVERE, ex.getDetails().getMessage().toString(), ex);
            
        }catch (Exception ex) {
            Logger.getLogger(StudentServletControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);    */  
    }

    private void bachMSDNInport(HttpServletRequest request) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<Person> listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        List<String> msdnInport = CsvWriterMsdn.readWithCsvBeanReader(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_CSV_IMPORT, msdnInport);*/
    }

    private void bachOffice365Inport(HttpServletRequest request) throws HibernateException {
        /*  String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<Person> listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        List<String> office365Inport = CsvWriterOffice365.writeWithCsvMapWriter(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_CSV_IMPORT, office365Inport);*/
    }

    private void bachSetMSDN(HttpServletRequest request, boolean flagMsdn) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB.Student.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        Person.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                student.setMsdnaa(flagMsdn);
                ses.update(student);
                tx.commit();
            } catch (Exception exp) {
                tx.rollback();
                System.out.println(exp.toString());
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);*/
    }

    private void bachSetOffice365(HttpServletRequest request, boolean falgOffice365) throws HibernateException {
        /* String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<Student> listStudent = new ArrayList<>();

        listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        for (Student student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                student.setOffice365(falgOffice365);
                ses.update(student);
                tx.commit();
            } catch (Exception exp) {
                tx.rollback();
                System.out.println(exp.toString());
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);*/
    }

    private void batchGenerateEmail(HttpServletRequest request) throws HibernateException {
        /*  //boolean ukr = Boolean.parseBoolean(request.getParameter(PARAMETER_UKRAINIAN));
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));

        Session ses = HibernateUtil.currentSession();
        List<Student> listStudent = new ArrayList<>();
        Criteria cr = ses.createCriteria(Student.class);
        Transaction tx = null;

//                List<Integer> listInvalidEmail = new ArrayList<>();
        cr = ses.createCriteria(Student.class);
        listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                .add(Restrictions.isNull(DB_0.Persone.COLUM_EMAIL))
                .add(Restrictions.isNull(DB_0.Persone.COLUM_PASSWORD))
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        for (Student student : listStudent) {
            List<String> listStudenEmail = null;
            List<Teacher> listTeacherEmail = null;
            for (int i = 1; i <= 4; i++) {
                student.generateEmail(i);
                String email = student.getEmail();
                cr = ses.createCriteria(Student.class);
                listStudenEmail = ses.createCriteria(Student.class)
                        .add(Restrictions.eq(DB_0.Persone.COLUM_EMAIL, email))
                        .list();

                cr = ses.createCriteria(Teacher.class);
                listTeacherEmail = ses.createCriteria(Teacher.class)
                        .add(Restrictions.eq(DB_0.Persone.COLUM_EMAIL, email))
                        .list();

                if (listStudenEmail.isEmpty() && listTeacherEmail.isEmpty()) {
                    break;
                }
                student.clearEmail();
            }
            if (listStudenEmail.isEmpty()) {
                tx = ses.beginTransaction();
                try {
                    student.generatePassword();
                    ses.update(student);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    //listInvalidEmail.add(student.getPer_id());
                    System.out.println(exp.toString());
                } finally {
                }
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);*/
    }

    private void batchGeneratePDF(HttpServletRequest request) throws IOException, HibernateException {
        /*   String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        @SuppressWarnings("UnusedAssignment")
        Criteria cr = ses.createCriteria(Student.class);
        List<Student> listStudent = cr.add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                //                .add(Restrictions.eq(DB.Persone.COLUM_UKRAINIAN, ukr))
                .list();
        HibernateUtil.closeSession();
        //String pathExportDir = "." +  request.getContextPath() + "/export/";

        Date date = new Date();
        String pathExportDir = ROOT_PATH_SITE + "export/Student/";
//        pathExportDir = ROOT_PATH_SITE + date.getTime() + "/";

        // создаем несколько директорий
//        (new File(pathExportDir)).mkdirs();
//        LiveManager.CreateFile(pathExportDir + "Student-" + date.getTime() + ".csv", listStudent);

        List<Integer> listID = new ArrayList<>();
        for (Student student : listStudent) {
            //Student student = (Student) object;

//            String root = pathExportDir
//                    + String.valueOf(student.getFaculty()) + "/"
//                    //+ String.valueOf(teacher.getDepartment()) + "/";
//                    + String.valueOf(student.getSpec()) + "/"
//                    //            + String.valueOf(student.getKurs()) + "/"
//                    + String.valueOf(student.getGroupa());

            Path pathOfGroup = ROOT_PATH_SITE
                    .resolve("export/Student")
                    .resolve(student.getFaculty())
                    .resolve(student.getSpec())
                    .resolve(student.getGroupa());
            
            pathOfGroup.toFile().mkdirs();
            PDFManager.setResurce(ROOT_PATH_SITE.resolve("res"));

            try {
                PDFManager.createPDF_New(student, pathOfGroup, student.isMsdnaa(), student.isOffice365());
                ses = HibernateUtil.currentSession();
                Transaction tr = ses.beginTransaction();
                try {
                    student.setPdf(true);
                    ses.update(student);
                    tr.commit();
                    // 
                    listID.add(student.getId());
                } catch (HibernateException ex) {
                    if (tr != null) {
                        tr.rollback();
                    }
                    Logger.getLogger(StudentServletControler.class.getName()).log(Level.SEVERE, "", ex);                                        
                } finally {
                    HibernateUtil.closeSession();
                }
            } catch (Exception ex) {                
                Logger.getLogger(StudentServletControler.class.getName()).log(Level.SEVERE, "message", ex);
            }
        }

        System.out.println(Arrays.toString(arrayID));
        ses = HibernateUtil.currentSession();

        cr = ses.createCriteria(Student.class);
        listStudent = cr.add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, listID))
                //                .add(Restrictions.eq(DB.Persone.COLUM_UKRAINIAN, ukr))
                .list();
        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);*/
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    } // </editor-fold>

    @Override
    public void init() throws ServletException {
        super.init();
//        ROOT_PATH_SITE = this.getServletContext().getRealPath(File.separator) + "LiveEdu/";
        ROOT_PATH_SITE = Paths.get(this.getServletContext().getRealPath("/")).resolve("LiveEdu");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //String str = request.getAttribute("updata");
        Enumeration<String> paramEnum = request.getParameterNames();
        String action = (request.getParameter("action") != null) ? request.getParameter("action") : ACTION_GENERATE_DEFAULT;
        String forwardPage = "/StudentList_1.jsp";
        switch (action) {
            case ACTION_GENERATE_FIO:
                bachGenerateFioEn(request);
                break;
            case ACTION_GENERATE_EMAIL:
                batchGenerateEmail(request);
                break;
            case ACTION_GENERATE_PDF:
                batchGeneratePDF(request);
                break;
            case ACTION_GENERATE_PASSWORD:
                batchGeneratePassword(request);
                break;
            case ACTION_CLEAR_FIO_AND_EMAIL:
                bachClearFioAndEmail(request);
                break;
            case ACTION_CLEAR_EMAIL:
                bachClearEmail(request);
                break;
            case ACTION_CLEAR_MSDN:
                bachSetMSDN(request, false);
                break;
            case ACTION_SET_MSDN:
                bachSetMSDN(request, true);
                break;
            case ACTION_SET_OFFICE365:
                bachSetOffice365(request, true);
                break;
            case ACTION_CLEAR_OFFICE365:
                bachSetOffice365(request, false);
                break;
            case ACTION_GENERATE_MSDN_IMPORT:
                bachMSDNInport(request);
                forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;

            case ACTION_CREATE_GMAIL:
                bachGmailImport(request);
                //forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;

            case ACTION_GENERATE_OFFICE365_IMPORT:
                bachOffice365Inport(request);
                forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;

            case ACTION_FILTER:
                tableFilter(request);
                break;
            default:
        }

        List<String> listSpeciality = StudentInf.getAllSpecs();
        request.setAttribute(ATTRIBUTE_SPECIALITY_LIST, listSpeciality);
        
        List<String> listGroup = StudentInf.getAllGroups();
        request.setAttribute(ATTRIBUTE_GROUP_LIST, listGroup);

        List<String> listFaculty = StudentInf.getAllFaculty();
        request.setAttribute(ATTRIBUTE_FACULTY_LIST, listFaculty);
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
        dispatcher.forward(request, response);
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    private void tableFilter(HttpServletRequest request) throws NumberFormatException, HibernateException {
        Session ses = HibernateUtil.currentSession();
        @SuppressWarnings("UnusedAssignment")
        List<StudentInf> listStudent = new ArrayList<>();
        Criteria cr = ses.createCriteria(StudentInf.class);

        // Creteria restictions
        /*String str = request.getParameter(PARAMETER_FACULTY);
        if (str != null && !str.equals("")) {
            cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_FACULTY, str));
        }
        
        String paramCourseStart = request.getParameter(PARAMETER_COURSE_START);        
        if (paramCourseStart != null && !paramCourseStart.equals("")) {
                       
            String paramCourseEnd = request.getParameter(PARAMETER_COURSE_END);
            if (paramCourseEnd != null && !paramCourseStart.equals("")) {
                cr = cr.add(Restrictions.ge(DB_0.Persone.COLUM_COURCE, Integer.valueOf(paramCourseStart)));
                cr = cr.add(Restrictions.le(DB_0.Persone.COLUM_COURCE, Integer.valueOf(paramCourseEnd)));
            }else {
                cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_COURCE, Integer.valueOf(paramCourseStart)));
            }
        }*/
        
        String strGroup = request.getParameter(PARAMETER_GROUP);
        if (strGroup != null && !strGroup.equals("")) {
            cr = cr.add(Restrictions.eq(DB.Student.COLUM_GROUP, strGroup));                        
        }

        /*str = request.getParameter(PARAMETER_SPESIALITY);
        if (str != null && !str.equals("")) {
            cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_SPEC, str));
        }
        boolean paramUkr = Boolean.parseBoolean(request.getParameter(PARAMETER_UKRAINIAN));
        cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_UKRAINIAN, paramUkr));
        
        String paramEmailStr = request.getParameter(PARAMETER_EMAIL);
        if (paramEmailStr != null && !paramEmailStr.equals("")) {
            if (Boolean.parseBoolean(paramEmailStr)) {
                cr = cr.add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL));
            } else {
                cr = cr.add(Restrictions.isNull(DB_0.Persone.COLUM_EMAIL));
            }
        }*/
        listStudent = cr.list();

        HibernateUtil.closeSession();
        request.setAttribute(PersonServletControler.ATTRIBUTE_PERSON, listStudent);
    }

    public enum Action {
        GENERATE_DEFAULT, GENERATE_EMAIL, GENERATE_FIO
    }

    public static void TryLast(HttpServletRequest request, String[] arrayID) throws HibernateException {
        Session ses = HibernateUtil.currentSession();
        List<Student> listStudent = new ArrayList<>();

        listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

}
