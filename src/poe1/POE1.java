/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poe1;

/**
 *
 * @author radzi
 */
import java.util.*;

public class POE1 {
    public static void main(String[] args) {
        Login loginSystem = new Login();
        MessageSystem messageSystem = new MessageSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("Registration and Login");
        
        while (running) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Select option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    // Signup
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    if (!loginSystem.validateUsername(username)) {
                        System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
                        break;
                    } else {
                        System.out.println("Username successfully captured.");
                    }
                    
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    if (!loginSystem.validatePassword(password)) {
                        System.out.println("Password is not correctly formatted, please ensure the password contains at least eight characters, a capital letter, a number, and a special character.");
                        break;
                    } else {
                        System.out.println("Password successfully captured.");
                    }
                    
                    System.out.print("Enter cell phone number: ");
                    String phoneNumber = scanner.nextLine();
                    if (!loginSystem.validatePhoneNumber(phoneNumber)) {
                        System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
                        break;
                    } else {
                        System.out.println("Cell phone number successfully added.");
                    }
                    
                    loginSystem.register(username, password, phoneNumber);
                    System.out.println("Registration successful!");
                    break;
                    
                case 2:
                    // Login
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
                        
                        // Message menu after login
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n1. Send message\n2. View messages\n3. Message count\n4. Logout");
                            System.out.print("Select option: ");
                            int msgChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            
                            switch (msgChoice) {
                                case 1:
                                    System.out.print("Enter recipient cell: ");
                                    String recipient = scanner.nextLine();
                                    if (!messageSystem.checkRecipientCell(recipient)) {
                                        System.out.println("Cell phone number incorrectly formatted.");
                                        break;
                                    }//switch statement is used to make decisions based on the value of a variable.
                                    
                                    System.out.print("Enter message: ");
                                    String messageText = scanner.nextLine();
                                    if (!messageSystem.checkMessageID(messageText)) {
                                        System.out.println("Message too long (max 10 characters).");
                                        break;
                                    }
                                    
                                    Message newMessage = messageSystem.createMessage(loginUsername, recipient, messageText);
                                    messageSystem.sendMessage(newMessage);
                                    break;
                                    
                                case 2:
                                    System.out.println(messageSystem.printMessages());
                                    break;
                                    
                                case 3:
                                    System.out.println("Total messages: " + messageSystem.returnTotalMessages());
                                    break;
                                    
                                case 4:
                                    loggedIn = false;
                                    System.out.println("Logged out successfully.");
                                    break;
                                    
                                default:
                                    System.out.println("Invalid option.");
                            }
                        }
                    } else {
                        System.out.println("Username or password incorrect, please try again.");
                    }
                    break;
                    
                case 3:
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                    
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}

class Login {
    private Map<String, String> userCredentials = new HashMap<>();
    private Map<String, String> userPhones = new HashMap<>();
    
    public boolean validateUsername(String username) {
        // Username must contain underscore and be max 5 characters
        return username.contains("_") && username.length() <= 5;
    }
    
    public boolean validatePassword(String password) {
        // At least 8 chars, capital letter, number, and special character
        if (password.length() < 8) return false;
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }//char (short for character) is a data type used to store a single character
        
        return hasCapital && hasNumber && hasSpecial;
    }
    
    public boolean validatePhoneNumber(String phone) {
        // Must start with +27 and be properly formatted
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
    
    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageID = "MSG" + System.currentTimeMillis();
        this.messageHash = "HASH" + Math.abs(message.hashCode());
    }
    
    @Override
    public String toString() {
        return "MessageID: " + messageID + ", MessageHash: " + messageHash + 
               ", Recipient: " + recipient + ", Message: " + message;
    }
}

class MessageSystem {
    private List<Message> messages = new ArrayList<>();
    
    public boolean checkMessageID(String message) {
        // Message must be max 10 characters
        return message.length() <= 10;
    }
    
    public boolean checkRecipientCell(String cell) {
        // Cell must be max 10 chars and start with +
        return cell.startsWith("+") && cell.length() <= 10;
    }
    
    public Message createMessage(String sender, String recipient, String message) {
        return new Message(sender, recipient, message);
    }
    
    public void sendMessage(Message message) {
        messages.add(message);
        System.out.println("Message sent successfully!");
    }
    
    public String printMessages() {
        if (messages.isEmpty()) {
            return "No messages to display.";
        }
        
        StringBuilder result = new StringBuilder("All Messages:\n");
        for (Message message : messages) {
            result.append(message.toString()).append("\n");
        }
        return result.toString();
    }
    
    public int returnTotalMessages() {
        return messages.size();
    }
    
    public void storeMessage(Message message) {
        messages.add(message);
        System.out.println("Message stored successfully!");
        // In a real implementation, this would save to JSON
    }
}
        
    