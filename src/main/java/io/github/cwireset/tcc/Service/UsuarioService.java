package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.CpfJaExisteException;
import io.github.cwireset.tcc.Exception.CpfNaoExisteException;
import io.github.cwireset.tcc.Exception.EmailJaExisteException;
import io.github.cwireset.tcc.Exception.IdNaoExisteException;
import io.github.cwireset.tcc.Repository.UsuarioRepository;
import io.github.cwireset.tcc.Request.UsuarioRequest;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UtilizarAvatarUsuarioService utilizarAvatarUsuarioService;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        boolean emailDeUsuarioJacadastrado = usuarioRepository.existsByEmail(usuario.getEmail());
        if (emailDeUsuarioJacadastrado)
            throw new EmailJaExisteException(usuario.getEmail());

        boolean cpfDeUsuarioJacadastrado = usuarioRepository.existsByCpf(usuario.getCpf());
        if (cpfDeUsuarioJacadastrado)
            throw new CpfJaExisteException(usuario.getCpf());

        usuario.setAvatar(utilizarAvatarUsuarioService.utilizarAvatar());
        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarTodosUsuariosPaginados(Integer page, Integer pageCount, String direction) {
        PageRequest pageRequest = PageRequest.of(page, pageCount,
                Sort.Direction.valueOf(direction), "nome");
        return usuarioRepository.findAll(pageRequest);
    }

    public ResponseEntity<?> procurarUsuarioId(Long idUsuario) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            throw new IdNaoExisteException(idUsuario);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    public ResponseEntity<?> procurarUsuarioCpf(String cpf) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findByCpf(cpf);
        if (usuario.isEmpty()) {
            throw new CpfNaoExisteException(cpf);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }


    public ResponseEntity<Usuario> atualizarUsuario(Long idUsuario, UsuarioRequest usuarioRequest)
            throws EmailJaExisteException, IdNaoExisteException {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            Usuario usuario1 = usuario.get();
            usuario1.setNome(usuarioRequest.getNome());
            usuario1.setSenha(usuarioRequest.getSenha());
            usuario1.setDataNascimento(usuarioRequest.getDataNascimento());
            usuario1.getEndereco().setNumero(usuarioRequest.getEndereco().getNumero());
            usuario1.getEndereco().setEstado(usuarioRequest.getEndereco().getEstado());
            usuario1.getEndereco().setLogradouro(usuarioRequest.getEndereco().getLogradouro());
            usuario1.getEndereco().setCidade(usuarioRequest.getEndereco().getCidade());
            usuario1.getEndereco().setBairro(usuarioRequest.getEndereco().getBairro());
            usuario1.getEndereco().setComplemento(usuarioRequest.getEndereco().getComplemento());
            usuario1.getEndereco().setCep(usuarioRequest.getEndereco().getCep());

            boolean emailDeUsuarioJacadastrado = usuarioRepository.existsByEmail(usuarioRequest.getEmail());
            if (emailDeUsuarioJacadastrado) {
                throw new EmailJaExisteException(usuarioRequest.getEmail());
            }

            usuario1.setEmail(usuarioRequest.getEmail());
            usuarioRepository.save(usuario1);
            return new ResponseEntity<Usuario>(usuario1, HttpStatus.CREATED);
        } else {
            throw new IdNaoExisteException(idUsuario);
        }
    }

}
