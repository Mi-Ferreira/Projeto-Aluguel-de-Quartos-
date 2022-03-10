package io.github.cwireset.tcc.Response;


import lombok.*;

import javax.persistence.Entity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosSolicitanteResponse {

    private long IdSolicitanteResponse;
    private String nomeSolicitanteResponse;
}
