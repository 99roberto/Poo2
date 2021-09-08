package com.poo.teste.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.poo.modelo.Hospital;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IHospitalDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.teste.TesteHerlper;

public class HospitalDaoTeste {

	private IHospitalDao dao;

	@BeforeEach
	public void befor() throws Exception {
		new TesteHerlper().iniciaBase();
		this.dao = DaoFactory.getInstae().getHospitalDao();

	}

	@Test
	public void criarNovoHospitalEBuscar() throws PersistenciaException {
		Hospital hospital = new Hospital();
		PersistenciaException ex = null;

		dao.salvarHospital(hospital);
		Hospital h = dao.get();
		assertNotNull(h.getAlaCardiologia());
		assertNotNull(h.getAlaNeurologia());
		assertNotNull(h.getAlaPneumologia());
		assertNotNull(h.getAlaPediatrica());
	}

}
