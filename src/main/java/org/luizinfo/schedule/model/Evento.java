package org.luizinfo.schedule.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "seq_evento", sequenceName = "seq_evento", initialValue = 1, allocationSize = 1)
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_evento")
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false)
	private boolean diaInteiro;

	@Column(nullable = false)
	private String inicio;

	private String fim;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "processo_automatico_id")
	private ProcessoAutomatico processoAutomatico;

	public Evento() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public boolean isDiaInteiro() {
		return diaInteiro;
	}

	public void setDiaInteiro(boolean diaInteiro) {
		this.diaInteiro = diaInteiro;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ProcessoAutomatico getProcessoAutomatico() {
		return processoAutomatico;
	}

	public void setProcessoAutomatico(ProcessoAutomatico processoAutomatico) {
		this.processoAutomatico = processoAutomatico;
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
		Evento other = (Evento) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Evento [id=" + id + ", titulo=" + titulo + ", diaInteiro=" + diaInteiro + ", inicio=" + inicio
				+ ", fim=" + fim + ", cliente=" + cliente + ", processoAutomatico=" + processoAutomatico + "]";
	}

}
