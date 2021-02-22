package hyve.petshow.mock;

import hyve.petshow.domain.embeddables.Auditoria;

public class AuditoriaMock {
    public static Auditoria auditoria(String ativo) {
        var auditoria = new Auditoria();

        auditoria.setFlagAtivo(ativo);

        return auditoria;
    }
}
