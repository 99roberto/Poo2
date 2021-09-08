package com.poo.visao.componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JframePrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private int xx;
	private int yy;

	private javax.swing.JDesktopPane desktop;

	private JLabel labelNome;

	/**
	 * Create the frame.
	 */
	public JframePrincipal() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(JframePrincipal.class.getResource("/imagens/hospital-24.png")));
		getContentPane().setPreferredSize(new Dimension(1000, 595));

		desktop = new javax.swing.JDesktopPane();
		desktop.setBounds(153, 23, 847, 572);
		desktop.setAlignmentY(0.0f);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setSize(new java.awt.Dimension(0, 0));
		setBackground(new java.awt.Color(237, 242, 249));

		desktop.setBackground(new java.awt.Color(237, 242, 249));
		desktop.setForeground(new java.awt.Color(237, 242, 249));
		desktop.setPreferredSize(new java.awt.Dimension(824, 600));

		getContentPane().setLayout(null);

		adicionaBtnFechar();

		getContentPane().add(desktop);

		criarPanelTitle();

		pack();
		setLocationRelativeTo(null);
	}

	private void criarPanelTitle() {
		JPanel panelNome = new JPanel();
		panelNome.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				panelMousePressed(e);
			}
		});
		panelNome.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				panelMouseDragged(e);
			}
		});
		FlowLayout flowLayout = (FlowLayout) panelNome.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelNome.setBounds(0, 0, 1000, 23);
		getContentPane().add(panelNome);
		panelNome.setBackground(new java.awt.Color(3, 133, 186));

		labelNome = new JLabel();
		labelNome.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelNome.setToolTipText("");
		labelNome.setForeground(Color.WHITE);
		labelNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNome.setBackground(Color.WHITE);
		panelNome.add(labelNome);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		this.labelNome.setText(title);
	}

	private void adicionaBtnFechar() {
		JButton btnFechar = new BtnCloseTela("X");
		btnFechar.setBounds(950, 0, 50, 23);
		getContentPane().add(btnFechar);
	}

	protected void setPanelDesktop(JInternalFrame itFrame) {
		desktop.removeAll();
		desktop.add(itFrame);
		try {
			itFrame.setMaximum(true);
		} catch (Exception ex) {
			Logger.getLogger(JframePrincipal.class.getName()).log(Level.SEVERE, null, ex);
		}

		itFrame.setVisible(true);
		itFrame.setSize(desktop.getSize());
	}

	private void panelMouseDragged(MouseEvent e) {
		int x = e.getXOnScreen();
		int y = e.getYOnScreen();
		setLocation(x - xx, y - yy);
	}

	private void panelMousePressed(MouseEvent e) {
		xx = e.getX();
		yy = e.getY();
	}

	protected javax.swing.JDesktopPane getDesktop() {
		return desktop;
	}

	protected void setDesktop(javax.swing.JDesktopPane desktop) {
		this.desktop = desktop;
	}

}
