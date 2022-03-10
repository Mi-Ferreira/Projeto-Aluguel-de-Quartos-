package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.*;
import io.github.cwireset.tcc.Repository.AnuncioRepository;
import io.github.cwireset.tcc.Repository.ReservaRepository;
import io.github.cwireset.tcc.Repository.UsuarioRepository;
import io.github.cwireset.tcc.Request.CadastrarReservaRequest;
import io.github.cwireset.tcc.Response.DadosAnuncioResponse;
import io.github.cwireset.tcc.Response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.Response.InformacaoReservaResponse;
import io.github.cwireset.tcc.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RealizarReservaService {
    @Autowired
    private final ReservaRepository reservaRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final AnuncioRepository anuncioRepository;


    public RealizarReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository,
                                  AnuncioRepository anuncioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.anuncioRepository = anuncioRepository;
    }

    public ResponseEntity<InformacaoReservaResponse> realizarReserva(CadastrarReservaRequest cadastrarReservaRequest)
            throws Exception {
        verificarSeIdSolicitanteExisteEIdAnuncianteExiste(cadastrarReservaRequest.getIdSolicitante(), cadastrarReservaRequest.getIdAnuncio());
        verificarSeDataFinalMaiorQueInicial(cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal());
        Optional<Usuario> usuario = usuarioRepository.findById(cadastrarReservaRequest.getIdSolicitante());
        Optional<Anuncio> anuncio = anuncioRepository.findById(cadastrarReservaRequest.getIdAnuncio());
        if (usuario.isPresent()) {
            if (anuncio.isPresent()) {
                Usuario usuario1 = usuario.get();
                Anuncio anuncio1 = anuncio.get();
                verificarSeAnuncianteEIgualSolicitante(cadastrarReservaRequest.getIdSolicitante(), anuncio1.getAnunciante().getId());
                verificarNumeroMinimoDePessoasNoHotel(cadastrarReservaRequest.getQuantidadePessoas(), anuncio1.getImovel().getTipoImovel());
                verificarNumeroMinimoDeDiariaENumeroMinimoDeDiariaParaPousada(cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal(), anuncio1.getImovel().getTipoImovel());
                verificarSeOAnuncioJaPossuiReservaAtivaParaDataFinalEDataInicial(cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal(), anuncio1.getImovel().getId());


                //SUBSCREVENDO HORA FINAL E INICIAL E SETANDO PERIODO
                Periodo periodo = new Periodo();
                periodo.setDataHoraInicial(sobscreverHoraInicial(cadastrarReservaRequest.getPeriodo().getDataHoraInicial()));
                periodo.setDataHoraFinal(sobscreverHoraFinal(cadastrarReservaRequest.getPeriodo().getDataHoraFinal()));

                //CALCULANDO VALOR TOTAL DA DIÁRIA E SETANDO PAGAMENTO
                long dias = periodo.getDataHoraInicial().until(periodo.getDataHoraFinal(), ChronoUnit.DAYS);
                BigDecimal valorTotal = new BigDecimal(dias * anuncio1.getValorDiaria().longValue());
                Pagamento pagamento = new Pagamento();
                pagamento.setStatus(StatusPagamento.PENDENTE);
                pagamento.setFormaEscolhida(FormaPagamento.NULL);
                pagamento.setValorTotal(valorTotal);

                //REALIZANDO A RESERVA
                Reserva reserva = new Reserva();
                reserva.setPeriodo(periodo);
                reserva.setSolicitante(usuario1);
                reserva.setAnuncio(anuncio1);
                reserva.setQuantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas());
                reserva.setDataHoraReserva(LocalDateTime.now());
                reserva.setPagamento(pagamento);
                reservaRepository.save(reserva);
                //CRIANDO A RESPOSTA DA RESERVA
                DadosSolicitanteResponse dadosSolicitanteResponse = new DadosSolicitanteResponse();
                dadosSolicitanteResponse.setIdSolicitanteResponse(cadastrarReservaRequest.getIdSolicitante());
                dadosSolicitanteResponse.setNomeSolicitanteResponse(usuario1.getNome());

                DadosAnuncioResponse dadosAnuncioResponse = new DadosAnuncioResponse();
                dadosAnuncioResponse.setIdAnuncioResponse(cadastrarReservaRequest.getIdAnuncio());
                dadosAnuncioResponse.setImovel(anuncio1.getImovel());
                dadosAnuncioResponse.setAnunciante(anuncio1.getAnunciante());
                dadosAnuncioResponse.setFormasAceitas(anuncio1.getFormasAceitas());
                dadosAnuncioResponse.setDescricao(anuncio1.getDescricao());

                InformacaoReservaResponse informacaoReservaResponse = new InformacaoReservaResponse();
                informacaoReservaResponse.setIdReservaResponse(reserva.getId());
                informacaoReservaResponse.setDadosSolicitanteResponse(dadosSolicitanteResponse);
                informacaoReservaResponse.setDadosAnuncioResponse(dadosAnuncioResponse);
                informacaoReservaResponse.setPagamento(reserva.getPagamento());
                informacaoReservaResponse.setPeriodo(reserva.getPeriodo());
                informacaoReservaResponse.setQuantidadePessoas(reserva.getQuantidadePessoas());
                return new ResponseEntity<>(informacaoReservaResponse, HttpStatus.CREATED);
            }
        }
        return null;
    }

    private void verificarSeIdSolicitanteExisteEIdAnuncianteExiste(Long idSolicitante, Long idAnuncio) throws IdNaoExisteException, IdAnuncioNaoExisteException {
        Optional<Usuario> usuario = usuarioRepository.findById(idSolicitante);
        Optional<Anuncio> anuncio = anuncioRepository.findById(idAnuncio);
        if (usuario.isEmpty()) {
            throw new IdNaoExisteException(idSolicitante);
        }
        if (anuncio.isEmpty()) {
            throw new IdAnuncioNaoExisteException(idAnuncio);

        }

    }

    private LocalDateTime sobscreverHoraInicial(LocalDateTime dataHoraInicial) {
        //NÃO USEI A COMPARAÇÃO POIS A HORA INICIAL SEMPRE SERÁ A MESMA
        LocalDate dataSubscrita = dataHoraInicial.toLocalDate();
        return dataSubscrita.atTime(14, 0, 0);
    }

    private LocalDateTime sobscreverHoraFinal(LocalDateTime dataHoraFinal) {
        //NÃO USEI A COMPARAÇÃO POIS A HORA FINAL SEMPRE SERÁ A MESMA
        LocalDate dataSubscrita = dataHoraFinal.toLocalDate();
        return dataSubscrita.atTime(12, 0, 0);
    }

    private void verificarSeDataFinalMaiorQueInicial(LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal)
            throws DataFinalNaoPodeSerMenorQueInicialException {
        //LIDANDO COM A EXCESSÂO QUE A DATA FINAL NÃO PODE SER MENOR QUE A INICIAL
        LocalDate dataInicial = dataHoraInicial.toLocalDate();
        LocalDate dataFinal = dataHoraFinal.toLocalDate();
        if (dataInicial.isAfter(dataFinal)) {
            throw new DataFinalNaoPodeSerMenorQueInicialException();
        }
    }

    private void verificarSeOAnuncioJaPossuiReservaAtivaParaDataFinalEDataInicial(LocalDateTime dataHoraInicial,
                                                                                  LocalDateTime dataHoraFinal,
                                                                                  Long idImovel)
            throws JaPossuiReservaParaAsDatasInformadasException {
        //LIDANDO COM A EXCESSÃO QUE A RESERVA ATIVA NÃO PODE SOBRESCREVER DATA INICIAL NEM A FINAL
        List<Reserva> reservas = (List<Reserva>) reservaRepository.findAll();

        for (Reserva reserva : reservas) {
            if (reserva.getAnuncio().getImovel().getId().equals(idImovel)) {
                if ((reserva.getPagamento().getStatus() == StatusPagamento.PENDENTE) || (reserva.getPagamento().getStatus() == StatusPagamento.PAGO)) {
                    if ((dataHoraInicial.isAfter(reserva.getPeriodo().getDataHoraInicial()) && dataHoraFinal.isBefore(reserva.getPeriodo().getDataHoraFinal()))
                            || (dataHoraInicial.isAfter(reserva.getPeriodo().getDataHoraInicial()) && dataHoraInicial.isBefore(reserva.getPeriodo().getDataHoraFinal()))
                            || (dataHoraFinal.isBefore(reserva.getPeriodo().getDataHoraFinal()) && dataHoraFinal.isAfter(reserva.getPeriodo().getDataHoraInicial()))
                            || (dataHoraInicial.isBefore(reserva.getPeriodo().getDataHoraInicial()) && dataHoraFinal.isAfter(reserva.getPeriodo().getDataHoraFinal())))
                        throw new JaPossuiReservaParaAsDatasInformadasException();
                }
            }
        }
    }

    private void verificarNumeroMinimoDeDiariaENumeroMinimoDeDiariaParaPousada(LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, TipoImovel tipoImovel) throws DiariaNaoPodeSerMenorqueUmException, DiariaNaoPodeSerMenorQueCincoParaPousadaException {
        //LIDANDO COM EXCESSÃO QUE A DIARIA TEM QUE SER MINIMO 1 DIA E COM A EXCESSÃO QUE PARA POUSADA TEM QUE SER NO MINIMO 5
        long dias = dataHoraInicial.until(dataHoraFinal, ChronoUnit.DAYS);
        if ((dias < 5) && (tipoImovel == TipoImovel.POUSADA)) {
            int diaria = 5;
            throw new DiariaNaoPodeSerMenorQueCincoParaPousadaException(diaria, TipoImovel.POUSADA);
        }
        if (dias < 1) throw new DiariaNaoPodeSerMenorqueUmException();
    }

    private void verificarSeAnuncianteEIgualSolicitante(Long idSolicitante, Long idAnunciante) throws Exception {
        //LIDANDO COM A EXCESSÃO QUE O ANUNCIANTE NÃO PODE SER O MESMO SOLICITANTE
        if (idSolicitante.equals(idAnunciante)) throw new IdSolicitanteNaoPodeSerIgualAnuncianteException();
    }

    private void verificarNumeroMinimoDePessoasNoHotel(Integer quantidadePessoas, TipoImovel tipoImovel) throws Exception {
        //LIDANDO COM A EXCESSÃO QUE O NUMERO DE PESSOAS PARA HOTEL TEM QUE SER NO MINIMO 2
        if ((quantidadePessoas < 2) && (tipoImovel == TipoImovel.HOTEL)) {
            throw new QuantidadeDePessoasNaoPodeSerMenorQueDoisParaHotelException(2, TipoImovel.HOTEL);
        }
    }
}