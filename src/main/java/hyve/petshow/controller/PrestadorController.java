package hyve.petshow.controller;

import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.service.port.PrestadorService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/prestador")
@OpenAPIDefinition(info = @Info(title = "API prestador", description = "API para CRUD de prestador"))
public class PrestadorController {
	@Autowired
	private PrestadorService service;
	@Autowired
	private PrestadorConverter converter;

	@Operation(summary = "Busca prestador por id.")
	@GetMapping("/{id}")
	public ResponseEntity<PrestadorRepresentation> buscarPrestadorPorId(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long id) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}", id);
		var prestador = service.buscarPorId(id);
		var representation = converter.toRepresentation(prestador);

		return ResponseEntity.status(HttpStatus.OK).body(representation);
	}

	@Operation(summary = "Atualiza prestador.")
	@PutMapping("/{id}")
	public ResponseEntity<PrestadorRepresentation> atualizarPrestador(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long id,
			@Parameter(description = "Prestador que será atualizado.")
			@RequestBody PrestadorRepresentation request) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}", request, id);
		var prestador = converter.toDomain(request);
		prestador = service.atualizarConta(id, prestador);
		var representation = converter.toRepresentation(prestador);

		return ResponseEntity.status(HttpStatus.OK).body(representation);
	}
}
