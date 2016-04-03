/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kieda
 */
public class ReadFile {
    public static String read(File f){
        String l = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String c;
            while((c=br.readLine())!=null){
                l+=c+"\n";
            }
        } catch (Exception ex) {}
        return l;
    }
}
