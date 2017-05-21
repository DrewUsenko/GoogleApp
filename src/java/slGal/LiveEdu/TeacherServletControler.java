/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import edu.hneu.googleapp.utill.StringExt;
import net.hneu.googleapp.parser.CsvWriterGmail;
import net.hneu.googleapp.parser.CsvWriterMsdn;
import net.hneu.googleapp.parser.CsvWriterOffice365;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.ORM.StuffInf;
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
import slGal.LiveEdu.ORM.StudentInf;

/**
 *
 * @author Andrey
 */
public class TeacherServletControler extends HttpServlet {

    public static enum Action {

        GENERATE_FIO,
        GENERATE_EMAIL,
        GENERATE_DEFAULT
    }
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
        //response.setContentType("text/html;charset=UTF-8");
//        String strRootPathSite = request.getSession().getServletContext().getRealPath(File.separator) + "LiveEdu/";
        Path strRootPathSite = Paths.get(request.getSession().getServletContext().getRealPath("/")).resolve("LiveEdu");
        //String str = request.getAttribute("updata");
        Enumeration<String> paramEnum = request.getParameterNames();
        String action = (request.getParameter("action") != null) ? request.getParameter("action") : ACTION_GENERATE_DEFAULT;

        String forwardPage = "/index.html";
        switch (action) {
            case ACTION_ADD_CSV_TEACHER:
                addTeacher(request);
                forwardPage = "/WEB-INF/jsp/view/teacherCSVFile.jsp";
                break;
            case ACTION_ADD_NEW_TEACHER:
                addTeacher(request);
                forwardPage = "/AddTeacher.jsp";
                break;
            case ACTION_GENERATE_FIO:
                generateFIO(request);
                //generateFIO(request);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_CLEAR_FIO_AND_EMAIL:
                bachClearFioAndEmail(request);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_GENERATE_PASSWORD:
                batchGeneratePassword(request);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_CLEAR_MSDN:
                bachSetMSDN(request, false);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_SET_MSDN:
                bachSetMSDN(request, true);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_CLEAR_OFFICE365:
                bachSetOffice365(request, false);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_SET_OFFICE365:
                bachSetOffice365(request, true);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_CLEAR_EMAIL:
                batchClearEmail(request);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_SET_DISMISS:
                bachSetDismiss(request, Boolean.TRUE);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_CLEAR_DISMISS:
                bachSetDismiss(request, Boolean.FALSE);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_GENERATE_EMAIL:
                batchGenerateEmail(request);
//                generateEmail(request);     
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_GENERATE_PDF:
                generatePDF(strRootPathSite, request);
                forwardPage = "/TeacherList.jsp";
                break;
            case ACTION_GENERATE_GMAIL_IMPORT:
                bachGmailImport(request);
                forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;
            case ACTION_GENERATE_MSDN_IMPORT:
                bachMSDNInport(request);
                forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;
            case ACTION_GENERATE_OFFICE365_IMPORT:
                bachOffice365Import(request, strRootPathSite);
                forwardPage = "/WEB-INF/jsp/view/rawCsv.jsp";
                break;

            case TeacherConst.ACTION_FILTER:
                teacherFilter(request);
                forwardPage = "/TeacherList.jsp";
                break;
            default:
                forwardPage = "/TeacherList.jsp";
        }
        List<String> listFaculty = StuffInf.getAllFaculty();
        request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, listFaculty);

        List<String> listDepartment = StuffInf.getAllDepartment();
        request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, listDepartment);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
        dispatcher.forward(request, response);
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    private void generatePDF(Path strRootPathSite, HttpServletRequest request) throws IOException, HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<StuffInf> listTeacher = ses.createCriteria(StuffInf.class, "sf")
                    .createAlias("st.personInf", "person")
                    .add(Restrictions.isNotNull("person.firstnameEn"))
                    .add(Restrictions.isNotNull("person.lastnameEn"))
                    .add(Restrictions.isNotNull("person.emailCorporate"))
                    .add(Restrictions.isNotNull("person.passCorporate"))
                    .add(Restrictions.in("sf" + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            HibernateUtil.closeSession();

            Path pathExportDir = strRootPathSite.resolve("export/Teacher/");
            for (StuffInf teacher : listTeacher) {
                Path root = pathExportDir
                        .resolve(teacher.getDepartmentInf().getFacultyInf().getFaculty())
                        .resolve(teacher.getDepartmentInf().getDepartment());
                root.toFile().mkdirs();

                PDFManager.setResurce(strRootPathSite.resolve("res"));
                try {
                    PDFManager.createPDF_New(teacher, root, teacher.getPersonInf().isMsdnaa(), teacher.getPersonInf().isOffice365());
                } catch (Exception ex) {
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "message", ex);
                }
            }
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTeacher);
        }
    }

    private void addTeacher(HttpServletRequest request) throws HibernateException {

    }

    private void batchGenerateEmail(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<StuffInf> listTeachers = ses.createCriteria(StuffInf.class, "sf")
                    .createAlias("sf.personInf", "person")
                    .add(Restrictions.isNotNull("person.firstnameEn"))
                    .add(Restrictions.isNotNull("person.lastnameEn"))
                    .add(Restrictions.isNull("person.emailCorporate"))
                    .add(Restrictions.isNull("person.passCorporate"))
                    .add(Restrictions.in("sf." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (StuffInf teacher : listTeachers) {
                List<String> listStudenEmail = null;
                List<StuffInf> listTeacherEmail = null;
                for (int i = 1; i <= 4; i++) {
                    teacher.getPersonInf().generateEmail(i);
                    String email = teacher.getPersonInf().getEmailCorporate();
                    
                    listStudenEmail = ses.createCriteria(StudentInf.class, "st")
                            .createAlias("st.personInf", "person")
                            .add(Restrictions.eq("person.emailCorporate", email))
                            .list();

                    listTeacherEmail = ses.createCriteria(StuffInf.class, "sf")
                            .createAlias("sf.personInf", "person")
                            .add(Restrictions.eq("person.emailCorporate", email))
                            .list();

                    if (listStudenEmail.isEmpty() && listTeacherEmail.isEmpty()) {
                        break;
                    }
                    teacher.getPersonInf().clearEmail();
                }
                if (listTeacherEmail.isEmpty()) {
                    Transaction tx = ses.beginTransaction();
                    try {
                        teacher.getPersonInf().generatePassword();
                        ses.update(teacher);
                        tx.commit();
                    } catch (Exception exp) {
                        tx.rollback();
                        HibernateUtil.closeSession();

                        Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "batchGenerateEmail: " + teacher, exp);

                        ses = HibernateUtil.currentSession();
                    }
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTeachers);
        }
    }

    private void batchGeneratePassword(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<StuffInf> listTeachers = ses.createCriteria(StuffInf.class, "sf")
                    .createAlias("sf.personInf", "person")
                    .add(Restrictions.isNotNull("person.firstnameEn"))
                    .add(Restrictions.isNotNull("person.lastnameEn"))
                    .add(Restrictions.isNotNull("person.emailCorporate"))
                    .add(Restrictions.isNotNull("person.passCorporate"))
                    .add(Restrictions.in("sf" + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (StuffInf teacher : listTeachers) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.getPersonInf().clearPassword();
                    teacher.getPersonInf().generatePassword();

                    teacher.getPersonInf().setPdf(Boolean.FALSE);

                    ses.update(teacher);

                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();

                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "batchGeneratePassword", exp);

                    ses = HibernateUtil.currentSession();
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTeachers);
        }
    }

    private void bachSetMSDN(HttpServletRequest request, boolean msdnFlag) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));
        if (arrayID != null) {                
            Session ses = HibernateUtil.currentSession();            

            List<Teacher> teacherList = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Teacher teacher : teacherList) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.setMsdnaa(msdnFlag);
                    ses.update(teacher);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachSetMSDN", exp);
                    
                    ses =  HibernateUtil.currentSession();     
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, teacherList);
        }*/
    }

    private void bachSetOffice365(HttpServletRequest request, boolean office365Flag) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));
        if (arrayID != null) {                
            Session ses = HibernateUtil.currentSession();            

            List<Teacher> teacherList = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Teacher teacher : teacherList) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.setOffice365(office365Flag);
                    ses.update(teacher);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachSetMSDN", exp);
                    
                    ses =  HibernateUtil.currentSession();     
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, teacherList);
        }*/
    }

    private void bachSetDismiss(HttpServletRequest request, boolean dismissFlag) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));
        if (arrayID != null) {
            Session ses = HibernateUtil.currentSession();

            List<StuffInf> stuffList = ses.createCriteria(StuffInf.class)
                    .add(Restrictions.in(DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (StuffInf stuff : stuffList) {
                Transaction tx = ses.beginTransaction();
                try {
                    stuff.setDismiss(dismissFlag);
                    ses.update(stuff);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();

                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachSetDismiss", exp);

                    ses = HibernateUtil.currentSession();
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, stuffList);
        }
    }

    private void generateEmail(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<Integer> listInvalidEmail = new ArrayList<>();

            List<StuffInf> listTteacher = ses.createCriteria(StuffInf.class, "sf")
                    .createAlias("sf.personInf", "person")
                    .add(Restrictions.isNotNull("person.firstname"))
                    .add(Restrictions.isNotNull("person.lastnameEn"))
                    .add(Restrictions.isNull("person.emailCorporate"))
                    .add(Restrictions.isNull("person.passCorporate"))
                    .add(Restrictions.in("sf" + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (StuffInf item : listTteacher) {
                Transaction tx = ses.beginTransaction();
                try {
                    item.getPersonInf().generateEmail();
                    ses.update(item);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    ses = HibernateUtil.currentSession();
                    listInvalidEmail.add(item.getIdStuff());
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "generateEmail", exp);
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTteacher);
        }
    }

    private void generateFIO(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            //Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();
            List<StuffInf> listTteacher = new ArrayList<>();
            
            Criteria cr = ses.createCriteria(StuffInf.class, "sf");
            cr.createAlias("sf.personInf", "person");
            cr.add(Restrictions.isNull("person.firstnameEn"))
                    .add(Restrictions.isNull("person.lastnameEn"))
                    .add(Restrictions.isNull("person.emailCorporate"))
                    .add(Restrictions.isNull("person.passCorporate"))
                    .add(Restrictions.in("sf." + DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)));
            listTteacher = cr.list();

            //Teacher.nameFileAlphabetic = strRootPathSite + "res/alphabetic.ini";
            PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
            for (StuffInf stuff : listTteacher) {
                Transaction tx = ses.beginTransaction();
                try {
                    stuff.getPersonInf().translateFIO();
                    ses.update(stuff);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, "generateFIO", exp);
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTteacher);
        }
    }

    private void bachClearFioAndEmail(HttpServletRequest request)
            throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<StuffInf> teacherList = ses.createCriteria(StuffInf.class)
                    .add(Restrictions.in(DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            PersonInf.setNameOfAlphabeticFile(ROOT_PATH_SITE);
            for (StuffInf teacher : teacherList) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.getPersonInf().setLastnameEn(null);
                    teacher.getPersonInf().setFirstnameEn(null);
                    teacher.getPersonInf().setPatronymicEn(null);
                    teacher.getPersonInf().setEmailCorporate(null);
                    teacher.getPersonInf().setPassCorporate(null);
                    teacher.getPersonInf().setPdf(Boolean.FALSE);

                    ses.update(teacher);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachClearFioAndEmail", exp);
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, teacherList);
        }
    }

    private void batchClearEmail(HttpServletRequest request)
            throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();
            List<StuffInf> listTeacher = ses.createCriteria(StuffInf.class)
                    .add(Restrictions.in(DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (StuffInf teacher : listTeacher) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.getPersonInf().setEmailCorporate(null);
                    teacher.getPersonInf().setPassCorporate(null);
                    teacher.getPersonInf().setPdf(Boolean.FALSE);

                    ses.update(teacher);

                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();

                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachClearEmail", exp);

                    ses = HibernateUtil.currentSession();
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTeacher);
        }
    }

    private void bachMSDNInport(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<StuffInf> listStuff = ses.createCriteria(StuffInf.class)
                .add(Restrictions.in(DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                .list();

        List<String> msdnInport = CsvWriterMsdn.readWithCsvBeanReader(listStuff);

        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, msdnInport);
    }

    private void bachGmailImport(HttpServletRequest request) throws HibernateException {
        /*String[] arrayID = request.getParameterValues("check");
        
        Logger.getLogger(TeacherServletControler.class.getName())
                .log(Level.INFO, "Ceched ID : {0}", Arrays.toString(arrayID));
        
        if (arrayID != null) {        
            Session ses = HibernateUtil.currentSession();

            List<PersonInf> listStudent = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            List<String> importOffice365 = CsvWriterGmail.readWithCsvBeanReader(listStudent);

            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, importOffice365);
        }*/
    }

    private void bachOffice365Import(HttpServletRequest request, Path strRootPathSite) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");

        Logger.getLogger(TeacherServletControler.class.getName())
                .log(Level.INFO, "Ceched ID : {0}", Arrays.toString(arrayID));

        if (arrayID != null) {
            Session ses = HibernateUtil.currentSession();
            List<StuffInf> listStuff = ses.createCriteria(StuffInf.class)
                    .add(Restrictions.in(DB.Stuff.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            HibernateUtil.closeSession();

            List<String> office365Import = CsvWriterOffice365.writeWithCsvMapWriter(listStuff);

            request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, office365Import);
        }

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
