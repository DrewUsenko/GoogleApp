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
import slGal.LiveEdu.ORM.StudentInf;

/**
 *
 * @author Andrey
 */
public class CsvWriterMsdn {

    private static final Charset UTF = Charset.forName("UTF-8");

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors;
        processors = new CellProcessor[]{
            new NotNull(), // email
        };
        return processors;
    }

    public static List<String> readWithCsvBeanReader(List<PersonInf> listPerson) {
        final String[] fieldMapping = new String[]{"email"};
        final CellProcessor[] processors = getProcessors();

        ICsvBeanWriter beanWriter = null;
        StringWriter sw = new StringWriter();
        List<String> list = new ArrayList<>();
        try {
            beanWriter = new CsvBeanWriter(sw,
                    CsvPreference.STANDARD_PREFERENCE);

            // write the beans
            for (final PersonInf person : listPerson) {
                beanWriter.write(person, fieldMapping, processors);
            }
            beanWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(CsvWriterMsdn.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>(Arrays.asList(sw.toString().split("\n")));
    }
    
    public static List<String> readWithCsvBeanReaderStud(List<StudentInf> listStudent) {
        final String[] fieldMapping = new String[]{"email"};
        final CellProcessor[] processors = getProcessors();

        ICsvBeanWriter beanWriter = null;
        StringWriter sw = new StringWriter();
        try {
            beanWriter = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);

            // write the beans
            for (final StudentInf student : listStudent) {
                beanWriter.write(student, fieldMapping, processors);
            }
            beanWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(CsvWriterMsdn.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>(Arrays.asList(sw.toString().split("\n")));
    }
}
