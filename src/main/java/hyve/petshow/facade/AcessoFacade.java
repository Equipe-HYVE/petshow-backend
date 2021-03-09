package hyve.petshow.facade;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hyve.petshow.controller.converter.ContaConverter;
import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.representation.ContaRepresentation;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.domain.Prestador;
import hyve.petshow.service.port.AcessoService;
import hyve.petshow.service.port.EmpresaService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.util.AuditoriaUtils;

@Component
public class AcessoFacade {
	@Autowired
	private PrestadorService prestadorService;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private AcessoService acessoService;
	@Autowired
	private PrestadorConverter prestadorConverter;
	@Autowired
	private ContaConverter contaConverter;

	public ContaRepresentation salvaConta(ContaRepresentation conta) throws Exception {
		var domain = contaConverter.toDomain(conta);
		return contaConverter.toRepresentation(acessoService.adicionarConta(domain));
	}

	public PrestadorRepresentation salvaPrestador(PrestadorRepresentation representation) throws Exception {
		var domain = prestadorConverter.toDomain(representation);
		var prestador = new Prestador(acessoService.adicionarConta(domain));
		var empresa = domain.getEmpresa();

		var empresaSalva = empresaService.salvarEmpresa(empresa, prestador.getId());
		prestador.setEmpresa(empresaSalva);
		prestadorService.atualizarConta(prestador.getId(), prestador, AuditoriaUtils.INATIVO);
		return prestadorConverter.toRepresentation(prestadorService.buscarPorId(prestador.getId()));
	}
}
