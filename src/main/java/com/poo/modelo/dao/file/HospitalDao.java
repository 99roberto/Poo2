package com.poo.modelo.dao.file;

import java.util.HashMap;

import com.poo.modelo.Hospital;
import com.poo.modelo.dao.IHospitalDao;
import com.poo.modelo.dao.PersistenciaException;

public class HospitalDao extends AbstractDao<Integer, Hospital> implements IHospitalDao {

	@Override
	protected String getNomeArq() {
		return "hospital.txt";
	}

	@Override
	public Hospital get() throws PersistenciaException {
		try {
			HashMap<Integer, Hospital> map = lerArquivoBinario();
			return map.get(0);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	@Override
	public void salvarHospital(Hospital hospital) throws PersistenciaException {
		try {
			HashMap<Integer, Hospital> map = lerArquivoBinario();
			map.remove(0);
			map.put(0, hospital);
			gravarArquivoBinario(map);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao buscar paciente", e);
		}
	}

	@Override
	protected Class<Hospital> getValueClass() {
		return Hospital.class;
	}

	@Override
	protected Class<Integer> getKeyClass() {
		return Integer.class;
	}

}
