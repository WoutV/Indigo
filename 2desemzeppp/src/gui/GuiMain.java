/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
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
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

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
		typevisibility.put(GUIEvent.EventType.KeyEvent, true);
		typevisibility.put(GUIEvent.EventType.Misc, true);
		typevisibility.put(GUIEvent.EventType.SentOther, true);
		typevisibility.put(GUIEvent.EventType.ReceivedOther, true);
		typevisibility.put(GUIEvent.EventType.PropStatus, true);

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

		propellorNotActive(Propellor.LEFT);
		propellorNotActive(Propellor.UP);
		propellorNotActive(Propellor.RIGHT);

		//initialise tab 3
		photoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		resource = GuiMain.class.getResourceAsStream("/zepp.jpg");
		try {
			Image image = ImageIO.read(resource);
			photoLabel.setIcon(new ImageIcon(image));
		} catch (IOException e) {}

		creditsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		creditsLbl.setText("<html><p align=\"center\">IndigoZeppelin v3.00: <br> <br> " +
				"&copy Team Indigo. All rights reserved. </p></html>");

		//disableAllComponents(this);
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
        mapPanel = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        eigenHoogteTxt = new javax.swing.JLabel();
        eigenHoogteDisplay = new javax.swing.JLabel();
        enemyHoogteTxt = new javax.swing.JLabel();
        enemyHoogteDisplay = new javax.swing.JLabel();
        rightPropDisplay = new javax.swing.JLabel();
        upPropDisplay = new javax.swing.JLabel();
        leftPropDisplay = new javax.swing.JLabel();
        leftPropTxt = new javax.swing.JLabel();
        rightPropTxt = new javax.swing.JLabel();
        upPropTxt = new javax.swing.JLabel();
        commandPanel = new javax.swing.JPanel();
        eventOverviewPanel = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        commandlistTab = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        shownGUIEventList = new javax.swing.JTextArea();
        filterBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        credittab = new javax.swing.JPanel();
        creditsLbl = new javax.swing.JLabel();
        photoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jTabbedPane1.setToolTipText("Zeppelin Indigo");
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 600));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 600));

        mapPanel.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        mapPanel.setBounds(10, 0, 400, 400);
        overviewTab.add(mapPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        eigenHoogteTxt.setText("Eigen hoogte : ");

        eigenHoogteDisplay.setBackground(new java.awt.Color(0, 0, 0));
        eigenHoogteDisplay.setForeground(new java.awt.Color(0, 255, 0));
        eigenHoogteDisplay.setText("Hier komt de hoogte!");
        eigenHoogteDisplay.setOpaque(true);

        enemyHoogteTxt.setText("Enemy hoogte : ");

        enemyHoogteDisplay.setBackground(new java.awt.Color(0, 0, 0));
        enemyHoogteDisplay.setForeground(new java.awt.Color(255, 0, 51));
        enemyHoogteDisplay.setText("Hier komt enemy ho!");
        enemyHoogteDisplay.setOpaque(true);

        rightPropDisplay.setText("jLabel1");
        rightPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        rightPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        upPropDisplay.setText("jLabel1");
        upPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        upPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        leftPropDisplay.setText("jLabel1");
        leftPropDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        leftPropDisplay.setPreferredSize(new java.awt.Dimension(51, 44));

        leftPropTxt.setText("Left");

        rightPropTxt.setText("Right");

        upPropTxt.setText("Up");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eigenHoogteTxt)
                    .addComponent(enemyHoogteTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(upPropTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eigenHoogteDisplay)
                            .addComponent(enemyHoogteDisplay))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(leftPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(leftPropTxt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rightPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rightPropTxt)))))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eigenHoogteTxt)
                            .addComponent(eigenHoogteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rightPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(leftPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemyHoogteTxt)
                    .addComponent(enemyHoogteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leftPropTxt)
                    .addComponent(rightPropTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upPropDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upPropTxt))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        statusPanel.setBounds(10, 400, 400, 150);
        overviewTab.add(statusPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        eventOverviewPanel.setViewportView(jTextArea1);

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(eventOverviewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(eventOverviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );

        commandPanel.setBounds(410, 0, 370, 550);
        overviewTab.add(commandPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
                .addContainerGap(37, Short.MAX_VALUE))
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
                .addContainerGap(36, Short.MAX_VALUE))
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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBtnActionPerformed
        //checkbox voor elke GUIEvent.EventType
        JCheckBox heightreceived = new JCheckBox("Height Received");
        JCheckBox propstatus = new JCheckBox("PropStatus");
        JCheckBox keyevent = new JCheckBox("KeyEvent");
        JCheckBox receivedother = new JCheckBox("ReceivedOther");
        JCheckBox sentother = new JCheckBox("SentOther");
        JCheckBox misc = new JCheckBox("Misc");
        Object[] p = {"Choose which GUIEvents are shown:",heightreceived,propstatus,keyevent,receivedother,
            sentother,misc};
        int a = JOptionPane.showConfirmDialog(this, p,"Filter GUI events",JOptionPane.OK_CANCEL_OPTION);
        if(a == JOptionPane.OK_OPTION) {
            //update typevisibility
            typevisibility.put(GUIEvent.EventType.HeightReceived,heightreceived.isSelected());
            typevisibility.put(GUIEvent.EventType.PropStatus,propstatus.isSelected());
            typevisibility.put(GUIEvent.EventType.KeyEvent,keyevent.isSelected());
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GuiMain gui = new GuiMain();
        		gui.setVisible(true);
        		gui.enableAllButtons();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JPanel commandlistTab;
    private javax.swing.JLabel creditsLbl;
    private javax.swing.JPanel credittab;
    private javax.swing.JLabel eigenHoogteDisplay;
    private javax.swing.JLabel eigenHoogteTxt;
    private javax.swing.JLabel enemyHoogteDisplay;
    private javax.swing.JLabel enemyHoogteTxt;
    private javax.swing.JScrollPane eventOverviewPanel;
    private javax.swing.JButton filterBtn;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel leftPropDisplay;
    private javax.swing.JLabel leftPropTxt;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JLayeredPane overviewTab;
    private javax.swing.JLabel photoLabel;
    private javax.swing.JLabel rightPropDisplay;
    private javax.swing.JLabel rightPropTxt;
    private javax.swing.JTextArea shownGUIEventList;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel upPropDisplay;
    private javax.swing.JLabel upPropTxt;
    // End of variables declaration//GEN-END:variables
    
    private ImageIcon propact;
	private ImageIcon propnotact;
	private GuiCommands guic = new GuiCommands(this);
	private LinkedList<GUIEvent> fullGUIEventList = new LinkedList<GUIEvent>();
	private HashMap<GUIEvent.EventType,Boolean> typevisibility = new HashMap<>();
	
    public void setEigenHoogte(double hoogte) {
    	DecimalFormat df = new DecimalFormat("#.##");
		String s = df.format(hoogte);
		eigenHoogteDisplay.setText(s+" cm");
		
	}
    
    public void setEnemyHoogte(double hoogte){
    	DecimalFormat df = new DecimalFormat("#.##");
		String s = df.format(hoogte);
		enemyHoogteDisplay.setText(s+" cm");
    }
    

	
	/**
	 * Update de status van een propellor wanneer deze is ingeschakeld.
	 * @param nbPropellor
	 */
	public void propellorActive(Propellor nbPropellor){
		if(nbPropellor==Propellor.LEFT){
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
		if(nbPropellor==Propellor.LEFT){
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
	 * Geeft een image weer in tab 1 window1.
	 * @param image
	 */
	public void setImageDisplay(ImageIcon image){
//		if(image.getIconHeight()>275 || image.getIconWidth()>360) {
//			Image resizedImg = image.getImage().getScaledInstance(360, 275, Image.SCALE_DEFAULT);
//			image = new ImageIcon(resizedImg);
//		}
//		tab1window1Lbl.setIcon(image);
//		addToGUIEventList(GUIEvent.EventType.ReceivedOther," - New image received from Zeppelin ");
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
