package com.poo.modelo.dao;

import com.poo.modelo.Hospital;

public interface IHospitalDao {

	public Hospital get() throws PersistenciaException;

	public void salvarHospital(Hospital hospital) throws PersistenciaException;

}
