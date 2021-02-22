package hyve.petshow.mock;

import hyve.petshow.controller.representation.AnimalEstimacaoRepresentation;
import hyve.petshow.controller.representation.TipoAnimalEstimacaoRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.TipoAnimalEstimacao;
import hyve.petshow.domain.embeddables.Auditoria;

import java.util.Collections;

public class AnimalEstimacaoMock {
    public static AnimalEstimacao criaAnimalEstimacao(){
        AnimalEstimacao animalEstimacao = new AnimalEstimacao();
        Cliente cliente = new Cliente();

        cliente.setAnimaisEstimacao(Collections.emptyList());

        animalEstimacao.setId(1L);
        animalEstimacao.setNome("pedrinho");
        animalEstimacao.setFoto("hahah");
        animalEstimacao.setTipo(criaTipoAnimalEstimacao());
        animalEstimacao.setAuditoria(new Auditoria());
        animalEstimacao.setDonoId(1L);

        return animalEstimacao;
    }

    public static AnimalEstimacao animalEstimacaoAlt(){
        AnimalEstimacao animalEstimacao = new AnimalEstimacao();

        animalEstimacao.setId(1L);
        animalEstimacao.setNome("andrezinho");
        animalEstimacao.setFoto("aaaaa");
        animalEstimacao.setTipo(criaTipoAnimalEstimacao());
        animalEstimacao.setAuditoria(new Auditoria());
        animalEstimacao.setDonoId(1L);

        return animalEstimacao;
    }

    public static AnimalEstimacaoRepresentation animalEstimacaoRepresentation(){
        AnimalEstimacaoRepresentation animalEstimacaoRepresentation = new AnimalEstimacaoRepresentation();

        animalEstimacaoRepresentation.setNome("pedrinho");
        animalEstimacaoRepresentation.setFoto("hahah");
        animalEstimacaoRepresentation.setTipo(criaTipoAnimalEstimacaoRepresentation());
        animalEstimacaoRepresentation.setDonoId(1L);

        return animalEstimacaoRepresentation;
    }

    public static TipoAnimalEstimacao criaTipoAnimalEstimacao(){
        var tipoAnimalEstimacao = new TipoAnimalEstimacao();

        tipoAnimalEstimacao.setId(1);
        tipoAnimalEstimacao.setNome("GATO");

        return tipoAnimalEstimacao;
    }

    public static TipoAnimalEstimacaoRepresentation criaTipoAnimalEstimacaoRepresentation(){
        var tipoAnimalEstimacaoRepresentation = new TipoAnimalEstimacaoRepresentation();

        tipoAnimalEstimacaoRepresentation.setId(1);
        tipoAnimalEstimacaoRepresentation.setNome("GATO");

        return tipoAnimalEstimacaoRepresentation;
    }
}
