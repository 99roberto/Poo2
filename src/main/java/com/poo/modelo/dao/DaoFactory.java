package com.poo.modelo.dao;

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

	public HospitalDao getHospitalDao() {
		if (hospitalDao == null)
			hospitalDao = new HospitalDao();
		return hospitalDao;
	}

	public AtendimentoDao getAtendimentoDao() {
		if (atendimentoDao == null)
			atendimentoDao = new AtendimentoDao();
		return atendimentoDao;
	}

	public ConsultaDao getConsultaDao() {
		if (consultaDao == null)
			consultaDao = new ConsultaDao();
		return consultaDao;
	}

	public PacienteDao getPacienteDao() {
		if (pacienteDao == null)
			pacienteDao = new PacienteDao();
		return pacienteDao;
	}

}
