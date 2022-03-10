package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdAnuncioNaoExisteException extends Exception {

    public IdAnuncioNaoExisteException(Long idAnuncio) {
        super(String.format("Nenhum(a) Anuncio com Id com o valor '%d' foi encontrado.", idAnuncio));
    }
}
