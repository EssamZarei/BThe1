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
public abstract class Drawing {
     
     protected Model model = new Model();
     protected View view = new View();
     protected Controller controller = new Controller(model, view);
     protected DBConnection DBInstance = controller.getConnection("Drawing");
     
     protected Drawing drawing;
     protected int drawID;
     protected String drawType;
     protected String drawName;
     protected int drawPrice;
     
     public static int numberOfDraw = 0;
     
     public Drawing(){
          drawID = 0;
          drawType = "Base Drawing";
          drawName = "Base Drawing";
          drawPrice = 0;
     }
     
     public Drawing(int drawID, Drawing drawing) {
          numberOfDraw ++;
          this.drawing = drawing;
          this.drawID = drawID;
          setDrawingByID();
          
     }
     
     public void setDrawingByID(){
          String query = "SELECT * FROM Draw_Table WHERE drawID = ?";
          
          try(PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)){
               pstmt.setInt(1, drawID);
               ResultSet rs = pstmt.executeQuery();
               rs.next();
               drawType = rs.getString(2);
               drawName = rs.getString(3);
               drawPrice = rs.getInt(4);
               
          }catch (SQLException e){
               System.err.println("Error when setting the Drawing by ID");
               e.printStackTrace();
          }
          
     }
     
     public String getDescribtion() {
          return drawing.getDescribtion() + "Name: " + drawName + ", ";
     }

     public int getTotalPrice() {
          return drawPrice;
     }

     public int getDrawID() {
          return drawID;
     }

     public String getDrawType() {
          return drawType;
     }

     public String getDrawName() {
          return drawName;
     }

     public int getDrawPrice() {
          return drawPrice;
     }

     public static int getNumberOfDraw() {
          return numberOfDraw;
     }
     
     


}
