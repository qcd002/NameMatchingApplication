/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.commons.codec.language;

import java.util.Scanner;

/**
 *
 * @author QUANDO
 */
public class Prg {
    public static void main(String[] args) throws ClassNotFoundException, Exception {
        DatabaseConnection dc = new DatabaseConnection();
        dc.load();
      // dc.add();
       Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: ");
        
        String name = scan.next();
   
        dc.search(name);
        //dc.testSoundex();
       // dc.testMetaphone();
        
    }
}
