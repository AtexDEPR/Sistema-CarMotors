package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.VehicleController;
import com.carmotorsproject.services.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swing UI for managing vehicles.
 */
public class VehicleView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(VehicleView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final VehicleController controller;

    // UI Components
    private JTable vehicleTable;
    private DefaultTableModel vehicleTableModel;

    // Form Components
    private JTextField customerIdField;
    private JTextField licensePlateField;
    private JTextField makeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField vinField;
    private JTextField colorField;
    private JTextField engineTypeField;
    private JTextField transmissionField;
    private JTextField mileageField;
    private JTextField lastServiceDateField;
    private JTextArea notesArea;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;

    // Selected ID
    private int selectedVehicleId = 0;

    /**
     * Constructor that initializes the UI and controller.
     */
    public VehicleView() {
        this.controller = new VehicleController(this);
        initComponents();
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Vehicle Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Vehicles"));

        // Create the table model
        vehicleTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        vehicleTableModel.addColumn("ID");
        vehicleTableModel.addColumn("Customer ID");
        vehicleTableModel.addColumn("License Plate");
        vehicleTableModel.addColumn("Make");
        vehicleTableModel.addColumn("Model");
        vehicleTableModel.addColumn("Year");
        vehicleTableModel.addColumn("VIN");
        vehicleTableModel.addColumn("Color");
        vehicleTableModel.addColumn("Engine Type");
        vehicleTableModel.addColumn("Transmission");
        vehicleTableModel.addColumn("Mileage");
        vehicleTableModel.addColumn("Last Service Date");

        // Create the table
        vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.getTableHeader().setReorderingAllowed(false);

        // Add a mouse listener to the table
        vehicleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = vehicleTable.getSelectedRow();
                if (row >= 0) {
                    selectedVehicleId = (int) vehicleTableModel.getValueAt(row, 0);
                    controller.loadVehicleDetails(selectedVehicleId);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setPreferredSize(new Dimension(850, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Customer ID:"), gbc);

        gbc.gridx = 1;
        customerIdField = new JTextField(10);
        panel.add(customerIdField, gbc);

        // License Plate
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("License Plate:"), gbc);

        gbc.gridx = 1;
        licensePlateField = new JTextField(10);
        panel.add(licensePlateField, gbc);

        // Make
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Make:"), gbc);

        gbc.gridx = 1;
        makeField = new JTextField(20);
        panel.add(makeField, gbc);

        // Model
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Model:"), gbc);

        gbc.gridx = 1;
        modelField = new JTextField(20);
        panel.add(modelField, gbc);

        // Year
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Year:"), gbc);

        gbc.gridx = 1;
        yearField = new JTextField(5);
        panel.add(yearField, gbc);

        // VIN
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("VIN:"), gbc);

        gbc.gridx = 1;
        vinField = new JTextField(20);
        panel.add(vinField, gbc);

        // Color
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Color:"), gbc);

        gbc.gridx = 1;
        colorField = new JTextField(10);
        panel.add(colorField, gbc);

        // Engine Type
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Engine Type:"), gbc);

        gbc.gridx = 1;
        engineTypeField = new JTextField(20);
        panel.add(engineTypeField, gbc);

        // Transmission
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Transmission:"), gbc);

        gbc.gridx = 1;
        transmissionField = new JTextField(10);
        panel.add(transmissionField, gbc);

        // Mileage
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(new JLabel("Current Mileage:"), gbc);

        gbc.gridx = 1;
        mileageField = new JTextField(10);
        panel.add(mileageField, gbc);

        // Last Service Date
        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(new JLabel("Last Service Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        lastServiceDateField = new JTextField(10);
        panel.add(lastServiceDateField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 11;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        panel.add(notesScrollPane, gbc);

        return panel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        addButton = new JButton("Add Vehicle");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVehicle();
            }
        });
        panel.add(addButton);

        updateButton = new JButton("Update Vehicle");
        updateButton.setEnabled(false);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVehicle();
            }
        });
        panel.add(updateButton);

        deleteButton = new JButton("Delete Vehicle");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVehicle();
            }
        });
        panel.add(deleteButton);

        clearButton = new JButton("Clear Form");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        panel.add(clearButton);

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVehicles();
            }
        });
        panel.add(searchButton);

        return panel;
    }

    /**
     * Loads data into the table.
     */
    public void loadData() {
        controller.loadVehicles();
    }

    /**
     * Adds a new vehicle.
     */
    private void addVehicle() {
        try {
            // Create a new Vehicle object from form data
            Vehicle vehicle = getVehicleFromForm();

            // Call the controller to add the vehicle
            controller.addVehicle(vehicle);

            // Clear the form
            clearForm();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error adding vehicle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing vehicle.
     */
    private void updateVehicle() {
        try {
            // Create a Vehicle object from form data
            Vehicle vehicle = getVehicleFromForm();
            vehicle.setVehicleId(selectedVehicleId);

            // Call the controller to update the vehicle
            controller.updateVehicle(vehicle);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error updating vehicle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a vehicle.
     */
    private void deleteVehicle() {
        try {
            // Confirm deletion
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this vehicle?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Call the controller to delete the vehicle
                controller.deleteVehicle(selectedVehicleId);

                // Clear the form
                clearForm();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting vehicle", ex);
            JOptionPane.showMessageDialog(this, "Error deleting vehicle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for vehicles based on make and model.
     */
    private void searchVehicles() {
        try {
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();

            if (make.isEmpty() && model.isEmpty()) {
                // If both fields are empty, load all vehicles
                controller.loadVehicles();
            } else {
                // Search by make and model
                controller.searchVehiclesByMakeAndModel(make, model);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error searching vehicles", ex);
            JOptionPane.showMessageDialog(this, "Error searching vehicles: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form.
     */
    private void clearForm() {
        // Reset form fields
        customerIdField.setText("");
        licensePlateField.setText("");
        makeField.setText("");
        modelField.setText("");
        yearField.setText("");
        vinField.setText("");
        colorField.setText("");
        engineTypeField.setText("");
        transmissionField.setText("");
        mileageField.setText("");
        lastServiceDateField.setText("");
        notesArea.setText("");

        // Reset selected ID
        selectedVehicleId = 0;

        // Disable buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Creates a Vehicle object from the form data.
     *
     * @return A Vehicle object populated with form data
     * @throws ParseException If a date parsing error occurs
     */
    private Vehicle getVehicleFromForm() throws ParseException {
        Vehicle vehicle = new Vehicle();

        // Get the customer ID
        String customerIdStr = customerIdField.getText().trim();
        if (!customerIdStr.isEmpty()) {
            try {
                vehicle.setCustomerId(Integer.parseInt(customerIdStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid customer ID.");
            }
        } else {
            throw new IllegalArgumentException("Please enter a customer ID.");
        }

        // Get the license plate
        String licensePlate = licensePlateField.getText().trim();
        if (!licensePlate.isEmpty()) {
            vehicle.setLicensePlate(licensePlate);
        } else {
            throw new IllegalArgumentException("Please enter a license plate.");
        }

        // Get the make
        String make = makeField.getText().trim();
        if (!make.isEmpty()) {
            vehicle.setMake(make);
        } else {
            throw new IllegalArgumentException("Please enter a make.");
        }

        // Get the model
        String model = modelField.getText().trim();
        if (!model.isEmpty()) {
            vehicle.setModel(model);
        } else {
            throw new IllegalArgumentException("Please enter a model.");
        }

        // Get the year
        String yearStr = yearField.getText().trim();
        if (!yearStr.isEmpty()) {
            try {
                int year = Integer.parseInt(yearStr);
                if (year < 1900 || year > 2100) {
                    throw new IllegalArgumentException("Please enter a valid year (1900-2100).");
                }
                vehicle.setYear(year);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid year.");
            }
        } else {
            throw new IllegalArgumentException("Please enter a year.");
        }

        // Get the VIN
        vehicle.setVin(vinField.getText().trim());

        // Get the color
        vehicle.setColor(colorField.getText().trim());

        // Get the engine type
        vehicle.setEngineType(engineTypeField.getText().trim());

        // Get the transmission
        vehicle.setTransmission(transmissionField.getText().trim());

        // Get the mileage
        String mileageStr = mileageField.getText().trim();
        if (!mileageStr.isEmpty()) {
            try {
                vehicle.setCurrentMileage(Integer.parseInt(mileageStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid mileage.");
            }
        } else {
            throw new IllegalArgumentException("Please enter the current mileage.");
        }

        // Get the last service date
        String lastServiceDateStr = lastServiceDateField.getText().trim();
        if (!lastServiceDateStr.isEmpty()) {
            vehicle.setLastServiceDate(DATE_FORMAT.parse(lastServiceDateStr));
        }

        // Get the notes
        vehicle.setNotes(notesArea.getText().trim());

        return vehicle;
    }

    /**
     * Updates the vehicle table with the provided vehicles.
     *
     * @param vehicles The list of vehicles to display
     */
    public void updateVehicleTable(List<Vehicle> vehicles) {
        // Clear the table
        vehicleTableModel.setRowCount(0);

        // Add vehicles to the table
        for (Vehicle vehicle : vehicles) {
            Vector<Object> row = new Vector<>();
            row.add(vehicle.getVehicleId());
            row.add(vehicle.getCustomerId());
            row.add(vehicle.getLicensePlate());
            row.add(vehicle.getMake());
            row.add(vehicle.getModel());
            row.add(vehicle.getYear());
            row.add(vehicle.getVin());
            row.add(vehicle.getColor());
            row.add(vehicle.getEngineType());
            row.add(vehicle.getTransmission());
            row.add(vehicle.getCurrentMileage());
            row.add(vehicle.getLastServiceDate() != null ? DATE_FORMAT.format(vehicle.getLastServiceDate()) : "");

            vehicleTableModel.addRow(row);
        }
    }

    /**
     * Populates the form with the provided vehicle.
     *
     * @param vehicle The vehicle to display
     */
    public void populateVehicleForm(Vehicle vehicle) {
        customerIdField.setText(String.valueOf(vehicle.getCustomerId()));
        licensePlateField.setText(vehicle.getLicensePlate());
        makeField.setText(vehicle.getMake());
        modelField.setText(vehicle.getModel());
        yearField.setText(String.valueOf(vehicle.getYear()));
        vinField.setText(vehicle.getVin());
        colorField.setText(vehicle.getColor());
        engineTypeField.setText(vehicle.getEngineType());
        transmissionField.setText(vehicle.getTransmission());
        mileageField.setText(String.valueOf(vehicle.getCurrentMileage()));

        if (vehicle.getLastServiceDate() != null) {
            lastServiceDateField.setText(DATE_FORMAT.format(vehicle.getLastServiceDate()));
        } else {
            lastServiceDateField.setText("");
        }

        notesArea.setText(vehicle.getNotes());
    }
}