package org.luizinfo.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.luizinfo.schedule.repository.ICliente;
import org.luizinfo.schedule.repository.IEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Métodos do Controller de Eventos")
@RequestMapping(value = "/evento")
public class EventoController implements CrudController<Evento> {

	@Autowired
	private IEvento iEvento;

	@Autowired
	private ICliente iCliente;

	@Override
	public ResponseEntity<?> listar() {
		
		List<Cliente> clientes = iCliente.findAll();
		
		if (clientes.size() > 0) {
			return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Não foram encontrados clientes cadastrados!", HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> inserir(Evento objeto) {

		if (objeto.getId() != null) {
			return new ResponseEntity<String>("ID do Registro não deve ser Informado para cadastrar!", HttpStatus.BAD_REQUEST);
		}
		
		Evento eventoAux = iEvento.save(objeto);
		
		return new ResponseEntity<Evento>(eventoAux, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> atualizar(Evento objeto) {
		
		if (objeto.getId() == null) {
			return new ResponseEntity<String>("ID do Registro não foi Informado para atualizar!", HttpStatus.BAD_REQUEST);
		}

		Evento eventoAux = iEvento.save(objeto);
		
		return new ResponseEntity<Evento>(eventoAux, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> excluir(Long id) {

		Optional<Evento> eventoOp = iEvento.findById(id);

		if (eventoOp.isPresent()) {
			iEvento.delete(eventoOp.get());
			return new ResponseEntity<String>("Registro Excluído com Sucesso!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Registro não Encontrado com o ID: " + id, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> localizar(Long id) {

		Optional<Evento> eventoOp = iEvento.findById(id);
		
		if (eventoOp.isPresent()) {
			return new ResponseEntity<Evento>(eventoOp.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Registro não Encontrado com o ID: " + id, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Excluir Eventos com Exclusão dos Futuros já cadastrados")
	@DeleteMapping(value = "id/{id}/excluirFuturos/{excluirFuturos}")
	@ResponseBody
	public ResponseEntity<String> excluirEvento(@PathVariable(name = "id") Long id, 
			@PathVariable(name = "excluirFuturos") boolean excluirFuturos) {

		try {
			Optional<Evento> eventoOp = iEvento.findById(id);
			
			if (eventoOp.isPresent()) {
				if (excluirFuturos) {
					List<Evento> eventos = iEvento.findEventosFuturosCliente(eventoOp.get().getCliente(), eventoOp.get().getInicio());
					iEvento.deleteAll(eventos);
					iCliente.inativarCliente(eventoOp.get().getCliente().getId());
				} else {
					iEvento.delete(eventoOp.get());
				}
		
				return new ResponseEntity<String>("Registro Excluído com Sucesso!", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Registro Não Encontrado!", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Erro ao Excluir Registro!", HttpStatus.BAD_REQUEST);
		}
	}

}
