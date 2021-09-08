package com.poo.controle;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Consulta;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.IConsultaDao;
import com.poo.modelo.dao.IHospitalDao;
import com.poo.modelo.dao.PersistenciaException;

public class ConsultaControle extends HospitalControle {

	private IAtendimentoDao atendimentoDao = DaoFactory.getInstae().getAtendimentoDao();
	private IHospitalDao hospitalDao = DaoFactory.getInstae().getHospitalDao();
	private IConsultaDao consultaDao = DaoFactory.getInstae().getConsultaDao();

	public Atendimento proximoAtendimento() throws ControleException {

		try {
			String cpf = getHospital().getFilaAtendimento().proximo();
			return atendimentoDao.buscarAtendimentoAberto(cpf);
		} catch (PersistenciaException e) {
			resturarHospital();
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public String internar(Consulta consulta) throws ControleException {

		try {
			if (consulta.getAla() == null)
				throw new ControleException("Nenhuma ala informada");

			removerFilaAtendimento(consulta);

			AlaHospial ala = getHospital().getAlas().get(consulta.getAla());
			String msg = "";
			if (ala.temVaga()) {
				ala.internar(consulta.getCpf());
				msg = "Paciente internado na ala " + ala.getAla();
			} else {
				ala.adicionarFila(consulta.getCpf());
				msg = "Paciente adicionado na fila da ala " + ala.getAla() + " \nPosição na fila da ala: "
						+ ala.getFilaEspera().size() + " \nPosição na fila da enfermagem: "
						+ getHospital().getFilaEmfermagem().size();

			}
			consultaDao.salvarConsulta(consulta);
			hospitalDao.salvarHospital(getHospital());
			return msg;
		} catch (ControleException e) {
			throw e;
		} catch (Exception e) {
			resturarHospital();
			throw new ControleException("Erro ao gerar internação: " + e.getCause());
		}
	}

	private void removerFilaAtendimento(Consulta consulta) throws ControleException {

		if (StringUtils.isBlank(consulta.getCpf()) || StringUtils.isBlank(consulta.getNome()))
			throw new ControleException("Informe todos os dados do paciente");

		getHospital().getFilaAtendimento().remove(consulta.getCpf());
	}

	public void finalizaConsulta(Consulta consulta) throws ControleException {

		try {
			removerFilaAtendimento(consulta);
			consulta.setDataTermino(new Date());

			consultaDao.salvarConsulta(consulta);
			hospitalDao.salvarHospital(getHospital());
		} catch (ControleException e) {
			throw e;
		} catch (Exception e) {
			resturarHospital();
			e.printStackTrace();
			throw new ControleException("Erro ao finalizar consulta: " + e.getMessage(), e);
		}
	}

}
