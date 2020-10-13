package hyve.petshow.unit.controller.representation;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import hyve.petshow.controller.representation.PrestadorRepresentation;
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PrestadorRepresentationTest {
	@Test
    public void deve_ter_os_metodos_implementados(){
        //dado
		final Class<PrestadorRepresentation> prestador = PrestadorRepresentation.class;

        //entao
        assertPojoMethodsFor(prestador).areWellImplemented();
    }
}