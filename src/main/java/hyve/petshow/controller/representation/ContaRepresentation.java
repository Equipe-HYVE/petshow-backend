package hyve.petshow.controller.representation;

import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.domain.embeddables.Login;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaRepresentation {
	private Long id;
	@Size(max = 100, message = "O nome informado ultrapassa o limite de 100 caracteres.")
	@NotNull(message = "O nome da conta é obrigatório.")
	private String nome;
	@Size(max = 50, message = "O nome social informado ultrapassa o limite de 50 caracteres.")
	private String nomeSocial;
	@Size(max = 15, message = "O CPF informado ultrapassa o limite de 15 caracteres.")
	@NotNull(message = "O CPF da conta é obrigatório.")
	private String cpf;
	@Size(max = 15, message = "O telefone informado ultrapassa o limite de 15 caracteres.")
	@NotNull(message = "O telefone da conta é obrigatório.")
	private String telefone;
	@NotNull(message = "O tipo da conta é obrigatório.")
	private Integer tipo;
	private String foto;
	private Float mediaAvaliacao;
	@NotNull(message = "O endereço da conta é obrigatório.")
	private Endereco endereco;
	@NotNull(message = "O login da conta é obrigatório.")
	private Login login;
	@NotNull(message = "A geolocalização da conta é obrigatória.")
	private Geolocalizacao geolocalizacao;
	@Setter(value = AccessLevel.NONE)
	private Boolean isAtivo;
	@Setter(value = AccessLevel.PRIVATE)
	private String mensagem;

	private static final String MENSAGEM_CONTA_INATIVA = "MENSAGEM_CONTA_INATIVA"; // Essa conta ainda não foi ativada. Verifique seu e-mail para poder usar todas as funcionalidades da plataforma!

	public void setIsAtivo(Boolean isAtivo) {
		this.isAtivo = isAtivo;
		setMensagem(getIsAtivo() ? null : MENSAGEM_CONTA_INATIVA);
	}
}
