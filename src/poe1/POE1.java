/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poe1;

/**
 *
 * @author radzi
 */
  /*
 * Enhanced POE1 Messaging System
 * Complete implementation with all required features
 */
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class POE1 {
    public static void main(String[] args) {
        Login loginSystem = new Login();
        MessageSystem messageSystem = new MessageSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("Welcome to QuickChat ");
        System.out.println("Registration and Login System");
        
        while (running) {
            System.out.println("\n MAIN MENU ");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    handleRegistration(scanner, loginSystem);
                    break;
                    
                case 2:
                    handleLogin(scanner, loginSystem, messageSystem);
                    break;
                    
                case 3:
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                    
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void handleRegistration(Scanner scanner, Login loginSystem) {
        System.out.println("\n=== USER REGISTRATION ===");
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (!loginSystem.validateUsername(username)) {
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            return;
        }
        System.out.println("Username successfully captured.");
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!loginSystem.validatePassword(password)) {
            System.out.println("Password is not correctly formatted, please ensure the password contains at least eight characters, a capital letter, a number, and a special character.");
            return;
        }
        System.out.println("Password successfully captured.");
        
        System.out.print("Enter cell phone number: ");
        String phoneNumber = scanner.nextLine();
        if (!loginSystem.validatePhoneNumber(phoneNumber)) {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code. Please use format +27xxxxxxxxx and try again.");
            return;
        }
        System.out.println("Cell phone number successfully captured.");
        
        loginSystem.register(username, password, phoneNumber);
        System.out.println("Registration successful!");
    }
    
    private static void handleLogin(Scanner scanner, Login loginSystem, MessageSystem messageSystem) {
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        if (loginSystem.login(loginUsername, loginPassword)) {
            String firstName = loginUsername.split("_")[0];
            String lastName = loginUsername.contains("_") && loginUsername.split("_").length > 1 ? 
                             loginUsername.split("_")[1] : "";
            System.out.println("Welcome user " + firstName + " " + lastName + 
                             " (" + loginUsername + ")! It is great to see you again.");
            
            showMessageMenu(scanner, messageSystem, loginUsername);
        } else {
            System.out.println("Username or password incorrect, please try again.");
        }
    }
    
    private static void showMessageMenu(Scanner scanner, MessageSystem messageSystem, String username) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== MESSAGE MENU ===");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recent sent messages");
            System.out.println("3. Quit");
            System.out.print("Select option: ");
            
            int msgChoice = scanner.nextInt();
            scanner.nextLine();
            
            switch (msgChoice) {
                case 1:
                    handleSendMessage(scanner, messageSystem, username);
                    break;
                    
                case 2:
                    System.out.println(messageSystem.printMessages());
                    System.out.println("Total messages sent: " + messageSystem.returnTotalMessages());
                    break;
                    
                case 3:
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                    
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private static void handleSendMessage(Scanner scanner, MessageSystem messageSystem, String username) {
        System.out.println("\n=== SEND MESSAGE ===");
        
        System.out.print("Enter recipient cell number: ");
        String recipient = scanner.nextLine();
        if (!messageSystem.checkRecipientCell(recipient)) {
            System.out.println("Cell phone number incorrectly formatted or does not contain an international code. Please use format +27xxxxxxx and try again.");
            return;
        }
        
        System.out.print("Enter message (max 250 characters): ");
        String messageText = scanner.nextLine();
        if (!messageSystem.checkMessageID(messageText)) {
            System.out.println("Message exceeds 250 characters by " + 
                             (messageText.length() - 250) + " {enter number here}, please reduce size.");
            return;
        }
        
        Message newMessage = messageSystem.createMessage(username, recipient, messageText);
        
        // Show message options
        System.out.println("\nMessage ready to send. Choose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Select option: ");
        
        int sendChoice = scanner.nextInt();
        scanner.nextLine();
        
        switch (sendChoice) {
            case 1:
                messageSystem.sendMessage(newMessage);
                System.out.println("Message successfully sent.");
                break;
            case 2:
                System.out.println("Press 0 to delete message.");
                int deleteChoice = scanner.nextInt();
                if (deleteChoice == 0) {
                    System.out.println("Message deleted successfully.");
                }
                break;
            case 3:
                messageSystem.storeMessage(newMessage);
                System.out.println("Message successfully stored.");
                break;
            default:
                System.out.println("Invalid option. Message discarded.");
        }
    }
}

class Login {
    private Map<String, String> userCredentials = new HashMap<>();
    private Map<String, String> userPhones = new HashMap<>();
    
    public boolean validateUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    public boolean validatePassword(String password) {
        if (password.length() < 8) return false;
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }
    
    public boolean validatePhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() >= 12;
    }
    
    public void register(String username, String password, String phone) {
        userCredentials.put(username, password);
        userPhones.put(username, phone);
    }
    
    public boolean login(String username, String password) {
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}

class Message {
    private String messageID;
    private String messageHash;
    private String sender;
    private String recipient;
    private String message;
    private LocalDateTime timestamp;
    private String status;
    
    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageID = "MSG-" + System.currentTimeMillis();
        this.messageHash = generateMessageHash();
        this.timestamp = LocalDateTime.now();
        this.status = "Created";
    }
    
    private String generateMessageHash() {
        String hashInput = message + recipient + sender;
        return String.format("%08X", Math.abs(hashInput.hashCode()));
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    
    // Setters
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("MessageID: %s | Hash: %s | To: %s | Message: %s | Status: %s | Time: %s",
                messageID, messageHash, recipient, message, status, timestamp.format(formatter));
    }
}

