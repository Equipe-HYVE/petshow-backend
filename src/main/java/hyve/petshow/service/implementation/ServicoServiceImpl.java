package hyve.petshow.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.Servico;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoRepository;
import hyve.petshow.service.port.ServicoService;

@Service
public class ServicoServiceImpl implements ServicoService {

    private static final String MENSAGEM_SUCESSO = "Opera��o executada com sucesso!";
    private static final String MENSAGEM_FALHA = "Falha durante a execu��o da opera��o.";

    @Autowired
    private ServicoRepository repository;

    @Override
    public Servico adicionarServico(Servico servico) throws Exception{
    	validaNovoServico(servico);
    	return repository.save(servico);
    }
    
    private void validaNovoServico(Servico servico) throws BusinessException {
		if (repository.buscarPorNome(servico.getNome())!=null) {
			throw new BusinessException("Servi�o j� cadastrado.");
		}
	}
    
    @Override
    public List<Servico> buscarServicos() {
        return repository.findAll();
    }

    //
    @Override
    public Servico atualizarServico(Long id, Servico servicoRequest) throws Exception{
       repository.findById(id).orElseThrow(() -> new NotFoundException("Servi�o n�o encontrado"));
       return repository.save(servicoRequest);
   }


    @Override
    public MensagemRepresentation removerServico(Long id) {
    	repository.deleteById(id);
		MensagemRepresentation mensagem = new MensagemRepresentation();
		mensagem.setId(id);
		mensagem.setSucesso(!repository.existsById(id));
		return mensagem;
    }
    
   
    
}

