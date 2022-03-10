package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.IdImovelNaoExisteException;
import io.github.cwireset.tcc.Exception.IdNaoExisteException;
import io.github.cwireset.tcc.Exception.NaoDeletarImovelQueTemAnuncioException;
import io.github.cwireset.tcc.Repository.AnuncioRepository;
import io.github.cwireset.tcc.Repository.ImovelRepository;
import io.github.cwireset.tcc.Repository.UsuarioRepository;
import io.github.cwireset.tcc.Request.CadastrarImovelRequest;
import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.jetbrains.annotations.NotNull;
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
public class ImovelService {

    @Autowired
    private final ImovelRepository imovelRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public final UsuarioService usuarioService;
    @Autowired
    private final AnuncioRepository anuncioRepository;
    @Autowired
    private final ImovelServiceInativo imovelServiceInativo;
    public Long idProprietario;
    public Long idImovel;

    public ImovelService(ImovelRepository imovelRepository, UsuarioRepository usuarioRepository,
                         UsuarioService usuarioService, AnuncioRepository anuncioRepository,
                         ImovelServiceInativo imovelServiceInativo) {
        this.imovelRepository = imovelRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.anuncioRepository = anuncioRepository;
        this.imovelServiceInativo = imovelServiceInativo;
    }

    public ResponseEntity<Imovel> cadastrarImovel(@NotNull CadastrarImovelRequest cadastrarImovelRequest)
            throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(cadastrarImovelRequest.getIdProprietario());
        if (usuario.isPresent()) {
            Usuario usuario1 = usuario.get();
            Imovel imovel = new Imovel();
            imovel.setIdentificacao(cadastrarImovelRequest.getIdentificacao());
            imovel.setTipoImovel(cadastrarImovelRequest.getTipoImovel());
            imovel.setEndereco(cadastrarImovelRequest.getEndereco());
            imovel.setProprietario(usuario1);
            imovel.setCaracteristicas(cadastrarImovelRequest.getCaracteristicas());
            imovelRepository.save(imovel);
            return new ResponseEntity<>(imovel, HttpStatus.CREATED);
        } else {
            throw new IdNaoExisteException(cadastrarImovelRequest.getIdProprietario());
        }
    }

    public Page<Imovel> listarTodosImoveisPaginados(Integer page, Integer pageCount, String direction) {
        PageRequest pageRequest = PageRequest.of(page, pageCount,
                Sort.Direction.valueOf(direction), "identificacao");
        return imovelRepository.findAll(pageRequest);
    }

    public Page<Imovel> listarTodosImoveisPorProprietarioPaginados(Long idProprietario, Integer page,
                                                                   Integer pageCount, String direction) {
        this.idProprietario = idProprietario;
        List<Imovel> imoveis = (List<Imovel>) imovelRepository.findAll();
        List<Imovel> imoveisPorProprietario = new ArrayList<>();

        for (Imovel imovel : imoveis) {
            if (imovel.getProprietario().getId().equals(idProprietario)) {
                imoveisPorProprietario.add(imovel);
            }
        }

        PageRequest pageRequest = PageRequest.of(page, pageCount, Sort.Direction.valueOf(direction),
                "identificacao");
        Page<Imovel> pageImovel;
        pageImovel = new PageImpl<>(imoveisPorProprietario, pageRequest, imoveisPorProprietario.size());
        return pageImovel;
    }

    public Imovel procurarImovel(Long idImovel) throws Exception {
        this.idImovel = idImovel;
        List<Imovel> imoveis = (List<Imovel>) imovelRepository.findAll();

        for (Imovel imovel : imoveis) {
            if (idImovel.equals(imovel.getId())) {
                return imovel;
            }
        }
        throw new IdImovelNaoExisteException(idImovel);
    }

    public void deletarImovel(Long idImovel) throws Exception {
        Optional<Imovel> imovelOptional = imovelRepository.findById(idImovel);
        List<Anuncio> anuncios = (List<Anuncio>) anuncioRepository.findAll();

        if (imovelOptional.isPresent()) {
            for (Anuncio anuncio : anuncios) {
                if (anuncio.getImovel().getId().equals(idImovel)) {
                    throw new NaoDeletarImovelQueTemAnuncioException();
                }
                Imovel ImovelParaDeletar = imovelOptional.get();
                imovelServiceInativo.salvarImovelInativos(ImovelParaDeletar);
                imovelRepository.delete(ImovelParaDeletar);
            }
        } else {
            throw new IdImovelNaoExisteException(idImovel);
        }
    }

}
