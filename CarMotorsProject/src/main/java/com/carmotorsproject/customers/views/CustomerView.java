package com.carmotorsproject.customers.views;

import com.carmotorsproject.customers.controller.CustomerController;
import com.carmotorsproject.customers.model.Customer;

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

/**
 * Swing UI for managing customers.
 * Provides a graphical interface for viewing, adding, updating, and deleting customers.
 */
public class CustomerView extends JFrame {

    // Controller
    private CustomerController controller;

    // UI Components
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtIdentification;
    private JComboBox<String> cmbIdentificationType;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextField txtCity;
    private JTextField txtState;
    private JTextField txtZipCode;
    private JTextField txtCountry;
    private JCheckBox chkActive;
    private JTextArea txtNotes;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;

    // Selected customer ID
    private int selectedCustomerId = 0;

    // Date formatter
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor that initializes the view.
     */
    public CustomerView() {
        initComponents();
        controller = new CustomerController(this);
        loadCustomers();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        setTitle("Customer Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = createFormPanel();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Search panel
        JPanel searchPanel = createSearchPanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

        // Add panels to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        addActionListeners();
    }

    /**
     * Creates the form panel.
     *
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        formPanel.setPreferredSize(new Dimension(400, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        txtFirstName = new JTextField(20);
        formPanel.add(txtFirstName, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        txtLastName = new JTextField(20);
        formPanel.add(txtLastName, gbc);

        // Identification
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Identification:"), gbc);

        gbc.gridx = 1;
        txtIdentification = new JTextField(20);
        formPanel.add(txtIdentification, gbc);

        // Identification Type
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("ID Type:"), gbc);

        gbc.gridx = 1;
        cmbIdentificationType = new JComboBox<>(new String[]{"ID Card", "Passport", "Driver's License", "Other"});
        formPanel.add(cmbIdentificationType, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        txtAddress = new JTextField(20);
        formPanel.add(txtAddress, gbc);

        // City
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("City:"), gbc);

        gbc.gridx = 1;
        txtCity = new JTextField(20);
        formPanel.add(txtCity, gbc);

        // State
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("State:"), gbc);

        gbc.gridx = 1;
        txtState = new JTextField(20);
        formPanel.add(txtState, gbc);

        // ZIP Code
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("ZIP Code:"), gbc);

        gbc.gridx = 1;
        txtZipCode = new JTextField(20);
        formPanel.add(txtZipCode, gbc);

        // Country
        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("Country:"), gbc);

        gbc.gridx = 1;
        txtCountry = new JTextField(20);
        formPanel.add(txtCountry, gbc);

        // Active
        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("Active:"), gbc);

        gbc.gridx = 1;
        chkActive = new JCheckBox();
        chkActive.setSelected(true);
        formPanel.add(chkActive, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 12;
        formPanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridheight = 2;
        txtNotes = new JTextArea(4, 20);
        JScrollPane notesScrollPane = new JScrollPane(txtNotes);
        formPanel.add(notesScrollPane, gbc);

        return formPanel;
    }

    /**
     * Creates the table panel.
     *
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Customer List"));

        // Create table model
        String[] columnNames = {"ID", "Name", "Identification", "Email", "Phone", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the search panel.
     *
     * @return The search panel
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));

        // Search type combo box
        cmbSearchType = new JComboBox<>(new String[]{"Name", "Identification", "Email", "Phone"});
        searchPanel.add(cmbSearchType);

        // Search text field
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        // Search button
        btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);

        // Reset button
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadCustomers();
        });
        searchPanel.add(btnReset);

        return searchPanel;
    }

    /**
     * Creates the button panel.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Add button
        btnAdd = new JButton("Add");
        buttonPanel.add(btnAdd);

        // Update button
        btnUpdate = new JButton("Update");
        buttonPanel.add(btnUpdate);

        // Delete button
        btnDelete = new JButton("Delete");
        buttonPanel.add(btnDelete);

        // Clear button
        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);

        return buttonPanel;
    }

    /**
     * Adds action listeners to the buttons and table.
     */
    private void addActionListeners() {
        // Add button
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        // Update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });

        // Delete button
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        // Clear button
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Search button
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });

