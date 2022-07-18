package org.luizinfo.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.repository.ICliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Métodos do Controller de Clientes")
@RequestMapping(value = "/cliente")
public class ClienteController implements CrudController<Cliente> {

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
	public ResponseEntity<?> inserir(Cliente objeto) {

		if (objeto.getId() != null) {
			return new ResponseEntity<String>("ID do Registro não deve ser Informado para cadastrar!", HttpStatus.BAD_REQUEST);
		}
		
		Cliente clienteAux = iCliente.save(objeto);
		
		return new ResponseEntity<Cliente>(clienteAux, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> atualizar(Cliente objeto) {
		
		if (objeto.getId() == null) {
			return new ResponseEntity<String>("ID do Registro não foi Informado para atualizar!", HttpStatus.BAD_REQUEST);
		}

		Cliente clienteAux = iCliente.save(objeto);
		
		return new ResponseEntity<Cliente>(clienteAux, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> excluir(Long id) {

		Optional<Cliente> clienteOp = iCliente.findById(id);

		if (clienteOp.isPresent()) {
			iCliente.delete(clienteOp.get());
			return new ResponseEntity<String>("Registro Excluído com Sucesso!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Registro não Encontrado com o ID: " + id, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> localizar(Long id) {

		Optional<Cliente> clienteOp = iCliente.findById(id);
		
		if (clienteOp.isPresent()) {
			return new ResponseEntity<Cliente>(clienteOp.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Registro não Encontrado com o ID: " + id, HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Localizar Registros por Nome")
	@GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listarClientesPorNome(@PathVariable(value = "nome") String nome) {
    	
    	List<Cliente> clientes = iCliente.findAllByNome(nome.trim().toUpperCase());
   		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
    }

	@ApiOperation(value = "Localizar Clientes Ativos")
    @GetMapping(value = "/ativos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listarClientesAtivos() {
    	List<Cliente> clientes = iCliente.findAllByStatus(true);
    	
		if (clientes.size() > 0) {
			return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Não foram encontrados clientes ativos!", HttpStatus.BAD_REQUEST);
		}
    }

	@ApiOperation(value = "Localizar Clientes por Nome")
	@GetMapping(value = "/cadastro")
	public ModelAndView cadCliente() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("cadCliente");
	    return modelAndView;
	}

}
