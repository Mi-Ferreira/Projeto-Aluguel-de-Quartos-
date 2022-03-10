package io.github.cwireset.tcc.Response;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import lombok.*;

import javax.persistence.Entity;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosAnuncioResponse {
    private Long idAnuncioResponse;
    private Imovel imovel;
    private Usuario anunciante;
    private List<FormaPagamento> formasAceitas;
    private String descricao;
}
