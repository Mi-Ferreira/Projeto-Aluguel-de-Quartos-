package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImovelJaTemAnuncioException extends Exception {

    public ImovelJaTemAnuncioException(Long idImovel) {
        super(String.format("Já existe um recurso do tipo Anuncio com IdImovel com o valor '%d'.", idImovel));
    }
}