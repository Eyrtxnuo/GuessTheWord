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
        BACKGROUND("sala.png");
        
        private String path;

        private Sprites(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    } 
    public static HashMap<Sprites,BufferedImage> fileCache = new HashMap<>();
    
    public static BufferedImage getImage(Sprites atlas){
        if(!fileCache.containsKey(atlas)){
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
    public static Font RegisterFont(String PathToFont) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font newFont = Font.createFont(Font.TRUETYPE_FONT, LoadSave.class.getResourceAsStream("/font/"+PathToFont));
            ge.registerFont(newFont);
            return newFont;
        } catch (IOException|FontFormatException e) {
            return null;
        }
    }
}
