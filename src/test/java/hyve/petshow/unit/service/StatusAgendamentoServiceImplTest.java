package hyve.petshow.unit.service;

import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.StatusAgendamentoRepository;
import hyve.petshow.service.implementation.StatusAgendamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StatusAgendamentoServiceImplTest {
    @Mock
    private StatusAgendamentoRepository repository;
    @InjectMocks
    private StatusAgendamentoServiceImpl service;

    private StatusAgendamento statusAgendamento = criaStatusAgendamento();

    @BeforeEach
    public void init() {
        openMocks(this);

        doReturn(singletonList(statusAgendamento)).when(repository).findAll();
        doReturn(Optional.of(statusAgendamento)).when(repository).findById(anyInt());
    }

    @Test
    public void deve_retornar_lista() throws NotFoundException {
        assertFalse(service.buscarStatusAgendamento().isEmpty());
    }

    @Test
    public void deve_disparar_excecao_ao_lista_vazia() {
        doReturn(emptyList()).when(repository).findAll();

        assertThrows(NotFoundException.class,
                () -> service.buscarStatusAgendamento());
    }

    @Test
    public void deve_retornar_status_agendamento_ao_buscar_por_id() throws NotFoundException {
        assertEquals(statusAgendamento, service.buscarStatusAgendamento(1));
    }

    @Test
    public void deve_lancar_not_found_exception_ao_nao_encontrar_ao_buscar_por_id(){
        doReturn(Optional.empty()).when(repository).findById(anyInt());

        assertThrows(NotFoundException.class,
                () -> service.buscarStatusAgendamento(1));
    }
}