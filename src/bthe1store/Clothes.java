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
class Clothes {

     private static Model model = new Model();
     private static View view = new View();
     private static Controller controller = new Controller(model, view);

     private DBConnection DBInstance = controller.getConnection("Clothes");

     private String clothesName;
     private int clothesPrice;
     private int clothesID;

     public Clothes(int clothesID) {
          this.clothesID = clothesID;
          setClothesByID();
     }

     public void setClothesByID() {
          String query = "SELECT * FROM Clothes_Table WHERE clothesID = ?";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, clothesID);
               ResultSet rs = pstmt.executeQuery();
               rs.next();
               clothesName = rs.getString(2);
               clothesPrice = rs.getInt(3);
               System.out.println("Clothes here ");

          } catch (SQLException e) {
               System.err.println("Error when setting the Clothes by ID");
               e.printStackTrace();
          }
     }

     public void changeClothes(int clothesID) {
          this.clothesID = clothesID;
          setClothesByID();
     }

     public String getClothesName() {
          return clothesName;
     }

     public int getClothesPrice() {
          return clothesPrice;
     }

     public int getClothesID() {
          return clothesID;
     }

     @Override
     public String toString() {
          return String.format("Clothes Details - ID: %d | Name: %s | Price: $%d", clothesID, clothesName, clothesPrice);
     }

}
