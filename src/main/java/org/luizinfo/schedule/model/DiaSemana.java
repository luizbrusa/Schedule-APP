package org.luizinfo.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiaSemana {

	SUNDAY ("Domingo"),
	MONDAY ("Segunda-Feira"),
	TUESDAY ("Terça-Feira"),
	WEDNESDAY ("Quarta-Feira"),
	THURSDAY ("Quinta-Feira"),
	FRIDAY ("Sexta-Feira"),
	SATURDAY ("Sábado");

    private String descricao;
    
    private DiaSemana(String descricao) {
        this.descricao = descricao;
	}
    
    public String getValue() {
		return this.name();
	}

    public String getDescricao() {
		return descricao;
	}
}
