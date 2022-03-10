package io.github.cwireset.tcc.Controller;

import io.github.cwireset.tcc.Exception.FormadePagamentoNaoAceitaNoAnuncioException;
import io.github.cwireset.tcc.Exception.IdReservaNaoExisteException;
import io.github.cwireset.tcc.Exception.NaoPodeRealizarPagamentoException;
import io.github.cwireset.tcc.Request.CadastrarReservaRequest;
import io.github.cwireset.tcc.Response.InformacaoReservaResponse;
import io.github.cwireset.tcc.Service.*;
import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    RealizarReservaService realizarReservaService;
    @Autowired
    ListarReservaService listarReservaService;
    @Autowired
    PagarReservaService pagarReservaService;
    @Autowired
    CancelarReservaService cancelarReservaService;
    @Autowired
    EstornarReservaService estornarReservaService;

    public ReservaController(RealizarReservaService realizarReservaService,
                             ListarReservaService listarReservaService,
                             PagarReservaService pagarReservaService,
                             CancelarReservaService cancelarReservaService,
                             EstornarReservaService estornarReservaService) {

        this.realizarReservaService = realizarReservaService;
        this.listarReservaService = listarReservaService;
        this.pagarReservaService = pagarReservaService;
        this.cancelarReservaService = cancelarReservaService;
        this.estornarReservaService = estornarReservaService;
    }

    @PostMapping
    public ResponseEntity<InformacaoReservaResponse> realizarReserva(@RequestBody @Valid CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        return this.realizarReservaService.realizarReserva(cadastrarReservaRequest);
    }


    @GetMapping(path = "/solicitantes/{idSolicitante}")
    public Page<Reserva> listarTodasReservasPorSolicitantePaginadas(
            @PathVariable Long idSolicitante,
            @RequestParam(value = "dataHoraInicial", required = false) String dataHoraInicial,
            @RequestParam(value = "dataHoraFinal", required = false) String dataHoraFinal,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort) {

        Periodo periodo = new Periodo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.parse(dataHoraInicial, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataHoraFinal, formatter);
        periodo.setDataHoraInicial(inicio);
        periodo.setDataHoraFinal(fim);
        return this.listarReservaService.listarTodasReservasPorSolicitantePaginadas(idSolicitante, periodo, page, size, sort);
    }

    @GetMapping(path = "/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> listarTodasReservasPorAnunciantePaginadas(
            @PathVariable Long idAnunciante,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort) {
        return this.listarReservaService.listarTodasReservasPorAnunciantePaginadas(
                idAnunciante, page, size, sort);
    }

    @Transient
    @PutMapping(path = "/{idReserva}/pagamentos")
    public void pagarReserva(@PathVariable Long idReserva, @RequestBody @Valid FormaPagamento formaPagamento)
            throws FormadePagamentoNaoAceitaNoAnuncioException, IdReservaNaoExisteException,
            NaoPodeRealizarPagamentoException {
        this.pagarReservaService.pagarReserva(idReserva, formaPagamento);
    }

    @Transient
    @PutMapping(path = "/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) throws Exception {
        this.cancelarReservaService.cancelarReserva(idReserva);
    }

    @Transient
    @PutMapping(path = "/{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) throws Exception {
        this.estornarReservaService.estornarReserva(idReserva);
    }

}
