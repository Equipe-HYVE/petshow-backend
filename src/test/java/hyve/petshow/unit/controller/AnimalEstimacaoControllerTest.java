package hyve.petshow.unit.controller;

import hyve.petshow.controller.AnimalEstimacaoController;
import hyve.petshow.controller.converter.AnimalEstimacaoConverter;
import hyve.petshow.controller.converter.TipoAnimalEstimacaoConverter;
import hyve.petshow.controller.representation.AnimalEstimacaoRepresentation;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.controller.representation.TipoAnimalEstimacaoRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.domain.TipoAnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.AnimalEstimacaoService;
import hyve.petshow.service.port.TipoAnimalEstimacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AnimalEstimacaoMock.*;
import static hyve.petshow.mock.MensagemMock.criaMensagemRepresentationFalha;
import static hyve.petshow.mock.MensagemMock.criaMensagemRepresentationSucesso;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnimalEstimacaoControllerTest {
	@Mock
	private AnimalEstimacaoConverter converter;
	@Mock
	private AnimalEstimacaoService service;
	@Mock
	private TipoAnimalEstimacaoService tipoService;
	@Mock
	private TipoAnimalEstimacaoConverter tipoConverter;
	@InjectMocks
	private AnimalEstimacaoController controller;

	private AnimalEstimacaoRepresentation animalEstimacaoRepresentation = animalEstimacaoRepresentation();
	private AnimalEstimacao animalEstimacao = criaAnimalEstimacao();

	private List<AnimalEstimacaoRepresentation> animalEstimacaoRepresentationList = singletonList(animalEstimacaoRepresentation);
	private List<AnimalEstimacao> animalEstimacaoList = singletonList(animalEstimacao);
	private Page<AnimalEstimacao> animalEstimacaoPage = new PageImpl<>(animalEstimacaoList);
	private Page<AnimalEstimacaoRepresentation> animalEstimacaoRepresentationPage = new PageImpl<>(animalEstimacaoRepresentationList);

	private Pageable pageable = geraPageable(0, 5);

	private MensagemRepresentation mensagemSucesso = criaMensagemRepresentationSucesso();
	private MensagemRepresentation mensagemFalha = criaMensagemRepresentationFalha();

	private List<TipoAnimalEstimacaoRepresentation> tipoAnimalEstimacaoRepresentationList =
			Arrays.asList(criaTipoAnimalEstimacaoRepresentation());
	private List<TipoAnimalEstimacao> tipoAnimalEstimacaoList = Arrays.asList(criaTipoAnimalEstimacao());

	@BeforeEach
	public void init() throws NotFoundException, BusinessException {
		openMocks(this);

		doReturn(animalEstimacao).when(converter).toDomain(any(AnimalEstimacaoRepresentation.class));
		doReturn(animalEstimacaoRepresentation).when(converter).toRepresentation(any(AnimalEstimacao.class));
		doReturn(animalEstimacaoRepresentationList).when(converter).toRepresentationList(anyList());
		doReturn(animalEstimacaoRepresentationPage).when(converter).toRepresentationPage(any(Page.class));
		doReturn(animalEstimacao).when(service).adicionarAnimalEstimacao(any(AnimalEstimacao.class));
		doReturn(animalEstimacaoPage).when(service).buscarAnimaisEstimacaoPorDono(1L, pageable);
		doThrow(NotFoundException.class).when(service).buscarAnimaisEstimacaoPorDono(2L, pageable);
		doReturn(animalEstimacao).when(service).atualizarAnimalEstimacao(anyLong(), any(AnimalEstimacao.class));
		doThrow(NotFoundException.class).when(service).atualizarAnimalEstimacao(2L, animalEstimacao);
		doReturn(mensagemSucesso).when(service).removerAnimalEstimacao(1L, 1L);
		doReturn(mensagemFalha).when(service).removerAnimalEstimacao(2L, 2L);
		doReturn(tipoAnimalEstimacaoList).when(tipoService).buscarTiposAnimalEstimacao();
		doReturn(tipoAnimalEstimacaoRepresentationList).when(tipoConverter).toRepresentationList(anyList());
	}

	@Test
	public void deve_retornar_animal_salvo() {
		var expected = ResponseEntity.status(HttpStatus.CREATED).body(animalEstimacaoRepresentation);

		var actual = controller.adicionarAnimalEstimacao(animalEstimacaoRepresentation);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_obter_lista_de_animais() throws NotFoundException {
		var expected = ResponseEntity.status(HttpStatus.OK).body(animalEstimacaoRepresentationPage);

		var actual = controller.buscarAnimaisEstimacao(1L, 0, 5);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_lancar_not_found_exception() {
		assertThrows(NotFoundException.class, () -> controller.buscarAnimaisEstimacao(2L, 0, 5));
	}

	@Test
	public void deve_retornar_animal_atualizado() throws NotFoundException, BusinessException {
		var expected = ResponseEntity.status(HttpStatus.OK).body(animalEstimacaoRepresentation);

		var actual = controller.atualizarAnimalEstimacao(1L, animalEstimacaoRepresentation);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_lancar_not_found_exception_quando_animal_nao_existir() {
		assertThrows(NotFoundException.class,
				() -> controller.atualizarAnimalEstimacao(2L, animalEstimacaoRepresentation));
	}

	@Test
	public void removerAnimalEstimacaoTestCase01() throws Exception {
		var expected = ResponseEntity.status(HttpStatus.OK).body(mensagemSucesso);

		var actual = controller.removerAnimalEstimacao(1L, 1L);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_retornar_falha_quando_nao_tiver_o_que_remover() throws Exception {
		var expected = ResponseEntity.status(HttpStatus.OK).body(mensagemFalha);

		var actual = controller.removerAnimalEstimacao(2L, 2L);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_buscar_tipos_animal_estimacao() throws NotFoundException {
		var expected = ResponseEntity.ok(tipoAnimalEstimacaoRepresentationList);

		var actual = controller.buscarTiposAnimalEstimacao();

		assertEquals(expected, actual);
	}
}
