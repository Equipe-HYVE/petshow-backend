package hyve.petshow.unit.controller.converter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PrestadorConverterTest {
	/*@Autowired
	private PrestadorConverter converter;

	@Test
	public void deve_retornar_prestador_convertido() {
		var esperado = PrestadorMock.criaRepresentationSemSenha();
		var prestador = PrestadorMock.criaPrestador();

		var representation = converter.toRepresentation(prestador);

		assertEquals(esperado, representation);
	}

	@Test
	public void deve_retornar_representacao_convertida() {
		var esperado = PrestadorMock.criaPrestador();
		var prestador = PrestadorMock.criaRepresentation();

		var domain = converter.toDomain(prestador);

		assertEquals(esperado, domain);
	}*/
}
