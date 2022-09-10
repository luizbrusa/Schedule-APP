$(document).ready(function() {
	listarClientes();
});

function validaMinMax(){
	var mesInicial = $("#sMesPrograma").val();
	var meses = 13 - parseInt(mesInicial,10);
	
	if ($("#mesesPrograma").val() > meses) {
		$("#mesesPrograma").val(meses);
	}

	if ($("#anoPrograma").val() < document.getElementById("anoPrograma").min) {
		$("#anoPrograma").val(document.getElementById("anoPrograma").min);
	}
	
	if ($("#anoPrograma").val() > document.getElementById("anoPrograma").max) {
		$("#anoPrograma").val(document.getElementById("anoPrograma").max);
	}
}
      
function listarClientes(){
	$.ajax({
		method: "GET",
		url: "/scheduleAPP/cliente/ativos",
		success: function(response) {
			$("#clientes > div").remove();
			for(var i=0; i<response.length; i++){
				$("#clientes")
					.append("<div class='fc-event fc-h-event fc-daygrid-event fc-daygrid-block-event' id='" + response[i].id + "'>" +
                            "<div class='fc-event-main'>" + response[i].apelido + "</div>" +
						    "</div>");
			}
		}
	
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Buscar: " + xhr.responseText);    			
	});
}

function cadastroEventoModal(info) {
	if (info.event != null) {
		$.ajax({
			method: "GET",
			url: "/scheduleAPP/evento/" + info.event.id,
			success: function(response) {
				var dataInicio = response.inicio;
				var dataFim = response.fim;
				var idCliente = null;
				var nomeCliente = null;
				
				if (response.cliente != null) {
					idCliente = response.cliente.id;
					nomeCliente = response.cliente.nome;
				}
				
				$("#idCliente").val(idCliente);
				$("#allDay").val(false);
				$("#cliente").val(nomeCliente);
				$("#id").val(response.id);
				$("#title").val(response.titulo);
				$("#start").val(moment(dataInicio).format('YYYY-MM-DD'));
				
				if (response.diaInteiro){
					$("#startTime").val(document.getElementById("startTime").min);
					$("#endTime").val(document.getElementById("endTime").max);
				} else {
					$("#startTime").val(moment(dataInicio).format('HH:mm'));
					$("#endTime").val(moment(dataFim).format('HH:mm'));
				}
				
				$('#eventoModal').modal("show");
			}
		}).fail(function (xhr, status, errorThrown) {
			alert("Erro ao Buscar por Id " + xhr.responseText);
		});
	} else if (info.draggedEl != null) {
		$.ajax({
			method: "GET",
			url: "/scheduleAPP/cliente/" + info.draggedEl.id,
			success: function(response) {
				$("#idCliente").val(response.id);
				$("#allDay").val(false);
				$("#cliente").val(response.nome);
				$("#title").val(response.apelido);
				$("#start").val(info.dateStr);
				$("#startTime").val(response.horaInicio);
				$("#endTime").val(response.horaFim);

				$('#eventoModal').modal("show");
			}
		}).fail(function (xhr, status, errorThrown) {
			alert("Erro ao Buscar por Id " + xhr.responseText);
		});
	} else {
		$("#idCliente").val(null);
		$("#allDay").val(true);
		$("#cliente").val(null);
		$("#title").val("Evento Avulso");
		$("#start").val(info.dateStr);
		$("#startTime").val(document.getElementById("startTime").min);
		$("#endTime").val(document.getElementById("endTime").max);

		$('#eventoModal').modal("show");
	}
}

function programarMesesModal() {
	var dataAtual = new Date();
	
	$("#anoPrograma").val(moment(dataAtual).format('YYYY'));
	$("#sMesPrograma").val(moment(dataAtual).format('MM'));
	$("#mesesPrograma").val(1);
	
	$('#programarMesesModal').modal("show");
}

function salvarProgramacaoMeses(){
	var ano = $("#anoPrograma").val();
	var mesInicial = $("#sMesPrograma").val();
	var meses = $("#mesesPrograma").val();
	
	if ((ano == null) || (ano.toString() == "")){
		alert("Ano a Progrmar � Obrigat�rio!");
		$("#anoPrograma").focus();
		
		return;
	}
	
	if ((mesInicial == null) || (mesInicial == "")){
		alert("M�s Inicial a Progrmar � Obrigat�rio!");
		$("#sMesPrograma").focus();
		
		return;
	}

	if ((meses == null) || (meses.toString() == "")){
		alert("M�ses a Progrmar � Obrigat�rio!");
		$("#mesesPrograma").focus();
		
		return;
	}

	$.ajax({
		method: "POST",
		url: "programarMeses",
		data: "ano=" + ano + "&mesInicial=" + mesInicial + "&meses=" + meses,
		success: function(response) {
			alert("Programação Efetuada com Sucesso!");

			$('#programarMesesModal').modal("hide");
			window.location.href='/scheduleAPP/';
		}
	
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Progrmar " + xhr.responseText);    			
	});
}

