package io.github.cwireset.tcc.Exception;

import io.github.cwireset.tcc.domain.TipoImovel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DiariaNaoPodeSerMenorQueCincoParaPousadaException extends Exception {

    public DiariaNaoPodeSerMenorQueCincoParaPousadaException(int diaria, TipoImovel pousada) {
        super(String.format("Não é possível realizar uma reserva com menos de '%d' diárias para imoveis do tipo '%s'.", diaria, pousada));
    }
}

