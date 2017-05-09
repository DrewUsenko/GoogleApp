// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:40:45
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Student.java
package slGal.LiveEdu.ORM;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvFile;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.*;
import slGal.LiveEdu.PasswordGenerator;

// Referenced classes of package slGal.LiveEdu:
//            Person, Speciality, Translate, Password
@CsvFile
public class Student extends Person {
    
    @CsvCell (columnName = "Группа.факультет.краткое название")    
    private String faculty;
    
    @CsvCell (columnName = "Группа.факультет.факультет")    
    private String facultyFull;

    @CsvCell (columnName = "курс")
    private int course;
    
    @CsvCell (columnName = "Группа.группа")
    private String groupa;
    
    @CsvCell (columnName = "Группа.специальность.код")
    private String spec;
    
    @CsvCell (columnName = "Группа.специальность.номер спец")
    private String numberSpec;
    
    @CsvCell (columnName = "студ. билет")
    private String card;
    
    @CsvCell (columnName = "Личные данные.код ЄДБО")
    private String edbo;
            
    private String studies;

    
    private boolean msdnaa;
    private boolean office365;
    public static String nameFileAlphabetic;
//  --------------------------------------------    

    public Student() {
        super();
    }

    public Student(String firstname, String lastname, String patronymic, int year,
            String faculty, String group, String spec)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        super(firstname, lastname, patronymic);
        this.faculty = faculty;
        this.course = year;
        this.groupa = group;
        this.spec = spec;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getGroupa() {
        return groupa;
    }

    public String getSpec() {
        return spec;
    }

    public int getCourse() {
        return course;
    }

    public boolean isMsdnaa() {
        return msdnaa;
    }

    public boolean isOffice365() {
        return office365;
    }

    public void setOffice365(boolean office365) {
        this.office365 = office365;
    }

    public Student setGroupa(String group) {
        this.groupa = group;
        return this;
    }

    public String getStudies() {
        return studies;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }
//    public void clearMailAndPassword() {
//        this.password = null;
//        this.email = null;
//    }

