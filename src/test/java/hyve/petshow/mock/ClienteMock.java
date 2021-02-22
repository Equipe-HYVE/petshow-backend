package hyve.petshow.mock;

import hyve.petshow.controller.representation.ClienteRepresentation;
import hyve.petshow.domain.Cliente;

import java.util.Arrays;
import java.util.Collections;

import static hyve.petshow.mock.AnimalEstimacaoMock.animalEstimacaoRepresentation;
import static hyve.petshow.mock.AnimalEstimacaoMock.criaAnimalEstimacao;
import static hyve.petshow.mock.ContaMock.contaCliente;

public class ClienteMock {
	public static Cliente criaCliente() {
		return new Cliente(contaCliente(), Arrays.asList(criaAnimalEstimacao()));
	}

	public static ClienteRepresentation clienteRepresentation() {
		var cliente = criaCliente();
		var clienteRepresentation = new ClienteRepresentation();

		clienteRepresentation.setId(cliente.getId());
		clienteRepresentation.setNome(cliente.getNome());
		clienteRepresentation.setNomeSocial(cliente.getNomeSocial());
		clienteRepresentation.setCpf(cliente.getCpf());
		clienteRepresentation.setTelefone(cliente.getTelefone());
		clienteRepresentation.setTipo(cliente.getTipo().getTipo());
		clienteRepresentation.setFoto(cliente.getFoto());
		clienteRepresentation.setMediaAvaliacao(cliente.getMediaAvaliacao());
		clienteRepresentation.setEndereco(cliente.getEndereco());
		clienteRepresentation.setLogin(cliente.getLogin());
		clienteRepresentation.setGeolocalizacao(cliente.getGeolocalizacao());
		clienteRepresentation.setAnimaisEstimacao(Collections.singletonList(animalEstimacaoRepresentation()));

		return clienteRepresentation;
	}
}
