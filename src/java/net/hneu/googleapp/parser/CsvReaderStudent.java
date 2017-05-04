package net.hneu.googleapp.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import slGal.LiveEdu.DB.DB_0.Persone;
import slGal.LiveEdu.ORM.Student;

/**
 *
 * @author Andrey
 */
public class CsvReaderStudent extends AbstarctCsvReader{
    private static final Charset UTF = Charset.forName("UTF-8");    
    
    public CsvReaderStudent(File fileCSV) {
        super(fileCSV);
    }
    
    private CsvReaderStudent(String fileName) {
        super(new File(fileName));
    }
   
    private CellProcessor[] getProcessors() {
//        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
//        StrRegEx.registerMessage(emailRegex, "must be a valid email address");
        final String groupRegex = "\\d(?:\\.\\d\\d){4}";    //   Группа.специальность.код -> Student.specialityName             
        StrRegEx.registerMessage(groupRegex, "must be a valid format 9.99.99.99.99 (\"\\d(?:\\.\\d\\d){4}\")");

        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            // Presone
            new SuppressException(new NotNull(new Trim())), // Фамилия -> Person.firstName 
            new SuppressException(new NotNull(new Trim())), // Имя -> Person.lastName
            new Optional(new Trim()), // Отчество -> Person.patronymic
            new SuppressException(new NotNull(new ParseBool())), // Личные данные.украинец? -> Person.ukrainian
            new SuppressException(new NotNull(new ParseDate("dd.MM.yyyy"))), // нач. обуч. -> Person.dateLiveReg

            // Student
            new SuppressException(new NotNull(new ParseInt())), // курс -> kurs
            new Optional(new ParseBool()),  /*null,*/   // отчислен? ->
            new Optional(new Trim()),       /*null,*/   // Группа.отделение.отделение -> 
            new SuppressException(new Trim(new StrRegEx(groupRegex))), // Группа.группа -> Student.groupa
            new SuppressException(new NotNull(new Trim())), // Группа.факультет.факультет -> facultyFull
            new SuppressException(new NotNull(new Trim())), // Группа.факультет.краткое название -> faculty
            new Optional(new Trim()),       /*null,*/   // Группа.специальность.специальность -> 
            new SuppressException(new NotNull(new Trim())), // Группа.специальность.код -> specialityName
            new SuppressException(new NotNull(new Trim())), // Группа.специальность.номер спец -> specialityNumber            
            new SuppressException(new NotNull(new Trim())), // студ. билет -> card
            new SuppressException(new NotNull(new Trim())), // Личные данные.код ЄДБО -> edbo            
        };
        return processors;
    }

    public List<Student> readWithCsvBeanReader(File fileCsv) throws FileNotFoundException {
        return readWithCsvBeanReader(new InputStreamReader(new FileInputStream(fileCsv), UTF));
    }

    public List<Student> readWithCsvBeanReader(String fileName) throws FileNotFoundException {
        return readWithCsvBeanReader(new File(fileName));
    }
   
    private List<Student> readWithCsvBeanReader() throws FileNotFoundException {
        return readWithCsvBeanReader(fileCSV);
    }
    
    private List<Student> readWithCsvBeanReader(Reader readerCSV) {
        errorList.clear();
        List<Student> studentList = new ArrayList<>();        
        try (ICsvBeanReader beanReader = new CsvBeanReader(readerCSV, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);) {
            // ensures that this method is only called when reading the first line (as that's where the header is meant to be)
            //final String[] header = beanReader.getHeader(true);
            beanReader.getHeader(true);            
            final String[] mapPropeties = {"lastname", "firstName", "patronymic", "ukrainian", "dateLiveReg",
                "course", null, null, "groupa", "facultyFull", "faculty", null, "spec", "numberSpec", "card", "edbo"};
            final CellProcessor[] processors = getProcessors();
            
            Student student = null;
            do {
                student = beanReader.read(Student.class, mapPropeties, processors);
                if (!SuppressException.SUPPRESSED_EXCEPTIONS.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder("Suppressed exceptions for row "
                            + beanReader.getRowNumber() + ":");

                    SuppressException.SUPPRESSED_EXCEPTIONS.stream().forEach((e) -> {
                        stringBuilder.append(e);
                        errorList.add(e);
                    });
                    
                    Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO, stringBuilder.toString());

                    SuppressException.SUPPRESSED_EXCEPTIONS.clear();
                } else {
                    if(student != null) studentList.add(student);
                    Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO, 
                            String.format("lineNo=%s, rowNo=%s, customer=%s", 
                                   beanReader.getLineNumber(), beanReader.getRowNumber(), student));
                }
            } while (student != null);
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }        
        return (errorList.isEmpty())? studentList : new ArrayList<>(0);
    }
    
    
    public List<CSVBean> readWithCsvBeanReaderValidate(File fileCsv) throws FileNotFoundException {
        return readWithCsvBeanReader_validate(new InputStreamReader(new FileInputStream(fileCsv), UTF));
    }
    
    public List<CSVBean> readWithCsvBeanReaderValidate(String fileName) throws FileNotFoundException {
        return readWithCsvBeanReaderValidate(new File(fileName));
    }

    public static class Validate{
        List<SuppressException> listError;
        Persone persone;
    }
    
    public static class CSVBean{
        private Student bean;
        private List<SuperCsvCellProcessorException> list;

        public Student getBean() {
            return bean;
        }

        public void setBean(Student bean) {
            this.bean = bean;
        }

        public List<SuperCsvCellProcessorException> getList() {
            return list;
        }

        public void setList(List<SuperCsvCellProcessorException> list) {
            this.list = list;
        }
        
        
    }
            
    private List<CSVBean> readWithCsvBeanReader_validate(Reader readerCSV) {
        return readWithCsvBeanReader_validate(readerCSV, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    }
    
    private List<CSVBean> readWithCsvBeanReader_validate(Reader readerCSV, CsvPreference csvPreference) {
        errorList.clear();
        //List<Student> studentList = new ArrayList<>();
        List<CSVBean> _studentList = new ArrayList<>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(readerCSV, csvPreference);) {
            // ensures that this method is only called when reading the first line (as that's where the header is meant to be)
            //final String[] header = beanReader.getHeader(true);
            beanReader.getHeader(true);
            final String[] mapPropeties = {"lastname", "firstName", "patronymic", "ukrainian", "dateLiveReg",
                "course", null, null, "groupa", "facultyFull", "faculty", null, "spec", "numberSpec", "card", "edbo"};
            final CellProcessor[] processors = getProcessors();
            
            Student student = null;            
            do {                
                student = beanReader.read(Student.class, mapPropeties, processors);
                               
                CSVBean csvBean = new CSVBean();
                csvBean.setBean(student);
                csvBean.setList(new ArrayList<>(SuppressException.SUPPRESSED_EXCEPTIONS));
                
                if(student != null) _studentList.add(csvBean);
                
                    if (!SuppressException.SUPPRESSED_EXCEPTIONS.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder("Suppressed exceptions for row "
                            + beanReader.getRowNumber() + ":");

                        List<String> messageErrorRerord = new ArrayList<>();
                        SuppressException.SUPPRESSED_EXCEPTIONS.stream().forEach((e) -> {
                        stringBuilder.append(e);
                        errorList.add(e);
                        });
                        messageErrorList.add(messageErrorRerord);

                    Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO, stringBuilder.toString());

                        SuppressException.SUPPRESSED_EXCEPTIONS.clear();
                    } else {                        
                    //if(student != null) studentList.add(csvBean);
                        Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO,
                            String.format("lineNo=%s, rowNo=%s, customer=%s", 
                                   beanReader.getLineNumber(), beanReader.getRowNumber(), student));
                    }                
            } while (student != null);
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        //return (errorList.isEmpty())? studentList : new ArrayList<>(0);
        return _studentList;
    }
            
    public CsvReaderStudent validate(String fileName) throws FileNotFoundException {
        return validate(new File(fileName));
    }
    
    public static CsvReaderStudent validate(File fileCsv) throws FileNotFoundException {
        CsvReaderStudent csvReaderStudent = new CsvReaderStudent(fileCsv);        
        csvReaderStudent.executeValidate(new InputStreamReader(new FileInputStream(fileCsv), UTF), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        return csvReaderStudent;
    }
    
    private void executeValidate(Reader readerCSV, CsvPreference csvPreference) {
        if (isComplite) return;
        
        try (ICsvBeanReader beanReader = new CsvBeanReader(readerCSV, csvPreference);) {
            header = beanReader.getHeader(true);
            final String[] mapPropeties = {"lastName", "firstName", "patronymic", "ukrainian", "dateLiveReg",
                "course", /*null*/"dismiss", /*null*/"extramuralStudent", "groupa", "facultyFull", "faculty", 
                "specialityName", "specialityCode", "specialityNumber", "card", "edbo"};
            final CellProcessor[] processors = getProcessors();
            
            CsvStudent student = null;            
            do {                
                student = beanReader.read(CsvStudent.class, mapPropeties, processors);
                
                if (student != null) {
                    studentList.add(student);
                    if (!SuppressException.SUPPRESSED_EXCEPTIONS.isEmpty()) {                        
                        Logger.getLogger(CsvReaderStudent.class.getName())
                                .log(Level.INFO, "Suppressed exceptions for row {0}:{1}",
                                        new Object[]{beanReader.getRowNumber(),
                                            SuppressException.SUPPRESSED_EXCEPTIONS.stream()
                                            .map(x -> x.toString())
                                            .collect(Collectors.joining("\n "))});
                        
                        isError = true;
                        List<String> messageErrorRerord = new ArrayList<>();
                        SuppressException.SUPPRESSED_EXCEPTIONS.stream().forEach((e) -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Error: ").append(e.getMessage()).append(", ");
                            sb.append("line: ").append(e.getCsvContext().getRowNumber()).append(", ");
                            sb.append("column: ").append(e.getCsvContext().getColumnNumber()).append(", ");
                            messageErrorRerord.add(sb.toString());
                        });
                        messageErrorList.add(messageErrorRerord);

                        SuppressException.SUPPRESSED_EXCEPTIONS.clear();
                    } else {                        
                        Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.INFO,
                                "lineNo={0}, rowNo={1}, customer={2}",
                                        new Object[]{beanReader.getLineNumber(), beanReader.getRowNumber(), student});
                        messageErrorList.add(new ArrayList<>(0));
                    }
                }
            } while (student != null);
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvReaderStudent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        isComplite = true;
    }
    
    public static class CsvStudent extends CsvEntity {
        private String lastName;
        private String firstName;
        private String patronymic;
        private boolean ukrainian;
        private Date dateLiveReg;
        private int course;
        private Boolean dismiss;
        private String extramuralStudent;
        private String groupa;
        private String facultyFull; 
        private String faculty;
        private String specialityName;
        private String specialityCode;
        private String specialityNumber;
        private String card;
        private String edbo;

        public CsvStudent() {
        }

        public String getSpecialityCode() {
            return specialityCode;
        }

        public void setSpecialityCode(String specialityCode) {
            this.specialityCode = specialityCode;
        }
        
        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        public boolean isUkrainian() {
            return ukrainian;
        }

        public void setUkrainian(boolean ukrainian) {
            this.ukrainian = ukrainian;
        }

        public Date getDateLiveReg() {
            return dateLiveReg;
        }

        public void setDateLiveReg(Date dateLiveReg) {
            this.dateLiveReg = dateLiveReg;
        }

        public Boolean getDismiss() {
            return dismiss;
        }

        public void setDismiss(Boolean dismiss) {
            this.dismiss = dismiss;
        }
        
        public int getCourse() {
            return course;
        }

        public void setCourse(int course) {
            this.course = course;
        }

        public String getGroupa() {
            return groupa;
        }

        public void setGroupa(String groupa) {
            this.groupa = groupa;
        }

        public String getExtramuralStudent() {
            return extramuralStudent;
        }

        public void setExtramuralStudent(String extramuralStudent) {
            this.extramuralStudent = extramuralStudent;
        }
                
        public String getFacultyFull() {
            return facultyFull;
        }

        public void setFacultyFull(String facultyFull) {
            this.facultyFull = facultyFull;
        }

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public String getSpecialityName() {
            return specialityName;
        }

        public void setSpecialityName(String specialityName) {
            this.specialityName = specialityName;
        }

        public String getSpecialityNumber() {
            return specialityNumber;
        }

        public void setSpecialityNumber(String specialityNumber) {
            this.specialityNumber = specialityNumber;
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
        
        
    }
}   


