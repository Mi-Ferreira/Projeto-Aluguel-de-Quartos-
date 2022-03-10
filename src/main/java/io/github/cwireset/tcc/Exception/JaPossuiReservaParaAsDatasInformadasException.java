package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JaPossuiReservaParaAsDatasInformadasException extends Exception {

    public JaPossuiReservaParaAsDatasInformadasException() {
        super("Este anúncio já está reservado para o período informado.");
    }
}

