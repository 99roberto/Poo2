package com.poo.visao.componentes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.poo.modelo.vo.InternacaoVo;

public class InternacaoVoTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private String[] columnNames = new String[] { "Nome", "Ala", "posição", "Leitos Vagos" };
		private List<InternacaoVo> dados = new ArrayList<InternacaoVo>();

		public List<InternacaoVo> getDados() {
			return dados;
		}

		public void setDados(List<InternacaoVo> dados) {
			this.dados = dados;
			fireTableDataChanged();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			if (dados == null)
				return 0;
			return dados.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			if (dados == null)
				return null;
			InternacaoVo vo = dados.get(row);
			switch (col) {
			case 0:
				return vo.getAtendimento().getNome();
			case 1:
				return vo.getAla().toString();
			case 2:
				return vo.getPosicaoFilaAla();
			default:
				return vo.getLeitosVagos() + "/" + vo.getLeitos();
			}
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void setValueAt(Object value, int row, int col) {

		}
	}