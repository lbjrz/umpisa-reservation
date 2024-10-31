package com.umpisa.reservation.controller;

import com.umpisa.reservation.model.CommunicationMethod;
import com.umpisa.reservation.model.ReservationData;
import com.umpisa.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationData>> getAllReservations() {
        List<ReservationData> allReservations = reservationService.getAllReservations();
        return ResponseEntity.ok(allReservations);
    }

    @PostMapping
    public ResponseEntity<ReservationData> createReservation(@RequestBody ReservationData reservation) {
        ReservationData createdReservation = reservationService.createReservation(reservation);
        notifyUser(createdReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        boolean isCancelled = reservationService.cancelReservation(reservationId);
        if (isCancelled) {
            return ResponseEntity.ok("Reservation cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReservationData>> getUpcomingReservations() {
        List<ReservationData> upcomingReservations = reservationService.getUpcomingReservations();
        return ResponseEntity.ok(upcomingReservations);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationData> updateReservation(
            @PathVariable Long reservationId,
            @RequestParam LocalDateTime newDateTime,
            @RequestParam int newGuestCount) {

        Optional<ReservationData> updatedReservation = reservationService.updateReservation(reservationId, newDateTime, newGuestCount);

        if (updatedReservation.isPresent()) {
            return ResponseEntity.ok(updatedReservation.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    private void notifyUser(ReservationData reservation) {
        if (reservation.getCommunicationMethod() == CommunicationMethod.SMS) {
            System.out.println("Sent SMS to " + reservation.getPhoneNumber());
        } else {
            System.out.println("Sent Email to " + reservation.getEmail());
        }
    }
}