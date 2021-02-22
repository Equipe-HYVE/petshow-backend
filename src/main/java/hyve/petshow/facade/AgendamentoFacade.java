package hyve.petshow.facade;

import hyve.petshow.controller.converter.AgendamentoConverter;
import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.domain.*;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;

@Component
public class AgendamentoFacade {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PrestadorService prestadorService;
    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private ServicoDetalhadoService servicoDetalhadoService;
    @Autowired
    private AnimalEstimacaoService animalEstimacaoService;
    @Autowired
    private AdicionalService adicionalService;
    @Autowired
    private StatusAgendamentoService statusAgendamentoService;
    @Autowired
    private AgendamentoConverter agendamentoConverter;

    public AgendamentoRepresentation adicionarAgendamento(AgendamentoRepresentation request) throws Exception {
        var agendamento = agendamentoConverter.toDomain(request);
        var cliente = clienteService.buscarPorId(request.getClienteId());
        var prestador = prestadorService.buscarPorId(request.getPrestadorId());
        var servicoDetalhado = servicoDetalhadoService.buscarPorId(request.getServicoDetalhadoId());
        var auditoria = geraAuditoriaInsercao(Optional.of(request.getClienteId()));
        var status = new StatusAgendamento(1, "AGENDADO");
        var animaisAtendidos = this.processaAnimaisEstimacaoAgendamento(
                request.getClienteId(), request.getAnimaisAtendidosIds(), agendamento);
        var adicionais = this.processaAgendamentoAdicional(
                request.getServicoDetalhadoId(), request.getAdicionaisIds(), agendamento);
        var precoFinal = new BigDecimal(0);

        /*TODO: UTILIZAR LAMBDA FUNCTION COMO MELHORIA DE PERFOMANCE*/
        for (var animal : animaisAtendidos) {
            for(var tipoAnimal : servicoDetalhado.getTiposAnimaisAceitos()){
                if(animal.getAnimalEstimacao().getTipo().equals(tipoAnimal.getTipoAnimalEstimacao()))
                    precoFinal = precoFinal.add(tipoAnimal.getPreco());
            }
        }

        for(var adicional : adicionais){
            precoFinal = precoFinal.add(adicional.getAdicional().getPreco());
        }

        /*TODO: INSERIR REGRA PARA ENDEREÇO DE PRESTADOR*/
        agendamento.setEndereco(prestador.getEndereco());
        agendamento.setPrecoFinal(precoFinal);
        agendamento.setAuditoria(auditoria);
        agendamento.setStatus(status);
        agendamento.setCliente(cliente);
        agendamento.setPrestador(prestador);
        agendamento.setServicoDetalhado(servicoDetalhado);
        agendamento.setAnimaisAtendidos(animaisAtendidos);
        agendamento.setAdicionais(adicionais);
        agendamento = agendamentoService.adicionarAgendamento(agendamento);

        var representation = agendamentoConverter.toRepresentation(agendamento);

        return representation;
    }

    public Boolean atualizarStatusAgendamento(Long id, Long prestadorId, Integer statusId)
            throws NotFoundException, BusinessException {
        var statusAgendamento =  statusAgendamentoService.buscarStatusAgendamento(statusId);

        var agendamento = agendamentoService.atualizarStatusAgendamento(id, prestadorId, statusAgendamento);

        return agendamento.getStatus().getId() == statusId;
    }

    private List<AnimalEstimacaoAgendamento> processaAnimaisEstimacaoAgendamento(Long donoId,
                                                                                 List<Long> animaisEstimacaoIds,
                                                                                 Agendamento agendamento) throws NotFoundException {
        var animaisBuscados = animalEstimacaoService
                .buscarAnimaisEstimacaoPorIds(donoId, animaisEstimacaoIds);
        var animaisAtendidos = new ArrayList<AnimalEstimacaoAgendamento>();

        for (AnimalEstimacao animalEstimacao : animaisBuscados) {
            animaisAtendidos.add(new AnimalEstimacaoAgendamento(agendamento, animalEstimacao));
        }

        return animaisAtendidos;
    }

    private List<AdicionalAgendamento> processaAgendamentoAdicional(Long servicoDetalhadoId, List<Long> adicionaisIds,
                                                                    Agendamento agendamento) throws Exception {
        var adicionais = new ArrayList<AdicionalAgendamento>();

        if(! adicionaisIds.isEmpty()) {
            var adicionaisBuscados = adicionalService
                    .buscarAdicionaisPorIds(servicoDetalhadoId, adicionaisIds);

            for (Adicional adicional : adicionaisBuscados) {
                adicionais.add(new AdicionalAgendamento(agendamento, adicional));
            }
        }

        return adicionais;
    }
}
