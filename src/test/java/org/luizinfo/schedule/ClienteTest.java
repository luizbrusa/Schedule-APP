package org.luizinfo.schedule;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.DiaSemana;
import org.luizinfo.schedule.model.FormaPagamento;
import org.luizinfo.schedule.model.Frequencia;
import org.luizinfo.schedule.repository.ICliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ClienteTest {
	
	@Autowired
	private ICliente iCliente;

	private Cliente cliente;
	private Long idAuxCliente = 98989898L;

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
		cliente.setId(idAuxCliente);
	}

	@Test
	@Order(1)
	public void testCadastroCliente() {

		if (cliente != null) {
			iCliente.save(cliente);
		}

		Optional<Cliente> clienteOp = iCliente.findById(idAuxCliente);
		assertTrue(clienteOp.isPresent(), "Cliente Não Encontrado");
	}

	@Test
	@Order(2)
	public void testFindClientesByNome() {
		List<Cliente> clientes = iCliente.findAllByNome("Teste JUNIT");
		assertTrue(clientes.size() > 0, "Clientes Não Encontrados com o Nome informado");
	}
	
	@Test
	@Order(3)
	public void testFindClientesByStatus() {
		List<Cliente> clientes = iCliente.findAllByStatus(true);
		assertTrue(clientes.size() > 0, "Clientes Não Encontrados com o Status Informado");
	}

	@Test
	@Order(4)
	public void testFindClientesByDiaSemana() {
		List<Cliente> clientes = iCliente.findAllByDiaSemana(DiaSemana.D2_MONDAY.getValue());
		assertTrue(clientes.size() > 0, "Clientes Não Encontrados com o Dia da Semana Informado");
	}

	@Test
	@Order(5)
	public void testFindClientesByStatusDiaSemana() {
		List<Cliente> clientes = iCliente.findAllByStatusDiaSemana(true, DiaSemana.D2_MONDAY.getValue());
		assertTrue(clientes.size() > 0, "Clientes Não Encontrados com o Status e Dia da Semana Informados");
	}

	@AfterAll
	public void finishTests() {
		Optional<Cliente> clienteOp = iCliente.findById(idAuxCliente);
		
		if (clienteOp.isPresent()) {
			iCliente.delete(clienteOp.get());
		}
	}

}
