package hyve.petshow.util;

import hyve.petshow.controller.representation.ComparacaoRepresentation;
import hyve.petshow.controller.representation.ComparacaoWrapper;
import hyve.petshow.controller.representation.PrecoPorTipoRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComparacaoUtils {
	public static ComparacaoWrapper criaWrapper(List<ServicoDetalhadoRepresentation> representation) {
		var servicosComparados = representation.stream().map(el -> criaRepresentation(el)).collect(Collectors.toList());
		var comparacao = geraComparacao(servicosComparados);
		return new ComparacaoWrapper(servicosComparados, comparacao);
	}

	private static Map<String, Long> geraComparacao(List<ComparacaoRepresentation> servicosComparados) {
		var comparacao = new HashMap<String, Long>();
		comparacao.put("menorPreco", getIdMenorPreco(servicosComparados));
		comparacao.put("maiorPreco", getIdMaiorPreco(servicosComparados));
		comparacao.put("mediaAvaliacao", getIdMaiorMediaAvaliacao(servicosComparados));
		return comparacao;
	}

	private static Long getIdMenorPreco(List<ComparacaoRepresentation> servicosComparados) {
		BigDecimal menorPreco = null;
		Long idServico = null;
		for(ComparacaoRepresentation comparacao: servicosComparados) {
			if(menorPreco == null || menorPreco.compareTo(comparacao.getMenorPreco()) == 1) {
				menorPreco = comparacao.getMenorPreco();
				idServico = comparacao.getId();
			} else if (menorPreco.compareTo(comparacao.getMenorPreco()) == 0) {
				menorPreco = null;
				idServico = null;
			}
		}
		
		return idServico;
	}
	
	private static Long getIdMaiorPreco(List<ComparacaoRepresentation> servicosComparados) {
		BigDecimal maiorPreco = null;
		Long idServico = null;
		for(ComparacaoRepresentation comparacao: servicosComparados) {
			if(maiorPreco == null || maiorPreco.compareTo(comparacao.getMaiorPreco()) == -1) {
				maiorPreco = comparacao.getMaiorPreco();
				idServico = comparacao.getId();
			} else if (maiorPreco.compareTo(comparacao.getMaiorPreco()) == 0) {
				maiorPreco = null;
				idServico = null;
			}
		}
		
		return idServico;
	}
	
	private static Long getIdMaiorMediaAvaliacao(List<ComparacaoRepresentation> servicosComparados) {
		BigDecimal maiorMedia = null;
		Long idServico = null;
		for(ComparacaoRepresentation comparacao: servicosComparados) {
			if(maiorMedia == null || maiorMedia.compareTo(comparacao.getMediaAvaliacao()) == -1) {
				maiorMedia = comparacao.getMediaAvaliacao();
				idServico = comparacao.getId();
			} else if (maiorMedia.compareTo(comparacao.getMediaAvaliacao()) == 0) {
				maiorMedia = null;
				idServico = null;
			}
		}
		
		return idServico;
	}
	

	public static ComparacaoRepresentation criaRepresentation(ServicoDetalhadoRepresentation representation) {
		var comparacao = new ComparacaoRepresentation();
		comparacao.setId(representation.getId());
		comparacao.setNome(representation.getTipo().getNome() + " - " + representation.getPrestador().getNome());
		comparacao.setAdicionais(representation.getAdicionais());
		comparacao.setMaiorPreco(geraMaiorPreco(representation.getPrecoPorTipo()));
		comparacao.setMenorPreco(geraMenorPreco(representation.getPrecoPorTipo()));
		comparacao.setTabelaPrecos(representation.getPrecoPorTipo());
		comparacao.setTiposAtendidos(geraTiposAtendidos(representation.getPrecoPorTipo()));
		comparacao.setMediaAvaliacao(new BigDecimal(representation.getMedia()).setScale(2));
		return comparacao;
	}

	private static Map<String, Boolean> geraTiposAtendidos(List<PrecoPorTipoRepresentation> precoPorTipo) {
		return precoPorTipo.stream().map(el -> el.getTipoAnimal().getNome()).collect(Collectors.toSet()).stream()
				.collect(Collectors.toMap(el -> el, el -> true));
	}

	private static BigDecimal geraMenorPreco(List<PrecoPorTipoRepresentation> precoPorTipo) {
		return precoPorTipo.stream().min(Comparator.comparing(PrecoPorTipoRepresentation::getPreco))
				.map(el -> el.getPreco()).orElse(null);
	}

	private static BigDecimal geraMaiorPreco(List<PrecoPorTipoRepresentation> precoPorTipo) {
		return precoPorTipo.stream().max(Comparator.comparing(PrecoPorTipoRepresentation::getPreco))
				.map(el -> el.getPreco()).orElse(null);
	}

}
