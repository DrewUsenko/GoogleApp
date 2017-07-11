/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import edu.hneu.googleapp.utill.StringExt;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.ORM.PersonInf;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import slGal.LiveEdu.DB.DB;
import slGal.LiveEdu.ORM.StuffInf;

/**
 *
 * @author Andrey
 */
public class TeacherLDAPController extends HttpServlet {

    public static enum Action {

        GENERATE_FIO,
        GENERATE_EMAIL,
        GENERATE_DEFAULT
    }
    public static final String ACTION_CREATE_ACCOUNT = "ACTION_CREATE_ACCOUNT";
    public static final String ACTION_SET_MOODLE = "ACTION_SET_MOODLE";
    public static final String ACTION_SET_GITLAB = "ACTION_SET_GITLAB";
    public static final String ACTION_MAKE_PDF = "ACTION_MAKE_PDF";
    public static final String ACTION_DELETE_ACCOUNT = "ACTION_DELETE_ACCOUNT";
    public static final String ACTION_DOWN_MOODLE = "ACTION_DOWN_MOODLE";
    public static final String ACTION_DOWN_GITLAB = "ACTION_DOWN_GITLAB";
    
    public static final String ACTION_CHANGE = "ACTION_CHANGE";
    public static final String ACTION_GENERATE_FIO = "GENERATE_FIO";
    public static final String ACTION_GENERATE_EMAIL = "GENERATE_EMAIL";
    public static final String ACTION_GENERATE_DEFAULT = "ACTION_DEFAULT";

    public static final String ACTION_GENERATE_PDF = "ACTION_DEFAULT_GENERATE_PDF";
    public static final String ACTION_GENERATE_PASSWORD = "ACTION_GENERATE_PASSWORD";

    public static final String ACTION_CLEAR_MSDN = "ACTION_CLEAR_MSDN";
    public static final String ACTION_SET_MSDN = "ACTION_SET_MSDN";

    public static final String ACTION_CLEAR_OFFICE365 = "ACTION_CLEAR_OFFICE365";
    public static final String ACTION_SET_OFFICE365 = "ACTION_SET_OFFICE365";

    public static final String ACTION_GENERATE_MSDN_IMPORT = "ACTION_GENERATE_MSDN_IMPORT";
    public static final String ACTION_GENERATE_GMAIL_IMPORT = "ACTION_GENERATE_GMAIL_IMPORT";
    public static final String ACTION_GENERATE_OFFICE365_IMPORT = "ACTION_GENERATE_OFFICE365_IMPORT";

    public static final String ACTION_ADD_NEW_TEACHER = "ACTION_ADD_NEW_TEACHER";
    public static final String ACTION_ADD_CSV_TEACHER = "ACTION_ADD_CSV_TEACHER";

    public static final String ACTION_CLEAR_FIO_AND_EMAIL = "CLEAR_FIO_AND_EMAIL";
    public static final String ACTION_CLEAR_EMAIL = "CLEAR_EMAIL";

    public static final String ACTION_SET_DISMISS = "ACTION_SET_DISMISS";
    public static final String ACTION_CLEAR_DISMISS = "ACTION_CLEAR_DISMISS";

    private static Path ROOT_PATH_SITE;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        Path strRootPathSite = Paths.get(request.getSession().getServletContext().getRealPath("/")).resolve("LiveEdu");
        Enumeration<String> paramEnum = request.getParameterNames();
        String action = (request.getParameter("action") != null) ? request.getParameter("action") : ACTION_GENERATE_DEFAULT;

