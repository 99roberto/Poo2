package com.poo.teste.dao;

import static com.poo.teste.TesteHerlper.newAtendimento;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.poo.modelo.Atendimento;
import com.poo.modelo.dao.DaoFactory;
import com.poo.modelo.dao.IAtendimentoDao;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.dao.SemVagaExeception;
import com.poo.teste.TesteHerlper;

public class AtendimentoDaoTeste {

	private IAtendimentoDao dao;
	SimpleDateFormat sd = new SimpleDateFormat("dd/MM/YYYY");

	@BeforeEach
	public void beforeEach() throws Exception {
		new TesteHerlper().iniciaBase();
		this.dao = DaoFactory.getInstae().getAtendimentoDao();
	}

	@Test
	public void criarNovoAtendimentoAndBuscarPorCpf() throws PersistenciaException, SemVagaExeception {
		Atendimento at = newAtendimento("46748251052");
		List<Atendimento> lst = dao.buscarPorCpf(at.getCpf());
		assertEquals(0, lst.size());
		dao.criarAtendimento(at);
		lst = dao.buscarPorCpf(at.getCpf());
		assertEquals(lst.size(), 1);
		Atendimento at2 = lst.get(0);
		//
		assertEquals(at.getNome(), at2.getNome());
		assertEquals(at.getDataEntrada(), at2.getDataEntrada());
		assertEquals(at.getQueixa(), at2.getQueixa());
		assertEquals(at.getPrioridade(), at2.getPrioridade());
		assertEquals(at.getAla(), at2.getAla());
		assertEquals(at.getObservacao(), at2.getObservacao());
		assertEquals(at.getDataSaida(), at2.getDataSaida());
		assertEquals(at.getChave(), at2.getChave());
	}

	@Test
	public void criar2AtendimentoAndBuscarPorCpf() throws PersistenciaException, ParseException {
		Atendimento at = newAtendimento("46748251052");
		at.setDataEntrada(sd.parse("01/01/2001"));
		at.setDataSaida(sd.parse("01/01/2001"));
		Atendimento at2 = newAtendimento("46748251052");
		List<Atendimento> lst = dao.buscarPorCpf(at.getCpf());
		assertEquals(lst.size(), 0);
		// salvando
		dao.salvarAtendimento(at);
		dao.salvarAtendimento(at2);
		// Buscando
		lst = dao.buscarPorCpf(at.getCpf());

		assertEquals(2, lst.size());
		at = lst.get(0);
		at2 = lst.get(1);
		//
		assertNotEquals(at.getChave(), at2.getChave());
	}

	@Test
	public void criarNovoAtendimentoAndBuscarPorEntrada() throws PersistenciaException, ParseException {
		Atendimento at = newAtendimento("00000000001");
		at.setDataEntrada(sd.parse("01/01/2001"));
		at.setDataSaida(sd.parse("01/01/2001"));
		Atendimento aux = dao.buscarPorEntrada(at.getCpf(), at.getDataEntrada());
		assertNull(aux);
		// salvando
		dao.salvarAtendimento(at);
		// buscando
		at = dao.buscarPorEntrada(at.getCpf(), at.getDataEntrada());
		assertNotNull(at);
	}

	@Test
	public void buscarAtendimentoAberto() throws PersistenciaException, ParseException {
		Atendimento at = newAtendimento("46748251052");
		at.setDataEntrada(sd.parse("01/01/2001"));

		assertNull(dao.buscarAtendimentoAberto(at.getCpf()));
		// salvando
		dao.salvarAtendimento(at);

		at = dao.buscarAtendimentoAberto(at.getCpf());
		// nulo pois atendimento fechado
		assertNotNull(at);
		assertNotNull(at.getChave());

		// fechando atendimento
		at.setDataSaida(sd.parse("01/01/2001"));
		dao.salvarAtendimento(at);

		assertNull(dao.buscarAtendimentoAberto(at.getCpf()));
	}

	@Test
	public void naoCiarAtendimentSemPaciente() throws PersistenciaException {
		// cpf sem registro
		Atendimento at = newAtendimento("999999999");

		assertEquals(0, dao.buscarPorCpf(at.getCpf()).size());
		Exception ex = null;
		try {
			dao.salvarAtendimento(at);
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(0, dao.buscarPorCpf(at.getCpf()).size());
	}

}
