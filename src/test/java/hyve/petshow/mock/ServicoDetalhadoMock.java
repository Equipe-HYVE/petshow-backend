package hyve.petshow.mock;

import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AdicionalMock.criaAdicional;
import static hyve.petshow.mock.AnimalEstimacaoMock.criaTipoAnimalEstimacao;
import static hyve.petshow.mock.AuditoriaMock.auditoria;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacaoRepresentation;
import static hyve.petshow.mock.ServicoMock.servico;
import static hyve.petshow.mock.ServicoMock.servicoRepresentation;
import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static java.util.Collections.singletonList;

public class ServicoDetalhadoMock {
	public static ServicoDetalhado criaServicoDetalhado() {
		var servicoDetalhado = new ServicoDetalhado();
		var tiposAnimaisAceitos =
				new ServicoDetalhadoTipoAnimalEstimacao(servicoDetalhado, criaTipoAnimalEstimacao(), BigDecimal.TEN);

		servicoDetalhado.setId(1L);
		servicoDetalhado.setMediaAvaliacao(4.5F);
		servicoDetalhado.setAuditoria(auditoria(ATIVO));
		servicoDetalhado.setTipo(servico());
		servicoDetalhado.setPrestadorId(1L);
		servicoDetalhado.setAdicionais(Arrays.asList(criaAdicional(1L)));
		servicoDetalhado.setTiposAnimaisAceitos(Arrays.asList(tiposAnimaisAceitos));

		return servicoDetalhado;
	}

	public static ServicoDetalhadoRepresentation criaServicoDetalhadoRepresentation() {
		var servicoDetalhadoRepresentation = new ServicoDetalhadoRepresentation();
		var servicoDetalhado = criaServicoDetalhado();
		var prestador = new PrestadorRepresentation();

		prestador.setNome("teste");

		servicoDetalhadoRepresentation.setId(servicoDetalhado.getId());
		servicoDetalhadoRepresentation.setMediaAvaliacao(servicoDetalhado.getMediaAvaliacao());
		servicoDetalhadoRepresentation.setAvaliacoes(singletonList(criaAvaliacaoRepresentation()));
		servicoDetalhadoRepresentation.setTipo(servicoRepresentation());
		servicoDetalhadoRepresentation.setPrestador(prestador);
		servicoDetalhadoRepresentation.setPrestadorId(servicoDetalhado.getPrestadorId());

		return servicoDetalhadoRepresentation;
	}

	public static List<ServicoDetalhado> criaServicoDetalhadoList(){
		return singletonList(criaServicoDetalhado());
	}

	public static List<ServicoDetalhadoRepresentation> criaServicoDetalhadoRepresentationList(){
		return singletonList(criaServicoDetalhadoRepresentation());
	}
}
