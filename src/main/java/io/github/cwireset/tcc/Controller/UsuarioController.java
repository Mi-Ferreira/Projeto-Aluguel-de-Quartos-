package io.github.cwireset.tcc.Controller;

import io.github.cwireset.tcc.Request.UsuarioRequest;
import io.github.cwireset.tcc.Service.UsuarioService;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody @Valid Usuario usuario) throws Exception {
        this.usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }


    @GetMapping
    public Page<Usuario> listarTodosUsuariosPaginados(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        return this.usuarioService.listarTodosUsuariosPaginados(page,size,sort);
    }


    @GetMapping(path = "/{idUsuario}")
    public ResponseEntity<?> procurarUsuarioId(@PathVariable Long idUsuario) throws Exception {
        return this.usuarioService.procurarUsuarioId(idUsuario);
    }

    @GetMapping(path = "/cpf/{cpf}")
    public ResponseEntity<?> procurarUsuarioCpf(@PathVariable String cpf) throws Exception {
        return this.usuarioService.procurarUsuarioCpf(cpf);
    }

    @Transient
    @PutMapping(path = "/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequest
            usuarioRequest) throws Exception {
        return this.usuarioService.atualizarUsuario(id, usuarioRequest);
    }
}
