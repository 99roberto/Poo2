package com.poo.visao.componentes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.poo.modelo.vo.AtendimentoFilaVo;

public class LeitosTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = new String[] { "Nome", "Situação" };
	private List<AtendimentoFilaVo> dados = new ArrayList<AtendimentoFilaVo>();

	public List<AtendimentoFilaVo> getDados() {
		return dados;
	}

	public void setDados(List<AtendimentoFilaVo> dados) {
		this.dados = dados;
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if (dados == null)
			return 0;
		return dados.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (dados == null)
			return null;
		AtendimentoFilaVo vo = dados.get(row);
		if (col == 0)
			return vo.getAtendimento().getNome();
		return vo.getDescSituacaoAtendimento();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
	}
}