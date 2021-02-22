package hyve.petshow.controller.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComparacaoWrapper {
	private List<ComparacaoRepresentation> servicosComparados = new ArrayList<>();
	private Map<String, Long> comparacoes = new HashMap<String, Long>();
}
