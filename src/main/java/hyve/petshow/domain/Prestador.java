package hyve.petshow.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(value="P") //Prestador
public class Prestador extends Conta {
	private String descricao;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_conta")
	private List<ServicoDetalhado> servicosPrestados;

	public Prestador(Conta conta){
		super(conta.getId(), conta.getNome(), conta.getNomeSocial(), conta.getCpf(), conta.getTelefone(),
				conta.getMediaAvaliacao(), conta.getFoto(), conta.getTipo(), conta.getEndereco(),
				conta.getLogin(), conta.getAuditoria(), conta.getGeolocalizacao());
	}

	public Prestador(Conta conta, List<ServicoDetalhado> servicosDetalhados){
		this(conta);
		setServicosPrestados(servicosDetalhados);
	}
}