        String forwardPage = "/TeacherLDAP.html";
        switch (action) {
            case ACTION_CREATE_ACCOUNT:
                bachCreateAccount(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case ACTION_DELETE_ACCOUNT:
                bachDeleteAccount(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case ACTION_SET_MOODLE:
                bachSetMoodle(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case ACTION_SET_GITLAB:
                bachSetGit(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case ACTION_DOWN_GITLAB:
                bachDownGit(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case ACTION_DOWN_MOODLE:
                bachDownMoodle(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            case TeacherConst.ACTION_FILTER:
                teacherFilter(request);
                forwardPage = "/TeacherLDAP.jsp";
                break;
            default:
                forwardPage = "/TeacherLDAP.jsp";
        }
        List<String> listFaculty = StuffInf.getAllFaculty();
        request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, listFaculty);

        List<String> listDepartment = StuffInf.getAllDepartment();
        request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, listDepartment);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
        dispatcher.forward(request, response);
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    private void bachCreateAccount(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStuff = new ArrayList<>();

        Criteria crStuff = ses.createCriteria(StuffInf.class, "st");
        crStuff.createAlias("st.personInf", "person");
        crStuff.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStuff);
        listStuff = crStuff.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf stuff : listStuff) {
            Transaction tx = ses.beginTransaction();
            try {
                if (!stuff.getPersonInf().getExist()) {
                    LDAP.addUser(stuff.getPersonInf().getEmailCorporate(), stuff.getPersonInf().getFirstnameEn(),
                            stuff.getPersonInf().getLastnameEn());
                    stuff.getPersonInf().setExist(Boolean.TRUE);
                    ses.update(stuff);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStuff);
    }

    private void bachDeleteAccount(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StuffInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if (student.getPersonInf().getExist()) {
                    LDAP.DeleteUser(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn());
                    student.getPersonInf().setExist(Boolean.FALSE);
                    student.getPersonInf().setMoodle(Boolean.FALSE);
                    student.getPersonInf().setGitlab(Boolean.FALSE);
                    ses.update(student);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
    }

    private void bachSetMoodle(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StuffInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if ((student.getPersonInf().getExist() && student.getPersonInf().getGitlab())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "gitmood");
                    student.getPersonInf().setMoodle(Boolean.TRUE);
                    ses.update(student);
                    tx.commit();
                } else if ((student.getPersonInf().getExist() && !student.getPersonInf().getGitlab())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "moodle");
                    student.getPersonInf().setMoodle(Boolean.TRUE);
                    ses.update(student);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
    }

    private void bachSetGit(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StuffInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if ((student.getPersonInf().getExist() && student.getPersonInf().getMoodle())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "gitmood");
                    student.getPersonInf().setGitlab(Boolean.TRUE);
                    ses.update(student);
                    tx.commit();
                } else if ((student.getPersonInf().getExist() && !student.getPersonInf().getMoodle())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "gitlab");
                    student.getPersonInf().setGitlab(Boolean.TRUE);
                    ses.update(student);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
    }

    private void bachDownGit(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StuffInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if ((student.getPersonInf().getExist() && student.getPersonInf().getMoodle() && student.getPersonInf().getGitlab())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "moodle");
                    student.getPersonInf().setGitlab(Boolean.FALSE);
                    ses.update(student);
                    tx.commit();
                } else if ((student.getPersonInf().getExist() && student.getPersonInf().getGitlab())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "default");
                    student.getPersonInf().setGitlab(Boolean.FALSE);
                    ses.update(student);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
    }

    private void bachDownMoodle(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StuffInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StuffInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StuffInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if ((student.getPersonInf().getExist() && student.getPersonInf().getMoodle() && student.getPersonInf().getGitlab())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "gitlab");
                    student.getPersonInf().setMoodle(Boolean.FALSE);
                    ses.update(student);
                    tx.commit();
                } else if ((student.getPersonInf().getExist() && student.getPersonInf().getMoodle())) {
                    LDAP.Modify(student.getPersonInf().getFirstnameEn(), student.getPersonInf().getLastnameEn(), "default");
                    student.getPersonInf().setMoodle(Boolean.FALSE);
                    ses.update(student);
                    tx.commit();
                }
            } catch (Exception exp) {
                tx.rollback();
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "bachCreateAccount", exp);
            } finally {
            }
        }
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
    }

    private void teacherFilter(HttpServletRequest request) throws NumberFormatException, HibernateException {
        Session ses = HibernateUtil.currentSession();
        List listTteacher = new ArrayList<StuffInf>();
        Criteria cr = ses.createCriteria(StuffInf.class, "sf");
        cr.createAlias("sf.departmentInf", "dep");
        cr.createAlias("dep.facultyInf", "fac");
        cr.createAlias("sf.personInf", "pr");

        // restictions
        String faculty = request.getParameter(TeacherConst.PARAMETER_FACULTY);
        if (faculty != null && !faculty.equals("")) {
            cr = cr.add(Restrictions.eq("fac.faculty", faculty));
        }

        String department = request.getParameter(TeacherConst.PARAMETER_DEPARTMENT);
        if (department != null && !department.equals("")) {
            cr = cr.add(Restrictions.eq("dep.department", department));
        }

        boolean paramDismiss = Boolean.parseBoolean(request.getParameter(TeacherConst.PARAMETER_DISMISS));
        cr = cr.add(Restrictions.eq(DB.Stuff.COLUM_DISMISS, paramDismiss));

        cr = cr.addOrder(Order.asc("pr.lastname"));
        listTteacher = cr.list();
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTteacher);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        ROOT_PATH_SITE = Paths.get(this.getServletContext().getRealPath("/")).resolve("LiveEdu");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
