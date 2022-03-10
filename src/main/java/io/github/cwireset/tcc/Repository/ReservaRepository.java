package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Reserva;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long> {
}
