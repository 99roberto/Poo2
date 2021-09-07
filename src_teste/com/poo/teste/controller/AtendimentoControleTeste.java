package com.poo.teste.controller;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.poo.controle.AtendimentoControle;
import com.poo.controle.ControleExcption;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Paciente;
import com.poo.modelo.vo.AtendimentoFilaVo;
import com.poo.teste.TesteHerlper;

public class AtendimentoControleTeste {

	private AtendimentoControle ctrl;

	@Before
	public void before() throws Exception {
		this.ctrl = new AtendimentoControle();
		new TesteHerlper().iniciaBase();
	}

	@Test
	public void buscarPaciente() throws Exception {
		Paciente p = ctrl.buscarPaciente("00000000002");
		assertNotNull(p);
		assertEquals("00000000002", p.getCPF());
	}

	@Test
	public void buscarPacienteQueJaTemAtendimento() throws Exception {
		ControleExcption ex = null;
		try {
			ctrl.buscarPaciente("00000000001");
		} catch (ControleExcption e) {
			ex = e;
		}
		assertNotNull(ex);

	}

	@Test
	public void gravarAtendimento() throws Exception {

		ControleExcption ex = null;
		try {
			Atendimento at = TesteHerlper.newAtendimento("00000000001");
			ctrl.gravar(at);
		} catch (ControleExcption e) {
			ex = e;
		}
		assertNotNull(ex);

		Atendimento at = TesteHerlper.newAtendimento("00000000002");
		assertNotNull(at);
	}

	@Test
	public void getAtendimentosPorFinalizar() throws Exception {
		// getAtendimentosPorFinalizar
		List<AtendimentoFilaVo> lst = ctrl.getAtendimentosPorFinalizar();
		assertNotNull(lst);
		assertEquals(0, lst.size());

		Atendimento at = TesteHerlper.newAtendimento("46748251052");
		at.setDataSaida(null);
		at.setPrioridade(5);
		ctrl.gravar(at);

		lst = ctrl.getAtendimentosPorFinalizar();
		assertNotNull(lst);
		assertEquals(1, lst.size());

	}

	@Test
	public void getPacientesInterados() throws Exception {
		// List<AtendimentoFilaVo> getPacientesInterados() throws ControleExcption {

		List<AtendimentoFilaVo> lst = ctrl.getPacientesInterados();
		assertNotNull(lst);
		assertEquals(0, lst.size());

		Atendimento at = TesteHerlper.newAtendimento("46748251052");
		at.setDataSaida(null);
		at.setPrioridade(5);
		ctrl.gravar(at);

		lst = ctrl.getPacientesInterados();
		assertNotNull(lst);
		assertEquals(1, lst.size());
	}

	public void finalizarAtendimento() throws Exception {
		Atendimento at = TesteHerlper.newAtendimento("46748251052");
		at.setDataSaida(null);
		at.setPrioridade(5);
		ctrl.gravar(at);

		List<AtendimentoFilaVo> lst = ctrl.getPacientesInterados();
		assertNotNull(lst);
		assertEquals(1, lst.size());
		ctrl.finalizarAtendimento(at);

		//
		lst = ctrl.getPacientesInterados();
		assertNotNull(lst);
		assertEquals(0, lst.size());
	}

}
