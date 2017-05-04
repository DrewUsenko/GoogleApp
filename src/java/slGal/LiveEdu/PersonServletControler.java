/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

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
import slGal.LiveEdu.DB.DB_0;*/

/**
 *
 * @author Andrey
 */
public class PersonServletControler extends HttpServlet {
    public static final String ACTION_CLEAR_EMAIL = "CLEAR_EMAIL";

    public static final String ACTION_CLEAR_FIO_AND_EMAIL = "CLEAR_FIO_AND_EMAIL";
//    public static final String ACTION_CLEAR_MSDN = "ACTION_CLEAR_MSDN";
    public static final String ACTION_FILTER = "ACTION_FILTER";
    public static final String ACTION_GENERATE_DEFAULT = "ACTION_DEFAULT";
    public static final String ACTION_GENERATE_EMAIL = "GENERATE_EMAIL";
    //
    public static final String ACTION_GENERATE_FIO = "GENERATE_FIO";
//    public static final String ACTION_GENERATE_GMAIL_IMPORT = "ACTION_GENERATE_GMAIL_IMPORT";
//    public static final String ACTION_GENERATE_MSDN_IMPORT = "ACTION_GENERATE_MSDN_IMPORT";
//    public static final String ACTION_GENERATE_OFFICE365_IMPORT = "ACTION_GENERATE_OFFICE365_IMPORT";
    
    public static final String ACTION_GENERATE_PASSWORD = "ACTION_GENERATE_PASSWORD";   
   
    public static final String ACTION_GENERATE_PDF = "ACTION_GENERATE_PDF";
    public static final String ACTION_SET_MSDN = "ACTION_SET_MSDN";
    public static final String ACTION_SET_OFFICE365 = "ACTION_SET_OFFICE365";    
    public static final String ACTION_CLEAR_OFFICE365 = "ACTION_CLEAR_OFFICE365";    
    public static final String ATTRIBUTE_CSV_IMPORT = "ATTRIBUTE_CSV_IMPORT";
    //
//    public static final String ATTRIBUTE_FACULTY = "ATTRIBUTE_FACULTY";
//    public static final String ATTRIBUTE_FACULTY_LIST = "ATTRIBUTE_FACULTY_LIST";
//    public static final String ATTRIBUTE_GROUP = "ATTRIBUTE_GROUP";
    //
//    public static final String ATTRIBUTE_GROUP_LIST = "ATTRIBUTE_GROUP_LIST";
//    public static final String ATTRIBUTE_SPECIALITY = "ATTRIBUTE_SPECIALITY";
//    public static final String ATTRIBUTE_SPECIALITY_LIST = "ATTRIBUTE_SPECIALITY_LIST";
//    public static final String ATTRIBUTE_COURSE_LIST = "ATTRIBUTE_COURSE_LIST";
    public static final String ATTRIBUTE_PERSON = "ATTRIBUTE_PERSON";
    
    //
//    public static final String ATTRIBUTE_STUDENT = "ATTRIBUTE_STUDENT";
//    public static final String ATTRIBUTE_YEAR = "ATTRIBUTE_YEAR";
    public static final String PARAMETER_EMAIL = "paramEmail";
    //
//    public static final String PARAMETER_FACULTY = "paramFaculty";
//    public static final String PARAMETER_COURSE_START = "paramCourseStart";
//    public static final String PARAMETER_COURSE_END = "paramCourseEnd";
//    public static final String PARAMETER_GROUP = "paramGroup";
//    public static final String PARAMETER_SPESIALITY = "paramSpeciality";
    public static final String PARAMETER_UKRAINIAN = "paramUkranianian";    
//    public static final String PARAMETER_YEAR = "year";

//    private static String ROOT_PATH_SITE;
    private static Path ROOT_PATH_SITE;

    private void batchGeneratePassword(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID != null) {
            Logger.getLogger(TeacherServletControler.class.getName()).log(Level.INFO, "check : {0}", Arrays.toString(arrayID));

            Session ses = HibernateUtil.currentSession();

            List<PersonInf> personList = ses.createCriteria(Student.class)
                    .add(Restrictions.isNotNull(DB.Person.COLUM_FIRST_NAME_EN))
                    .add(Restrictions.isNotNull(DB.Person.COLUM_LAST_NAME_EN))
                    .add(Restrictions.isNotNull(DB.Person.COLUM_EMAIL_CORPORATE))
                    .add(Restrictions.isNotNull(DB.Person.COLUM_PASS_CORPORATE))
                    .add(Restrictions.in(DB.Person.COLUM_ID, StringExt.toInt(arrayID)))
                    .list();
                        
            for (PersonInf person : personList) {
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
                    
                    Logger.getLogger(TeacherServletControler.class.getName()).log(Level.SEVERE, "batchGeneratePassword", exp);
                    
                    ses =  HibernateUtil.currentSession();                    
                } finally {
                }
            }
            HibernateUtil.closeSession();
            request.setAttribute(PersonServletControler.ATTRIBUTE_PERSON, personList);
        }
    }

    private void bachGmailImport(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        //System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<PersonInf> listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.in(DB.Person.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        List<String> msdnInport = CsvWriterGmail.readWithCsvBeanReader(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_CSV_IMPORT, msdnInport);
    }
    
    private void bachMSDNInport(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        //System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<PersonInf> listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.in(DB.Person.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        List<String> msdnInport = CsvWriterMsdn.readWithCsvBeanReader(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_CSV_IMPORT, msdnInport);
    }

    private void bachOffice365Inport(HttpServletRequest request) throws HibernateException {
        String[] arrayID = request.getParameterValues("check");
        if (arrayID == null) {
            return;
        }
        System.out.println(Arrays.toString(arrayID));
        Session ses = HibernateUtil.currentSession();

        List<PersonInf> listStudent = ses.createCriteria(StudentInf.class)
                .add(Restrictions.in(DB.Person.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        List<String> office365Inport = CsvWriterOffice365.writeWithCsvMapWriter(listStudent);

        HibernateUtil.closeSession();
        request.setAttribute(ATTRIBUTE_CSV_IMPORT, office365Inport);
    }
}
