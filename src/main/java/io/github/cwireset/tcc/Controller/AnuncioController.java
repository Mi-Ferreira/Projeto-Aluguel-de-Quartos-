package io.github.cwireset.tcc.Controller;

import io.github.cwireset.tcc.Request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.Service.AnuncioService;
import io.github.cwireset.tcc.domain.Anuncio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @PostMapping
    public ResponseEntity<Anuncio> anunciarImovel(@RequestBody @Valid CadastrarAnuncioRequest cadastrarAnuncioRequest)
            throws Exception {
        return this.anuncioService.anunciarImovel(cadastrarAnuncioRequest);
    }

    @GetMapping
    public Page<Anuncio> listarTodosAnunciosPaginados(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        return this.anuncioService.listarTodosAnunciosPaginados(page, size, sort);
    }


    @GetMapping(path = "/anunciantes/{idAnunciante}")
    public Page<Anuncio> listarTodosAnunciosPorAnunciantePaginados(
            @PathVariable Long idAnunciante,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort) {
        return this.anuncioService.listarTodosAnunciosPorAnunciantePaginados(idAnunciante, page, size, sort);
    }

    @DeleteMapping(path = "/{idAnuncio}")
    public void deletarAnuncio(@PathVariable Long idAnuncio) throws Exception {
        this.anuncioService.deletarAnuncio(idAnuncio);
    }


}
