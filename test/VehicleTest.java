import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {
    @Test
    public void testVehicleCreationWithWallet() {
        Wallet wallet = new Wallet(500);

        Vehicle vehicle = new Vehicle(1,VehicleType.CAR,wallet);

        assertEquals(1, vehicle.getVehicleId());
        assertEquals(VehicleType.CAR, vehicle.getVehicleType());
        assertEquals(wallet, vehicle.getWallet());
        assertEquals(500, vehicle.getBalance(), 0.001);
    }

     @Test
    public void testVehicleCreationWithInitialBalance() {

        Vehicle vehicle =  new Vehicle(2,VehicleType.CAR, 1000);

        assertEquals(2, vehicle.getVehicleId());
        assertEquals(VehicleType.CAR, vehicle.getVehicleType());
        assertEquals(1000, vehicle.getBalance(), 0.001);
    }

     @Test
    public void testVehicleToString() {

        Vehicle vehicle = new Vehicle(1,VehicleType.CAR,1000);

        String result = vehicle.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
    }


    @Test
    public void testAllVehicleTypes() {
        assertEquals(VehicleType.MOTORCYCLE, new Vehicle(1, VehicleType.MOTORCYCLE, 100).getVehicleType());
        assertEquals(VehicleType.TRUCK,      new Vehicle(2, VehicleType.TRUCK,      100).getVehicleType());
        assertEquals(VehicleType.BICYCLE,    new Vehicle(3, VehicleType.BICYCLE,    100).getVehicleType());
        assertEquals(VehicleType.MICROCAR,   new Vehicle(4, VehicleType.MICROCAR,   100).getVehicleType());
        assertEquals(VehicleType.BUS,        new Vehicle(5, VehicleType.BUS,        100).getVehicleType());
    }



     @Test
    public void defect_NullWalletAccepted() {


        Vehicle vehicle =
                new Vehicle(1,VehicleType.CAR,(Wallet) null);

        assertThrows(NullPointerException.class,vehicle::getBalance);
    }
     @Test
    public void defect_NullVehicleTypeAccepted() {

        Vehicle vehicle = new Vehicle(1,null,100);

        assertNull(vehicle.getVehicleType());
    }
  

    @Test
    public void testVehicleWithNegativeVehicleId() {
        Vehicle vehicle = new Vehicle(-1, VehicleType.CAR, 50.0);
        assertEquals(-1, vehicle.getVehicleId());

    }

    @Test
    public void testVehicleWithZeroVehicleId() {
        Vehicle vehicle = new Vehicle(0, VehicleType.CAR, 50.0);
        assertEquals(0, vehicle.getVehicleId());

    }


    @Test
    public void testVehicleConstructorWithNegativeBalance() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, -50.0);
        assertEquals(-50.0, vehicle.getBalance(), 0.001);

    }

    @Test
    public void testVehicleWithExtremelyLargeVehicleId() {
        Vehicle vehicle = new Vehicle(Integer.MAX_VALUE, VehicleType.CAR, 50.0);
        assertEquals(Integer.MAX_VALUE, vehicle.getVehicleId());
    }

    @Test
    public void testVehicleWithExtremelySmallVehicleId() {
        Vehicle vehicle = new Vehicle(Integer.MIN_VALUE, VehicleType.CAR, 50.0);
        assertEquals(Integer.MIN_VALUE, vehicle.getVehicleId());
    }


    @Test
    public void testMultipleVehiclesSharingWalletModification() {
        Wallet sharedWallet = new Wallet(100.0);
        Vehicle v1 = new Vehicle(1, VehicleType.CAR, sharedWallet);
        Vehicle v2 = new Vehicle(2, VehicleType.MOTORCYCLE, sharedWallet);
        
        v1.getWallet().deductFunds(20.0);
        

        assertEquals(80.0, v2.getBalance(), 0.001);
    }

    @Test
    public void testVehicleGetWalletReturnsActualReference() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 50.0);
        Wallet wallet1 = vehicle.getWallet();
        Wallet wallet2 = vehicle.getWallet();
        
        assertSame(wallet1, wallet2);
        wallet1.addFunds(10.0);
        assertEquals(60.0, vehicle.getBalance(), 0.001);
    }

}