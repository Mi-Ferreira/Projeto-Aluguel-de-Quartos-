package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException extends Exception {

    public NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException() {
        super("Não é possível realizar o estorno para esta reserva, pois ela não está no status PAGO.");
    }
}
