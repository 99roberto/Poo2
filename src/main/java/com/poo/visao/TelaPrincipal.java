package com.poo.visao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.poo.visao.componentes.BtnCloseTela;
import com.poo.visao.componentes.IInternalFrame;
import com.poo.visao.componentes.JframePrincipal;
import com.poo.visao.componentes.MenuBarItem;

public class TelaPrincipal extends JframePrincipal {

	private static final long serialVersionUID = 1L;

	private List<Component> lstMenuButos = new ArrayList<Component>();

	private JMenuBar painelMenu;
	private SequentialGroup seqGrup;
	private ParallelGroup parlGrup;

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {

		alterarLayoutDesktp();
		criarMenu();
		criarBtnSair();

		setTitle("Sistema de Atendimento Hospitalar");
		pack();
		setLocationRelativeTo(null);
	}

	private void alterarLayoutDesktp() {

		javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(getDesktop());
		desktopLayout.setHorizontalGroup(
				desktopLayout.createParallelGroup(Alignment.LEADING).addGap(0, 847, Short.MAX_VALUE));
		desktopLayout
				.setVerticalGroup(desktopLayout.createParallelGroup(Alignment.LEADING).addGap(0, 572, Short.MAX_VALUE));
		desktopLayout.setAutoCreateGaps(false);
		desktopLayout.setAutoCreateContainerGaps(false);
		getDesktop().setLayout(desktopLayout);

	}

	protected LinkedHashMap<String, IInternalFrame> getInernalTelas() {

		LinkedHashMap<String, IInternalFrame> map = new LinkedHashMap<String, IInternalFrame>();

		map.put("Gerenciar Paciente", new GerenciarPaciente());
		map.put("Gerar Atendimento", new GeraAtendimento());
		map.put("Gerar Consulta", new GeraConsulta());
		map.put("Interna\u00E7\u00E3o", new InternacoesView());
		map.put("Finalizar Atendimento", new FinalizarAtendimentoView());
		map.put("Aguardando Leito", new FilaLeitosView());
		map.put("Internados", new PacientesInternadosView());
		map.put("Leitos Vagos", new LeitosVagosView());

		return map;
	}

	private void criarPanelMenu() {
		painelMenu = new JMenuBar();
		painelMenu.setBounds(0, 23, 154, 572);
		painelMenu.setBackground(new java.awt.Color(4, 158, 218));
		painelMenu.setForeground(new java.awt.Color(105, 196, 218));
		getContentPane().add(painelMenu);
	}

	private void criarMenu() {
		criarPanelMenu();

		JLabel logoLabel = new JLabel("");
		logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setIcon(new ImageIcon(TelaPrincipal.class.getResource("/imagens/hospital.png")));

		javax.swing.GroupLayout painelMenuLayout = new javax.swing.GroupLayout(painelMenu);
		parlGrup = painelMenuLayout.createParallelGroup(Alignment.TRAILING);
		parlGrup.addComponent(logoLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 154,
				GroupLayout.PREFERRED_SIZE);
		seqGrup = painelMenuLayout.createSequentialGroup()
				.addComponent(logoLabel, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED);

		painelMenuLayout.setHorizontalGroup(painelMenuLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(painelMenuLayout.createSequentialGroup().addGroup(parlGrup).addContainerGap()));
		painelMenuLayout.setVerticalGroup(seqGrup);

		painelMenu.setLayout(painelMenuLayout);

		LinkedHashMap<String, IInternalFrame> mapInternalTelas = getInernalTelas();
		for (Entry<String, IInternalFrame> set : mapInternalTelas.entrySet()) {
			adicionarMenu(set.getKey(), set.getValue());
		}

	}

	private void criarBtnSair() {
		JButton btnSair = new BtnCloseTela("Sair");
		btnSair.setAlignmentY(Component.TOP_ALIGNMENT);
		parlGrup.addComponent(btnSair, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE);
		seqGrup.addPreferredGap(ComponentPlacement.RELATED, 74, Short.MAX_VALUE).addComponent(btnSair,
				GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE);
	}

	protected MenuBarItem adicionarMenu(String label, IInternalFrame classItFrame) {

		MenuBarItem btn = new MenuBarItem(label);
		lstMenuButos.add(btn);

		btn.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				for (Component jButton : lstMenuButos) {
					jButton.setForeground(Color.WHITE);
					jButton.setBackground(new java.awt.Color(4, 158, 218));

				}
				btn.setBackground(new java.awt.Color(237, 242, 249));
				btn.setForeground(Color.BLACK);
				repaint();
			}
		});

		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				classItFrame.dadosDefault();
				setPanelPrincipal(classItFrame);
			}
		});

		seqGrup.addGap(1).addComponent(btn, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE);
		parlGrup.addComponent(btn, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE);

		return btn;
	}

	private void setPanelPrincipal(IInternalFrame itFrame) {
		itFrame.limpar();
		((BasicInternalFrameUI) itFrame.getUI()).setNorthPane(null);
		setPanelDesktop(itFrame);
	}

}
