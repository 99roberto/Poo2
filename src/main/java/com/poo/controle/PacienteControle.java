package com.poo.controle;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.Paciente;
import com.poo.modelo.dao.PacienteDao;
import com.poo.modelo.dao.PersistenciaException;

public class PacienteControle {
	private PacienteDao dao = new PacienteDao();

	public Paciente buscar(String cpf) throws ControleExcption {
		try {
			System.err.println("#"+cpf);
			return dao.buscarPorCpf(cpf);
		} catch (PersistenciaException e) {
			throw new ControleExcption("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public void gravar(Paciente paciente) throws ControleExcption {
		try {
			if(paciente==null)
				throw new ControleExcption("Nenhum paciente informado");

			if(StringUtils.isBlank(paciente.getCPF()))
				throw new ControleExcption("CPF do paciente n�o informado");

			if(StringUtils.isAllBlank(paciente.getNome()))
				throw new ControleExcption("Nome do paciente n�o informado");

			dao.salva(paciente);
		} catch (ControleExcption  e) {
			throw e;
		}catch (PersistenciaException e) {
			throw new ControleExcption("Erro ao salvar paciente: \n" + e.getMessage(), e);
		}
	}

}
