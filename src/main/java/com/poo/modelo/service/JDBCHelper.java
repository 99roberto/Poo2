package com.poo.modelo.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCHelper {
	private static String DRIVER = null;
	private static String URL = null;
	private static String USERNAME = null;
	private static String PASSWORD = null;
	private static Connection connection;

	static {

		System.out.println("lendo configurações...");
		try (InputStream stream = new FileInputStream("config.properties")) {

			Properties prop = new Properties();
			prop.load(stream);
			DRIVER = prop.getProperty("jdbc.DRIVER", "org.apache.derby.jdbc.EmbeddedDriver");
			URL = prop.getProperty("jdbc.URL", "jdbc:derby:escoladb;create=true");
			USERNAME = prop.getProperty("jdbc.USERNAME", "app");
			PASSWORD = prop.getProperty("jdbc.PASSWORD", "app");

			Class.forName(DRIVER);
			System.out.println("obtendo conexão...");
			getConnection();
			System.out.println("Criando base...");
			criarBase();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		return connection;
	}

	public static void closeConnection(Connection con) throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	public static void closePrepaerdStatement(PreparedStatement stmt) throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
	}

	public static void closeResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}

	static boolean tableExists(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });

		return resultSet.next();
	}

	private static void criarBase() throws SQLException {
		try (Connection conn = getConnection(); Statement stmt = conn.createStatement();) {
			if (!tableExists(conn, "PESSOA")) {
				stmt.execute("CREATE SEQUENCE SQ_PESSOA_ID AS BIGINT START WITH 1 ");
				
				String sql = "CREATE TABLE PESSOA " + //
						"(id INTEGER not NULL  , " + //
						" NOME VARCHAR(255), " + //
						" IDADE INTEGER, " + //
						" PRIMARY KEY ( id ))";
				stmt.executeUpdate(sql);
			}

			if (!tableExists(conn, "ESCOLA")) {
				System.out.println("Criando tabela ESCOLA");
				String sql = "CREATE TABLE ESCOLA " + //
						"(ID INTEGER not NULL  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " + //
						" NOME VARCHAR(255), " + //
						" PRIMARY KEY ( id ))";
				stmt.executeUpdate(sql);
			}
			if (!tableExists(conn, "DISCIPLINA")) {
				System.out.println("Criando tabela DISCIPLINA");
				String sql = "CREATE TABLE DISCIPLINA " + //
						"( NOME VARCHAR(255) not NULL, " + //
						" PRIMARY KEY ( NOME ))";
				stmt.executeUpdate(sql);
			}
			if (!tableExists(conn, "ESTUDANTE")) {
				System.out.println("Criando tabela ESTUDANTE");
				String sql = "CREATE TABLE ESTUDANTE "//
						+ "(ID INTEGER not NULL  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "//
						+ " ID_PESSOA INTEGER not NULL, " //
						+ " ID_ESCOLA INTEGER not NULL, " //
						+ " ANO_ESCOLAR INTEGER, " //
						+ " PRIMARY KEY ( id )," //
						+ " FOREIGN KEY (ID_PESSOA) REFERENCES PESSOA(ID),"//
						+ " FOREIGN KEY (ID_ESCOLA) REFERENCES ESCOLA(ID)" //
						+ ")";
				stmt.executeUpdate(sql);
			}

			if (!tableExists(conn, "DIRETOR")) {
				System.out.println("Criando tabela DIRETOR");
				String sql = "CREATE TABLE DIRETOR " //
						+ "(ID INTEGER not NULL  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " //
						+ " ID_PESSOA INTEGER not NULL, " //
						+ " ID_ESCOLA INTEGER not NULL, " //
						+ " PRIMARY KEY ( id )," //
						+ " FOREIGN KEY (ID_PESSOA) REFERENCES PESSOA(ID),"//
						+ " FOREIGN KEY (ID_ESCOLA) REFERENCES ESCOLA(ID)" //
						+ ")";
				stmt.executeUpdate(sql);
			}

			if (!tableExists(conn, "PROFESSOR")) {
				System.out.println("Criando tabela PROFESSOR");
				String sql = "CREATE TABLE PROFESSOR " //
						+ "(ID INTEGER not NULL  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " //
						+ " ID_PESSOA INTEGER not NULL, " //
						+ " ID_ESCOLA INTEGER not NULL, " //
						+ " DISCIPLINA VARCHAR(255), " //
						+ " PRIMARY KEY ( id )," //
						+ " FOREIGN KEY (ID_PESSOA) REFERENCES PESSOA(ID),"//
						+ " FOREIGN KEY (ID_ESCOLA) REFERENCES ESCOLA(ID)," //
						+ " FOREIGN KEY (DISCIPLINA) REFERENCES DISCIPLINA(NOME)" //
						+ ")";
				stmt.executeUpdate(sql);
			}

			System.out.println("Base criada");
		}
	}

	public static void closeConnection() {
		try {
			DriverManager.getConnection(URL + ";shutdown=true", USERNAME, PASSWORD);
		} catch (SQLException e) {
		}
	}
}