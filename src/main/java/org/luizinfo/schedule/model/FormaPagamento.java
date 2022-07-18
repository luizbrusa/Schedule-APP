package org.luizinfo.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FormaPagamento {

	CASH ("Dinheiro"),
	CHECK ("Cheque"),
	TRANSFER ("Transferência Bancária");
	
	private String descricao;
	
	private FormaPagamento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getValue() {
		return this.name();
	}
}
