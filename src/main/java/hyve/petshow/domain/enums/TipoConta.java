package hyve.petshow.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoConta {
	CLIENTE(1),
	PRESTADOR_AUTONOMO(2),
    PRESTADOR_CONTRATADO(3),
    CLIENTE_PRESTADOR_AUTONOMO(4);
	
	private static final Map<Integer, TipoConta> BY_INTEGER = new HashMap<Integer, TipoConta>();
	private Integer tipo;
	
	static {
		for(TipoConta e: values()) {
			BY_INTEGER.put(e.getTipo(), e);
		}
	}
	
	TipoConta(Integer tipo) {
		this.tipo = tipo;
	}
	
	public Integer getTipo() {
		return tipo;
	}
	
	public static TipoConta getTipoByInteger(Integer i) {
		return BY_INTEGER.get(i);
	}
}
