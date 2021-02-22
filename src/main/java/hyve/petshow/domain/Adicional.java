package hyve.petshow.domain;

import hyve.petshow.domain.embeddables.Auditoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "adicional")
public class Adicional {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal preco;
	@Column(name = "fk_servico_detalhado")
	private Long servicoDetalhadoId;
	@Embedded
	private Auditoria auditoria;
}