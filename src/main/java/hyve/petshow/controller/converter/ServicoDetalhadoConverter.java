package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.PrecoPorTipoRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;



@Component
public class ServicoDetalhadoConverter implements Converter<ServicoDetalhado, ServicoDetalhadoRepresentation>{
	@Autowired
    private ServicoConverter servicoConverter;
	@Autowired
    private TipoAnimalEstimacaoConverter tipoAnimalEstimacaoConverter;
	@Autowired
	private AdicionalConverter adicionalConverter;
	
	@Override
    public ServicoDetalhadoRepresentation toRepresentation(ServicoDetalhado domain) {
    	var representation = new ServicoDetalhadoRepresentation();
    	
    	representation.setId(domain.getId());
        representation.setTipo(servicoConverter.toRepresentation(domain.getTipo()));
        representation.setPrestadorId(domain.getPrestadorId());
        representation.setMediaAvaliacao(domain.getMediaAvaliacao());
        
        var precosPorTipo = Optional.ofNullable(domain.getTiposAnimaisAceitos())
        .map(tiposAceitos -> tiposAceitos.stream()
        		.map(tipoAceito -> PrecoPorTipoRepresentation.builder()
        				.preco(tipoAceito.getPreco())
        				.tipoAnimal(tipoAnimalEstimacaoConverter.toRepresentation(tipoAceito.getTipoAnimalEstimacao()))
        				.build()).collect(Collectors.toList())).orElse(new ArrayList<>());
        
        representation.setPrecoPorTipo(precosPorTipo);

        representation.setAdicionais(adicionalConverter.toRepresentationList(domain.getAdicionais()));
        return representation;
    }

    @Override
    public ServicoDetalhado toDomain(ServicoDetalhadoRepresentation representation) {
    	var domain = new ServicoDetalhado();
        
    	domain.setId(representation.getId());
        domain.setTipo(servicoConverter.toDomain(representation.getTipo()));
        domain.setPrestadorId(representation.getPrestadorId());
        domain.setMediaAvaliacao(representation.getMediaAvaliacao());
        
        var tiposAnimaisAceitos = Optional.ofNullable(representation.getPrecoPorTipo())
        		.map(precos -> precos.stream()
        				.map(preco -> new ServicoDetalhadoTipoAnimalEstimacao(domain, tipoAnimalEstimacaoConverter.toDomain(preco.getTipoAnimal()), preco.getPreco()))
        				.collect(Collectors.toList()))
        		.orElse(new ArrayList<>());
        
        
        domain.setTiposAnimaisAceitos(tiposAnimaisAceitos);
        domain.setAdicionais(adicionalConverter.toDomainList(representation.getAdicionais()));

        return domain;
    }
}
