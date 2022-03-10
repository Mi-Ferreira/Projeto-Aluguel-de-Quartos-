package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Repository.AnuncioRepositoryServiceInativo;
import io.github.cwireset.tcc.domain.Anuncio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnuncioServiceInativo {
    @Autowired
    private final AnuncioRepositoryServiceInativo anuncioRepositoryServiceInativo;

    public AnuncioServiceInativo(AnuncioRepositoryServiceInativo anuncioRepositoryServiceInativo) {
        this.anuncioRepositoryServiceInativo = anuncioRepositoryServiceInativo;
    }

    public void salvarInativos(Anuncio anuncioParaDeletar) {
        anuncioRepositoryServiceInativo.save(anuncioParaDeletar);
    }
}
