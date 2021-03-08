package hyve.petshow.controller;

import hyve.petshow.controller.converter.ClienteConverter;
import hyve.petshow.controller.representation.ClienteRepresentation;
import hyve.petshow.service.port.ClienteService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hyve.petshow.util.LogUtils.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;
import static hyve.petshow.util.LogUtils.INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/cliente")
@OpenAPIDefinition(info = @Info(title = "API cliente", description = "API para CRUD de cliente"))
public class ClienteController {
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private ClienteConverter clienteConverter;

	@Operation(summary = "Busca cliente por id.")
	@GetMapping("/{id}")
	public ResponseEntity<ClienteRepresentation> buscarClientePorId(
			@Parameter(description = "Id do cliente.")
			@PathVariable Long id) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/cliente/{}", id);
		var cliente = clienteService.buscarPorId(id);
		var representation = clienteConverter.toRepresentation(cliente);

		return ResponseEntity.status(HttpStatus.OK).body(representation);
	}

	@Operation(summary = "Atualiza cliente.")
	@PutMapping("/{id}")
	public ResponseEntity<ClienteRepresentation> atualizarCliente(
			@Parameter(description = "Id do cliente.")
			@PathVariable Long id,
			@Parameter(description = "Cliente que será atualizado")
			@RequestBody ClienteRepresentation request) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/cliente/{}", request, id);
		var cliente = clienteService.atualizarConta(id, clienteConverter.toDomain(request));
		var representation = clienteConverter.toRepresentation(cliente);

		return ResponseEntity.status(HttpStatus.OK).body(representation);
	}
}
