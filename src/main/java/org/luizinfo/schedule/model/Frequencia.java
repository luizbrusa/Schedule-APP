package org.luizinfo.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Frequencia {

	WEEKLY("Semanal"),
	FORTNIGHT("Quinzenal"),
	MONTHLY("Mensal");
	
	private String descricao;
	
	private Frequencia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getValue() {
		return this.name();
	}
}
