package hyve.petshow.domain;

import hyve.petshow.domain.embeddables.Auditoria;
import hyve.petshow.domain.embeddables.Endereco;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime data;
    private String comentario;
    private Endereco endereco;
    private BigDecimal precoFinal;
    @Embedded
    private Auditoria auditoria;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_status_agendamento")
    private StatusAgendamento status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_prestador")
    private Prestador prestador;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_servico_detalhado")
    private ServicoDetalhado servicoDetalhado;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_avaliacao")
    private Avaliacao avaliacao;
    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private List<AnimalEstimacaoAgendamento> animaisAtendidos = new ArrayList<>();
    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private List<AdicionalAgendamento> adicionais = new ArrayList<>();
}
