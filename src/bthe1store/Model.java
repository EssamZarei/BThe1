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
public class Model {
     private DBConnection instance;
     
     public Model(){
          instance = instance.getInstance();
     }
     
     public DBConnection getDBConnection(){
          return instance;
     }
     
     
}
