package hyve.petshow.facade;

import hyve.petshow.controller.converter.AvaliacaoConverter;
import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.service.port.AvaliacaoService;
import hyve.petshow.service.port.ClienteService;
import hyve.petshow.service.port.ServicoDetalhadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoFacade {
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private ServicoDetalhadoService servicoDetalhadoService;
	@Autowired
	private AvaliacaoService avaliacaoService;
	@Autowired
	private AvaliacaoConverter converter;

	public void adicionarAvaliacao(AvaliacaoRepresentation request, Long clienteId, Long servicoDetalhadoId)
			throws Exception {
		var cliente = clienteService.buscarPorId(clienteId);
		var servicoDetalhado = servicoDetalhadoService.buscarPorId(servicoDetalhadoId);
		var avaliacao = converter.toDomain(request);

		servicoDetalhado.addAvaliacao(avaliacao);
		avaliacao.setCliente(cliente);

		avaliacaoService.adicionarAvaliacao(avaliacao);
	}

	public Page<AvaliacaoRepresentation> buscarAvaliacaoPorServico(Long idServicoPrestado, Pageable pageable)
			throws Exception {
		var servico = servicoDetalhadoService.buscarPorId(idServicoPrestado);
		var avaliacoes = avaliacaoService.buscarAvaliacoesPorServicoId(servico.getId(), pageable);

		return converter.toRepresentationPage(avaliacoes);
	}

}
