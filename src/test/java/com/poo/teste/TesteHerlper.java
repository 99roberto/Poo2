package com.poo.teste;

import java.io.File;
import java.util.Date;

import com.poo.controle.AtendimentoControle;
import com.poo.controle.ControleException;
import com.poo.modelo.Atendimento;
import com.poo.modelo.Consulta;
import com.poo.modelo.EnumAlaHospital;
import com.poo.modelo.Hospital;
import com.poo.modelo.Paciente;
import com.poo.modelo.dao.PersistenciaException;
import com.poo.modelo.dao.file.HospitalDao;
import com.poo.modelo.dao.file.PacienteDao;

public class TesteHerlper {

	public void iniciaBase() throws Exception {
		delete("atendimentos.txt");
		delete("consulta.txt");
		delete("hospital.txt");
		delete("pacientes.txt");

		seed(5);

	}

	private void delete(String file) {
		File f = new File(file);
		f.deleteOnExit();
		f.delete();
	}

	public static Atendimento newAtendimento(String cpf) {
		return newAtendimento(cpf, new Date());
	}

	public static Atendimento newAtendimento(String cpf, Date dtaEntrada) {
		Atendimento at = new Atendimento();
		at.setCpf(cpf);
		at.setNome("Alcemar Illuminati");
		at.setDataEntrada(dtaEntrada);
		at.setChave(null);
		at.setQueixa("Queixa do paciente");
		at.setPrioridade(1);
		at.setAla(EnumAlaHospital.PEDIATRICA);
		at.setObservacao("Minha obs.");
		at.setDataSaida(null);
		return at;
	}

	public static void seed(int qtd) throws PersistenciaException, InterruptedException, ControleException {
		PacienteDao pDao = new PacienteDao();
		AtendimentoControle ctr = new AtendimentoControle();
		HospitalDao hDao = new HospitalDao();
		Hospital h = new Hospital();
		hDao.salvarHospital(h);
		int nivel = 3;
		for (int i = 0; i < qtd; i++) {
			Paciente p = new Paciente();
			p.setCPF("0000000000" + i);
			p.setNome("Paciente " + i);

			pDao.salvarPaciente(p);
			if (i % 2 == 0)
				continue;
			Atendimento a = new Atendimento();
			a.setDataEntrada(new Date());
			a.setCpf(p.getCPF());
			a.setNome(p.getNome());
			a.setQueixa("Queixa do paciente " + i);
			a.setPrioridade(nivel++);
			a.setAla(i % 2 == 0 ? EnumAlaHospital.CARDIOLOGIA : EnumAlaHospital.NEUROLOGIA);
			if (nivel > 5)
				nivel = 3;
			ctr.gravarNovoAtendimento(a);

		}

	}

	public static Consulta newConsulta(Date dataTermino, String cpf) {
		Consulta c = new Consulta();
		c.setCpf(cpf);
		c.setNome("Paciente");
		c.setQueixa("Queixa do paciente ");
		c.setAvaliacao("Avalia????o");
		c.setPrescricao("prescri????o");
		c.setAla(EnumAlaHospital.PEDIATRICA);
		c.setObservacao("obs");
		c.setDataTermino(dataTermino);

		return c;
	}

	public static Paciente newPaciente(String cpf) {
		Paciente p = new Paciente();
		p.setCPF(cpf);
		p.setNome("Paciente ");

		return p;
	}

	public static Consulta newConsulta(Atendimento at) {
		Consulta consulta = new Consulta();
		consulta.setNome(at.getNome());
		consulta.setCpf(at.getCpf());
		consulta.setQueixa(at.getQueixa());
		consulta.setAvaliacao("Avalia????o ");
		consulta.setPrescricao("Pre3scri????o");
		consulta.setAla(at.getAla());
		consulta.setObservacao(at.getObservacao());

		return consulta;
	}
}
