package com.poo.visao;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;

import com.poo.controle.AtendimentoControle;
import com.poo.controle.ControleException;
import com.poo.modelo.Atendimento;
import com.poo.modelo.EnumAlaHospital;
import com.poo.modelo.Paciente;
import com.poo.visao.componentes.IInternalFrame;
import com.poo.visao.componentes.MDateTimeField;
import com.poo.visao.componentes.MJButton;
import com.poo.visao.componentes.MPanelCenter;
import com.poo.visao.componentes.MTextField;

public class GeraAtendimento extends IInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AtendimentoControle atendimentoControle = new AtendimentoControle();
	private MPanelCenter panelForm;

	private MTextField txtCpf;
	private JTextField txtNome;
	private MDateTimeField txtDtaEntrada;
	private JTextArea areaQueixa;
	private JComboBox<Integer> cbxPrioridade;
	private JComboBox<Object> cbxAla;
	private JTextField txtObservacao;
	private MJButton btnLimpar;
	private MJButton btnCadastrar;

	@Override
	public String getInternalTitle() {
		return "Gerar Atendimento";
	}

	@Override
	protected JPanel getPanelForm() throws Exception {

		String colunas = "[grow][grow][][grow][grow]";
		String linhas = "[grow][23px][5px,fill][23px][5px][60px][5px][23px][5px][5px][35px][grow,fill]";
		panelForm = new MPanelCenter(colunas, linhas);

		txtCpf = new MTextField("###.###.###-##");
		txtNome = new MTextField();
		txtDtaEntrada = new MDateTimeField();
		areaQueixa = new JTextArea();
		cbxPrioridade = new JComboBox<Integer>();
		cbxAla = new JComboBox<Object>();
		txtObservacao = new MTextField();

		txtNome.setColumns(10);
		areaQueixa.setColumns(50);
		areaQueixa.setRows(3);
		areaQueixa.setBorder(new LineBorder(new Color(171, 173, 179)));
		cbxPrioridade.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5 }));
		txtNome.setEditable(false);

		ArrayList<Object> alas = new ArrayList<Object>(Arrays.asList(EnumAlaHospital.listar()));
		alas.add(0, "Selecione");
		cbxAla.setModel(new DefaultComboBoxModel<Object>(alas.toArray()));

		// linha 1
		panelForm.add(new JLabel("CPF"), "flowx,cell 1 1,growy");
		panelForm.add(new JLabel("Nome"), "flowx,cell 3 1,growy");
		panelForm.add(txtCpf, "flowx,cell 1 1,grow");
		panelForm.add(txtNome, "cell 3 1,grow");

		// linha 2
		panelForm.add(new JLabel("Entrada"), "flowx,cell 1 3,growy");
		panelForm.add(new JLabel("Observa��o"), "flowx,cell 3 3,growy");
		panelForm.add(txtDtaEntrada, "cell 1 3,grow");
		panelForm.add(txtObservacao, "cell 3 3,grow");

		// linha 3
		panelForm.add(new JLabel("Queixa"), "flowx,cell 1 5 3 1");
		panelForm.add(new JScrollPane(areaQueixa), "cell 1 5 3 1,grow");

		// linha 3
		panelForm.add(new JLabel("Prioridade"), "flowx,cell 1 7,growy");
		panelForm.add(new JLabel("Ala"), "flowx,cell 3 7,growy");
		panelForm.add(cbxPrioridade, "flowx,cell 1 7,grow");
		panelForm.add(cbxAla, "cell 3 7,grow");

		btnLimpar = new MJButton("Limpar Tela");
		btnCadastrar = new MJButton("Gerar atendimento");

		addRodaPe(btnCadastrar);
		addRodaPe(btnLimpar);

		return panelForm;
	}

	@Override
	protected void addEvents() {

		txtCpf.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				buscar();
			}
		});

		btnCadastrar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				gravar();
			}
		});

		btnLimpar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				limpar();
			}
		});

	}

	private void buscar() {

		txtNome.setEditable(false);
		String cpf = txtCpf.getApenasFigitos();
		try {
			if (StringUtils.isBlank(cpf))
				return;

			btnCadastrar.setEnabled(false);
			Paciente paciente = atendimentoControle.buscarPaciente(cpf);

			if (paciente == null) {
				JOptionPane.showMessageDialog(this, "Nenhum paciente encontrado para o CPF: " + cpf);
				limpar();
				return;
			}
			btnCadastrar.setEnabled(true);
			txtNome.setText(paciente.getNome());
			panelForm.repaint();
			txtDtaEntrada.setDate(new Date());
			txtDtaEntrada.setEditable(false);
			JOptionPane.showMessageDialog(GeraAtendimento.this, "Paciente: " + paciente.getNome());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(GeraAtendimento.this, e.getMessage());
		} finally {
			txtNome.setEditable(true);
		}

	}

	private void gravar() {

		Atendimento atendimento = montarAtendimento();
		btnCadastrar.setEnabled(false);

		try {
			String msg = atendimentoControle.gravarNovoAtendimento(atendimento);
			JOptionPane.showMessageDialog(GeraAtendimento.this, msg);
			limpar();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(GeraAtendimento.this, e.getMessage());
		} finally {
			btnCadastrar.setEnabled(true);
		}
	}

	private Atendimento montarAtendimento() {

		Atendimento atendimento = new Atendimento();
		atendimento.setCpf(txtCpf.getApenasFigitos());
		atendimento.setNome(txtNome.getText());
		atendimento.setDataEntrada(txtDtaEntrada.getDate());
		atendimento.setQueixa(areaQueixa.getText());
		atendimento.setPrioridade((Integer) cbxPrioridade.getSelectedItem());
		if (cbxAla.getSelectedIndex() > 0)
			atendimento.setAla((EnumAlaHospital) cbxAla.getSelectedItem());
		atendimento.setObservacao(txtObservacao.getText());
		return atendimento;
	}

	@Override
	public void limpar() {
		txtNome.setText("");
		txtCpf.setText("");
		txtDtaEntrada.setDate(null);
		areaQueixa.setText("");
	}

}
