package com.poo.teste.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.poo.teste.TesteHerlper.newConsulta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import static org.junit.jupiter.api.Assertions.*;

import com.poo.modelo.Consulta;
import com.poo.modelo.dao.ConsultaDao;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.teste.TesteHerlper;

public class ConsultaDaoTeste {
	private ConsultaDao dao;
	SimpleDateFormat sd = new SimpleDateFormat("dd/MM/YYYY");

	@BeforeEach
	public void beforeEach() throws Exception {
		new TesteHerlper().iniciaBase();
		this.dao = DaoFactory.getInstae().getConsultaDao();
	}

	@Test
	public void salvarAndBuscarPorCpf() throws PersistenciaException {
		Consulta c = newConsulta(new Date(), "00000000001");
		dao.salva(c);
		List<Consulta> c2 = dao.buscarPorCpf(c.getCpf());
		assertNotNull(c2);
		assertEquals(c2.size(), 1);
	}

	@Test
	public void naoSalvarSemPaciente() throws PersistenciaException {
		Consulta c = newConsulta(new Date(), "99999999999");
		Exception ex = null;
		try {
			dao.salva(c);
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(0, dao.buscarPorCpf(c.getCpf()));
	}
}
