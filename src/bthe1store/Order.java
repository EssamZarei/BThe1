/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author eaz99
 */
public class Order implements SubjectTotal {

     private static Model model = new Model();
     private static View view = new View();
     private static Controller controller = new Controller(model, view);

     private static DBConnection DBInstance = controller.getConnection("Piece");

     private int total;

     private static ArrayList<Piece> pieces = new ArrayList<Piece>();
     private static ArrayList<ObserverTotal> observers = new ArrayList<ObserverTotal>();

     public void newPiece() {
          Piece tempPiece = new Piece();
          addPieace(tempPiece);

          tempPiece.addClothes();
          recalculateTotal();

          tempPiece.addColor();
          recalculateTotal();

          addSingleDraw(tempPiece);

     }

     public void addSingleDraw(Piece pieceTemp) {
          boolean isBreak = true;
          do {
               System.out.println("Press 1 : to add a draw");
               System.out.println("Press 2 : to exit");
               System.out.println("Note : You Must Have At Least 1 Draw");
               int choiceDraw = BThe1.validChoice(2);

               if (choiceDraw == 1) {
                    pieceTemp.addDraw();
               } else {
                    if (Drawing.getNumberOfDraw() != 0) {
                         isBreak = false;
                    }
               }

               recalculateTotal();
          } while (isBreak);
     }

     public void modifyPiece(int pieceID) {

          System.out.println("Press 1 : to modify Clothes");
          System.out.println("Press 2 : to modify Color");
          System.out.println("Press 3 : to modify Draw (Please Do Not Press It Is Hard To Implement)");
          System.out.println("Press 4 : to Cancell");

          int userModify = BThe1.validChoice(4);

          Piece tempPiece = pieces.get(pieceID);
          if (userModify == 1) {
               // modify Clothes
               tempPiece.addClothes();
          } else if (userModify == 2) {
               // modify Color
               tempPiece.addColor();
          } else if (userModify == 3) {
               // modify Draw
          }

          recalculateTotal();

     }

     public Piece getPiece(int pieaceID) {
          return pieces.get(pieaceID);
     }

     public void addPieace(Piece pieceNew) {
          pieces.add(pieceNew);
     }

     public void removePiece(int pieceID) {
          pieces.remove(pieceID);
          Piece.setTotalPieces();
          recalculateTotal();
     }

     @Override
     public void addObserver(ObserverTotal observer) {
          observers.add(observer);
     }

     //                     clothes
// 1 2   6 user    1  1      4
     @Override
     public void removeObserver(ObserverTotal observer) {
          observers.remove(observer);

     }

     @Override
     public void notifyObservers() {
          for (ObserverTotal observer : observers) {
               observer.updateTotal(total);
          }
     }

     private void setTotal(int total) {
          this.total = total;
          notifyObservers();
     }

     // New method to recalculate the total price
     public void recalculateTotal() {
          total = 0;
          int subTotal;

          for (Piece piece : pieces) {
               subTotal = 0;
               if (piece.getClothes() != null) {
                    subTotal = subTotal + piece.getClothes().getClothesPrice();
               }
               if (piece.getColor() != null) {
                    subTotal += piece.getColor().getColorPrice();
               }
               if (piece.getBaseDrawing() != null) {
                    subTotal += piece.getBaseDrawing().getTotalPrice();
               }

               piece.setSubTotal(subTotal);
               total += subTotal;
          }

          notifyObservers();
     }

     public String getOrderDetails() {
          StringBuilder details = new StringBuilder();

          // Order level details
          details.append("Order Details:\n");
          details.append("User ID: ").append(BThe1.getUserID()).append("\n");
          details.append("Total Price: ").append(total).append("\n");
          details.append("Number of Pieces: ").append(pieces.size()).append("\n\n");

          // Loop through each piece
          for (int i = 0; i < pieces.size(); i++) {
               Piece piece = pieces.get(i);
               details.append("Piece ").append(i + 1).append(":\n");

               // Add Clothes details
               if (piece.getClothes() != null) {
                    Clothes clothes = piece.getClothes();
                    details.append("  Clothes: ").append(clothes.getClothesName())
                            .append(" (Price: ").append(clothes.getClothesPrice()).append(")\n");
               } else {
                    details.append("  Clothes: None\n");
               }

               // Add Color details
               if (piece.getColor() != null) {
                    Color color = piece.getColor();
                    details.append("  Color: ").append(color.getColorName())
                            .append(" (Price: ").append(color.getColorPrice()).append(")\n");
               } else {
                    details.append("  Color: None\n");
               }

               // Add Drawing details
               Drawing currentDrawing = piece.getBaseDrawing();
               if (currentDrawing != null) {
                    details.append("  Drawings:\n");
                    while (currentDrawing != null) {
                         details.append("    - ").append(currentDrawing.getDrawName())
                                 .append(" (Price: ").append(currentDrawing.getDrawPrice()).append(")\n");
                         currentDrawing = currentDrawing.drawing; // Access nested drawing
                    }
               } else {
                    details.append("  Drawings: None\n");
               }

               // Add Subtotal for the piece
               details.append("  Subtotal for Piece: ").append(piece.getSubTotal()).append("\n");
               details.append("\n"); // Separate pieces
          }

          return details.toString();
     }

     // method take the Array List of pieces
     // store each piece to the DB with the User ID
     // take each piece created and put its id to the DB insert into order with user ID
     public void orderToDB() {
          String orderQuery = "INSERT INTO Order_Table (userID, pieceID, status, total, orderID) VALUES (?, ?, ?, ? , ?)";

          //double total = 0;
          int orderID = generateNewOrderID();

          for (Piece piece : pieces) {
               try {
                    int pieceID = piece.pieceToDB();
                    //total += piece.getSubTotal();

                    try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(orderQuery)) {
                         pstmt.setInt(1, BThe1.getUserID());
                         pstmt.setInt(2, pieceID);
                         pstmt.setString(3, "Preparing ...");
                         pstmt.setInt(4, total); 
                         pstmt.setInt(5, orderID);
                         pstmt.executeUpdate();
                    }

               } catch (SQLException e) {
                    System.out.println("Error when inserting into Order_Table");
                    e.printStackTrace();
               }
          }
     }

     private static int generateNewOrderID() {
          String query = "SELECT IFNULL(MAX(orderID), 0) + 1 AS nextOrderID FROM Order_Table";

          try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(query)) {
               ResultSet rs = pstmt.executeQuery();
               if (rs.next()) {
                    return rs.getInt("nextOrderID");
               }
          } catch (SQLException e) {
               System.err.println("Error generating new Order ID.");
               e.printStackTrace();
          }
          return 1; // Default to 1 if no orders exist
     }

     // method to list all orders by a user ID
     //admin can make some one admin or as default user
     // add clothes
     // add color
     // add draw
     // remove clothes
     // remove color
     // remove draw
}
