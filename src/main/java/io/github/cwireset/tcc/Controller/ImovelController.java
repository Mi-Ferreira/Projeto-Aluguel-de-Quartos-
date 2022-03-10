package io.github.cwireset.tcc.Controller;

import io.github.cwireset.tcc.Request.CadastrarImovelRequest;
import io.github.cwireset.tcc.Service.ImovelService;
import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    ImovelService imovelService;
    private Long idImovel;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @PostMapping
    public ResponseEntity<Imovel> cadastrarImovel(@RequestBody @Valid CadastrarImovelRequest
                                                              cadastrarImovelRequest) throws Exception {
        return this.imovelService.cadastrarImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> listarTodosImoveisPaginados(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        return this.imovelService.listarTodosImoveisPaginados(page,size,sort);
    }


    @GetMapping(path = "/proprietarios/{idProprietario}")
    public Page<Imovel> listarTodosImoveisPorProprietarioPaginados(
            @PathVariable Long idProprietario,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        return this.imovelService.listarTodosImoveisPorProprietarioPaginados(idProprietario, page,size,sort);
    }

    @GetMapping(path = "/{idImovel}")
    public Imovel procurarImovel(@PathVariable Long idImovel) throws Exception {
        return this.imovelService.procurarImovel(idImovel);
    }

    @DeleteMapping(path = "/{idImovel}")
    public void deletarImovel(@PathVariable Long idImovel) throws Exception {
        this.imovelService.deletarImovel(idImovel);
    }
}