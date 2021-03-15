package hyve.petshow.controller.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyve.petshow.domain.embeddables.Endereco;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgendamentoRepresentation {
    private Long id;
    @NotNull(message = "A data do agendamento é obrigatória.")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;
    @Size(max = 280, message = "O comentário informado ultrapassa o limite de 280 caracteres.")
    private String comentario;
    @NotNull(message = "O endereço do agendamento é obrigatório.")
    private Endereco endereco;
    @NotNull(message = "O preço final do agendamento é obrigatório.")
    @DecimalMin(value = "0.0", message = "O preço informado é inferior ao mínimo de R$0,0")
    @DecimalMax(value = "99999.99", message = "O preço informado ultrapassa o limite de R$99999,99")
    private BigDecimal precoFinal;
    private Integer statusId;
    private StatusAgendamentoRepresentation status;
    private Long clienteId;
    @NotNull(message = "O cliente do agendamento é obrigatório.")
    private ClienteRepresentation cliente;
    private Long prestadorId;
    @NotNull(message = "O prestador do agendamento é obrigatório.")
    private PrestadorRepresentation prestador;
    private Long servicoDetalhadoId;
    @NotNull(message = "O serviço detalhado do agendamento é obrigatório.")
    private ServicoDetalhadoRepresentation servicoDetalhado;
    private AvaliacaoRepresentation avaliacao;
    private NegociacaoRepresentation negociacao;
    private List<Long> animaisAtendidosIds;
    @NotEmpty(message = "Ao menos um animal de estimação deve ser informado para o agendamento")
    private List<AnimalEstimacaoRepresentation> animaisAtendidos;
    private List<Long> adicionaisIds;
    private List<AdicionalRepresentation> adicionais;
}
