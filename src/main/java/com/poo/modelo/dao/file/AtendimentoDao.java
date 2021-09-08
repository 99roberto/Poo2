package com.poo.modelo.dao.file;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Hospital;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.dao.SemVagaExeception;
import com.poo.modelo.vo.CriacaoAtendimentoRetornoVo;

public class AtendimentoDao extends AbstractDao<String, Atendimento> implements IAtendimentoDao {

	private SimpleDateFormat dtf = new SimpleDateFormat("ddMMyyyyHH:mm");
	private HospitalDao hospitalDao = new HospitalDao();

	@Override
	protected String getNomeArq() {
		return "atendimentos.txt";
	}

	@Override
	public Atendimento buscarAtendimentoAberto(String cpf) throws PersistenciaException {

		List<Atendimento> atendimentos = buscarPorCpf(cpf);
		for (Atendimento atendimento : atendimentos) {
			if (atendimento.getDataSaida() == null)
				return atendimento;
		}

		return null;
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
	public void salvarAtendimento(Atendimento atendimento) throws PersistenciaException {

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
		return Atendimento.class;
	}

	@Override
	protected Class<String> getKeyClass() {
		return String.class;
	}

	private CriacaoAtendimentoRetornoVo internar(Atendimento atendiemnto, Hospital hospital)
			throws PersistenciaException, SemVagaExeception {

		if (atendiemnto.getAla() == null)
			throw new PersistenciaException("Informe uma ala para a internação");

		CriacaoAtendimentoRetornoVo vo = new CriacaoAtendimentoRetornoVo();

		AlaHospial ala = hospital.getAlas().get(atendiemnto.getAla());
		vo.setAla(ala.getAla());

		boolean semVagas = false;
		if (ala.temVaga()) {
			// tem vaga de elito na ala
			try {
				ala.internar(atendiemnto.getCpf());
			} catch (Exception e) {
				throw new PersistenciaException(e.getMessage(), e);
			}
			vo.setInInternado(true);
		} else if (hospital.temVagasEnfermagem()) {
			// Tem vaga na fila de enfermagem da ala
			hospital.getFilaEmfermagem().add(atendiemnto.getCpf());
			ala.adicionarFila(atendiemnto.getCpf());

			vo.setIndFilaInternacao(true);
			vo.setPosicaoFilaAla(ala.getFilaEspera().size());
			vo.setPosicaoFilaEnfermagem(hospital.getFilaEmfermagem().size());
		} else {
			// não ha vaga de leito nem na fila
			semVagas = true;
			atendiemnto.setDataSaida(new Date());
			atendiemnto.setObservacao("Paciente encaminhado para outro hospital por indisponibilidade de leitos");
		}
		salvarAtendimento(atendiemnto);
		hospitalDao.salvarHospital(hospital);
		if (semVagas)
			throw new SemVagaExeception(
					"não temos vagas para internação na enfermaria. \nPor favor procure outro hospital");
		return vo;
	}

	@Override
	public CriacaoAtendimentoRetornoVo criarAtendimento(Atendimento atendiemnto)
			throws PersistenciaException, SemVagaExeception {

		Hospital hospital = hospitalDao.get();

		if (atendiemnto.getPrioridade() == 5) {
			return internar(atendiemnto, hospital);
		}
		CriacaoAtendimentoRetornoVo vo = new CriacaoAtendimentoRetornoVo();
		vo.setIndFilaInternacao(false);
		vo.setPosicaoFilaAtendimento(hospital.getFilaAtendimento().getFila(atendiemnto.getPrioridade()).size());
		vo.setInInternado(false);

		hospital.getFilaAtendimento().add(atendiemnto.getPrioridade(), atendiemnto.getCpf());
		salvarAtendimento(atendiemnto);
		hospitalDao.salvarHospital(hospital);

		return vo;
	}

}
