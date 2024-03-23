import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CarRentalSystemGUI {
    private CarRentalSystem rentalSystem;
    private JTextArea availableCarsTextArea;

    private JFrame frame;
    private JTextField nameField;
    private JTextField aadharField;
    private JTextField carIdField;
    private JTextField rentalDaysField;

    public CarRentalSystemGUI(CarRentalSystem rentalSystem) {
        this.rentalSystem = rentalSystem;

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Car Rental System");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titleLabel = new JLabel("Car Rental System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(150, 10, 200, 30);
        frame.getContentPane().add(titleLabel);

        // ----------------------------------- Name
        // -----------------------------------------
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 20);
        frame.getContentPane().add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 150, 20);
        frame.getContentPane().add(nameField);

        // ----------------------------------- Aadhar
        // -----------------------------------------
        JLabel aadharLabel = new JLabel("Aadhar Number:");
        aadharLabel.setBounds(50, 80, 100, 20);
        frame.getContentPane().add(aadharLabel);

        aadharField = new JTextField();
        aadharField.setBounds(150, 80, 150, 20);
        frame.getContentPane().add(aadharField);

        // ----------------------------------- Licence
        // -----------------------------------------
        JLabel licenceLabel = new JLabel("Licence Number:");
        licenceLabel.setBounds(50, 110, 100, 20);
        frame.getContentPane().add(licenceLabel);

        JTextField licenceField = new JTextField();
        licenceField.setBounds(150, 110, 150, 20);
        frame.getContentPane().add(licenceField);

        // ----------------------------------- Car ID
        // -----------------------------------------
        JLabel carIdLabel = new JLabel("Car ID:");
        carIdLabel.setBounds(50, 140, 100, 20);
        frame.getContentPane().add(carIdLabel);

        carIdField = new JTextField();
        carIdField.setBounds(150, 140, 150, 20);
        frame.getContentPane().add(carIdField);

        // ----------------------------------- Rental Days
        // -----------------------------------------
        JLabel rentalDaysLabel = new JLabel("Rental Days:");
        rentalDaysLabel.setBounds(50, 170, 100, 20);
        frame.getContentPane().add(rentalDaysLabel);

        rentalDaysField = new JTextField();
        rentalDaysField.setBounds(150, 170, 150, 20);
        frame.getContentPane().add(rentalDaysField);

        // ----------------------------------- Buttons
        // -----------------------------------------
        JButton rentButton = new JButton("Rent Car");
        rentButton.setBounds(50, 200, 100, 30);
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rentCar();
            }
        });
        frame.getContentPane().add(rentButton);

        JButton returnButton = new JButton("Return Car");
        returnButton.setBounds(170, 200, 100, 30);
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCar();
            }
        });
        frame.getContentPane().add(returnButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(290, 200, 100, 30);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(exitButton);

        JLabel availableCarsLabel = new JLabel("Available Cars:");
        availableCarsLabel.setBounds(50, 230, 100, 20);
        frame.getContentPane().add(availableCarsLabel);

        availableCarsTextArea = new JTextArea();
        availableCarsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(availableCarsTextArea);
        scrollPane.setBounds(50, 250, 400, 100);
        frame.getContentPane().add(scrollPane);

        updateAvailableCarsList();
    }

    private void updateAvailableCarsList() {
        StringBuilder sb = new StringBuilder();
        for (Car car : rentalSystem.getCars()) {
            if (car.isAvailable()) {
                sb.append(car.getCarId()).append(" - ").append(car.getBrand()).append(" ").append(car.getModel())
                        .append("\n");
            }
        }
        availableCarsTextArea.setText(sb.toString());
    }

    private void rentCar() {
        String name = nameField.getText();
        String aadharNumber = aadharField.getText();
        String carId = carIdField.getText();
        int rentalDays = Integer.parseInt(rentalDaysField.getText());

        Customer customer = new Customer("CUS" + (rentalSystem.getCustomers().size() + 1), name);
        rentalSystem.addCustomer(customer);

        Car selectedCar = null;
        for (Car car : rentalSystem.getCars()) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar != null) {
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            rentalSystem.rentCar(selectedCar, customer, rentalDays);
            JOptionPane.showMessageDialog(frame, "Car rented successfully.\nTotal Price: $" + totalPrice);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid car selection or car not available for rent.");
        }
    }

    private void returnCar() {
        String carId = carIdField.getText();

        Car carToReturn = null;
        for (Car car : rentalSystem.getCars()) {
            if (car.getCarId().equals(carId) && !car.isAvailable()) {
                carToReturn = car;
                break;
            }
        }

        if (carToReturn != null) {
            Customer customer = null;
            for (Rental rental : rentalSystem.getRentals()) {
                if (rental.getCar() == carToReturn) {
                    customer = rental.getCustomer();
                    break;
                }
            }

            if (customer != null) {
                rentalSystem.returnCar(carToReturn);
                JOptionPane.showMessageDialog(frame, "Car returned successfully by " + customer.getName());
            } else {
                JOptionPane.showMessageDialog(frame, "Car was not rented or rental information is missing.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid car ID or car is not rented.");
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}