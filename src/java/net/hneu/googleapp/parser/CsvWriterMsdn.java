/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hneu.googleapp.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import slGal.LiveEdu.ORM.PersonInf;

/**
 *
 * @author Andrey
 */
public class CsvWriterMsdn {
    private static final Charset UTF = Charset.forName("UTF-8");
       
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            // Presone
            new NotNull(),  // email
        };
        return processors;
    }
   
    public static List<String> readWithCsvBeanReader(List<PersonInf> personList) {
//        public static String readWithCsvBeanReader(List<Person> personList) {
        //final String[] header = new String[] { "First Name", "Last Name", "Birthday"};
        final String[] fieldMapping = new String[] {"email"};
        final CellProcessor[] processors = getProcessors();
        
        ICsvBeanWriter beanWriter = null;
        StringWriter sw = new StringWriter();
        List<String> list = new ArrayList<>();
        try {
            beanWriter = new CsvBeanWriter(sw,
                        //new FileWriter("target/writeWithCsvBeanWriter.csv"),
                          CsvPreference.STANDARD_PREFERENCE);
                
            // write the header
//                beanWriter.writeHeader(header);
                
            // write the beans
            for( final PersonInf person : personList ) {
                beanWriter.write(person, fieldMapping , processors);
//                beanWriter.flush();
//                list.add(sw.toString());
//                sw.flush();
//                sb.append(sw.toString());
            }
            beanWriter.close();            
        }catch(IOException ex){
            Logger.getLogger(CsvWriterMsdn.class.getName()).log(Level.SEVERE, null, ex);
        }         
        
        return new ArrayList<>(Arrays.asList(sw.toString().split("\n")));
//        return sw.toString();
    }
}
