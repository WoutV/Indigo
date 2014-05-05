/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import map.Map;

import zeppelin.Propellor;

public class GuiMain extends javax.swing.JFrame {

    /**
     * Creates new form GuiMain
     */
    public GuiMain() {
        initComponents();
        init2();
    }
    
    private void init2() {

		InputStream resource = GuiMain.class.getResourceAsStream("/ballon.jpg");
		try {
			Image image = ImageIO.read(resource);
			this.setIconImage(image);
		} catch (IOException e) {}

		//initialise all GUIEvents to true
		typevisibility.put(GUIEvent.EventType.HeightReceived, true);
		typevisibility.put(GUIEvent.EventType.Misc, true);
		typevisibility.put(GUIEvent.EventType.SentOther, true);
		typevisibility.put(GUIEvent.EventType.ReceivedOther, true);
		typevisibility.put(GUIEvent.EventType.PropStatus, true);
		typevisibility.put(GUIEvent.EventType.ReceivedLocation, true);

		//initialise the propellor icons
		resource = GuiMain.class.getResourceAsStream("/propelloractive.jpg");
		try {
			Image image = ImageIO.read(resource);
			propact = new ImageIcon(image);
		} catch (IOException e) {}
		resource = GuiMain.class.getResourceAsStream("/propellornotactive.jpg");
		try {
			Image image = ImageIO.read(resource);
			propnotact = new ImageIcon(image);
		} catch (IOException e) {}

		propellorNotActive(Propellor.X);
		propellorNotActive(Propellor.UP);
		propellorNotActive(Propellor.Y);

		//initialise tab 3
		photoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		resource = GuiMain.class.getResourceAsStream("/zepp.jpg");
		try {
			Image image = ImageIO.read(resource);
			photoLabel.setIcon(new ImageIcon(image));
		} catch (IOException e) {}

		creditsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		creditsLbl.setText("<html><p align=\"center\">IndigoZeppelin v4.00: " +
				"Episode IV <br> " +
				"&copy Team Indigo. All rights reserved. </p></html>");

		//add map of playing field
		mapofplayingfield.setSize(495, 495);
		mapPanel.add(mapofplayingfield);
		
		disableAllComponents(this);
		
		jTabbedPane1.setToolTipText(null);
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        overviewTab = new javax.swing.JLayeredPane();
        commandPanel = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        ownHeightTxt = new javax.swing.JLabel();
        ownHeightDisplay = new javax.swing.JLabel();
        enemyHeightDisplay = new javax.swing.JLabel();
        enemyHeightTxt = new javax.swing.JLabel();
        leftPropDisplay = new javax.swing.JLabel();
        leftPropTxt = new javax.swing.JLabel();
        rightPropDisplay = new javax.swing.JLabel();
        rightPropTxt = new javax.swing.JLabel();
        upPropDisplay = new javax.swing.JLabel();
        setTarget = new javax.swing.JButton();
        upPropTxt = new javax.swing.JLabel();
        tab1LabelDisplay = new javax.swing.JLabel();
        mapPanel = new javax.swing.JPanel();
        mapDisplay = new javax.swing.JLabel();
        mapDisplayEnemy = new javax.swing.JLabel();
        commandlistTab = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        shownGUIEventList = new javax.swing.JTextArea();
        filterBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        credittab = new javax.swing.JPanel();
        creditsLbl = new javax.swing.JLabel();
        photoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Indigo Zeppelin");
        setResizable(false);

        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 600));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 600));

        ownHeightTxt.setText("Own height : ");

        ownHeightDisplay.setBackground(new java.awt.Color(0, 0, 0));
        ownHeightDisplay.setForeground(new java.awt.Color(0, 255, 0));
        ownHeightDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ownHeightDisplay.setText("Hier komt de hoogte!");
        ownHeightDisplay.setOpaque(true);

        enemyHeightDisplay.setBackground(new java.awt.Color(0, 0, 0));
        enemyHeightDisplay.setForeground(new java.awt.Color(255, 0, 51));
        enemyHeightDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enemyHeightDisplay.setText("Hier komt enemy hoogte!");
        enemyHeightDisplay.setOpaque(true);

        enemyHeightTxt.setText("Enemy height : ");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enemyHeightTxt)
                    .addComponent(ownHeightTxt, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enemyHeightDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(ownHeightDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ownHeightTxt)
                    .addComponent(ownHeightDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemyHeightTxt)
                    .addComponent(enemyHeightDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        leftPropDisplay.setText("jLabel1");
        leftPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        leftPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        leftPropTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftPropTxt.setText("X");

        rightPropDisplay.setText("jLabel1");
        rightPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        rightPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        rightPropTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightPropTxt.setText("Y");

        upPropDisplay.setText("jLabel1");
        upPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        upPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        setTarget.setText("Target ...");
        setTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTargetActionPerformed(evt);
            }
        });

        upPropTxt.setText("Up");

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(leftPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(commandPanelLayout.createSequentialGroup()
                                .addComponent(leftPropTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(upPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rightPropTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rightPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(121, 121, 121))
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(setTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(upPropTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tab1LabelDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addComponent(tab1LabelDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leftPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rightPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(upPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(leftPropTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rightPropTxt)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upPropTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(setTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        overviewTab.add(commandPanel);
        commandPanel.setBounds(520, 0, 280, 550);

        mapPanel.setMinimumSize(new java.awt.Dimension(50, 500));
        mapPanel.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        overviewTab.add(mapPanel);
        mapPanel.setBounds(10, 0, 500, 500);

        mapDisplay.setBackground(new java.awt.Color(204, 102, 255));
        mapDisplay.setForeground(new java.awt.Color(255, 255, 255));
        mapDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mapDisplay.setText("map display");
        mapDisplay.setOpaque(true);
        overviewTab.add(mapDisplay);
        mapDisplay.setBounds(10, 500, 500, 28);

        mapDisplayEnemy.setBackground(new java.awt.Color(204, 0, 0));
        mapDisplayEnemy.setForeground(new java.awt.Color(255, 255, 255));
        mapDisplayEnemy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mapDisplayEnemy.setText("map display enemy");
        mapDisplayEnemy.setOpaque(true);
        overviewTab.add(mapDisplayEnemy);
        mapDisplayEnemy.setBounds(10, 530, 500, 28);

        jTabbedPane1.addTab("overview", overviewTab);

        shownGUIEventList.setColumns(20);
        shownGUIEventList.setRows(5);
        jScrollPane2.setViewportView(shownGUIEventList);

        filterBtn.setText("filter ...");
        filterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout commandlistTabLayout = new javax.swing.GroupLayout(commandlistTab);
        commandlistTab.setLayout(commandlistTabLayout);
        commandlistTabLayout.setHorizontalGroup(
            commandlistTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandlistTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(commandlistTabLayout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addComponent(filterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        commandlistTabLayout.setVerticalGroup(
            commandlistTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandlistTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(commandlistTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("commandlist", commandlistTab);

        javax.swing.GroupLayout credittabLayout = new javax.swing.GroupLayout(credittab);
        credittab.setLayout(credittabLayout);
        credittabLayout.setHorizontalGroup(
            credittabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, credittabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(credittabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(photoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(creditsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE))
                .addContainerGap())
        );
        credittabLayout.setVerticalGroup(
            credittabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(credittabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(photoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("credits", credittab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBtnActionPerformed
        //checkbox voor elke GUIEvent.EventType
        JCheckBox heightreceived = new JCheckBox("Height Received");
        JCheckBox propstatus = new JCheckBox("PropStatus");
        JCheckBox receivedlocation = new JCheckBox("Received Location");
        JCheckBox receivedother = new JCheckBox("ReceivedOther");
        JCheckBox sentother = new JCheckBox("SentOther");
        JCheckBox misc = new JCheckBox("Misc");
        Object[] p = {"Choose which GUIEvents are shown:",heightreceived,propstatus,receivedother,
            receivedlocation,sentother,misc};
        int a = JOptionPane.showConfirmDialog(this, p,"Filter GUI events",JOptionPane.OK_CANCEL_OPTION);
        if(a == JOptionPane.OK_OPTION) {
            //update typevisibility
            typevisibility.put(GUIEvent.EventType.HeightReceived,heightreceived.isSelected());
            typevisibility.put(GUIEvent.EventType.PropStatus,propstatus.isSelected());
            typevisibility.put(GUIEvent.EventType.ReceivedLocation,receivedother.isSelected());
            typevisibility.put(GUIEvent.EventType.ReceivedOther,receivedother.isSelected());
            typevisibility.put(GUIEvent.EventType.SentOther,sentother.isSelected());
            typevisibility.put(GUIEvent.EventType.Misc,misc.isSelected());
            remakeGUIEvents();
        }
    }//GEN-LAST:event_filterBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        fullGUIEventList.clear();
        GUIEvent g = new GUIEvent(GUIEvent.EventType.Misc,getTime() + " -- Commandlist CLEARED -- ");
        fullGUIEventList.add(g);
        for(GUIEvent.EventType type : typevisibility.keySet())
        typevisibility.put(type, true);
        remakeGUIEvents();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void setTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTargetActionPerformed
//    	JLabel lbl = new JLabel("<html>Set a new target location for the zeppelin (in cm): <br> " + 
//    			"Values between 0 and 400. <br> (left,up) is (0,0). <br> " +
//    			"Input format: ---- x , y ---- OR  ---- ( x , y ) ---- </html>");
//    	
//        String a = JOptionPane.showInputDialog(lbl);
//        boolean noinput = true;
//        if(a != null) {
//           if(coord.length == 2) {
//        	   String x0 = coord[0].trim();
//        	   if(x0.charAt(0) == '(') 
//        		   x0 = x0.substring(1,x0.length()).trim();
//        	   String y0 = coord[1].trim();
//        	   if(y0.charAt(y0.length()-1) == ')') 
//        		   y0 = y0.substring(0,y0.length()-1).trim();
//        	   int x = 0;
//        	   int y = 0;
//        	   try {
//        		   x = Integer.parseInt(x0);
//        		   y = Integer.parseInt(y0);
//        		   guic.sendTarget(x,y);
//        		   this.setTargetLocation(x, y);
//        		   noinput = false;
//        	   }
//        	   catch (NumberFormatException exc){
//        	   }
//           }
//        }
//        if(noinput) {
//        	JOptionPane.showMessageDialog(this, "No new target sent.");
//        }
        
    	
    	JLabel lbl = new JLabel("<html> Give tableto numbero</html>");

    	String a = JOptionPane.showInputDialog(lbl);
    	boolean noinput = true;
    	if(a != null) {
    		int tabletnumbero = Integer.parseInt(a);
    		double[] coord = map.getTablet(tabletnumbero);
    		guic.sendTarget((int) coord[0],(int) coord[1]); 
    		
    	}
    	if(noinput) {
    		JOptionPane.showMessageDialog(this, "No new target sent.");
    	}
    	
    	
    }//GEN-LAST:event_setTargetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
       
                GuiMain gui = new GuiMain();
        		gui.setVisible(true);
        		gui.enableAllButtons();
        		gui.setMap("/shapesDemo.csv");
        		gui.setOwnLocation(200, 200);
        		gui.setEnemyLocation(300, 300);
        		gui.setTargetLocation(210, 210);
        		
        	
         
                try {  
                     BufferedImage image = ImageIO.read(new File("C:\\Users\\Study\\Desktop\\test.png"));
                     gui.setImage(image);
                } catch (IOException e) {  
                     e.printStackTrace();  
                 
                }  
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JPanel commandlistTab;
    private javax.swing.JLabel creditsLbl;
    private javax.swing.JPanel credittab;
    private javax.swing.JLabel enemyHeightDisplay;
    private javax.swing.JLabel enemyHeightTxt;
    private javax.swing.JButton filterBtn;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel leftPropDisplay;
    private javax.swing.JLabel leftPropTxt;
    private javax.swing.JLabel mapDisplay;
    private javax.swing.JLabel mapDisplayEnemy;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JLayeredPane overviewTab;
    private javax.swing.JLabel ownHeightDisplay;
    private javax.swing.JLabel ownHeightTxt;
    private javax.swing.JLabel photoLabel;
    private javax.swing.JLabel rightPropDisplay;
    private javax.swing.JLabel rightPropTxt;
    private javax.swing.JButton setTarget;
    private javax.swing.JTextArea shownGUIEventList;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel tab1LabelDisplay;
    private javax.swing.JLabel upPropDisplay;
    private javax.swing.JLabel upPropTxt;
    // End of variables declaration//GEN-END:variables
    
    private ImageIcon propact;
	private ImageIcon propnotact;
	private GuiCommands guic = new GuiCommands(this);
	private LinkedList<GUIEvent> fullGUIEventList = new LinkedList<GUIEvent>();
	private HashMap<GUIEvent.EventType,Boolean> typevisibility = new HashMap<>();
	
	//map, mapmaker, and most recent locations.
	private MapMaker mapmaker = MapMaker.getInstance();
	private JLabel mapofplayingfield = new JLabel();
	private Map map;
	private double[] own;
	private double[] enemy;
	private double[] target;
	private double[] enemyTarget;
	
    public void setEigenHoogte(double hoogte) {
    	DecimalFormat df = new DecimalFormat("#.##");
		String s = df.format(hoogte);
		ownHeightDisplay.setText(s+" cm");
		addToGUIEventList(GUIEvent.EventType.HeightReceived," - New height received from own Zeppelin : "+hoogte);
	}
    
    public void setEnemyHoogte(double hoogte){
    	DecimalFormat df = new DecimalFormat("#.##");
		String s = df.format(hoogte);
		enemyHeightDisplay.setText(s+" cm");
		addToGUIEventList(GUIEvent.EventType.HeightReceived," - New height received from enemy Zeppelin : "+hoogte);
    }
    
    /**
     * Geeft de locatie van de eigen zeppelin weer op de kaart.
     * Locatie wordt gegeven door x- en y-coordinaat in cm.
     * Coordinaten volgens de richting van de kaart. Hoek zoals PositionController.
     * 
     * @param x
     * 			x in cm. X loopt van links naar rechts.
     * @param y
     * 			y in cm. Y loopt van boven naar beneden.
     * @param alpha
     * 			Hoek. 0 -> wijst naar boven, > 0 -> wijzerzin, < 0 -> tegenwijzerzin
     */
    public void setOwnLocation(double x, double y, double alpha) {
    	double[] loc = {x,y,alpha};
    	own = loc;
    	mapofplayingfield.setIcon(mapmaker.getLocations(own, enemy, target, enemyTarget));
    	addToGUIEventList(GUIEvent.EventType.ReceivedLocation," - Location own zeppelin received.");
    	updateMapDisplay();
    }
    
    /**
     * Geeft de locatie van de eigen zeppelin weer op de kaart.
     * Locatie wordt gegeven door x- en y-coordinaat in cm.
     * Hoek wordt op 0 gezet.
     * 
     * @param x
     * 			x in cm. X loopt van links naar rechts.
     * @param y
     * 			y in cm. Y loopt van boven naar beneden.
     */
    public void setOwnLocation(double x, double y) {
    	setOwnLocation(x,y,0);
    }
    
    /**
     * Geeft de locatie van de enemy zeppelin weer op de kaart.
     * Locatie wordt gegeven door x- en y-coordinaat in cm.
     * Coordinaten volgens de richting van de kaart.
     * 
     * @param x
     * 			x in cm. X loopt van links naar rechts.
     * @param y
     * 			y in cm. Y loopt van boven naar beneden.
     */
    public void setEnemyLocation(double x, double y) {
    	double[] loc = {x,y};
    	enemy = loc;
    	mapofplayingfield.setIcon(mapmaker.getLocations(own, enemy, target, enemyTarget));
    	addToGUIEventList(GUIEvent.EventType.ReceivedLocation," - Location enemy zeppelin received.");
    	updateMapDisplay();
    }
    
    /**
     * Geeft de locatie van de bestemming weer op de kaart.
     * Locatie wordt gegeven door x- en y-coordinaat in cm.
     * Coordinaten volgens de richting van de kaart.
     * 
     * @param x
     * 			x in cm. X loopt van links naar rechts.
     * @param y
     * 			y in cm. Y loopt van boven naar beneden.
     */
    public void setTargetLocation(double x, double y) {
    	double[] loc = {x,y};
    	target = loc;
    	mapofplayingfield.setIcon(mapmaker.getLocations(own, enemy, target, enemyTarget));
    	addToGUIEventList(GUIEvent.EventType.ReceivedLocation," - Location target received.");
    	updateMapDisplay();
    }
    
    /**
     * Geeft de bestemming van de vijand weer op de kaart.
     * Deze wordt gegeven door x- en y-coordinaat in cm.
     * Coordinaten volgens de richting van de kaart.
     * 
     * @param 	x
     * 			x in cm. X loopt van links naar rechts.
     * @param 	y
     * 			y in cm. Y loopt van boven naar beneden.
     */
    public void setEnemyTarget(double x, double y) {
    	double[] loc = {x,y};
    	enemyTarget = loc;
    	mapofplayingfield.setIcon(mapmaker.getLocations(own, enemy, target, enemyTarget));
    	addToGUIEventList(GUIEvent.EventType.ReceivedLocation," - Location target enemy received.");
    	updateMapDisplay();
    }
    
    /**
     * Sets the map to the map contained in the file the path refers to.
     * The filename should refer to a file in the resources folder, and should be a
	 * comma separated value file.
	 * The filename should start with "/" and contain the extension.
	 * 
	 * Constraints on the content of the file can be found in map>Map class documentation
     */
    public void setMap(String filepath) {
    	map = new Map(filepath);
    	mapofplayingfield.setIcon(mapmaker.getMap(map));
    	addToGUIEventList(GUIEvent.EventType.ReceivedOther," - New map.");
    }
    
    /**
     * Sets the map to the map contained in the file.
     * 
     * Constraints on the content of the file can be found in map>Map class documentation
     */
    public void setMap(File file) {
    	map = new Map(file);
    	mapofplayingfield.setIcon(MapMaker.getInstance().getMap(map));
    	addToGUIEventList(GUIEvent.EventType.ReceivedOther," - New map.");
    }
    
    private void updateMapDisplay() {
    	DecimalFormat df = new DecimalFormat("#.##");
    	String s = "";
    	s = s + "Indigo: ";
    	if(own != null)
    		s = s + "(" + df.format(own[0]) + " ; " + df.format(own[1]) + ")";
    	else
    		s = s + "(_,_)";
    	s = s + "  |  Target: ";
    	if(target != null)
    		s = s + "(" + df.format(target[0]) + "," + df.format(target[1]) + ")";
    	else
    		s = s + "(_,_)";
    	mapDisplay.setText(s);
    	
    	s = "Enemy: " ;
    	if(enemy != null)
    		s = s + "(" + df.format(enemy[0]) + " ; " + df.format(enemy[1]) + ")";
    	else
    		s = s + "(_,_)";
    	s = s + "  |  Target: ";
    	if(enemyTarget != null)
    		s = s + "(" + df.format(enemyTarget[0]) + "," + df.format(enemyTarget[1]) + ")";
    	else
    		s = s + "(_,_)";
    	mapDisplayEnemy.setText(s);
    	
    }
    
	/**
	 * Update de status van een propellor wanneer deze is ingeschakeld.
	 * @param nbPropellor
	 */
	public void propellorActive(Propellor nbPropellor){
		if(nbPropellor==Propellor.X){
			leftPropDisplay.setIcon(propact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, "- Left propellor activated ");
		}else if(nbPropellor==Propellor.UP){
			upPropDisplay.setIcon(propact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, " - Up propellor activated ");
		}else{
			rightPropDisplay.setIcon(propact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, " - Right propellor activated " );
		}
	}

	/**
	 * Update de status van een propellor wanneer deze is uitgeschakeld.
	 * @param nbPropellor
	 */
	public void propellorNotActive(Propellor nbPropellor){
		if(nbPropellor==Propellor.X){
			leftPropDisplay.setIcon(propnotact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, " - Left propellor turned off " );
		}else if(nbPropellor==Propellor.UP){
			upPropDisplay.setIcon(propnotact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, " - Up propellor turned off " );
		}else{
			rightPropDisplay.setIcon(propnotact);
			addToGUIEventList(GUIEvent.EventType.PropStatus, " - Right propellor turned off " );
		}
	}
	
	public String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	/**
	 * Methode die alle buttons in de opgegeven container deactiveert.
	 */
	private void disableAllComponents(Container c){
		for(Component c2:c.getComponents()) {
			if(c2 instanceof JButton || c2 instanceof JToggleButton)
				c2.setEnabled(false);
			if(c2 instanceof Container)
				disableAllComponents((Container) c2);
		}
	}
	
	/**
	 * Geeft een image weer in het label display van tab 1.
	 * @param image
	 */
	public void setImage(BufferedImage image){ImageIcon img = new ImageIcon(image);
		if(img.getIconHeight()>(260/1.33) || img.getIconWidth()>260) {
			Image resizedImg = img.getImage().getScaledInstance(260, (int) (260/1.33), Image.SCALE_DEFAULT);
			img = new ImageIcon(resizedImg);
		}
		tab1LabelDisplay.setIcon(img);
		addToGUIEventList(GUIEvent.EventType.ReceivedOther," - New image received.");
	}

	/**
	 * Methode die alle buttons in de GUI activeert.
	 */
	public void enableAllButtons() {
		enableAllComponents(this);	
	}

	private void enableAllComponents(Container c){
		for(Component c2:c.getComponents()) {
			if(c2 instanceof JButton || c2 instanceof JToggleButton)
				c2.setEnabled(true);
			if(c2 instanceof Container)
				enableAllComponents((Container) c2);
		}
	}
	
	/**
	 * Voegt een GUIEvent toe aan tab 2.
	 * @param type
	 * 		EventType: enum in GUIEvent
	 * @param s
	 * 		tekst
	 */
	public void addToGUIEventList(GUIEvent.EventType type, String s) {
		GUIEvent g = new GUIEvent(type,getTime()+s);
		fullGUIEventList.add(g);
		if(typevisibility.get(type))
			shownGUIEventList.setText(shownGUIEventList.getText() + g.text + "\n");
	}

	/**
	 * Herschrijft alle tekst in de shownGUIEventList, aan de hand van de typevisibility.
	 */
	private void remakeGUIEvents() {
		String s = "";
		for(GUIEvent g : fullGUIEventList)
			if(typevisibility.get(g.type))
				s = s + g.text + "\n";
		shownGUIEventList.setText(s);
	}

	public GuiCommands getGuic() {
		return guic;
	}

	public void setGuic(GuiCommands guic) {
		this.guic = guic;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
