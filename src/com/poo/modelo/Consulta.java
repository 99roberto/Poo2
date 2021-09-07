package com.poo.modelo;

import java.io.Serializable;
import java.util.Date;

public class Consulta implements Serializable, Comparable<Consulta> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cpf;
	private String nome;

	// i) a descri��o do hist�rico de sa�de (queixa)
	private String queixa;
	// i) avalia��o do m�dico,
	private String avaliacao;
	// iii) medica��o prescrita
	private String prescricao;
	// iv)ala para interna��o e
	private EnumAlaHospital ala;
	// v)observa��o.
	private String Observacao;
	// data e hora do t�rmino
	private Date dataTermino;
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getQueixa() {
		return queixa;
	}

	public void setQueixa(String queixa) {
		this.queixa = queixa;
	}

	public String getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(String avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getPrescricao() {
		return prescricao;
	}

	public void setPrescricao(String prescricao) {
		this.prescricao = prescricao;
	}

	public EnumAlaHospital getAla() {
		return ala;
	}

	public void setAla(EnumAlaHospital ala) {
		this.ala = ala;
	}

	public String getObservacao() {
		return Observacao;
	}

	public void setObservacao(String observacao) {
		Observacao = observacao;
	}

	
	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	@Override
	public int compareTo(Consulta arg0) {
		if(getDataTermino()!=null && arg0.getDataTermino()!=null) {
			return getDataTermino().compareTo(arg0.getDataTermino());
		}
			
		return 0;
	}


}
