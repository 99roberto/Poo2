package com.poo.visao;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.poo.controle.ControleException;
import com.poo.controle.PacienteControle;
import com.poo.modelo.Paciente;
import com.poo.modelo.TipoSang;
import com.poo.visao.componentes.IInternalFrame;
import com.poo.visao.componentes.MDateField;
import com.poo.visao.componentes.MJButton;
import com.poo.visao.componentes.MTextField;

import net.miginfocom.swing.MigLayout;

public class GerenciarPaciente extends IInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PacienteControle controle = new PacienteControle();

	private JPanel panelForm;

	private MTextField txtNome;
	private MTextField txtNomePai;
	private MTextField txtEndereco;
	private MTextField txtNomeMae;
	private MTextField txtCpf;

	private MDateField txtDtaNacimento;

	private JComboBox<TipoSang> cmbTipoSanguineo;

	private JButton btnLimpar;
	private JButton btnCadastrar;

	@Override
	public String getInternalTitle() {
		return "Gerenciar Paciente";
	}

	@Override
	protected JPanel getPanelForm() throws Exception {

		panelForm = new JPanel();
		panelForm.setPreferredSize(new Dimension(0, 0));
		panelForm.setLayout(new MigLayout("", "[grow][grow][][grow][grow]",
				"[grow][23px][5px,fill][23px][5px][23px][5px][23px][5px][5px][35px][grow,fill]"));

		panelForm.setBackground(new java.awt.Color(237, 242, 249));

		JLabel cpfLabel = new JLabel("CPF");
		panelForm.add(cpfLabel, "flowx,cell 1 1,growy");

		JLabel nomeLabel = new JLabel("Nome");
		panelForm.add(nomeLabel, "flowx,cell 3 1,growy");

		JLabel nomeMaeLabel = new JLabel("Nome da M\u00E3e");
		panelForm.add(nomeMaeLabel, "flowx,cell 1 3,growy");

		JLabel nomePaiLabel = new JLabel("Nome do Pai");
		panelForm.add(nomePaiLabel, "flowx,cell 3 3");

		JLabel enderecoLabel = new JLabel("Endere\u00E7o");
		panelForm.add(enderecoLabel, "flowx,cell 1 5 3 1,growy");

		txtEndereco = new MTextField();
		panelForm.add(txtEndereco, "cell 1 5 3 1,grow");
		txtEndereco.setColumns(20);

		txtNomeMae = new MTextField();
		txtNomeMae.setColumns(20);

		txtNomePai = new MTextField();
		txtNomePai.setColumns(20);

		txtCpf = new MTextField("###.###.###-##");

		txtNome = new MTextField();
		txtNome.setColumns(20);

		panelForm.add(txtCpf, "flowx,cell 1 1,grow");
		panelForm.add(txtNome, "cell 3 1,grow");
		panelForm.add(txtNomeMae, "cell 1 3,grow");
		panelForm.add(txtNomePai, "cell 3 3,grow");

		JLabel nascimentoLabel = new JLabel("Nascimento");
		panelForm.add(nascimentoLabel, "flowx,cell 1 7,growy");

		JLabel tipoSanguineoLabel = new JLabel("Tipo Sangu\u00EDneo");
		panelForm.add(tipoSanguineoLabel, "flowx,cell 3 7,alignx left,growy");

		txtDtaNacimento = new MDateField();
		panelForm.add(txtDtaNacimento, "cell 1 7,grow");

		cmbTipoSanguineo = new JComboBox<TipoSang>();
		cmbTipoSanguineo.setModel(new DefaultComboBoxModel<TipoSang>(TipoSang.list()));
		panelForm.add(cmbTipoSanguineo, "cell 3 7,grow");

		btnLimpar = new MJButton("Limpar Tela");
		btnCadastrar = new MJButton("Cadastrar Paciente");

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
		final String cpf = txtCpf.getApenasFigitos();
		SwingWorker<Paciente, Void> mySwingWorker = new SwingWorker<Paciente, Void>() {

			@Override
			protected Paciente doInBackground() throws Exception {
				try {
					return controle.buscar(cpf);
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(GerenciarPaciente.this, e.getMessage());
				} finally {
					txtNome.setEditable(true);
				}
				return null;
			}

			@Override
			protected void done() {
				super.done();
				try {
					Paciente paciente = get();
					if (paciente == null)
						return;
					txtNome.setText(paciente.getNome());
					txtNomeMae.setText(paciente.getNomeMae());
					txtNomePai.setText(paciente.getNomePai());
					txtEndereco.setText(paciente.getEndereco());
					txtDtaNacimento.setDate(paciente.getNascimento());
					cmbTipoSanguineo.setSelectedItem(paciente.getTipoSanguinio());

					panelForm.repaint();
					JOptionPane.showMessageDialog(GerenciarPaciente.this, "Paciente: " + paciente.getNome());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(GerenciarPaciente.this, e.getMessage());
				} finally {
					txtNome.setEditable(true);
				}
			}
		};

		mySwingWorker.execute();
	}

	private void gravar() {

		txtNome.setEditable(false);
		Paciente paciente = obtemPaciente();
		try {
			controle.gravarPaciente(paciente);
			JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso");
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(GerenciarPaciente.this, e.getMessage());
		}

	}

	private Paciente obtemPaciente() {

		Paciente paciente = new Paciente();
		paciente.setNome(txtNome.getText());
		paciente.setCPF(txtCpf.getApenasFigitos());
		paciente.setNomeMae(txtNomeMae.getText());
		paciente.setNomePai(txtNomePai.getText());
		paciente.setEndereco(txtEndereco.getText());
		paciente.setNascimento(txtDtaNacimento.getDate());
		paciente.setTipoSanguinio((TipoSang) cmbTipoSanguineo.getSelectedItem());
		return paciente;
	}

	@Override
	public void limpar() {

		txtNome.setText("");
		txtNomeMae.setText("");
		txtNomePai.setText("");
		txtEndereco.setText("");
		txtDtaNacimento.setDate(null);
		cmbTipoSanguineo.setSelectedIndex(0);
		txtCpf.setText("");
	}
}
