package com.poo.visao;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.poo.controle.ConsultaControle;
import com.poo.controle.ControleException;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Consulta;
import com.poo.modelo.EnumAlaHospital;
import com.poo.visao.componentes.IInternalFrame;
import com.poo.visao.componentes.MJButton;
import com.poo.visao.componentes.MPanelCenter;
import com.poo.visao.componentes.MTextField;

/**
 *
 */
public class GeraConsulta extends IInternalFrame {

	private static final long serialVersionUID = 1L;

	private ConsultaControle cControle = new ConsultaControle();
	private MPanelCenter panelForm;

	private MTextField txtCpf;
	private JTextField txtNome;

	private JTextArea areaQueixa;
	private JTextArea areaAvaliacao;
	private JTextArea areaPrescricao;
	private JTextArea obsTextField;

	private JComboBox<EnumAlaHospital> cbxAla;

	private MJButton btnFfinalizarConsulta;
	private MJButton btnInternar;;
	private MJButton btnLimpar;

	@Override
	public String getInternalTitle() {
		return "Gerar Consulta";
	}

	@Override
	protected JPanel getPanelForm() throws Exception {

		String colunas = "[grow][grow][][grow][grow]";
		String linhas = "[grow][23px][5px,fill][50px][5px][50px][5px][50px][5px][23px][5px][50px][5px][grow,fill]";
		panelForm = new MPanelCenter(colunas, linhas);

		txtCpf = new MTextField("###.###.###-##");
		txtNome = new MTextField();
		areaQueixa = new JTextArea();
		areaAvaliacao = new JTextArea();
		areaPrescricao = new JTextArea();
		cbxAla = new JComboBox<EnumAlaHospital>();
		obsTextField = new JTextArea();

		txtNome.setColumns(10);
		areaQueixa.setColumns(50);
		areaQueixa.setRows(3);
		areaQueixa.setBorder(new LineBorder(new Color(171, 173, 179)));
		areaAvaliacao.setBorder(new LineBorder(new Color(171, 173, 179)));
		areaPrescricao.setBorder(new LineBorder(new Color(171, 173, 179)));
		obsTextField.setBorder(new LineBorder(new Color(171, 173, 179)));
		cbxAla.setModel(new DefaultComboBoxModel<EnumAlaHospital>(EnumAlaHospital.listar()));

		txtCpf.setEditable(false);
		txtNome.setEditable(false);
		// linha 1
		panelForm.add(new JLabel("CPF"), "flowx,cell 1 1,growy");
		panelForm.add(new JLabel("Nome"), "flowx,cell 3 1,growy");
		panelForm.add(txtCpf, "flowx,cell 1 1,grow");
		panelForm.add(txtNome, "cell 3 1,grow");

		// linha 2
		panelForm.add(new JLabel("Queixa"), "flowx,cell 1 2 3 1");
		panelForm.add(new JScrollPane(areaQueixa), "cell 1 3 3 1,grow");
		// linha 3
		panelForm.add(new JLabel("Avaliação"), "flowx,cell 1 4 3 1");
		panelForm.add(new JScrollPane(areaAvaliacao), "cell 1 5 3 1,grow");
		// linha 4
		panelForm.add(new JLabel("Medicação prescrita"), "flowx,cell 1 6 3 1");
		panelForm.add(new JScrollPane(areaPrescricao), "cell 1 7 3 1,grow");

		// linha 3
		panelForm.add(new JLabel("Ala"), "flowx,cell 1 9 3 1,growy");
		panelForm.add(cbxAla, "cell 1 9 3 ,grow");

		// linha 3
		panelForm.add(new JLabel("Observação"), "flowx,cell 1 10 3 1,growy");
		panelForm.add(new JScrollPane(obsTextField), "cell 1 11 3 1,grow");

		btnLimpar = new MJButton("Limpar Tela");
		btnFfinalizarConsulta = new MJButton("Finalizar Consulta");
		btnInternar = new MJButton("Encaminhar Internação");

		addRodaPe(btnFfinalizarConsulta);
		addRodaPe(btnInternar);
		addRodaPe(btnLimpar);

		return panelForm;
	}

	@Override
	protected void addEvents() {

		btnInternar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				internar();
			}
		});

		btnLimpar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				limpar();
			}
		});

		btnFfinalizarConsulta.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnFfinalizarConsulta.isEnabled())
					finalizarConsulta();
			}
		});

	}

	private Consulta getConsultaBean() {

		Consulta consulta = new Consulta();
		consulta.setNome(txtNome.getText());
		consulta.setCpf(txtCpf.getApenasFigitos());

		consulta.setQueixa(areaQueixa.getText());
		consulta.setAvaliacao(areaAvaliacao.getText());
		consulta.setPrescricao(areaPrescricao.getText());
		consulta.setAla((EnumAlaHospital) cbxAla.getSelectedItem());
		consulta.setObservacao(obsTextField.getText());

		return consulta;
	}

	private void internar() {

		Consulta consulta = getConsultaBean();
		try {
			String msg = cControle.internar(consulta);
			JOptionPane.showMessageDialog(this, msg);
			limpar();
			dadosDefault();
		} catch (ControleException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	private void finalizarConsulta() {

		Consulta consulta = getConsultaBean();
		try {
			cControle.finalizaConsulta(consulta);
			JOptionPane.showMessageDialog(this, "Consulta finalizada com sucesso");
			limpar();
			dadosDefault();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	@Override
	public void limpar() {

		txtNome.setText("");
		txtCpf.setText("");
		areaQueixa.setText("");
		areaAvaliacao.setText("");
		obsTextField.setText("");
		cbxAla.setSelectedIndex(-1);
		dadosDefault();
	}

	@Override
	public void dadosDefault() {

		try {

			if (cControle == null)
				cControle = new ConsultaControle();
			Atendimento atendimento = cControle.proximoAtendimento();

			if (atendimento == null) {
				btnFfinalizarConsulta.setEnabled(false);
				return;
			}
			txtNome.setText(atendimento.getNome());
			txtCpf.setText(atendimento.getCpf());
			areaQueixa.setText(atendimento.getQueixa());
			cbxAla.setSelectedItem(atendimento.getAla());

		} catch (ControleException e) {
			JOptionPane.showMessageDialog(GeraConsulta.this, "Erro ao buscar proximo paciente: " + e.getMessage());
		}

	}

}
