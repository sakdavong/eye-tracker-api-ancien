/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hazydesigns.capstone.worldWindGazeInput;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWUtil;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import rit.eyeTrackingAPI.ApplicationUtilities.EyeTrackingFilterListener;
import rit.eyeTrackingAPI.DataConstructs.GazePoint;
import rit.eyeTrackingAPI.EyeTrackerUtilities.eyeTrackerClients.EyeTrackerClient;
import rit.eyeTrackingAPI.EyeTrackerUtilities.eyeTrackerClients.EyeTrackerClientSimulator;
import rit.eyeTrackingAPI.SmoothingFilters.Filter;
import rit.eyeTrackingAPI.SmoothingFilters.PassthroughFilter;
import sun.security.util.SecurityConstants;

/**
 *
 * @author mhazlewood
 */
public class MainFrame extends JFrame
{
   private final Dimension mCanvasSize = new Dimension(800, 600);
   private WorldWindPanel mMainViewPanel;
   
   // Eye tracker connection stuff
   private GazePoint mGazePoint;
   private Filter mSmoothingFilter;
   private EyeTrackerClient mEyeTrackerClient;
   private EyeTrackingFilterListener mEyeTrackerListener;
   
   
   private static final String TEST_FILE_PATH = System.getProperty("java.io.tmpdir") + "\\simulatedEyeData.txt";

   /**
    * Creates new form MainFrame
    */
   public MainFrame()
   {
      mSmoothingFilter = new PassthroughFilter();
      mGazePoint = new GazePoint(mSmoothingFilter);
      
      mEyeTrackerClient = new EyeTrackerClientSimulator(mGazePoint, TEST_FILE_PATH, (short)0, false);
      mEyeTrackerClient.connect();
      ((EyeTrackerClientSimulator)mEyeTrackerClient).setJitter(5);
      
      ActionListener l = new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            System.out.println("Here we are");
         }
      };
      
      mEyeTrackerListener = new EyeTrackerListener(mSmoothingFilter, l, false, 0);
   }
   
   private void initialize()
   {
      mMainViewPanel = new WorldWindPanel(mCanvasSize);
      
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(mMainViewPanel, BorderLayout.CENTER);

      // Register a rendering exception listener that's notified when exceptions occur during rendering.
      mMainViewPanel.getWorldWindow().addRenderingExceptionListener(new RenderingExceptionListener()
      {
         @Override
         public void exceptionThrown(Throwable t)
         {
            if (t instanceof WWAbsentRequirementException)
            {
               String message = "Computer does not meet minimum graphics requirements.\n";
               message += "Please install up-to-date graphics driver and try again.\n";
               message += "Reason: " + t.getMessage() + "\n";
               message += "This program will end when you press OK.";

               JOptionPane.showMessageDialog(MainFrame.this, message, "Unable to Start Program",
                       JOptionPane.ERROR_MESSAGE);
               System.exit(-1);
            }
         }
      });

      // Search the layer list for layers that are also select listeners and register them with the World
      // Window. This enables interactive layers to be included without specific knowledge of them here.
      for (Layer layer : mMainViewPanel.getWorldWindow().getModel().getLayers())
      {
         if (layer instanceof SelectListener)
         {
            mMainViewPanel.getWorldWindow().addSelectListener((SelectListener) layer);
         }
      }

      this.pack();

      // Center the application on the screen.
      WWUtil.alignComponent(null, this, AVKey.CENTER);
      this.setResizable(true);
      
      mEyeTrackerClient.start();
      mEyeTrackerListener.start();
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 300, Short.MAX_VALUE)
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   /**
    * @param args the command line arguments
    */
   public static void main(String args[])
   {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try
      {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      }
      catch (ClassNotFoundException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (InstantiationException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (IllegalAccessException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (javax.swing.UnsupportedLookAndFeelException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            MainFrame main = new MainFrame();
            main.setTitle("World Wind Gaze Input");
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            main.initialize();
            main.setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables

   
   private class EyeTrackerListener extends EyeTrackingFilterListener
   {
      Robot mRobot;
      
      public EyeTrackerListener(Filter filter, ActionListener actionListener,
                                 boolean paintingFixations, int display)
      {
         super(filter, actionListener, paintingFixations, display);
         
         try
         {
            mRobot = new Robot();
         }
         catch (AWTException ex)
         {
            ex.printStackTrace();
         }
      }
   
      /**
       * This function will be called whenever the filter owned by this class has a
       * new gaze point to report.
       *
       * @param newUserGazePoint, the new gaze point
       */
      @Override
      protected void newPoint(Point newUserGazePoint)
      {
         mRobot.mouseMove(newUserGazePoint.x, newUserGazePoint.y);
         System.out.println(newUserGazePoint);
      }

      @Override
      protected void updateCursorCoordinates()
      {
         System.out.println("");
      }
   }
   
}