package hyve.petshow.unit.service;


import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Prestador;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.PrestadorRepository;
import hyve.petshow.service.implementation.PrestadorServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static hyve.petshow.mock.PrestadorMock.criaPrestador;
import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PrestadorServiceTest {
    @Mock
    private PrestadorRepository repository;
    @InjectMocks
    private PrestadorServiceImpl service;

    private Prestador prestador = criaPrestador();

    @BeforeEach
    public void init() {
        initMocks(this);

        doReturn(Optional.of(prestador)).when(repository).findById(1L);
        doReturn(Optional.of(prestador)).when(repository).findByEmail(anyString());
    }

    @AfterEach
    public void afterEach(){
        prestador.getAuditoria().setFlagAtivo(ATIVO);
    }

    @Test
    public void deve_atualizar_conta() throws Exception {
        var prestadorRequest = prestador;
        prestadorRequest.setTelefone("15152020");

        doReturn(prestadorRequest).when(repository).save(prestadorRequest);

        var actual = service.atualizarConta(1L, prestadorRequest);

        assertEquals(actual.getTelefone(), prestador.getTelefone());
    }

    @Test
    public void deve_encontrar_conta_correta() throws Exception {
        var prestador = service.buscarPorId(1L);

        assertEquals(this.prestador, prestador);
    }

    @Test
    public void deve_retornar_excecao_por_pessoa_nao_encontrada() {
        assertThrows(Exception.class, () -> service.buscarPorId(5L));
    }

    @Test
    public void deve_remover_elemento_e_retornar_mensagem_sucesso() throws Exception {
        doReturn(prestador).when(repository).save(prestador);

        var mensagem = service.desativarConta(1L);

        assertAll(
                () -> assertEquals(MensagemRepresentation.MENSAGEM_SUCESSO, mensagem.getMensagem()),
                () -> assertTrue(mensagem.getSucesso()));
    }

    @Test
    public void deve_retornar_excecao_quando_nao_encontrar_para_atualizar() {
    	assertThrows(NotFoundException.class, () -> service.atualizarConta(20l, new Prestador()));
    }
    
    @Test
    public void deve_retornar_mensagem_de_erro_em_execucao_de_delecao() throws Exception {
        var prestadorAtivo = criaPrestador();

        doReturn(prestadorAtivo).when(repository).save(prestador);

        var mensagem = service.desativarConta(1L);

        assertEquals(MensagemRepresentation.MENSAGEM_FALHA, mensagem.getMensagem());
    }
    
    @Test
    public void deve_encontrar_por_email() {
    	var prestador = service.buscarPorEmail("teste@teste");

    	assertNotNull(prestador);
    }
}
