package hyve.petshow.facade;

import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;
import static hyve.petshow.util.NullUtils.isNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hyve.petshow.controller.converter.AgendamentoConverter;
import hyve.petshow.controller.converter.NegociacaoConverter;
import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.controller.representation.NegociacaoRepresentation;
import hyve.petshow.domain.Adicional;
import hyve.petshow.domain.AdicionalAgendamento;
import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.domain.AnimalEstimacaoAgendamento;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.enums.StatusAgendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.AdicionalService;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.AnimalEstimacaoService;
import hyve.petshow.service.port.ClienteService;
import hyve.petshow.service.port.NegociacaoService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.service.port.ServicoDetalhadoService;
import hyve.petshow.service.port.StatusAgendamentoService;

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
	@Autowired
	private NegociacaoConverter negociacaoConverter;
	@Autowired
	private NegociacaoService negociacaoService;

	public AgendamentoRepresentation adicionarAgendamento(AgendamentoRepresentation request) throws Exception {
		var agendamento = criarAgendamento(request);
		if (isNotNull(request.getNegociacao())) {
			var domain = negociacaoConverter.toDomain(request.getNegociacao());
			var negociacao = negociacaoService.criaNegociacao(agendamento.getId(), agendamento.getCliente().getId(),
					domain);
			var ofertaSolicitada = statusAgendamentoService
					.buscarStatusPorNome(StatusAgendamento.OFERTA_SOLICITADA.getValue());
			agendamento.setStatus(ofertaSolicitada);
			agendamento.setNegociacao(negociacao);
			agendamentoService.atualizarAgendamento(agendamento.getId(), agendamento);
		}

		var representation = agendamentoConverter.toRepresentation(agendamento);

		return representation;
	}

	public Boolean atualizarStatusAgendamento(Long id, Long prestadorId, Integer statusId)
			throws NotFoundException, BusinessException {
		var statusAgendamento = statusAgendamentoService.buscarStatusAgendamento(statusId);

		var agendamento = agendamentoService.atualizarStatusAgendamento(id, prestadorId, statusAgendamento);

		return agendamento.getStatus().getId() == statusId;
	}

	private List<AnimalEstimacaoAgendamento> processaAnimaisEstimacaoAgendamento(Long donoId,
			List<Long> animaisEstimacaoIds, Agendamento agendamento) throws NotFoundException {
		var animaisBuscados = animalEstimacaoService.buscarAnimaisEstimacaoPorIds(donoId, animaisEstimacaoIds);
		var animaisAtendidos = new ArrayList<AnimalEstimacaoAgendamento>();

		for (AnimalEstimacao animalEstimacao : animaisBuscados) {
			animaisAtendidos.add(new AnimalEstimacaoAgendamento(agendamento, animalEstimacao));
		}

		return animaisAtendidos;
	}

	public List<String> buscaHorariosAgendamento(Long prestadorId, LocalDate dataAgendamento) {
		return agendamentoService.buscarHorariosAgendamento(prestadorId, dataAgendamento);
	}

	private Agendamento criarAgendamento(AgendamentoRepresentation request) throws Exception, NotFoundException {
		var agendamento = agendamentoConverter.toDomain(request);
		var cliente = clienteService.buscarPorId(request.getClienteId());
		var prestador = prestadorService.buscarPorId(request.getPrestadorId());
		var servicoDetalhado = servicoDetalhadoService.buscarPorId(request.getServicoDetalhadoId());
		var auditoria = geraAuditoriaInsercao(Optional.of(request.getClienteId()));
		var status = statusAgendamentoService.buscarStatusPorNome(StatusAgendamento.PENDENTE_PAGAMENTO.getValue());
		var animaisAtendidos = this.processaAnimaisEstimacaoAgendamento(request.getClienteId(),
				request.getAnimaisAtendidosIds(), agendamento);
		var adicionais = this.processaAgendamentoAdicional(request.getServicoDetalhadoId(), request.getAdicionaisIds(),
				agendamento);
		var precoFinal = new BigDecimal(0);

		/* TODO: UTILIZAR LAMBDA FUNCTION COMO MELHORIA DE PERFOMANCE */
		for (var animal : animaisAtendidos) {
			for (var tipoAnimal : servicoDetalhado.getTiposAnimaisAceitos()) {
				if (animal.getAnimalEstimacao().getTipo().equals(tipoAnimal.getTipoAnimalEstimacao()))
					precoFinal = precoFinal.add(tipoAnimal.getPreco());
			}
		}

		for (var adicional : adicionais) {
			precoFinal = precoFinal.add(adicional.getAdicional().getPreco());
		}

		agendamento.setEndereco(geraEnderecoAgendamento(prestador));
		agendamento.setPrecoFinal(precoFinal);
		agendamento.setAuditoria(auditoria);
		agendamento.setStatus(status);
		agendamento.setCliente(cliente);
		agendamento.setPrestador(prestador);
		agendamento.setServicoDetalhado(servicoDetalhado);
		agendamento.setAnimaisAtendidos(animaisAtendidos);
		agendamento.setAdicionais(adicionais);
		agendamento = agendamentoService.adicionarAgendamento(agendamento);
		return agendamento;
	}

	private Endereco geraEnderecoAgendamento(Prestador prestador) {
		if (isNotNull(prestador.getEmpresa())) {
			return prestador.getEmpresa().getEndereco();
		}
		return prestador.getEndereco();
	}

	private List<AdicionalAgendamento> processaAgendamentoAdicional(Long servicoDetalhadoId, List<Long> adicionaisIds,
			Agendamento agendamento) throws Exception {
		var adicionais = new ArrayList<AdicionalAgendamento>();

		if (!adicionaisIds.isEmpty()) {
			var adicionaisBuscados = adicionalService.buscarAdicionaisPorIds(servicoDetalhadoId, adicionaisIds);

			for (Adicional adicional : adicionaisBuscados) {
				adicionais.add(new AdicionalAgendamento(agendamento, adicional));
			}
		}

		return adicionais;
	}

	public AgendamentoRepresentation confirmaNegociacao(Long agendamentoId, Long prestadorId,
			NegociacaoRepresentation negociacaoRepresentation) throws BusinessException, NotFoundException {
		var agendamento = agendamentoService.buscarPorId(agendamentoId, prestadorId);
		var negociacao = negociacaoConverter.toDomain(negociacaoRepresentation);
		if (negociacaoRepresentation.getRespostaOferta()) {
			agendamento.setPrecoFinal(negociacao.getPrecoOferta());
		}
		agendamento.setStatus(
				statusAgendamentoService.buscarStatusPorNome(StatusAgendamento.PENDENTE_PAGAMENTO.getValue()));
		agendamento.setNegociacao(negociacao);
		
		var agendamentoAtualizado = agendamentoService.atualizarAgendamento(agendamento.getId(), agendamento);
		return agendamentoConverter.toRepresentation(agendamentoAtualizado);
	}
}
