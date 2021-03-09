package hyve.petshow.domain.enums;

public enum StatusAgendamento {
	PENDENTE_PAGAMENTO("PENDENTE_PAGAMENTO"),
	OFERTA_SOLICITADA("OFERTA_SOLICITADA"),
	AGENDADO("AGENDADO"),
	CONCLUIDO("CONCLUIDO"),
	CANCELADO("CANCELADO");
	
	
	private final String value;
	
	private StatusAgendamento(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
