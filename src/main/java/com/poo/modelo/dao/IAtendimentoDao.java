package com.poo.modelo.dao;

import java.util.Date;
import java.util.List;

import com.poo.modelo.Atendimento;
import com.poo.modelo.vo.CriacaoAtendimentoRetornoVo;

public interface IAtendimentoDao {

	public Atendimento buscarAtendimentoAberto(String cpf) throws PersistenciaException;

	public List<Atendimento> buscarPorCpf(String cpf) throws PersistenciaException;

	public List<Atendimento> buscarAtendimentosAberto() throws PersistenciaException;

	public Atendimento buscarPorEntrada(String cpf, Date dtaEntrada) throws PersistenciaException;

	public void salvarAtendimento(Atendimento atendimento) throws PersistenciaException;

	public CriacaoAtendimentoRetornoVo criarAtendimento(Atendimento atendiemnto)
			throws PersistenciaException, SemVagaExeception;

}
