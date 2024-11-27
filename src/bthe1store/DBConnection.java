/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

import java.sql.*;

/**
 *
 * @author eaz99
 */
public class DBConnection {
     
     private static DBConnection instance;
     
     private static Connection connection;
     private String url = "jdbc:mysql://localhost:3306/DBBThe1";
     private String name = "root";
     private String p = "whysqlserver?11112";
     
     private DBConnection(){
          try{
               connection = DriverManager.getConnection(url, name, p);
          }catch (SQLException e){
               e.printStackTrace();
          }
     }
     
     public static synchronized DBConnection getInstance(){
          
          instance = instance == null ? (new DBConnection()) : instance;
          return instance;

     }
     
     public static Connection getConnection(){
          return connection;
     }

     @Override
     public String toString() {
          return "DBConnection{" + "connection=" + connection + ", url=" + url + ", name=" + name + '}';
     }
     
     
     
}
