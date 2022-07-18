package org.luizinfo.schedule.repository;

import org.luizinfo.schedule.model.ProcessoAutomatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProcessoAutomatico extends JpaRepository<ProcessoAutomatico, Long>{

	@Query(value = "select CASE WHEN COUNT(p) > 0 THEN true ELSE false END from ProcessoAutomatico p where p.data = :data")
	public boolean existeProcessamentoData(@Param("data") String data);
}
