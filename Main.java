import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Admin curAdmin = new Admin(1234, "Stephan", "xa1@LW42%3");
        User curUser = new User(4455, "John");

        curAdmin.performOperation(new Update());
        curAdmin.performOperation(new Update());

        curUser.performOperation(new View());

        curAdmin.performOperation(new Delete());

        curUser.performOperation(new View());
    }
}

// Abstract class Account
abstract class Account {
    protected int id;
    protected String name;
    protected static List<String> dataStore = new ArrayList<>(); 

    public void performOperation(DataSource _myData) {
        _myData.execute();
    }
}

interface DataSource {
    void execute();
}

// Admin class extending Account
class Admin extends Account {
    private String AuthPassword;

    public Admin(int _id, String _name, String _password) {
        id = _id;
        name = _name;
        AuthPassword = _password;
    }
}

// User class extending Account
class User extends Account {
    public User(int _id, String _name) {
        id = _id;
        name = _name;
    }
}

// Update class implementing DataSource
class Update implements DataSource {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the data to add
        System.out.print("Enter data to update: ");
        String newData = scanner.nextLine();

        // Add the entered data to the shared dataStore
        Account.dataStore.add(newData);

        // Display a success message
        System.out.println("Data has been updated successfully!");
        System.out.println("Current data: " + Account.dataStore);
    }
}

// View class implementing DataSource
class View implements DataSource {
    @Override
    public void execute() {
        if (Account.dataStore.isEmpty()) {
            System.out.println("No data to display.");
        } else {
            System.out.println("Current data: " + Account.dataStore);
        }
    }
}


// Delete class implementing DataSource
class Delete implements DataSource {
    @Override
    public void execute() {
        if (!Account.dataStore.isEmpty()) {
            // Remove the first item (oldest data) from the dataStore
            String removedData = Account.dataStore.remove(0);
            System.out.println("Deleted: " + removedData);
        } else {
            System.out.println("No data to delete.");
        }
    }
}
