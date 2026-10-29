import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


public class ParkingSystemTest {
    ParkingSystem system;
    Vehicle vehicle;
    ParkingSlot slot;
    LocalDateTime start;
    LocalDateTime end;

    @BeforeEach
    public void testParkingSystem() {
        system = ParkingSystem.getInstance();
        system.setVehicles(new java.util.ArrayList<>());
        system.setParkingSlots(new java.util.ArrayList<>());
        system.setBookings(new java.util.ArrayList<>());
        system.setSYSTEM_WALLET(new Wallet());

        vehicle = new Vehicle(1, VehicleType.CAR, 500.0);
        slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        start = LocalDateTime.of(2026, 6, 1, 10, 0);
        end = LocalDateTime.of(2026, 6, 1, 12, 0);

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);
    }

    @Test
    public void testGetAvailableSlots_returnsCompatibleSlot() {

        java.util.List<ParkingSlot> slots = system.getAvailableParkingSlots(vehicle, start, end);
        assertFalse(slots.isEmpty());
    }

    @Test
    public void testGetAvailableSlots_returnsNoCompatibleSlot() {

        Vehicle truck = new Vehicle(2, VehicleType.TRUCK, 500.0);
        java.util.List<ParkingSlot> slots = system.getAvailableParkingSlots(truck, start, end);
        assertTrue(slots.isEmpty());
    }


    @Test
    public void testGetAvailableSlots_deactivatedSlotNotReturned() {
        slot.deactivate();
        java.util.List<ParkingSlot> slots = system.getAvailableParkingSlots(vehicle, start, end);
        assertTrue(slots.isEmpty());
    }


    @Test
    public void testBook_bookingNotNull() {
        Booking booking = system.book(vehicle, slot, start, end);
        assertNotNull(booking);
    }

    @Test
    public void testBook_createsActiveBooking() {
        Booking booking = system.book(vehicle, slot, start, end);
        assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    }

    @Test
    public void testBook_deductsFromVehicleWallet() {
        double balanceBefore = vehicle.getBalance();
        system.book(vehicle, slot, start, end);
        assertTrue(vehicle.getBalance() < balanceBefore);
    }

    @Test
    public void testBook_addsToSystemWallet() {
        system.book(vehicle, slot, start, end);
        assertTrue(system.getBalance() > 0);
    }

    @Test
    public void testBook_endTimeBeforeStartTime() {
        assertThrows(IllegalBookingTimeException.class, () -> {
            system.book(vehicle, slot, end, start);
        });
    }

    @Test
    public void testBook_endTimeEqualsStartTime() {
        assertThrows(IllegalBookingTimeException.class, () -> {
            system.book(vehicle, slot, start, start);
        });
    }

    @Test
    public void testBook_correctAmountCharged() {

        Booking booking = system.book(vehicle, slot, start, end);
        assertEquals(20.0, booking.getAmount());
    }

    @Test
    public void testBook_vehicleWalletReducedByCorrectAmount() {

        double balanceBefore = vehicle.getBalance();
        system.book(vehicle, slot, start, end);
        assertEquals(balanceBefore - 20.0, vehicle.getBalance());
    }

    @Test
    public void testCompleteBooking_statusChangesToCompleted() {
        Booking booking = system.book(vehicle, slot, start, end);
        system.completeBooking(booking);
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
    }

    @Test
    public void testCompleteBooking_80PercentGoesToSlot() {
        Booking booking = system.book(vehicle, slot, start, end);
        double amount = booking.getAmount();
        system.completeBooking(booking);
        assertEquals(amount * 0.8, slot.getBalance(), 0.001);
    }

    @Test
    public void testCompleteBooking_systemRetains20Percent() {
        Booking booking = system.book(vehicle, slot, start, end);
        double amount = booking.getAmount();
        system.completeBooking(booking);
        assertEquals(amount * 0.2, system.getBalance(), 0.001);
    }

    @Test
    public void testCancelBooking_statusChangesToCancelled() {
        Booking booking = system.book(vehicle, slot, start, end);
        system.cancelBooking(booking);
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }

    @Test
    public void testCancelBooking_90PercentRefundedToVehicle() {
        Booking booking = system.book(vehicle, slot, start, end);
        double amount = booking.getAmount();
        double balanceAfterBooking = vehicle.getBalance();
        system.cancelBooking(booking);
        assertEquals(balanceAfterBooking + amount * 0.9, vehicle.getBalance(), 0.001);
    }


    @Test
    public void testCancelBooking_systemRetains10Percent() {
        Booking booking = system.book(vehicle, slot, start, end);
        double amount = booking.getAmount();
        system.cancelBooking(booking);
        assertEquals(amount * 0.1, system.getBalance(), 0.001);
    }


    @Test
    public void testBook_zeroHourDuration() {

        LocalDateTime sameStart = LocalDateTime.of(2026, 6, 1, 10, 0);
        LocalDateTime thirtyMinEnd = LocalDateTime.of(2026, 6, 1, 10, 30);
        assertThrows(IllegalBookingTimeException.class, () -> {
            system.book(vehicle, slot, sameStart, thirtyMinEnd);
        });
    }


    @Test
    public void testCancelBooking_afterComplete() {

        Booking booking = system.book(vehicle, slot, start, end);
        system.completeBooking(booking);
        system.cancelBooking(booking);

        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
    }


    @Test
    public void testBook_incompatibleSlot_throwsException() {
        ParkingSlot compactSlot = new ParkingSlot("S2", ParkingSlotType.COMPACT);
        system.addParkingSlot(compactSlot);
        assertThrows(IllegalArgumentException.class, () -> {
            system.book(vehicle, compactSlot, start, end);
        });
    }


    @Test
    public void testBook_insufficientFunds_bookingNotAdded() {
        Vehicle poorVehicle = new Vehicle(2, VehicleType.CAR, 10.0);
        system.addVehicle(poorVehicle);
        try {
            system.book(poorVehicle, slot, start, end);
        } catch (InsufficientFundsException e) {

        }

        assertEquals(1, system.getBookings().size());
    }




    }
