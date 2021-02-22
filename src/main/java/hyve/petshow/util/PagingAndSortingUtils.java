package hyve.petshow.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagingAndSortingUtils {
    public static Pageable geraPageable(Integer pagina, Integer quantidadeItens){
        return PageRequest.of(pagina, quantidadeItens);
    }
}
