/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

/**
 *
 * @author Andrey
 */
public interface Converter<S,T> {
    public T convert(S source);
}
