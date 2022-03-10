package io.github.cwireset.tcc.Service;

import io.github.cwireset.tcc.Exception.IdReservaNaoExisteException;
import io.github.cwireset.tcc.Exception.NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException;
import io.github.cwireset.tcc.Repository.ReservaRepository;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstornarReservaService {
    @Autowired
    private final ReservaRepository reservaRepository;

    public EstornarReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public void estornarReserva(Long idReserva) throws IdReservaNaoExisteException,
            NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException {
        verificarIdReservaExiste(idReserva);
        verificarSeAReservaFoiPaga(idReserva);
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            reservaEncontrada.getPagamento().setStatus(StatusPagamento.ESTORNADO);
            reservaEncontrada.getPagamento().setFormaEscolhida(null);
            reservaRepository.save(reservaEncontrada);
        }
    }

    private void verificarSeAReservaFoiPaga(Long idReserva) throws
            NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isPresent()) {
            Reserva reservaEncontrada = reserva.get();
            if (reservaEncontrada.getPagamento().getStatus() != StatusPagamento.PAGO) {
                throw new NaoPodeRealizarEstornoSeaReservaNaoEstiverPagaException();
            }
        }
    }

    private void verificarIdReservaExiste(Long idReserva) throws IdReservaNaoExisteException {
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (reserva.isEmpty()) {
            throw new IdReservaNaoExisteException(idReserva);
        }
    }

}
