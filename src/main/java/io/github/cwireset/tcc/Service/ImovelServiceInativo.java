package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Repository.ImovelRepositoryInativo;
import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImovelServiceInativo {
    @Autowired
    private final ImovelRepositoryInativo imovelRepositoryInativo;

    public ImovelServiceInativo(ImovelRepositoryInativo imovelRepositoryInativo) {
        this.imovelRepositoryInativo = imovelRepositoryInativo;
    }

    public void salvarImovelInativos(Imovel imovelParaDeletar) {

        imovelRepositoryInativo.save(imovelParaDeletar);
    }
}
