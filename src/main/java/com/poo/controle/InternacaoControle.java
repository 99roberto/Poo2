package com.poo.controle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.poo.modelo.AlaHospial;
import com.poo.modelo.Atendimento;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.IHospitalDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.vo.InternacaoVo;

public class InternacaoControle extends HospitalControle {

	private IAtendimentoDao atendimentoDao = DaoFactory.getInstae().getAtendimentoDao();
	private IHospitalDao hospitalDao = DaoFactory.getInstae().getHospitalDao();

	public List<InternacaoVo> getFila() throws ControleException {

		try {
			List<InternacaoVo> lstVo = new ArrayList<InternacaoVo>();

			for (AlaHospial ala : getHospital().getAlas().values()) {
				for (int i = 0; i < ala.getFilaEspera().size(); i++) {
					Atendimento atendimento = atendimentoDao.buscarAtendimentoAberto(ala.getFilaEspera().get(i));
					if (atendimento == null)
						throw new ControleException("Nenhum atendimento encontrado");
					InternacaoVo vo = new InternacaoVo(atendimento, i + 1);
					vo.setLeitos(ala.getQtdleitos());
					vo.setLeitosVagos(ala.getQtdleitos() - ala.getLeitos().size());
					lstVo.add(vo);
				}
			}

			return lstVo;
		} catch (PersistenciaException e) {
			resturarHospital();
			e.printStackTrace();
			throw new ControleException("Erro ao buscar paciente: \n" + e.getMessage(), e);
		}
	}

	public void internar(Atendimento atendimento) throws ControleException {

		try {
			if (StringUtils.isBlank(atendimento.getCpf()))
				throw new ControleException("Informe todos os dados do paciente");

			getHospital().getFilaAtendimento().remove(atendimento.getCpf());
			AlaHospial ala = getHospital().getAlas().get(atendimento.getAla());
			if (!ala.temVaga()) {
				throw new ControleException("Não há vagas na ala " + ala.getAla());
			}
			ala.internar(atendimento.getCpf());
			getHospital().getFilaEmfermagem().remove(atendimento.getCpf());

			hospitalDao.salvarHospital(getHospital());
		} catch (ControleException e) {
			throw e;
		} catch (Exception e) {
			resturarHospital();
			throw new ControleException("Erro ao gerar internação: " + e.getCause());
		}
	}

}
