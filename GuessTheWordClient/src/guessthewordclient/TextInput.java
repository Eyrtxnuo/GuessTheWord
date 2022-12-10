package guessthewordclient;

import static guessthewordclient.TextInput.TextInputValues.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import utils.LoadSave;
import utils.Utils;

/**
 *
 * @author matti
 */
public class TextInput {

    private int xPos, yPos, index;

    private int yOffestCenter = TI_HEIGHT / 2;

    private boolean cursor = true;
    private boolean insert = false;

    private int cursorIndex = 0;

    private String text = "";
    private int leftSkipped = 0;
    private int rightSkipped = 0;

    private int minCharWidth = 54;

    private static final String PREFIX = "…";
    private static final String SUFFIX = "…";

    private final Font font = LoadSave.getOrRegisterFont("minecraft-gnu-font.otf","Minecraft").deriveFont(50 * SCALE);

    private int animTick;
    private final int animSpeed = 100;

    private BufferedImage[] imgs;

    private Rectangle bounds;
    private final int leftBorder = (int) (new Canvas().getFontMetrics(font).stringWidth(PREFIX) + 4 * SCALE);
    private final int rightBorder = (int) (new Canvas().getFontMetrics(font).stringWidth(SUFFIX) + 4 * SCALE);
    private final int upperBorder = (int) (9 * SCALE);
    
    

    public TextInput(int xPos, int yPos) {

        this.xPos = xPos;
        this.yPos = yPos;
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos - yOffestCenter, TI_WIDTH, TI_HEIGHT);
    }

    String colorMap="";
    public void setColorMap(String colorMap){
        this.colorMap = colorMap;
    }
    
    public void draw(Graphics g) {

        //g.drawImage(imgs[index], xPos, yPos - yOffestCenter, bounds.width, bounds.height, null);
        Color old = g.getColor();
        g.setColor(Color.black);

        g.setFont(font);
        String printText = text;
        int xOffset = 0;
        int forIndex = 0;
        for (char ch : printText.toCharArray()) {
            if(forIndex<colorMap.length()){
                switch(colorMap.charAt(forIndex)){
                    case '!' -> g.setColor(Color.GREEN);
                    case '*' -> g.setColor(Color.ORANGE);
                    case '?' -> g.setColor(Color.GRAY);
                    default -> g.setColor(Color.BLACK);
                }
            }else{
                g.setColor(Color.BLACK);
            }
            g.drawString(ch + "", (int) (xPos + leftBorder + xOffset), (int) (yPos + upperBorder));
            xOffset += Math.max(minCharWidth, g.getFontMetrics().charWidth(ch));
            forIndex++;
        }

        g.setColor(old);
    }

    public void update() {
        animTick++;
        if (animTick > animSpeed) {
            animTick %= animSpeed;
            cursor = !cursor;
        }
        index = 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void processKeyEvent(KeyEvent e) {

        if (Utils.isPrintableChar(e.getKeyChar(), font)) {
            append(e.getKeyChar());
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> {
                    if (cursorIndex == text.length()) {
                        break;
                    }
                    int intsertionPoint = text.length() - cursorIndex;
                    text = text.substring(0, intsertionPoint - 1) + text.substring(intsertionPoint);
                }

                case KeyEvent.VK_DELETE -> {
                    if (cursorIndex == 0) {
                        break;
                    }
                    int intsertionPoint = text.length() - cursorIndex;
                    text = text.substring(0, intsertionPoint) + text.substring(intsertionPoint + 1);
                    cursorIndex--;
                }

                case KeyEvent.VK_V -> {
                    if (!e.isControlDown()) {
                        break;
                    }
                    {
                        try {
                            append((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                        } catch (UnsupportedFlavorException | IOException ex) {
                            System.out.println("Clipboard is not text!");
                        }
                    }
                }
                /*case KeyEvent.VK_LEFT,KeyEvent.VK_DOWN -> {
                    if (cursorIndex < text.length()) {
                        cursorIndex++;
                    }
                }
                case KeyEvent.VK_RIGHT,KeyEvent.VK_UP -> {
                    if (cursorIndex > 0) {
                        cursorIndex--;
                    }
                }
                case KeyEvent.VK_INSERT -> {
                    insert = !insert;
                }*/
            }

        }
    }

    public void append(String str) {
        for (Character ch : str.toCharArray()) {
            append(ch);
        }
    }

    public void append(Character ch) {
        if (text.length() < 5 && Utils.isPrintableChar(ch, font)) {
            int insertionPoint = text.length() - cursorIndex;
            int reinsertionPoint = insertionPoint;
            if (insert && cursorIndex > 0) {
                cursorIndex--;
                reinsertionPoint += 1;
            }
            text = text.substring(0, insertionPoint) + ch + text.substring(reinsertionPoint);

        }
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text =  text;
    }

    public static class TextInputValues {

        public final static float SCALE = 1;
        public final static int TI_WIDTH_DEFAULT = 404;
        public final static int TI_HEIGHT_DEFAULT = 44;
        public static int TI_WIDTH = (int) (TI_WIDTH_DEFAULT * SCALE);
        public static int TI_HEIGHT = (int) (TI_HEIGHT_DEFAULT * SCALE);
    }
}
