package com.poo.modelo.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.poo.controle.ControleExcption;
import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Hospital;
import com.poo.modelo.vo.CriarAtenimentoRetornoVo;

public class AtendimentoDao extends AbstractDao<String, Atendimento> {
	private SimpleDateFormat dtf = new SimpleDateFormat("ddMMyyyyHH:mm");
	private HospitalDao hospitalDao = new HospitalDao();

	@Override
	protected String getNomeArq() {
		return "atendimentos.txt";
	}

	public Atendimento buscarAtendimentoAberto(String cpf) throws PersistenciaException {
		List<Atendimento> atendimentos = buscarPorCpf(cpf);
		for (Atendimento atendimento : atendimentos) {
			if (atendimento.getDataSaida() == null)
				return atendimento;
		}

		return null;
	}

	public List<Atendimento> buscarPorCpf(String cpf) throws PersistenciaException {
		try {
			HashMap<String, Atendimento> map = lerArquivoBinario();

			List<Atendimento> lst = new ArrayList<Atendimento>();
			for (Atendimento atd : map.values()) {
				if (atd.getCpf() != null && atd.getCpf().equals(cpf))
					lst.add(atd);
			}
			Collections.sort(lst);
			return lst;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	public List<Atendimento> buscarAtendimentosAberto() throws PersistenciaException {
		try {
			HashMap<String, Atendimento> map = lerArquivoBinario();

			List<Atendimento> lst = new ArrayList<Atendimento>();
			for (Atendimento atd : map.values()) {
				if (atd.getDataSaida() == null)
					lst.add(atd);
			}
			Collections.sort(lst);
			return lst;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	public Atendimento buscarPorEntrada(String cpf, Date dtaEntrada) throws PersistenciaException {
		try {
			HashMap<String, Atendimento> map = lerArquivoBinario();

			List<Atendimento> lst = new ArrayList<Atendimento>();
			for (Atendimento atd : map.values()) {
				if (atd.getCpf() != null && atd.getCpf().equals(cpf)) {
					lst.add(atd);
					if (atd.getDataEntrada() == null && dtaEntrada == null || atd.getDataEntrada().equals(dtaEntrada))
						return atd;

				}
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	public void salva(Atendimento atendimento) throws PersistenciaException {
		try {
			HashMap<String, Atendimento> map = lerArquivoBinario();
			if (atendimento.getChave() == null)
				atendimento.setChave(atendimento.getCpf() + dtf.format(atendimento.getDataEntrada()));

			map.put(atendimento.getChave(), atendimento);
			gravarArquivoBinario(map);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}

	}

	@Override
	protected Class<Atendimento> getValueClass() {
		// TODO Auto-generated method stub
		return Atendimento.class;
	}

	@Override
	protected Class<String> getKeyClass() {
		// TODO Auto-generated method stub
		return String.class;
	}

	private CriarAtenimentoRetornoVo internar(Atendimento atendiemnto, Hospital hospital)
			throws PersistenciaException, SemVagaExeception {

		if (atendiemnto.getAla() == null)
			throw new PersistenciaException("Informe uma ala para a internação");

		CriarAtenimentoRetornoVo vo = new CriarAtenimentoRetornoVo();

		AlaHospial ala = hospital.getAlas().get(atendiemnto.getAla());
		boolean semVagas = false;
		if (ala.temVaga()) {

			try {
				ala.internar(atendiemnto.getCpf());
			} catch (Exception e) {
				throw new PersistenciaException(e.getMessage(), e);
			}
			vo.setInInternado(true);
			vo.setAla(ala.getAla());
		} else if (hospital.temVagasEnfermagem()) {
			hospital.getFilaEmfermagem().add(atendiemnto.getCpf());
			ala.adFila(atendiemnto.getCpf());

			vo.setIndFilaInternacao(true);
			vo.setAla(ala.getAla());
			vo.setPosicaoFilaAla(ala.getFilaEspera().size());
			vo.setPosicaoFilaEnfermagem(hospital.getFilaEmfermagem().size());
		} else {
			semVagas = true;
			atendiemnto.setDataSaida(new Date());
			atendiemnto.setObservacao("Paciente encaminhado para outro hospital por indisponibilidade de leitos");
		}
		salva(atendiemnto);
		hospitalDao.salva(hospital);
		if (semVagas)
			throw new SemVagaExeception(
					"não temos vagas para internação na enfermaria. \nPor favor procure outro hospital");
		return vo;
	}

	public CriarAtenimentoRetornoVo criarAtendimento(Atendimento atendiemnto)
			throws PersistenciaException, SemVagaExeception {
		Hospital hospital = hospitalDao.get();

		if (atendiemnto.getPrioridade() == 5) {
			return internar(atendiemnto, hospital);
		}
		CriarAtenimentoRetornoVo vo = new CriarAtenimentoRetornoVo();
		vo.setIndFilaInternacao(false);
		vo.setPosicaoFilaAtendimento(hospital.getfAtendimento().getFila(atendiemnto.getPrioridade()).size());
		vo.setInInternado(false);
		
		hospital.getfAtendimento().add(atendiemnto.getPrioridade(), atendiemnto.getCpf());
		salva(atendiemnto);
		hospitalDao.salva(hospital);

		return vo;
	}

}
