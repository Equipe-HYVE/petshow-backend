package hyve.petshow.domain;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "servico")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    
}