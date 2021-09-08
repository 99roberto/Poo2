package com.poo.modelo.dao;

import com.poo.modelo.dao.file.AtendimentoDao;
import com.poo.modelo.dao.file.ConsultaDao;
import com.poo.modelo.dao.file.HospitalDao;
import com.poo.modelo.dao.file.PacienteDao;

public class DaoFactory {

	private static DaoFactory instace;
	private HospitalDao hospitalDao;
	private AtendimentoDao atendimentoDao;
	private ConsultaDao consultaDao;
	private PacienteDao pacienteDao;

	public static DaoFactory getInstae() {

		if (instace == null)
			instace = new DaoFactory();
		return instace;
	}

	public IHospitalDao getHospitalDao() {

		if (hospitalDao == null)
			hospitalDao = new HospitalDao();
		return hospitalDao;
	}

	public IAtendimentoDao getAtendimentoDao() {

		if (atendimentoDao == null)
			atendimentoDao = new AtendimentoDao();
		return atendimentoDao;
	}

	public IConsultaDao getConsultaDao() {

		if (consultaDao == null)
			consultaDao = new ConsultaDao();
		return consultaDao;
	}

	public IPacienteDao getPacienteDao() {

		if (pacienteDao == null)
			pacienteDao = new PacienteDao();
		return pacienteDao;
	}
}
