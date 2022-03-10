package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImovelRepositoryInativo  extends CrudRepository<Imovel, Long> {
}
