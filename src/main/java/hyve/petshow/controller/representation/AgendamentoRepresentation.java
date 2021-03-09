package hyve.petshow.controller.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyve.petshow.domain.embeddables.Endereco;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgendamentoRepresentation {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;
    private String comentario;
    private Endereco endereco;
    private BigDecimal precoFinal;
    private Integer statusId;
    private StatusAgendamentoRepresentation status;
    private Long clienteId;
    private ClienteRepresentation cliente;
    private Long prestadorId;
    private PrestadorRepresentation prestador;
    private Long servicoDetalhadoId;
    private ServicoDetalhadoRepresentation servicoDetalhado;
    private AvaliacaoRepresentation avaliacao;
    private NegociacaoRepresentation negociacao;
    private List<Long> animaisAtendidosIds;
    private List<AnimalEstimacaoRepresentation> animaisAtendidos;
    private List<Long> adicionaisIds;
    private List<AdicionalRepresentation> adicionais;
}
