package hyve.petshow.service.implementation;

import hyve.petshow.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class TipoService <T> {
	private final String NENHUM_ENCONTRADO;
	
	public TipoService(String mensagem) {
		NENHUM_ENCONTRADO = mensagem;
	}
	
	public abstract List<T> buscarLista();
	
	public List<T> buscarTodos() throws NotFoundException {
		var lista = buscarLista();
		if(lista.isEmpty()) {
			throw new NotFoundException(NENHUM_ENCONTRADO);
		}
		return lista;
	}
}
