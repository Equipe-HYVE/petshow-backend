package hyve.petshow.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(value = "P") // Prestador
@ToString(callSuper = true)
public class Prestador extends Conta {
	private String descricao;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_prestador")
	private List<ServicoDetalhado> servicosPrestados = new ArrayList<ServicoDetalhado>();

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_empresa")
	private Empresa empresa;

	public Prestador(Conta conta) {
		super(conta.getId(), conta.getNome(), conta.getNomeSocial(), conta.getCpf(), conta.getTelefone(),
				conta.getMediaAvaliacao(), conta.getFoto(), conta.getTipo(), conta.getEndereco(), conta.getLogin(),
				conta.getAuditoria(), conta.getGeolocalizacao());
	}

	public Prestador(Conta conta, List<ServicoDetalhado> servicosDetalhados) {
		this(conta);
		setServicosPrestados(servicosDetalhados);
	}
}
