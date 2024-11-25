import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create Admin and User
        Admin curAdmin = new Admin(1234, "Stephan", "xa1@LW42%3");
        User curUser = new User(4455, "John");

        // Add initial data to the dataStore
        Account.initializeDataStore(List.of(curAdmin.toString(), curUser.toString()));

        // View initial data
        System.out.println("Viewing Initial Data:");
        curUser.performOperation(new View());

        // Admin updates the data
        curAdmin.performOperation(new Update());
        curAdmin.performOperation(new Update());

        // Admin modifies their name
        System.out.println("\nModifying Admin Name:");
        curAdmin.performOperation(new Modify("name"));

        // User views updated data
        System.out.println("\nAfter Updates:");
        curUser.performOperation(new View());

        // Admin deletes an entry
        curAdmin.performOperation(new Delete());

        // User views data after deletion
        System.out.println("\nAfter Deletion:");
        curUser.performOperation(new View());
    }
}

// Abstract class Account
abstract class Account {
    protected int id;
    protected String name;
    private static List<String> dataStore = new ArrayList<>();

    public void performOperation(DataSource operation) {
        operation.execute();
    }

    // Initialize the data store with initial values
    public static void initializeDataStore(List<String> initialData) {
        dataStore.addAll(initialData);
    }

    // Encapsulated methods to manage dataStore
    protected static void addData(String data) {
        dataStore.add(data);
    }

    protected static boolean removeFirstData() {
        if (!dataStore.isEmpty()) {
            dataStore.remove(0);
            return true;
        }
        return false;
    }

    protected static boolean isDataStoreEmpty() {
        return dataStore.isEmpty();
    }

    protected static List<String> getDataStore() {
        return new ArrayList<>(dataStore); // Return a copy for safety
    }

    protected static void printDataStore() {
        if (dataStore.isEmpty()) {
            System.out.println("No data available.");
        } else {
            System.out.println("Current data: " + dataStore);
        }
    }

    protected static void updateDataStore(int index, String updatedValue) {
        if (index >= 0 && index < dataStore.size()) {
            dataStore.set(index, updatedValue);
        }
    }

    @Override
    public abstract String toString(); // Enforce custom string representation
}

// Interface for operations
interface DataSource {
    void execute();
}

// Admin class extending Account
class Admin extends Account {
    private String authPassword;

    public Admin(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.authPassword = password;
    }

    public void updateName(String newName) {
        this.name = newName;
        // Update dataStore to reflect the change
        Account.updateDataStore(0, this.toString());
    }

    @Override
    public String toString() {
        return "Admin{id=" + id + ", name='" + name + "', password='" + authPassword + "'}";
    }
}

// User class extending Account
class User extends Account {
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}

// Update operation
class Update implements DataSource {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new data to add: ");
        String newData = scanner.nextLine();

        // Add data and show success message
        Account.addData(newData);
        System.out.println("Data has been updated successfully!");
        Account.printDataStore();
    }
}

// Modify operation
class Modify implements DataSource {
    private final String attributeToModify;

    public Modify(String attributeToModify) {
        this.attributeToModify = attributeToModify;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        if (attributeToModify.equalsIgnoreCase("name")) {
            System.out.print("Enter the new name for Admin: ");
            String newName = scanner.nextLine();

            // Directly modify the Admin's name in the dataStore
            List<String> dataStore = Account.getDataStore();

            // Find the Admin data in the dataStore (assume Admin is always the first entry)
            if (!dataStore.isEmpty() && dataStore.get(0).contains("Admin")) {
                // Extract the Admin object and update its name
                String adminData = dataStore.get(0);
                String updatedAdminData = adminData.replaceFirst("name='[^']*'", "name='" + newName + "'");
                
                // Update the dataStore
                Account.updateDataStore(0, updatedAdminData);
                System.out.println("Admin's name has been updated successfully!");
            } else {
                System.out.println("No Admin data found to modify.");
            }
        }
    }
}


// View operation
class View implements DataSource {
    @Override
    public void execute() {
        System.out.println("Viewing data...");
        Account.printDataStore();
    }
}

// Delete operation
class Delete implements DataSource {
    @Override
    public void execute() {
        if (Account.removeFirstData()) {
            System.out.println("Oldest data deleted successfully.");
        } else {
            System.out.println("No data to delete.");
        }
        Account.printDataStore();
    }
}
