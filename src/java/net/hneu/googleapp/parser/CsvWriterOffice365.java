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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import slGal.LiveEdu.ORM.PersonInf;
import slGal.LiveEdu.ORM.StuffInf;
import slGal.LiveEdu.ORM.StudentInf;

/**
 *
 * @author Andrey
 */
public class CsvWriterOffice365 {
    private static final Charset UTF = Charset.forName("UTF-8");
       
    /**
     * <pre>
     *      User Name,First Name,Last Name,Display Name,Job Title,Department,Office Number,Office Phone,Mobile Phone,Fax,Address,City,State or Province,ZIP or Postal Code,Country or Region
     *      Имя пользователя,Имя,Фамилия,Отображаемое имя,Должность,Отдел,Номер офиса,Рабочий телефон,Мобильный телефон,Факс,Адрес,Город,"Область, край",Почтовый индекс,Страна или регион     
     * </pre>
     * @return 
     */
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            // Preson
            // The first 4 columns
            new NotNull(),      //  *User Name (email)            
            new NotNull(),      //  *First Name
            new NotNull(),      //  *Last Name
            new NotNull(),     //  *Display Name (First Name + Last Name)
            
            // 
            new Optional(),     //Job Title,
            new Optional(),     //Department,
            new Optional(),     //Office Number,
            new Optional(),     //Office Phone,
                        
            new Optional(),     //Mobile Phone,
            new Optional(),     //Fax,
            
            new Optional(),     //Address,
            new Optional(),     //City,
            new Optional(),     //State or Province,
            
            new Optional(),     //ZIP or Postal Code,
            new Optional(),     //Country or Region 
            
            new NotNull(),      //  ### Passwoed  
        };
        return processors;
    }
   
    public static List<String> readWithCsvBeanReader(List<PersonInf> personList) {
        //The first 4 columns: first name, last name, email address, password are mandatory
        //First Name,Last Name,Email Address,Password,Secondary Email,Work Phone 1,Home Phone 1,Mobile Phone 1,Work address 1,Home address 1,Employee Id,Employee Type,Employee Title,Manager,Department,Cost Center
        final String[] header = new String[] { 
            "User Name", "First Name", "Last Name", "Display Name", 
            "Job Title", "Department", "Office Number", 
            "Office Phone", "Mobile Phone", "Fax", 
            "Address", "City", "State or Province", "ZIP or Postal Code", "Country or Region",
            };
                
        final String[] fieldMapping = new String[] {
            "email", "firstname", "lastname", null,
            null, null, null, 
            null, null, null, 
            null, null, null, 
            null, null, null, null, null};
    
        final CellProcessor[] processors = getProcessors();
        
        ICsvBeanWriter beanWriter; 
//        List<String> list = new ArrayList<>();
        String str = "";
        try(StringWriter sw = new StringWriter();) {
            beanWriter = new CsvBeanWriter(sw,                   
                          CsvPreference.STANDARD_PREFERENCE);
                
            // write the header
                beanWriter.writeHeader(header);
                
            // write the beans
            for( final PersonInf person : personList ) {
                beanWriter.write(person, fieldMapping , processors);
            }     
            sw.flush();
            str = sw.toString();
        }catch(IOException ex){
            Logger.getLogger(CsvWriterOffice365.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>(Arrays.asList(str.split("\n")));
    }
    
    public static List<String> writeWithCsvMapWriter(List<StuffInf> personList){

        final String[] header = new String[]{
            "User Name", 
            "First Name", "Last Name", "Display Name",
            "Job Title", "Department", "Office Number",
            "Office Phone", "Mobile Phone", "Fax",
            "Address", "City", "State or Province", "ZIP or Postal Code", "Country or Region",             
            "Password"
        };
        
        String str = "";
        try(StringWriter sw = new StringWriter();
            ICsvMapWriter mapWriter = new CsvMapWriter(sw, CsvPreference.STANDARD_PREFERENCE);) {
            
            final CellProcessor[] processors = getProcessors();

            // write the header
            mapWriter.writeHeader(header);
            
            // create the customer Maps (using the header elements for the column keys)
            final Map<String, Object> mapItem = new HashMap<>();
            for (StuffInf item : personList) {                
                int i;
                mapItem.put(header[i=0], item.getPersonInf().getEmailCorporate().replace("hneu.net", "m.hneu.edu.ua"));
                mapItem.put(header[++i], item.getPersonInf().getFirstname());
                mapItem.put(header[++i], item.getPersonInf().getLastname());
                mapItem.put(header[++i], item.getPersonInf().getFirstname() + " " + item.getPersonInf().getLastname());
                
               /*????????????????????????????????*/ 
                
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);

                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);

                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], "Ukraine");                
                
                mapItem.put(header[++i], item.getPersonInf().getPassCorporate());
                // write the customer maps
                mapWriter.write(mapItem, header, processors);                
                
                mapItem.clear();
            }
            mapWriter.flush();
            sw.flush();
            str =  sw.toString();
        } catch (IOException ex) {
            Logger.getLogger(CsvWriterOffice365.class.getName()).log(Level.SEVERE, "message");
        }
        return new ArrayList<>(Arrays.asList(str.split("\n")));
    }       
    public static List<String> writeWithCsvMapWriterStud(List<StudentInf> listStudent){

        final String[] header = new String[]{
            "User Name", 
            "First Name", "Last Name", "Display Name",
            "Job Title", "Department", "Office Number",
            "Office Phone", "Mobile Phone", "Fax",
            "Address", "City", "State or Province", "ZIP or Postal Code", "Country or Region",             
            "Password"
        };
        
        String str = "";
        try(StringWriter sw = new StringWriter();
            ICsvMapWriter mapWriter = new CsvMapWriter(sw, CsvPreference.STANDARD_PREFERENCE);) {
            
            final CellProcessor[] processors = getProcessors();

            // write the header
            mapWriter.writeHeader(header);
            
            // create the customer Maps (using the header elements for the column keys)
            final Map<String, Object> mapItem = new HashMap<>();
            for (StudentInf item : listStudent) {                
                int i;
                mapItem.put(header[i=0], item.getPersonInf().getEmailCorporate().replace("hneu.net", "m.hneu.edu.ua"));
                mapItem.put(header[++i], item.getPersonInf().getFirstname());
                mapItem.put(header[++i], item.getPersonInf().getLastname());
                mapItem.put(header[++i], item.getPersonInf().getFirstname() + " " + item.getPersonInf().getLastname());
                
               /*????????????????????????????????*/ 
                
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], (item instanceof StudentInf) ? ((StudentInf)item).getGroup() : null);

                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);

                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], null);
                mapItem.put(header[++i], "Ukraine");                
                
                mapItem.put(header[++i], item.getPersonInf().getPassCorporate());
                // write the customer maps
                mapWriter.write(mapItem, header, processors);                
                
                mapItem.clear();
            }
            mapWriter.flush();
            sw.flush();
            str =  sw.toString();
        } catch (IOException ex) {
            Logger.getLogger(CsvWriterOffice365.class.getName()).log(Level.SEVERE, "message");
        }
        return new ArrayList<>(Arrays.asList(str.split("\n")));
    }
}

