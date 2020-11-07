package hyve.petshow.controller;

import hyve.petshow.controller.converter.AnimalEstimacaoConverter;
import hyve.petshow.controller.converter.TipoAnimalEstimacaoConverter;
import hyve.petshow.controller.representation.AnimalEstimacaoRepresentation;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.controller.representation.TipoAnimalEstimacaoRepresentation;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.AnimalEstimacaoService;
import hyve.petshow.service.port.TipoAnimalEstimacaoService;
import hyve.petshow.util.JwtUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "API animal de estimação",
        description = "API para CRUD de animal de estimação"))
public class AnimalEstimacaoController {
    @Autowired
    private AnimalEstimacaoService animalEstimacaoService;
    @Autowired
    private TipoAnimalEstimacaoService tipoAnimalEstimacaoService;
    @Autowired
    private AnimalEstimacaoConverter animalEstimacaoConverter;
    @Autowired
    private TipoAnimalEstimacaoConverter tipoAnimalEstimacaoConverter;
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Adiciona um animal de estimação ao sistema.")
    @PostMapping("/cliente/animal-estimacao")
    public ResponseEntity<AnimalEstimacaoRepresentation> adicionarAnimalEstimacao(
            @Parameter(description = "Animal que será inserido no sistema.")
            @RequestBody AnimalEstimacaoRepresentation request){
        var animalEstimacao = animalEstimacaoConverter.toDomain(request);

        animalEstimacao = animalEstimacaoService.adicionarAnimalEstimacao(animalEstimacao);

        var representation = animalEstimacaoConverter.toRepresentation(animalEstimacao);

        return ResponseEntity.status(HttpStatus.CREATED).body(representation);
    }

    @Operation(summary = "Busca animais de estimação por dono.")
    @GetMapping("/cliente/{donoId}/animal-estimacao")
    public ResponseEntity<List<AnimalEstimacaoRepresentation>> buscarAnimaisEstimacao(
            @Parameter(description = "Id do dono dos animais.")
            @PathVariable Long donoId) throws NotFoundException {
        var animaisEstimacao = animalEstimacaoService.buscarAnimaisEstimacaoPorDono(donoId);

        var representation = animalEstimacaoConverter.toRepresentationList(animaisEstimacao);

        return ResponseEntity.status(HttpStatus.OK).body(representation);
    }

    @Operation(summary = "Atualiza animal de estimação.")
    @PutMapping("/cliente/animal-estimacao/{id}")
    public ResponseEntity<AnimalEstimacaoRepresentation> atualizarAnimalEstimacao(
            @Parameter(description = "Id do animal de estimação que será atualizado.")
            @PathVariable Long id,
            @Parameter(description = "Animal que será atualizado.")
            @RequestBody AnimalEstimacaoRepresentation request) throws NotFoundException, BusinessException {
        var animalEstimacao = animalEstimacaoConverter.toDomain(request);

        animalEstimacao = animalEstimacaoService.atualizarAnimalEstimacao(id, animalEstimacao);

        var representation = animalEstimacaoConverter.toRepresentation(animalEstimacao);

        return ResponseEntity.status(HttpStatus.OK).body(representation);
    }

    @Operation(summary = "Deleta animal de estimação.")
    @DeleteMapping("/cliente/{donoId}/animal-estimacao/{id}")
    public ResponseEntity<MensagemRepresentation> removerAnimalEstimacao(
            @Parameter(description = "Id do dono do animal.")
            @PathVariable Long donoId,
            @Parameter(description = "Id do animal de estimação que será deletado.")
            @PathVariable Long id) throws BusinessException, NotFoundException {
        var response = animalEstimacaoService.removerAnimalEstimacao(id, donoId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Busca todos os tipos de animal de estimação.")
    @GetMapping("/cliente/animal-estimacao/tipos")
    public ResponseEntity<List<TipoAnimalEstimacaoRepresentation>> buscarTiposAnimalEstimacao()
            throws NotFoundException {
        var tiposAnimalEstimacao = tipoAnimalEstimacaoService.buscarTiposAnimalEstimacao();

        var representation = tipoAnimalEstimacaoConverter.toRepresentationList(tiposAnimalEstimacao);

        return ResponseEntity.status(HttpStatus.OK).body(representation);
    }
}
