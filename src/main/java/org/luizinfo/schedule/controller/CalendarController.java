package org.luizinfo.schedule.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.List;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.luizinfo.schedule.model.Frequencia;
import org.luizinfo.schedule.model.ProcessoAutomatico;
import org.luizinfo.schedule.repository.ICliente;
import org.luizinfo.schedule.repository.IEvento;
import org.luizinfo.schedule.repository.IProcessoAutomatico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "Métodos do Controller do Calendar")
@RequestMapping(value = "/")
public class CalendarController {
	
	@Autowired
	private IEvento iEvento;
	
	@Autowired
	private ICliente iCliente;
	
	@Autowired
	private IProcessoAutomatico iProcessoAutomatico;

	@GetMapping
	public ModelAndView index() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("calendar");
	    return modelAndView;
	}

	@GetMapping(value = "listarEventosCalendar")
	@ResponseBody
	public ResponseEntity<String> listarEventosCalenddar() {
		List<Evento> eventos = iEvento.findAll();
		String retorno = "[";
		
		for (Evento evento : eventos) {
			retorno += "{ " + 
				    "\"id\":\"" + evento.getId() + "\"," +
					"\"title\":\"" + evento.getTitulo() + "\"," +
					"\"start\":\"" + evento.getInicio() + "\"," +
					"\"end\":\"" + evento.getFim() + "\",";
			if (evento.getCliente() == null) {
				retorno += "\"backgroundColor\": \"blue\",";
			} else {
				if (evento.getCliente().isAtivo()) {
					retorno += "\"backgroundColor\": \"green\",";
				} else {
					retorno += "\"backgroundColor\": \"red\",";
				}
			}
			
			retorno += "\"allDay\":" + evento.isDiaInteiro();
			retorno += "},";
		}

		if (!retorno.isEmpty()) {
			retorno = retorno.substring(0,retorno.length()-1);
		}
		retorno += "]";

		return new ResponseEntity<String>(retorno, HttpStatus.OK);
	}

	@PostMapping(value = "programarMeses")
	@ResponseBody
	public ResponseEntity<?> programarMeses(@RequestParam(name = "ano") String ano, 
			@RequestParam(name = "mesInicial") String mesInicial,
			@RequestParam(name = "meses") String meses) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calDataInicial = Calendar.getInstance();
			Calendar calDataFinal = Calendar.getInstance();
	
			if (Integer.parseInt(mesInicial) < 10) {
				calDataInicial.setTime(formatter.parse(ano + "-0" + Integer.parseInt(mesInicial) + "-01"));
			} else {
				calDataInicial.setTime(formatter.parse(ano + "-" + Integer.parseInt(mesInicial) + "-01"));
			}
	
			if (Integer.parseInt(mesInicial) + Integer.parseInt(meses) < 10) {
				calDataFinal.setTime(formatter.parse(ano + "-0" + (Integer.parseInt(mesInicial) + Integer.parseInt(meses) -1) + "-01"));
			} else {
				calDataFinal.setTime(formatter.parse(ano + "-" + (Integer.parseInt(mesInicial) + Integer.parseInt(meses) -1) + "-01"));
			}
	
			LocalDate localDataInicial = calDataInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDataFinal = LocalDate.parse(formatter.format(calDataFinal.getTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
				       .with(TemporalAdjusters.lastDayOfMonth());
			
			for (LocalDate data = localDataInicial; data.isEqual(localDataFinal) || data.isBefore(localDataFinal); data = data.plusDays(1)) {
				
				if (!iProcessoAutomatico.existeProcessamentoData(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
					adicionaEventosData(data);
				}
			}

			return new ResponseEntity<String>("Programação Concluída!", HttpStatus.OK);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Programação não Concluída!", HttpStatus.BAD_REQUEST);
		}	
	}
	
	private void adicionaEventosData(LocalDate data) {
		List<Cliente> clientes = iCliente.findAllByStatusDiaSemana(true, data.getDayOfWeek().toString());
		ProcessoAutomatico processoAutomatico = null;

		for (Cliente cliente : clientes) {
			if (!cliente.getFrequencia().equals(Frequencia.WEEKLY)) {
				if (cliente.getFrequencia().equals(Frequencia.MONTHLY)) {
					if (iEvento.existeEventoAutomaticoClienteMes(cliente,
							data.getYear(),
							data.getMonthValue())) {
						continue;
					}
				} else {
					if (iEvento.existeEventoAutomaticoClienteQuinzena(cliente,
							data.getYear(),
							data.getMonthValue(),
							data.getDayOfMonth() <= 15 ? 1 : 16, 
							data.getDayOfMonth() <= 15 ? 15 : 31)) {
						continue;
					}
				}
			}

			if (processoAutomatico == null) {
				processoAutomatico = new ProcessoAutomatico();
				processoAutomatico.setData(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				iProcessoAutomatico.save(processoAutomatico);
			}
			
			Evento evento = new Evento();

			evento.setCliente(cliente);
			evento.setTitulo(cliente.getApelido());
			evento.setInicio(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + cliente.getHoraInicio() + ":00");
			evento.setFim(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + cliente.getHoraFim() + ":00");
			evento.setDiaInteiro(false);
			evento.setProcessoAutomatico(processoAutomatico);
			
			iEvento.save(evento);
		}
	}
	
}
