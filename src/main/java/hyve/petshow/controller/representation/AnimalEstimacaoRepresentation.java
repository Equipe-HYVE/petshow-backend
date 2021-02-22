package hyve.petshow.controller.representation;

import lombok.Data;

@Data
public class AnimalEstimacaoRepresentation {
    private Long id;
    private String nome;
    private String foto;
    private TipoAnimalEstimacaoRepresentation tipo;
    private Long donoId;
}
