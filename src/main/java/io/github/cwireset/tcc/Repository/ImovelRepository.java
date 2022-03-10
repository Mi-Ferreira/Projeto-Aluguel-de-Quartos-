package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImovelRepository extends PagingAndSortingRepository<Imovel, Long> {
}
