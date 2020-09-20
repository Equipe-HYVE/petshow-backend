package hyve.petshow.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Cliente;
//import hyve.petshow.controller.representation.ServicoDetalhadoResponseRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoDetalhadoRepository;
import hyve.petshow.service.port.ServicoDetalhadoService;

@Service
public class ServicoDetalhadoServiceImpl implements ServicoDetalhadoService {

    private static final String MENSAGEM_SUCESSO = "Opera��o executada com sucesso!";
    private static final String MENSAGEM_FALHA = "Falha durante a execu��o da opera��o.";

    @Autowired
    private ServicoDetalhadoRepository repository;

    //criarServicos
    @Override
    public List<ServicoDetalhado> criarServico(List<ServicoDetalhado> servicoDetalhado) {
        return repository.save(servicoDetalhado);
    }
    

    @Override
    public List<ServicoDetalhado> buscaServicosPorPrestador(Long id) {
        return repository.findByPrestador(id);
    }

    //
    @Override
    public Optional<ServicoDetalhado> atualizarServicoDetalhado(Long id, ServicoDetalhado servicoDetalhadoRequest) {
        Optional<ServicoDetalhado> servicoDetalhadoOptional = repository.findById(id);
        Optional<ServicoDetalhado> response = Optional.empty();

        if(servicoDetalhadoOptional.isPresent()){
        	ServicoDetalhado servicoDetalhado = servicoDetalhadoOptional.get();
           
        	servicoDetalhado.setPreco(servicoDetalhadoRequest.getPreco());
        	
            response = Optional.of(repository.save(servicoDetalhado));
        }

        return response;
    }

    @Override
    public MensagemRepresentation removerServicoDetalhado(Long id) {
    	repository.deleteById(id);
		MensagemRepresentation mensagem = new MensagemRepresentation();
		mensagem.setId(id);
		mensagem.setSucesso(!repository.existsById(id));
		return mensagem;
    }
}

