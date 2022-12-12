/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guessthewordclient;

import static guessthewordclient.KeyboardKey.KeyGraphicsValues.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author matti
 */
public class KeyboardKey {

    private final Font font = new Font("Sans Serif", 0, 40);//LoadSave.getOrRegisterFont("minecraft-gnu-font.otf","Minecraft").deriveFont(40f * SCALE);

    private Rectangle bounds;

    private char symbol;
    
    //private STATUS currentStatus = STATUS.NONE;

    public KeyboardKey(char symbol) {
        this.symbol = symbol;
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(0, 0, KEY_WIDTH, KEY_HEIGHT);
    }

    public void draw(int x, int y, Graphics g) {

        //g.drawImage(imgs[index], xPos, yPos - yOffestCenter, bounds.width, bounds.height, null);
        Color old = g.getColor();
        g.setColor(GameFrame.pressedChars.contains(Character.toLowerCase(symbol)) ? GameFrame.getCharStatus(Character.toLowerCase(symbol)).getColor().darker() : GameFrame.getCharStatus(Character.toLowerCase(symbol)).getColor());

        g.setFont(font);

        g.fillRoundRect(x, y, KEY_WIDTH, KEY_HEIGHT, KEY_ROUNDING, KEY_ROUNDING);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, KEY_WIDTH, KEY_HEIGHT, KEY_ROUNDING, KEY_ROUNDING);
        int xStringPos = (KEY_WIDTH - g.getFontMetrics().charWidth(symbol)) / 2;//8 è lo spazio al destra del carattere vuota 
        int yStringPos = (KEY_HEIGHT - (g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent())) / 2 + (g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent());
        /*g.fillRect(x+xStringPos, y+yStringPos-(g.getFontMetrics().getAscent()-g.getFontMetrics().getDescent()), g.getFontMetrics().charWidth(symbol), (g.getFontMetrics().getAscent()-g.getFontMetrics().getDescent()));
        g.setColor(Color.WHITE);*/
        g.drawString(symbol + "", x + xStringPos, y + yStringPos);

        g.setColor(old);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public static class KeyGraphicsValues {

        public final static float SCALE = 1;
        public final static int KEY_WIDTH_DEFAULT = 50;
        public final static int KEY_HEIGHT_DEFAULT = 50;
        public final static int KEY_ROUNDING_DEFAULT = 20;
        public final static Color KEY_BACKGROUND_COLOR = new Color(164, 165, 165);
        public static int KEY_WIDTH = (int) (KEY_WIDTH_DEFAULT * SCALE);
        public static int KEY_HEIGHT = (int) (KEY_HEIGHT_DEFAULT * SCALE);
        public static int KEY_ROUNDING = (int) (KEY_ROUNDING_DEFAULT * SCALE);
    }

    public enum STATUS {
        NONE(new Color(164, 165, 165)),
        NOT_PRESENT(new Color(164, 165, 165).darker()),
        PRESENT(Color.ORANGE),
        FOUND(Color.GREEN);
        
        final Color color; 

        private STATUS(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }        
    }
}
