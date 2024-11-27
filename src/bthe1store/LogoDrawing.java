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
public class LogoDrawing extends Drawing {

     public LogoDrawing(int drawID, Drawing drawing) {
          super(drawID, drawing);
     }

     @Override
     public String getDescribtion() {
          return drawing.getDescribtion() + "\nLogo: " + drawName + "\tPrice : " + drawPrice;
     }
     
     public int getTotalPrice() {
          return drawing.getTotalPrice() + drawPrice;
     }
}
