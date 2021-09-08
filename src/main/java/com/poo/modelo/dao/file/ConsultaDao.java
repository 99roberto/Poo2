package com.poo.modelo.dao.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.poo.modelo.Consulta;
import com.poo.modelo.dao.IConsultaDao;
import com.poo.modelo.dao.PersistenciaException;

public class ConsultaDao extends AbstractDao<Integer, Consulta> implements IConsultaDao {

	@Override
	protected String getNomeArq() {
		return "consulta.txt";
	}

	@Override
	public List<Consulta> buscarPorCpf(String cpf) throws PersistenciaException {

		try {
			HashMap<Integer, Consulta> map = lerArquivoBinario();

			List<Consulta> lst = new ArrayList<Consulta>();
			for (Consulta atd : map.values()) {
				if (atd.getCpf() != null && atd.getCpf().equals(cpf))
					lst.add(atd);
			}
			Collections.sort(lst);
			return lst;
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	@Override
	public void salvarConsulta(Consulta consulta) throws PersistenciaException {

		try {
			HashMap<Integer, Consulta> map = lerArquivoBinario();
			map.put(map.size(), consulta);
			gravarArquivoBinario(map);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	@Override
	protected Class<Consulta> getValueClass() {

		return Consulta.class;
	}

	@Override
	protected Class<Integer> getKeyClass() {
		return Integer.class;
	}

}
