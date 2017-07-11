/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import javax.servlet.http.HttpServlet;

/**
 *
 * @author Andrey
 */
public class PersonServletControler extends HttpServlet {

    public static final String ACTION_CLEAR_EMAIL = "CLEAR_EMAIL";

    public static final String ACTION_CLEAR_FIO_AND_EMAIL = "CLEAR_FIO_AND_EMAIL";
    public static final String ACTION_FILTER = "ACTION_FILTER";
    public static final String ACTION_GENERATE_DEFAULT = "ACTION_DEFAULT";
    public static final String ACTION_GENERATE_EMAIL = "GENERATE_EMAIL";
    //
    public static final String ACTION_GENERATE_FIO = "GENERATE_FIO";

    public static final String ACTION_GENERATE_PASSWORD = "ACTION_GENERATE_PASSWORD";

    public static final String ACTION_GENERATE_PDF = "ACTION_GENERATE_PDF";
    public static final String ACTION_SET_MSDN = "ACTION_SET_MSDN";
    public static final String ACTION_SET_OFFICE365 = "ACTION_SET_OFFICE365";
    public static final String ACTION_CLEAR_OFFICE365 = "ACTION_CLEAR_OFFICE365";
    public static final String ATTRIBUTE_CSV_IMPORT = "ATTRIBUTE_CSV_IMPORT";
    //
    public static final String ATTRIBUTE_PERSON = "ATTRIBUTE_PERSON";

    //
    public static final String PARAMETER_EMAIL = "paramEmail";
    //
    public static final String PARAMETER_UKRAINIAN = "paramUkranianian";
}
