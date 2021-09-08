package com.poo.controle.service;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.poo.controle.ControleException;
import com.poo.modelo.Atendimento;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.PersistenciaException;

public class AtendimentoService {

	public static void verificarTemAtendimento(IAtendimentoDao atendimentoDao, String cpf)
			throws ControleException, PersistenciaException {

		Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(cpf);
		if (atendimento != null && atendimento.getDataSaida() == null) {
			SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			throw new ControleException(
					"Já há um atendimento para esse paciente. Entrada:" + dtf.format(atendimento.getDataEntrada()));
		}

	}

	public static void validarAtendimentoBean(Atendimento atendiemnto) throws ControleException {

		if (atendiemnto == null)
			throw new ControleException("Nenhum paciente informado");

		if (StringUtils.isBlank(atendiemnto.getCpf()))
			throw new ControleException("CPF do paciente não informado");

		if (StringUtils.isBlank(atendiemnto.getNome()))
			throw new ControleException("Nome do paciente não informado");
	}

}
