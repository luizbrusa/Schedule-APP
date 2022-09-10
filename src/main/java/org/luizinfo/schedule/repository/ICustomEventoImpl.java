package org.luizinfo.schedule.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ICustomEventoImpl implements ICustomEvento {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Evento findUltimoEventoCliente(Cliente cliente, Date data) {

		Query query = new Query()
				.addCriteria(Criteria
						.where("cliente").is(cliente)
						.and("inicio").lte(data))
				.with(Sort.by(Sort.Direction.DESC, "inicio"));
		
		Evento result = mongoTemplate.findOne(query, Evento.class);
		
		return result;
	}
	
	@Override
	public List<Evento> findEventosFuturosCliente(Cliente cliente, Date dataInicial) {
		Query query = new Query(Criteria
				.where("cliente").is(cliente)
				.and("inicio").gte(dataInicial));
		
		List<Evento> result = mongoTemplate.find(query, Evento.class);
		
		return result;
	}

	@Override
	public boolean existeEventoAutomaticoClienteData(Cliente cliente, int ano, int mes, int dia) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes-1, 1, 0, 0, 0);
		Date dataMin = calendar.getTime();

		calendar.set(ano, mes-1, 1, 23, 59, 59);
		Date dataMax = calendar.getTime();

		return existeEventoClientePeriodo(cliente, dataMin, dataMax);
	}

	@Override
	public boolean existeEventoAutomaticoClienteMes(Cliente cliente, int ano, int mes) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, 1, 0, 0, 0); //Mês menos 1 pois o Gregorian Calendar começa com 0 para Janeiro
		Date dataMin = calendar.getTime();
		
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 23, 59, 59);
		Date dataMax = calendar.getTime();

		return existeEventoClientePeriodo(cliente, dataMin, dataMax);
	}

	@Override
	public boolean existeEventoAutomaticoClienteQuinzena(Cliente cliente, int ano, int mes, int inicioQuinzena, int fimQuinzena) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, inicioQuinzena, 0, 0, 0);
		Date dataMin = calendar.getTime();

		calendar.set(ano,  mes, fimQuinzena, 23, 59, 59);
		Date dataMax = calendar.getTime();
		
		return existeEventoClientePeriodo(cliente, dataMin, dataMax);
	}
	
	private boolean existeEventoClientePeriodo(Cliente cliente, Date dataMin, Date dataMax) {
		Query query = new Query(Criteria
				.where("cliente").is(cliente)
				.and("inicio").gte(dataMin)
				.and("fim").lte(dataMax));
		Long qtd = mongoTemplate.find(query, Evento.class).stream()
				.count();
		return qtd > 0;
	}
}
