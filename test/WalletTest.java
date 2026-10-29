import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {


    @Test
    public void testAddFunds() {
        Wallet wallet = new Wallet(100.0);

        wallet.addFunds(50.0);

        assertEquals(150.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testDeductFunds() {
        Wallet wallet = new Wallet(100.0);

        wallet.deductFunds(40.0);

        assertEquals(60.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testTransferFunds() {
        Wallet from = new Wallet(100.0);
        Wallet to = new Wallet(50.0);

        from.transferFunds(to, 40.0);

        assertEquals(60.0, from.getBalance(), 0.001);
        assertEquals(90.0, to.getBalance(), 0.001);
    }



    @Test
    public void testDeductFundsExactlyRemainingBalance() {
        Wallet wallet = new Wallet(50.0);

        wallet.deductFunds(50.0);

        assertEquals(0.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testTransferExactlyAvailableFunds() {
        Wallet from = new Wallet(75.0);
        Wallet to = new Wallet(25.0);

        from.transferFunds(to, 75.0);

        assertEquals(0.0, from.getBalance(), 0.001);
        assertEquals(100.0, to.getBalance(), 0.001);
    }



    @Test
    public void testAddFundsWithNegativeValue() {

        Wallet wallet = new Wallet(50.0);

        assertThrows(InvalidAmountException.class,() -> wallet.addFunds(-10.0)
        );
    }

    @Test
    public void testAddFundsWithZero() {

        Wallet wallet = new Wallet(50.0);

        assertThrows(InvalidAmountException.class,() -> wallet.addFunds(0.0)
        );
    }

    @Test
    public void testDeductFundsWithNegativeValue() {

        Wallet wallet = new Wallet(50.0);

        assertThrows(
                InvalidAmountException.class,
                () -> wallet.deductFunds(-10.0)
        );
    }

    @Test
    public void testDeductFundsWithZero() {

        Wallet wallet = new Wallet(50.0);

        assertThrows(
                InvalidAmountException.class,
                () -> wallet.deductFunds(0.0)
        );
    }

    @Test
    public void testDeductFundsJustBeyondAvailable() {

        Wallet wallet = new Wallet(50.0);

        assertThrows(
                InsufficientFundsException.class,
                () -> wallet.deductFunds(50.01)
        );
    }

    @Test
    public void testTransferFundsWithNegativeAmount() {

        Wallet from = new Wallet(50.0);
        Wallet to = new Wallet(50.0);

        assertThrows(
                InvalidAmountException.class,
                () -> from.transferFunds(to, -25.0)
        );
    }

    @Test
    public void testTransferFundsWithZero() {

        Wallet from = new Wallet(50.0);
        Wallet to = new Wallet(50.0);

        assertThrows(
                InvalidAmountException.class,
                () -> from.transferFunds(to, 0.0)
        );
    }

    @Test
    public void testTransferMoreThanAvailable() {

        Wallet from = new Wallet(30.0);
        Wallet to = new Wallet(20.0);

        assertThrows(
                InsufficientFundsException.class,
                () -> from.transferFunds(to, 40.0)
        );

        assertEquals(30.0, from.getBalance(), 0.001);
        assertEquals(20.0, to.getBalance(), 0.001);
    }



    @Test
    public void testWalletConstructorWithNegativeBalance() {

        Wallet wallet = new Wallet(-100.0);

        assertEquals(-100.0, wallet.getBalance(), 0.001);


    }

    @Test
    public void testTransferFundsToNullWallet() {

        Wallet from = new Wallet(50.0);

        assertThrows(
                NullPointerException.class,
                () -> from.transferFunds(null, 10.0)
        );


    }

    @Test
    public void testFloatingPointPrecisionIssue() {

        Wallet wallet = new Wallet(0.1);

        wallet.addFunds(0.2);

        assertNotEquals(0.3, wallet.getBalance());


    }

    @Test
    public void testWalletWithInfinityValue() {

        Wallet wallet =
                new Wallet(Double.POSITIVE_INFINITY);

        assertTrue(
                Double.isInfinite(wallet.getBalance())
        );


    }

    @Test
    public void testWalletWithNaNValue() {

        Wallet wallet =
                new Wallet(Double.NaN);

        assertTrue(
                Double.isNaN(wallet.getBalance())
        );


    }


    @Test
    public void testWalletStateAfterFailedAddFunds() {

        Wallet wallet = new Wallet(50.0);

        try {
            wallet.addFunds(-10.0);
        } catch (InvalidAmountException e) {

        }

        assertEquals(50.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testWalletStateAfterFailedDeductFunds() {

        Wallet wallet = new Wallet(30.0);

        try {
            wallet.deductFunds(50.0);
        } catch (InsufficientFundsException e) {

        }

        assertEquals(30.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testWalletStateAfterFailedTransferFunds() {

        Wallet from = new Wallet(20.0);
        Wallet to = new Wallet(30.0);

        try {
            from.transferFunds(to, 50.0);
        } catch (InsufficientFundsException e) {

        }

        assertEquals(20.0, from.getBalance(), 0.001);
        assertEquals(30.0, to.getBalance(), 0.001);
    }
}