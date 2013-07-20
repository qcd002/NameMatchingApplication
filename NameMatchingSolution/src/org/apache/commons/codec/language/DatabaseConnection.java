/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.commons.codec.language;

/**
 *
 * @author QUANDO
 */

import java.sql.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private Connection conn = null;
    private PreparedStatement addName = null;
    private PreparedStatement searchName = null;
    private PreparedStatement test = null;
   // private static final String url = "jdbc:sqlite:C:/sqlite/Dic.s3db";
    private static final String url = "jdbc:sqlite:C:/sqlite/Names.s3db";
    private ResultSet rs = null;
    private ResultSet rs1 = null;
    private ArrayList<FirstName> listCode = null;
    private ArrayList<FirstName> listBest = null;

     public void DatabaseConnection(String connectionString) throws ClassNotFoundException {

		
	}
    
	public void load() throws ClassNotFoundException {

		try{
			Class.forName("org.sqlite.JDBC");
			   conn = DriverManager.getConnection(url);
               System.out.println ("Database connection established");
		}
		catch(SQLException e)
	    {

	      System.err.println(e.getMessage());
	    }

	}
        
       public void add() throws Exception{
                BufferedReader br =null;
		try{
                int prior = 1;
                String name,mcode;
              //  Soundex sd = new Soundex();
                DoubleMetaphone dm = new DoubleMetaphone();
           //     Scanner file = new Scanner(new File ("C:/java/names.txt"));
                br = new BufferedReader(new FileReader("C:/Users/QUANDO/Documents/NetBeansProjects/Soundex/src/org/apache/commons/codec/language/names.txt"));
                File fi = new File("C:/java/output.txt");
                if(!fi.exists()){
                    fi.createNewFile();
                }
                FileWriter fw = new FileWriter(fi.getAbsoluteFile());    
                BufferedWriter bw = new BufferedWriter(fw);
                
               System.out.println("Adding names.............");
              while((name = br.readLine()) != null){
                        mcode = dm.doubleMetaphone(name);             
       			addName = conn.prepareStatement("INSERT INTO Name(prior,name,mCode) "+
       		                                       "VALUES (?,?,?)");     	
       		
       			addName.setInt(1, prior);
       			addName.setString(2, name);
                        addName.setString(3, mcode);
       			addName.executeUpdate();
                        prior++;
		}
       
                }
		catch (Exception e){
			throw e;
		}
               
                 System.out.println("Names are added");
	}
    
       public void search(String name)throws Exception{
      
           DoubleMetaphone dm = new DoubleMetaphone();
           //String scode = sd.soundex(name);
           String mcode = dm.doubleMetaphone(name);
           int p = 0;
           String n,m;
           String NAME = name.toUpperCase();
           
         try{
			// If input is a valid name, do nothing
			searchName = conn.prepareStatement("SELECT name FROM Name WHERE name = ?");
			searchName.setString(1,NAME); 
			rs1 = searchName.executeQuery();
			if (rs1.next()){
				System.out.println(name +" is a valid name!");
			}
			else{
				   try{
						listCode = new ArrayList<FirstName>();
						listBest = new ArrayList<FirstName>();
						searchName = conn.prepareStatement("SELECT prior,name,mCode FROM Name WHERE mCode = ?");
						searchName.setString(1,mcode);       
						rs = searchName.executeQuery();
						while(rs.next()){
							p = rs.getInt(1);
							n = rs.getString(2);
							m = rs.getString(3);
							FirstName f = new FirstName(p,n,m);
							listCode.add(f); 							   
						}
						System.out.println("Name by Double Metaphone: ");
						for(int i=0; i< listCode.size(); i++){
							FirstName f1 = listCode.get(i);								   
							System.out.println(f1.getPrior()+ "  " +f1.getName()+"   "+f1.getCode());
						}
							
							// Select strings with three first letters matched
						String subOri = NAME.substring(0,3);   
						String subFou = "";    
						int counter=0;
						for(int i = 0; i<listCode.size();i++){
							FirstName f1 = listCode.get(i);
							subFou = f1.getName().substring(0, 3);                   
							if(subOri.equals(subFou)){
								listBest.add(f1);
								counter++;
							}             
						}
							
						if (counter == 1){ //if there is only one string that match the first three letters, print it.
							FirstName f1 = listBest.get(0);
							System.out.println("The best match is (first 3 letters): " +
												f1.getPrior()+ "  " +f1.getName()+"   "+f1.getCode());
						}
							
						else{    // if seleting three first letter resulting in more than 1 matched string
							 // Seletion based on frequency of usage of names
							 //  The name with the smallest priority will be the best match 							
							if(listCode.size()>0){  //check for outOfBound
							   FirstName temp = listCode.get(0);
							   for(int i=1; i< listCode.size(); i++){
								   FirstName f2 = listCode.get(i);
								   if (f2.getPrior()<temp.getPrior()){ // the smallest value of "prior" field is the first priority
									   f2 = temp;
								   }
							   }         
							   System.out.println("The best match is (frequency): " +
														temp.getPrior()+ "  " +temp.getName()+"   "+temp.getCode());
							}    
							else  //Double Metaphone can't produce any code that matches code in the database
								System.out.println("Can't find any match!");
							   ////////////////////////
						}
							 rs.close();
				   }
				   catch(Exception e){ throw e;}//second try
			}//esle
       }
       catch(Exception e){ throw e;}//first try
       }//close search
       
       public void testSoundex() throws Exception{
           Soundex sd = new Soundex();
           File fi = new File("C:/java/output.txt");
                if(!fi.exists()){
                    fi.createNewFile();
                }
                FileWriter fw = new FileWriter(fi.getAbsoluteFile());    
                BufferedWriter bw = new BufferedWriter(fw);
           try{
               test = conn.prepareStatement("select n.name, n.sCode\n" +
                                                "from MisSpell m, Name n\n" +
                                                    "where m.sCode = n.sCode;");
               rs = test.executeQuery();
               while(rs.next()){
                   bw.write("Name: "+rs.getString(1)+"\t"+"sCode: "+rs.getString(2)+"\n");
               }
               rs.close();
               bw.close();
           }
           catch(Exception e){throw e;}
       }
       
       public void testMetaphone() throws Exception{
           Soundex sd = new Soundex();
           File fi = new File("C:/java/outputMeta.txt");
                if(!fi.exists()){
                    fi.createNewFile();
                }
                FileWriter fw = new FileWriter(fi.getAbsoluteFile());    
                BufferedWriter bw = new BufferedWriter(fw);
           try{
               test = conn.prepareStatement("select n.name, n.mCode\n" +
                                                "from MisSpell m, Name n\n" +
                                                    "where m.mCode = n.mCode;");
               rs = test.executeQuery();
               while(rs.next()){
                   bw.write("Name: "+rs.getString(1)+"\t"+"sCode: "+rs.getString(2)+"\n");
               }
               rs.close();
               bw.close();
           }
           catch(Exception e){throw e;}
       }
       
       
    

}