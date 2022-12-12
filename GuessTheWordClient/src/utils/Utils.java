/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 *
 * @author matti
 */
public abstract class Utils {
    
    public static boolean isPrintableChar( char c , Font font  ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
            c != KeyEvent.CHAR_UNDEFINED &&
            block != null &&
            block != Character.UnicodeBlock.SPECIALS &&
            font.canDisplay(c);
    }
    
    public static class Tuple<X, Y> { 
        public final X x; 
        public final Y y; 
        public Tuple(X x, Y y) { 
        this.x = x; 
        this.y = y; 
        } 
    } 
}
