package org.luizinfo.schedule.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document("Cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME = "cliente_seq";

	@Id
	private Long id;

	private String nome;

	private String apelido;

	private String endereco;

	private Double valor;

    @Field(name = "forma_pagamento")
	private FormaPagamento formaPagamento;

    @Field(name = "dia_semana")
	private DiaSemana diaSemana;

    @Field(name = "frequencia")
	private Frequencia frequencia;

    @Field(name = "tipo_limpeza")
	private String tipoLimpeza;

    @Field(name = "hora_inicio")
	private String horaInicio;

    @Field(name = "hora_fim")
	private String horaFim;

	private String observacao;

	private boolean ativo;

	private String telefone;

	@JsonIgnore
	private List<Evento> eventos;

	public Cliente() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Frequencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

	public String getTipoLimpeza() {
		return tipoLimpeza;
	}

	public void setTipoLimpeza(String tipoLimpeza) {
		this.tipoLimpeza = tipoLimpeza;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", apelido=" + apelido + ", endereco=" + endereco + ", valor="
				+ valor + ", formaPagamento=" + formaPagamento + ", diaSemana=" + diaSemana + ", frequencia="
				+ frequencia + ", tipoLimpeza=" + tipoLimpeza + ", horaInicio=" + horaInicio + ", horaFim=" + horaFim
				+ ", observacao=" + observacao + ", ativo=" + ativo + ", telefone=" + telefone + ", eventos=" + eventos
				+ "]";
	}

}
