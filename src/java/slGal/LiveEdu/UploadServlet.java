/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import edu.hneu.googleapp.utill.StringExt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hneu.googleapp.parser.CsvEntity;
import net.hneu.googleapp.parser.CsvReaderStudent;
import net.hneu.googleapp.parser.CsvReaderTeacher;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.ORM.Student;
import slGal.LiveEdu.ORM.Teacher;
import static slGal.LiveEdu.PersonServletControler.ACTION_GENERATE_DEFAULT;
import static slGal.LiveEdu.StudentServletControler.ATTRIBUTE_STUDENT;
import slGal.LiveEdu.bean.CsvStudentBean;
import slGal.LiveEdu.bean.CsvStudentBeanToStudentEntityConverter;
import slGal.LiveEdu.bean.CsvStudentToCsvStudentBeanConverter;
import slGal.LiveEdu.bean.CsvUploadBean;
import slGal.LiveEdu.DB.DB_0;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "UploadServlet", 
        urlPatterns = {"/UploadServlet"})
public class UploadServlet extends HttpServlet {
    public static String ATTRIBUTE_CSV_HEADER = "ATTRIBUTE_CSV_HEADER";
    public static String ATTRIBUTE_CSV_UPLOAD_BEAN = "ATTRIBUTE_CSV_UPLOAD_BEAN";
    public static String ATTRIBUTE_STUDENT = "ATTRIBUTE_STUDENT";
    public static final String ACTION_REJECT_CSV_DATA = "ACTION_REJECT_CSV_DATA";
    public static final String ACTION_ACCEPT_CSV_DATA = "ACTION_ACCEPT_CSV_DATA";
    public static final String ACTION_ACCEPT_GO_BACK = "ACTION_ACCEPT_GO_BACK";

    private static Path ROOT_PATH_SITE;
    private static final int maxFileSize = 5000 * 1024;
    private static final int maxMemSize = 5000 * 1024;

    public static final String PARAM_IN_ATRIBUTE = "PARAM";
    public static final String PARAMERET_ACTION = "action";
    public static final String ACTION_UPLOAD_STUDENT_CSV = "ACTION_UPLOAD_STUDENT_CSV";
    public static final String ACTION_UPLOAD_TEACHER_CSV = "ACTION_UPLOAD_TEACHER_CSV";
    public static final String ACTION_DEFAULT = "ACTION_DEFAULT";
    //
    public static final String PARAMETER_UPLOAD_STUDENTS = "parameterUploadStudent";
    public static final String PARAMETER_VALUE_STUDENT_CSV = "paramValueStudentCsv";
    public static final String PARAMETER_ERROR_STUDENT_CSV = "paramErrorStudentCsv";
    public static final String PARAMETER_FILE = "paramFile";
    
    public static String from = "";

        
    @Override
    public void init() throws ServletException {
        super.init();
        ROOT_PATH_SITE = Paths.get(this.getServletContext().getRealPath("/"));// + "LiveEdu/";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String contentType = request.getContentType();
        if (contentType.contains("multipart/form-data")) {
            uploadFile(request);
        }

//        parameterToAtribute(request);
//        String action = (request.getParameter(PARAMERET_ACTION) != null) ? request.getParameter(PARAMERET_ACTION) : ACTION_DEFAULT;
        String action = getParam(request, PARAMERET_ACTION, ACTION_DEFAULT);
        String forward = "/index.html";
        switch (action) {
            case ACTION_UPLOAD_STUDENT_CSV:
                validateStudent(request);
//                uploadStudent(request);
                forward = "/StudentList_1.jsp";

                List<String> listSpeciality = Student.getAllSpecs();
                request.setAttribute(StudentServletControler.ATTRIBUTE_SPECIALITY_LIST, listSpeciality);

                List<String> listGroup = Student.getAllGroups();
                request.setAttribute(StudentServletControler.ATTRIBUTE_GROUP_LIST, listGroup);

                List<String> listFaculty = Student.getAllFaculty();
                request.setAttribute(StudentServletControler.ATTRIBUTE_FACULTY_LIST, listFaculty);
                response.setContentType("text/html;charset=UTF-8");
                                
                break;
            case ACTION_UPLOAD_TEACHER_CSV:
                uploadTeachers(request);
                forward = "/TeacherList.jsp";

                request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, Teacher.getAllDepartment());
                request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, Teacher.getAllFaculty());
                
