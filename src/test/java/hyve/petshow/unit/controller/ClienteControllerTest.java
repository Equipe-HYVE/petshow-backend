package hyve.petshow.unit.controller;

import hyve.petshow.controller.ClienteController;
import hyve.petshow.controller.converter.ClienteConverter;
import hyve.petshow.controller.representation.ClienteRepresentation;
import hyve.petshow.domain.Cliente;
import hyve.petshow.service.port.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static hyve.petshow.mock.ClienteMock.clienteRepresentation;
import static hyve.petshow.mock.ClienteMock.criaCliente;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ClienteControllerTest {
    @Mock
    private ClienteService service;
    @Mock
    private ClienteConverter converter;
    @InjectMocks
    private ClienteController controller;

    private Cliente cliente = criaCliente();
    private ClienteRepresentation clienteRepresentation = clienteRepresentation();

    @BeforeEach
    public void init() throws Exception {
        initMocks(this);

        doReturn(cliente).when(service).buscarPorId(anyLong());
        doReturn(cliente).when(service).atualizarConta(anyLong(), any(Cliente.class));
        doReturn(clienteRepresentation).when(converter).toRepresentation(any(Cliente.class));
        doReturn(cliente).when(converter).toDomain(any(ClienteRepresentation.class));
    }

    @Test
    public void deve_retornar_cliente_buscado() throws Exception {
        var expected = ResponseEntity.ok(clienteRepresentation);

        var actual = controller.buscarClientePorId(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_retornar_cliente_atualizado() throws Exception {
        var expected = ResponseEntity.ok(clienteRepresentation);

        var actual = controller.atualizarCliente(1L, clienteRepresentation);

        assertEquals(expected, actual);
    }
}
