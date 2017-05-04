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
import org.supercsv.cellprocessor.Optional;
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
public class CsvWriterGmail {
    private static final Charset UTF = Charset.forName("UTF-8");
       
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            // Preson
            // The first 4 columns
            new NotNull(),      //  First Name
            new NotNull(),      //  Last Name
            new NotNull(),      //  Email Address
            new NotNull(),      //  Password
            
            // 
            new Optional(),     //Secondary Email,
            new Optional(),     //Work Phone 1,
            new Optional(),     //Home Phone 1,
            new Optional(),     //Mobile Phone 1,
            
            new Optional(),     //Work address 1,
            new Optional(),     //Home address 1,
            
            new Optional(),     //Employee Id,
            new Optional(),     //Employee Type,
            new Optional(),     //Employee Title,
            
            new Optional(),     //Manager,
            new Optional(),     //Department,
            new Optional(),     //Cost Center            
        };
        return processors;
    }
   
    public static List<String> readWithCsvBeanReader(List<PersonInf> personList) {
        //The first 4 columns: first name, last name, email address, password are mandatory
        //First Name,Last Name,Email Address,Password,Secondary Email,Work Phone 1,Home Phone 1,Mobile Phone 1,Work address 1,Home address 1,Employee Id,Employee Type,Employee Title,Manager,Department,Cost Center
        final String[] header = new String[] { "First Name","Last Name","Email Address","Password",
            "Secondary Email","Work Phone 1","Home Phone 1","Mobile Phone 1",
            "Work address 1","Home address 1",
            "Employee Id","Employee Type","Employee Title",
            "Manager","Department","Cost Center",};
                
        final String[] fieldMapping = new String[] {"firstname", "lastname", "email", "password",
            null, null, null, null,
            null, null, 
            null, null, null, 
            null, null, null,};
    
        final CellProcessor[] processors = getProcessors();
        
        ICsvBeanWriter beanWriter = null;
        StringWriter sw = new StringWriter();
        List<String> list = new ArrayList<>();
        try {
            beanWriter = new CsvBeanWriter(sw,                   
                          CsvPreference.STANDARD_PREFERENCE);
                
            // write the header
                beanWriter.writeHeader(header);
                
            // write the beans
            for( final PersonInf person : personList ) {
                beanWriter.write(person, fieldMapping , processors);
            }
            beanWriter.close();            
        }catch(IOException ex){
            Logger.getLogger(CsvWriterGmail.class.getName()).log(Level.SEVERE, null, ex);
        }                 
        return new ArrayList<>(Arrays.asList(sw.toString().split("\n")));
    }
}
