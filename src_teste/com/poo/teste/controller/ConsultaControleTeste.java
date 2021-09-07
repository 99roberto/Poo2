package com.poo.teste.controller;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.poo.controle.ConsultaControle;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Consulta;
import com.poo.modelo.dao.ConsultaDao;
import com.poo.teste.TesteHerlper;

public class ConsultaControleTeste {

	private ConsultaControle ctrl;
	private ConsultaDao dao;

	@Before
	public void before() throws Exception {
		this.ctrl = new ConsultaControle();
		this.dao = new ConsultaDao();
		new TesteHerlper().iniciaBase();
	}
	
	@Test
	public void proximoAtendimento() throws Exception {
		Atendimento at = ctrl.proximoAtendimento();
		assertNotNull(at);

	}

	@Test
	public void internar() throws Exception {
		Atendimento at = ctrl.proximoAtendimento();
		assertNotNull(at);
		Consulta c = TesteHerlper.newConsulta(at);
		
		//
		assertNotNull(dao.buscarPorCpf(c.getCpf()));
		//
		ctrl.finalizaConsulta(c);
		List<Consulta> ax = dao.buscarPorCpf(c.getCpf());
		assertNotNull(ax.get(0).getDataTermino());
	}


}
