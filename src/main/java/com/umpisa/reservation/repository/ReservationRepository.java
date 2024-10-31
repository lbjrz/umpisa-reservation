package com.umpisa.reservation.repository;

import com.umpisa.reservation.model.ReservationData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationData, Long> {
    List<ReservationData> findByReservationDateTimeAfter(LocalDateTime dateTime);

}