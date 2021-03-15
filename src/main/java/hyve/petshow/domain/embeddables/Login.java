package hyve.petshow.domain.embeddables;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
@NoArgsConstructor
public class Login {
	@Size(max = 256, message = "O email informado ultrapassa o limite de 256 caracteres.")
	@NotNull(message = "O email é obrigatório.")
	private String email;
	@NotNull(message = "A senha é obrigatória.")
	private String senha;

	public Login(String email){
		this.email = email;
	}
}
