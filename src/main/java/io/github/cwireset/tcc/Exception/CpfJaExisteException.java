package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfJaExisteException extends Exception {

    public CpfJaExisteException(String cpfInformado) {
        super(String.format("Já existe um recurso do tipo Usuario com CPF com o valor '%s'.", cpfInformado));
    }
}
