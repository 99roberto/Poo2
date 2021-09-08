package com.poo.controle;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.Paciente;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IPacienteDao;
import com.poo.modelo.dao.PersistenciaException;

public class PacienteControle {

	private IPacienteDao dao = DaoFactory.getInstae().getPacienteDao();

	public Paciente buscar(String cpf) throws ControleException {

		try {
			return dao.buscarPorCpf(cpf);
		} catch (PersistenciaException e) {
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public void gravarPaciente(Paciente paciente) throws ControleException {

		try {
			if (paciente == null)
				throw new ControleException("Nenhum paciente informado");

			if (StringUtils.isBlank(paciente.getCPF()))
				throw new ControleException("CPF do paciente não informado");

			if (StringUtils.isAllBlank(paciente.getNome()))
				throw new ControleException("Nome do paciente não informado");

			dao.salvarPaciente(paciente);
		} catch (ControleException e) {
			throw e;
		} catch (PersistenciaException e) {
			e.printStackTrace();
			throw new ControleException("Erro ao salvar paciente: \n" + e.getMessage(), e);
		}
	}

}
