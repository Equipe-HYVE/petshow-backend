package hyve.petshow.mock;

import hyve.petshow.controller.representation.AdicionalRepresentation;
import hyve.petshow.domain.Adicional;

import java.math.BigDecimal;
import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;

public class AdicionalMock {
	public static Adicional criaAdicional(Long idAdicional) {
		var adicional = new Adicional();
		adicional.setId(idAdicional);
		adicional.setNome(String.valueOf(idAdicional));
		adicional.setDescricao(String.valueOf(idAdicional));
		adicional.setPreco(BigDecimal.valueOf(idAdicional));
		adicional.setServicoDetalhadoId(1l);
		adicional.setAuditoria(geraAuditoriaInsercao(Optional.of(1L)));
		return adicional;
	}

	public static AdicionalRepresentation criaAdicionalRepresentation(Long idAdicional) {
		var adicional = new AdicionalRepresentation();
		adicional.setId(idAdicional);
		adicional.setNome(String.valueOf(idAdicional));
		adicional.setDescricao(String.valueOf(idAdicional));
		adicional.setPreco(BigDecimal.valueOf(idAdicional));
		adicional.setServicoDetalhadoId(1l);
		return adicional;
	}
}

