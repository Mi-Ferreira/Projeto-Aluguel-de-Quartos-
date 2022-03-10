package io.github.cwireset.tcc.Exception;


import io.github.cwireset.tcc.domain.TipoImovel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantidadeDePessoasNaoPodeSerMenorQueDoisParaHotelException extends Exception {

    public QuantidadeDePessoasNaoPodeSerMenorQueDoisParaHotelException(Integer numeroPessoas, TipoImovel tipoImovel) {
        super(String.format("Não é possível realizar uma reserva com menos de '%d' pessoas para imoveis do tipo '%s'.", numeroPessoas, tipoImovel));
    }
}
