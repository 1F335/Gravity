//Adapted from Mr. Torbert's PolkaDotPanel

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class GravityPanel extends JPanel
{
//constants
   private static final int FRAME = 800;   //frame size
   private static final Color BACKGROUND = new Color(0, 0, 0);
   private static final int G = 500;      //gravitational constant
   private static final int DELAY = 20;   // delay between ticks
//fields
   private BufferedImage myImage;
   private Graphics myBuffer;
   private static JFrame myFrame;
   private Timer t;
   private float lx, ly, vx, vy;          //location x, y, velocity x, y
   public GravityPanel()
   {
      myImage =  new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myBuffer.setColor(BACKGROUND);
      myBuffer.fillRect(0, 0, FRAME, FRAME);
      addMouseListener(new Mouse());
      t = new Timer(DELAY, new Listener());
      t.start();
      lx = FRAME / 2;//start in the center
      ly = FRAME / 2;
      vx = 0;        // start with 0 velocity
      vy = 0;
   }
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
      
         Point p = myFrame.getMousePosition();     // get mouse position
         if(p == null)// if mouse is not in window
         {
            repaint();//don't run simulation this tick
            return;
         }
         // fill background
         myBuffer.setColor(new Color(0, 0, 0, 10));      //transparency for trail (R G B A)
         myBuffer.fillRect(0, 0, FRAME, FRAME);
         
         mouseTranslate(p);
         
         // PHYSICS CODE----------------------------------------------------
         double fx, fy;
         // Calc distance
         double d = Math.sqrt(Math.pow(p.x - lx, 2) + Math.pow(p.y - ly, 2));
         
         // force of gravity is inverse square, e.g. strenth / distance ^ 2
         // (G is gravitational constant)
         double f = G / (d * d);
         
         // Force components are
         // total force * component of    unit vector pointing towards mouse
         fx = f         * Math.cos(       Math.atan2(p.y - ly, p.x - lx));
         fy = f         * Math.sin(       Math.atan2(p.y - ly, p.x - lx));
         
         // change velocity with force
         vx += fx;
         vy += fy;
         
         // change location with velocity
         lx += vx;
         ly += vy;
         
         // END PHYSICS------------------------------------------------------
         
         //cast location for graphics
         int glx = (int)lx;
         int gly = (int)ly;
         
         //draw dot
         myBuffer.setColor(new Color(255, 255, 255));
         myBuffer.fillOval(glx - 4, gly - 4, 9,9);
         
         //draw velocity vector
         myBuffer.setColor(new Color(0, 255, 0));
         myBuffer.drawLine(glx, gly, glx + (int)(vx * 10), gly + (int)(vy * 10));
         
         //draw force vector
         myBuffer.setColor(new Color(0, 0, 255));
         myBuffer.drawLine(glx, gly, glx + (int)(fx * 90), gly + (int)(fy * 90));
         
         repaint();
      }
   }
   private class Mouse extends MouseAdapter
   {
      public void mousePressed(MouseEvent e)
      {
         // reset velocity to 0 when mouse is pressed
         vx = 0;
         vy = 0;
      }
   }
   
   public static void main(String[] args)
   {
      //frame stuff
      myFrame = new JFrame("Gravity!");
      myFrame.setSize(FRAME+10,FRAME+20);
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myFrame.setContentPane(new GravityPanel());
      myFrame.setLocationRelativeTo(null);
      myFrame.setVisible(true);
   }
   
   private static void mouseTranslate(Point p)
   {
      //translate jframe mouse position to match graphical mouse position
      p.x -= 7;
      p.y -= 24;
   }
}