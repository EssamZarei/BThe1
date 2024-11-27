/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bthe1store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author eaz99
 */
public class BThe1 implements ObserverTotal {

     /**
      * @param args the command line arguments
      */
     public static Scanner in = new Scanner(System.in);

     private static DBConnection DBInstance;

     private static int userID;
     private static Order userOrder;
     private static int isAdmin;  // 1 admin    0 default user

     // the benefits of this, to know the number of each item
     // so lated the user will have a range to choose from
     private static int rangeClothes;
     private static int rangeColor;
     private static int rangeDraw;

     private int userTotal = 0;

     public BThe1() {
          userOrder.addObserver(this);
     }

     public static void main(String[] args) {
          // TODO code application logic here

          if (userOrder == null) {
               userOrder = new Order();
          }

          BThe1 bThe1 = new BThe1();

          System.out.println("Welcome to   -->  B The 1  <--   Store");
          Model model = new Model();
          View view = new View();
          Controller controller = new Controller(model, view);
          DBInstance = controller.start();

          boolean loged = false;

          while (!loged) {
               System.out.println("\n\n===\t===\t===\t===\t===");
               System.out.println("Press 1 to Rigester");
               System.out.println("Press 2 to Login");

               int choice1 = validInt();

               if (choice1 == 1) {
                    loged = register(false);
                    isAdmin = 0;
               } else if (choice1 == 2) {
                    loged = login();
                    isAdmin = 0;
               } else if (choice1 == 3) {
                    // Register as admin
                    loged = register(true);
                    isAdmin = 1;
               } else if (choice1 == 4) {
                    loged = login();
                    isAdmin = 1;
               } else {
                    System.exit(1);
               }

          }

          // 5 e       Admin
          // 6 user   user
          if (isAdmin == 1) {
               // here Admin User
               do {
                    getStore();
                    System.out.println("\n\n\nAdmin Menu:");
                    System.out.println("Press 1: Add Clothes");
                    System.out.println("Press 2: Remove Clothes");
                    System.out.println("Press 3: Add Color");
                    System.out.println("Press 4: Remove Color");
                    System.out.println("Press 5: Add Draw");
                    System.out.println("Press 6: Remove Draw");
                    System.out.println("Press 7: Exit");

                    int adminAction = validChoice(7);

                    if (adminAction == 1) {
                         addClothes();
                    } else if (adminAction == 2) {
                         removeClothes();
                    } else if (adminAction == 3) {
                         addColor();
                    } else if (adminAction == 4) {
                         removeColor();
                    } else if (adminAction == 5) {
                         addDraw();
                    } else if (adminAction == 6) {
                         removeDraw();
                    } else {
                         break;
                    }
               } while (true);

          } else {
               System.out.println("Press 1 : to create new order");
               System.out.println("Press 2 : to see your orders");
               int orderORList = validChoice(2);

               if (orderORList == 1) {
                    getStore();
                    // here will add piece
                    // remove piece
                    // change Clothes, Color
                    // add piece
                    do {
                         System.out.println("Press 1 : to add new Piece");
                         System.out.println("Press 2 : to remove a Piece");
                         System.out.println("Press 3 : to modify a Piece");
                         System.out.println("Press 4 : to Cancell");
                         int userAction = validChoice(4);

                         if (userAction == 1) {
                              // Add Piece
                              userOrder.newPiece();
                         } else if (userAction == 2 && Piece.getTotalPueces() != 0) {
                              // remove Piece
                              int pieceNumber = validChoice(Piece.getTotalPueces());
                              userOrder.removePiece(pieceNumber - 1);

                         } else if (userAction == 3 && Piece.getTotalPueces() != 0) {
                              // modify Piece
                              int pieceNumber = validChoice(Piece.getTotalPueces());
                              userOrder.modifyPiece(pieceNumber - 1);
                         } else {
                              break;
                         }
                         getStore();
                         //                     new    clothes     color    yes draw    character    luffy    no draw
                         // 1 2   6 user    1  1      4         3        1             2          2       2
                         //                    1      2         1        1             3           3       2
                         //  modify     what process     color    black
                         //  3              2             2        2
                         //  remove   piece 1
                         //   2       1
                         //   Done  
                         //    4

                         // 1 2   6 user    1  1      4         3        4      1       2          2       2
                         //                    1      2         1        3      1       1           1       2
                         System.out.println(userOrder.getOrderDetails());

                    } while (true);
                    userOrder.orderToDB();
               } else {
                    getAllOrdersByUser();
               }

          }

     }

