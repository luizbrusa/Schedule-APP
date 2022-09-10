package org.luizinfo.schedule;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.DiaSemana;
import org.luizinfo.schedule.model.Evento;
import org.luizinfo.schedule.model.FormaPagamento;
import org.luizinfo.schedule.model.Frequencia;
import org.luizinfo.schedule.repository.ICliente;
import org.luizinfo.schedule.repository.IEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class EventoTest {
	
	@Autowired
	private ICliente iCliente;
	
	@Autowired
	private IEvento iEvento;

	private Cliente cliente;
	private Evento evento;
	private Long idAux = 98989898L;

	@BeforeAll
	public void setup() {
		cliente = new Cliente();
		cliente.setNome("Teste JUNIT");
		cliente.setApelido("Teste");
		cliente.setAtivo(true);
		cliente.setDiaSemana(DiaSemana.D2_MONDAY);
		cliente.setEndereco("Teste Endereço");
		cliente.setFormaPagamento(FormaPagamento.CASH);
		cliente.setFrequencia(Frequencia.FORTNIGHT);
		cliente.setHoraInicio("13:00");
		cliente.setHoraFim("15:00");
		cliente.setId(idAux);

		Calendar calendar = Calendar.getInstance();
		calendar.set(2022, 9, 1, 13, 00);
		Date inicio = calendar.getTime();
		calendar.set(2022, 9, 1, 15, 00);
		Date fim = calendar.getTime();

		evento = new Evento();
		evento.setId(idAux);
		evento.setInicio(inicio);
		evento.setFim(fim);
		evento.setDiaInteiro(false);
//		evento.setProcessoAutomatico(null);
		evento.setTitulo("Teste");
		evento.setCliente(cliente);
	}

	@Test
	@Order(1)
	public void testCadastroEvento() {

		if ((cliente != null) && (evento != null)) {
			iCliente.save(cliente);
			iEvento.save(evento);
		}

		Optional<Cliente> clienteOp = iCliente.findById(idAux);
		assertTrue(clienteOp.isPresent(), "Cliente Não Encontrado");
	}

	@Test
	@Order(2)
	public void testFindAllEventos() {
		List<Evento> eventos = iEvento.findAll();
		assertTrue(eventos.size() > 0, "Eventos Não Encontrados");
	}

	@Test
	@Order(3)
	public void testFindEventosByCliente() {
		List<Evento> eventos = iEvento.findByCliente(cliente);
		assertTrue(eventos.size() > 0, "Eventos Não Encontrados com o Cliente informado");
	}

	@AfterAll
	public void finishTests() {
		Optional<Evento> eventoOp = iEvento.findById(idAux);
		if (eventoOp.isPresent()) {
			iEvento.delete(eventoOp.get());
		}
		
		Optional<Cliente> clienteOp = iCliente.findById(idAux);
		if (clienteOp.isPresent()) {
			iCliente.delete(clienteOp.get());
		}
	}

}
