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
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.*;
import slGal.LiveEdu.ORM.StudentInf;
import slGal.LiveEdu.ORM.PersonInf;
import slGal.LiveEdu.ORM.StuffInf;

/*import slGal.LiveEdu.ORM.Person;
import slGal.LiveEdu.ORM.Student;
import slGal.LiveEdu.ORM.Teacher;
import java.io.*;
import slGal.LiveEdu.DB.DB_0;*/
/**
 *
 * @author Andrey
 */
public class StudentLDAPControler extends HttpServlet {
    
    public static final String ACTION_GENERATE_FIO = "GENERATE_FIO";
    public static final String ACTION_GENERATE_EMAIL = "GENERATE_EMAIL";
    public static final String ACTION_GENERATE_DEFAULT = "ACTION_DEFAULT";
    public static final String ACTION_CLEAR_FIO_AND_EMAIL = "CLEAR_FIO_AND_EMAIL";
    public static final String ACTION_CLEAR_EMAIL = "CLEAR_EMAIL";
    public static final String ACTION_FILTER = "ACTION_FILTER";

    public static final String ACTION_CLEAR_MSDN = "ACTION_CLEAR_MSDN";
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
   
    
    private void bachGenerateFioEn(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNull("person.firstnameEn"))
                .add(Restrictions.isNull("person.lastnameEn"))
                .add(Restrictions.isNull("person.emailCorporate"))
                .add(Restrictions.isNull("person.passCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                student.getPersonInf().translateFIO();
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
    
    private void bachSetMSDN(HttpServletRequest request, boolean flagMsdn) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<PersonInf> listPerson = new ArrayList<>();

        listPerson = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB.Person.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        Person.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (PersonInf person : listPerson) {
            Transaction tx = ses.beginTransaction();
            try {
                
                person.setPersoneServicesConsultations(personeServicesConsultations);
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
        String forwardPage = "/StudentLDAP.jsp";
        switch (action) {
            case ACTION_GENERATE_FIO:
                bachGenerateFioEn(request);
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

            case ACTION_FILTER:
                tableFilter(request);
                break;
            default:
                //forwardPage = "/StudentLDAP.jsp";
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
        Criteria cr = ses.createCriteria(StudentInf.class, "st");

        // Creteria restictions
        String str = request.getParameter(PARAMETER_FACULTY);
        if (str != null && !str.equals("")) {
            cr = cr.createAlias("st.specInf", "sp")
                    .createAlias("sp.facultyInf", "fc")
                    .add(Restrictions.eq("fc.faculty", str));
        }
        
        String strGroup = request.getParameter(PARAMETER_GROUP);
        if (strGroup != null && !strGroup.equals("")) {
            cr = cr.add(Restrictions.eq(DB.Student.COLUM_GROUP, strGroup));
        }

        str = request.getParameter(PARAMETER_SPESIALITY);
        if (str != null && !str.equals("")) {
            cr = cr.add(Restrictions.eq("sp.spec", str));
        }
        
        boolean paramUkr = Boolean.parseBoolean(request.getParameter(PARAMETER_UKRAINIAN));
        cr = cr.add(Restrictions.eq(DB.Student.COLUM_UKRAINIAN, paramUkr));
        
        String paramEmailStr = request.getParameter(PARAMETER_EMAIL);
        if (paramEmailStr != null && !paramEmailStr.equals("")) {
            if (Boolean.parseBoolean(paramEmailStr)) {
                cr = cr.createAlias("st.personInf", "pr")
                        .add(Restrictions.isNotNull("pr.emailCorporate"));
            } else {
                cr = cr.createAlias("st.personInf", "pr")
                        .add(Restrictions.isNull("pr.emailCorporate"));
            }
        }
        listStudent = cr.list();

        HibernateUtil.closeSession();
        request.setAttribute(PersonServletControler.ATTRIBUTE_PERSON, listStudent);
    }

    public enum Action {
        GENERATE_DEFAULT
    }    

}
