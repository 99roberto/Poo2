package com.poo.controle;

import static com.poo.controle.service.AtendimentoService.validarAtendimentoBean;
import static com.poo.controle.service.AtendimentoService.verificarTemAtendimento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Paciente;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.IHospitalDao;
import com.poo.modelo.dao.IPacienteDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.dao.SemVagaExeception;
import com.poo.modelo.vo.AtendimentoFilaVo;
import com.poo.modelo.vo.CriacaoAtendimentoRetornoVo;

public class AtendimentoControle extends HospitalControle {

	private IPacienteDao pacienteDao = DaoFactory.getInstae().getPacienteDao();
	private IAtendimentoDao atendimentoDao = DaoFactory.getInstae().getAtendimentoDao();
	private IHospitalDao hospitalDao = DaoFactory.getInstae().getHospitalDao();

	public Paciente buscarPaciente(String cpf) throws ControleException {

		try {
			Paciente paciente = pacienteDao.buscarPorCpf(cpf);
			if (paciente != null) {
				verificarTemAtendimento(atendimentoDao, cpf);
			}
			return paciente;
		} catch (PersistenciaException e) {
			e.printStackTrace();
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public String gravarNovoAtendimento(Atendimento atendiemnto) throws ControleException {

		try {
			validarAtendimentoBean(atendiemnto);
			verificarTemAtendimento(atendimentoDao, atendiemnto.getCpf());

			CriacaoAtendimentoRetornoVo vo = atendimentoDao.criarAtendimento(atendiemnto);

			if (vo.getInInternado())
				return "Paciente internado na ala " + vo.getAla();
			if (vo.getIndFilaInternacao())
				return "Paciente adicionado na fila da ala " + vo.getAla() + " \nPosiçãoo na fila da ala: "
						+ vo.getPosicaoFilaAla() + " \nPosição na fila da enfermagem: " + vo.getPosicaoFilaEnfermagem();

			return "Paciente adicionado na fina de atendimento (tamanho da fila " + atendiemnto.getPrioridade() + ": "
					+ vo.getPosicaoFilaAtendimento();
		} catch (ControleException e) {
			throw e;
		} catch (SemVagaExeception e) {
			throw new ControleException(
					"Não temos vagas para internação na enfermaria. \nPor favor procure outro hospital", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControleException("Erro ao salvar paciente: \n" + e.getMessage(), e);
		} finally {
			resturarHospital();
		}
	}

	public List<AtendimentoFilaVo> getAtendimentosPorFinalizar() throws ControleException {

		try {
			List<AtendimentoFilaVo> lstVo = new ArrayList<AtendimentoFilaVo>();

			getAtendimentosPorFinalizarAlas(lstVo);
			getAtendimentosPorFinalizarEnferemagem(lstVo);

			return lstVo;
		} catch (PersistenciaException e) {
			resturarHospital();
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public List<AtendimentoFilaVo> getPacientesInterados() throws ControleException {

		try {
			List<AtendimentoFilaVo> lstVo = new ArrayList<AtendimentoFilaVo>();

			for (AlaHospial ala : getHospital().getAlas().values()) {
				for (int i = 0; i < ala.getLeitos().size(); i++) {
					Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(ala.getLeitos().get(i));
					if (atendimento == null)
						throw new ControleException("Nenhum atendimento encontrado");
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
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public void finalizarAtendimento(Atendimento atendimento) throws ControleException {

		if (StringUtils.isBlank(atendimento.getCpf()))
			throw new ControleException("Informe todos os dados do paciente");
		try {
			getHospital().getFilaEmfermagem().remove(atendimento.getCpf());
			for (AlaHospial halohospital : getHospital().getAlas().values()) {
				halohospital.getLeitos().remove(atendimento.getCpf());
				halohospital.getFilaEspera().remove(atendimento.getCpf());
			}
			atendimento.setDataSaida(new Date());
			atendimentoDao.salvarAtendimento(atendimento);
			hospitalDao.salvarHospital(getHospital());
		} catch (PersistenciaException e) {
			resturarHospital();
			e.printStackTrace();
			throw new ControleException("Erro ao finalizar o atendimento: " + e.getMessage());
		}
	}

	private void getAtendimentosPorFinalizarAlas(List<AtendimentoFilaVo> lstVo) throws PersistenciaException {

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
	}

	private void getAtendimentosPorFinalizarEnferemagem(List<AtendimentoFilaVo> lstVo) throws PersistenciaException {

		for (String cpf : getHospital().getFilaEmfermagem()) {
			Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(cpf);
			AtendimentoFilaVo vo = new AtendimentoFilaVo(atendimento, "Fila Enfermagem");
			lstVo.add(vo);
		}
	}
}
