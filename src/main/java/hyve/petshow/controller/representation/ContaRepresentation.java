package hyve.petshow.controller.representation;

import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.domain.embeddables.Login;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaRepresentation {
	private static final String MENSAGEM_CONTA_INATIVA = "MENSAGEM_CONTA_INATIVA"; // Essa conta ainda não foi ativada. Verifique seu e-mail para poder usar todas as funcionalidades da plataforma!
	private Long id;
	private String nome;
	private String nomeSocial;
	private String cpf;
	private String telefone;
	private Integer tipo;
	private String foto;
	private Float mediaAvaliacao;
	private Endereco endereco;
	private Login login;
	private Geolocalizacao geolocalizacao;
	@Setter(value = AccessLevel.NONE)
	private Boolean isAtivo;
	@Setter(value = AccessLevel.PRIVATE)
	private String mensagem;
	
	public void setIsAtivo(Boolean isAtivo) {
		this.isAtivo = isAtivo;
		setMensagem(getIsAtivo() ? null : MENSAGEM_CONTA_INATIVA);
	}
}
