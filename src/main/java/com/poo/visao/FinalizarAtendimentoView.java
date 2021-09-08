package com.poo.visao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.poo.controle.AtendimentoControle;
import com.poo.controle.ControleException;
import com.poo.modelo.Atendimento;
import com.poo.modelo.vo.AtendimentoFilaVo;
import com.poo.visao.componentes.AtendimentoTableModel;
import com.poo.visao.componentes.MJButton;
import com.poo.visao.componentes.PanelMestreEscraco;

/**
 *
 */
public class FinalizarAtendimentoView extends PanelMestreEscraco {

	private static final long serialVersionUID = 1L;

	private AtendimentoControle controle = new AtendimentoControle();
	private AtendimentoTableModel tbModel;
	protected AtendimentoFilaVo atendimentoCorrente;
	private MJButton finalizarAtendimentoBtn;

	@Override
	public String getInternalTitle() {
		return "Finalizar Atendimento";
	}

	@Override
	public void limpar() {
		super.limpar();
	}

	@Override
	protected void criaBotoes() {

		finalizarAtendimentoBtn = new MJButton("Finalizar Atendimento");
		addRodaPe(finalizarAtendimentoBtn);
	}

	@Override
	protected void addEvents() {

		super.addEvents();

		if (finalizarAtendimentoBtn != null)
			finalizarAtendimentoBtn.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					finalizarAtendimento();
				}
			});
	}

	protected void finalizarAtendimento() {

		if (atendimentoCorrente == null)
			return;

		try {
			controle.finalizarAtendimento(atendimentoCorrente.getAtendimento());
			JOptionPane.showMessageDialog(this, "Atendiemtno finalizado com sucesso");
			limpar();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	@Override
	public void dadosDefault() {

		try {
			if (controle == null)
				controle = new AtendimentoControle();
			List<AtendimentoFilaVo> lst = controle.getAtendimentosPorFinalizar();
			tbModel.setDados(lst);

		} catch (ControleException e) {
			JOptionPane.showMessageDialog(FinalizarAtendimentoView.this,
					"Erro ao buscar proximo paciente: " + e.getMessage());
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
			tbModel = new AtendimentoTableModel();
		return tbModel;
	}

}
