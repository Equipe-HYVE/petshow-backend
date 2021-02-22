package hyve.petshow.domain;

import hyve.petshow.domain.embeddables.Auditoria;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.domain.embeddables.Login;
import hyve.petshow.domain.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "conta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name = "discriminator",
		discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="G") //Generico
@NoArgsConstructor
@AllArgsConstructor
public class Conta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String nomeSocial;
	private String cpf;
	private String telefone;
	private Float mediaAvaliacao;
	private String foto;
	@Enumerated(EnumType.STRING)
	private TipoConta tipo;
	@Embedded
    private Endereco endereco;
	@Embedded
    private Login login;
	@Embedded
	private Auditoria auditoria;
	@Embedded
	private Geolocalizacao geolocalizacao;
	
	public String getEmail() {
		return getLogin() == null ? null : getLogin().getEmail();
	}
	
	public Boolean isAtivo() {
		return getAuditoria() == null ? false: getAuditoria().isAtivo();
	}
}
