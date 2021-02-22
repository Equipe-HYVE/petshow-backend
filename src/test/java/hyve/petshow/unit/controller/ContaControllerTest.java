package hyve.petshow.unit.controller;

import hyve.petshow.controller.ContaController;
import hyve.petshow.controller.converter.ContaConverter;
import hyve.petshow.controller.representation.ContaRepresentation;
import hyve.petshow.mock.ClienteMock;
import hyve.petshow.service.port.GenericContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ContaControllerTest {
	@Mock
	private ContaConverter converter;
	@Mock
	private GenericContaService service;
	@InjectMocks
	private ContaController controller;
	
	@BeforeEach
    public void init() throws Exception {
        initMocks(this);
    }
	
	@Test
	public void deve_retornar_um_corpo() throws Exception {
		var conta = ClienteMock.criaCliente();
		var esperado = new ResponseEntity<ContaRepresentation>(ClienteMock.clienteRepresentation(), HttpStatus.OK);
		Mockito.doCallRealMethod().when(converter).toRepresentation(Mockito.any());
		Mockito.doReturn(conta).when(service).buscarPorId(Mockito.anyLong());
		
		var busca = controller.buscarContaPorId(1l);
		assertEquals(esperado.getBody().getId(), busca.getBody().getId());
	}
	
}
