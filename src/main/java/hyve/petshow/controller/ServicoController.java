package hyve.petshow.controller;

import hyve.petshow.controller.converter.ServicoConverter;
import hyve.petshow.controller.representation.ServicoRepresentation;
import hyve.petshow.domain.Servico;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.ServicoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servico")
@OpenAPIDefinition(info = @Info(title = "API servico", description = "API para CRUD de servico"))
public class ServicoController {
	@Autowired
	private ServicoService service;

	@Autowired
	private ServicoConverter converter;

    @Operation(summary = "Busca todos os tipos de servicos.")
    @GetMapping
    public ResponseEntity<List<ServicoRepresentation>> buscarServicos(@RequestParam(name = "cidade", required = false) String cidade,
    		@RequestParam(name = "estado", required = false) String estado) throws NotFoundException {
        var servicos = buscaServicos(cidade, estado);
        var representation = converter.toRepresentationList(servicos);
        return ResponseEntity.ok(representation);
    }
    
    private List<Servico> buscaServicos(String cidade, String estado) throws NotFoundException {
    	if(cidade == null && estado == null) {
    		return service.buscarServicos();
    	}
    	
    	return service.buscarServicosPresentesEmEstado(cidade, estado);
    }
}
