/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.hneu.googleapp.utill;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author Andrey
 */
public class StringExt {
    static final String CHARSET_UTF_8 = "UTF-8";
    static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    /**
     * Convert first char of word in upper case
     * @param word
     * @return 
     */
    public static String toUpperCaseFirtLitere(String word){        
        return (word.isEmpty())? "" : (Character.toUpperCase(word.charAt(0)) + word.substring(1));        
    }

    public static String requestStringToCyrillic(final String decode) throws UnsupportedEncodingException {
        return new String(decode.getBytes(StringExt.CHARSET_ISO_8859_1), StringExt.CHARSET_UTF_8);
    }
    
    public static Integer[] toInt(String[] strArray){
        ArrayList<Integer> list = new ArrayList<>(strArray.length);
        for(int i=0; i < strArray.length; i++){
            list.add(new Integer(strArray[i]));            
        }
        return list.toArray(new Integer[]{});
    }
    
    public static int indexOfIgnoreCase(String str, String find){
        String uc = find.toUpperCase();             
        int res = str.indexOf(uc);
        if (res > -1){return 0;}
        
        String lc = find.toLowerCase();
        res = str.indexOf(str.indexOf(lc));
        return (res > -1) ? 1: -1;
    }
}
