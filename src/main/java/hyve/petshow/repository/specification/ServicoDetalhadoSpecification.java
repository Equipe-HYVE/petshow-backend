package hyve.petshow.repository.specification;

import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.math.BigDecimal;

import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static hyve.petshow.util.NullUtils.isNotNull;

@Slf4j
public class ServicoDetalhadoSpecification {
    private static final int UM_DEG_EM_METROS = 111111;

	public static Specification<ServicoDetalhado> geraSpecification(ServicoDetalhadoFilter filtragem) {
        return (Specification<ServicoDetalhado>) (root, query, builder) -> {
            var predicate = builder.and();

            var subQueryFiltrarPreco = query.subquery(BigDecimal.class);
            var subQueryTiposAnimaisAceitos = subQueryFiltrarPreco.from(ServicoDetalhadoTipoAnimalEstimacao.class);
            var subQueryPrestador = query.subquery(Prestador.class);

            var servico = root.join("tipo");
            var adicionais = root.join("adicionais", JoinType.LEFT);
            var tiposAnimaisAceitos = root.join("tiposAnimaisAceitos", JoinType.LEFT);
            var prestador = subQueryPrestador.from(Prestador.class);
            prestador.join("empresa", JoinType.LEFT);

            predicate = builder.and(predicate, builder.equal(root.get("auditoria").get("flagAtivo"), ATIVO));
            predicate = builder.and(predicate, builder.equal(servico.get("id"), filtragem.getTipoServicoId()));
            predicate = adicionaFiltroAdicionais(predicate, builder, filtragem, adicionais);
            predicate = adicionaFiltroAvaliacao(filtragem, root, builder, predicate);
            predicate = adicionaFiltroGeoloc(filtragem, root, builder, predicate, subQueryPrestador, prestador);
            predicate = adicionaFiltroPrecoMinimo(filtragem, root, builder, predicate, subQueryFiltrarPreco, subQueryTiposAnimaisAceitos);
            predicate = adicionaFiltroPrecoMaximo(filtragem, root, builder, predicate, subQueryFiltrarPreco, subQueryTiposAnimaisAceitos);

            adicionaOrdenacao(filtragem, root, query, builder, tiposAnimaisAceitos);
            query.distinct(true);

            return predicate;
        };
    }

	private static void adicionaOrdenacao(ServicoDetalhadoFilter filtragem, Root<ServicoDetalhado> root, CriteriaQuery<?> query, CriteriaBuilder builder, Join<Object, Object> tiposAnimaisAceitos) {
        if(isNotNull(filtragem.getOrdenacao())){
            var ordenacao = filtragem.getOrdenacao();

            if(ordenacao.equals("mediaAvaliacao")){
                query.orderBy(builder.desc(root.get("mediaAvaliacao")));
            }
            if(ordenacao.equals("menorPreco")) {
                query.orderBy(builder.asc(builder.min(tiposAnimaisAceitos.get("preco"))));
            }
            if(ordenacao.equals("maiorPreco")) {
                query.orderBy(builder.desc(builder.min(tiposAnimaisAceitos.get("preco"))));
            }
        }

        query.groupBy(root.get("id"));
    }
    
   

