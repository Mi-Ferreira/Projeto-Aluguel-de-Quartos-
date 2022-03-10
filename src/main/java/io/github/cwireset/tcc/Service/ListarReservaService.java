package io.github.cwireset.tcc.Service;


import io.github.cwireset.tcc.Repository.ReservaRepository;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListarReservaService {

    @Autowired
    private final ReservaRepository reservaRepository;

    public ListarReservaService(ReservaRepository reservaRepository) {

        this.reservaRepository = reservaRepository;
    }

    public Page<Reserva> listarTodasReservasPorAnunciantePaginadas(Long idAnunciante,
                                                                   Integer page,
                                                                   Integer pageCount,
                                                                   String direction) {
        List<Reserva> reservasDoAnunciante = new ArrayList<>();
        List<Reserva> reservas = (List<Reserva>) reservaRepository.findAll();

        for (Reserva reserva : reservas) {
            if (reserva.getAnuncio().getAnunciante().getId().equals(idAnunciante)) {
                reservasDoAnunciante.add(reserva);
            }
        }
        PageRequest pageRequest = PageRequest.of(page, pageCount,
                Sort.Direction.valueOf(direction), "dataHoraFinal");
        Page<Reserva> pageReserva;
        pageReserva = new PageImpl<>(reservasDoAnunciante, pageRequest, reservasDoAnunciante.size());
        return pageReserva;
    }

    public Page<Reserva> listarTodasReservasPorSolicitantePaginadas(Long idSolicitante,
                                                                    Periodo periodo,
                                                                    Integer page,
                                                                    Integer pageCount,
                                                                    String direction) {
        List<Reserva> reservasDoSolicitante = new ArrayList<>();
        List<Reserva> reservas = (List<Reserva>) reservaRepository.findAll();
        if (periodo == null) {
            for (Reserva reservaSemPeriodo : reservas) {
                if (reservaSemPeriodo.getSolicitante().getId().equals(idSolicitante)) {
                    reservasDoSolicitante.add(reservaSemPeriodo);
                }
            }
            PageRequest pageRequest = PageRequest.of(page, pageCount, Sort.Direction.valueOf(direction), "dataHoraFinal");
            Page<Reserva> pageReserva;
            pageReserva = new PageImpl<>(reservasDoSolicitante, pageRequest, reservasDoSolicitante.size());
            return pageReserva;
        } else {
            for (Reserva reservaSemPeriodo : reservas) {
                if (reservaSemPeriodo.getSolicitante().getId().equals(idSolicitante)) {
                    reservasDoSolicitante.add(reservaSemPeriodo);
                    List<Reserva> reservaFiltradaPorPeriodo = new ArrayList<>();
                    for (Reserva reservaComPeriodo : reservasDoSolicitante) {
                        if ((reservaComPeriodo.getPeriodo().getDataHoraInicial().isAfter(periodo.getDataHoraInicial())) && (periodo.getDataHoraFinal().isAfter(reservaComPeriodo.getPeriodo().getDataHoraFinal()))) {
                            reservaFiltradaPorPeriodo.add(reservaComPeriodo);
                        }
                    }
                    PageRequest pageRequest = PageRequest.of(page, pageCount, Sort.Direction.valueOf(direction), "dataHoraFinal");
                    Page<Reserva> pageReserva;
                    pageReserva = new PageImpl<>(reservaFiltradaPorPeriodo, pageRequest, reservaFiltradaPorPeriodo.size());
                    return pageReserva;
                }
            }
        }
        return null;
    }

}

