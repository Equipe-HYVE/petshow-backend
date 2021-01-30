package hyve.petshow.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import hyve.petshow.controller.converter.AdicionalConverter;
import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.representation.AdicionalRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.service.port.AdicionalService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.service.port.ServicoDetalhadoService;

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

    public Page<ServicoDetalhadoRepresentation> buscarServicosDetalhadosPorTipoServico(Integer id, Pageable pageable) throws Exception {
        var servicosDetalhados = servicoDetalhadoConverter.toRepresentationPage(
                servicoDetalhadoService.buscarServicosDetalhadosPorTipoServico(id, pageable));

        for (ServicoDetalhadoRepresentation servico : servicosDetalhados) {
            var prestador = prestadorConverter.toRepresentation(
                    prestadorService.buscarPorId(servico.getPrestadorId()));

            servico.setPrestador(prestador);
        }

        return servicosDetalhados;
    }

    public ServicoDetalhadoRepresentation buscarPorPrestadorIdEServicoId(Long prestadorId, Long servicoId) throws Exception {
        var servico = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestadorId, servicoId);
        var prestador = prestadorConverter.toRepresentation(
                prestadorService.buscarPorId(servico.getPrestadorId()));
        var representation = servicoDetalhadoConverter.toRepresentation(servico);

        representation.setPrestador(prestador);

        return representation;
    }
    
    public List<AdicionalRepresentation> buscarAdicionais(Long prestadorId, Long servicoId) throws Exception {
    	var prestador = prestadorService.buscarPorId(prestadorId);
    	var servicoDetalhado = servicoDetalhadoService.buscarPorPrestadorIdEServicoId(prestador.getId(), servicoId);
    	var adicionais = adicionalService.buscarPorServicoDetalhado(servicoDetalhado.getId());
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
		
		for(var representation: representationList) {
			var prestadorRepresentation = prestadorConverter.toRepresentation(prestadorService.buscarPorId(representation.getPrestadorId()));
			representation.setPrestador(prestadorRepresentation);
		}
		return representationList;
	}
    
}
