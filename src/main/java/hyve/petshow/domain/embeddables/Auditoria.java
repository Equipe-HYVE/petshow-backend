package hyve.petshow.domain.embeddables;

import lombok.Data;

import javax.persistence.Embeddable;

import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import java.time.LocalDate;

@Data
@Embeddable
public class Auditoria {
    private LocalDate dataCriacao;
    private LocalDate dataAtualizacao;
    private Long usuarioCriacao;
    private String flagAtivo;
    
    public Boolean isAtivo() {
    	return ATIVO.equals(getFlagAtivo());
    }
}
