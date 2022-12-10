/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guessthewordclient;

import static guessthewordclient.KeyboardGraphics.KeyboardGraphicsValues.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author matti
 */
public class KeyboardGraphics {

    private int xPos, yPos;

    private final Font font = new Font("Sans Serif", 0, 20);

    private Rectangle bounds;
    private Component component;

    public KeyboardGraphics(int xPos, int yPos, Component c) {
        this.component = c;
        this.xPos = xPos;
        this.yPos = yPos;
        initBounds();
        
        int chN = 0;
        for(var keyRow : keys){
            for(int i = 0; i < keyRow.length; i++){
                keyRow[i] = new KeyboardKey(chars.charAt(chN++));
            }
        }
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos, KB_WIDTH, KB_HEIGHT);
    }

    KeyboardKey[][] keys = {new KeyboardKey[10],new KeyboardKey[9],new KeyboardKey[7]};
    String chars = "qwertyuiopasdfghjklzxcvbnm".toUpperCase();
    
    
    public void draw(Graphics g) {
        Color old = g.getColor();
        //g.drawImage(imgs[index], xPos, yPos - yOffestCenter, bounds.width, bounds.height, null);
        
        g.setColor(KB_BACKGROUND_COLOR);
        
        g.setFont(font);
        g.fillRoundRect(xPos, yPos, KB_WIDTH, KB_HEIGHT, KB_ROUNDING, KB_ROUNDING);
        
        
        int row = 0;
        for(var keyRow : keys){
            int column = 0;
            
            float buttonSpacing = 20;//(KB_WIDTH-(keyRow.length*KeyboardKey.KeyGraphicsValues.KEY_WIDTH))/keyRow.length;
            float leftSpacing = (KB_WIDTH-keyRow.length*(KeyboardKey.KeyGraphicsValues.KEY_WIDTH+buttonSpacing)-buttonSpacing)/2;
            
            for(var key : keyRow){
                key.draw((int) (xPos+leftSpacing+buttonSpacing+column*(key.getBounds().getWidth()+buttonSpacing)), (int) (30+yPos+row*(key.getBounds().getHeight()+20)), g);
                column++;
            }
            row++;
        }
        
        g.setColor(old);
    }


    public Rectangle getBounds() {
        return bounds;
    }

  
    public static class KeyboardGraphicsValues {
        public final static float SCALE = 1;
        public final static int KB_WIDTH_DEFAULT = 700;
        public final static int KB_HEIGHT_DEFAULT = 250;
        public final static int KB_ROUNDING_DEFAULT = 40;
        public static int KB_WIDTH = (int) (KB_WIDTH_DEFAULT * SCALE);
        public static int KB_HEIGHT = (int) (KB_HEIGHT_DEFAULT * SCALE);
        public static int KB_ROUNDING = (int) (KB_ROUNDING_DEFAULT * SCALE);
        public final static Color KB_BACKGROUND_COLOR = new Color(204,205,209);
    }
}
