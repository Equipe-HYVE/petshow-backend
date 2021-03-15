package hyve.petshow.controller.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrecoPorTipoRepresentation {
    @NotNull(message = "O tipo de animal é obrigatório.")
    private TipoAnimalEstimacaoRepresentation tipoAnimal;
    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", message = "O preço informado é inferior ao mínimo de R$0,0")
    @DecimalMax(value = "99999.99", message = "O preço informado ultrapassa o limite de R$99999,99")
    private BigDecimal preco;
    private Boolean ativo;
}