     public static void getStore() {
          System.out.println("===\t===\t===\t===");
          getAllClothes();
          getAllColors();
          getAllDraws();
          System.out.println("===\t===\t===\t===");
     }

     public static void getAllClothes() {
          rangeClothes = 0;

          String query = "SELECT * FROM Clothes_Table";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               ResultSet rs = pstmt.executeQuery();
               System.out.println("\n\nClothes : ");
               System.out.println(String.format("%-5s %-20s %-10s", "ID", "Name", "Price"));
               System.out.println("------------------------------------------");
               while (rs.next()) {
                    rangeClothes++; // increase the number of the Clothes
                    System.out.println(String.format("%-5d %-20s $%-10.2f",
                            rs.getInt("clothesID"),
                            rs.getString("clothesName"),
                            rs.getDouble("clothesPrice")));
               }

          } catch (SQLException e) {
               System.out.println("Error When Select All Clothes_Table");
               e.printStackTrace();
          }
     }

     public static void getAllColors() {
          rangeColor = 0;

          String query = "SELECT * FROM Colors_Table";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               ResultSet rs = pstmt.executeQuery();
               System.out.println("\n\nColors : ");
               System.out.println(String.format("%-5s %-15s %-10s", "ID", "Color", "Price"));
               System.out.println("-----------------------------------");

               while (rs.next()) {
                    rangeColor++; // increase the number of the Colors
                    System.out.println(String.format("%-5d %-15s $%-10.2f",
                            rs.getInt("colorID"),
                            rs.getString("colorName"),
                            rs.getDouble("colorPrice")));
               }

          } catch (SQLException e) {
               System.out.println("Error When Select All Colors_Table");
               e.printStackTrace();
          }
     }

     public static void getAllDraws() {
          rangeDraw = 0;

          String query = "SELECT * FROM Draw_Table";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               ResultSet rs = pstmt.executeQuery();
               System.out.println("\n\nDraws : ");
               System.out.println(String.format("%-5s %-10s %-20s %-10s", "ID", "Type", "Name", "Price"));
               System.out.println("-----------------------------------------------------------");
               while (rs.next()) {
                    rangeDraw++; // increase the number of Draws
                    System.out.println(String.format("%-5d %-10s %-20s $%-10.2f",
                            rs.getInt("DrawID"),
                            rs.getString("DrawType"),
                            rs.getString("DrawName"),
                            rs.getDouble("DrawPrice")));
               }

          } catch (SQLException e) {
               System.out.println("Error When Select All Draw_Table");
               e.printStackTrace();
          }
     }

     public static boolean login() {
          System.out.println("Enter your ID");
          userID = validInt();
          System.out.println("Enter your Password");
          String pass = validString();

          String query = "SELECT * FROM User_Table WHERE userID = ? AND userPass = ?";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, userID);
               pstmt.setString(2, pass);
               ResultSet rs = pstmt.executeQuery();
               if (rs.next()) {
                    return true;
               }

          } catch (SQLException e) {
               e.printStackTrace();
          }

          return false;
     }

     public static boolean register(boolean isAdmin) {
          String query = "INSERT INTO User_Table (userPass, userIdAdmin) VALUES (? , ?)";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
               pstmt.setString(1, validString());
               pstmt.setBoolean(2, isAdmin);
               pstmt.executeUpdate();

               try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                         int ID = rs.getInt(1);
                         System.out.println("User Added Successfully, ID : " + ID);
                    }
               }

          } catch (Exception e) {
               e.printStackTrace();
          }

          return true;
     }

     public static int validChoice(int max) {
          int choice;
          do {
               System.out.print("Enter a choice (1-" + max + "): ");
               while (!in.hasNextInt()) {
                    System.out.print("Invalid input. Try again: ");
                    in.next();
               }
               choice = in.nextInt();
          } while (choice < 1 || choice > max);
          return choice;
     }

     public static int validInt() {
          System.out.println("Enter Int : ");
          return in.nextInt();
     }

     public static String validString() {
          System.out.println("Enter String : ");
          return in.next();
     }

     public static int getRangeClothes() {
          return rangeClothes;
     }

     public static int getRangeColor() {
          return rangeColor;
     }

     public static int getRangeDraw() {
          return rangeDraw;
     }

     public static int getUserID() {
          return userID;
     }

     @Override
     public void updateTotal(int total) {
          this.userTotal = total;
          System.err.println("The Updating Total Is : " + userTotal);
     }

     public static void getAllOrdersByUser() {
          String orderQuery = "SELECT o.orderID, o.status, o.total, p.pieceID, p.subTotal, p.clothesID, "
                  + "c.clothesName, c.clothesPrice, p.colorID, co.colorName, co.colorPrice, "
                  + "d.drawID, d.drawName, d.drawPrice, d.drawType "
                  + "FROM Order_Table o "
                  + "JOIN Piece_Table p ON o.pieceID = p.pieceID "
                  + "JOIN Clothes_Table c ON p.clothesID = c.clothesID "
                  + "JOIN Colors_Table co ON p.colorID = co.colorID "
                  + "LEFT JOIN Piece_Draw_Table pd ON p.pieceID = pd.pieceID "
                  + "LEFT JOIN Draw_Table d ON pd.drawID = d.drawID "
                  + "WHERE o.userID = ? "
                  + "ORDER BY o.orderID, p.pieceID";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(orderQuery)) {
               pstmt.setInt(1, getUserID());
               ResultSet rs = pstmt.executeQuery();

               StringBuilder details = new StringBuilder();
               int currentOrderID = -1;
               int currentPieceID = -1;

               while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    String status = rs.getString("status");
                    double orderTotal = rs.getDouble("total");
                    int pieceID = rs.getInt("pieceID");
                    double pieceSubTotal = rs.getDouble("subTotal");

                    // Add a new order section
                    if (currentOrderID != orderID) {
                         if (currentOrderID != -1) {
                              details.append("\n"); // Add spacing between orders
                         }
                         currentOrderID = orderID;

                         details.append("Order ID: ").append(orderID).append("\n");
                         details.append("Status: ").append(status).append("\n");
                         details.append("Total: ").append(orderTotal).append("\n\n");
                         details.append("Pieces:\n");
                    }

                    // Add a new piece section
                    if (currentPieceID != pieceID) {
                         currentPieceID = pieceID;

                         details.append("  Piece ID: ").append(pieceID).append("\n");
                         details.append("    Subtotal: ").append(pieceSubTotal).append("\n");

                         // Add Clothes details
                         details.append("    Clothes: ").append(rs.getString("clothesName"))
                                 .append(" (Price: ").append(rs.getDouble("clothesPrice")).append(")\n");

                         // Add Color details
                         details.append("    Color: ").append(rs.getString("colorName"))
                                 .append(" (Price: ").append(rs.getDouble("colorPrice")).append(")\n");

                         // Add Drawings header
                         details.append("    Drawings:\n");
                    }

                    // Add Drawing details
                    String drawName = rs.getString("drawName");
                    if (drawName != null) {
                         details.append("      - ").append(drawName)
                                 .append(" (Type: ").append(rs.getString("drawType"))
                                 .append(", Price: ").append(rs.getDouble("drawPrice")).append(")\n");
                    }
               }

               System.out.println(details.toString());

          } catch (SQLException e) {
               System.err.println("Error retrieving orders for user.");
               e.printStackTrace();
          }
     }

     public static void addClothes() {
          System.out.println("Enter the name of the new clothes:");
          String clothesName = validString();

          System.out.println("Enter the price of the new clothes:");
          double clothesPrice = in.nextDouble();

          String query = "INSERT INTO Clothes_Table (clothesName, clothesPrice) VALUES (?, ?)";
          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setString(1, clothesName);
               pstmt.setDouble(2, clothesPrice);
               pstmt.executeUpdate();
               System.out.println("Clothes added successfully!");

          } catch (SQLException e) {
               System.out.println("Error adding clothes.");
               e.printStackTrace();
          }
     }

     public static void removeClothes() {
          System.out.println("Enter the ID of the clothes to remove:");
          int clothesID = validInt();

          String query = "DELETE FROM Clothes_Table WHERE clothesID = ?";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, clothesID);
               int rows = pstmt.executeUpdate();

               if (rows > 0) {
                    System.out.println("Clothes removed successfully!");
               } else {
                    System.out.println("No clothes found with the provided ID.");
               }

          } catch (SQLException e) {
               System.out.println("Error removing clothes.");
               e.printStackTrace();
          }
     }

     public static void addColor() {
          System.out.println("Enter the name of the new color:");
          String colorName = validString();

          System.out.println("Enter the price of the new color:");
          double colorPrice = in.nextDouble();

          String query = "INSERT INTO Colors_Table (colorName, colorPrice) VALUES (?, ?)";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setString(1, colorName);
               pstmt.setDouble(2, colorPrice);
               pstmt.executeUpdate();
               System.out.println("Color added successfully!");

          } catch (SQLException e) {
               System.out.println("Error adding color.");
               e.printStackTrace();
          }
     }

     public static void removeColor() {
          System.out.println("Enter the ID of the color to remove:");
          int colorID = validInt();

          String query = "DELETE FROM Colors_Table WHERE colorID = ?";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, colorID);
               int rows = pstmt.executeUpdate();

               if (rows > 0) {
                    System.out.println("Color removed successfully!");
               } else {
                    System.out.println("No color found with the provided ID.");
               }

          } catch (SQLException e) {
               System.out.println("Error removing color.");
               e.printStackTrace();
          }
     }

     public static void addDraw() {
          System.out.println("Enter the type of the draw (Name, Logo, Character):");
          String drawType = validString();

          System.out.println("Enter the name of the draw:");
          String drawName = validString();

          System.out.println("Enter the price of the draw:");
          double drawPrice = in.nextDouble();

          String query = "INSERT INTO Draw_Table (drawType, drawName, drawPrice) VALUES (?, ?, ?)";

          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setString(1, drawType);
               pstmt.setString(2, drawName);
               pstmt.setDouble(3, drawPrice);
               pstmt.executeUpdate();
               System.out.println("Draw added successfully!");

          } catch (SQLException e) {
               System.err.println("Error adding draw.");
               e.printStackTrace();
          }
     }

     public static void removeDraw() {
          System.out.println("Enter the ID of the draw to remove:");
          int drawID = validInt();

          String query = "DELETE FROM Draw_Table WHERE drawID = ?";
          try (PreparedStatement pstmt = DBInstance.getConnection().prepareStatement(query)) {
               pstmt.setInt(1, drawID);
               int rows = pstmt.executeUpdate();

               if (rows > 0) {
                    System.out.println("Draw removed successfully!");
               } else {
                    System.out.println("No draw found with the provided ID.");
               }

          } catch (SQLException e) {
               System.out.println("Error removing draw.");
               e.printStackTrace();
          }
     }

}
