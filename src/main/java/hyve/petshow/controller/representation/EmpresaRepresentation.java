package hyve.petshow.controller.representation;


import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmpresaRepresentation {
	private Long id;
	@Size(max = 80, message = "O nome informado ultrapassa o limite de 80 caracteres.")
	@NotNull(message = "O nome da empresa é obrigatório.")
	private String nome;
	@Size(max = 90, message = "A razão social informada ultrapassa o limite de 90 caracteres.")
	@NotNull(message = "A razão social da empresa é obrigatória.")
	private String razaoSocial;
	@Size(max = 15, message = "O CNPJ informado ultrapassa o limite de 15 caracteres.")
	@NotNull(message = "O CNPJ da empresa é obrigatório.")
	private String cnpj;
	@NotNull(message = "O endereço da empresa é obrigatório.")
	private Endereco endereco;
	@NotNull(message = "A geolocalização da empresa é obrigatória.")
	private Geolocalizacao geolocalizacao;
}
