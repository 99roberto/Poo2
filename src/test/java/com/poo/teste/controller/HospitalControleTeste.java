package com.poo.teste.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.poo.controle.HospitalControle;
import com.poo.modelo.AlaHospial;
import com.poo.teste.TesteHerlper;

public class HospitalControleTeste {

	private HospitalControle ctrl;

	@BeforeEach
	public void before() throws Exception {
		this.ctrl = new HospitalControle();
		new TesteHerlper().iniciaBase();
	}

	@Test
	public void getHospital() {
		assertNotNull(ctrl.getHospital());
	}

	@Test
	public void getLotacoesAlas() {
		Collection<AlaHospial> lst = ctrl.getLotacoesAlas();
		assertNotNull(lst);
		assertEquals(4, lst.size());
	}

}
