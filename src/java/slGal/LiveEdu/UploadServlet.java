/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import slGal.LiveEdu.bean.CsvStudentBean;
import slGal.LiveEdu.bean.CsvStudentBeanToStudentEntityConverter;
import slGal.LiveEdu.bean.CsvStudentToCsvStudentBeanConverter;
import slGal.LiveEdu.bean.CsvUploadBean;
import slGal.LiveEdu.DB.DB;
import slGal.LiveEdu.ORM.StudentInf;
import slGal.LiveEdu.ORM.StuffInf;

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

        String action = getParam(request, PARAMERET_ACTION, ACTION_DEFAULT);
        String forward = "/index.html";
        switch (action) {
            case ACTION_UPLOAD_STUDENT_CSV:
                validateStudent(request);
                forward = "/StudentList_1.jsp";
                List<String> listGroup = StudentInf.getAllGroups();
                request.setAttribute(StudentServletControler.ATTRIBUTE_GROUP_LIST, listGroup);
                response.setContentType("text/html;charset=UTF-8");
                                
                break;
            case ACTION_UPLOAD_TEACHER_CSV:
                uploadTeachers(request);
                forward = "/TeacherList.jsp";
                request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, StuffInf.getAllDepartment());
                request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, StuffInf.getAllFaculty());
                
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
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse Student
            CsvReaderStudent parserCsvStudent = new CsvReaderStudent(csvFile);
            List<CsvReaderStudent.CSVBean> studentList = new ArrayList<>();
            try {
                studentList = parserCsvStudent.readWithCsvBeanReaderValidate(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            if (studentList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                for (CsvReaderStudent.CSVBean csvBean : studentList){                    
                    if (!csvBean.getList().isEmpty()) {                        
                    
                        StringBuilder stringBuilder = new StringBuilder("row "
                            + csvBean.getList().get(0).getCsvContext().getRowNumber() + ":");

                        csvBean.getList().stream().forEach((e) -> {
                        stringBuilder.append(e);                      
                    });
                    
                    Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO, stringBuilder.toString());
                }                   
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
                            .forEach(x -> {                        
                        StudentInf student = (StudentInf) ses.createCriteria(StudentInf.class)
                            .add(Restrictions.eq(DB.Student.COLUM_EDBO, x.getEdbo()))
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
                              
        Session ses = HibernateUtil.currentSession();       
        
        final int[] idx = { 0 };
        csvUploadBean.getBodyOfTable().forEach((CsvStudentBean x)->{
            StudentInf student = (StudentInf) ses.createCriteria(StudentInf.class)
                .add(Restrictions.eq(DB.Student.COLUM_EDBO, x.getEdbo()))
                .uniqueResult();
            if (student != null)
                csvUploadBean.getErrorOfRecords().get(idx[0]).add("Exist in DB");
            else {        
                    ses.beginTransaction();   
                    ses.saveOrUpdate(csvStudentBeanToStudentEntityConverter.convert(x));
                    ses.getTransaction().commit();                    
                    csvUploadBean.getErrorOfRecords().get(idx[0]).add("Added to DB");
            }
            idx[0]++;
        });               
        
        HibernateUtil.closeSession();      
        request.getSession().setAttribute(UploadServlet.ATTRIBUTE_CSV_UPLOAD_BEAN, csvUploadBean);
        }
    
    
    
    private void uploadStudent(HttpServletRequest request) {
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            // Parse Student
            CsvReaderStudent parserCsvStudent = new CsvReaderStudent(csvFile);
            List<StudentInf> studentList = new ArrayList<>(0);
            try {
                studentList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (studentList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<StudentInf> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                try {
                for (StudentInf st : studentList) {
                    Transaction tx = ses.beginTransaction();
                    try {
                        ses.save(st);
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
                request.setAttribute(StudentServletControler.ATTRIBUTE_STUDENT, studentList);
            }
        }
    }

    private void validateTeachers(HttpServletRequest request) {       
        File csvFile = getParam(request, PARAMETER_FILE, File.class);
        if (csvFile != null) {
            CsvReaderTeacher parserCsvStudent = new CsvReaderTeacher(csvFile);
            List<StuffInf> teacherList = new ArrayList<>(0);
            try {
                teacherList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (teacherList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<StuffInf> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                
                Transaction tx;
                
                for (ListIterator<StuffInf> iterator = teacherList.listIterator(); iterator.hasNext();){
                    StuffInf item = iterator.next();
                    StuffInf teacher = (StuffInf)ses.createCriteria(StuffInf.class, "sf")
                            .createAlias("sf.personInf", "person")
                            .add(Restrictions.eq("person.iin", item.getPersonInf().getIin()))
                            .uniqueResult();                

                    if (teacher != null){
                                               
                        if (item.getPersonInf().getFirstname() != null)
                            teacher.getPersonInf().setFirstname(item.getPersonInf().getFirstname());
                        
                        if (item.getPersonInf().getIin()!=null)
                            teacher.getPersonInf().setIin(item.getPersonInf().getIin());
                        
                        if (item.getStuffPost() != null)
                            teacher.setStuffPost(item.getStuffPost());  
                        
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
            List<StuffInf> teacherList = new ArrayList<>(0);
            try {
                teacherList = parserCsvStudent.readWithCsvBeanReader(csvFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (teacherList.isEmpty()) {
                request.setAttribute(PARAMETER_ERROR_STUDENT_CSV, parserCsvStudent.getErrorList());
            } else {
                List<StuffInf> listInvalidInport = new ArrayList<>();
                Session ses = HibernateUtil.currentSession();
                
                Transaction tx;
                
                for (ListIterator<StuffInf> iterator = teacherList.listIterator(); iterator.hasNext();){
                    StuffInf item = iterator.next();
                    StuffInf teacher = (StuffInf)ses.createCriteria(StuffInf.class, "sf")
                            .createAlias("sf.personInf", "person")
                            .add(Restrictions.eq("person.iin", item.getPersonInf().getIin()))
                            .uniqueResult();                

                    if (teacher != null){
                                               
                        if (item.getPersonInf().getFirstname() != null)
                            teacher.getPersonInf().setFirstname(item.getPersonInf().getFirstname());
                                                
                        if (item.getPersonInf().getIin()!=null)
                            teacher.getPersonInf().setIin(item.getPersonInf().getIin());
                        
                        if (item.getStuffPost() != null)
                            teacher.setStuffPost(item.getStuffPost());  
                        
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
                    if (fileItem.isFormField()) {
                        // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                        String fieldName = fileItem.getFieldName();
                        String fieldValue = fileItem.getString();
                        parameterRequest.put(fieldName, fieldValue);
                    } else {
                        String fileName = FilenameUtils.getName(fileItem.getName());
                        
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
        return file;
    }

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
                validateStudent1(request);                
                
                forward = "/WEB-INF/jsp/view/validate.jsp";
                              
                request.setAttribute(StudentServletControler.ATTRIBUTE_GROUP_LIST, StudentInf.getAllGroups());
                
                response.setContentType("text/html;charset=UTF-8");
                break;
            case ACTION_UPLOAD_TEACHER_CSV:
                uploadTeachers(request);
                forward = "/TeacherList.jsp";

                request.setAttribute(TeacherConst.ATTRIBUTE_DEPARTMENT_LIST, StuffInf.getAllDepartment());
                request.setAttribute(TeacherConst.ATTRIBUTE_FACULTY_LIST, StuffInf.getAllFaculty());
                
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
        String queryTo = "SELECT idStudent FROM student_inf WHERE  idStudent=(SELECT MAX(idStudent) FROM student_inf)";
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
        
        //request.setAttribute(ATTRIBUTE_STUDENT, listStudent);.
        StudentServletControler.TryLast(request, arrayID);
    }

}
