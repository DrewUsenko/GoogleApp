/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.ORM;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvFile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.DB.DB_0;

/**
 *
 * @author Admin
 */
@CsvFile
public class Teacher extends Person {
    @CsvCell(columnName = "")    
    private String faculty;
    private String facultyFull;
    private String department;
    private String departmentFull;
    private String StaffPost;
    private boolean msdnaa;
    private boolean office365;
    private String iin;

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String mFaculty) {
        this.faculty = mFaculty;
    }

    public String getFacultyFull() {
        return facultyFull;
    }

    public void setFacultyFull(String mFacultyFull) {
        this.facultyFull = mFacultyFull;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }
    
    
    public static String nameFileAlphabetic;
    private static final String EMAIL_DOMEN = "@hneu.net";

    public Teacher(String mFaculty, String mFacultyFull, String department, String departmentFull, String StaffPost, boolean msdnaa, String firstname, String lastname, String patronymic) {
        super(firstname, lastname, patronymic);
        this.faculty = mFaculty;
        this.facultyFull = mFacultyFull;
        this.department = department;
        this.departmentFull = departmentFull;
        this.StaffPost = StaffPost;
        this.msdnaa = msdnaa;
    }

    public Teacher(String mFaculty, String mFacultyFull, String department, 
            String departmentFull, String StaffPost, String firstname, 
            String lastname, String patronymic) {
        super(firstname, lastname, patronymic);
        this.faculty = mFaculty;
        this.facultyFull = mFacultyFull;
        this.department = department;
        this.departmentFull = departmentFull;
        this.StaffPost = StaffPost;
//        this.msdnaa = msdnaa;
    }
    
    public Teacher() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentFull() {
        return departmentFull;
    }

    public void setDepartmentFull(String departmentFull) {
        this.departmentFull = departmentFull;
    }

    public String getStaffPost() {
        return StaffPost;
    }

    public void setStaffPost(String StaffPost) {
        this.StaffPost = StaffPost;
    }

    public boolean isMsdnaa() {
        return msdnaa;
    }

    public boolean getMsdnaa() {
        return msdnaa;
    }

    public void setMsdnaa(boolean msdnaa) {
        this.msdnaa = msdnaa;
    }

    public static String getNameFileAlphabetic() {
        return nameFileAlphabetic;
    }

    public static Teacher getById(int id) {
        Session ses = HibernateUtil.currentSession();
        List teacher = new ArrayList<Teacher>();
        Criteria cr = ses.createCriteria(Teacher.class);
        cr = cr.add(Restrictions.eq("per_id", id));
        teacher = cr.list();
        return ((Teacher) teacher.get(0));
    }

    public static List<String> getAllFaculty() {
        Session ses = HibernateUtil.currentSession();
        List<String> listFaculty = new ArrayList<>();
        Criteria cr = ses.createCriteria(Teacher.class);
        cr.setProjection(Projections.distinct(Projections.property("faculty")));
        cr.addOrder(Order.asc("faculty"));
        listFaculty = cr.list();
        HibernateUtil.closeSession();
        return listFaculty;
    }

    public static List<String> getAllDepartment() {
        Session ses = HibernateUtil.currentSession();
        List<String> listFaculty = new ArrayList<>();
        Criteria cr = ses.createCriteria(Teacher.class);
        cr.setProjection(Projections.distinct(Projections.property("department")));
        cr.addOrder(Order.asc("department"));
        listFaculty = cr.list();
        HibernateUtil.closeSession();
        return listFaculty;
    }

    @Override
    public void SaveOrUpdate(Session session) {
        try {
            session.saveOrUpdate(this);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public static void UpdateById(int id, String fName, String lName, String patronymic,
            String email, String password, String pFaculty, String pDepartment, String pDepartmentFull,
            String pStaffPost, boolean msdnaa) throws UnsupportedEncodingException {
        //Init Inhered Persone
        Teacher teacher = getById(id);
        teacher.setFirstname(fName);
        teacher.setLastname(lName);
        teacher.setPatronymic(patronymic);
        teacher.setEmail(email);
        teacher.setPassword(password);

        // Init teacher
        teacher.setDepartment(pDepartment);
        teacher.setDepartmentFull(pDepartmentFull);
        teacher.setFaculty(pFaculty);
        teacher.setFaculty(pFaculty);
        teacher.setStaffPost(pStaffPost);

        teacher.SaveOrUpdate(HibernateUtil.currentSession());
    }

    public boolean isOffice365() {
        return office365;
    }

    public void setOffice365(boolean office365) {
        this.office365 = office365;
    }

    @Override
    public String toString() {
        return "Teacher{" + "ID=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", patronymic=" + patronymic + ", firstnameEn=" + firstnameEn + ", lastnameEn=" + lastnameEn + ", patronymicEn=" + patronymicEn + ", email=" + email + ", liveemail=" + this.getLiveemail() + ", password=" + password + ", ukrainian=" + ukrainian + ", dateLiveReg=" + dateLiveReg + ", pdf=" + pdf + ", typeOfEmail=" + typeOfEmail +
                ", mFaculty=" + faculty + ", mFacultyFull=" + facultyFull + ", department=" + department + ", departmentFull=" + departmentFull + ", StaffPost=" + StaffPost + ", msdnaa=" + msdnaa + '}';
    }
    
}