package com.poo.visao.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class BtnCloseTela extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BtnCloseTela(String lbl) {

		super(lbl);
		setFocusPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorderPainted(false);
		setForeground(Color.WHITE);
		setBorder(null);
		setBackground(new java.awt.Color(3, 133, 186));

		addEvents();

	}

	private void addEvents() {

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				btnColorEntered(BtnCloseTela.this);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(new java.awt.Color(3, 133, 186));
			}
		});
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

	}

	private void btnColorEntered(JButton button) {
		if (!button.getBackground().equals(new java.awt.Color(237, 242, 249))) {
			button.setBackground(new java.awt.Color(105, 196, 218));
			button.repaint();
		}
	}
}
