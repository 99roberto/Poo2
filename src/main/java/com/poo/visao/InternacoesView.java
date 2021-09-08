package com.poo.visao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.poo.controle.ControleException;
import com.poo.controle.InternacaoControle;
import com.poo.modelo.Atendimento;
import com.poo.modelo.vo.InternacaoVo;
import com.poo.visao.componentes.InternacaoVoTableModel;
import com.poo.visao.componentes.MJButton;
import com.poo.visao.componentes.PanelMestreEscraco;

/**
 *
 */
public class InternacoesView extends PanelMestreEscraco {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InternacaoControle controle = new InternacaoControle();
	protected InternacaoVo atendimentoCorrente;

	private MJButton btnInternar;
	private InternacaoVoTableModel tbModel;

	@Override
	public String getInternalTitle() {
		return "Aguardando Internação";
	}

	@Override
	protected void criaBotoes() {

		btnInternar = new MJButton("Encaminhar Internação");
		addRodaPe(btnInternar);
	}

	@Override
	protected void addEvents() {

		if (btnInternar != null) {
			btnInternar.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					internar();
				}
			});
		}
	}

	protected void internar() {

		if (atendimentoCorrente == null)
			return;

		try {
			controle.internar(atendimentoCorrente.getAtendimento());
			JOptionPane.showMessageDialog(this, "Interna��o realizada com sucesso");
			limpar();
		} catch (ControleException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	@Override
	public void limpar() {

		tbModel.setDados(null);
		super.limpar();
	}

	@Override
	public void dadosDefault() {

		try {
			if (controle == null)
				controle = new InternacaoControle();

			List<InternacaoVo> lst = controle.getFila();
			tbModel.setDados(lst);

		} catch (ControleException e) {
			JOptionPane.showMessageDialog(InternacoesView.this, "Erro ao buscar proximo paciente: " + e.getMessage());
		}

	}

	@Override
	protected Atendimento linhaSelecionada(int selectedRow) {

		if (tbModel.getDados().isEmpty())
			return null;

		atendimentoCorrente = tbModel.getDados().get(selectedRow);
		if (atendimentoCorrente != null)
			return atendimentoCorrente.getAtendimento();
		return null;
	}

	@Override
	protected AbstractTableModel getTableModel() {

		if (tbModel == null)
			tbModel = new InternacaoVoTableModel();
		return tbModel;
	}

}
