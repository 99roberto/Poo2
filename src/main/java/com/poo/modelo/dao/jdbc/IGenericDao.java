package com.poo.modelo.dao.jdbc;

import java.util.List;

public interface IGenericDao<E, P> {

	public E find(P pk) throws Exception;

	public List<E> all() throws Exception;

	public void create(E entity) throws Exception;

}
