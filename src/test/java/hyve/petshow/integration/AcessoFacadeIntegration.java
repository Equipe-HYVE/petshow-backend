package hyve.petshow.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import hyve.petshow.controller.representation.EmpresaRepresentation;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Login;
import hyve.petshow.facade.AcessoFacade;
import hyve.petshow.repository.AcessoRepository;
import hyve.petshow.repository.EmpresaRepository;
import hyve.petshow.repository.PrestadorRepository;

@Disabled("Destativado enquanto não funcionar em PRD")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AcessoFacadeIntegration {
	@Autowired
	private AcessoFacade facade;
	@Autowired
	private AcessoRepository repository;
	@Autowired
	private PrestadorRepository prestadorRepository;
	@Autowired
	private EmpresaRepository empresaRepository;

	private PrestadorRepresentation representation;

	@BeforeEach
	public void init() {
		representation = new PrestadorRepresentation();
		representation.setNome("Guilherme");
		representation.setNomeSocial("Gauso");
		representation.setCpf("1251251215");
		representation.setTipo(2);
		representation.setGeolocalizacao(null);
		representation.setIsAtivo(true);

		var login = new Login();
		login.setEmail("labut@gmail.com");
		login.setSenha("1234");
		representation.setLogin(login);

		var endereco = new Endereco();
		endereco.setLogradouro("alsdjkklssssssssadg");
		endereco.setCep("0298323");
		endereco.setBairro("ssssss");
		endereco.setCidade("Ariquemes");
		endereco.setEstado("RO");
		endereco.setNumero("12");
		endereco.setComplemento("445");

		representation.setEndereco(endereco);

		var empresa = new EmpresaRepresentation();
		empresa.setNome("AAAAAA");
		empresa.setRazaoSocial("BBBBB");
		empresa.setCnpj("00000000000000");

		var enderecoEmpresa = new Endereco();
		enderecoEmpresa.setLogradouro("aljgsldgjadg");
		enderecoEmpresa.setCep("235255");
		empresa.setEndereco(enderecoEmpresa);

		representation.setEmpresa(empresa);

	}

	@AfterEach
	public void limpaRepositories() {
		prestadorRepository.deleteAll();
		empresaRepository.deleteAll();
		repository.deleteAll();
	}

	@Test
	public void deve_inserir_prestador() throws Exception {

		facade.salvaPrestador(representation);

		assertFalse(repository.findAll().isEmpty());

	}

	@Test
	public void deve_inserir_empresa() throws Exception {
		facade.salvaPrestador(representation);

		assertFalse(empresaRepository.findAll().isEmpty());
	}

	@Test
	public void deve_adicionar_empresa() throws Exception {
		facade.salvaPrestador(representation);

		var prestador = prestadorRepository.findAll().stream().findFirst().get();

		assertTrue(prestador.getEmpresa() != null);
	}
	
	@Test
	public void deve_inserir_nome_da_empresa() throws Exception {
		facade.salvaPrestador(representation);
		
		var prestador = prestadorRepository.findAll().stream().findFirst().get();
		
		assertNotNull(prestador.getEmpresa().getNome());
		assertNotNull(prestador.getEmpresa().getRazaoSocial());
		assertTrue(empresaRepository.findAll().size() == 1);
	}
}
