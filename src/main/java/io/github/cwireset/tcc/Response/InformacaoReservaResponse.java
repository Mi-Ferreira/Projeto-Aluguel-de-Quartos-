package io.github.cwireset.tcc.Response;

import io.github.cwireset.tcc.domain.Pagamento;
import io.github.cwireset.tcc.domain.Periodo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformacaoReservaResponse {

    private Long idReservaResponse;

    private DadosSolicitanteResponse dadosSolicitanteResponse;

    private DadosAnuncioResponse dadosAnuncioResponse;

    private Periodo periodo;

    private Pagamento pagamento;

    private Integer quantidadePessoas;
}
