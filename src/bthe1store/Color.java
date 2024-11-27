/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author eaz99
 */
public class Color {

     private int colorID;
     private String colorName;
     private int colorPrice;

     private static String colors;

     private static Model model = new Model();
     private static View view = new View();
     private static Controller controller = new Controller(model, view);

     private static DBConnection DBInstance = controller.getConnection("Color");

     public Color(int colorID) {
          this.colorID = colorID;
          setColorByID();

     }

     public void setColorByID() {
          String query = "SELECT * FROM Colors_Table WHERE colorID = ?";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, colorID);
               ResultSet rs = pstmt.executeQuery();
               rs.next();
               colorName = rs.getString(2);
               colorPrice = rs.getInt(3);
          } catch (SQLException e) {
               System.err.println("Error when setting the Color by ID");
               e.printStackTrace();
          }

     }

     public void changeColor(int colorID) {
          this.colorID = colorID;
          setColorByID();
     }

     public int getColorID() {
          return colorID;
     }

     public String getColorName() {
          return colorName;
     }

     public int getColorPrice() {
          return colorPrice;
     }

     @Override
     public String toString() {
          return String.format("Color Details - ID: %d | Name: %s | Price: $%d", colorID, colorName, colorPrice);
     }

}
