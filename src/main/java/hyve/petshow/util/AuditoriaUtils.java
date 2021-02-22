package hyve.petshow.util;

import hyve.petshow.domain.embeddables.Auditoria;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuditoriaUtils {
    public static final String ATIVO = "S";
    public static final String INATIVO = "N";

    public static Auditoria geraAuditoriaInsercao(Optional<Long> usuarioId){
        var auditoria = new Auditoria();

        auditoria.setDataCriacao(LocalDateTime.now());
        auditoria.setDataAtualizacao(LocalDateTime.now());
        auditoria.setUsuarioCriacao(usuarioId.isPresent() ? usuarioId.get() : null);
        auditoria.setFlagAtivo(ATIVO);

        return auditoria;
    }

    public static Auditoria geraAuditoriaInsercaoConta(Optional<Long> usuarioId){
        var auditoria = new Auditoria();

        auditoria.setDataCriacao(LocalDateTime.now());
        auditoria.setDataAtualizacao(LocalDateTime.now());
        auditoria.setUsuarioCriacao(usuarioId.isPresent() ? usuarioId.get() : null);
        auditoria.setFlagAtivo(INATIVO);

        return auditoria;
    }

    public static Auditoria atualizaAuditoria(Auditoria auditoria, String flagAtivo){
        auditoria.setDataAtualizacao(LocalDateTime.now());
        auditoria.setFlagAtivo(flagAtivo);

        return auditoria;
    }
}
