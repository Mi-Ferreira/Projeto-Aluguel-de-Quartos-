package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Anuncio;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnuncioRepository extends PagingAndSortingRepository<Anuncio, Long> {
}
