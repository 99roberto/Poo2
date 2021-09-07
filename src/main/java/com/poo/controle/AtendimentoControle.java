package com.poo.controle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.FilaAtendimento;
import com.poo.modelo.Paciente;
import com.poo.modelo.dao.AtendimentoDao;
import com.poo.modelo.dao.HospitalDao;
import com.poo.modelo.dao.PacienteDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.dao.SemVagaExeception;
import com.poo.modelo.vo.AtendimentoFilaVo;
import com.poo.modelo.vo.CriarAtenimentoRetornoVo;

public class AtendimentoControle extends HospitalControle {
	private PacienteDao pacienteDao = new PacienteDao();
	private AtendimentoDao atendimentoDao = new AtendimentoDao();
	private HospitalDao hospitalDao = new HospitalDao();

	public Paciente buscarPaciente(String cpf) throws ControleExcption {
		try {
			Paciente paciente = pacienteDao.buscarPorCpf(cpf);
			if (paciente != null) {
				hasAtendimento(cpf);
			}
			return paciente;
		} catch (PersistenciaException e) {
			e.printStackTrace();
			throw new ControleExcption("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	private void hasAtendimento(String cpf) throws ControleExcption, PersistenciaException {
		Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(cpf);
		if (atendimento != null && atendimento.getDataSaida() == null) {
			SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			throw new ControleExcption(
					"Já há um atendimento para esse paciente. Entrada:" + dtf.format(atendimento.getDataEntrada()));
		}

	}

	public String gravar(Atendimento atendiemnto) throws ControleExcption {
		try {
			if (atendiemnto == null)
				throw new ControleExcption("Nenhum paciente informado");

			if (StringUtils.isBlank(atendiemnto.getCpf()))
				throw new ControleExcption("CPF do paciente não informado");

			if (StringUtils.isBlank(atendiemnto.getNome()))
				throw new ControleExcption("Nome do paciente não informado");

			hasAtendimento(atendiemnto.getCpf());

			CriarAtenimentoRetornoVo vo = atendimentoDao.criarAtendimento(atendiemnto);

			if (vo.getInInternado())
				return "Paciente internado na ala " + vo.getAla();
			if (vo.getIndFilaInternacao())
				return "Paciente adicionado na fila da ala " + vo.getAla() + " \nPosiçãoo na fila da ala: "
						+ vo.getPosicaoFilaAla() + " \nPosição na fila da enfermagem: " + vo.getPosicaoFilaEnfermagem();

			return "Paciente adicionado na fina de atendimento (tamanho da fila " + atendiemnto.getPrioridade() + ": "
					+ vo.getPosicaoFilaAtendimento();
		} catch (ControleExcption e) {
			throw e;
		} catch (SemVagaExeception e) {
			e.printStackTrace();
			throw new ControleExcption(
					"Não temos vagas para internação na enfermaria. \nPor favor procure outro hospital", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControleExcption("Erro ao salvar paciente: \n" + e.getMessage(), e);
		} finally {
			resturarHospital();
		}
	}

	public List<AtendimentoFilaVo> getAtendimentosPorFinalizar() throws ControleExcption {
		try {
			List<AtendimentoFilaVo> lstVo = new ArrayList<AtendimentoFilaVo>();

			for (AlaHospial ala : getHospital().getAlas().values()) {
				for (int i = 0; i < ala.getLeitos().size(); i++) {
					Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(ala.getLeitos().get(i));

					AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Internado na ala " + ala.getAla());
					lstVo.add(vo);
				}

				for (int i = 0; i < ala.getFilaEspera().size(); i++) {
					Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(ala.getFilaEspera().get(i));

					AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Fila da ala " + ala.getAla());
					lstVo.add(vo);
				}
			}
			
			for (String cpf : getHospital().getFilaEmfermagem()) {
				Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(cpf);
				AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Fila Enfermagem");
				lstVo.add(vo);
			}
			
			return lstVo;
		} catch (PersistenciaException e) {
			resturarHospital();
			throw new ControleExcption("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public List<AtendimentoFilaVo> getPacientesInterados() throws ControleExcption {
		try {
			List<AtendimentoFilaVo> lstVo = new ArrayList<AtendimentoFilaVo>();

			for (AlaHospial ala : getHospital().getAlas().values()) {
				for (int i = 0; i < ala.getLeitos().size(); i++) {
					Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(ala.getLeitos().get(i));
					if (atendimento == null)
						throw new ControleExcption("Nenhum atendimento encontrado");
					AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Internado na ala " + ala.getAla());
					lstVo.add(vo);
				}
			}
			for (String cpf : getHospital().getFilaEmfermagem()) {
				Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(cpf);
				AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Fila Enfermagem");
				lstVo.add(vo);
			}

			return lstVo;
		} catch (PersistenciaException e) {
			resturarHospital();
			throw new ControleExcption("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public void finalizarAtendimento(Atendimento atendimento) throws ControleExcption {
		if (StringUtils.isBlank(atendimento.getCpf()))
			throw new ControleExcption("Informe todos os dados do paciente");
		try {
			getHospital().getFilaEmfermagem().remove(atendimento.getCpf());
			for (AlaHospial halohospital : getHospital().getAlas().values()) {
				halohospital.getLeitos().remove(atendimento.getCpf());
				halohospital.getFilaEspera().remove(atendimento.getCpf());
			}
			atendimento.setDataSaida(new Date());
			atendimentoDao.salva(atendimento);
			hospitalDao.salva(getHospital());
		} catch (PersistenciaException e) {
			resturarHospital();
			e.printStackTrace();
			throw new ControleExcption("Erro ao finalizar o atendimento: " + e.getMessage());
		}
	}

}
