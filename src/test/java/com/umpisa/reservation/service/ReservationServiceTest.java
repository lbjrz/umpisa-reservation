package com.umpisa.reservation.service;

import com.umpisa.reservation.model.CommunicationMethod;
import com.umpisa.reservation.model.ReservationData;
import com.umpisa.reservation.repository.ReservationRepository;
import com.umpisa.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation() {
        ReservationData reservation = new ReservationData();
        reservation.setName("John Wick");
        reservation.setPhoneNumber("123456789");
        reservation.setEmail("johnwick@gmail.com");
        reservation.setReservationDateTime(LocalDateTime.now().plusDays(1));
        reservation.setNumberOfGuests(4);
        reservation.setCommunicationMethod(CommunicationMethod.EMAIL);

        when(reservationRepository.save(any(ReservationData.class))).thenReturn(reservation);
        ReservationData createdReservation = reservationService.createReservation(reservation);

        assertEquals("John Wick", createdReservation.getName());
        assertEquals("123456789", createdReservation.getPhoneNumber());
        assertEquals("johnwick@gmail.com", createdReservation.getEmail());
        assertEquals(4, createdReservation.getNumberOfGuests());
        assertEquals(CommunicationMethod.EMAIL, createdReservation.getCommunicationMethod());

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservation);
    }

    @Test
    public void testCancelReservation_Success() {
        Long reservationId = 1L;
        when(reservationRepository.existsById(reservationId)).thenReturn(true);
        boolean result = reservationService.cancelReservation(reservationId);
        assertTrue(result);
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    public void testCancelReservation_NotFound() {
        Long reservationId = 2L;
        when(reservationRepository.existsById(reservationId)).thenReturn(false);
        boolean result = reservationService.cancelReservation(reservationId);
        assertFalse(result);
        verify(reservationRepository, never()).deleteById(reservationId);
    }

    @Test
    public void testGetUpcomingReservations() {
        ReservationData reservation1 = new ReservationData();
        reservation1.setReservationDateTime(LocalDateTime.now().plusDays(2));
        ReservationData reservation2 = new ReservationData();
        reservation2.setReservationDateTime(LocalDateTime.now().plusDays(5));

        List<ReservationData> expectedReservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findByReservationDateTimeAfter(any(LocalDateTime.class)))
                .thenReturn(expectedReservations);

        List<ReservationData> actualReservations = reservationService.getUpcomingReservations();

        assertEquals(2, actualReservations.size());
        assertEquals(expectedReservations, actualReservations);
        verify(reservationRepository, times(1)).findByReservationDateTimeAfter(any(LocalDateTime.class));
    }

    @Test
    public void testUpdateReservation_Success() {
        Long reservationId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(3);
        int newGuestCount = 5;

        ReservationData reservation = new ReservationData();
        reservation.setReservationDateTime(LocalDateTime.now().plusDays(1));
        reservation.setNumberOfGuests(3);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(ReservationData.class))).thenReturn(reservation);

        Optional<ReservationData> updatedReservation = reservationService.updateReservation(reservationId, newDateTime, newGuestCount);

        assertEquals(newDateTime, updatedReservation.get().getReservationDateTime());
        assertEquals(newGuestCount, updatedReservation.get().getNumberOfGuests());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testUpdateReservation_NotFound() {
        Long reservationId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(3);
        int newGuestCount = 5;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        Optional<ReservationData> updatedReservation = reservationService.updateReservation(reservationId, newDateTime, newGuestCount);

        assertEquals(Optional.empty(), updatedReservation);
        verify(reservationRepository, never()).save(any(ReservationData.class));
    }
}
