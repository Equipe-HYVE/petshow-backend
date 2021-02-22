package hyve.petshow.unit.service;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AnimalEstimacaoRepository;
import hyve.petshow.service.implementation.AnimalEstimacaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static hyve.petshow.mock.AnimalEstimacaoMock.criaAnimalEstimacao;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnimalEstimacaoServiceTest {
    @Mock
    private AnimalEstimacaoRepository repository;
    @InjectMocks
    private AnimalEstimacaoServiceImpl service;

    private AnimalEstimacao animalEstimacao = criaAnimalEstimacao();
    private List<AnimalEstimacao> animaisEstimacao = singletonList(animalEstimacao);
    private Page<AnimalEstimacao> animalEstimacaoPage = new PageImpl<>(animaisEstimacao);
    private Pageable pageable = geraPageable(0, 5);

    @BeforeEach
    public void init() {
        openMocks(this);

        doReturn(animalEstimacao).when(repository).save(animalEstimacao);
        doReturn(Optional.of(animalEstimacao)).when(repository).findById(1L);
        doReturn(animaisEstimacao).when(repository).findAll();
        doReturn(animalEstimacaoPage).when(repository).findByDonoId(1L, pageable);
        doReturn(animaisEstimacao).when(repository).findByDonoIdAndIdIn(anyLong(), anyList());
        doNothing().when(repository).delete(any(AnimalEstimacao.class));
    }

    @Test
    public void deve_retornar_animal_com_sucesso() {
        var actual = service.adicionarAnimalEstimacao(animalEstimacao);

        assertNotNull(actual);
    }

    @Test
    public void deve_trazer_animal_correto() throws NotFoundException {
        var actual = service.buscarAnimalEstimacaoPorId(1L);

        assertNotNull(actual);
    }

    @Test
    public void deve_retornar_lista_de_animais() throws NotFoundException {
        var actual = service.buscarAnimaisEstimacaoPorDono(1L, pageable);

        assertNotNull(actual);
    }

    @Test
    public void deve_retornar_animal_atualizado() throws NotFoundException, BusinessException {
        var animalEstimacaoRequest = animalEstimacao;
        animalEstimacaoRequest.setNome("arnaldo antunes");

        doReturn(animalEstimacaoRequest).when(repository).save(animalEstimacaoRequest);

        var actual = service.atualizarAnimalEstimacao(1L, animalEstimacaoRequest);

        assertEquals(animalEstimacao.getNome(), actual.getNome());
    }

    @Test
    public void deve_retornar_vazio_se_animal_nao_existir() {
        assertThrows(NotFoundException.class, () -> service.atualizarAnimalEstimacao(3L, animalEstimacao));
    }

    @Test
    public void deve_retornar_mensagem_sucesso_ao_remover_elemento() throws Exception {
        var mensagem = service.removerAnimalEstimacao(1L, 1L);

        doReturn(FALSE).when(repository).existsById(1L);

        assertAll(
                () -> assertEquals(MensagemRepresentation.MENSAGEM_SUCESSO, mensagem.getMensagem()),
                () -> assertTrue(mensagem.getSucesso()));
    }

    @Test
    public void deve_retornar_excecao_de_nao_encontrado() {
    	assertThrows(NotFoundException.class, () -> service.buscarAnimalEstimacaoPorId(3L));
    }
    
    @Test
    public void deve_retornar_excecao_por_lista_nao_encontrada() {
        doReturn(Page.empty()).when(repository).findByDonoId(2L, pageable);

        assertThrows(NotFoundException.class, () -> service.buscarAnimaisEstimacaoPorDono(2L, pageable));
    }
    
    @Test
    public void deve_retornar_excecao_por_donos_diferentes() {
    	var animalRequest = criaAnimalEstimacao();
    	animalRequest.setDonoId(2L);

    	assertThrows(BusinessException.class, () -> service.atualizarAnimalEstimacao(1L, animalRequest));
    }
    
    @Test
    public void deve_retornar_excecao_por_donos_diferentes_delecao() {
        var animalRequest = criaAnimalEstimacao();
        animalRequest.setDonoId(2L);

        assertThrows(BusinessException.class, () -> service.removerAnimalEstimacao(1L, animalRequest.getDonoId()));
    }
    
    @Test
    public void deve_retornar_mensagem_de_falha() throws BusinessException, NotFoundException {
    	doReturn(TRUE).when(repository).existsById(1L);

    	var removerAnimalEstimacao = service.removerAnimalEstimacao(1L, animalEstimacao.getDonoId());

    	assertEquals(MensagemRepresentation.MENSAGEM_FALHA, removerAnimalEstimacao.getMensagem());
    }

    @Test
    public void deve_retornar_lista_animais_de_estimacao() throws NotFoundException {
        var actual = service.buscarAnimaisEstimacaoPorIds(1L, Arrays.asList(1L, 2L));

        assertEquals(animaisEstimacao, actual);
    }

    @Test
    public void deve_lancar_not_found_exception_ao_nao_encontrar_animais() throws NotFoundException {
        doReturn(Collections.emptyList()).when(repository).findByDonoIdAndIdIn(1L, Arrays.asList(1L, 2L));

        assertThrows(NotFoundException.class, () ->
                service.buscarAnimaisEstimacaoPorIds(1L, Arrays.asList(1L, 2L)));
    }
}
