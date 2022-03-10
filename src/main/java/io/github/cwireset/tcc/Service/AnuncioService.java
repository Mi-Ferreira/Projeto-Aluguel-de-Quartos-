package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.*;
import io.github.cwireset.tcc.Repository.AnuncioRepository;
import io.github.cwireset.tcc.Repository.AnuncioRepositoryServiceInativo;
import io.github.cwireset.tcc.Repository.ImovelRepository;
import io.github.cwireset.tcc.Repository.UsuarioRepository;
import io.github.cwireset.tcc.Request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    @Autowired
    private final ImovelRepository imovelRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final AnuncioRepository anuncioRepository;
    @Autowired
    private final AnuncioRepositoryServiceInativo anuncioRepositoryServiceInativo;
    @Autowired
    private final AnuncioServiceInativo anuncioServiceInativo;
    private Object Imovel;

    public AnuncioService(ImovelRepository imovelRepository, UsuarioRepository usuarioRepository, AnuncioRepository anuncioRepository, AnuncioRepositoryServiceInativo anuncioRepositoryServiceInativo, AnuncioServiceInativo anuncioServiceInativo) {
        this.imovelRepository = imovelRepository;
        this.usuarioRepository = usuarioRepository;
        this.anuncioRepository = anuncioRepository;
        this.anuncioRepositoryServiceInativo = anuncioRepositoryServiceInativo;
        this.anuncioServiceInativo = anuncioServiceInativo;
    }

    public ResponseEntity<Anuncio> anunciarImovel(CadastrarAnuncioRequest cadastrarAnuncioRequest)
            throws ImovelJaTemAnuncioException, IdImovelNaoExisteException, IdNaoExisteException {
        Optional<Usuario> usuario = usuarioRepository.findById(cadastrarAnuncioRequest.getIdAnunciante());
        Optional<Imovel> imovel = imovelRepository.findById(cadastrarAnuncioRequest.getIdImovel());
        List<Anuncio> anuncios = (List<Anuncio>) anuncioRepository.findAll();

        if (usuario.isPresent()) {
            if (imovel.isPresent()) {
                for (Anuncio anuncio : anuncios) {
                    if (anuncio.getImovel().getId().equals(cadastrarAnuncioRequest.getIdImovel())) {
                        throw new ImovelJaTemAnuncioException(cadastrarAnuncioRequest.getIdImovel());
                    }
                }
                Usuario usuario1 = usuario.get();
                Imovel imovel2 = imovel.get();
                Anuncio anuncio = new Anuncio();
                anuncio.setImovel(imovel2);
                anuncio.setAnunciante(usuario1);
                anuncio.setTipoAnuncio(cadastrarAnuncioRequest.getTipoAnuncio());
                anuncio.setValorDiaria(cadastrarAnuncioRequest.getValorDiaria());
                anuncio.setValorDiaria(cadastrarAnuncioRequest.getValorDiaria());
                anuncio.setFormasAceitas(cadastrarAnuncioRequest.getFormasAceitas());
                anuncio.setDescricao(cadastrarAnuncioRequest.getDescricao());
                anuncioRepository.save(anuncio);
                return new ResponseEntity<>(anuncio, HttpStatus.CREATED);
            }
            throw new IdImovelNaoExisteException(cadastrarAnuncioRequest.getIdImovel());
        }

        throw new IdNaoExisteException(cadastrarAnuncioRequest.getIdAnunciante());
    }

    public Page<Anuncio> listarTodosAnunciosPaginados(Integer page, Integer pageCount, String direction) {
        PageRequest pageRequest = PageRequest.of(page, pageCount,
                Sort.Direction.valueOf(direction), "valorDiaria");
        return anuncioRepository.findAll(pageRequest);
    }

    public Page<Anuncio> listarTodosAnunciosPorAnunciantePaginados(Long idAnunciante, Integer page,
                                                                   Integer pageCount, String direction) {
        List<Anuncio> imoveisDoAnunciante = new ArrayList<>();
        List<Anuncio> anunciantes = (List<Anuncio>) anuncioRepository.findAll();

        for (Anuncio anuncio : anunciantes) {
            if (anuncio.getAnunciante().getId().equals(idAnunciante)) {
                imoveisDoAnunciante.add(anuncio);
            }
        }
        PageRequest pageRequest = PageRequest.of(page, pageCount,
                Sort.Direction.valueOf(direction), "valorDiaria");
        Page<Anuncio> pageAnuncio;
        pageAnuncio = new PageImpl<>(imoveisDoAnunciante, pageRequest, imoveisDoAnunciante.size());
        return pageAnuncio;
    }

    public void deletarAnuncio(Long idAnuncio) throws IdAnuncioNaoExisteException {
        Optional<Anuncio> anuncioExistente = anuncioRepository.findById(idAnuncio);

        if (anuncioExistente.isPresent()) {
            Anuncio AnuncioParaDeletar = anuncioExistente.get();
            anuncioServiceInativo.salvarInativos(AnuncioParaDeletar);
            anuncioRepository.delete(AnuncioParaDeletar);
        } else {
            throw new IdAnuncioNaoExisteException(idAnuncio);
        }
    }

}
