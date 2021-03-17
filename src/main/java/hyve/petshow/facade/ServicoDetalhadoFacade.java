package hyve.petshow.facade;

import java.util.ArrayList;
import java.util.List;

import hyve.petshow.domain.ServicoDetalhado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import hyve.petshow.controller.converter.AdicionalConverter;
import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.AdicionalRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.AdicionalService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.service.port.ServicoDetalhadoService;
import hyve.petshow.service.port.TipoAnimalEstimacaoService;

@Component
public class ServicoDetalhadoFacade {
    @Autowired
    private ServicoDetalhadoService servicoDetalhadoService;
    @Autowired
    private PrestadorService prestadorService;
    @Autowired
    private ServicoDetalhadoConverter servicoDetalhadoConverter;
    @Autowired
    private PrestadorConverter prestadorConverter;
    @Autowired
    private AdicionalService adicionalService;
    @Autowired
    private AdicionalConverter adicionalConverter;
    @Autowired
    private TipoAnimalEstimacaoService tipoAnimalEstimacaoService;

    public Page<ServicoDetalhadoRepresentation> buscarServicosDetalhadosPorTipoServico(Pageable pageable,
                                                                                       ServicoDetalhadoFilter filtragem) throws Exception {
        var servicosDetalhados = servicoDetalhadoConverter.toRepresentationPage(
                servicoDetalhadoService.buscarServicosDetalhadosPorTipoServico(pageable, filtragem));

        for (ServicoDetalhadoRepresentation servico : servicosDetalhados) {
            var prestador = prestadorConverter.toRepresentation(
                    prestadorService.buscarPorId(servico.getPrestadorId()));

            servico.setPrestador(prestador);
        }

        return servicosDetalhados;
    }
    
	public List<ServicoDetalhadoRepresentation> buscarServicosDetalhadosPorTipoServico(ServicoDetalhadoFilter filtragem)
			throws Exception {
		var servicos = servicoDetalhadoService.buscarServicosDetalhadosPorTipoServico(filtragem);
		var representation = servicoDetalhadoConverter.toRepresentationList(servicos);

		for (var servicoRepresentation : representation) {
			var prestador = prestadorService.buscarPorId(servicoRepresentation.getPrestadorId());
			var prestadorRepresentation = prestadorConverter.toRepresentation(prestador);
			servicoRepresentation.setPrestador(prestadorRepresentation);
		}

		return representation;
	}

    public ServicoDetalhadoRepresentation buscarPorPrestadorIdEServicoId(Long prestadorId, Long servicoId) throws Exception {
        var servico = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestadorId, servicoId);
        var prestador = prestadorConverter.toRepresentation(prestadorService.buscarPorId(servico.getPrestadorId()));
        var representation = servicoDetalhadoConverter.toRepresentation(servico);

        representation.setPrestador(prestador);

        return representation;
    }
    
    public List<AdicionalRepresentation> buscarAdicionais(Long prestadorId, Long servicoId, Boolean apenasAtivos) throws Exception {
    	var prestador = prestadorService.buscarPorId(prestadorId);
    	var servicoDetalhado = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestador.getId(), servicoId);
        var adicionais = apenasAtivos ? adicionalService.buscarAtivosPorServicoDetalhado(servicoDetalhado.getId()) :
                adicionalService.buscarPorServicoDetalhado(servicoDetalhado.getId());

    	return adicionalConverter.toRepresentationList(adicionais);
    }
    
    public AdicionalRepresentation criaAdicional(Long idPrestador, Long idServico, AdicionalRepresentation novoAdicional) throws Exception {
    	var prestador = prestadorService.buscarPorId(idPrestador);
    	var servico = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestador.getId(), idServico);
    	novoAdicional.setServicoDetalhadoId(servico.getId());
    	var domain = adicionalConverter.toDomain(novoAdicional);
    	var adicional = adicionalService.criarAdicional(domain, idPrestador);

    	return adicionalConverter.toRepresentation(adicional);
    }

	public List<ServicoDetalhadoRepresentation> buscarServicosDetalhadosPorIds(List<Long> idsServicos) throws Exception {
		var servicosDb = servicoDetalhadoService.buscarServicosDetalhadosPorIds(idsServicos);
		var representationList = servicoDetalhadoConverter.toRepresentationList(servicosDb);
		
		for(var representation : representationList) {
			var prestadorRepresentation = prestadorConverter.toRepresentation(prestadorService.buscarPorId(representation.getPrestadorId()));
			representation.setPrestador(prestadorRepresentation);
		}

		return representationList;
	}

    public AdicionalRepresentation atualizarAdicional(Long idPrestador, Long idServico, Long idAdicional,
                                                      AdicionalRepresentation adicional) throws Exception {
        var prestador = prestadorService.buscarPorId(idPrestador);
        var servico = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestador.getId(), idServico);
        adicional.setServicoDetalhadoId(servico.getId());
        var domain = adicionalConverter.toDomain(adicional);
        var response = adicionalService.atualizarAdicional(idAdicional, domain);

        return adicionalConverter.toRepresentation(response);
    }

    public AdicionalRepresentation desativarAdicional(Long idPrestador, Long idServico, Long idAdicional, Boolean ativo) throws Exception {
        var prestador = prestadorService.buscarPorId(idPrestador);
        var servico = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestador.getId(), idServico);
        var adicional = adicionalService.desativarAdicional(idAdicional, servico.getId(), ativo);
        var representation = adicionalConverter.toRepresentation(adicional);

        return representation;
    }

    public ServicoDetalhadoRepresentation adicionarTipoAnimalAceito(Long id, Long prestadorId, Integer idTipoAnimal,
                                                                    ServicoDetalhadoTipoAnimalEstimacao request) throws NotFoundException, BusinessException {
        var tipoAnimalEstimacao = tipoAnimalEstimacaoService.buscarTipoAnimalEstimacaoPorId(idTipoAnimal);

        request.setTipoAnimalEstimacao(tipoAnimalEstimacao);

        var response = servicoDetalhadoService.adicionarTipoAnimalAceito(id, prestadorId, request);
        var representation = servicoDetalhadoConverter.toRepresentation(response);

        return representation;
    }

	public ServicoDetalhado adicionarServicoDetalhado(ServicoDetalhado servico) throws BusinessException, NotFoundException {
		var adicionais = servico.getAdicionais();
		servico.setAdicionais(new ArrayList<>());
		var servicoAdicionado = servicoDetalhadoService.adicionarServicoDetalhado(servico);
		adicionais.stream().forEach(adicional -> {
			adicional.setServicoDetalhadoId(servicoAdicionado.getId());
			adicionalService.criarAdicional(adicional, servicoAdicionado.getPrestadorId());
		});
		return servicoDetalhadoService.buscarPorId(servicoAdicionado.getId());
	}
}
