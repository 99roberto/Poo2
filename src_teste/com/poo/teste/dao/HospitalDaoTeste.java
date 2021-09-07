package com.poo.teste.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.poo.modelo.Hospital;
import com.poo.modelo.dao.HospitalDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.teste.TesteHerlper;

public class HospitalDaoTeste {

	private HospitalDao dao;

	@Before
	public void befor() throws Exception {
		new TesteHerlper().iniciaBase();
		this.dao = new HospitalDao();

	}

	@Test
	public void criarNovoHospitalEBuscar() throws PersistenciaException {
		Hospital hospital = new Hospital();
		PersistenciaException ex = null;
		
			dao.salva(hospital);
			Hospital h = dao.get();
			assertNotNull(h.getAlaCardiologia());
			assertNotNull(h.getAlaNeurologia());
			assertNotNull(h.getAlaPneumologia());
			assertNotNull(h.getAlaPediatrica());
	}
	
}
