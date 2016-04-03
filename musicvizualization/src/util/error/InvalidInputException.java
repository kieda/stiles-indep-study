/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.error;

/**
 *
 * @author kieda
 */
public class InvalidInputException extends Exception{
    public InvalidInputException(){}
    public InvalidInputException(String mess){super(mess);}
}
