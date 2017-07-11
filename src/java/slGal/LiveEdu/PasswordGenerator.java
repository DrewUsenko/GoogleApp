/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Andrey
 */
public class PasswordGenerator {
    public static final String SYMBOLS_LOWER_ALPHABETIC_ENGLISH = "e";
    public static final String SYMBOLS_UPPER_ALPHABETIC_ENGLISH = "E";
    public static final String SYMBOLS_DIGIT = "d";
    public static final String SYMBOLS_ZNAKI = "z";
    
    private static final Map<String, String> mapSymbols = new HashMap<>();
    static {        
        mapSymbols.put(SYMBOLS_LOWER_ALPHABETIC_ENGLISH, "abcdefghijklmnopqrstuvwxyz");
        mapSymbols.put(SYMBOLS_UPPER_ALPHABETIC_ENGLISH, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        mapSymbols.put(SYMBOLS_DIGIT, "1234567890");
        mapSymbols.put(SYMBOLS_ZNAKI, "!;%:?*()_+=-~/\\<>,.[]{}");        
    }
    
    static Random random ;
    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");                
            Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, "Generate => random: ", random);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 

    private List<Mask> maskList;

    private static class Mask{
        public int length; 
        public String symbols;

        public Mask(String symbols, int length) {
            this.symbols = symbols;
            this.length = length;
        }               
    }
    
    public static class BuilderMask {
        List<Mask> masks = new ArrayList<>();
        
        public BuilderMask(){            
        }

        private void Password(List<Mask> masks) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public BuilderMask appendMask(String mask, int lenPassword){            
            mapSymbols.forEach((String k, String v) -> {
                if (mask.contains(k)){
                    masks.add(new Mask(v, lenPassword));
                }
            });                            
            return this;
        }                 
    
    public BuilderMask appendAlphabetic(String alphabetic, int lenPassword){            
            masks.add(new Mask(alphabetic, lenPassword));                                                            
            return this;
        }                 
        
    public PasswordGenerator build(){
            return new PasswordGenerator(this.masks);
        }                 
    }

    public PasswordGenerator() {  
    }

    private PasswordGenerator(List<Mask> maskList){
        this.maskList = maskList;
    }   
    
    public String generate(){
        return maskList.stream()
                .map(z -> generate1(z))
                .collect(Collectors.joining());
    }
    
    private String generate1(Mask mask){        
        StringBuilder psw = new StringBuilder(mask.length);
        for (int i = 0; i < mask.length; i++) {
            int indexRandom = random.nextInt(mask.symbols.length());
            psw.append(mask.symbols.charAt(indexRandom));
        }
        return psw.toString();
    }
        
    private int getLength(){
        return maskList.stream()
                .collect(Collectors.summingInt(z -> z.length));                
    }
}
