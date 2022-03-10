package io.github.cwireset.tcc.Repository;

import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Usuario> findById(Long idUsuario);

    Optional<Usuario> findByCpf(String cpf);

}