function salvarEventoModal(){
	var allDay = $("#allDay").val();
	
	if (($("#startTime").val() == document.getElementById("startTime").min) && 
		($("#endTime").val() == document.getElementById("endTime").max)) {
		allDay = true;
	} else {
		allDay = false;
	}
	
	salvarEvento($("#id").val(), 
			$("#title").val(), 
			allDay, 
			$("#start").val() + "T" + $("#startTime").val(), 
			$("#start").val() + "T" + $("#endTime").val(), 
			$("#idCliente").val());
	
	$('#eventoModal').modal("hide");
	window.location.href='/scheduleAPP/';
}

function salvarEventoDrop(info) {
	$.ajax({
		method: "GET",
		url: "/scheduleAPP/evento/" + info.event.id,
		success: function(response) {
			var idCliente = null;
			if (response.cliente != null) {
				idCliente = response.cliente.id;
			} else {
				idCliente = null;
			}
			
			salvarEvento(info.event.id,
					info.event.title,
					info.event.allDay,
					info.event.start,
					info.event.end,
					idCliente.toString());
		}
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Buscar por Id " + xhr.responseText);
	});
}

function salvarEvento(id,title,allDay,start,end,idCliente){
	var inicio = new Date(start);
	inicio = moment(inicio).format('YYYY-MM-DD[T]HH:mm:ss');
	
	var fim = new Date(end);
	fim = moment(fim).format('YYYY-MM-DD[T]HH:mm:ss');

	var url = "";
	var method = "";
	var evento = null;

	if ((idCliente != null) && (idCliente.trim() != "")) {
		evento = {id : id,
				titulo : title, 
				diaInteiro : allDay, 
				inicio : inicio,
				fim : fim,
				processoAutomatico : null,
				cliente : {id : idCliente.trim()}
		};
	} else {
		evento = {id : id,
				titulo : title, 
				diaInteiro : allDay, 
				inicio : inicio,
				fim : fim,
				processoAutomatico : null,
				cliente : null
		};
	}
	
	if ((id != null) & (id.trim() != "")) {
		method = "PUT";
		url = "/scheduleAPP/evento/";
	} else {
		method = "POST";
		url = "/scheduleAPP/evento/";
	}
	
	$.ajax({
		contentType: "application/json; charset=utf-8",
		method: method,
		url: url,
		data: JSON.stringify(evento),
		success: function(response) {
			alert("Salvo com Sucesso!");
		}
	
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Salvar " + xhr.responseText);    			
	});
}

function excluirEventoModal(){
	var id = $("#id").val();
	var idCliente = $("#idCliente").val();

	if (confirm("Deseja realmente Excluir o Registro?")){
		var excluirFuturos = false;

		if ((idCliente != null) && (idCliente.trim() != "") && (confirm("Excluir Programação Futura e Cancelar o Cliente?"))) {
			excluirFuturos = true;
		}

		$.ajax({
			method: "DELETE",
			url: "/scheduleAPP/evento/id/" + id + "/excluirFuturos/" + excluirFuturos,
			success: function(response) {
				alert("Exclu�do com Sucesso!");
			}
		
		}).fail(function (xhr, status, errorThrown) {
			alert("Erro ao Excluir " + xhr.responseText);    			
		});

		$('#eventoModal').modal("hide");
		window.location.href='/scheduleAPP/';
	} else {
		$('#eventoModal').modal("hide");
	}
}

document.addEventListener('DOMContentLoaded', function() {
	  var Calendar = FullCalendar.Calendar;
	  var Draggable = FullCalendar.Draggable;
	
	  var containerEl = document.getElementById('external-events');
	  var calendarEl = document.getElementById('calendar');
	  var checkbox = document.getElementById('drop-remove');
	
	  new Draggable(containerEl, {
	    itemSelector: '.fc-event',
	    eventData: function(eventEl) {
	      return {
	        title: eventEl.innerText
	      };
	    }
	  });
	  
	  var calendar = new Calendar(calendarEl, {
	    headerToolbar: {
	      left: 'prev,next today',
	      center: 'title',
	      right: 'dayGridMonth,timeGridWeek,timeGridDay'
	    },
	    editable: true,
	    droppable: true, // this allows things to be dropped onto the calendar
	    locale: 'pt-br',
	    drop: function(info) {
	        cadastroEventoModal(info);
	    },
	    eventDrop: function(info) {
	        if (!confirm("Deseja realmente alterar este agendamento?")) {
	            info.revert();
	        } else {
	        	salvarEventoDrop(info);
	        }
	    },
	    eventClick: function(info) {
		    cadastroEventoModal(info);
	    },
		dateClick: function(info) {
		    cadastroEventoModal(info);
		},
		events: "/scheduleAPP/listarEventosCalendar"
	  });
	
	  calendar.render();
});
