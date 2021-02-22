package hyve.petshow.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BusinessException() {}
	
	public BusinessException(String mensagem) {
		super(mensagem);
	}

}
