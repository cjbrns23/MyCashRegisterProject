// Carl Justine H. Briones
// IT1B

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class MenuItem {
    public String foodName;
    public double foodPrice;
    public int foodQuantity;
    
    public MenuItem(String foodName, double foodPrice, int foodQuantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        
    }
}

class User {
    public String username;
    public String password;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        
    }
}

public class Main {
    static ArrayList<MenuItem> menu = new ArrayList<>();
    static ArrayList<MenuItem> cart = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static String currentUser = "";
    
    public static void main(String[] args) {
        System.out.println("<>-----------------------<>");
        System.out.println("<>-----------------------<>");
        System.out.println(" Welcome to Cheesy Domain! ");
        System.out.println("<>-----------------------<>");
        System.out.println("<>-----------------------<>");
        
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("\n1. Sign Up\n2. Log In\n Enter a number: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                signup();
            } else if (choice == 2) {
                loggedIn = login();
            } else {
                System.out.print("Invalid choice, try again.");
            }
        }
        
        System.out.println("Login successful!");
        theMenu();
        boolean cheese = true;
        
        while (cheese) {
            System.out.println("1. Add Item");
            System.out.println("2. Show Menu");
            System.out.println("3. Show Cart Items");
            System.out.println("4. Total Payment");
            System.out.println("5. Remove Item");
            System.out.println("6. Accept Payment");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            
            switch (choice) {
                case 1: addItem(); break;
                case 2: showMenu(); break;
                case 3: showItems(); break;
                case 4: totalPay(); break;
                case 5: removeItem(); break; 
                case 6: showPayment(); break;
                case 7: cheese = false; break;
                default: System.out.println("Invalid choice, tyr again.");
            }
        }
        System.out.println("Thank you for ordering at Cheesy Domain, Come again!");
        
    }
    
    public static void signup() {
        while (true) {
            System.out.print("Create username (alphanumeric and 5-15 characters long): ");
            String username = sc.nextLine();
            System.out.print("Create password (one uppercase letter, one number, and be 8-20 characters long): ");
            String password = sc.nextLine();
            
            if (isValidUsername(username) && isValidPassword(password)) {
                users.add(new User(username, password));
                System.out.println("Signup successful, proceed to login.");
                break;
            } else {
                System.out.println("Invalid username or password, try again.\n");
            }
        }
    }
    
    public static boolean login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return true;
            }
        }
        System.out.println("Incorrect username or password, try again.");
        return false;
    }
    
    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{5,15}$");
        
    }
    
    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{8,20}$");
        
    }
    
    public static void theMenu() {
        menu.add(new MenuItem("Cheesy Overload Burger", 320, 0));
        menu.add(new MenuItem("Bacon n Cheese Supreme", 280, 0));
        menu.add(new MenuItem("Double Cheese Burger", 260, 0));
        menu.add(new MenuItem("Cheesy Fries", 140, 0));
        menu.add(new MenuItem("Melted Nachos", 150, 0));
        
    }
    
    public static void showMenu() {
        System.out.println("Menu");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + "." + menu.get(i).foodName + " - Php " + menu.get(i).foodPrice);

        }
    }
    
    public static void addItem() {
        showMenu();
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        if (choice > 0 && choice <= menu.size()) {
            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();
            sc.nextLine();
            MenuItem selectedItem = menu.get(choice - 1);
            cart.add(new MenuItem(selectedItem.foodName, selectedItem.foodPrice, quantity));
            System.out.println(quantity + "x" + selectedItem.foodName + "has beed added to your cart.");
        } else {
            System.out.print("Invalid choice, try again.");
        }
    }
    
    public static void showItems() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour cart:");
            for (MenuItem item : cart) {
                System.out.println(item.foodQuantity + "x" + item.foodName + "- Php" + (item.foodPrice * item.foodQuantity));
            }
        }
    }
    
    public static double totalPay() {
        double total = 0;
        for (MenuItem item : cart) {
            total += item.foodPrice * item.foodQuantity;
        }
        System.out.println("Total Price: Php " + total);
        return total;
        
    }
    
    public static void removeItem() {
        showItems();
        System.out.print("Enter item number to remove: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
            System.out.println("Item removed");
        } else {
            System.out.print("Invalid, try again");
            
        }
    }
    
    public static void showPayment() {
        if (cart.isEmpty()) {
            System.out.print("Cart is empty!");
            return;
            
        }
        
        double total = totalPay();
        try {
            FileWriter writer = new FileWriter("transactions.txt", true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            
            writer.write("Date/Time: " + dtf.format(now) + "\n");
            writer.write("Cashier: " + currentUser + "\n");
            writer.write("Items Purchased:\n");
            for (MenuItem item : cart) {
                writer.write("- " + item.foodName + " | QTY: " + item.foodQuantity + "| Price: Php" + item.foodPrice + "\n");
                
            }
            writer.write("Total Amount: Php" + total + "\n");
            writer.write("-----------------------------------------------------");
            writer.close();
            System.out.println("Transaction saved!");
            cart.clear();
            
        } catch (IOException e) {
            System.out.println("Invalid transaction");
            e.printStackTrace();
            
            return;
            
            
        }
    }   
}
