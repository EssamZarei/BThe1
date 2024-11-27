/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

import java.util.Scanner;

/**
 *
 * @author eaz99
 */
public class View {
     private Scanner in = new Scanner(System.in);
     
     
     public int menu(){
          System.out.println("Press 1 : Get DB Connection");
          System.out.println("Press 2 : To Show DB Connection information");
          System.out.println("Press 3 : To Exit");
          return BThe1.validChoice(3);
     }
     
     
     public void message(String message){
          System.out.println(message);
     }
     
     
     
     
}
