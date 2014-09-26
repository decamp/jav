/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


/** 
 * @author Philip DeCamp  
 */
public class ImagePanel extends JPanel {

    public static JFrame showImage(File file) {
        return showImage(file, true);
    }
    
    public static JFrame showImage(File file, boolean exitOnClose) {
        try{
            BufferedImage image = ImageIO.read(file);
            return showImage(image, exitOnClose);
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }   
    }
    
    public static JFrame showImage(BufferedImage image){
        return showImage(image, true);
    }
    
    public static JFrame showImage(BufferedImage image, boolean exitOnClose) {
        JFrame frame = frameImagePanel(new ImagePanel(image), exitOnClose);
        frame.setVisible(true);
        return frame;
    }
        
    
    public static JFrame frameImagePanel(ImagePanel panel) {
        return frameImagePanel(panel, true);
    }
    
    public static JFrame frameImagePanel(ImagePanel panel, boolean exitOnClose){
        JFrame frame = new JFrame();
        if(exitOnClose) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }else{
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        
        BufferedImage image = panel.getImage();
        if(image == null){
            frame.setBounds(100, 100, 500, 500);
        }else{
            frame.setBounds(100, 100, image.getWidth() + 10, image.getHeight() + 40);
        }

        frame.setContentPane(panel);
        frame.pack();

        if(image != null) {
            frame.setSize(frame.getWidth() + image.getWidth() - panel.getWidth(),
                          frame.getHeight() + image.getHeight() - panel.getHeight());
        }
        
        return frame;
    }
    
    public static JFrame framePanel(JPanel panel) {
        return framePanel(panel, true);
    }
    
    public static JFrame framePanel(JPanel panel, boolean exitOnClose) {
        JFrame frame = new JFrame();
        
        if(exitOnClose) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }else{
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        
        frame.setBounds(100, 100, 500, 500);
        frame.setContentPane(panel);
        return frame;
    }
    
    
    private BufferedImage mImage;
    private boolean mResize = true;
    private boolean mKeepAspectRatio = true;
    
    public ImagePanel(){
        this(null);
    }
    
    public ImagePanel(BufferedImage image){
        mImage = image;
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getButton() == 3){
                    mResize = !mResize;
                    repaint();
                }
            }
        });
    }
    
    
    
    public synchronized void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D)gg;
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
        if(mImage != null){
            if(mResize){
                if(mKeepAspectRatio) {
                    if(getWidth() * mImage.getHeight() < getHeight() * mImage.getWidth()) {
                        int h = getWidth() * mImage.getHeight() / mImage.getWidth();
                        g.drawImage(mImage, 0, 0, getWidth(), h, null);

                    }else{
                        int w = getHeight() * mImage.getWidth() / mImage.getHeight();
                        g.drawImage(mImage, 0, 0, w, getHeight(), null);
                    }
                }else{
                    g.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);
                }
            }else{
                g.drawImage(mImage, 0, 0, null);
            }
        }
    }
    
    public synchronized void setImage(BufferedImage image) {
        mImage = image;
        repaint();
    }
    
    public BufferedImage getImage(){
        return mImage;
    }
}

