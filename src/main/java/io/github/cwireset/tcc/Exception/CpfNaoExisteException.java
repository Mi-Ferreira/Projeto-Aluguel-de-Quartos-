package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CpfNaoExisteException extends Exception {

    public CpfNaoExisteException(String cpf) {
        super(String.format("Nenhum(a) Usuario com CPF com o valor '%s' foi encontrado.", cpf));
    }

}
