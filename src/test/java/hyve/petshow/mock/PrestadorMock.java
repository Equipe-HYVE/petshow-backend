package hyve.petshow.mock;

import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.domain.Prestador;

import java.util.Arrays;

import static hyve.petshow.mock.ContaMock.contaPrestador;
import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhado;
import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhadoRepresentationList;

public class PrestadorMock {
    public static Prestador criaPrestador() {
        return new Prestador(contaPrestador(), Arrays.asList(criaServicoDetalhado()));
    }

    public static PrestadorRepresentation criaPrestadorRepresentation() {
        var prestador = criaPrestador();
        var prestadorRepresentation = new PrestadorRepresentation();

        prestadorRepresentation.setId(prestador.getId());
        prestadorRepresentation.setNome(prestador.getNome());
        prestadorRepresentation.setNomeSocial(prestador.getNomeSocial());
        prestadorRepresentation.setCpf(prestador.getCpf());
        prestadorRepresentation.setTelefone(prestador.getTelefone());
        prestadorRepresentation.setTipo(prestador.getTipo().getTipo());
        prestadorRepresentation.setFoto(prestador.getFoto());
        prestadorRepresentation.setMediaAvaliacao(prestador.getMediaAvaliacao());
        prestadorRepresentation.setEndereco(prestador.getEndereco());
        prestadorRepresentation.setLogin(prestador.getLogin());
        prestadorRepresentation.setGeolocalizacao(prestador.getGeolocalizacao());
        prestadorRepresentation.setServicos(criaServicoDetalhadoRepresentationList());

        return prestadorRepresentation;
    }
}