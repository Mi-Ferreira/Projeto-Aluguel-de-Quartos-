package io.github.cwireset.tcc.Request;

import io.github.cwireset.tcc.domain.Periodo;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarReservaRequest {

    @NotNull
    private Long idSolicitante;

    @NotNull
    private Long idAnuncio;

    @NotNull
    private Periodo periodo;

    @NotNull
    private Integer quantidadePessoas;

}
