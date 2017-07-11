/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import edu.hneu.googleapp.utill.StringExt;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.DB;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.ORM.StudentInf;
import slGal.LiveEdu.ORM.PersonInf;

/**
 *
 * @author Andrey
 */
public class StudentLDAPControler extends HttpServlet {

    public static final String ACTION_GENERATE_DEFAULT = "ACTION_DEFAULT";
    public static final String ACTION_FILTER = "ACTION_FILTER";

    public static final String ACTION_CREATE_ACCOUNT = "ACTION_CREATE_ACCOUNT";
    public static final String ACTION_SET_MOODLE = "ACTION_SET_MOODLE";
    public static final String ACTION_SET_GITLAB = "ACTION_SET_GITLAB";
    public static final String ACTION_MAKE_PDF = "ACTION_MAKE_PDF";
    public static final String ACTION_DELETE_ACCOUNT = "ACTION_DELETE_ACCOUNT";
    public static final String ACTION_DOWN_MOODLE = "ACTION_DOWN_MOODLE";
    public static final String ACTION_DOWN_GITLAB = "ACTION_DOWN_GITLAB";

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
    public static final String ATTRIBUTE_STUDENT = "ATTRIBUTE_STUDENT";
    public static final String ATTRIBUTE_YEAR = "ATTRIBUTE_YEAR";
    public static final String PARAMETER_EMAIL = "paramEmail";
    //
    public static final String PARAMETER_FACULTY = "paramFaculty";
    public static final String PARAMETER_COURSE_START = "paramCourseStart";
    public static final String PARAMETER_COURSE_END = "paramCourseEnd";
    public static final String PARAMETER_GROUP = "paramGroup";
    public static final String PARAMETER_SPESIALITY = "paramSpeciality";
    public static final String PARAMETER_UKRAINIAN = "paramUkranianian";
    public static final String PARAMETER_LDAP = "paramLDAP";
    public static final String PARAMETER_YEAR = "year";

    private static Path ROOT_PATH_SITE;

    private void bachCreateAccount(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
            Transaction tx = ses.beginTransaction();
            try {
                if (!student.getPersonInf().getExist()) {
                    LDAP.addUser(student.getPersonInf().getEmailCorporate(), student.getPersonInf().getFirstnameEn(),
                            student.getPersonInf().getLastnameEn());
                    student.getPersonInf().setExist(Boolean.TRUE);
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachDeleteAccount(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachSetMoodle(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachSetGit(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachDownGit(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    private void bachDownMoodle(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();
        List<StudentInf> listStudent = new ArrayList<>();

        Criteria crStudent = ses.createCriteria(StudentInf.class, "st");
        crStudent.createAlias("st.personInf", "person");
        crStudent.add(Restrictions.isNotNull("person.firstnameEn"))
                .add(Restrictions.isNotNull("person.lastnameEn"))
                .add(Restrictions.isNotNull("person.emailCorporate"))
                .add(Restrictions.in("st." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)));

        System.out.println(crStudent);
        listStudent = crStudent.list();

        PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
        for (StudentInf student : listStudent) {
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
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
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
        Path strRootPathSite = Paths.get(request.getSession().getServletContext().getRealPath("/")).resolve("LiveEdu");
        Enumeration<String> paramEnum = request.getParameterNames();
        String action = (request.getParameter("action") != null) ? request.getParameter("action") : ACTION_GENERATE_DEFAULT;
        String forwardPage = "/StudentLDAP.jsp";
        switch (action) {
            case ACTION_CREATE_ACCOUNT:
                bachCreateAccount(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_DELETE_ACCOUNT:
                bachDeleteAccount(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_SET_MOODLE:
                bachSetMoodle(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_SET_GITLAB:
                bachSetGit(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_DOWN_GITLAB:
                bachDownGit(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_DOWN_MOODLE:
                bachDownMoodle(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_MAKE_PDF:
                generatePDF(strRootPathSite, request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            case ACTION_FILTER:
                tableFilter(request);
                forwardPage = "/StudentLDAP.jsp";
                break;
            default:
                forwardPage = "/StudentLDAP.jsp";
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

    private void tableFilter(HttpServletRequest request) throws NumberFormatException, HibernateException {
        Session ses = HibernateUtil.currentSession();
        @SuppressWarnings("UnusedAssignment")
        List<StudentInf> listStudent = new ArrayList<>();
        Criteria cr = ses.createCriteria(StudentInf.class, "st")
                .createAlias("st.personInf", "pr");

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
                cr = cr.add(Restrictions.isNotNull("pr.emailCorporate"));
            } else {
                cr = cr.add(Restrictions.isNull("pr.emailCorporate"));
            }
        }
        listStudent = cr.list();

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_STUDENT, listStudent);
    }

    public enum Action {
        GENERATE_DEFAULT
    }
    
    private void generatePDF(Path strRootPathSite, HttpServletRequest request) throws IOException, HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<StudentInf> listStudent = ses.createCriteria(StudentInf.class, "sf")
                    .createAlias("sf.personInf", "person")
                    .add(Restrictions.isNotNull("person.firstnameEn"))
                    .add(Restrictions.isNotNull("person.lastnameEn"))
                    .add(Restrictions.isNotNull("person.emailCorporate"))
                    .add(Restrictions.isNotNull("person.passCorporate"))
                    .add(Restrictions.in("sf." + DB.Student.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            HibernateUtil.closeSession();

            for (StudentInf student : listStudent) {
                Path root = ROOT_PATH_SITE
                    .resolve("export/Student")
                    .resolve(student.getSpecInf().getFacultyInf().getFaculty())
                    .resolve(student.getSpecInf().getSpec())
                    .resolve(student.getGroup());
                root.toFile().mkdirs();

                PDFManager.setResurce(strRootPathSite.resolve("res"));
                try {
                    PDFManager.createPDF_LDAP(student, root);
                } catch (Exception ex) {
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "message", ex);
                }
            }
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listStudent);
        }
    }

}
