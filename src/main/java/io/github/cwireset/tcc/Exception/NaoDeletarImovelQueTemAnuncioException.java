package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NaoDeletarImovelQueTemAnuncioException extends Exception {

    public NaoDeletarImovelQueTemAnuncioException() {
        super("Não é possível excluir um imóvel que possua anúncio.");
    }
}



