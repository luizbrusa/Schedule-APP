package org.luizinfo.schedule.repository;

import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEvento extends MongoRepository<Evento, Long> {

	public List<Evento> findByCliente(Cliente cliente);

}
