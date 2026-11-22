import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

class User {
    private String name;
    private String pass;
    private String email;
    private String dob;
    private String phno;
    private String address;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getPhno() { return phno; }
    public void setPhno(String phno) { this.phno = phno; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

class Driver extends User {
    private String gender;
    private String licenseNo;
    private String expDate;
    private String typeOfVehicle;
    private String licensePhotoPath;

    // Getters and setters
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public String getExpDate() { return expDate; }
    public void setExpDate(String expDate) { this.expDate = expDate; }

    public String getTypeOfVehicle() { return typeOfVehicle; }
    public void setTypeOfVehicle(String typeOfVehicle) { this.typeOfVehicle = typeOfVehicle; }

    public String getLicensePhotoPath() { return licensePhotoPath; }
    public void setLicensePhotoPath(String licensePhotoPath) { this.licensePhotoPath = licensePhotoPath; }
}

public class Porterapp {
    private static Connection con;
    private static PreparedStatement ps1;

    public void jspInit() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/porterdb", "root", "1234");
            System.out.println("Database connection established.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return Pattern.matches(emailPattern, email);
    }

    private static boolean isValidLicenseNumber(String licenseNo) {
        String licensePattern = "^[A-Z0-9]{15}$";
        return Pattern.matches(licensePattern, licenseNo);
    }

    public static void registerDriver() {
        Scanner scanner = new Scanner(System.in);
        Driver driver = new Driver();

        System.out.print("Enter name: ");
        driver.setName(scanner.nextLine());

        while (true) {
            System.out.print("Enter password: ");
            driver.setPass(scanner.nextLine());
            if (isValidPassword(driver.getPass())) {
                break;
            } else {
                System.out.println("Invalid password. It must contain at least one capital letter, one number, and one special character.");
            }
        }

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        driver.setDob(scanner.nextLine());

        System.out.print("Enter gender: ");
        driver.setGender(scanner.nextLine());

        System.out.print("Enter phone number: ");
        driver.setPhno(scanner.nextLine());

        System.out.print("Enter address: ");
        driver.setAddress(scanner.nextLine());

        System.out.print("Enter path to license photo: ");
        String photoPath = scanner.nextLine();
        File photo = new File(photoPath);

        try {
            if (photo.exists() && !photo.isDirectory()) {
                driver.setLicensePhotoPath(photoPath);
            } else {
                throw new IOException("License photo file not found: " + photoPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            System.out.print("Enter license number: ");
            driver.setLicenseNo(scanner.nextLine());
            if (isValidLicenseNumber(driver.getLicenseNo())) {
                break;
            } else {
                System.out.println("Invalid license number. It must be alphanumeric and exactly 15 characters long.");
            }
        }

        System.out.print("Enter license expiry date (YYYY-MM-DD): ");
        driver.setExpDate(scanner.nextLine());

        System.out.print("Enter type of vehicle: ");
        driver.setTypeOfVehicle(scanner.nextLine());

        try {
            String sql = "INSERT INTO driver_details (name, pass, dob, gender, phno, address, license_photo, license_no, exp_date, type_of_vehicle) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, driver.getName());
            ps1.setString(2, driver.getPass());
            ps1.setString(3, driver.getDob());
            ps1.setString(4, driver.getGender());
            ps1.setString(5, driver.getPhno());
            ps1.setString(6, driver.getAddress());
            ps1.setString(7, driver.getLicensePhotoPath());
            ps1.setString(8, driver.getLicenseNo());
            ps1.setString(9, driver.getExpDate());
            ps1.setString(10, driver.getTypeOfVehicle());
            ps1.executeUpdate();
            System.out.println("Driver registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loginDriver() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        try {
            String sql = "SELECT * FROM driver_details WHERE name = ? AND pass = ?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, name);
            ps1.setString(2, pass);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful. Welcome, " + name + "!");
            } else {
                System.out.println("Invalid name or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        System.out.print("Enter name: ");
        user.setName(scanner.nextLine());

        while (true) {
            System.out.print("Enter password: ");
            user.setPass(scanner.nextLine());
            if (isValidPassword(user.getPass())) {
                break;
            } else {
                System.out.println("Invalid password. It must contain at least one capital letter, one number, and one special character.");
            }
        }

        while (true) {
            System.out.print("Enter email: ");
            user.setEmail(scanner.nextLine());
            if (isValidEmail(user.getEmail())) {
                break;
            } else {
                System.out.println("Invalid email. It must be a valid Gmail address ending with @gmail.com.");
            }
        }

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        user.setDob(scanner.nextLine());

        System.out.print("Enter phone number: ");
        user.setPhno(scanner.nextLine());

        System.out.print("Enter address: ");
        user.setAddress(scanner.nextLine());

        try {
            String sql = "INSERT INTO user_details (name, pass, email, dob, phno, address) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, user.getName());
            ps1.setString(2, user.getPass());
            ps1.setString(3, user.getEmail());
            ps1.setString(4, user.getDob());
            ps1.setString(5, user.getPhno());
            ps1.setString(6, user.getAddress());
            ps1.executeUpdate();
            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        try {
            String sql = "SELECT * FROM user_details WHERE name = ? AND pass = ?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, name);
            ps1.setString(2, pass);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful. Welcome, " + name + "!");
            } else {
                System.out.println("Invalid name or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void bookRide() {
    	System.out.println("Book a Ride");        
    	Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Pickup Point: ");
        String pickPoint = scanner.nextLine();

        System.out.print("Enter Drop Point: ");
        String dropPoint = scanner.nextLine();

        System.out.print("Select Vehicle (Car, Bike, Auto): ");
        String selectVehicle = scanner.nextLine();

        System.out.print("Enter Estimated Kilometers: ");
        float estiKm = scanner.nextFloat();

        // Base fare for different vehicles
        float perKmRate;
        switch (selectVehicle.toLowerCase()) {
            case "car":
                perKmRate = 20.0f;
                break;
            case "auto":
                perKmRate = 15.0f;
                break;
            case "bike":
                perKmRate = 10.0f;
                break;
            default:
                System.out.println("Invalid vehicle type. Please choose Car, Bike, or Auto.");
                return;
        }

        // Calculate fare (base rate + per km charge)
        float baseRate = 50.0f; // base fare
        float calculateFare = baseRate + (estiKm * perKmRate);

        // Booking status: true for confirmed, false for pending or cancelled
        boolean bookStatus = true;

        try {
            String sql = "INSERT INTO user_home (user_id, pickpoint, droppoint, select_vehicle, book_status, esti_km, calculate_fare) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps1 = con.prepareStatement(sql);
            ps1.setInt(1, userId);
            ps1.setString(2, pickPoint);
            ps1.setString(3, dropPoint);
            ps1.setString(4, selectVehicle);
            ps1.setBoolean(5, bookStatus);
            ps1.setFloat(6, estiKm);
            ps1.setFloat(7, calculateFare);
            ps1.executeUpdate();
            System.out.println("Ride booked successfully. Estimated fare: " + calculateFare);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Porterapp app = new Porterapp();
        app.jspInit();  // Initialize the connection

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Register Driver");
            System.out.println("4. Login Driver");
            //System.out.println("5. Book Ride");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    bookRide();
                    break;
                case 3:
                    registerDriver();
                    break;
                case 4:
                    loginDriver();
                    break;
                //case 5:
                	//bookRide();
                	//break;
                case 6:
                    System.out.println("Exiting...");
                    try {
                        if (con != null && !con.isClosed()) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