class MessageSystem {
    private List<Message> messages = new ArrayList<>();
    private List<Message> storedMessages = new ArrayList<>();
    private int messageCount = 0;
    
    public boolean checkMessageID(String message) {
        return message.length() <= 250;
    }
    
    public boolean checkRecipientCell(String cell) {
        return cell.startsWith("+27") && cell.length() >= 12 && cell.length() <= 13;
    }
    
    public Message createMessage(String sender, String recipient, String message) {
        return new Message(sender, recipient, message);
    }
    
    public void sendMessage(Message message) {
        message.setStatus("Sent");
        messages.add(message);
        messageCount++;
        
        // Auto-generate test data as specified
        if (messageCount == 1) {
            generateTestMessage1();
        } else if (messageCount == 2) {
            generateTestMessage2();
        }
    }
    
    public void storeMessage(Message message) {
        message.setStatus("Stored");
        storedMessages.add(message);
        
        // Create JSON representation (simulated)
        try {
            saveMessageToJSON(message);
        } catch (Exception e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
    
    private void saveMessageToJSON(Message message) {
        // Simulated JSON storage
        String jsonData = String.format(
            "{\n  \"messageID\": \"%s\",\n  \"messageHash\": \"%s\",\n  \"sender\": \"%s\",\n  \"recipient\": \"%s\",\n  \"message\": \"%s\",\n  \"status\": \"%s\",\n  \"timestamp\": \"%s\"\n}",
            message.getMessageID(),
            message.getMessageHash(),
            message.getSender(),
            message.getRecipient(),
            message.getMessage(),
            message.getStatus(),
            message.getTimestamp().toString()
        );
        
        System.out.println("Message stored in JSON format:");
        System.out.println(jsonData);
    }
    
    private void generateTestMessage1() {
        Message testMsg1 = new Message("test_user", "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        testMsg1.setStatus("Sent");
        messages.add(testMsg1);
        messageCount++;
    }
    
    private void generateTestMessage2() {
        Message testMsg2 = new Message("test_user", "0857975680", "Hi Karsen, did you receive the payment?");
        testMsg2.setStatus("Discarded");
        messages.add(testMsg2);
        messageCount++;
    }
    
    public String printMessages() {
        if (messages.isEmpty()) {
            return "No messages to display.";
        }
        
        StringBuilder result = new StringBuilder("=== ALL SENT MESSAGES ===\n");
        for (int i = 0; i < messages.size(); i++) {
            result.append((i + 1)).append(". ").append(messages.get(i).toString()).append("\n");
        }
        
        if (!storedMessages.isEmpty()) {
            result.append("\n=== STORED MESSAGES ===\n");
            for (int i = 0; i < storedMessages.size(); i++) {
                result.append((i + 1)).append(". ").append(storedMessages.get(i).toString()).append("\n");
            }
        }
        
        return result.toString();
    }
    
    public int returnTotalMessages() {
        return messages.size() + storedMessages.size();
    }
    
    // Additional utility methods for testing
    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messages);
        allMessages.addAll(storedMessages);
        return allMessages;
    }
    
    public void clearMessages() {
        messages.clear();
        storedMessages.clear();
        messageCount = 0;
    }
    
    // Method to simulate message status updates
    public void updateMessageStatus(String messageID, String newStatus) {
        for (Message msg : messages) {
            if (msg.getMessageID().equals(messageID)) {
                msg.setStatus(newStatus);
                break;
            }
        }
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(messageID)) {
                msg.setStatus(newStatus);
                break;
            }
        }
    }
}      
    