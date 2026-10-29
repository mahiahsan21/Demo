import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BookingTest {

    Vehicle vehicle;
    ParkingSlot slot;
    Booking booking;
    LocalDateTime start;
    LocalDateTime end;


    @BeforeEach
           public void testBooking(){
         vehicle = new Vehicle(1, VehicleType.MOTORCYCLE, 500.0);
         slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
         start = LocalDateTime.of(2026, 6, 1, 10, 0);
         end = LocalDateTime.of(2026, 6, 1, 12, 0);
         booking = new Booking(1, vehicle, slot, start, end, 20.0);

    }

    @Test
    public void testStatus() {

        assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    }

    @Test
    public void testBookingId() {

        assertEquals(1,booking.getBookingId());
    }

    @Test
    public void testVehicleNotNull() {
        assertNotNull(booking.getVehicle());
    }

    @Test
    public void testVehicle(){

        assertEquals(vehicle,booking.getVehicle());
    }

    @Test
    public void testSlotNotNull() {
        assertNotNull(booking.getParkingSlot());
    }


    @Test
    public void testSlot(){

        assertEquals(slot,booking.getParkingSlot());
    }

    @Test
    public void testStartTime() {
        assertEquals(start,booking.getStartTime());
    }
    @Test
    public void testEndTime() {
        assertEquals(end,booking.getEndTime());
    }

    @Test
    public void testAmount() {
        assertEquals(20.0, booking.getAmount());
    }

   @Test
    public void testCompleteBooking() {
        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED,  booking.getBookingStatus());
   }


    @Test
    public void testCancelBooking() {
        booking.cancelBooking();
        assertEquals(BookingStatus.CANCELLED,  booking.getBookingStatus());
    }


    @Test
    public void testCompleteAfterCancel() {
        booking.cancelBooking();
        booking.completeBooking();
        assertEquals(BookingStatus.CANCELLED,  booking.getBookingStatus());
    }


   @Test
    public void testBookingStatus() {

        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
   }

    @Test
    public void testCompleteTwiceStaysCompleted() {
        booking.completeBooking();
        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
    }


}

