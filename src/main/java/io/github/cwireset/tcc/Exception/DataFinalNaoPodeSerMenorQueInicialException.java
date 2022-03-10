package io.github.cwireset.tcc.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataFinalNaoPodeSerMenorQueInicialException extends Exception {

    public DataFinalNaoPodeSerMenorQueInicialException() {
        super("Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
    }

}
