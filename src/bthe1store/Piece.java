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
public class Piece {

     private static Model model = new Model();
     private static View view = new View();
     private static Controller controller = new Controller(model, view);

     private static DBConnection DBInstance = controller.getConnection("Piece");

     private int pieceID;
     private Clothes clothes;
     private Color color;
     private Drawing baseDrawing;
     private int subTotal = 0;

     private static int totalPieces = 0; // total number of pieces

     public Piece() {
          baseDrawing = new BaseDrawing();

          totalPieces++;
     }

     public void modifyPiece() {

     }

     // to add OR change Clothes based on ID
     public void addClothes() {
          System.out.println("Choose a Clothes By Entering Its ID");
          this.clothes = new Clothes(BThe1.validChoice(BThe1.getRangeClothes()));
     }

     // to add OR change Color based on ID
     public void addColor() {
          System.out.println("Choose a Color By Entering Its ID");
          this.color = new Color(BThe1.validChoice(BThe1.getRangeColor()));

     }

     // to add or change the Draw
     public void addDraw() {

          System.out.println("Press 1 : To Draw Name");
          System.out.println("Press 2 : To Draw Logo");
          System.out.println("Press 3 : To Draw Character");

          int typeToDraw = BThe1.validChoice(3);
          int drawID = BThe1.validChoice(BThe1.getRangeDraw());

          if (typeToDraw == 1) {
               baseDrawing = new NameDrawing(drawID, baseDrawing);
          } else if (typeToDraw == 2) {
               baseDrawing = new LogoDrawing(drawID, baseDrawing);
          } else {
               baseDrawing = new CharacterDrawing(drawID, baseDrawing);
          }

     }

     public int pieceToDB() {
          String pieceQuery = "INSERT INTO Piece_Table (clothesID, colorID, subTotal) VALUES (?, ?, ?)";
          String drawQuery = "INSERT INTO Piece_Draw_Table (pieceID, drawID) VALUES (?, ?)";

          int generatedPieceID = -1;

          try (PreparedStatement pstmtPiece = DBInstance.getConnection().prepareStatement(pieceQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
               pstmtPiece.setInt(1, clothes.getClothesID());
               pstmtPiece.setInt(2, color.getColorID());
               pstmtPiece.setDouble(3, getSubTotal()); 
               pstmtPiece.executeUpdate();

               try (ResultSet rs = pstmtPiece.getGeneratedKeys()) {
                    if (rs.next()) {
                         generatedPieceID = rs.getInt(1);
                         this.pieceID = generatedPieceID;
                    }
               }

               Drawing currentDrawing = baseDrawing;

               try (PreparedStatement pstmtDraw = DBInstance.getConnection().prepareStatement(drawQuery)) {
                    while (currentDrawing != null) {
                         if (currentDrawing.getDrawID() != 0) {
                              pstmtDraw.setInt(1, generatedPieceID);
                              pstmtDraw.setInt(2, currentDrawing.getDrawID());
                              pstmtDraw.executeUpdate();
                         }
                         currentDrawing = currentDrawing.drawing;
                    }
               }

          } catch (SQLException e) {
               System.out.println("Error when inserting into Piece_Table or Piece_Draw_Table");
               e.printStackTrace();
          }
          pieceID = generatedPieceID;
          return pieceID;
     }

     public Clothes getClothes() {
          return clothes;
     }

     public Color getColor() {
          return color;
     }

     public Drawing getBaseDrawing() {
          return baseDrawing;
     }

     public static int getTotalPueces() {
          return totalPieces;
     }

     public int getSubTotal() {
          return subTotal;
     }

     public void setSubTotal(int subTotal) {
          this.subTotal = subTotal;
     }

     public static void setTotalPieces() {
          Piece.totalPieces--;
     }

}
