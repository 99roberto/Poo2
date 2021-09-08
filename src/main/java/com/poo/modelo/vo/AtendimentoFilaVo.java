package com.poo.modelo.vo;

import com.poo.modelo.Atendimento;

public class AtendimentoFilaVo {

	private Atendimento atendimento;
	private String descSituacaoAtendimento;

	public AtendimentoFilaVo(Atendimento atendimento, String situacao) {
		super();
		this.atendimento = atendimento;
		this.descSituacaoAtendimento = situacao;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public String getDescSituacaoAtendimento() {
		return descSituacaoAtendimento;
	}

	public void setDescSituacaoAtendimento(String situacao) {
		this.descSituacaoAtendimento = situacao;
	}
}
