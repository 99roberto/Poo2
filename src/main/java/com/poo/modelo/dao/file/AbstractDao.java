package com.poo.modelo.dao.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class AbstractDao<T extends Serializable, E extends Serializable> {

	private ObjectMapper objectMapper = new ObjectMapper();

	protected abstract String getNomeArq();

	protected abstract Class<E> getValueClass();

	protected abstract Class<T> getKeyClass();

	/**
	 * serializado: gravando o objetos no arquivo binário "nomeArq"
	 * 
	 * @param lista
	 * @throws Exception
	 */
	protected void gravarArquivoBinario(HashMap<T, E> lista) throws Exception {

		File arq = new File(getNomeArq());
		try {
			if (!arq.exists())
				arq.createNewFile();
			OutputStream objOutput = new FileOutputStream(arq);
			objOutput.flush();
			objectMapper.writeValue(arq, lista);

			objOutput.close();
		} catch (Exception e) {
			System.out.printf("Erro: %s", e.getMessage());
			throw e;
		}
	}

	/**
	 * desserialização: recuperando os objetos gravados no arquivo binário "nomeArq"
	 * 
	 * @return
	 * @throws Exception
	 */
	protected HashMap<T, E> lerArquivoBinario() throws Exception {

		HashMap<T, E> lista = new HashMap<T, E>();
		FileInputStream objInput = null;
		try {
			File arq = new File(getNomeArq());
			if (arq.exists()) {
				objInput = new FileInputStream(arq);
				TypeFactory factory = TypeFactory.defaultInstance();
				MapType type = factory.constructMapType(HashMap.class, getKeyClass(), getValueClass());
				HashMap<T, E> obj = objectMapper.readValue(objInput, type);
				lista = obj;

			}
		} catch (Exception e) {
			System.out.printf("Erro: %s", e.getMessage());
			throw e;
		} finally {
			if (objInput != null)
				objInput.close();
		}

		return (lista);
	}

}