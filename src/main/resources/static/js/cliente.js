$(document).ready(function() {
	listarClientes();
});
    
function listarClientes(){
	$.ajax({
		method: "GET",
		url: "/scheduleAPP/cliente/",
		success: function(response) {
			$("#tabelaClientes > tbody > tr").remove();
			
			for(var i=0; i<response.length; i++){
				$('#tabelaClientes > tbody')
					.append('<tr id="' + response[i].id + '"><td>' + response[i].id + '</td>' +
                            '<td>' + response[i].nome + '</td>' +
                            '<td>' + response[i].telefone + '</td>' +
                            '<td>' + response[i].valor + '</td>' +
                            '<td>' + response[i].formaPagamento.descricao + '</td>' +
                            '<td>' + response[i].frequencia.descricao + '</td>' +
                            '<td>' + response[i].diaSemana.descricao + '</td>' +
                            '<td>' + response[i].horaInicio + " | " + response[i].horaFim + '</td>' +
                            '<td>' + ((response[i].ativo) ? "Sim" : "Não") + '</td>' +
						    '<td><button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="editaCliente(' + response[i].id + ')">Editar</button></td></tr>');
			}
		}
	
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Buscar: " + xhr.responseText);    			
	});
}

function pesquisaClientesModal(){
	var nomeBusca = $("#nomeBusca").val();
	
	if ((nomeBusca != null) && (nomeBusca.trim() != "")) {
		$.ajax({
			method: "GET",
			url: "/scheduleAPP/cliente/nome/" + nomeBusca,
			success: function(response) {
				$("#tabelaClientesModal > tbody > tr").remove();
				
				for(var i=0; i<response.length; i++){
					$('#tabelaClientesModal > tbody')
						.append('<tr id="modal' + response[i].id + '"><td>' + response[i].id + '</td>' +
                                '<td>' + response[i].nome + '</td>' +
							    '<td><button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="editaCliente(' + response[i].id + ')">Selecionar</button></td></tr>');
				}
			}
		
		}).fail(function (xhr, status, errorThrown) {
			alert("Erro ao Buscar " + xhr.responseText);    			
		});
	}
}

function editaCliente(id) {
	$.ajax({
		method: "GET",
		url: "/scheduleAPP/cliente/" + id,
		contentType: "application/json; charset=utf-8",
		success: function(response) {
    		$("#id").val(response.id);
    		$("#nome").val(response.nome);
    		$("#valor").val(response.valor);
    		$("#telefone").val(response.telefone);
    		$("#endereco").val(response.endereco);
    		$("#horaInicio").val(response.horaInicio);
    		$("#horaFim").val(response.horaFim);
    		$("#sDiaSemana").val(response.diaSemana.value);
    		$("#sFormaPagamento").val(response.formaPagamento.value);
    		$("#sFrequencia").val(response.frequencia.value);
    		$("input:radio[name=tipoLimpeza]").filter("[value=" + response.tipoLimpeza + "]").prop("checked", true);
    		$("#observacao").val(response.observacao);
    		$("#apelido").val(response.apelido);
    		$("#ativo").prop("checked",response.ativo);
		}
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Localizar para Editar " + xhr.responseText);    			
	});
}

function salvarCliente(){
	var id = $("#id").val();
	var nome = $("#nome").val();
	var valor = $("#valor").val();
	var telefone = $("#telefone").val();
	var endereco = $("#endereco").val();
	var horaInicio = $("#horaInicio").val();
	var horaFim = $("#horaFim").val();
	var diaSemana = $("#sDiaSemana").val();
	var formaPagamento = $("#sFormaPagamento").val();
	var frequencia = $("#sFrequencia").val();
	var tipoLimpeza = $('input[name="tipoLimpeza"]:checked').val();
	var observacao = $("#observacao").val();
	var apelido = $("#apelido").val();
	var ativo = $("#ativo").prop("checked");
	
	var url = "";
	var method = "";
	
	if ((nome == null) || ((nome != null) && (nome.trim() == ""))) {
		alert("Nome é Obrigatório!");
		$("#nome").focus();
		return;
	}
		
	if ((id != null) & (id != "")) {
		url = "/scheduleAPP/cliente/";
		method = "PUT";
	} else {
		url = "/scheduleAPP/cliente/";
		method = "POST";
	}

	$.ajax({
		method: method,
		url: url,
		data: JSON.stringify({
			id : id, 
			nome : nome, 
			valor : valor, 
			telefone : telefone,
			endereco : endereco,
			horaInicio : horaInicio, 
			horaFim : horaFim, 
			diaSemana : diaSemana, 
			formaPagamento : formaPagamento, 
			frequencia : frequencia, 
			tipoLimpeza : tipoLimpeza, 
			observacao : observacao,
			apelido : apelido,
			ativo : ativo
		}),
		contentType: "application/json; charset=utf-8",
		success: function(response) {
			$("#id").val(response.id);
			alert("Registro salvo com Sucesso");
			listarClientes();
		}
	
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Salvar " + xhr.responseText);    			
	});
}

function excluirClienteModal(){
	var id = $("#id").val();

	if ((id != null) && (id.trim() != "")) {
		if (confirm("Deseja Realmente Excluir o Registro?")){
			excluirCliente(id);
    		$("#" + id).remove();
		}
	}
}

function excluirClienteTelaPrincipal(){
	var id = $("#id").val();

	if ((id != null) && (id.trim() != "")) {
		if (confirm("Deseja Realmente Excluir o Registro?")){
			excluirCliente(id);
    		$("#" + id).remove();
			document.getElementById('formCadastroCliente').reset();
		}
	}
}

function excluirCliente(id) {
	$.ajax({
		method: "DELETE",
		url: "/scheduleAPP/cliente/" + id,
		success: function(response) {
    		$("#" + id).remove();
			alert(response);
		}
	}).fail(function (xhr, status, errorThrown) {
		alert("Erro ao Excluir Registro " + xhr.responseText);
	});
}

function preencheApelido(){
	var apelido = $("#apelido").val();
	if ($("#nome").val() != null){
		var arrayNome = $("#nome").val().split(" ");
		
		if ((apelido == null) || (apelido != null && apelido.trim() == "")){
			$("#apelido").val(arrayNome[0]);
		}
	}
}
