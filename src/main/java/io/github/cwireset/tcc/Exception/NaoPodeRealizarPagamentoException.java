package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NaoPodeRealizarPagamentoException extends Exception {

    public NaoPodeRealizarPagamentoException(){
        super("Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.");
    }
}

