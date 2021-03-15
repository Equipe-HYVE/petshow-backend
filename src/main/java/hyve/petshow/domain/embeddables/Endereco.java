package hyve.petshow.domain.embeddables;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
public class Endereco {
	@Size(max = 80, message = "O logradouro informado ultrapassa o limite de 80 caracteres.")
	@NotNull(message = "O logradouro é obrigatório.")
	private String logradouro;
	@Size(max = 8, message = "O CEP informado ultrapassa o limite de 8 caracteres.")
	@NotNull(message = "O CEP é obrigatório.")
	private String cep;
	@Size(max = 30, message = "O bairro informado ultrapassa o limite de 30 caracteres.")
	@NotNull(message = "O bairro é obrigatório.")
	private String bairro;
	@Size(max = 30, message = "A cidade informada ultrapassa o limite de 30 caracteres.")
	@NotNull(message = "A cidade é obrigatória.")
	private String cidade;
	@Size(max = 2, message = "O estado informado ultrapassa o limite de 2 caracteres.")
	@NotNull(message = "O estado é obrigatório.")
	private String estado;
	@Size(max = 3, message = "O número informado ultrapassa o limite de 3 caracteres.")
	@NotNull(message = "O número é obrigatório.")
	private String numero;
	@Size(max = 30, message = "O complemento informado ultrapassa o limite de 30 caracteres.")
	private String complemento; 
}
