package hyve.petshow.unit.service;

import static hyve.petshow.mock.AgendamentoMock.criaAgendamento;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static hyve.petshow.util.AuditoriaUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AgendamentoRepository;
import hyve.petshow.service.implementation.AgendamentoServiceImpl;

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
        doReturn(agendamentoPage).when(repository).findByClienteIdAndAuditoriaFlagAtivo(anyLong(), anyString(), any(Pageable.class));
        doReturn(agendamentoPage).when(repository).findByPrestadorIdAndAuditoriaFlagAtivo(anyLong(), anyString(), any(Pageable.class));
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

        doReturn(paginaVazia).when(repository).findByClienteIdAndAuditoriaFlagAtivo(1L, ATIVO, pageable);

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

        doReturn(paginaVazia).when(repository).findByPrestadorIdAndAuditoriaFlagAtivo(1L, ATIVO,pageable);

        assertThrows(NotFoundException.class, () ->
                service.buscarAgendamentosPorPrestador(1L, pageable));
    }

    @Test
    public void deve_buscar_agendamento_por_id() throws NotFoundException, BusinessException {
    	doReturn(Optional.ofNullable(agendamento)).when(repository).findByIdAndAuditoriaFlagAtivo(anyLong(), anyString());
        var actual = service.buscarPorIdAtivo(1L, 1L);

        assertEquals(agendamento, actual);
    }

    @Test
    public void deve_lancar_not_found_exception_ao_buscar_agendamento_por_id(){
        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThrows(NotFoundException.class,
                () -> service.buscarPorIdAtivo(1L, 1L));
    }

    @Test
    public void deve_lancar_business_exception_caso_cliente_id_e_prestador_id_divergentes_ao_buscar_por_id(){
        assertThrows(NotFoundException.class, () ->
                service.buscarPorIdAtivo(1L, 2L));
    }

    @Test
    public void deve_atualizar_agendamento() throws NotFoundException, BusinessException {
        var request = criaAgendamento();

        request.getAuditoria().setFlagAtivo(INATIVO);

        doReturn(request).when(repository).save(any(Agendamento.class));
        doReturn(Optional.of(criaAgendamento())).when(repository).findByIdAndAuditoriaFlagAtivo(anyLong(), anyString());
        var actual = service.atualizarAgendamento(1L, 1L, request);

        assertNotEquals(agendamento, actual);
    }

    @Test
    public void deve_atualizar_status_agendamento() throws NotFoundException, BusinessException {
        var request = criaStatusAgendamento();
        var agendamentoResponse = criaAgendamento();

        request.setId(1);
        agendamentoResponse.setStatus(request);
        agendamentoResponse.setAuditoria(atualizaAuditoria(agendamentoResponse.getAuditoria(),INATIVO));

        doReturn(agendamentoResponse).when(repository).save(any(Agendamento.class));
        doReturn(Optional.ofNullable(agendamentoResponse)).when(repository).findByIdAndAuditoriaFlagAtivo(anyLong(), anyString());
        var actual = service.atualizarStatusAgendamento(1L, 1L, request);

        assertNotEquals(agendamento, actual);
    }
    
    @Test
    public void deve_retornar_lista_de_horarios() {
    	var agendamentos = new ArrayList<Agendamento>() {
			private static final long serialVersionUID = 1L;

		{
    		var agendamento1 = new Agendamento();
    		var agendamento2 = new Agendamento();
    		agendamento1.setData(LocalDateTime.of(2021, 01, 01, 10, 0));
    		agendamento2.setData(LocalDateTime.of(2021, 01, 01, 11, 0));
    		add(agendamento1);
    		add(agendamento2);
    	}};
    	
    	var esperado = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

		{
    		add("10:00");
    		add("11:00");
    	}};
    	
    	doReturn(agendamentos).when(repository).findAllByPrestadorAndDateAndAuditoriaFlagAtivo(anyLong(), any());
    	var retorno = service.buscarHorariosAgendamento(1l, LocalDate.of(2021, 01, 01));
    	assertEquals(esperado, retorno);
    }
}
