package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/** Methods to load maps, audios and textures
 *
 * @author matti
 */
public class LoadSave {
    
    /** sprites path */
    public enum Sprites{
        BACKGROUND("textures/salaLibera.png"),
        SCRIVANIA("textures/scrivania.png"),
        TIZIO("textures/tizio.png"),
        TASTIERA("textures/keyboard.png");
        
        private final String path;

        private Sprites(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    } 
    public static HashMap<Sprites,BufferedImage> fileCache = new HashMap<>();//change to volatileimage
    
    public static synchronized BufferedImage getImage(Sprites atlas){
        if(!fileCache.containsKey(atlas)){
            System.out.println("cache miss: "+atlas.getPath());
            /*System.out.println("Printing stack trace:");
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (int i = 1; i < elements.length; i++) {
                 StackTraceElement s = elements[i];
                 System.out.println("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
            }*/
            fileCache.put(atlas, LoadImage(atlas.getPath()));
        }
        return fileCache.get(atlas);
    }
    
    
    /** load sprite form local path
     * @param atlas altlas path
     * @return loaded Image*/
    public static BufferedImage LoadImage(String atlas){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+atlas);
        try {
           img = ImageIO.read(is);
            
        } catch (IOException ex) {
            Logger.getLogger(LoadSave.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(LoadSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return img;
    }
    
    /**
     * Loads and register a font from the file path 
     * 
     * @param PathToFont path to the font file (*.otf and *.ttf are supported)
     * @return the newly created font reference, this Font can now be used in Graphics
     */
    public static Font RegisterFont(String PathToFont, String name) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font newFont = Font.createFont(Font.TRUETYPE_FONT, LoadSave.class.getResourceAsStream("/font/"+PathToFont));
            ge.registerFont(newFont);
            fontCache.put(name, newFont);
            return newFont;
        } catch (IOException|FontFormatException e) {
            return null;
        }
    }
    
    public static HashMap<String,Font> fontCache = new HashMap<>();
    
    public static Font getFont(String Name){
        return fontCache.get(Name);
    }
    
    public static Font getOrRegisterFont(String PathToFont, String name){
        if(fontCache.containsKey(name)){
            return getFont(name);
        }else{
            return RegisterFont(PathToFont, name);
        }
    }
    
    {
        getOrRegisterFont("minecraft-gnu-font.otf","Minecraft");
    }
}
