package com.umpisa.reservation.service;

import com.umpisa.reservation.model.ReservationData;
import com.umpisa.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationData createReservation(ReservationData reservation) {
        return reservationRepository.save(reservation);
    }

    public List<ReservationData> getUpcomingReservations() {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.findByReservationDateTimeAfter(now);
    }

    public Optional<ReservationData> updateReservation(Long reservationId, LocalDateTime newDateTime, int newGuestCount) {
        Optional<ReservationData> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            ReservationData reservation = reservationOpt.get();
            reservation.setReservationDateTime(newDateTime);
            reservation.setNumberOfGuests(newGuestCount);
            reservationRepository.save(reservation);
            return Optional.of(reservation);
        }
        return Optional.empty();
    }

    public boolean cancelReservation(Long reservationId) {
        if (reservationRepository.existsById(reservationId)) {
            reservationRepository.deleteById(reservationId);
            return true;
        }
        return false;
    }

    public List<ReservationData> getAllReservations() {
        return reservationRepository.findAll();
    }
}