package hyve.petshow.unit.service;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AgendamentoRepository;
import hyve.petshow.service.implementation.AgendamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static hyve.petshow.mock.AgendamentoMock.criaAgendamento;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class AgendamentoServiceTest {
    @Mock
    private AgendamentoRepository repository;
    @InjectMocks
    private AgendamentoServiceImpl service;

    private Agendamento agendamento = criaAgendamento();
    private List<Agendamento> agendamentoList = Arrays.asList(agendamento);
    private Page<Agendamento> agendamentoPage = new PageImpl<>(agendamentoList);

    private Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void init() {
        openMocks(this);

        doReturn(agendamento).when(repository).save(any(Agendamento.class));
        doReturn(agendamentoPage).when(repository).findByClienteId(anyLong(), any(Pageable.class));
        doReturn(agendamentoPage).when(repository).findByPrestadorId(anyLong(), any(Pageable.class));
        doReturn(Optional.of(agendamento)).when(repository).findById(anyLong());
    }

    @Test
    public void deve_adicionar_agendamento(){
        var actual = service.adicionarAgendamento(agendamento);

        assertEquals(agendamento, actual);
    }

    @Test
    public void deve_buscar_agendamentos_por_cliente() throws NotFoundException, BusinessException {
        var actual = service.buscarAgendamentosPorCliente(1L, pageable);

        assertEquals(agendamentoPage, actual);
    }

    @Test
    public void deve_lancar_not_found_exception_caso_buscar_agendamentos_por_cliente_nao_retornar_nada(){
        var paginaVazia = new PageImpl<Agendamento>(Collections.emptyList());

        doReturn(paginaVazia).when(repository).findByClienteId(1L, pageable);

        assertThrows(NotFoundException.class, () ->
                service.buscarAgendamentosPorCliente(1L, pageable));
    }

    @Test
    public void deve_lancar_business_exception_caso_cliente_id_divergente_ao_buscar_agendamentos_por_cliente(){
        assertThrows(BusinessException.class, () ->
                service.buscarAgendamentosPorCliente(2L, pageable));
    }

    @Test
    public void deve_buscar_agendamentos_por_prestador() throws NotFoundException {
        var actual = service.buscarAgendamentosPorPrestador(1L, pageable);

        assertEquals(agendamentoPage, actual);
    }

    @Test
    public void deve_lancar_not_found_exception_caso_buscar_agendamentos_por_prestador_nao_retornar_nada(){
        var paginaVazia = new PageImpl<Agendamento>(Collections.emptyList());

        doReturn(paginaVazia).when(repository).findByPrestadorId(1L, pageable);

        assertThrows(NotFoundException.class, () ->
                service.buscarAgendamentosPorPrestador(1L, pageable));
    }

    @Test
    public void deve_buscar_agendamento_por_id() throws NotFoundException, BusinessException {
        var actual = service.buscarPorId(1L, 1L);

        assertEquals(agendamento, actual);
    }

    @Test
    public void deve_lancar_not_found_exception_ao_buscar_agendamento_por_id(){
        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThrows(NotFoundException.class,
                () -> service.buscarPorId(1L, 1L));
    }

    @Test
    public void deve_lancar_business_exception_caso_cliente_id_e_prestador_id_divergentes_ao_buscar_por_id(){
        assertThrows(BusinessException.class, () ->
                service.buscarPorId(1L, 2L));
    }

    @Test
    public void deve_atualizar_agendamento() throws NotFoundException, BusinessException {
        var request = criaAgendamento();

        request.setComentario("alt");

        doReturn(request).when(repository).save(any(Agendamento.class));

        var actual = service.atualizarAgendamento(1L, 1L, request);

        assertNotEquals(agendamento, actual);
    }

    @Test
    public void deve_atualizar_status_agendamento() throws NotFoundException, BusinessException {
        var request = criaStatusAgendamento();
        var agendamentoResponse = criaAgendamento();

        request.setId(2);
        agendamentoResponse.setStatus(request);

        doReturn(agendamentoResponse).when(repository).save(any(Agendamento.class));

        var actual = service.atualizarStatusAgendamento(1L, 1L, request);

        assertNotEquals(agendamento, actual);
    }
}
