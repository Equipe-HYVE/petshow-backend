package hyve.petshow.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "C") // Cliente
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Conta {
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fk_conta")
	private List<AnimalEstimacao> animaisEstimacao;

	public Cliente(Conta conta) {
		super(conta.getId(), conta.getNome(), conta.getNomeSocial(), conta.getCpf(), conta.getTelefone(),
				conta.getMediaAvaliacao(), conta.getFoto(), conta.getTipo(), conta.getEndereco(),
				conta.getLogin(), conta.getAuditoria(), conta.getGeolocalizacao());
	}

	public Cliente(Conta conta, List<AnimalEstimacao> animaisEstimacao){
		this(conta);
		setAnimaisEstimacao(animaisEstimacao);
	}
}
