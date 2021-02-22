package hyve.petshow.unit.controller;

import hyve.petshow.controller.ServicoController;
import hyve.petshow.controller.converter.ServicoConverter;
import hyve.petshow.controller.representation.ServicoRepresentation;
import hyve.petshow.domain.Servico;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.ServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static hyve.petshow.mock.ServicoMock.servico;
import static hyve.petshow.mock.ServicoMock.servicoRepresentation;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServicoControllerTest {
    @Mock
    private ServicoService service;
    @Mock
    private ServicoConverter converter;
    @InjectMocks
    private ServicoController controller;

    private List<Servico> servicos = singletonList(servico());
    private List<ServicoRepresentation> servicosRepresentation = singletonList(servicoRepresentation());

    @BeforeEach
    public void init() throws NotFoundException {
        initMocks(this);

        doReturn(servicos).when(service).buscarServicos();
        doReturn(servicosRepresentation).when(converter).toRepresentationList(servicos);
    }

    @Test
    public void deve_retornar_todos_os_tipos_de_servico() throws NotFoundException {
        var expected = ResponseEntity.ok(servicosRepresentation);

        var actual = controller.buscarServicos();

        assertEquals(expected, actual);
    }
}
