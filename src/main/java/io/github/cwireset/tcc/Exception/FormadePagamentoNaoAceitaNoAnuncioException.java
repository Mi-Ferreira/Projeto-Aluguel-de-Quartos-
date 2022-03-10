package io.github.cwireset.tcc.Exception;

import io.github.cwireset.tcc.domain.FormaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormadePagamentoNaoAceitaNoAnuncioException extends Exception {

    public FormadePagamentoNaoAceitaNoAnuncioException(
            FormaPagamento formaPagamento,
            String pagamentosAceitos
    ) {
        super(String.format("O anúncio não aceita '%s' como forma de pagamento. As formas aceitas são '%s' "
                ,formaPagamento.toString(),pagamentosAceitos));
    }
}
