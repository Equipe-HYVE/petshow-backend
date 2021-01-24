package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.AnimalEstimacaoAgendamento;
import hyve.petshow.domain.AgendamentoAdicional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.DataOutput;
import java.util.stream.Collectors;

@Component
public class AgendamentoConverter implements Converter<Agendamento, AgendamentoRepresentation> {
    @Autowired
    private StatusAgendamentoConverter statusConverter;
    @Autowired
    private ClienteConverter clienteConverter;
    @Autowired
    private PrestadorConverter prestadorConverter;
    @Autowired
    private ServicoDetalhadoConverter servicoDetalhadoConverter;
    @Autowired
    private AvaliacaoConverter avaliacaoConverter;
    @Autowired
    private AnimalEstimacaoConverter animalConverter;
    @Autowired
    private AdicionalConverter adicionalConverter;

    @Override
    public AgendamentoRepresentation toRepresentation(Agendamento domain) {
        var representation = new AgendamentoRepresentation();

        representation.setId(domain.getId());
        representation.setData(domain.getData());
        representation.setComentario(domain.getComentario());
        representation.setEndereco(domain.getEndereco());
        representation.setPrecoFinal(domain.getPrecoFinal());
        representation.setStatusId(domain.getStatus().getId());
        representation.setStatus(statusConverter.toRepresentation(domain.getStatus()));
        representation.setClienteId(domain.getCliente().getId());
        representation.setCliente(clienteConverter.toRepresentation(domain.getCliente()));
        representation.setPrestadorId(domain.getPrestador().getId());
        representation.setPrestador(prestadorConverter.toRepresentation(domain.getPrestador()));
        representation.setServicoDetalhadoId(domain.getServicoDetalhado().getId());
        representation.setServicoDetalhado(servicoDetalhadoConverter.toRepresentation(domain.getServicoDetalhado()));
        representation.setAvaliacao(avaliacaoConverter.toRepresentation(domain.getAvaliacao()));

        representation.setAnimaisAtendidos(
                animalConverter.toRepresentationList(domain.getAnimaisAtendidos().stream()
                        .map(animalEstimacaoAgendamento -> animalEstimacaoAgendamento.getAnimalEstimacao())
                        .collect(Collectors.toList())));

        representation.setAdicionais(
                adicionalConverter.toRepresentationList(domain.getAdicionais().stream()
                        .map(agendamentoAdicional -> agendamentoAdicional.getAdicional())
                        .collect(Collectors.toList())));

        return representation;
    }

    @Override
    public Agendamento toDomain(AgendamentoRepresentation representation) {
        var domain = new Agendamento();

        domain.setId(representation.getId());
        domain.setData(representation.getData());
        domain.setComentario(representation.getComentario());
        domain.setEndereco(representation.getEndereco());
        domain.setPrecoFinal(representation.getPrecoFinal());

        animalConverter.toDomainList(representation.getAnimaisAtendidos()).stream()
                .forEach(animalAtendido -> domain.getAnimaisAtendidos()
                        .add(new AnimalEstimacaoAgendamento(domain, animalAtendido)));

        adicionalConverter.toDomainList(representation.getAdicionais()).stream()
                .forEach(adicionais -> domain.getAdicionais()
                        .add(new AgendamentoAdicional(domain, adicionais)));

        return domain;
    }
}
