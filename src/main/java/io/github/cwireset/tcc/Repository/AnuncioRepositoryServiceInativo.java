package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Anuncio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnuncioRepositoryServiceInativo extends CrudRepository<Anuncio, Long> {
}
