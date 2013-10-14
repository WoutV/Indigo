/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author user
 */
public class GUIMain extends javax.swing.JFrame {
	
	//enkel wanneer voor het eerste wordt ingedrukt een signaal geven (indien ingedrukt blijft: niks)
	//alternatief: constant sturen (booleans niet nodig)
	private boolean up,left,right,down,elevate;

    /**
     * Creates new form GUIMain
     */
    public GUIMain() {
        initComponents();
        init2();
    }
    
    private void init2() {
    	
    	//zorgt er voor dat de focus goed blijft wanneer van tab wordt gewisseld
    	//voorlopig: focus op tab1window2 in tab1, voor de pijltjes
    	jTabbedPane.addChangeListener(new TabChangeListener());
        
        jTabbedPane.setTitleAt(0, "main");
        jTabbedPane.setTitleAt(1, "2");
        jTabbedPane.setTitleAt(2, "credits");
        
        //functies van de pijltjes met een keylistener
        tab1window3.addKeyListener(new MotorListener());
    	tab1window3.requestFocusInWindow(); //voor arrow keys: focus vragen, voor andere keys automatisch goed
    	
    	//alternatief: keybindings (ook focus nodig)
    	//tab1window2.getActionMap().put("up", new upAction());
        //tab1window2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "up");
        /*jPanel5.getInputMap().put(KeyStroke.getKeyStroke("F2"),
                            "button");
        jPanel5.getActionMap().put("button",
                             new upAction());
    */
       // tab1window1.registerKeyboardAction(new upAction(),KeyStroke.getKeyStroke("UP"),JComponent.WHEN_IN_FOCUSED_WINDOW);
       upButton.setFocusable(false);
       downButton.setFocusable(false);
       rightButton.setFocusable(false);
       leftButton.setFocusable(false);
       elevateButton.setFocusable(false);
       setHeightBtn.setFocusable(false);
       
       
       ImageIcon image = new ImageIcon("./resources/ballon.jpg");
       Image img = image.getImage();
       this.setIconImage(img);
       
       tab1window1Lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
       tab1window1Lbl.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
       
    }
    
    class TabChangeListener implements ChangeListener {
    	@Override
		public void stateChanged(ChangeEvent arg0) {
			Component comp = jTabbedPane.getSelectedComponent();
            if (comp.equals(tab1))
            {
              	tab1window3.requestFocusInWindow();
            }
		}
    }
    
    /**
     * Keylistener voor de besturingen om de motoren te besturen.
     *	UP: vooruit
     *	LEFT: links
     *	RIGHT: rechts
     *	DOWN: achteruit
     *	SPACE: omhoog
     */
    class MotorListener implements KeyListener {
    	
