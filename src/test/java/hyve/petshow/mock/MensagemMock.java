package hyve.petshow.mock;

import hyve.petshow.controller.representation.MensagemRepresentation;

public class MensagemMock {
    public static MensagemRepresentation criaMensagemRepresentationSucesso(){
        var mensagemRepresentation = new MensagemRepresentation(1L);

        mensagemRepresentation.setSucesso(Boolean.TRUE);

        return mensagemRepresentation;
    }

    public static MensagemRepresentation criaMensagemRepresentationFalha(){
        var mensagemRepresentation = new MensagemRepresentation(1L);

        mensagemRepresentation.setSucesso(Boolean.FALSE);

        return mensagemRepresentation;
    }
}
