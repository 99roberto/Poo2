package com.poo.modelo.dao;

import java.util.List;

import com.poo.modelo.Consulta;

public interface IConsultaDao {

	public List<Consulta> buscarPorCpf(String cpf) throws PersistenciaException;

	public void salvarConsulta(Consulta consulta) throws PersistenciaException;

}
