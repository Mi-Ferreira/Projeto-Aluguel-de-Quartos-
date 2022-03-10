package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdImovelNaoExisteException extends Exception {

    public IdImovelNaoExisteException(Long idImovel) {
        super(String.format("Nenhum(a) Imovel com Id com o valor '%d' foi encontrado.", idImovel));
    }
}
