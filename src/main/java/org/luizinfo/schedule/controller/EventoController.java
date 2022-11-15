package org.luizinfo.schedule.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.luizinfo.schedule.repository.ICliente;
import org.luizinfo.schedule.repository.ICustomEventoImpl;
import org.luizinfo.schedule.repository.IEvento;
import org.luizinfo.schedule.service.SequenceGeneratorService;
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
	private ICustomEventoImpl iCustomEventoImpl;

	@Autowired
	private ICliente iCliente;

	@Override
	public ResponseEntity<?> listar() {
		
		List<Evento> eventos = iEvento.findAll();
		
		if (eventos.size() > 0) {
			return new ResponseEntity<List<Evento>>(eventos, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Não foram encontrados eventos cadastrados!", HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> inserir(Evento objeto) {

		if (objeto.getId() != null) {
			return new ResponseEntity<String>("ID do Registro não deve ser Informado para cadastrar!", HttpStatus.BAD_REQUEST);
		}
		
		objeto.setId(SequenceGeneratorService.generateSequence(Evento.SEQUENCE_NAME));
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
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.US);
			cal.setTime(eventoOp.get().getInicio());
			cal.add(Calendar.HOUR, 5);
			eventoOp.get().setInicio(cal.getTime());
			cal.setTime(eventoOp.get().getFim());
			cal.add(Calendar.HOUR, 5);
			eventoOp.get().setFim(cal.getTime());

			if (eventoOp.get().getCliente() != null) {
				eventoOp.get().setCliente(iCliente.findById(eventoOp.get().getCliente().getId()).get());
			}
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
					Cliente cliente = eventoOp.get().getCliente();
					cliente.setAtivo(false);
					iCliente.save(cliente);

					List<Evento> eventos = iCustomEventoImpl.findEventosFuturosCliente(eventoOp.get().getCliente(), eventoOp.get().getInicio());
					iEvento.deleteAll(eventos);
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