    private static Predicate adicionaFiltroPrecoMaximo(ServicoDetalhadoFilter filtragem, Root<ServicoDetalhado> root, CriteriaBuilder builder, Predicate predicate, Subquery<BigDecimal> subQueryFiltrarPreco, Root<ServicoDetalhadoTipoAnimalEstimacao> subQueryTiposAnimaisAceitos) {
        if(isNotNull(filtragem.getMaiorPreco())){
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(
                    subQueryFiltrarPreco
                            .select((builder.min(subQueryTiposAnimaisAceitos.get("preco"))))
                            .where(builder.equal(subQueryTiposAnimaisAceitos.get("servicoDetalhado"), root.get("id"))),
                    filtragem.getMaiorPreco()));
        }
        return predicate;
    }

    private static Predicate adicionaFiltroPrecoMinimo(ServicoDetalhadoFilter filtragem, Root<ServicoDetalhado> root,
                                                       CriteriaBuilder builder, Predicate predicate,
                                                       Subquery<BigDecimal> subQueryFiltrarPreco,
                                                       Root<ServicoDetalhadoTipoAnimalEstimacao> subQueryTiposAnimaisAceitos) {
        if(isNotNull(filtragem.getMenorPreco())){

            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(
                    subQueryFiltrarPreco
                            .select((builder.min(subQueryTiposAnimaisAceitos.get("preco"))))
                            .where(builder.equal(subQueryTiposAnimaisAceitos.get("servicoDetalhado"), root.get("id"))),
                    filtragem.getMenorPreco()));
        }
        return predicate;
    }

    private static Predicate adicionaFiltroAvaliacao(ServicoDetalhadoFilter filtragem, Root<ServicoDetalhado> root,
                                                     CriteriaBuilder builder, Predicate predicate) {
        if(isNotNull(filtragem.getMediaAvaliacao())){
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(root.get("mediaAvaliacao"), filtragem.getMediaAvaliacao()));
        }

        return predicate;
    }
    
    private static Predicate adicionaFiltroGeoloc(ServicoDetalhadoFilter filtragem, Root<ServicoDetalhado> root,
                                                  CriteriaBuilder builder,  Predicate predicate,
                                                  Subquery<Prestador> queryPrestador, Root<Prestador> prestador) {
    	if(isNotNull(filtragem.getPosicaoAtual())) {
    		var geolocPositiva = geraGeolocalizacaoPositiva(filtragem.getPosicaoAtual(), filtragem.getMetrosGeoloc());
    		var geolocNegativa = geraGeolocalizacaoNegativa(filtragem.getPosicaoAtual(), filtragem.getMetrosGeoloc());
    		
			predicate = builder.and(predicate,
					builder.in(root.get("prestadorId")).value(queryPrestador.select(prestador.get("id"))
							.where(builder.and(builder.or(builder.and(prestador.get("empresa").isNull(),
									builder.between(prestador.get("geolocalizacao").get("geolocLongitude"),
											geolocPositiva.getGeolocLongitude(), geolocNegativa.getGeolocLongitude()),
									builder.between(prestador.get("geolocalizacao").get("geolocLatitude"),
											geolocPositiva.getGeolocLatitude(), geolocNegativa.getGeolocLatitude())
							), builder.and(prestador.get("empresa").isNotNull(),
									builder.between(prestador.get("empresa").get("geolocalizacao").get("geolocLongitude"),
											geolocPositiva.getGeolocLongitude(), geolocNegativa.getGeolocLongitude()),
									builder.between(prestador.get("empresa").get("geolocalizacao").get("geolocLatitude"),
											geolocPositiva.getGeolocLatitude(), geolocNegativa.getGeolocLatitude())))))));
			
			
//					builder.equal(queryPrestador.select(prestador.get("id"))
//							.where(builder.and(builder.or(builder.and(prestador.get("empresa").isNull(),
//									builder.between(prestador.get("geolocalizacao").get("geolocLongitude"),
//											geolocPositiva.getGeolocLongitude(), geolocNegativa.getGeolocLongitude()),
//									builder.between(prestador.get("geolocalizacao").get("geolocLatitude"),
//											geolocPositiva.getGeolocLatitude(), geolocNegativa.getGeolocLatitude())
//
//							), builder.and(prestador.get("empresa").isNotNull(),
//									builder.between(prestador.get("empresa").get("geolocalizacao").get("geolocLongitude"),
//											geolocPositiva.getGeolocLongitude(), geolocNegativa.getGeolocLongitude()),
//									builder.between(prestador.get("empresa").get("geolocalizacao").get("geolocLatitude"),
//											geolocPositiva.getGeolocLatitude(), geolocNegativa.getGeolocLatitude())))),
//									builder.equal(prestador.get("id"), root.get("id"))),
//							root.get("prestadorId")));
    	}

    	return predicate;
    }
    
    private static Geolocalizacao geraGeolocalizacaoPositiva(Geolocalizacao geoloc, Double metros) {
    	var geolocalizacao = new Geolocalizacao();
    	var latitude = calculaDegLatitude(metros);
    	var longitude = calculaDegLongitude(metros, latitude);
    	var latitudeNova = Double.parseDouble(geoloc.getGeolocLatitude()) + latitude;
    	var longitudeNova = Double.parseDouble(geoloc.getGeolocLongitude()) + longitude;
    	geolocalizacao.setGeolocLatitude(String.valueOf(latitudeNova));
    	geolocalizacao.setGeolocLongitude(String.valueOf(longitudeNova));
		return geolocalizacao;
    }
    
    private static Geolocalizacao geraGeolocalizacaoNegativa(Geolocalizacao geoloc, Double metros) {
    	var geolocalizacao = new Geolocalizacao();
    	var latitude = calculaDegLatitude(metros);
    	var longitude = calculaDegLongitude(metros, latitude);
    	var latitudeNova = Double.parseDouble(geoloc.getGeolocLatitude()) - latitude;
    	var longitudeNova = Double.parseDouble(geoloc.getGeolocLongitude()) - longitude;
    	geolocalizacao.setGeolocLatitude(String.valueOf(latitudeNova));
    	geolocalizacao.setGeolocLongitude(String.valueOf(longitudeNova));
		return geolocalizacao;
    }
    
    private static Double calculaDegLatitude(Double metros) {
    	return metros / UM_DEG_EM_METROS;
    }
    
    private static Double calculaDegLongitude(Double metros, Double latitude) {
    	return metros / (UM_DEG_EM_METROS * Math.cos(latitude));
    }

    private static Predicate adicionaFiltroAdicionais(Predicate predicate, CriteriaBuilder builder,
                                            ServicoDetalhadoFilter filtragem, Join adicionais){
        if(isNotNull(filtragem.getPossuiAdicionais()) && filtragem.getPossuiAdicionais()){
            predicate = builder.and(predicate, builder.isNotNull(adicionais.get("id")));
        }

        return predicate;
    }
}
