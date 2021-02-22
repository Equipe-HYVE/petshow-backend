package hyve.petshow.domain;

import hyve.petshow.domain.embeddables.Auditoria;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "servico_detalhado")
public class ServicoDetalhado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float mediaAvaliacao;
    @Embedded
    private Auditoria auditoria;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_servico_detalhado")
    private List<Adicional> adicionais = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "fk_servico")
    private Servico tipo;
    @Column(name = "fk_conta")
    private Long prestadorId;
    @OneToMany(mappedBy = "servicoDetalhado", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<ServicoDetalhadoTipoAnimalEstimacao> tiposAnimaisAceitos = new ArrayList<>();
}
