package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdReservaNaoExisteException extends Exception {

    public IdReservaNaoExisteException(Long idReserva) {
        super(String.format("Nenhum(a) Reserva com Id com o valor '%d' foi encontrado.", idReserva));
    }
}
