import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSlotTest {

    ParkingSlot compactSlot;
    ParkingSlot regularSlot;
    ParkingSlot largeSlot;
    ParkingSlot handicappedSlot;
    LocalDateTime start;
    LocalDateTime end;


    @BeforeEach
    public void testParkingSlot() {
        compactSlot = new ParkingSlot("S1", ParkingSlotType.COMPACT);
        regularSlot = new ParkingSlot("S2", ParkingSlotType.REGULAR);
        largeSlot = new ParkingSlot("S3", ParkingSlotType.LARGE);
        handicappedSlot = new ParkingSlot("S4", ParkingSlotType.HANDICAPPED);
        start = LocalDateTime.of(2026, 6, 1, 10, 0);
        end = LocalDateTime.of(2026, 6, 1, 12, 0);

    }

    @Test
    public void testParkingSlotId(){
        assertEquals ("S1", compactSlot.getSlotId());
    }
    @Test
    public void testParkingSlotType(){
        assertEquals(ParkingSlotType.REGULAR, regularSlot.getSlotType());
    }
    @Test
    public void testIsActive(){
        assertTrue(largeSlot.isActive());
    }
    @Test
    public void testWallet(){
        assertEquals(0.0,handicappedSlot.getBalance() );
    }
    @Test
    public void testBookinglist(){
        assertTrue(regularSlot.getBookings().isEmpty() );
    }

    @Test
    public void testGetWalletNotNull(){
        assertNotNull(compactSlot.getWallet());
    }

    @Test
    public void testActivate(){

        assertTrue(compactSlot.isActive());
    }
    @Test
    public void testDeactivate(){

        largeSlot.deactivate();
        assertFalse(largeSlot.isActive());
    }

    @Test
    public void testDeactivateThenActivate(){
        handicappedSlot.deactivate();
        assertFalse(handicappedSlot.isActive());
        handicappedSlot.activate();
        assertTrue(handicappedSlot.isActive());
    }


    @Test
    public void testSlotIsAvailableWhenNoBooking(){
        assertTrue(compactSlot.isAvailable(start,end));
    }

    @Test
    void testSlotIsUnavailableWhenBookingOverlaps() {

        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 500.0);
        Booking booking = new Booking(1, vehicle, regularSlot, start, end, 20.0);
        regularSlot.getBookings().add(booking);

        assertFalse(regularSlot.isAvailable(start, end));
    }


    @Test
    void testSlotIsUnavailableWhenNewBookingStartsDuringExisting() {

        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 500.0);
        Booking booking = new Booking(1, vehicle, compactSlot, start, end, 20.0);
        compactSlot.getBookings().add(booking);


        LocalDateTime newStart = LocalDateTime.of(2026, 6, 4, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2026, 6, 4, 12, 0);

        assertFalse(compactSlot.isAvailable(newStart, newEnd));
    }


    @Test
    void testSlotIsAvailableWhenNewBookingStartsExactlyWhenOldEnds() {

        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 500.0);
        Booking booking = new Booking(1, vehicle, compactSlot, start, end, 20.0);
        compactSlot.getBookings().add(booking);


        LocalDateTime newStart = LocalDateTime.of(2026, 6, 1, 12, 0);
        LocalDateTime newEnd = LocalDateTime.of(2026, 6, 1, 14, 0);

        assertTrue(compactSlot.isAvailable(newStart, newEnd));
    }


    @Test
    void testMotorcycleCompatibleWithCompact() {

        assertTrue(compactSlot.isCompatible(VehicleType.MOTORCYCLE, start, end));

    }
    @Test
    void testMotorcycleCompatibleWithRegular() {

        assertTrue(regularSlot.isCompatible(VehicleType.MOTORCYCLE, start, end));
    }
    @Test
    void testMotorcycleCompatibleWithLarge() {
        assertTrue(largeSlot.isCompatible(VehicleType.MOTORCYCLE, start, end));
    }

    @Test
    void testMotorcycleCompatibleWithHandicapped() {
        assertTrue(handicappedSlot.isCompatible(VehicleType.MOTORCYCLE, start, end));
    }

    @Test
    void testCarCompatibleWithRegular() {

        assertTrue(regularSlot.isCompatible(VehicleType.CAR, start, end));
    }
    @Test
    void testCarCompatibleWithLarge() {
        assertTrue(largeSlot.isCompatible(VehicleType.CAR, start, end));
    }

    @Test
    void testCarCompatibleWithHandicapped() {
        assertTrue(handicappedSlot.isCompatible(VehicleType.CAR, start, end));
    }

    @Test
    void testLargeBusCompatibleWithLarge() {
        assertTrue(largeSlot.isCompatible(VehicleType.BUS, start, end));
    }

    @Test
    void testLargeBusCompatibleWithRegular() {
        assertTrue(regularSlot.isCompatible(VehicleType.BUS, start, end));
    }
    @Test
    void testLargeBicycleCompatibleWithCompact() {
        assertTrue(compactSlot.isCompatible(VehicleType.BICYCLE, start, end));
    }
    @Test
    void testLargeBicycleCompatibleWithRegular() {
        assertTrue(regularSlot.isCompatible(VehicleType.BICYCLE, start, end));
    }
    @Test
    void testLargeBicycleCompatibleWithHandicapped() {
        assertTrue(compactSlot.isCompatible(VehicleType.BICYCLE, start, end));
    }
    @Test
    void testLargeBicycleCompatibleWithLarge() {
        assertTrue(largeSlot.isCompatible(VehicleType.BICYCLE, start, end));
    }


    @Test
    void testMicrocarCompatibleWithCompact() {

        assertFalse(largeSlot.isCompatible(VehicleType.MICROCAR, start, end));
    }


    @Test
    void testTruckNotCompatibleWithAnySlot() {

        assertTrue(compactSlot.isCompatible(VehicleType.TRUCK, start, end));
        assertTrue(regularSlot.isCompatible(VehicleType.TRUCK, start, end));
        assertTrue(largeSlot.isCompatible(VehicleType.TRUCK, start, end));
        assertTrue(handicappedSlot.isCompatible(VehicleType.TRUCK, start, end));
    }

    @Test
    void testDeactivatedSlotNotCompatibleWithAnything() {

        compactSlot.deactivate();
        assertFalse(compactSlot.isCompatible(VehicleType.CAR, start, end));
        assertFalse(compactSlot.isCompatible(VehicleType.MOTORCYCLE, start, end));
        assertFalse(compactSlot.isCompatible(VehicleType.BICYCLE, start, end));
    }













}
