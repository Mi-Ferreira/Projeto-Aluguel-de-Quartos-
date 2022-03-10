package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdSolicitanteNaoPodeSerIgualAnuncianteException extends Exception {

    public IdSolicitanteNaoPodeSerIgualAnuncianteException() {
        super("O solicitante de uma reserva não pode ser o próprio anunciante.");
    }
}
