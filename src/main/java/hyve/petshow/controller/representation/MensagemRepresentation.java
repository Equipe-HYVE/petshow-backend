package hyve.petshow.controller.representation;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemRepresentation {
	public static final String MENSAGEM_SUCESSO = "MENSAGEM_SUCESSO";//"Operação concluída com sucesso";
	public static final String MENSAGEM_FALHA = "MENSAGEM_FALHA";//"Falha durante a execução da operação";
	
	private Long id;
	@Setter(value = AccessLevel.NONE)
	private Boolean sucesso;
	private String mensagem;

	public MensagemRepresentation(Long id) {
		this.id = id;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
		this.setMensagem(getSucesso() ? MENSAGEM_SUCESSO : MENSAGEM_FALHA);
	}
}
