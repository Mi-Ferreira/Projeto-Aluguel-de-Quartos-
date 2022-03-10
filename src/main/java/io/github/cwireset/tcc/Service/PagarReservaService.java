package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.FormadePagamentoNaoAceitaNoAnuncioException;
import io.github.cwireset.tcc.Exception.IdReservaNaoExisteException;
import io.github.cwireset.tcc.Exception.NaoPodeRealizarPagamentoException;
import io.github.cwireset.tcc.Repository.ReservaRepository;
import io.github.cwireset.tcc.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagarReservaService {
    @Autowired
    private final ReservaRepository reservaRepository;

    public PagarReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws
            FormadePagamentoNaoAceitaNoAnuncioException, IdReservaNaoExisteException, NaoPodeRealizarPagamentoException{
        verificarIdReservaExiste(idReserva);
        verificarSeAFormaDePagamentoInformadoTemNoAnuncio(idReserva, formaPagamento);
        verificarSeAReservaAindaEstaComPagamanetoPendente(idReserva);
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            reservaEncontrada.getPagamento().setStatus(StatusPagamento.PAGO);
            reservaRepository.save(reservaEncontrada);
        }
    }

    private void verificarSeAReservaAindaEstaComPagamanetoPendente(Long idReserva)
            throws NaoPodeRealizarPagamentoException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            if (reservaEncontrada.getPagamento().getStatus() != StatusPagamento.PENDENTE) {
                throw new NaoPodeRealizarPagamentoException();
            }
        }
    }

    private void verificarSeAFormaDePagamentoInformadoTemNoAnuncio(Long idReserva, FormaPagamento formaPagamento)
            throws FormadePagamentoNaoAceitaNoAnuncioException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            if (!(reservaEncontrada.getAnuncio().getFormasAceitas().contains(formaPagamento))) {
                throw new FormadePagamentoNaoAceitaNoAnuncioException(formaPagamento,
                        formatarFormasDePagamentoAceitas(reservaEncontrada.getAnuncio().getFormasAceitas()));
            }
        }

    }

    private String formatarFormasDePagamentoAceitas(List<FormaPagamento> pagamentosAceitos) {
        StringBuilder aceitos = new StringBuilder();
        for (int i = 0; i < pagamentosAceitos.size(); i++) {
            if (i == pagamentosAceitos.size() - 1) {
                aceitos.append(pagamentosAceitos.get(i).toString()).append(".");
            } else {
                aceitos.append(pagamentosAceitos.get(i).toString()).append(", ");
            }
        }
        return aceitos.toString();
    }

    private void verificarIdReservaExiste(Long idReserva1) throws IdReservaNaoExisteException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva1);
        if (reserva.isEmpty()) {
            throw new IdReservaNaoExisteException(idReserva1);
        }
    }

}
