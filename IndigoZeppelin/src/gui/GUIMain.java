/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import zeppelin.Propellor;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import transfer.Transfer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *	Hoofdklasse voor weergave en reacties van de GUI
 */
@SuppressWarnings("serial")
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
		jTabbedPane.setTitleAt(1, "command list");
		jTabbedPane.setTitleAt(2, "credits");

		//functies van de pijltjes met een keylistener
		tab1window3.addKeyListener(keyboardListener);
		tab1window3.requestFocusInWindow(); //voor arrow keys: focus vragen, voor andere keys automatisch goed
		
		upButton.setFocusable(false);
		downButton.setFocusable(false);
		rightButton.setFocusable(false);
		leftButton.setFocusable(false);
		elevateButton.setFocusable(false);
		setHeightBtn.setFocusable(false);

		//program icon
		/*ImageIcon image = new ImageIcon("./resources/ballon.jpg");
		Image img = image.getImage();
		this.setIconImage(img);*/
		
		//deze is beter voor jar
		InputStream resource = GUIMain.class.getResourceAsStream("/ballon.jpg");
		try {
			Image image = ImageIO.read(resource);
			this.setIconImage(image);
		} catch (IOException e) {}
		

		tab1window1Lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		tab1window1Lbl.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

		//pijltjes afb
		upButton.setText("\u2191");
		downButton.setText("\u2193");
		leftButton.setText("\u2190");
		rightButton.setText("\u2192");
		
		//initialise the propellor icons
		resource = GUIMain.class.getResourceAsStream("/propelloractive.jpg");
		try {
			Image image = ImageIO.read(resource);
			propact = new ImageIcon(image);
		} catch (IOException e) {}
		resource = GUIMain.class.getResourceAsStream("/propellornotactive.jpg");
		try {
			Image image = ImageIO.read(resource);
			propnotact = new ImageIcon(image);
		} catch (IOException e) {}
		
		propellorNotActive(Propellor.LEFT);
		propellorNotActive(Propellor.UP);
		propellorNotActive(Propellor.RIGHT);
		
		manual();
		
		disableAllComponents(this);
	}

	/**
	 * Deze ChangeListener zorgt ervoor dat de focus in tab 1 gaat naar tab1window3,
	 * zodat de pijltjestoetsen reageren.
	 *
	 */
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
	 * Keylistener voor de pijltjes om de motoren te besturen.
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

	public void addToCommandList(String s) {
		fullCommandList.setText(fullCommandList.getText() + "\n" +"Tijd:"+getTime()+ s);
	}
	
	//wanneer een toets ingedrukt
	private void upPressed() {
		up = true;
		upButton.setSelected(true);

		addToCommandList("-Up pressed!");
		guic.sendKeyPressed(Transfer.Key.UP);
	}

	private void upUnpressed() {
		up = false;
		upButton.setSelected(false);

		addToCommandList("-Up unpressed!");
		guic.sendKeyReleased(Transfer.Key.UP);
	}

	private void leftPressed() {
		left = true;
		leftButton.setSelected(true);
		addToCommandList("-Left pressed!");
		guic.sendKeyPressed(Transfer.Key.LEFT);
	}

	private void leftUnpressed() {
		left = false;
		leftButton.setSelected(false);
		addToCommandList("-Left unpressed!");
		guic.sendKeyReleased(Transfer.Key.LEFT);
	}

	private void rightPressed() {
		right = true;
		rightButton.setSelected(true);
		addToCommandList("-Right pressed!");
		guic.sendKeyPressed(Transfer.Key.RIGHT);
	}

	private void rightUnpressed() {
		right = false;
		rightButton.setSelected(false);
		addToCommandList("-Right unpressed!");
		guic.sendKeyReleased(Transfer.Key.RIGHT);
	}

	private void downPressed() {
		down = true;
		downButton.setSelected(true);
		addToCommandList("-Down pressed!");
		guic.sendKeyPressed(Transfer.Key.DOWN);
	}

	private void downUnpressed() {
		down = false;
		downButton.setSelected(false);
		addToCommandList("-Down unpressed!");
		guic.sendKeyReleased(Transfer.Key.DOWN);
	}

	private void elevatePressed() {
		elevate = true;
		elevateButton.setSelected(true);
		addToCommandList("-Elevate pressed!");
		guic.sendKeyPressed(Transfer.Key.ELEVATE);
	}

	private void elevateUnpressed() {
		elevate = false;
		elevateButton.setSelected(false);
		addToCommandList("-Elevate unpressed!");
		guic.sendKeyReleased(Transfer.Key.ELEVATE);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JLayeredPane();
        tab1window1 = new javax.swing.JPanel();
        tab1window1Lbl = new javax.swing.JLabel();
        tab1Window1StringLbl = new javax.swing.JLabel();
        manualButton = new javax.swing.JButton();
        automaticButton = new javax.swing.JButton();
        tab1window2 = new javax.swing.JPanel();
        labelHoogteDisplay = new javax.swing.JLabel();
        labelCommandsTxt = new javax.swing.JLabel();
        labelCommandsDisplay = new javax.swing.JLabel();
        labelHoogteTxt = new javax.swing.JLabel();
        propellor1 = new javax.swing.JLabel();
        propellor2 = new javax.swing.JLabel();
        propellor3 = new javax.swing.JLabel();
        prop1Lbl = new javax.swing.JLabel();
        prop2Lbl = new javax.swing.JLabel();
        prop3Lbl = new javax.swing.JLabel();
        tab1window3 = new javax.swing.JPanel();
        upButton = new javax.swing.JToggleButton();
        downButton = new javax.swing.JToggleButton();
        leftButton = new javax.swing.JToggleButton();
        rightButton = new javax.swing.JToggleButton();
        elevateButton = new javax.swing.JToggleButton();
        setHeightBtn = new javax.swing.JButton();
        setPwmValue = new javax.swing.JButton();
        zoekZweefPwm = new javax.swing.JButton();
        tab2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fullCommandList = new javax.swing.JTextArea();
        tab3 = new javax.swing.JPanel();
        creditsLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zeppelin GUI");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jTabbedPane.setName(""); // NOI18N

        manualButton.setText("manual");
        manualButton.setFocusable(false);
        manualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualButtonActionPerformed(evt);
            }
        });

        automaticButton.setText("automatic");
        automaticButton.setFocusable(false);
        automaticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab1window1Layout = new javax.swing.GroupLayout(tab1window1);
        tab1window1.setLayout(tab1window1Layout);
        tab1window1Layout.setHorizontalGroup(
            tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window1Layout.createSequentialGroup()
                .addGroup(tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1window1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tab1Window1StringLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                            .addComponent(tab1window1Lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(tab1window1Layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(manualButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(automaticButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        tab1window1Layout.setVerticalGroup(
            tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab1window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(manualButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(automaticButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tab1window1Lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tab1Window1StringLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
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

        propellor1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        propellor1.setMaximumSize(new java.awt.Dimension(51, 44));
        propellor1.setMinimumSize(new java.awt.Dimension(51, 44));
        propellor1.setPreferredSize(new java.awt.Dimension(51, 44));

        propellor2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        propellor2.setMaximumSize(new java.awt.Dimension(51, 44));
        propellor2.setMinimumSize(new java.awt.Dimension(51, 44));
        propellor2.setPreferredSize(new java.awt.Dimension(51, 44));

        propellor3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        propellor3.setMaximumSize(new java.awt.Dimension(51, 44));
        propellor3.setMinimumSize(new java.awt.Dimension(51, 44));
        propellor3.setPreferredSize(new java.awt.Dimension(51, 44));

        prop1Lbl.setText("Left");

        prop2Lbl.setText("Up");

        prop3Lbl.setText("Right");

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
                            .addComponent(labelHoogteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(tab1window2Layout.createSequentialGroup()
                                    .addComponent(prop1Lbl)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(prop3Lbl))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tab1window2Layout.createSequentialGroup()
                                    .addComponent(propellor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(tab1window2Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(propellor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(tab1window2Layout.createSequentialGroup()
                                            .addGap(23, 23, 23)
                                            .addComponent(prop2Lbl)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(propellor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 50, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGap(58, 58, 58)
                .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prop3Lbl, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(prop1Lbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1window2Layout.createSequentialGroup()
                        .addComponent(prop2Lbl)
                        .addGap(11, 11, 11)
                        .addComponent(propellor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab1window2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(propellor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(propellor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        propellor1.getAccessibleContext().setAccessibleName("propellor1");
        propellor2.getAccessibleContext().setAccessibleName("propellor2");
        propellor3.getAccessibleContext().setAccessibleName("propellor3");

        tab1window2.setBounds(500, 0, 290, 570);
        tab1.add(tab1window2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        leftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftButtonActionPerformed(evt);
            }
        });

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

        setPwmValue.setText("set zweef");
        setPwmValue.setFocusable(false);
        setPwmValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPwmValueActionPerformed(evt);
            }
        });

        zoekZweefPwm.setText("zoek zweef");
        zoekZweefPwm.setFocusable(false);
        zoekZweefPwm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoekZweefPwmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab1window3Layout = new javax.swing.GroupLayout(tab1window3);
        tab1window3.setLayout(tab1window3Layout);
        tab1window3Layout.setHorizontalGroup(
            tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1window3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(zoekZweefPwm, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPwmValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
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
                .addGroup(tab1window3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tab1window3Layout.createSequentialGroup()
                        .addComponent(zoekZweefPwm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPwmValue, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(elevateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tab1window3.setBounds(0, 400, 500, 170);
        tab1.add(tab1window3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTabbedPane.addTab("tab1", tab1);

        fullCommandList.setColumns(20);
        fullCommandList.setRows(5);
        jScrollPane2.setViewportView(fullCommandList);

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab2", tab2);

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
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

	/**
	 * Actie wanneer de up-button wordt aangeklikt
	 */
	private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
		if(upButton.isSelected())
			upPressed();
		else
			upUnpressed();
	}//GEN-LAST:event_upButtonActionPerformed

	/**
	 * Actie wanneer de left-button wordt aangeklikt
	 */
	private void leftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftButtonActionPerformed
		if(leftButton.isSelected())
			leftPressed();
		else
			leftUnpressed();
	}//GEN-LAST:event_leftButtonActionPerformed

	/**
	 * Actie wanneer de down-button wordt aangeklikt
	 */
	private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
		if(downButton.isSelected())
			downPressed();
		else
			downUnpressed();
	}//GEN-LAST:event_downButtonActionPerformed

	/**
	 * Actie wanneer de right-button wordt aangeklikt
	 */
	private void rightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightButtonActionPerformed
		if(rightButton.isSelected())
			rightPressed();
		else
			rightUnpressed();
	}//GEN-LAST:event_rightButtonActionPerformed

	/**
	 * Actie wanneer de elevate-button wordt aangeklikt
	 */
	private void elevateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elevateButtonActionPerformed
		if(elevateButton.isSelected())
			elevatePressed();
		else
			elevateUnpressed();
	}//GEN-LAST:event_elevateButtonActionPerformed

	/**
	 * Actie wanneer de set height button wordt aangeklikt
	 */
	private void setHeightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setHeightBtnActionPerformed
		String height = JOptionPane.showInputDialog(this, "Go to height (cm)");
		double heightgetal = 0;
		try {
			if(height != null){
				heightgetal = Double.parseDouble(height);
				guic.sendHeightZep(heightgetal);
			}
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ongeldige hoogte");
		}

	}//GEN-LAST:event_setHeightBtnActionPerformed

    private void setPwmValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPwmValueActionPerformed
    	String pwm = JOptionPane.showInputDialog(this, "Set this pwm as float-pwm value");
		int pwmgetal = 0;
		try {
			if(pwm != null){
				pwmgetal = Integer.parseInt(pwm);
				if(pwmgetal >= -1024 && pwmgetal <= 1024)
					guic.sendPwmZep(pwmgetal);
				else
					JOptionPane.showMessageDialog(this, "Ongeldige pwm-waarde");
			}
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ongeldige pwm-waarde");
		}
    }//GEN-LAST:event_setPwmValueActionPerformed

    private void zoekZweefPwmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoekZweefPwmActionPerformed
        guic.searchPwmZep();
    }//GEN-LAST:event_zoekZweefPwmActionPerformed

    private void manualButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualButtonActionPerformed
    	 manual();
    }//GEN-LAST:event_manualButtonActionPerformed

    private void automaticButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticButtonActionPerformed
        automatic();
    }//GEN-LAST:event_automaticButtonActionPerformed
    private boolean manual;
    private void manual() {
    	if(!manual){
    	downButton.setEnabled(true);
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        upButton.setEnabled(true);
        elevateButton.setEnabled(true);
        setHeightBtn.setEnabled(true);
        
        automaticButton.setBackground(Color.RED);
        manualButton.setBackground(Color.GREEN);
        
        tab1window3.addKeyListener(keyboardListener);
        
        guic.setManualContr();
        manual=true;
    	}
    }
    
    private void automatic() {
    	if(manual){
    	downButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        upButton.setEnabled(false);
        elevateButton.setEnabled(false);
        setHeightBtn.setEnabled(false);
        
        automaticButton.setBackground(Color.GREEN);
        manualButton.setBackground(Color.RED);
        
        tab1window3.removeKeyListener(keyboardListener);
        
        guic.setAutomaticContr();
        manual=false;
    	}
    }
    
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton automaticButton;
    private javax.swing.JLabel creditsLbl;
    private javax.swing.JToggleButton downButton;
    private javax.swing.JToggleButton elevateButton;
    private javax.swing.JTextArea fullCommandList;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JLabel labelCommandsDisplay;
    private javax.swing.JLabel labelCommandsTxt;
    private javax.swing.JLabel labelHoogteDisplay;
    private javax.swing.JLabel labelHoogteTxt;
    private javax.swing.JToggleButton leftButton;
    private javax.swing.JButton manualButton;
    private javax.swing.JLabel prop1Lbl;
    private javax.swing.JLabel prop2Lbl;
    private javax.swing.JLabel prop3Lbl;
    private javax.swing.JLabel propellor1;
    private javax.swing.JLabel propellor2;
    private javax.swing.JLabel propellor3;
    private javax.swing.JToggleButton rightButton;
    private javax.swing.JButton setHeightBtn;
    private javax.swing.JButton setPwmValue;
    private javax.swing.JLayeredPane tab1;
    private javax.swing.JLabel tab1Window1StringLbl;
    private javax.swing.JPanel tab1window1;
    private javax.swing.JLabel tab1window1Lbl;
    private javax.swing.JPanel tab1window2;
    private javax.swing.JPanel tab1window3;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JToggleButton upButton;
    private javax.swing.JButton zoekZweefPwm;
    // End of variables declaration//GEN-END:variables
    
    private ImageIcon propact;
	private ImageIcon propnotact;
	private GuiCommands guic = new GuiCommands(this);
	
	private KeyListener keyboardListener = new MotorListener();
	
	public void setHoogteLabel(double hoogte){
		DecimalFormat df = new DecimalFormat("#.##");
        String s = df.format(hoogte);
		labelHoogteDisplay.setText(s+" cm");
	}

	/**
	 * Geeft de message weer op het commando-display in tab 1 van de GUI.
	 * @param message
	 */
	public void showMessage(String message){
		labelCommandsDisplay.setText(message);
	}
	
	/**
	 * Geeft de message weer in een pop-up venster
	 * @param message
	 * @param type
	 * 		Is: JOptionPane. ... (error, question, information, warning, plain)
	 */
	public void displayMessage(String message, int type) {
		JOptionPane.showMessageDialog(this,message,"Information",type);
	}

	public void setImageDisplay(ImageIcon image){
		tab1window1Lbl.setIcon(image);
		addToCommandList( " - New image received from Zeppelin ");
	}

	public void propellorActive(Propellor nbPropellor){
		if(nbPropellor==Propellor.LEFT){
			propellor1.setIcon(propact);
			addToCommandList( "- Left propellor activated ");
		}else if(nbPropellor==Propellor.UP){
			propellor2.setIcon(propact);
			addToCommandList( " - Up propellor activated ");
		}else{
			propellor3.setIcon(propact);
			addToCommandList( " - Right propellor activated " );
		}
	}

	public void propellorNotActive(Propellor nbPropellor){
		if(nbPropellor==Propellor.LEFT){
			propellor1.setIcon(propnotact);
			addToCommandList( " - Left propellor turned off " );
		}else if(nbPropellor==Propellor.UP){
			propellor2.setIcon(propnotact);
			addToCommandList( " - Up propellor turned off " );
		}else{
			propellor3.setIcon(propnotact);
			addToCommandList( " - Right propellor turned off " );
		}
	}

	public GuiCommands getGuiCommands(){
		return guic;
	}

	public String setIP(){
		return JOptionPane.showInputDialog(this, "Please enter server IP");
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
	
	public static void main(String[] args) {
		GUIMain gui = new GUIMain();
		gui.setVisible(true);
		gui.enableAllButtons();
		
	}

	public String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}
