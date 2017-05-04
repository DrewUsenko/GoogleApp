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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import slGal.LiveEdu.ORM.Teacher;

/**
 *
 * @author Andrey
 */
public class CsvReaderTeacher {

    private static final Charset UTF = Charset.forName("UTF-8");
    private List<SuperCsvCellProcessorException> errorList = new ArrayList<>();    
    private File fileCSV;
    
    public CsvReaderTeacher(File file) {
        this.fileCSV = file;
    }
    
    public CsvReaderTeacher(String fileName) {
        this(new File(fileName));
    }

    public List<SuperCsvCellProcessorException> getErrorList() {
        return errorList;
    }
    /**
     * <pre>
     * Фамилия,Имя,Отчество,личные данные.украинец?,должность.должность.должность,подразделение.название,подразделение.факультет.факультет,личные данные.идентиф. код
     * </pre>
     * @return 
     */
    private CellProcessor[] getProcessors() {
//        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
//        StrRegEx.registerMessage(emailRegex, "must be a valid email address");
        final String groupRegex = "\\d(?:\\.\\d\\d){4}";    //   Группа.специальность.код -> Student.spec             
        StrRegEx.registerMessage(groupRegex, "must be a valid format 9.99.99.99.99 (\"\\d(?:\\.\\d\\d){4}\")");

        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            // Presone
            new SuppressException(new NotNull(new Trim())), // Фамилия -> Person.firstName 
            new SuppressException(new NotNull(new Trim())), // Имя -> Person.lastname
            new Optional(new Trim()), // Отчество -> Person.patronymic
            
            new SuppressException(new NotNull(new Trim())), // личные данные.идентиф. код-> edbo          
            
            new SuppressException(new NotNull(new ParseBool())), // Личные данные.украинец? -> Person.ukrainian

            new Optional(new Trim()), // должность -> Person.staffPost            
//            new SuppressException(new NotNull(new Trim())), // должность.должность.должность -> Teacher.staffPost            
            new SuppressException(new NotNull(new Trim())), // подразделение.факультет.факультет -> faculty
            new SuppressException(new NotNull(new Trim())), // подразделение.факультет.факультет -> facultyFull
            
            new SuppressException(new NotNull(new Trim())), // подразделение.название -> department
            new SuppressException(new NotNull(new Trim())), // подразделение.название -> departmentFull
                                   
            // English Family name
            new Optional(new Trim()), // Last Name -> Person.lastnameEn
            new Optional(new Trim()), // First Name -> Person.firstNameEn
        };

        return processors;
    }

    public List<Teacher> readWithCsvBeanReader(File fileCsv) throws FileNotFoundException {
        return readWithCsvBeanReader(new InputStreamReader(new FileInputStream(fileCsv), UTF));
    }

    public List<Teacher> readWithCsvBeanReader(String fileName) throws FileNotFoundException {
        return readWithCsvBeanReader(new File(fileName));
    }

    private List<Teacher> readWithCsvBeanReader() throws FileNotFoundException {
        return readWithCsvBeanReader(fileCSV);
    }
    
    //private List<Pair<Teacher, List<CsvError>> readWithCsvBeanReader(Reader readerCSV) {
    private List<Teacher> readWithCsvBeanReader(Reader readerCSV) {
        errorList.clear();
        List<Teacher> teacherList = new ArrayList<>();        
        try (ICsvBeanReader beanReader = new CsvBeanReader(readerCSV, CsvPreference.STANDARD_PREFERENCE);) {
            // ensures that this method is only called when reading the first line (as that's where the header is meant to be)
            //final String[] header = beanReader.getHeader(true);
            beanReader.getHeader(true);         
            
            final String[] mapPropeties = {"lastname", "firstname", "patronymic",                 
                "iin", 
                "ukrainian",
                "staffPost",
                "faculty", "facultyFull", 
                "department", "departmentFull",                
                "lastname", "firstname"};
            final CellProcessor[] processors = getProcessors();

            Teacher teacher;
            do {
                teacher = beanReader.read(Teacher.class, mapPropeties, processors);
                if (!SuppressException.SUPPRESSED_EXCEPTIONS.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder("Suppressed exceptions for row "
                            + beanReader.getRowNumber() + ":");
              
//                    SuppressException.SUPPRESSED_EXCEPTIONS.forEach((e) -> {
//                        stringBuilder.append(e);
//                        errorList.add(e);
//                    });
                    
                    for (SuperCsvCellProcessorException e : SuppressException.SUPPRESSED_EXCEPTIONS){
                        stringBuilder.append(e);
                        errorList.add(e);
                    }
                    
                    Logger.getLogger(CsvReaderTeacher.class.getName()).log(Level.INFO, stringBuilder.toString());
                                      
                    // clear ready for next row
                    SuppressException.SUPPRESSED_EXCEPTIONS.clear();
                } else {
                    if(teacher != null) teacherList.add(teacher);
                    Logger.getLogger(CsvReaderTeacher.class.getName()).log(Level.INFO, 
                            String.format("lineNo=%s, rowNo=%s, customer=%s", 
                                   beanReader.getLineNumber(), beanReader.getRowNumber(), teacher));                        
                }
            } while (teacher != null);
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(CsvReaderTeacher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvReaderTeacher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }        
        return (errorList.isEmpty())? teacherList : new ArrayList<>(0);
    }
}