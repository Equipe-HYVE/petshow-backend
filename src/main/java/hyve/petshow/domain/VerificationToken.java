package hyve.petshow.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String token;
	
	@OneToOne(targetEntity = Conta.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "fk_conta")
	private Conta conta;

	public VerificationToken(Conta conta, String token) {
		setConta(conta);
		setToken(token);
	}
}
