package io.github.cwireset.tcc.Request;

import io.github.cwireset.tcc.domain.CaracteristicaImovel;
import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.TipoImovel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarImovelRequest {

    @NotNull
    private String identificacao;

    @NotNull
    private TipoImovel tipoImovel;

    @NotNull
    private Endereco endereco;

    @NotNull
    private Long idProprietario;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CaracteristicaImovel> caracteristicas;
}
