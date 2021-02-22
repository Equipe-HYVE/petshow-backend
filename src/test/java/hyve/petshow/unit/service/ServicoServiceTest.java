package hyve.petshow.unit.service;

import hyve.petshow.domain.Servico;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoRepository;
import hyve.petshow.service.implementation.ServicoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServicoServiceTest {
    @Mock
    private ServicoRepository repository;
    @InjectMocks
    private ServicoServiceImpl service;

    @BeforeEach
    public void init() {
        openMocks(this);

        doReturn(singletonList(new Servico())).when(repository).findAll();
    }

    @Test
    public void deve_retornar_lista() throws NotFoundException {
        assertFalse(service.buscarServicos().isEmpty());
    }

    @Test
    public void deve_disparar_excecao_ao_lista_vazia() {
        doReturn(emptyList()).when(repository).findAll();

        assertThrows(NotFoundException.class,
                () -> service.buscarServicos());
    }
}