                response.setContentType("text/html;charset=UTF-8");
                break;
            default:
        }
        getServletContext().getRequestDispatcher(forward).forward(request, response);
    }

    private <T> T getParam(HttpServletRequest request, String name, T def) {
        Logger.getLogger(UploadServlet.class.getName()).log(Level.INFO, "param => name: {0}, def: {1}", 
                new String[]{name, def.toString()});
        Map<String, Object> map = (Map<String, Object>) request.getAttribute(PARAM_IN_ATRIBUTE);
        Logger.getLogger(UploadServlet.class.getName()).log(Level.INFO, "map: {0}, map.get(name): {1}", 
                new String[]{map.toString(), map.get(name).toString()});
        if (map.get(name) != null) {
            return (T) map.get(name);
        } else {
            return def;
        }
//            return (((Map<String, Object>)request.getAttribute(PARAM_IN_ATRIBUTE)).get(name) != null) ? def.getClass().cast(request.getAttribute(PARAM_IN_ATRIBUTE)) : def;        
    }

    private <T> T getParam(HttpServletRequest request, String name, Class<T> clazz) {
        Map<String, Object> map = (Map<String, Object>) request.getAttribute(PARAM_IN_ATRIBUTE);
        if (map.containsKey(name)) {
            return clazz.cast(map.get(name));
        } else {
            return null;
        }
    }

    private void validateStudent(HttpServletRequest request) {
//        File csvFile = uploadFile(request);         
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse Student
            CsvReaderStudent parserCsvStudent = new CsvReaderStudent(csvFile);
            List<CsvReaderStudent.CSVBean> studentList = new ArrayList<>();
            try {
//                studentList = parserCsvStudent.readWithCsvBeanReader(csvFile);
                studentList = parserCsvStudent.readWithCsvBeanReaderValidate(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            if (studentList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<Student> listStud = new ArrayList<>();
                for (CsvReaderStudent.CSVBean csvBean : studentList){                    
                    if (!csvBean.getList().isEmpty()) {                        
                        //SuperCsvCellProcessorException ex = csvBean.getList().get(0);
                    
                        StringBuilder stringBuilder = new StringBuilder("row "
                            + csvBean.getList().get(0).getCsvContext().getRowNumber() + ":");

                        csvBean.getList().stream().forEach((e) -> {
                        stringBuilder.append(e);                      
                    });
                    
                    Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO, stringBuilder.toString());
                }                   
//                Session ses = HibernateUtil.currentSession();
//                try {
//                for (Student st : studentList) {
//                    Transaction tx = ses.beginTransaction();
//                    try {
////                        Integer id = (Integer) ses.save(st);
//                        ses.save(st);
////                        st.setId(id);
//                        tx.commit();
//                    } catch (HibernateException ex) {
//                        if (tx != null)
//                            tx.rollback();
//                        listInvalidInport.add(st);
//                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
//                        throw ex;
//                    }                                       
//                }}catch(HibernateException ex){
//                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                finally {
//                        HibernateUtil.closeSession();
//                    }
//                
//                if (!listInvalidInport.isEmpty()) {
//                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, listInvalidInport);
//                }

                //request.setAttribute(PARAMETER_VALUE_STUDENT_CSV, parserCsvStudent.getErrorList());
                request.setAttribute(StudentServletControler.ATTRIBUTE_STUDENT, studentList);
                }
            }
        }
    }

    private void validateStudent1(HttpServletRequest request) {
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            try {
                CsvReaderStudent parserCsvStudent = CsvReaderStudent.validate(csvFile);                

                CsvUploadBean csvUploadBean = new CsvUploadBean();
                csvUploadBean.setTitleOfPage(String.format("%s: %s", "Импорт студетов из CSV", csvFile.getName()));
                csvUploadBean.setNumberValideRecord(parserCsvStudent.countValide());
                csvUploadBean.setHeadOfTable(parserCsvStudent.getHeader());                
                
                CsvStudentToCsvStudentBeanConverter csvStudentToCsvStudentBeanConverter = new CsvStudentToCsvStudentBeanConverter();
                csvUploadBean.setBodyOfTable(parserCsvStudent.getStudentList()
                            .stream()
                            .map(s -> csvStudentToCsvStudentBeanConverter.convert((CsvReaderStudent.CsvStudent) s))
                            .collect(Collectors.toList()));
                csvUploadBean.setErrorOfRecords(parserCsvStudent.getMessageErrorList());
                
                                Session ses = HibernateUtil.currentSession();
                try{
                    final int[] idx = { 0 };
                    csvUploadBean.getBodyOfTable()
                            //.stream()
                            //.filter(csvStudentBean -> csvStudentBean instanceof CsvStudentBean)
                            //.map(studentBean -> (CsvStudentBean)studentBean)
                            .forEach(x -> {                        
                        Student student = (Student) ses.createCriteria(Student.class)
                            .add(Restrictions.eq(DB_0.Persone.COLUM_EDBO, x.getEdbo()))
                            .uniqueResult();
                        if (student != null)
                            csvUploadBean.getErrorOfRecords().get(idx[0]).add("Exist in DB");                        
                        idx[0]++;
                    });
                }finally{                
                    HibernateUtil.closeSession();
                }
                
                request.getSession().setAttribute(UploadServlet.ATTRIBUTE_CSV_UPLOAD_BEAN, csvUploadBean);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }                      
        }
    }
    
    
    private void csvToDB(HttpServletRequest request){        
        CsvUploadBean csvUploadBean = (CsvUploadBean) request.getSession().getAttribute(UploadServlet.ATTRIBUTE_CSV_UPLOAD_BEAN);
        CsvStudentBeanToStudentEntityConverter csvStudentBeanToStudentEntityConverter = new CsvStudentBeanToStudentEntityConverter();
        
        
        Session ses1 = HibernateUtil.currentSession();
        String queryFrom = "SELECT id_person FROM   persons WHERE  id_person=(SELECT MAX(id_person) FROM persons)";
        List<String> list = null;
        from = ses1.createSQLQuery(queryFrom).list().toString();
        HibernateUtil.closeSession();
        
        
        Session ses = HibernateUtil.currentSession();       
        
        final int[] idx = { 0 };
        csvUploadBean.getBodyOfTable().forEach((CsvStudentBean x)->{
            Student student = (Student) ses.createCriteria(Student.class)
                .add(Restrictions.eq(DB_0.Persone.COLUM_EDBO, x.getEdbo()))
                .uniqueResult();
            if (student != null)
                csvUploadBean.getErrorOfRecords().get(idx[0]).add("Exist in DB");
            else {
                try{                                        
                    ses.beginTransaction();   
                    ses.saveOrUpdate(csvStudentBeanToStudentEntityConverter.convert(x));
                    ses.getTransaction().commit();
                    
                    csvUploadBean.getErrorOfRecords().get(idx[0]).add("Added to DB");
                } catch (HibernateException ex){
                    if (ses.getTransaction() != null){
                         ses.getTransaction().rollback();
                    }                                        
                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            idx[0]++;
        });               
        
        HibernateUtil.closeSession();      
        request.getSession().setAttribute(UploadServlet.ATTRIBUTE_CSV_UPLOAD_BEAN, csvUploadBean);
        
////        listStudent = ses.createCriteria(Student.class)
////                .add(Restrictions.in(DB.Persone.COLUM_ID, StringExt.toInt(arrayID)))
////                .list();
//
//        if (csvUploadBean.getstudentList.isEmpty()) {
//            request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
//        } else {
////                Session ses = HibernateUtil.currentSession();
////                try {
////                for (Student st : studentList) {
////                    Transaction tx = ses.beginTransaction();
////                    try {
//////                        Integer id = (Integer) ses.save(st);
////                        ses.save(st);
//////                        st.setId(id);
////                        tx.commit();
////                    } catch (HibernateException ex) {
////                        if (tx != null)
////                            tx.rollback();
////                        listInvalidInport.add(st);
////                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
////                        throw ex;
////                    }                                       
////                }}catch(HibernateException ex){
////                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
////                        }
////                finally {
////                        HibernateUtil.closeSession();
////                    }
////                
////                if (!listInvalidInport.isEmpty()) {
////                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, listInvalidInport);
////                }
//
//                //request.setAttribute(PARAMETER_VALUE_STUDENT_CSV, parserCsvStudent.getErrorList());
//                //request.setAttribute(StudentServletControler.ATTRIBUTE_STUDENT, studentList);
//                }
//            }
        }
    
    
    
    private void uploadStudent(HttpServletRequest request) {
//        File csvFile = uploadFile(request);         
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse Student
            CsvReaderStudent parserCsvStudent = new CsvReaderStudent(csvFile);
            List<Student> studentList = new ArrayList<>(0);
            try {
                studentList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (studentList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<Student> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                try {
                for (Student st : studentList) {
                    Transaction tx = ses.beginTransaction();
                    try {
//                        Integer id = (Integer) ses.save(st);
                        ses.save(st);
//                        st.setId(id);
                        tx.commit();
                    } catch (HibernateException ex) {
                        if (tx != null)
                            tx.rollback();
                        listInvalidInport.add(st);
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                        throw ex;
                    }                                       
                }}catch(HibernateException ex){
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                finally {
                        HibernateUtil.closeSession();
                    }
                
                if (!listInvalidInport.isEmpty()) {
                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, listInvalidInport);
                }

                //request.setAttribute(PARAMETER_VALUE_STUDENT_CSV, parserCsvStudent.getErrorList());
                request.setAttribute(StudentServletControler.ATTRIBUTE_STUDENT, studentList);
            }
        }
    }

    private void validateTeachers(HttpServletRequest request) {       
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse
            CsvReaderTeacher parserCsvStudent = new CsvReaderTeacher(csvFile);
            List<Teacher> teacherList = new ArrayList<>(0);
            try {
                teacherList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (teacherList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<Teacher> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                
                Transaction tx;
                
                for (ListIterator<Teacher> iterator = teacherList.listIterator(); iterator.hasNext();){
                    Teacher item = iterator.next();
                    Teacher teacher = (Teacher)ses.createCriteria(Teacher.class)
                            .add(Restrictions.eq(DB_0.Persone.COLUM_IIN, item.getIin()))
                            //.setProjection(Projections.distinct(Projections.property(DB.Persone.COLUM_IIN)))
                            //.addOrder(Order.asc(DB.Persone.COLUM_IIN))
                            .uniqueResult();                

                    if (teacher != null){
                                               
                        if (item.getFirstname() != null)
                            teacher.setFirstname(item.getFirstname());
                        
                        if (item.getDepartment() != null)
                            teacher.setDepartment(item.getDepartment());
                        
                        if (item.getDepartmentFull() != null)
                            teacher.setDepartmentFull(item.getDepartmentFull());
                        
                        if (item.getFaculty() != null)
                            teacher.setFaculty(item.getFaculty());
                        
                        if (item.getFacultyFull() != null)
                            teacher.setFacultyFull(item.getFacultyFull());
                        
                        if (item.getIin()!=null)
                            teacher.setIin(item.getIin());
                        
                        if (item.getStaffPost() != null)
                            teacher.setStaffPost(item.getStaffPost());  
                        
                        iterator.set(teacher);
                        
                        item = teacher;
                    }
                    
                    tx = ses.beginTransaction();
                    try {                        
                        ses.saveOrUpdate(item);
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.INFO, item.toString());    
                        tx.commit();
                    } catch (HibernateException ex) {
                        tx.rollback();

                        listInvalidInport.add(item);
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);

                        HibernateUtil.closeSession();
                        ses = HibernateUtil.currentSession();                                                
                    } finally {                        
                    }                                        
                }
                HibernateUtil.closeSession();
                if (!listInvalidInport.isEmpty()) {
                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, listInvalidInport);
                }
                
                request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, teacherList);
            }
        }
    }
    
      private void uploadTeachers(HttpServletRequest request) {       
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse Student
            CsvReaderTeacher parserCsvStudent = new CsvReaderTeacher(csvFile);
            List<Teacher> teacherList = new ArrayList<>(0);
            try {
                teacherList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (teacherList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<Teacher> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                
                Transaction tx;
                
                for (ListIterator<Teacher> iterator = teacherList.listIterator(); iterator.hasNext();){
                    Teacher item = iterator.next();
                    Teacher teacher = (Teacher)ses.createCriteria(Teacher.class)
                            .add(Restrictions.eq(DB_0.Persone.COLUM_IIN, item.getIin()))
                            //.setProjection(Projections.distinct(Projections.property(DB.Persone.COLUM_IIN)))
                            //.addOrder(Order.asc(DB.Persone.COLUM_IIN))
                            .uniqueResult();                

                    if (teacher != null){
                                               
                        if (item.getFirstname() != null)
                            teacher.setFirstname(item.getFirstname());
                        
                        if (item.getDepartment() != null)
                            teacher.setDepartment(item.getDepartment());
                        
                        if (item.getDepartmentFull() != null)
                            teacher.setDepartmentFull(item.getDepartmentFull());
                        
                        if (item.getFaculty() != null)
                            teacher.setFaculty(item.getFaculty());
                        
                        if (item.getFacultyFull() != null)
                            teacher.setFacultyFull(item.getFacultyFull());
                        
                        if (item.getIin()!=null)
                            teacher.setIin(item.getIin());
                        
                        if (item.getStaffPost() != null)
                            teacher.setStaffPost(item.getStaffPost());  
                        
                        iterator.set(teacher);
                        
                        item = teacher;
                    }
                    
                    tx = ses.beginTransaction();
                    try {                        
                        ses.saveOrUpdate(item);
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.INFO, item.toString());    
                        tx.commit();
                    } catch (HibernateException ex) {
                        tx.rollback();

                        listInvalidInport.add(item);
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);

                        HibernateUtil.closeSession();
                        ses = HibernateUtil.currentSession();                                                
                    } finally {                        
                    }                                        
                }
                HibernateUtil.closeSession();
                if (!listInvalidInport.isEmpty()) {
                    Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, listInvalidInport);
                }
                
                request.setAttribute(TeacherConst.ATTRIBUTE_TEACHERS, teacherList);
            }
        }
    }
    
    private File uploadFile(HttpServletRequest request) {
        File file = null;
        ServletContext context = getServletContext();
        Path relavantPath = Paths.get(context.getInitParameter("file-upload"));
        Path tempDir = ROOT_PATH_SITE.resolve(relavantPath);
        Map<String, Object> parameterRequest = new HashMap<>();
//        try {                  
        // Verify the content type
        String contentType = request.getContentType();
        if ((contentType.contains("multipart/form-data"))) {

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // maximum size that will be stored in memory
            factory.setSizeThreshold(maxMemSize);
            // Location to save data that is larger than maxMemSize.                
            factory.setRepository(tempDir.toFile());
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // maximum file size to be uploaded.
            upload.setSizeMax(maxFileSize);
            try {
                // Parse the request to get file items.
                List<FileItem> fileItems = upload.parseRequest(request);

                for (FileItem fileItem : fileItems) {
                    // Process the uploaded file items
//                Iterator i = fileItems.iterator();
                    if (fileItem.isFormField()) {
                        // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                        String fieldName = fileItem.getFieldName();
                        String fieldValue = fileItem.getString();
                        parameterRequest.put(fieldName, fieldValue);
                    } else {
                        // Process form file field (input type="file").
                        String fieldName = fileItem.getFieldName();
                        String fileName = FilenameUtils.getName(fileItem.getName());
//                            InputStream fileContent = fileItem.getInputStream();
                        boolean isInMemory = fileItem.isInMemory();
                        long sizeInBytes = fileItem.getSize();

                        // ... (do your job here)   
                        // Write the file
                        file = tempDir.resolve(fileName).toFile();
                        fileItem.write(file);
                        parameterRequest.put(PARAMETER_FILE, file);
                    }
                }
                request.setAttribute(PARAM_IN_ATRIBUTE, parameterRequest);
            } catch (FileUploadException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        }
        return file;
    }

//    private void parameterToAtribute(HttpServletRequest request) {
////        String contentType = request.getContentType();
//        if ((request.getContentType().contains("multipart/form-data"))) {
//
////            DiskFileItemFactory factory = new DiskFileItemFactory();            
////            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
//            try {
//                // Create a new file upload handler
//                // Parse the request to get file items.
//                List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
//
//                for (FileItem fileItem : fileItems) {
//                    // Process the uploaded file items
//                    if (fileItem.isFormField()) {
//                        // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
//                        String fieldName = fileItem.getFieldName();
//                        String fieldValue = fileItem.getString();
//                        request.setAttribute(fieldName, fieldValue);
//                        // ... (do your job here)
//                    }
//                }else {
//                        // Process form file field (input type="file").
//                        String fieldName = fileItem.getFieldName();
//                        String fileName = FilenameUtils.getName(fileItem.getName());
////                            InputStream fileContent = fileItem.getInputStream();
//                        boolean isInMemory = fileItem.isInMemory();
//                        long sizeInBytes = fileItem.getSize();
//
//                        // ... (do your job here)   
//                        // Write the file
//                        file = tempDir.resolve(fileName).toFile();
//                        fileItem.write(file);
//                    }
//            } catch (FileUploadException ex) {
//                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (Exception ex) {
//                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String forwardPath = "StudentList_1.jsp";
                
        String action = (request.getParameter(PARAMERET_ACTION) != null) ? request.getParameter(PARAMERET_ACTION) : ACTION_DEFAULT;
        
        switch (action) {
            case ACTION_UPLOAD_STUDENT_CSV:
                forwardPath = "/WEB-INF/jsp/view/uploadCsvFile.jsp";
                break;
            case ACTION_REJECT_CSV_DATA:
                forwardPath = "/WEB-INF/jsp/view/uploadCsvFile.jsp";
                break;
            case ACTION_UPLOAD_TEACHER_CSV:
                forwardPath = "/WEB-INF/jsp/view/uploadCsvFile.jsp";
                break;
            case ACTION_ACCEPT_CSV_DATA:
                csvToDB(request);
                forwardPath = "/WEB-INF/jsp/view/validate.jsp";
                break;      
            case ACTION_ACCEPT_GO_BACK:
                csvCont(request);
                forwardPath = "/StudentList_1.jsp";
                break;
            default:
        }
        getServletContext().getRequestDispatcher(forwardPath).forward(request, response);
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
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String contentType = request.getContentType();
        if (contentType.contains("multipart/form-data")) {
            uploadFile(request);
        }

        String action = getParam(request, PARAMERET_ACTION, ACTION_DEFAULT);

        String forward = "/index.html";
        switch (action) {
            case ACTION_UPLOAD_STUDENT_CSV:
//                uploadStudent(request);
                validateStudent1(request);                
                
                forward = "/WEB-INF/jsp/view/validate.jsp";
//                forward = "/StudentList_1.jsp";
                
                request.setAttribute(StudentServletControler.ATTRIBUTE_SPECIALITY_LIST, Student.getAllSpecs());                
                request.setAttribute(StudentServletControler.ATTRIBUTE_GROUP_LIST, Student.getAllGroups());
                request.setAttribute(StudentServletControler.ATTRIBUTE_FACULTY_LIST, Student.getAllFaculty());
                
                response.setContentType("text/html;charset=UTF-8");
                break;
            case ACTION_UPLOAD_TEACHER_CSV:
                uploadTeachers(request);
                forward = "/TeacherList.jsp";

                request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, Teacher.getAllDepartment());
                request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, Teacher.getAllFaculty());
                
                response.setContentType("text/html;charset=UTF-8");
                break;                
            default:
        }
        getServletContext().getRequestDispatcher(forward).forward(request, response);
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

    private void csvCont(HttpServletRequest request) {
        
        Session ses1 = HibernateUtil.currentSession();
        String queryTo = "SELECT id_person FROM persons WHERE  id_person=(SELECT MAX(id_person) FROM persons)";
        String to = "";
        to = ses1.createSQLQuery(queryTo).list().toString();
        HibernateUtil.closeSession();
        from = from.substring(1,from.length()-1);
        to = to.substring(1,to.length()-1);
                      
        int from = Integer.parseInt(this.from);
        int _to = Integer.parseInt(to);
        String[] arrayID = new String[_to - from];
        int count = 0;
        for (int i = from + 1; i < _to+1; i++) {
            arrayID[count] = Integer.toString(i);
            count++;
        }
        
        Session ses = HibernateUtil.currentSession();                     
        List<Student> listStudent = new ArrayList<>();

        listStudent = ses.createCriteria(Student.class)
                .add(Restrictions.in(DB_0.Persone.COLUM_ID, StringExt.toInt(arrayID)))
                .list();
        
        HibernateUtil.closeSession();
        //request.setAttribute(ATTRIBUTE_STUDENT, listStudent);.
        StudentServletControler.TryLast(request, arrayID);
    }

}
