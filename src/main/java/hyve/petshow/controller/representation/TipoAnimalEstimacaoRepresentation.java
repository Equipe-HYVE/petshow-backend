package hyve.petshow.controller.representation;

import lombok.Data;

@Data
public class TipoAnimalEstimacaoRepresentation {
    private Integer id;
    private String nome;
    private String porte;
    private String pelagem;
}