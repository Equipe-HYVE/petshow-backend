package hyve.petshow.util;

import hyve.petshow.domain.embeddables.Auditoria;

import java.time.LocalDate;
import java.util.Optional;

public class AuditoriaUtils {
    public static final String ATIVO = "S";
    public static final String INATIVO = "N";

    public static Auditoria geraAuditoriaInsercao(Optional<Long> usuarioId){
        var auditoria = new Auditoria();

        auditoria.setDataCriacao(LocalDate.now());
        auditoria.setDataAtualizacao(LocalDate.now());
        auditoria.setUsuarioCriacao(usuarioId.isPresent() ? usuarioId.get() : null);
        auditoria.setFlagAtivo(INATIVO);

        return auditoria;
    }

    public static Auditoria atualizaAuditoria(Auditoria auditoria, String flagAtivo){
        auditoria.setDataAtualizacao(LocalDate.now());
        auditoria.setFlagAtivo(flagAtivo);

        return auditoria;
    }
}
