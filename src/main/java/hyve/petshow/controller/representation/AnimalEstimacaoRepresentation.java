package hyve.petshow.controller.representation;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AnimalEstimacaoRepresentation {
    private Long id;
    @Size(max = 20, message = "O nome informado ultrapassa o limite de 20 caracteres.")
    @NotNull(message = "O nome do animal de estimação é obrigatório.")
    private String nome;
    private String foto;
    @NotNull(message = "O tipo do animal de estimação é obrigatório.")
    private TipoAnimalEstimacaoRepresentation tipo;
    @NotNull(message = "O id do dono do animal de estimação é obrigatório.")
    private Long donoId;
}
