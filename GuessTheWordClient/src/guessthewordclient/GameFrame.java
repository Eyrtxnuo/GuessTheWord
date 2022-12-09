/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package guessthewordclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.LoadSave.Sprites.*;
import static utils.LoadSave.getImage;
import utils.Updater;

/**
 *
 * @author matti
 */
public class GameFrame extends javax.swing.JFrame implements KeyListener{

    /**
     * Creates new form GameFrame
     */
    DataOutputStream output;
    
    public GameFrame(DataOutputStream output) {
        this.output = output;
        initComponents();
        addKeyListener(this);
        startFrameRendering();
    }
    Updater frameRepainter = new Updater(() -> {
            this.paint(this.getGraphics());
            return null;
        }, 60);
    Updater gcCaller = new Updater(() -> {
            System.gc();
            return null;
        }, 1);
    
    public void startFrameRendering(){
        frameRepainter.startThread();
        gcCaller.startThread();
    }
    
    public void stopFrameRendering(){
        frameRepainter.stopThread();
        gcCaller.stopThread();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setName("Frame"); // NOI18N
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    int frames=0;
    Font textFont = new Font("Serif",0, 40);
    TextInput ti = new TextInput(725, 270);
    @Override
    public void paint(Graphics gScreen) {
        Image imm = createVolatileImage(this.getWidth() - this.getInsets().left - this.getInsets().right, this.getHeight() - this.getInsets().top - this.getInsets().bottom);
        Graphics g = imm.getGraphics();
        float mov = frames*0.3f;
        g.drawImage(getImage(BACKGROUND),0,0,imm.getWidth(this),imm.getHeight(this), this);
        if(result){
            if(gotIt==null){
                g.setColor( Color.LIGHT_GRAY);
            }else if(gotIt==true){
                g.setColor( Color.GREEN);
            }else if(gotIt==false){
                g.setColor( Color.RED);
            }
            
            g.fillRect(750, 174, 271, 178);
        }
        g.drawImage(getImage(TIZIO), 550+(int) (mov%14-(mov%14-7)*(mov%14-7>0?2:0)), 200+(int) (mov%10-(mov%10-5)*(mov%10-5>0?2:0)), this);
        g.drawImage(getImage(SCRIVANIA),500,350,400,130, this);
        g.setFont(textFont);
        g.drawString("Frames: " + ++frames, 60, 50);
        ti.draw(g);
        gScreen.drawImage(imm, this.getInsets().left, this.getInsets().top, this);
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private boolean result = false;
    Boolean gotIt = false;
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(result == true){
                ti.setText("");
                result = false;
                ti.setColorMap("");
                return;
            }
            if(ti.getText().length()==5){
                try {
                    gotIt = null;
                    output.writeUTF(ti.getText());
                    result = true;
                    return;
                } catch (IOException ex) {
                    Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                    result = false;
                }
            }
            
            
        }
        if(!result){
            ti.processKeyEvent(e);
        }
        //System.out.println(e.getKeyChar()+":"+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public Boolean isGotIt() {
        return gotIt;
    }

    public void setGotIt(Boolean gotIt) {
        this.gotIt = gotIt;
    }

    
    public void setInputColorMap(String ColorMap){
        ti.setColorMap(ColorMap);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
