package org.luizinfo.schedule.repository;

import java.util.Date;
import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;

public interface ICustomEvento {

	public Evento findUltimoEventoCliente(Cliente cliente, Date data);

	public List<Evento> findEventosFuturosCliente(Cliente cliente, Date dataInicial);

	public boolean existeEventoAutomaticoClienteData(Cliente cliente, int ano, int mes, int dia);

	public boolean existeEventoAutomaticoClienteMes(Cliente cliente, int ano, int mes);

	public boolean existeEventoAutomaticoClienteQuinzena(Cliente cliente, int ano, int mes, int inicioQuinzena, int fimQuinzena);
	
}
