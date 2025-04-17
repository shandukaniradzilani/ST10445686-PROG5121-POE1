/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package poe1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class POE1Test {
    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    @Test
    void testValidateUsername_Valid() {
        assertTrue(login.validateUsername("abc_1"));
    }

    @Test
    void testValidateUsername_InvalidNoUnderscore() {
        assertFalse(login.validateUsername("abcde"));
    }

    @Test
    void testValidateUsername_InvalidTooLong() {
        assertFalse(login.validateUsername("abcde_1"));
    }

    @Test
    void testValidatePassword_Valid() {
        assertTrue(login.validatePassword("Passw0rd!"));
    }

    @Test
    void testValidatePassword_TooShort() {
        assertFalse(login.validatePassword("Pa0!"));
    }

    @Test
    void testValidatePassword_NoSpecialChar() {
        assertFalse(login.validatePassword("Password1"));
    }

    @Test
    void testValidatePassword_NoNumber() {
        assertFalse(login.validatePassword("Password!"));
    }

    @Test
    void testValidatePassword_NoUppercase() {
        assertFalse(login.validatePassword("password1!"));
    }

    @Test
    void testValidatePhoneNumber_Valid() {
        assertTrue(login.validatePhoneNumber("+27821234567"));
    }

    @Test
    void testValidatePhoneNumber_InvalidPrefix() {
        assertFalse(login.validatePhoneNumber("0821234567"));
    }

    @Test
    void testRegisterAndLogin_Success() {
        login.register("abc_1", "Passw0rd!", "+27821234567");
        assertTrue(login.login("abc_1", "Passw0rd!"));
    }

    @Test
    void testLogin_InvalidCredentials() {
        login.register("abc_1", "Passw0rd!", "+27821234567");
        assertFalse(login.login("abc_1", "WrongPass1!"));
    }
}
