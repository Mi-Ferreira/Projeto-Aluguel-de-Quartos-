package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.IdReservaNaoExisteException;
import io.github.cwireset.tcc.Exception.NaoPodeRealizarCancelamentoException;
import io.github.cwireset.tcc.Repository.ReservaRepository;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CancelarReservaService {

    @Autowired
    private final ReservaRepository reservaRepository;

    public CancelarReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public void cancelarReserva(Long idReserva) throws IdReservaNaoExisteException,
            NaoPodeRealizarCancelamentoException {
        verificarIdReservaExiste(idReserva);
        verificarSeAReservaAindaEstaComPagamanetoPendente(idReserva);
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            reservaEncontrada.getPagamento().setStatus(StatusPagamento.CANCELADO);
            reservaRepository.save(reservaEncontrada);
        }
    }

    private void verificarIdReservaExiste(Long idReserva1) throws IdReservaNaoExisteException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva1);
        if (reserva.isEmpty()) {
            throw new IdReservaNaoExisteException(idReserva1);
        }
    }

    private void verificarSeAReservaAindaEstaComPagamanetoPendente(Long idReserva) throws
            NaoPodeRealizarCancelamentoException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            if (reservaEncontrada.getPagamento().getStatus() != StatusPagamento.PENDENTE) {
                throw new NaoPodeRealizarCancelamentoException();
            }
        }
    }

}
