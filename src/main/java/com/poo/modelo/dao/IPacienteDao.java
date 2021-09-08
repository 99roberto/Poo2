package com.poo.modelo.dao;

import com.poo.modelo.Paciente;

public interface IPacienteDao {

	public Paciente buscarPorCpf(String cpf) throws PersistenciaException;

	public void salvarPaciente(Paciente paciente) throws PersistenciaException;

}
