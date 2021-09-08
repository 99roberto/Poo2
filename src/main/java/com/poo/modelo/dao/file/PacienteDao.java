package com.poo.modelo.dao.file;

import java.util.HashMap;

import com.poo.modelo.Paciente;
import com.poo.modelo.dao.IPacienteDao;
import com.poo.modelo.dao.PersistenciaException;

public class PacienteDao extends AbstractDao<String, Paciente> implements IPacienteDao {

	@Override
	protected String getNomeArq() {
		return "pacientes.txt";
	}

	@Override
	public Paciente buscarPorCpf(String cpf) throws PersistenciaException {

		try {
			HashMap<String, Paciente> map = lerArquivoBinario();
			return map.get(cpf);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	@Override
	public void salvarPaciente(Paciente paciente) throws PersistenciaException {

		try {
			HashMap<String, Paciente> map = lerArquivoBinario();
			map.remove(paciente.getCPF());
			map.put(paciente.getCPF(), paciente);
			gravarArquivoBinario(map);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}

	}

	@Override
	protected Class<Paciente> getValueClass() {
		return Paciente.class;
	}

	@Override
	protected Class<String> getKeyClass() {
		return String.class;
	}

}
