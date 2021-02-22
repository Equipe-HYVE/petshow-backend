package hyve.petshow.controller;

import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.service.port.PrestadorService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		var prestador = converter.toDomain(request);
		prestador = service.atualizarConta(id, prestador);
		var representation = converter.toRepresentation(prestador);

		return ResponseEntity.status(HttpStatus.OK).body(representation);
	}
}
