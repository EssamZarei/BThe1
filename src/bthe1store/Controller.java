/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

/**
 *
 * @author eaz99
 */
public class Controller {
     private Model model;
     private View view;
     
     public Controller(Model model, View view){
          this.model = model;
          this.view = view;
     }
     
     public DBConnection start(){
          int userCoice = view.menu();
          
          if(userCoice == 1){
               return model.getDBConnection();
          }else if(userCoice == 2){
               view.message(model.getDBConnection().toString());
          }else{
               view.message("Thank You To Intergate with DB :)");
          }
          return null;
     }
     
     public DBConnection getConnection(String classAsked){
          view.message("\n" + classAsked + " Class got DBInstance");
          return model.getDBConnection();
     }
     
     
     
}
