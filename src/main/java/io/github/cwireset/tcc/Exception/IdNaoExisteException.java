package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNaoExisteException extends Exception {

    public IdNaoExisteException(Long id) {
        super(String.format("Nenhum(a) Usuario com Id com o valor '%d' foi encontrado.", id));
    }
}


