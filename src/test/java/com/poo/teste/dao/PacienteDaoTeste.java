package com.poo.teste.dao;

import static com.poo.teste.TesteHerlper.newPaciente;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.poo.modelo.Paciente;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IPacienteDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.teste.TesteHerlper;

public class PacienteDaoTeste {

	private IPacienteDao dao;
	SimpleDateFormat sd = new SimpleDateFormat("dd/MM/YYYY");

	@BeforeEach
	public void beforeEach() throws Exception {
		new TesteHerlper().iniciaBase();
		this.dao = DaoFactory.getInstae().getPacienteDao();
	}

	@Test
	public void salvarAndBuscarPorCpf() throws PersistenciaException {
		Paciente p = newPaciente("467482510524");
		dao.salvarPaciente(p);
		Paciente p2 = dao.buscarPorCpf(p.getCPF());
		assertNotNull(p2);
	}
}
