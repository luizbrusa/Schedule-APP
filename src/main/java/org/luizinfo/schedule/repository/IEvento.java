package org.luizinfo.schedule.repository;

import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IEvento extends JpaRepository<Evento, Long> {

	@Query(value = "select e from Evento e where e.cliente = :cliente")
	public List<Evento> findByCliente(@Param("cliente") Cliente cliente);

	@Query(value = "select e from Evento e where e.cliente = :cliente and e.inicio >= :dataInicial")
	public List<Evento> findEventosFuturosCliente(@Param("cliente") Cliente cliente, 
			@Param("dataInicial") String dataInicial);

	@Query(value = "select CASE WHEN count(e) > 0 THEN true ELSE false END from Evento e inner join ProcessoAutomatico p on p = e.processoAutomatico where e.cliente = :cliente and p.data = :data")
	public boolean existeEventoAutomaticoClienteData(@Param("cliente") Cliente cliente, 
			@Param("data") String data);

	@Query(value = "select CASE WHEN count(e) > 0 THEN true ELSE false END from Evento e inner join ProcessoAutomatico p on p = e.processoAutomatico where e.cliente = :cliente and cast(substring(p.data, 1, 4) as int) = :ano and cast(substring(p.data, 6, 2) as int) = :mes")
	public boolean existeEventoAutomaticoClienteMes(@Param("cliente") Cliente cliente, 
			@Param("ano") int ano,
			@Param("mes") int mes);

	@Query(value = "select CASE WHEN count(e) > 0 THEN true ELSE false END from Evento e inner join ProcessoAutomatico p on p = e.processoAutomatico where e.cliente = :cliente and cast(substring(p.data, 1, 4) as int) = :ano and cast(substring(p.data, 6, 2) as int) = :mes and cast(substring(p.data, 9, 2) AS int) between :inicioQuinzena and :fimQuinzena")
	public boolean existeEventoAutomaticoClienteQuinzena(@Param("cliente") Cliente cliente, 
			@Param("ano") int ano, 
			@Param("mes") int mes,
			@Param("inicioQuinzena") int inicioQuinzena, 
			@Param("fimQuinzena") int fimQuinzena);

}