    	@Override
    	public void keyPressed(KeyEvent arg0) {
    		if(arg0.getKeyCode() == KeyEvent.VK_UP) {
    			if(!up) {
    				upPressed();
    			}
    		}
    		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT) {
    			if(!left) {
    				leftPressed();
    			}
    		}
    		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
    			if(!right) {
    				rightPressed();
    			}
    		}
    		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
    			if(!down) {
    				downPressed();
    			}
    		}
    		else if(arg0.getKeyCode() == KeyEvent.VK_SPACE) {
    			if(!elevate) {
    				elevatePressed();
    			}
    		}
    	}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_UP) {
    			upUnpressed();
    		}
			else if(arg0.getKeyCode() == KeyEvent.VK_LEFT) {
    			leftUnpressed();
    		}
			else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
    			rightUnpressed();
    		}
			else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
    			downUnpressed();
    		}
			else if(arg0.getKeyCode() == KeyEvent.VK_SPACE) {
				elevateUnpressed();
    		}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
    }
    
    //wanneer een toets ingedrukt
    private void upPressed() {
    	up = true;
    	upButton.setSelected(true);
		System.out.println("up pressed");
    }
    
    private void upUnpressed() {
    	up = false;
    	upButton.setSelected(false);
		System.out.println("up unpressed");
    }
    
    private void leftPressed() {
    	left = true;
    	leftButton.setSelected(true);
		System.out.println("left pressed");
    }
    
    private void leftUnpressed() {
    	left = false;
    	leftButton.setSelected(false);
		System.out.println("left unpressed");
    }
    
    private void rightPressed() {
    	right = true;
    	rightButton.setSelected(true);
		System.out.println("right pressed");
    }
    
    private void rightUnpressed() {
    	right = false;
    	rightButton.setSelected(false);
		System.out.println("right unpressed");
    }
    
    private void downPressed() {
    	down = true;
    	downButton.setSelected(true);
		System.out.println("down pressed");
    }
    
    private void downUnpressed() {
    	down = false;
    	downButton.setSelected(false);
		System.out.println("down unpressed");
    }
    
    private void elevatePressed() {
    	elevate = true;
    	elevateButton.setSelected(true);
		System.out.println("elevate pressed");
    }
    
    private void elevateUnpressed() {
    	elevate = false;
    	elevateButton.setSelected(false);
		System.out.println("elevate unpressed");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JLayeredPane();
        tab1window1 = new javax.swing.JPanel();
        tab1window1Lbl = new javax.swing.JLabel();
        tab1window2 = new javax.swing.JPanel();
        labelHoogteDisplay = new javax.swing.JLabel();
        labelCommandsTxt = new javax.swing.JLabel();
        labelCommandsDisplay = new javax.swing.JLabel();
        labelHoogteTxt = new javax.swing.JLabel();
        setImage = new javax.swing.JButton();
        setHoogte = new javax.swing.JButton();
        tab1window3 = new javax.swing.JPanel();
        upButton = new javax.swing.JToggleButton();
        downButton = new javax.swing.JToggleButton();
        leftButton = new javax.swing.JToggleButton();
        rightButton = new javax.swing.JToggleButton();
        elevateButton = new javax.swing.JToggleButton();
        setHeightBtn = new javax.swing.JButton();
        tab2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zeppelin GUI");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jTabbedPane.setName(""); // NOI18N

        javax.swing.GroupLayout tab1window1Layout = new javax.swing.GroupLayout(tab1window1);
        tab1window1.setLayout(tab1window1Layout);
        tab1window1Layout.setHorizontalGroup(
            tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1window1Lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        tab1window1Layout.setVerticalGroup(
            tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1window1Lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tab1window1.setBounds(0, 0, 500, 400);
        tab1.add(tab1window1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelHoogteDisplay.setBackground(new java.awt.Color(0, 0, 0));
        labelHoogteDisplay.setForeground(new java.awt.Color(0, 255, 0));
        labelHoogteDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelHoogteDisplay.setText("Hier komt hoogte");
        labelHoogteDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        labelHoogteDisplay.setOpaque(true);

        labelCommandsTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCommandsTxt.setText("Commands");
        labelCommandsTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        labelCommandsDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCommandsDisplay.setText("Lijst commandos");
        labelCommandsDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        labelHoogteTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelHoogteTxt.setText("Hoogte :");
        labelHoogteTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        setImage.setText("set Image temp");
        setImage.setFocusable(false);
        setImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setImageActionPerformed(evt);
            }
        });

        setHoogte.setText("set Hoogte temp");
        setHoogte.setFocusable(false);
        setHoogte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setHoogteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab1window2Layout = new javax.swing.GroupLayout(tab1window2);
        tab1window2.setLayout(tab1window2Layout);
        tab1window2Layout.setHorizontalGroup(
            tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCommandsDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tab1window2Layout.createSequentialGroup()
                        .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelHoogteTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCommandsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelHoogteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 50, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(tab1window2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(setHoogte, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(setImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab1window2Layout.setVerticalGroup(
            tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelHoogteTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelHoogteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelCommandsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelCommandsDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(setImage, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setHoogte, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        tab1window2.setBounds(500, 0, 290, 570);
        tab1.add(tab1window2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        upButton.setText("Z");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText("S");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        leftButton.setText("Q");
        leftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftButtonActionPerformed(evt);
            }
        });

        rightButton.setText("D");
        rightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightButtonActionPerformed(evt);
            }
        });

        elevateButton.setText("Elevate");
        elevateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elevateButtonActionPerformed(evt);
            }
        });

        setHeightBtn.setText("Elevate to..");
        setHeightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setHeightBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab1window3Layout = new javax.swing.GroupLayout(tab1window3);
        tab1window3.setLayout(tab1window3Layout);
        tab1window3Layout.setHorizontalGroup(
            tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window3Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(leftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1window3Layout.createSequentialGroup()
                        .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(setHeightBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(elevateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        tab1window3Layout.setVerticalGroup(
            tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tab1window3Layout.createSequentialGroup()
                        .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(leftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tab1window3Layout.createSequentialGroup()
                        .addComponent(setHeightBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elevateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tab1window3.setBounds(0, 400, 500, 170);
        tab1.add(tab1window3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTabbedPane.addTab("tab1", tab1);

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab2", tab2);

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 795, Short.MAX_VALUE)
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        jTabbedPane.addTab("tab3", tab3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        if(upButton.isSelected())
        	upPressed();
        else
        	upUnpressed();
    }//GEN-LAST:event_upButtonActionPerformed

    private void leftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftButtonActionPerformed
    	if(leftButton.isSelected())
        	leftPressed();
        else
        	leftUnpressed();
    }//GEN-LAST:event_leftButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
    	if(downButton.isSelected())
        	downPressed();
        else
        	downUnpressed();
    }//GEN-LAST:event_downButtonActionPerformed

    private void rightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightButtonActionPerformed
    	if(rightButton.isSelected())
        	rightPressed();
        else
        	rightUnpressed();
    }//GEN-LAST:event_rightButtonActionPerformed

    private void elevateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elevateButtonActionPerformed
    	if(elevateButton.isSelected())
        	elevatePressed();
        else
        	elevateUnpressed();
    }//GEN-LAST:event_elevateButtonActionPerformed

    double hooogtee=0;
    private void setHeightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setHeightBtnActionPerformed
        String height = JOptionPane.showInputDialog(this, "Go to height (cm)");
        double heightgetal = 0;
        try {
        	if(height != null){
        		heightgetal = Double.parseDouble(height);
        		hooogtee=heightgetal;
        	}
        	else
        		return;
        }
        catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "Ongeldige hoogte");
        	return;
        }
        
    }//GEN-LAST:event_setHeightBtnActionPerformed

    private void setImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setImageActionPerformed
    	ImageIcon image = new ImageIcon("./resources/Smalle.jpg");
    	tab1window1Lbl.setIcon(image);
    }//GEN-LAST:event_setImageActionPerformed

    private void setHoogteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setHoogteActionPerformed
    	setHoogteLabel(hooogtee);
    }//GEN-LAST:event_setHoogteActionPerformed


       // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton downButton;
    private javax.swing.JToggleButton elevateButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JLabel labelCommandsDisplay;
    private javax.swing.JLabel labelCommandsTxt;
    private javax.swing.JLabel labelHoogteDisplay;
    private javax.swing.JLabel labelHoogteTxt;
    private javax.swing.JToggleButton leftButton;
    private javax.swing.JToggleButton rightButton;
    private javax.swing.JButton setHeightBtn;
    private javax.swing.JButton setHoogte;
    private javax.swing.JButton setImage;
    private javax.swing.JLayeredPane tab1;
    private javax.swing.JPanel tab1window1;
    private javax.swing.JLabel tab1window1Lbl;
    private javax.swing.JPanel tab1window2;
    private javax.swing.JPanel tab1window3;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JToggleButton upButton;
    // End of variables declaration//GEN-END:variables
    
    public void setHoogteLabel(double hoogte){
    	labelHoogteDisplay.setText(hoogte+" cm");
    }
    
    public void showMessage(String message){
    	labelCommandsDisplay.setText(message);
    }
    
    
}
