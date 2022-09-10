package org.luizinfo.schedule.repository;

import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICliente extends MongoRepository<Cliente, Long> {

	@Query(value = "{'nome': ?0}")
	public List<Cliente> findAllByNome(String nome);

	@Query(value = "{'ativo': ?0}")
	public List<Cliente> findAllByStatus(boolean ativo);

	@Query(value = "{'ativo': true, 'dia_semana': ?0}")
	public List<Cliente> findAllByDiaSemana(String diaSemana);

	@Query(value = "{'ativo': ?0, 'dia_semana': ?1}")
	public List<Cliente> findAllByStatusDiaSemana(boolean ativo, String diaSemana);

}
