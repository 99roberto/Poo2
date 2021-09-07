package com.poo.modelo.vo;

import com.poo.modelo.Atendimento;
import com.poo.modelo.EnumAlaHospital;

public class CriarAtenimentoRetornoVo {
	private Atendimento at;
	private Boolean inInternado;
	private Boolean indFilaInternacao;
	private Boolean indSemVaga;
	private EnumAlaHospital ala;
	private Integer posicaoFilaAla;
	private Integer posicaoFilaEnfermagem;
	private Integer posicaoFilaAtendimento;

	public Atendimento getAt() {
		return at;
	}

	public void setAt(Atendimento at) {
		this.at = at;
	}

	public Boolean getInInternado() {
		return inInternado;
	}

	public void setInInternado(Boolean inInternado) {
		this.inInternado = inInternado;
	}

	public Boolean getIndSemVaga() {
		return indSemVaga;
	}

	public void setIndSemVaga(Boolean indSemVaga) {
		this.indSemVaga = indSemVaga;
	}

	public EnumAlaHospital getAla() {
		return ala;
	}

	public void setAla(EnumAlaHospital ala) {
		this.ala = ala;
	}

	public Integer getPosicaoFilaAla() {
		return posicaoFilaAla;
	}

	public void setPosicaoFilaAla(Integer posicaoFilaAla) {
		this.posicaoFilaAla = posicaoFilaAla;
	}

	public Integer getPosicaoFilaEnfermagem() {
		return posicaoFilaEnfermagem;
	}

	public void setPosicaoFilaEnfermagem(Integer posicaoFilaEnfermagem) {
		this.posicaoFilaEnfermagem = posicaoFilaEnfermagem;
	}

	public Integer getPosicaoFilaAtendimento() {
		return posicaoFilaAtendimento;
	}

	public void setPosicaoFilaAtendimento(Integer posicaoFiçlaAtendimento) {
		this.posicaoFilaAtendimento = posicaoFiçlaAtendimento;
	}

	public Boolean getIndFilaInternacao() {
		return indFilaInternacao;
	}

	public void setIndFilaInternacao(Boolean indFilaInternacao) {
		this.indFilaInternacao = indFilaInternacao;
	}

}
