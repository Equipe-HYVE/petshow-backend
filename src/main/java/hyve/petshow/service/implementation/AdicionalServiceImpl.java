package hyve.petshow.service.implementation;

import hyve.petshow.domain.Adicional;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AdicionalRepository;
import hyve.petshow.service.port.AdicionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hyve.petshow.util.AuditoriaUtils.*;

@Service
public class AdicionalServiceImpl implements AdicionalService {
	private static final String ADICIONAL_NAO_ENCONTRADO = "ADICIONAL_NAO_ENCONTRADO";
	private static final String ADICIONAL_DESATIVADO = "Adicional desativado";
	private static final String ADICIONAIS_NAO_ENCONTRADOS = "ADICIONAIS_NAO_ENCONTRADOS";
	private static final String ADICIONAL_SERVICO_DIVERGENTE =
			"Serviço detalhado informado difere do serviço detalhado ao qual o adicional pertence";

	@Autowired
	private AdicionalRepository repository;

	@Override
	public List<Adicional> buscarPorServicoDetalhado(Long idServico) throws Exception {
		var adicionais = repository.findByServicoDetalhadoIdAndAuditoriaFlagAtivo(idServico, ATIVO);

		if (adicionais.isEmpty()) {
			throw new NotFoundException(ADICIONAIS_NAO_ENCONTRADOS);
		}

		return adicionais;
	}

	@Override
	public Adicional buscarPorId(Long idAdicional) throws Exception {
		var adicional = repository.findById(idAdicional)
				.orElseThrow(() -> new NotFoundException(ADICIONAL_NAO_ENCONTRADO));

		if(! adicional.getAuditoria().isAtivo()){
			throw new NotFoundException(ADICIONAL_DESATIVADO);
		}

		return adicional;
	}

	@Override
	public List<Adicional> buscarAdicionaisPorIds(Long servicoDetalhadoId, List<Long> adicionaisIds) throws Exception {
		var adicionais = repository.findByServicoDetalhadoIdAndIdInAndAuditoriaFlagAtivo(servicoDetalhadoId, adicionaisIds, ATIVO);

		if(adicionais.isEmpty()) {
			throw new NotFoundException(ADICIONAIS_NAO_ENCONTRADOS);
		}

		return adicionais;
	}

	@Override
	public Adicional criarAdicional(Adicional adicional, Long prestadorId) {
		adicional.setAuditoria(geraAuditoriaInsercao(Optional.of(prestadorId)));

		return repository.save(adicional);
	}

	@Override
	public Adicional atualizarAdicional(Long idAdicional, Adicional adicional) throws Exception {
		var busca = buscarPorId(idAdicional);

		if(adicional.getServicoDetalhadoId() != busca.getServicoDetalhadoId()){
			throw new BusinessException(ADICIONAL_SERVICO_DIVERGENTE);
		}

		busca.setNome(adicional.getNome());
		busca.setDescricao(adicional.getDescricao());
		busca.setPreco(adicional.getPreco());
		busca.setAuditoria(atualizaAuditoria(busca.getAuditoria(), ATIVO));

		return repository.save(busca);
	}

	@Override
	public Boolean desativarAdicional(Long idAdicional, Long idServico) throws Exception {
		var adicional = buscarPorId(idAdicional);

		if (adicional.getServicoDetalhadoId() != idServico) {
			throw new BusinessException(ADICIONAL_SERVICO_DIVERGENTE);
		}

		adicional.setAuditoria(atualizaAuditoria(adicional.getAuditoria(), INATIVO));
		repository.save(adicional);

		return ! adicional.getAuditoria().isAtivo();
	}

	@Override
	public List<Adicional> criarAdicionais(List<Adicional> adicionais, Long prestadorId) {
		return Optional.ofNullable(adicionais).map(lista -> {
			return lista.stream()
					.map(el -> {
						return criarAdicional(el, prestadorId);
					}).collect(Collectors.toList());
		}).orElse(new ArrayList<>());
	}
}
