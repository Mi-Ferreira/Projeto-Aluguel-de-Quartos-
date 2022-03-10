package io.github.cwireset.tcc.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.cwireset.tcc.domain.Endereco;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequest {

    @NotNull
    private String nome;

    @NotNull
    private String email;

    @JsonIgnore
    private String senha;

    @NotNull
    private LocalDate dataNascimento;

    private Endereco endereco;
}
