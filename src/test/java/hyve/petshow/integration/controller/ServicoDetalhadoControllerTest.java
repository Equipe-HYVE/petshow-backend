package hyve.petshow.integration.controller;

import static hyve.petshow.mock.ContaMock.contaCliente;
import static hyve.petshow.mock.ContaMock.contaPrestador;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import hyve.petshow.controller.converter.AvaliacaoConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.Servico;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.embeddables.CriteriosAvaliacao;
import hyve.petshow.repository.AvaliacaoRepository;
import hyve.petshow.repository.ClienteRepository;
import hyve.petshow.repository.PrestadorRepository;
import hyve.petshow.repository.ServicoDetalhadoRepository;
import hyve.petshow.repository.ServicoRepository;
import hyve.petshow.service.port.ServicoDetalhadoService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServicoDetalhadoControllerTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate template;
	@Autowired
	private ServicoDetalhadoRepository repository;
	@Autowired
	private ServicoRepository servicoRepository;
	@Autowired
	private PrestadorRepository prestadorRepository;
	@Autowired
	private ServicoDetalhadoService service;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private ServicoDetalhadoConverter converter;
	@Autowired
	private AvaliacaoConverter avaliacaoConverter;
	private String url;
	
	private Prestador prestador;
	private Cliente cliente;
	private Servico tipoServico;
	private ServicoDetalhado servico;
	
	@BeforeEach
	public void init() {
		tipoServico = new Servico();
		tipoServico.setNome("Banho e tosa");
		servicoRepository.save(tipoServico);
		
		prestador = new Prestador(contaPrestador());
		prestador.setId(null);
		prestadorRepository.save(prestador);
		
		cliente = new Cliente(contaCliente());
		cliente.setId(null);
		clienteRepository.save(cliente);
		
		servico = new ServicoDetalhado();
		servico.setTipo(tipoServico);
		servico.setPrestadorId(prestador.getId());
		servico.setPreco(BigDecimal.valueOf(30));
		
		
		
		url = "http://localhost:" + port + "/prestador/" + prestador.getId() + "/servico-detalhado";
	}
	
	@AfterEach
	public void limpaRepositorio() {
		avaliacaoRepository.deleteAll();
		repository.deleteAll();
		servicoRepository.deleteAll();
		prestadorRepository.deleteAll();
		clienteRepository.deleteAll();
	}
	
	@Test
	public void deve_retornar_lista_com_servicos() {
		service.adicionarServicoDetalhado(servico);
		var uri = UriComponentsBuilder.fromHttpUrl(this.url)
				.queryParam("pagina", 0)
				.queryParam("quantidadeItens", 5)
				.toUriString();
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void deve_retornar_erro_de_lista_vazia() throws Exception {
		var uri = UriComponentsBuilder.fromHttpUrl(this.url)
				.queryParam("pagina", 0)
				.queryParam("quantidadeItens", 5)
				.toUriString();
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void deve_retornar_por_tipo_de_servico() throws Exception {
		service.adicionarServicoDetalhado(servico);
		var httpUrl = "http://localhost:"+this.port+"/servico-detalhado/tipo-servico/"+tipoServico.getId();
		var uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
		.queryParam("pagina", 0)
		.queryParam("quantidadeItens", 5)
		.toUriString();
		
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void deve_retornar_erro_por_nao_encontrar_tipo() throws Exception {
		var httpUrl = "http://localhost:"+this.port+"/servico-detalhado/tipo-servico/"+(tipoServico.getId() + 1);
		var uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
				.queryParam("pagina", 0)
				.queryParam("quantidadeItens", 5)
				.toUriString();
		
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void deve_retornar_lista_vazia_de_avaliacoes() {
		service.adicionarServicoDetalhado(servico);
		var httpUrl = "http://localhost:"+this.port + "/servico-detalhado/"+servico.getId()+"/avaliacoes";
		var uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
				.queryParam("pagina", 0)
				.queryParam("quantidadeItens", 5)
				.toUriString();
		
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void deve_adicionar_servico_detalhado() throws Exception {
		var representation = converter.toRepresentation(servico);
		var uri = new URI(this.url);
		var body = new HttpEntity<>(representation, new HttpHeaders());
		var response = template.postForEntity(uri, body, ServicoDetalhadoRepresentation.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	public void deve_remover_servico_detalhado() throws Exception {
		service.adicionarServicoDetalhado(servico);
		var uri = new URI(this.url + "/"+servico.getId());
		template.delete(uri);
		
		assertFalse(repository.existsById(servico.getId()));
	}
	
	@Test
	public void deve_buscar_servico() throws Exception {
		service.adicionarServicoDetalhado(servico);
		var uri = new URI(this.url + "/"+servico.getId());
		var response = template.getForEntity(uri, ServicoDetalhadoRepresentation.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void deve_atualizar_servico() throws Exception {
		var servicoAdd = service.adicionarServicoDetalhado(servico);
		var uri = new URI(this.url + "/"+servico.getId());
		servicoAdd.setPreco(BigDecimal.valueOf(25));
		var body = new HttpEntity<>(servicoAdd, new HttpHeaders());
		template.exchange(uri, HttpMethod.PUT, body, Void.class);
		
		var busca = repository.findById(servicoAdd.getId()).get();;
		assertEquals(25, busca.getPreco().doubleValue() , 0.001);
	}
	
	@Test
	public void deve_adicionar_avaliacao() throws Exception {
		var servicoAdd = service.adicionarServicoDetalhado(servico);
		var uri = new URI(this.url + "/"+servico.getId()+ "/avaliacoes");
		
		var avaliacao = new Avaliacao();
		avaliacao.setServicoAvaliadoId(servicoAdd.getId());
		avaliacao.setCliente(cliente);
		avaliacao.setCriteriosAvaliacao(new CriteriosAvaliacao());
		
		var representation = avaliacaoConverter.toRepresentation(avaliacao);
		var body = new HttpEntity<>(representation, new HttpHeaders());
		
		var response = template.postForEntity(uri, body, ServicoDetalhadoRepresentation.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	public void deve_retornar_lista_de_avaliacoes() {
		var servicoAdd = service.adicionarServicoDetalhado(servico);
		var avaliacao = new Avaliacao();
		avaliacao.setServicoAvaliadoId(servicoAdd.getId());
		avaliacao.setCliente(cliente);
		avaliacao.setCriteriosAvaliacao(new CriteriosAvaliacao());
		
		avaliacaoRepository.save(avaliacao);
		
		var httpUrl = "http://localhost:"+this.port + "/servico-detalhado/"+servico.getId()+"/avaliacoes";
		var uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
				.queryParam("pagina", 0)
				.queryParam("quantidadeItens", 5)
				.toUriString();
		
		var response = template.getForEntity(uri, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
}