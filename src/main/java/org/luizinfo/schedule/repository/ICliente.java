package org.luizinfo.schedule.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.luizinfo.schedule.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICliente extends JpaRepository<Cliente, Long> {

	@Query(value = "select c from Cliente c where upper(trim(c.nome)) like %:nome%")
	public List<Cliente> findAllByNome(@Param("nome") String nome);

	@Query(value = "select c from Cliente c where c.ativo = :ativo")
	public List<Cliente> findAllByStatus(@Param("ativo") boolean ativo);

	@Query(value = "select c from Cliente c where ativo = true and upper(trim(c.diaSemana)) = :diaSemana")
	public List<Cliente> findAllByDiaSemana(@Param("diaSemana") String diaSemana);

	@Query(value = "select c from Cliente c where ativo = :ativo and upper(trim(c.diaSemana)) = :diaSemana")
	public List<Cliente> findAllByStatusDiaSemana(@Param("ativo") boolean ativo, 
			@Param("diaSemana") String diaSemana);

	@Modifying
	@Transactional
	@Query(value = "update Cliente set ativo = true where id = :id")
	public void ativarCliente(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query(value = "update Cliente set ativo = false where id = :id")
	public void inativarCliente(@Param("id") Long id);

}
