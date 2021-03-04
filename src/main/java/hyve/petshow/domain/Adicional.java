package hyve.petshow.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hyve.petshow.domain.embeddables.Auditoria;
import lombok.Data;

@Data
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