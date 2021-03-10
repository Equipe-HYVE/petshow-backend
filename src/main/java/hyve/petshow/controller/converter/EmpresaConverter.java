package hyve.petshow.controller.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import hyve.petshow.controller.representation.EmpresaRepresentation;
import hyve.petshow.domain.Empresa;

@Component
public class EmpresaConverter implements Converter<Empresa, EmpresaRepresentation> {

	@Override
	public EmpresaRepresentation toRepresentation(Empresa domain) {
		return Optional.ofNullable(domain)
				.map(empresa -> {
					var representation = new EmpresaRepresentation();
					representation.setId(domain.getId());
					representation.setCnpj(domain.getCnpj());
					representation.setEndereco(domain.getEndereco());
					representation.setNome(domain.getNome());
					representation.setRazaoSocial(domain.getRazaoSocial());
					representation.setGeolocalizacao(empresa.getGeolocalizacao());
					return representation;
				}).orElse(null);
	}

	@Override
	public Empresa toDomain(EmpresaRepresentation representation) {
		return Optional.ofNullable(representation)
				.map(empresa -> {
					var domain = new Empresa();
					domain.setId(empresa.getId());
					domain.setCnpj(empresa.getCnpj());
					domain.setEndereco(empresa.getEndereco());
					domain.setNome(empresa.getNome());
					domain.setRazaoSocial(empresa.getRazaoSocial());
					domain.setGeolocalizacao(empresa.getGeolocalizacao());
					return domain;
				}).orElse(null);
	}

}
