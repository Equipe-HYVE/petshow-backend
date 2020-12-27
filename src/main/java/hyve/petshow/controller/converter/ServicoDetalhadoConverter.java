package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.PrecoPorTipoRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.domain.TipoAnimalEstimacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class ServicoDetalhadoConverter implements Converter<ServicoDetalhado, ServicoDetalhadoRepresentation>{
	@Autowired
    private ServicoConverter servicoConverter;
	@Autowired
	private AvaliacaoConverter avaliacaoConverter;
	@Autowired
<<<<<<< HEAD
    private TipoAnimalEstimacaoConverter tipoAnimalEstimacaoConverter;
=======
	private AdicionalConverter adicionalConverter;
>>>>>>> 205f6d1885fb0d324b30f2dcbf89d541b8164383
	
	@Override
    public ServicoDetalhadoRepresentation toRepresentation(ServicoDetalhado domain) {
    	var representation = new ServicoDetalhadoRepresentation();
    	
    	representation.setId(domain.getId());
        representation.setTipo(servicoConverter.toRepresentation(domain.getTipo()));
        representation.setPrestadorId(domain.getPrestadorId());
        representation.setAvaliacoes(avaliacaoConverter.toRepresentationList(domain.getAvaliacoes()));
        representation.setMediaAvaliacao(domain.getMediaAvaliacao());
<<<<<<< HEAD
        domain.getTiposAnimaisAceitos().stream()
            .forEach(tipoAnimalAceito -> {
                representation.getPrecoPorTipo().add(PrecoPorTipoRepresentation.builder()
                                .preco(tipoAnimalAceito.getPreco())
                                .tipoAnimal(tipoAnimalEstimacaoConverter.toRepresentation(tipoAnimalAceito.getTipoAnimalEstimacao()))
                                .build()
                );
            });

       return representation;
=======
        representation.setAdicionais(adicionalConverter.toRepresentationList(domain.getAdicionais()));
        return representation;
>>>>>>> 205f6d1885fb0d324b30f2dcbf89d541b8164383
    }

    @Override
    public ServicoDetalhado toDomain(ServicoDetalhadoRepresentation representation) {
    	var domain = new ServicoDetalhado();
        
    	domain.setId(representation.getId());
        domain.setTipo(servicoConverter.toDomain(representation.getTipo()));
        domain.setPrestadorId(representation.getPrestadorId());
        domain.setAvaliacoes(avaliacaoConverter.toDomainList(representation.getAvaliacoes()));
        domain.setMediaAvaliacao(representation.getMediaAvaliacao());
<<<<<<< HEAD
        representation.getPrecoPorTipo().stream()
                .forEach(precoPorTipo -> domain.getTiposAnimaisAceitos().add(
                        new ServicoDetalhadoTipoAnimalEstimacao(domain,
                                tipoAnimalEstimacaoConverter.toDomain(precoPorTipo.getTipoAnimal()),
                                precoPorTipo.getPreco())
                ));
=======
        domain.setAdicionais(adicionalConverter.toDomainSet(representation.getAdicionais()));
>>>>>>> 205f6d1885fb0d324b30f2dcbf89d541b8164383

        return domain;
    }
}
