package com.carmotorsproject.services.views;

import com.carmotorsproject.services.controller.ServiceController;
import com.carmotorsproject.services.model.*;
import com.carmotorsproject.parts.model.Part;

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
 * Swing UI for managing services.
 */
public class ServiceView extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(ServiceView.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Controller
    private final ServiceController controller;

    // UI Components
    private JTable serviceTable;
    private DefaultTableModel serviceTableModel;
    private JTable partsTable;
    private DefaultTableModel partsTableModel;

    // Service Form Components
    private JComboBox<Vehicle> vehicleComboBox;
    private JComboBox<Technician> technicianComboBox;
    private JComboBox<MaintenanceType> maintenanceTypeComboBox;
    private JComboBox<ServiceStatus> statusComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField descriptionField;
    private JTextArea diagnosisArea;
    private JTextField laborCostField;
    private JTextField mileageField;
    private JTextArea notesArea;

    // Part Form Components
    private JComboBox<Part> partComboBox;
    private JTextField quantityField;
    private JTextField unitPriceField;

    // Buttons
    private JButton addServiceButton;
    private JButton updateServiceButton;
    private JButton deleteServiceButton;
    private JButton clearServiceFormButton;
    private JButton addPartButton;
    private JButton removePartButton;
    private JButton generateReportButton;
    private JButton createDeliveryOrderButton;

    // Selected IDs
    private int selectedServiceId = 0;
    private int selectedPartInServiceId = 0;

    /**
     * Constructor that initializes the UI and controller.
     */
    public ServiceView() {
        this.controller = new ServiceController(this);
        initComponents();
        loadData();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        // Set up the frame
        setTitle("Service Management");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the service table panel
        JPanel serviceTablePanel = createServiceTablePanel();
        mainPanel.add(serviceTablePanel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Create the service form panel
        JPanel serviceFormPanel = createServiceFormPanel();
        formPanel.add(serviceFormPanel);

        // Create the parts panel
        JPanel partsPanel = createPartsPanel();
        formPanel.add(partsPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Initialize combo boxes
        initComboBoxes();
    }

    /**
     * Creates the service table panel.
     *
     * @return The service table panel
     */
    private JPanel createServiceTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Services"));

        // Create the table model
        serviceTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        serviceTableModel.addColumn("ID");
        serviceTableModel.addColumn("Vehicle");
        serviceTableModel.addColumn("Technician");
        serviceTableModel.addColumn("Type");
        serviceTableModel.addColumn("Status");
        serviceTableModel.addColumn("Start Date");
        serviceTableModel.addColumn("End Date");
        serviceTableModel.addColumn("Description");
        serviceTableModel.addColumn("Labor Cost");
        serviceTableModel.addColumn("Parts Cost");
        serviceTableModel.addColumn("Total Cost");

        // Create the table
        serviceTable = new JTable(serviceTableModel);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getTableHeader().setReorderingAllowed(false);

        // Add a mouse listener to the table
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = serviceTable.getSelectedRow();
                if (row >= 0) {
                    selectedServiceId = (int) serviceTableModel.getValueAt(row, 0);
                    controller.loadServiceDetails(selectedServiceId);
                    controller.loadPartsForService(selectedServiceId);
                    updateServiceButton.setEnabled(true);
                    deleteServiceButton.setEnabled(true);
                    addPartButton.setEnabled(true);
                    createDeliveryOrderButton.setEnabled(true);
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setPreferredSize(new Dimension(900, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the service form panel.
     *
     * @return The service form panel
     */
    private JPanel createServiceFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Service Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Vehicle
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Vehicle:"), gbc);

        gbc.gridx = 1;
        vehicleComboBox = new JComboBox<>();
        panel.add(vehicleComboBox, gbc);

        // Technician
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Technician:"), gbc);

        gbc.gridx = 1;
        technicianComboBox = new JComboBox<>();
        panel.add(technicianComboBox, gbc);

        // Maintenance Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Maintenance Type:"), gbc);

        gbc.gridx = 1;
        maintenanceTypeComboBox = new JComboBox<>(MaintenanceType.values());
        panel.add(maintenanceTypeComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(ServiceStatus.values());
        panel.add(statusComboBox, gbc);

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Start Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        startDateField = new JTextField(10);
        startDateField.setText(DATE_FORMAT.format(new Date())); // Default to current date
        panel.add(startDateField, gbc);

        // End Date
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("End Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        endDateField = new JTextField(10);
        panel.add(endDateField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        panel.add(descriptionField, gbc);

        // Diagnosis
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Diagnosis:"), gbc);

        gbc.gridx = 1;
        diagnosisArea = new JTextArea(3, 20);
        diagnosisArea.setLineWrap(true);
        JScrollPane diagnosisScrollPane = new JScrollPane(diagnosisArea);
        panel.add(diagnosisScrollPane, gbc);

        // Labor Cost
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Labor Cost:"), gbc);

        gbc.gridx = 1;
        laborCostField = new JTextField(10);
        panel.add(laborCostField, gbc);

        // Mileage
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(new JLabel("Mileage:"), gbc);

        gbc.gridx = 1;
        mileageField = new JTextField(10);
        panel.add(mileageField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        panel.add(notesScrollPane, gbc);

        return panel;
    }

    /**
     * Creates the parts panel.
     *
     * @return The parts panel
     */
    private JPanel createPartsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Parts"));

        // Create the parts table
        JPanel partsTablePanel = new JPanel(new BorderLayout(5, 5));

        // Create the table model
        partsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        partsTableModel.addColumn("ID");
        partsTableModel.addColumn("Part");
        partsTableModel.addColumn("Quantity");
        partsTableModel.addColumn("Unit Price");
        partsTableModel.addColumn("Total Price");

        // Create the table
        partsTable = new JTable(partsTableModel);
        partsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        partsTable.getTableHeader().setReorderingAllowed(false);

        // Add a mouse listener to the table
        partsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = partsTable.getSelectedRow();
                if (row >= 0) {
                    selectedPartInServiceId = (int) partsTableModel.getValueAt(row, 0);
                    removePartButton.setEnabled(true);
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(partsTable);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        partsTablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(partsTablePanel, BorderLayout.CENTER);

        // Create the parts form panel
        JPanel partsFormPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Part
        gbc.gridx = 0;
        gbc.gridy = 0;
        partsFormPanel.add(new JLabel("Part:"), gbc);

        gbc.gridx = 1;
        partComboBox = new JComboBox<>();
        partsFormPanel.add(partComboBox, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        partsFormPanel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        quantityField = new JTextField(5);
        partsFormPanel.add(quantityField, gbc);

        // Unit Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        partsFormPanel.add(new JLabel("Unit Price:"), gbc);

        gbc.gridx = 1;
        unitPriceField = new JTextField(10);
        partsFormPanel.add(unitPriceField, gbc);

        // Add and Remove Part buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel partButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        addPartButton = new JButton("Add Part");
        addPartButton.setEnabled(false);
        addPartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPart();
            }
        });
        partButtonPanel.add(addPartButton);

        removePartButton = new JButton("Remove Part");
        removePartButton.setEnabled(false);
        removePartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePart();
            }
        });
        partButtonPanel.add(removePartButton);

        partsFormPanel.add(partButtonPanel, gbc);

        panel.add(partsFormPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        addServiceButton = new JButton("Add Service");
        addServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addService();
            }
        });
        panel.add(addServiceButton);

        updateServiceButton = new JButton("Update Service");
        updateServiceButton.setEnabled(false);
        updateServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateService();
            }
        });
        panel.add(updateServiceButton);

        deleteServiceButton = new JButton("Delete Service");
        deleteServiceButton.setEnabled(false);
        deleteServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteService();
            }
        });
        panel.add(deleteServiceButton);

        clearServiceFormButton = new JButton("Clear Form");
        clearServiceFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearServiceForm();
            }
        });
        panel.add(clearServiceFormButton);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        panel.add(generateReportButton);

        createDeliveryOrderButton = new JButton("Create Delivery Order");
        createDeliveryOrderButton.setEnabled(false);
        createDeliveryOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDeliveryOrder();
            }
        });
        panel.add(createDeliveryOrderButton);

        return panel;
    }

    /**
     * Initializes the combo boxes with data.
     */
    private void initComboBoxes() {
        controller.loadVehicles();
        controller.loadTechnicians();
        controller.loadParts();
    }

    /**
     * Loads data into the tables.
     */
    public void loadData() {
        controller.loadServices();
    }

    /**
     * Adds a new service.
     */
    private void addService() {
        try {
            // Create a new Service object from form data
            Service service = getServiceFromForm();

            // Call the controller to add the service
            controller.addService(service);

            // Clear the form
            clearServiceForm();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding service", ex);
            JOptionPane.showMessageDialog(this, "Error adding service: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing service.
     */
    private void updateService() {
        try {
            // Create a Service object from form data
            Service service = getServiceFromForm();
            service.setServiceId(selectedServiceId);

            // Call the controller to update the service
            controller.updateService(service);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating service", ex);
            JOptionPane.showMessageDialog(this, "Error updating service: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a service.
     */
    private void deleteService() {
        try {
            // Confirm deletion
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this service?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Call the controller to delete the service
                controller.deleteService(selectedServiceId);

                // Clear the form
                clearServiceForm();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error deleting service", ex);
            JOptionPane.showMessageDialog(this, "Error deleting service: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a part to the service.
     */
    private void addPart() {
        try {
            // Get the selected part
            Part part = (Part) partComboBox.getSelectedItem();
            if (part == null) {
                JOptionPane.showMessageDialog(this, "Please select a part.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the quantity
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the unit price
            double unitPrice;
            try {
                unitPrice = Double.parseDouble(unitPriceField.getText().trim());
                if (unitPrice <= 0) {
                    JOptionPane.showMessageDialog(this, "Unit price must be greater than zero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid unit price.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a PartInService object
            PartInService partInService = new PartInService();
            partInService.setServiceId(selectedServiceId);
            partInService.setPartId(part.getId());
            partInService.setQuantity(quantity);
            partInService.setUnitPrice(unitPrice);
            partInService.setTotalPrice(quantity * unitPrice);

            // Call the controller to add the part
            controller.addPartToService(partInService);

            // Clear the part form
            quantityField.setText("");
            unitPriceField.setText("");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding part to service", ex);
            JOptionPane.showMessageDialog(this, "Error adding part to service: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Removes a part from the service.
     */
    private void removePart() {
        try {
            // Confirm removal
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this part?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Call the controller to remove the part
                controller.removePartFromService(selectedPartInServiceId);

                // Disable the remove button
                removePartButton.setEnabled(false);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error removing part from service", ex);
            JOptionPane.showMessageDialog(this, "Error removing part from service: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generates a service report.
     */
    private void generateReport() {
        // Open the service report view
        ServiceReportView reportView = new ServiceReportView();
        reportView.setVisible(true);
    }

    /**
     * Creates a delivery order for the selected service.
     */
    private void createDeliveryOrder() {
        try {
            // Check if the service is completed
            Service service = controller.getServiceById(selectedServiceId);
            if (service.getStatus() != ServiceStatus.COMPLETED) {
                JOptionPane.showMessageDialog(this,
                        "Service must be completed before creating a delivery order.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if a delivery order already exists
            if (controller.deliveryOrderExistsForService(selectedServiceId)) {
                JOptionPane.showMessageDialog(this,
                        "A delivery order already exists for this service.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Open the delivery order view
            DeliveryOrderView deliveryOrderView = new DeliveryOrderView(selectedServiceId);
            deliveryOrderView.setVisible(true);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error creating delivery order", ex);
            JOptionPane.showMessageDialog(this, "Error creating delivery order: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the service form.
     */
    private void clearServiceForm() {
        // Reset form fields
        vehicleComboBox.setSelectedIndex(-1);
        technicianComboBox.setSelectedIndex(-1);
        maintenanceTypeComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        startDateField.setText(DATE_FORMAT.format(new Date()));
        endDateField.setText("");
        descriptionField.setText("");
        diagnosisArea.setText("");
        laborCostField.setText("");
        mileageField.setText("");
        notesArea.setText("");

        // Clear the parts table
        partsTableModel.setRowCount(0);

        // Reset selected IDs
        selectedServiceId = 0;
        selectedPartInServiceId = 0;

        // Disable buttons
        updateServiceButton.setEnabled(false);
        deleteServiceButton.setEnabled(false);
        addPartButton.setEnabled(false);
        removePartButton.setEnabled(false);
        createDeliveryOrderButton.setEnabled(false);
    }

    /**
     * Creates a Service object from the form data.
     *
     * @return A Service object populated with form data
     * @throws ParseException If a date parsing error occurs
     */
    private Service getServiceFromForm() throws ParseException {
        Service service = new Service();

        // Get the selected vehicle
        Vehicle vehicle = (Vehicle) vehicleComboBox.getSelectedItem();
        if (vehicle != null) {
            service.setVehicleId(vehicle.getVehicleId());
        } else {
            throw new IllegalArgumentException("Please select a vehicle.");
        }

        // Get the selected technician
        Technician technician = (Technician) technicianComboBox.getSelectedItem();
        if (technician != null) {
            service.setTechnicianId(technician.getTechnicianId());
        } else {
            throw new IllegalArgumentException("Please select a technician.");
        }

        // Get the maintenance type
        MaintenanceType maintenanceType = (MaintenanceType) maintenanceTypeComboBox.getSelectedItem();
        service.setMaintenanceType(maintenanceType);

        // Get the status
        ServiceStatus status = (ServiceStatus) statusComboBox.getSelectedItem();
        service.setStatus(status);

        // Get the start date
        String startDateStr = startDateField.getText().trim();
        if (!startDateStr.isEmpty()) {
            service.setStartDate(DATE_FORMAT.parse(startDateStr));
        } else {
            throw new IllegalArgumentException("Please enter a start date.");
        }

        // Get the end date
        String endDateStr = endDateField.getText().trim();
        if (!endDateStr.isEmpty()) {
            service.setEndDate(DATE_FORMAT.parse(endDateStr));
        }

        // Get the description
        service.setDescription(descriptionField.getText().trim());

        // Get the diagnosis
        service.setDiagnosis(diagnosisArea.getText().trim());

        // Get the labor cost
        String laborCostStr = laborCostField.getText().trim();
        if (!laborCostStr.isEmpty()) {
            try {
                service.setLaborCost(Double.parseDouble(laborCostStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid labor cost.");
            }
        } else {
            service.setLaborCost(0.0);
        }

        // Get the mileage
        String mileageStr = mileageField.getText().trim();
        if (!mileageStr.isEmpty()) {
            try {
                service.setMileage(Integer.parseInt(mileageStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Please enter a valid mileage.");
            }
        } else {
            throw new IllegalArgumentException("Please enter the vehicle mileage.");
        }

        // Get the notes
        service.setNotes(notesArea.getText().trim());

        return service;
    }

    /**
     * Updates the service table with the provided services.
     *
     * @param services The list of services to display
     */
    public void updateServiceTable(List<Service> services) {
        // Clear the table
        serviceTableModel.setRowCount(0);

        // Add services to the table
        for (Service service : services) {
            Vector<Object> row = new Vector<>();
            row.add(service.getServiceId());
            row.add("ID: " + service.getVehicleId());
            row.add("ID: " + service.getTechnicianId());
            row.add(service.getMaintenanceType());
            row.add(service.getStatus());
            row.add(service.getStartDate() != null ? DATE_FORMAT.format(service.getStartDate()) : "");
            row.add(service.getEndDate() != null ? DATE_FORMAT.format(service.getEndDate()) : "");
            row.add(service.getDescription());
            row.add(service.getLaborCost());
            row.add(service.getPartsCost());
            row.add(service.getTotalCost());

            serviceTableModel.addRow(row);
        }
    }

    /**
     * Updates the parts table with the provided parts.
     *
     * @param parts The list of parts to display
     */
    public void updatePartsTable(List<PartInService> parts) {
        // Clear the table
        partsTableModel.setRowCount(0);

        // Add parts to the table
        for (PartInService part : parts) {
            Vector<Object> row = new Vector<>();
            row.add(part.getPartsInServiceId());
            row.add("ID: " + part.getPartId());
            row.add(part.getQuantity());
            row.add(part.getUnitPrice());
            row.add(part.getTotalPrice());

            partsTableModel.addRow(row);
        }
    }

    /**
     * Populates the service form with the provided service.
     *
     * @param service The service to display
     */
    public void populateServiceForm(Service service) {
        // Set the vehicle
        for (int i = 0; i < vehicleComboBox.getItemCount(); i++) {
            Vehicle vehicle = vehicleComboBox.getItemAt(i);
            if (vehicle.getVehicleId() == service.getVehicleId()) {
                vehicleComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set the technician
        for (int i = 0; i < technicianComboBox.getItemCount(); i++) {
            Technician technician = technicianComboBox.getItemAt(i);
            if (technician.getTechnicianId() == service.getTechnicianId()) {
                technicianComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Set the maintenance type
        maintenanceTypeComboBox.setSelectedItem(service.getMaintenanceType());

        // Set the status
        statusComboBox.setSelectedItem(service.getStatus());

        // Set the start date
        if (service.getStartDate() != null) {
            startDateField.setText(DATE_FORMAT.format(service.getStartDate()));
        } else {
            startDateField.setText("");
        }

        // Set the end date
        if (service.getEndDate() != null) {
            endDateField.setText(DATE_FORMAT.format(service.getEndDate()));
        } else {
            endDateField.setText("");
        }

        // Set the description
        descriptionField.setText(service.getDescription());

        // Set the diagnosis
        diagnosisArea.setText(service.getDiagnosis());

        // Set the labor cost
        laborCostField.setText(String.valueOf(service.getLaborCost()));

        // Set the mileage
        mileageField.setText(String.valueOf(service.getMileage()));

        // Set the notes
        notesArea.setText(service.getNotes());
    }

    /**
     * Updates the vehicle combo box with the provided vehicles.
     *
     * @param vehicles The list of vehicles to display
     */
    public void updateVehicleComboBox(List<Vehicle> vehicles) {
        vehicleComboBox.removeAllItems();
        for (Vehicle vehicle : vehicles) {
            vehicleComboBox.addItem(vehicle);
        }
    }

    /**
     * Updates the technician combo box with the provided technicians.
     *
     * @param technicians The list of technicians to display
     */
    public void updateTechnicianComboBox(List<Technician> technicians) {
        technicianComboBox.removeAllItems();
        for (Technician technician : technicians) {
            technicianComboBox.addItem(technician);
        }
    }

    /**
     * Updates the part combo box with the provided parts.
     *
     * @param parts The list of parts to display
     */
    public void updatePartComboBox(List<Part> parts) {
        partComboBox.removeAllItems();
        for (Part part : parts) {
            partComboBox.addItem(part);
        }
    }
}