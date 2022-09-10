package org.luizinfo.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiaSemana {

	D1_SUNDAY ("Domingo"),
	D2_MONDAY ("Segunda-Feira"),
	D3_TUESDAY ("Terça-Feira"),
	D4_WEDNESDAY ("Quarta-Feira"),
	D5_THURSDAY ("Quinta-Feira"),
	D6_FRIDAY ("Sexta-Feira"),
	D7_SATURDAY ("Sábado");

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