    public void generateYear() {
        if (this.firstnameEn == null || this.lastnameEn == null) {
            translateFIO();
        }
        if ((this.password == null) && (this.email == null)) {
            try {
                email = (new StringBuilder()).append(this.firstnameEn).append(".").append(this.lastnameEn).append(Calendar.getInstance().get(Calendar.YEAR)).append(EMAIL_DOMEN).toString();
                    
                PasswordGenerator pswGen = new PasswordGenerator.BuilderMask()
                    .appendMask(PasswordGenerator.SYMBOLS_UPPER_ALPHABETIC_ENGLISH, 1)
                    .appendMask(PasswordGenerator.SYMBOLS_LOWER_ALPHABETIC_ENGLISH, 3)
                    .appendMask(PasswordGenerator.SYMBOLS_DIGIT, 4)
                    .build();
                
                setPassword(pswGen.generate());
                //this.password = password.getPasword();

            } catch (NullPointerException ex) {
                //
                int i = 0;
            }
        }
    }

//    public Student setFaculty(String faculty) {
//        this.faculty = faculty;
//        return this;
//    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;        
    }

    public String getFacultyFull() {
        return facultyFull;
    }

    public void setFacultyFull(String facultyFull) {
        this.facultyFull = facultyFull;
    }

    public String getNumberSpec() {
        return numberSpec;
    }

    public void setNumberSpec(String numberSpec) {
        this.numberSpec = numberSpec;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEdbo() {
        return edbo;
    }

    public void setEdbo(String edbo) {
        this.edbo = edbo;
    }
    

    public Student setCourse(int year) {
        this.course = year;
        return this;
    }

    public Student setSpec(String spec) {
        this.spec = spec;
        return this;
    }

//    public Student setMsdnaa(boolean msdnaa) {
//        this.msdnaa = msdnaa;
//        return this;
//    }
    
    public void setMsdnaa(boolean msdnaa) {
        this.msdnaa = msdnaa;
    }


    public static Student getById(int id) {
        Session ses = HibernateUtil.currentSession();
//        List<Student>  students = new ArrayList<>();
        Criteria cr = ses.createCriteria(Student.class);
        cr = cr.add(Restrictions.eq(DB.Person.COLUM_ID, id));
        List<Student> students = cr.list();
        return ((Student) students.get(0));
    }

    public static void UpdateById(int id, String fName, String lName, String patronymic,
            String email, String pass, String faculty, String groupa, int kurs, String spec) throws UnsupportedEncodingException {

        Student student = getById(id);
        student.setFirstname(fName);
        student.setLastname(lName).setPatronymic(patronymic);
        student.setEmail(email);
        student.setPassword(pass);
        student.setFaculty(faculty);
        student.setGroupa(groupa);
        student.setCourse(kurs);
        student.setSpec(spec);

//        student.setFirstname(GetEncStr(fName));
//        student.setLastname(GetEncStr(lName));
//        student.setPatronymic(GetEncStr(patronymic));
//        student.setEmail(GetEncStr(email));
//        student.setPass(GetEncStr(password));
//        student.setFaculty(GetEncStr(faculty));
//        student.setGroupa(GetEncStr(groupa));
//        student.setKurs(kurs);
//        student.setSpec(spec);

        student.SaveOrUpdate(HibernateUtil.currentSession());
    }

    public static ArrayList<Integer> getAllCources() {
        List<Integer> cources = new ArrayList<>();
        /*Session ses = HibernateUtil.currentSession();
        
        Criteria cr = ses.createCriteria(Student.class);
        cr.setProjection(Projections.distinct(Projections.property(DB.Person.COLUM_COURCE)));
        cr.addOrder(Order.asc(DB.Persone.COLUM_COURCE));
        List<Integer> cources = cr.list();
        HibernateUtil.closeSession();*/
        return (ArrayList<Integer>) cources;
    }

    public static List<Integer> getCourcesBySpec(String spec) {
        List<Integer> cources = new ArrayList<>();
        /*Session ses = HibernateUtil.currentSession();
        
        Criteria cr = ses.createCriteria(Student.class);
        cr.setProjection(Projections.distinct(Projections.property(DB.Persone.COLUM_COURCE)));
        cr = cr.add(Restrictions.eq(DB.Persone.COLUM_SPEC, spec));
        cr.addOrder(Order.asc(DB.Persone.COLUM_COURCE));
        List<Integer> cources = cr.list();
        HibernateUtil.closeSession();*/
        return (ArrayList<Integer>) cources;
    }

    public static ArrayList<String> getAllGroups() {
        Session ses = HibernateUtil.currentSession();
//        List groups = new ArrayList<String>();
        Criteria cr = ses.createCriteria(Student.class);
        cr.setProjection(Projections.distinct(Projections.property("groupa")));
        cr.addOrder(Order.asc("groupa"));
        List<String> groups = cr.list();
        HibernateUtil.closeSession();
        return (ArrayList<String>) groups;
    }
    
    public static ArrayList<String> getAllFaculty() {
        Session ses = HibernateUtil.currentSession();
        List listFaculty = new ArrayList<Student>();
        /*Criteria cr = ses.createCriteria(Student.class);
        cr.setProjection(Projections.distinct(Projections.property(DB.Persone.COLUM_FACULTY)));
        cr.addOrder(Order.asc(DB.Persone.COLUM_FACULTY));
        listFaculty = cr.list();*/
        HibernateUtil.closeSession();
        return (ArrayList<String>) listFaculty;
    }

//    public static ArrayList<String> getAllCourse() {
//        Session ses = HibernateUtil.currentSession();
////        List listFaculty = new ArrayList<Student>();
//        Criteria cr = ses.createCriteria(Student.class);
//        cr.setProjection(Projections.distinct(Projections.property(DB.Persone.COLUM_FACULTY)));        
//        cr.addOrder(Order.asc(DB.Persone.COLUM_COURCE));
//        List<String> listFaculty = cr.list();
//        HibernateUtil.closeSession();
//        return (ArrayList<String>) listFaculty;
//    }

    public static List<String> getAllSpecs() {
        Session ses = null;
        ses = HibernateUtil.currentSession();
        List<String> specs = new ArrayList<>();
        Criteria cr = ses.createCriteria(Student.class);
        cr.setProjection(Projections.distinct(Projections.property("spec")));
        cr.addOrder(Order.asc("spec"));
        specs = cr.list();
        HibernateUtil.closeSession();
        return specs;
    }

    public static void DeleteById(int id) {
        Session session = null;
        session = HibernateUtil.currentSession();

        Transaction tr = session.beginTransaction();
        Student student = getById(id);
        session.delete(student);
        tr.commit();
    }

    @Override
    public void SaveOrUpdate(Session session) {
//        super.SaveOrUpdate();
//        DbManager dbManager = new DbManager();
//        int tspec = spec;
//
//        String updateQuery = "insert into students (Course,Groupa,PerId,SpecId) values (?,?,?,?)";
//        PreparedStatement stat = dbManager.CreatePreparedStatement(updateQuery, 1);
//
//        stat.setInt(2, year);
//        stat.setInt(3, group);
//        stat.setInt(4, personId);
//        stat.setInt(5, tspec);
//        stat.executeUpdate();
//        for(ResultSet res = stat.getGeneratedKeys(); res.next();)
//            studId = res.getInt(1);
//
//        stat.close();
//        Transaction tx = null;
        try {
//            tx = session.beginTransaction();
//            session.update(this);
            session.saveOrUpdate(this);            
//            tx.commit();
        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
            throw e;
        } finally {
//            HibernateUtil.closeSession();
        }
    }

    public static String GetEncStr(String someParam) throws UnsupportedEncodingException {
        if (someParam == null) {
            return "";
        }
        return (new String(someParam.getBytes("ISO-8859-1"), "UTF-8"));
    }

    public static void FakeMethod() {
        Integer i = 0;
    }

    @Override
    public String toString() {
        return "Student{" + "ID=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", patronymic=" + patronymic + ", firstnameEn=" + firstnameEn + ", lastnameEn=" + lastnameEn + ", patronymicEn=" + patronymicEn + ", email=" + email + ", liveemail=" + this.getLiveemail() + ", password=" + password + ", ukrainian=" + ukrainian + ", dateLiveReg=" + dateLiveReg + ", pdf=" + pdf + ", typeOfEmail=" + typeOfEmail
                + "faculty=" + faculty + ", facultyFull=" + facultyFull + ", kurs=" + course + ", groupa=" + groupa + ", spec=" + spec + ", numberSpec=" + numberSpec + ", card=" + card + ", edbo=" + edbo + ", msdnaa=" + msdnaa + '}';
    }
    
    
}
