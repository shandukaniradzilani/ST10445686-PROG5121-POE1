/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package poe1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class POE1Test {
    private Login login;
    private MessageSystem messageSystem;
    
    @BeforeEach
    void setUp() {
        login = new Login();
        messageSystem = new MessageSystem();
    }
    
    // ========== LOGIN CLASS TESTS ==========
    
    @Test
    void testValidateUsername_Valid() {
        assertTrue(login.validateUsername("abc_1"));
        assertTrue(login.validateUsername("jo_n"));
        assertTrue(login.validateUsername("a_b"));
    }
    
    @Test
    void testValidateUsername_InvalidNoUnderscore() {
        assertFalse(login.validateUsername("abcde"));
        assertFalse(login.validateUsername("john"));
    }
    
    @Test
    void testValidateUsername_InvalidTooLong() {
        assertFalse(login.validateUsername("abcde_1"));
        assertFalse(login.validateUsername("toolong_"));
    }
    
    @Test
    void testValidateUsername_EdgeCases() {
        assertTrue(login.validateUsername("a_bcd")); // exactly 5 characters
        assertFalse(login.validateUsername("a_bcde")); // 6 characters
        assertTrue(login.validateUsername("_")); // minimum valid
    }
    
    @Test
    void testValidatePassword_Valid() {
        assertTrue(login.validatePassword("Passw0rd!"));
        assertTrue(login.validatePassword("MyP@ssw0rd123"));
        assertTrue(login.validatePassword("Secure1#"));
    }
    
    @Test
    void testValidatePassword_TooShort() {
        assertFalse(login.validatePassword("Pa0!"));
        assertFalse(login.validatePassword("Abc1!"));
        assertFalse(login.validatePassword("1234567")); // 7 chars
    }
    
    @Test
    void testValidatePassword_NoSpecialChar() {
        assertFalse(login.validatePassword("Password1"));
        assertFalse(login.validatePassword("MyPassword123"));
    }
    
    @Test
    void testValidatePassword_NoNumber() {
        assertFalse(login.validatePassword("Password!"));
        assertFalse(login.validatePassword("MyPassword@"));
    }
    
    @Test
    void testValidatePassword_NoUppercase() {
        assertFalse(login.validatePassword("password1!"));
        assertFalse(login.validatePassword("mypassword1@"));
    }
    
    @Test
    void testValidatePassword_EdgeCases() {
        assertTrue(login.validatePassword("Password1!")); // exactly 8 chars
        assertFalse(login.validatePassword("passwor1!")); // no uppercase
        assertTrue(login.validatePassword("PASS1word@")); // uppercase at start
    }
    
    @Test
    void testValidatePhoneNumber_Valid() {
        assertTrue(login.validatePhoneNumber("+27821234567"));
        assertTrue(login.validatePhoneNumber("+27123456789012")); // longer valid number
    }
    
    @Test
    void testValidatePhoneNumber_InvalidPrefix() {
        assertFalse(login.validatePhoneNumber("0821234567"));
        assertFalse(login.validatePhoneNumber("+1234567890"));
        assertFalse(login.validatePhoneNumber("27821234567"));
    }
    
    @Test
    void testValidatePhoneNumber_TooShort() {
        assertFalse(login.validatePhoneNumber("+27123"));
        assertFalse(login.validatePhoneNumber("+2712345"));
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
        assertFalse(login.login("wrong_user", "Passw0rd!"));
    }
    
    @Test
    void testRegister_MultipleUsers() {
        login.register("abc_1", "Passw0rd!", "+27821234567");
        login.register("def_2", "MyPass1@", "+27987654321");
        
        assertTrue(login.login("abc_1", "Passw0rd!"));
        assertTrue(login.login("def_2", "MyPass1@"));
        assertFalse(login.login("abc_1", "MyPass1@")); // wrong password
    }
    
    // ========== MESSAGE SYSTEM TESTS ==========
    
    @Test
    void testCheckMessageID_Valid() {
        assertTrue(messageSystem.checkMessageID("Hello"));
        assertTrue(messageSystem.checkMessageID("This is a test message within limit"));
        // Test with exactly 250 characters
        String maxMessage = "a".repeat(250);
        assertTrue(messageSystem.checkMessageID(maxMessage));
    }
    
    @Test
    void testCheckMessageID_Invalid() {
        // Test with 251 characters (over limit)
        String overLimitMessage = "a".repeat(251);
        assertFalse(messageSystem.checkMessageID(overLimitMessage));
        
        // Test with much longer message
        String longMessage = "This message is way too long and exceeds the maximum character limit of 250 characters. " +
                           "It should definitely fail the validation test because it contains far more characters than allowed. " +
                           "The system should reject this message and display an appropriate error message to the user.";
        assertFalse(messageSystem.checkMessageID(longMessage));
    }
    
    @Test
    void testCheckRecipientCell_Valid() {
        assertTrue(messageSystem.checkRecipientCell("+27821234567"));
        assertTrue(messageSystem.checkRecipientCell("+27123456789"));
        assertTrue(messageSystem.checkRecipientCell("+271234567890")); // 12 chars
        assertTrue(messageSystem.checkRecipientCell("+2712345678901")); // 13 chars
    }
    
    @Test
    void testCheckRecipientCell_Invalid() {
        assertFalse(messageSystem.checkRecipientCell("0821234567")); // no +27
        assertFalse(messageSystem.checkRecipientCell("+1234567890")); // wrong country code
        assertFalse(messageSystem.checkRecipientCell("+27123")); // too short
        assertFalse(messageSystem.checkRecipientCell("+27123456789012345")); // too long (over 13)
        assertFalse(messageSystem.checkRecipientCell("27821234567")); // missing +
    }
    
    @Test
    void testCreateMessage() {
        Message message = messageSystem.createMessage("test_user", "+27821234567", "Hello World");
        
        assertNotNull(message);
        assertEquals("test_user", message.getSender());
        assertEquals("+27821234567", message.getRecipient());
        assertEquals("Hello World", message.getMessage());
        assertNotNull(message.getMessageID());
        assertNotNull(message.getMessageHash());
        assertEquals("Created", message.getStatus());
    }
    
    @Test
    void testSendMessage() {
        Message message = messageSystem.createMessage("test_user", "+27821234567", "Test message");
        
        assertEquals(0, messageSystem.returnTotalMessages());
        messageSystem.sendMessage(message);
        assertEquals(1, messageSystem.returnTotalMessages());
        assertEquals("Sent", message.getStatus());
    }
    
    @Test
    void testStoreMessage() {
        Message message = messageSystem.createMessage("test_user", "+27821234567", "Stored message");
        
        messageSystem.storeMessage(message);
        assertEquals(1, messageSystem.returnTotalMessages());
        assertEquals("Stored", message.getStatus());
    }
    
    @Test
    void testReturnTotalMessages() {
        assertEquals(0, messageSystem.returnTotalMessages());
        
        Message msg1 = messageSystem.createMessage("user1", "+27821234567", "Message 1");
        Message msg2 = messageSystem.createMessage("user2", "+27987654321", "Message 2");
        
        messageSystem.sendMessage(msg1);
        assertEquals(1, messageSystem.returnTotalMessages());
        
        messageSystem.storeMessage(msg2);
        assertEquals(2, messageSystem.returnTotalMessages());
    }
    
    @Test
    void testPrintMessages_Empty() {
        String result = messageSystem.printMessages();
        assertEquals("No messages to display.", result);
    }
    
    @Test
    void testPrintMessages_WithMessages() {
        Message message = messageSystem.createMessage("test_user", "+27821234567", "Test");
        messageSystem.sendMessage(message);
        
        String result = messageSystem.printMessages();
        assertTrue(result.contains("ALL SENT MESSAGES"));
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("+27821234567"));
    }
    
    @Test
    void testUpdateMessageStatus() {
        Message message = messageSystem.createMessage("test", "+27821234567", "Test");
        messageSystem.sendMessage(message);
        
        String messageID = message.getMessageID();
        messageSystem.updateMessageStatus(messageID, "Delivered");
        assertEquals("Delivered", message.getStatus());
    }
    
    @Test
    void testGetAllMessages() {
        Message sent = messageSystem.createMessage("user1", "+27821234567", "Sent message");
        Message stored = messageSystem.createMessage("user2", "+27987654321", "Stored message");
        
        messageSystem.sendMessage(sent);
        messageSystem.storeMessage(stored);
        
        List<Message> allMessages = messageSystem.getAllMessages();
        assertEquals(2, allMessages.size());
        assertTrue(allMessages.contains(sent));
        assertTrue(allMessages.contains(stored));
    }
    
    @Test
    void testClearMessages() {
        Message msg1 = messageSystem.createMessage("user1", "+27821234567", "Message 1");
        Message msg2 = messageSystem.createMessage("user2", "+27987654321", "Message 2");
        
        messageSystem.sendMessage(msg1);
        messageSystem.storeMessage(msg2);
        assertEquals(2, messageSystem.returnTotalMessages());
        
        messageSystem.clearMessages();
        assertEquals(0, messageSystem.returnTotalMessages());
    }
    
    // ========== MESSAGE CLASS TESTS ==========
    
    @Test
    void testMessageCreation() {
        Message message = new Message("sender_1", "+27821234567", "Test message");
        
        assertNotNull(message.getMessageID());
        assertNotNull(message.getMessageHash());
        assertEquals("sender_1", message.getSender());
        assertEquals("+27821234567", message.getRecipient());
        assertEquals("Test message", message.getMessage());
        assertEquals("Created", message.getStatus());
        assertNotNull(message.getTimestamp());
    }
    
    @Test
    void testMessageID_Uniqueness() {
        Message msg1 = new Message("user1", "+27821234567", "Message 1");
        Message msg2 = new Message("user2", "+27987654321", "Message 2");
        
        assertNotEquals(msg1.getMessageID(), msg2.getMessageID());
    }
    
    @Test
    void testMessageHash_Generation() {
        Message message = new Message("test", "+27821234567", "Hello");
        String hash = message.getMessageHash();
        
        assertNotNull(hash);
        assertTrue(hash.length() > 0);
        
        // Same content should produce same hash
        Message message2 = new Message("test", "+27821234567", "Hello");
    
    }

    @Test
    public void testMain() {
    }
}
