package org.luizinfo.schedule.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.luizinfo.schedule.model.Cliente;
import org.luizinfo.schedule.model.Evento;
import org.luizinfo.schedule.model.Frequencia;
import org.luizinfo.schedule.repository.ICliente;
import org.luizinfo.schedule.repository.ICustomEventoImpl;
import org.luizinfo.schedule.repository.IEvento;
import org.luizinfo.schedule.service.SequenceGeneratorService;
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

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Calendar", description = "Métodos do Controller do Calendar")
@RequestMapping(value = "")
@SuppressWarnings("deprecation")
public class CalendarController {
	
	@Autowired
	private IEvento iEvento;
	
	@Autowired
	private ICustomEventoImpl iCustomEventoImpl;
	
	@Autowired
	private ICliente iCliente;
	
	@GetMapping(value = "/")
	public ModelAndView index() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("calendar");
	    return modelAndView;
	}

	@GetMapping(value = "/listarEventosCalendar")
	@ResponseBody
	public ResponseEntity<String> listarEventosCalenddar() {
		List<Evento> eventos = iEvento.findAll();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retorno = "[";
		
		for (Evento evento : eventos) {
			retorno += "{ " + 
				    "\"id\":\"" + evento.getId() + "\"," +
					"\"title\":\"" + evento.getTitulo() + "\"," +
					"\"start\":\"" + fmt.format(evento.getInicio()) + "\"," +
					"\"end\":\"" + fmt.format(evento.getFim()) + "\",";
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

	@PostMapping(value = "/programarMeses")
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
				Calendar cal = Calendar.getInstance();
				cal.set(data.getYear(), data.getMonthValue() -1, data.getDayOfMonth(), 23, 59, 59);
				adicionaEventosData(cal.getTime());
			}

			return new ResponseEntity<String>("Programação Concluída!", HttpStatus.OK);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Programação não Concluída!", HttpStatus.BAD_REQUEST);
		}	
	}
	
	private void adicionaEventosData(Date data) {
		List<Cliente> clientes = iCliente.findAllByStatusDiaSemana(true, formatDiaSemana(data.getDay()));

		for (Cliente cliente : clientes) {
			Evento ultimoEventoCliente = iCustomEventoImpl.findUltimoEventoCliente(cliente, data);
			
			TimeUnit time = TimeUnit.DAYS; 
	        long diffDays = ultimoEventoCliente == null ? 999: time.convert((data.getTime() - ultimoEventoCliente.getInicio().getTime()), TimeUnit.MILLISECONDS);
			if (cliente.getFrequencia().equals(Frequencia.WEEKLY)) {
				if (diffDays < 7)
					continue;
			} else if (cliente.getFrequencia().equals(Frequencia.FORTNIGHT)) {
				if (diffDays < 14) {
					continue;
				}
			} else if (cliente.getFrequencia().equals(Frequencia.MONTHLY)) {
				if (diffDays < 28) {
					continue;
				}
			} else if (cliente.getFrequencia().equals(Frequencia.BIMONTLY)) {
				if (diffDays < 58) {
					continue;
				}
			} else if (cliente.getFrequencia().equals(Frequencia.QUARTERLY)) {
				if (diffDays < 88) {
					continue;
				}
			}

			Calendar calendar = Calendar.getInstance();
			calendar.set(data.getYear()+1900, 
					data.getMonth(), 
					data.getDate(), 
					Integer.parseInt(cliente.getHoraInicio().substring(0, 2)),
					Integer.parseInt(cliente.getHoraInicio().substring(3, 5)));
			Date inicio = calendar.getTime();
			
			calendar.set(data.getYear()+1900, 
					data.getMonth(), 
					data.getDate(), 
					Integer.parseInt(cliente.getHoraFim().substring(0, 2)),
					Integer.parseInt(cliente.getHoraFim().substring(3, 5)));
			Date fim = calendar.getTime();
			
			Evento evento = new Evento();
			evento.setId(SequenceGeneratorService.generateSequence(Evento.SEQUENCE_NAME));
			evento.setCliente(cliente);
			evento.setTitulo(cliente.getApelido());
			evento.setInicio(inicio);
			evento.setFim(fim);
			evento.setDiaInteiro(false);
			iEvento.save(evento);
		}
	}
	
    public String formatDiaSemana(int value) {
    	switch (value+1) {
		case 1:
			return "D1_SUNDAY";
		case 2:
			return "D2_MONDAY";
		case 3:
			return "D3_TUESDAY";
		case 4:
			return "D4_WEDNESDAY";
		case 5:
			return "D5_THURSDAY";
		case 6:
			return "D6_FRIDAY";
		case 7:
			return "D7_SATURDAY";
		default:
			return "";
		}
    }
    
	public int getLastDayOfMonth(Date data) {
    	switch (data.getMonth()) {
    	case 1:
    		return 28;
    	case 3, 5, 8, 10:
    		return 30;
		case 0, 2, 4, 6, 7, 9, 11:
			return 31;
		default:
			return 0;
		}
    }
	
}
