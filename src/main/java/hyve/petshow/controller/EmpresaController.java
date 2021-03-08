package hyve.petshow.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hyve.petshow.controller.converter.EmpresaConverter;
import hyve.petshow.controller.representation.EmpresaRepresentation;
import hyve.petshow.service.port.EmpresaService;

import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/empresa")
public class EmpresaController {
	@Autowired
	private EmpresaService service;
	@Autowired
	private EmpresaConverter converter;
	
	@PutMapping("/{id}")
	public ResponseEntity<EmpresaRepresentation> atualizaEmpresa(
			@Parameter(description = "Id da empresa.")
			@PathVariable Long id,
			@Parameter(description = "Requisição para atualizar")
			@RequestBody EmpresaRepresentation representation) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/empresa/{}", representation, id);
		var domain = converter.toDomain(representation);
		var empresa = service.atualizaEmpresa(id, domain);
		var response = converter.toRepresentation(empresa);
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<EmpresaRepresentation> atualizaEmpresa(
			@Parameter(description = "Id da empresa.")
			@PathVariable Long id,
			@Parameter(description = "Requisição para atualizar")
			@RequestBody EmpresaRepresentation representation,
			@Parameter(description = "Flag para definir se a empresa está ativa ou não")
			@RequestParam Boolean ativo) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/empresa/{}?ativo={}", representation, id, ativo);
		var domain = converter.toDomain(representation);
		var empresa = service.desativaEmpresa(id, ativo);
		var response = converter.toRepresentation(empresa);
		return ResponseEntity.ok(response);
	}

}