        // Table row selection
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row >= 0) {
                    selectedCustomerId = (int) customerTable.getValueAt(row, 0);
                    controller.loadCustomerDetails(selectedCustomerId);
                }
            }
        });
    }

    /**
     * Loads all customers and updates the table.
     */
    public void loadCustomers() {
        controller.loadCustomers();
    }

    /**
     * Updates the customer table with the provided list of customers.
     *
     * @param customers The list of customers to display
     */
    public void updateCustomerTable(List<Customer> customers) {
        // Clear the table
        tableModel.setRowCount(0);

        // Add customers to the table
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getIdentification(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.isActive()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Populates the form with the details of a customer.
     *
     * @param customer The customer to display
     */
    public void populateCustomerForm(Customer customer) {
        selectedCustomerId = customer.getCustomerId();
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtIdentification.setText(customer.getIdentification());
        cmbIdentificationType.setSelectedItem(customer.getIdentificationType());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtState.setText(customer.getState());
        txtZipCode.setText(customer.getZipCode());
        txtCountry.setText(customer.getCountry());
        chkActive.setSelected(customer.isActive());
        txtNotes.setText(customer.getNotes());
    }

    /**
     * Clears the form.
     */
    public void clearForm() {
        selectedCustomerId = 0;
        txtFirstName.setText("");
        txtLastName.setText("");
        txtIdentification.setText("");
        cmbIdentificationType.setSelectedIndex(0);
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtZipCode.setText("");
        txtCountry.setText("");
        chkActive.setSelected(true);
        txtNotes.setText("");
        customerTable.clearSelection();
    }

    /**
     * Adds a new customer.
     */
    private void addCustomer() {
        try {
            // Create a new customer from form data
            Customer customer = getCustomerFromForm();

            // Add the customer
            controller.addCustomer(customer);

            // Clear the form
            clearForm();

            // Show success message
            JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing customer.
     */
    private void updateCustomer() {
        if (selectedCustomerId == 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a customer from form data
            Customer customer = getCustomerFromForm();
            customer.setCustomerId(selectedCustomerId);

            // Update the customer
            controller.updateCustomer(customer);

            // Clear the form
            clearForm();

            // Show success message
            JOptionPane.showMessageDialog(this, "Customer updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a customer.
     */
    private void deleteCustomer() {
        if (selectedCustomerId == 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Delete the customer
            controller.deleteCustomer(selectedCustomerId);

            // Clear the form
            clearForm();

            // Show success message
            JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for customers based on the search criteria.
     */
    private void searchCustomers() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadCustomers();
            return;
        }

        String searchType = (String) cmbSearchType.getSelectedItem();

        try {
            switch (searchType) {
                case "Name":
                    controller.searchCustomersByName(searchText);
                    break;
                case "Identification":
                    controller.searchCustomersByIdentification(searchText);
                    break;
                case "Email":
                    controller.searchCustomersByEmail(searchText);
                    break;
                case "Phone":
                    controller.searchCustomersByPhone(searchText);
                    break;
                default:
                    loadCustomers();
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching customers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a Customer object from the form data.
     *
     * @return A Customer object with data from the form
     */
    private Customer getCustomerFromForm() {
        Customer customer = new Customer();

        // Set customer properties from form fields
        customer.setFirstName(txtFirstName.getText().trim());
        customer.setLastName(txtLastName.getText().trim());
        customer.setIdentification(txtIdentification.getText().trim());
        customer.setIdentificationType((String) cmbIdentificationType.getSelectedItem());
        customer.setEmail(txtEmail.getText().trim());
        customer.setPhone(txtPhone.getText().trim());
        customer.setAddress(txtAddress.getText().trim());
        customer.setCity(txtCity.getText().trim());
        customer.setState(txtState.getText().trim());
        customer.setZipCode(txtZipCode.getText().trim());
        customer.setCountry(txtCountry.getText().trim());
        customer.setActive(chkActive.isSelected());
        customer.setNotes(txtNotes.getText().trim());

        // Set registration date to current date for new customers
        if (selectedCustomerId == 0) {
            customer.setRegistrationDate(new Date());
        }

        return customer;
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to show
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an information message.
     *
     * @param message The information message to show
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method for testing the view.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomerView().setVisible(true);
            }
        });
    }
}