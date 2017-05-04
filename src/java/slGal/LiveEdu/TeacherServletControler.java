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
import slGal.LiveEdu.ORM.Person;
import slGal.LiveEdu.ORM.Student;
import slGal.LiveEdu.ORM.Teacher;

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
import slGal.LiveEdu.DB.DB_0;
/**
 *
 * @author Andrey
 */
public class    TeacherServletControler extends HttpServlet {

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
//    private static final String = "GenerateFIO";
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
                generateFIO(strRootPathSite, request);
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
        List<String> listFaculty = Teacher.getAllFaculty();
        request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, listFaculty);

        List<String> listDepartment = Teacher.getAllDepartment();
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

            List<Teacher> listTeacher = ses.createCriteria(Teacher.class)
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            HibernateUtil.closeSession();

            Path pathExportDir = strRootPathSite.resolve("export/Teacher/");
            for(Teacher teacher : listTeacher) {
                Path root = pathExportDir
                        .resolve(teacher.getFaculty())
                        .resolve(teacher.getDepartment());
                root.toFile().mkdirs();
                
                PDFManager.setResurce(strRootPathSite.resolve("res"));
                try {
                    PDFManager.createPDF_New(teacher, root, teacher.isMsdnaa(), teacher.isOffice365());
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

            List<Teacher> listTeachers = ses.createCriteria(Teacher.class)
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Teacher teacher : listTeachers) {
                List<String> listStudenEmail = null;
                List<Teacher> listTeacherEmail;
                for (int i = 1; i <= 4; i++) {
                    teacher.generateEmail(i);
                    String email = teacher.getEmail();

                    listStudenEmail = ses.createCriteria(Student.class)
                            .add(Restrictions.eq(DB_0.Persone.COLUM_EMAIL, email))
                            .list();

                    listTeacherEmail = ses.createCriteria(Teacher.class)
                            .add(Restrictions.eq(DB_0.Persone.COLUM_EMAIL, email))
                            .list();

                    if (listStudenEmail.isEmpty() && listTeacherEmail.isEmpty()) {
                        break;
                    }
                    teacher.clearEmail();
                }
                if (listStudenEmail != null && listStudenEmail.isEmpty()) {
                    Transaction tx = ses.beginTransaction();
                    try {
                        teacher.generatePassword();
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

            List<Teacher> listTeachers = ses.createCriteria(Teacher.class)
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
            
            
            for (Teacher teacher : listTeachers) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.clearPassword();
                    teacher.generatePassword();
                    
                    teacher.setPdf(Boolean.FALSE);
                   
                    ses.update(teacher);
                    
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "batchGeneratePassword", exp);
                    
                    ses =  HibernateUtil.currentSession();                    
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTeachers);
        }
    }
        
    
    private void bachSetMSDN(HttpServletRequest request, boolean msdnFlag) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
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
        }
    }
    
    private void bachSetOffice365(HttpServletRequest request, boolean office365Flag) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
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
        }
    }
    
    private void bachSetDismiss(HttpServletRequest request, boolean dismissFlag) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));
        if (arrayID != null) {                
            Session ses = HibernateUtil.currentSession();            

            List<Person> personList = ses.createCriteria(Person.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Person person : personList) {
                Transaction tx = ses.beginTransaction();
                try {
                    person.setDismiss(dismissFlag);
                    ses.update(person);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachSetDismiss", exp);
                    
                    ses =  HibernateUtil.currentSession();     
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, personList);
        }
    }
        
    private void generateEmail(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null){
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<Integer> listInvalidEmail = new ArrayList<>();

            List<Teacher> listTteacher = ses.createCriteria(Teacher.class)
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNotNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Teacher item : listTteacher) {
               Transaction tx = ses.beginTransaction();
                try {                    
                    item.generateEmail();                
                    ses.update(item);                    
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    ses = HibernateUtil.currentSession();
                    listInvalidEmail.add(item.getId());
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "generateEmail", exp);
                } finally {
                }
            }       
            HibernateUtil.closeSession();            
            request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTteacher);
        }
    }

    private void generateFIO(Path strRootPathSite, HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null){
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<Teacher> listTteacher = ses.createCriteria(Teacher.class)
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_EMAIL))
                    .add(Restrictions.isNull(DB_0.Persone.COLUM_PASSWORD))
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            //Teacher.nameFileAlphabetic = strRootPathSite + "res/alphabetic.ini";
            Person.setNameOfAlphabeticFile(strRootPathSite);
            for (Object object : listTteacher) {
                Teacher teacher = (Teacher) object;
                Transaction tx = ses.beginTransaction();
                try {
                    //student = (Student) object;
                    teacher.translateFIO();
                    ses.update(teacher);
                    //student.SaveOrUpdate(ses);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    System.out.println(exp.toString());
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
        if (arrayID != null){
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));
                    
            Session ses = HibernateUtil.currentSession();

            List<Teacher> teacherList = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

    //        Person.setNameOfAlphabeticFile(ROOT_PATH_SITE);
            for (Teacher teacher : teacherList) {
                Transaction tx = ses.beginTransaction();
                try {

                    teacher.setLastnameEn(null);
                    teacher.setFirstnameEn(null);
                    teacher.setPatronymicEn(null);

                    teacher.setEmail(null);
                    teacher.setPassword(null);
                    teacher.setPdf(Boolean.FALSE);
                    teacher.setTypeOfEmail(0);

                    ses.update(teacher);
                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachClearFioAndEmail", exp);
                    
                    ses =  HibernateUtil.currentSession();                    
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
            List<Teacher> listTeacher = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            for (Teacher teacher : listTeacher) {
                Transaction tx = ses.beginTransaction();
                try {
                    teacher.setEmail(null);
                    teacher.setPassword(null);
                    teacher.setPdf(Boolean.FALSE);
                    teacher.setTypeOfEmail(0);

                    ses.update(teacher);

                    tx.commit();
                } catch (Exception exp) {
                    tx.rollback();
                    HibernateUtil.closeSession();
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "bachClearEmail", exp);                                        
                    
                    ses =  HibernateUtil.currentSession();                    
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

        List<Person> listStudent = ses.createCriteria(Teacher.class)
//                .add(Restrictions.isNull(DB.Persone.COLUM_FIRST_NAME_EN))
//                .add(Restrictions.isNull(DB.Persone.COLUM_LAST_NAME_EN))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_EMAIL))
                .add(Restrictions.isNotNull(DB_0.Persone.COLUM_PASSWORD))
                .add(Restrictions.eq(DB_0.Persone.COLUM_MSDNAA, Boolean.TRUE))
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .addOrder(Order.asc(DB_0.Persone.COLUM_EMAIL))
                .list();
        
        List<String> msdnInport = CsvWriterMsdn.readWithCsvBeanReader(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, msdnInport);
    }
    
    private void bachGmailImport(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        
        Logger.getLogger(TeacherServletControler.class.getName())
                .log(Level.INFO, "Ceched ID : {0}", Arrays.toString(arrayID));
        
        if (arrayID != null) {        
            Session ses = HibernateUtil.currentSession();

            List<Person> listStudent = ses.createCriteria(Teacher.class)
                    .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();

            List<String> importOffice365 = CsvWriterGmail.readWithCsvBeanReader(listStudent);

            HibernateUtil.closeSession();
            request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, importOffice365);
        }
    }
        
    private void bachOffice365Import(HttpServletRequest request, Path strRootPathSite) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
            
        Logger.getLogger(TeacherServletControler.class.getName())
          .log(Level.INFO, "Ceched ID : {0}", Arrays.toString(arrayID));
            
        if (arrayID != null) {
            Session ses = HibernateUtil.currentSession();
            List<Person> listStudent = ses.createCriteria(Teacher.class)
            .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
            .list();
            HibernateUtil.closeSession();        
            
            List<String> office365Import = CsvWriterOffice365.writeWithCsvMapWriter(listStudent);
           
            request.setAttribute(TeacherConst.ATTRIBUTE_CSV_IMPORT, office365Import);
        }
        
    }
    
    
    private void teacherFilter(HttpServletRequest request) throws NumberFormatException, HibernateException {
        Session ses = HibernateUtil.currentSession();
        List listTteacher = new ArrayList<Teacher>();
        Criteria cr = ses.createCriteria(Teacher.class);
        
        // restictions
        String faculty = request.getParameter(TeacherConst.PARAMETER_FACULTY);
        if (faculty != null && !faculty.equals("")) {
//            if (str.equals("null")) {
//                cr = cr.add(Restrictions.isNull(DB.Persone.COLUM_FACULTY));
//            } else {
//                int kurs = Integer.parseInt(str);
                cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_FACULTY, faculty));
//            }
        }
        
        String department = request.getParameter(TeacherConst.PARAMETER_DEPARTMENT);
        if (department != null && !department.equals("")) {
//            if (str.equals("null")) {
//                cr = cr.add(Restrictions.isNull(DB.Persone.COLUM_DEPARTMENT));
//            } else {
//                int spec = Integer.parseInt(str);
                cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_DEPARTMENT, department));
//            }
        }
        
        boolean paramDismiss = Boolean.parseBoolean(request.getParameter(TeacherConst.PARAMETER_DISMISS));
        cr = cr.add(Restrictions.eq(DB_0.Persone.COLUM_DISMISS, paramDismiss));
        
        cr = cr.addOrder(Order.asc(DB_0.Persone.COLUM_LAST_NAME)); 
        listTteacher = cr.list();
        HibernateUtil.closeSession();
        request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, listTteacher);
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
