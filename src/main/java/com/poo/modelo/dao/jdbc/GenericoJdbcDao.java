package com.poo.modelo.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poo.modelo.service.JDBCHelper;

public abstract class GenericoJdbcDao<E> {

	public abstract String getTablename();

	protected abstract List<E> map(ResultSet rs, List<E> lst) throws Exception;

	public List<E> all() throws Exception {
		List<E> lst = fildByQuery("select * from  " + getTablename());

		return lst;
	}

	protected void executeUpdate(String insertSql, Object... parametros) throws SQLException {
		System.out.println(insertSql);
		try (PreparedStatement pstmt = JDBCHelper.getConnection().prepareStatement(insertSql)) {
			for (int i = 0; i < parametros.length; i++) {
				pstmt.setObject(i + 1, parametros[i]);
			}
			pstmt.execute();
		}
	}

	protected List<E> fildByQuery(String insertSql, Object... parametros) throws Exception {
		System.out.println(insertSql);
		try (PreparedStatement pstmt = JDBCHelper.getConnection().prepareStatement(insertSql)) {
			for (int i = 0; i < parametros.length; i++) {
				pstmt.setObject(i + 1, parametros[i]);
			}
			List<E> lst = new ArrayList<E>();
			ResultSet rs = pstmt.executeQuery();

			return map(rs, lst);
		}
	}

}