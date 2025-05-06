/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carmotorsproject.services.views;

import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.services.controller.ServiceController;
import com.carmotorsproject.services.model.PartsInService;
import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.services.model.Technician;
import com.carmotorsproject.services.model.Vehicle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class ServiceView extends JFrame {
    private ServiceController controller;
    private JComboBox<String> inputMaintenanceType;
    private JComboBox<String> inputVehicle;
    private JTextField inputMileage;
    private JTextArea inputDescription;
    private JTextArea inputInitialDiagnosis;
    private JTextArea inputFinalObservations;
    private JTextField inputEstimatedTime;
    private JTextField inputLaborCost;
    private JComboBox<String> inputStatus;
    private JDateChooser inputStartDate;
    private JDateChooser inputEndDate;
    private JDateChooser inputWarrantyUntil;
    private JList<Integer> inputTechnicianIds;
    private JList<PartsInService> inputPartsInService;
    private JTextField inputSearch;
    private JTable tableServices;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ServiceView() {
        this.controller = new ServiceController(this);
        initComponents();
        populateCombos();
        refreshTable();
    }

    private void initComponents() {
        setTitle("Service Management - Engines and Wheels");
        setSize(1300, 1000); // Aumentamos aún más el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Búsqueda y tabla
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = new JLabel("Search by ID:");
        inputSearch = new JTextField(20);
        inputSearch.setMinimumSize(new Dimension(200, 25));
        inputSearch.setPreferredSize(new Dimension(300, 25));
        JButton btnSearch = new JButton("SEARCH");
        searchPanel.add(lblSearch);
        searchPanel.add(inputSearch);
        searchPanel.add(btnSearch);

        // Tabla de servicios
        tableServices = new JTable();
        tableServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableServices.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableServices.getSelectedRow();
                if (selectedRow >= 0) {
                    loadServiceForEdit(selectedRow);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(tableServices);
    tableScrollPane.setMinimumSize(new Dimension(400, 200)); 
    tableScrollPane.setPreferredSize(new Dimension(400, 200)); 
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel central: Formulario con tabuladores
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña 1: Datos del Servicio
        JPanel serviceTab = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permitir expansión horizontal
        gbc.weightx = 1.0; // Dar más peso horizontal a los componentes

        gbc.gridx = 0; gbc.gridy = 0;
        serviceTab.add(new JLabel("Type of Maintenance:"), gbc);
        gbc.gridx = 1;
        inputMaintenanceType = new JComboBox<>(new String[]{"Preventive", "Corrective"});
        inputMaintenanceType.setMinimumSize(new Dimension(300, 25));
        inputMaintenanceType.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputMaintenanceType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        serviceTab.add(new JLabel("Vehicle:"), gbc);
        gbc.gridx = 1;
        inputVehicle = new JComboBox<>();
        inputVehicle.setMinimumSize(new Dimension(300, 25));
        inputVehicle.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputVehicle, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        serviceTab.add(new JLabel("Mileage:"), gbc);
        gbc.gridx = 1;
        inputMileage = new JTextField(20);
        inputMileage.setMinimumSize(new Dimension(300, 25));
        inputMileage.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputMileage, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        serviceTab.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputDescription = new JTextArea(5, 40);
        inputDescription.setLineWrap(true);
        inputDescription.setWrapStyleWord(true);
        inputDescription.setMinimumSize(new Dimension(400, 100));
        inputDescription.setPreferredSize(new Dimension(500, 120));
        serviceTab.add(new JScrollPane(inputDescription), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        serviceTab.add(new JLabel("Initial Diagnosis:"), gbc);
        gbc.gridx = 1;
        inputInitialDiagnosis = new JTextArea(5, 40);
        inputInitialDiagnosis.setLineWrap(true);
        inputInitialDiagnosis.setWrapStyleWord(true);
        inputInitialDiagnosis.setMinimumSize(new Dimension(400, 100));
        inputInitialDiagnosis.setPreferredSize(new Dimension(500, 120));
        serviceTab.add(new JScrollPane(inputInitialDiagnosis), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        serviceTab.add(new JLabel("Final Remarks:"), gbc);
        gbc.gridx = 1;
        inputFinalObservations = new JTextArea(5, 40);
        inputFinalObservations.setLineWrap(true);
        inputFinalObservations.setWrapStyleWord(true);
        inputFinalObservations.setMinimumSize(new Dimension(400, 100));
        inputFinalObservations.setPreferredSize(new Dimension(500, 120));
        serviceTab.add(new JScrollPane(inputFinalObservations), gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        serviceTab.add(new JLabel("Estimated Time (hours):"), gbc);
        gbc.gridx = 1;
        inputEstimatedTime = new JTextField(20);
        inputEstimatedTime.setMinimumSize(new Dimension(300, 25));
        inputEstimatedTime.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputEstimatedTime, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        serviceTab.add(new JLabel("Labor Cost:"), gbc);
        gbc.gridx = 1;
        inputLaborCost = new JTextField(20);
        inputLaborCost.setMinimumSize(new Dimension(300, 25));
        inputLaborCost.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputLaborCost, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        serviceTab.add(new JLabel("State:"), gbc);
        gbc.gridx = 1;
        inputStatus = new JComboBox<>(new String[]{"Pending", "In_progress", "Completed", "Delivered"});
        inputStatus.setMinimumSize(new Dimension(300, 25));
        inputStatus.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        serviceTab.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        inputStartDate = new JDateChooser();
        inputStartDate.setMinimumSize(new Dimension(300, 25));
        inputStartDate.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputStartDate, gbc);

        gbc.gridx = 0; gbc.gridy = 10;
        serviceTab.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        inputEndDate = new JDateChooser();
        inputEndDate.setMinimumSize(new Dimension(300, 25));
        inputEndDate.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputEndDate, gbc);

        gbc.gridx = 0; gbc.gridy = 11;
        serviceTab.add(new JLabel("Warranty Up To:"), gbc);
        gbc.gridx = 1;
        inputWarrantyUntil = new JDateChooser();
        inputWarrantyUntil.setMinimumSize(new Dimension(300, 25));
        inputWarrantyUntil.setPreferredSize(new Dimension(400, 25));
        serviceTab.add(inputWarrantyUntil, gbc);

        tabbedPane.addTab("Service Data", serviceTab);

        // Pestaña 2: Asignación de Técnicos
        JPanel techniciansTab = new JPanel(new BorderLayout());
        JLabel lblTechnicianIds = new JLabel("Assigned Technicians:");
        inputTechnicianIds = new JList<>(new DefaultListModel<>());
        inputTechnicianIds.setVisibleRowCount(10);
        inputTechnicianIds.setMinimumSize(new Dimension(400, 200));
        inputTechnicianIds.setPreferredSize(new Dimension(500, 250));
        techniciansTab.add(lblTechnicianIds, BorderLayout.NORTH);
        techniciansTab.add(new JScrollPane(inputTechnicianIds), BorderLayout.CENTER);
        tabbedPane.addTab("Technicians", techniciansTab);

        // Pestaña 3: Repuestos Usados
        JPanel partsTab = new JPanel(new BorderLayout());
        JLabel lblPartsInService = new JLabel("Used Spare Parts:");
        inputPartsInService = new JList<>(new DefaultListModel<>());
        inputPartsInService.setVisibleRowCount(10);
        inputPartsInService.setMinimumSize(new Dimension(400, 200));
        inputPartsInService.setPreferredSize(new Dimension(500, 250));
        partsTab.add(lblPartsInService, BorderLayout.NORTH);
        partsTab.add(new JScrollPane(inputPartsInService), BorderLayout.CENTER);
        tabbedPane.addTab("Spare parts", partsTab);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Register Service");
        JButton btnUpdate = new JButton("Update Service");
        JButton btnDelete = new JButton("Delete Service");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> addService());
        btnUpdate.addActionListener(e -> updateService());
        btnDelete.addActionListener(e -> deleteService());
        btnSearch.addActionListener(e -> searchService());
    }

    private void populateCombos() {
        inputVehicle.removeAllItems();
        inputVehicle.addItem("Select a vehicle");
        List<Vehicle> vehicles = controller.getAllVehicles();
        System.out.println("Number of vehicles recovered: " + vehicles.size());
        for (Vehicle vehicle : vehicles) {
            inputVehicle.addItem(vehicle.getVehicleId() + " - " + vehicle.getLicensePlate());
        }

        DefaultListModel<Integer> technicianModel = new DefaultListModel<>();
        List<Technician> technicians = controller.getAllTechnicians();
        System.out.println("Number of technicians recovered: " + technicians.size());
        for (Technician technician : technicians) {
            technicianModel.addElement(technician.getTechnicianId());
        }
        inputTechnicianIds.setModel(technicianModel);

        DefaultListModel<PartsInService> partsModel = new DefaultListModel<>();
        // Descomentar cuando PartDAO esté implementado
        /*
        List<Part> parts = controller.getAllParts();
        System.out.println("Número de repuestos recuperados: " + parts.size());
        for (Part part : parts) {
            partsModel.addElement(new PartsInService(0, 0, part.getPartId(), 1, part.getUnitPrice()));
        }
        */
        inputPartsInService.setModel(partsModel);
    }

    private void addService() {
        String maintenanceType = (String) inputMaintenanceType.getSelectedItem();
        int vehicleId = getSelectedVehicleId();
        Integer mileage = inputMileage.getText().isEmpty() ? null : Integer.parseInt(inputMileage.getText());
        String description = inputDescription.getText().trim();
        String initialDiagnosis = inputInitialDiagnosis.getText().trim();
        Double estimatedTime = inputEstimatedTime.getText().isEmpty() ? null : Double.parseDouble(inputEstimatedTime.getText());
        Double laborCost = inputLaborCost.getText().isEmpty() ? null : Double.parseDouble(inputLaborCost.getText());
        String status = (String) inputStatus.getSelectedItem();
        Date startDate = inputStartDate.getDate();
        Date endDate = inputEndDate.getDate();
        Date warrantyUntil = inputWarrantyUntil.getDate();
        List<Integer> technicianIds = inputTechnicianIds.getSelectedValuesList();
        List<PartsInService> partsInService = inputPartsInService.getSelectedValuesList();

        if (vehicleId == 0 || maintenanceType == null || status == null) {
            showAlert("Please select a vehicle, maintenance type, and condition.");
            return;
        }

        controller.addService(maintenanceType, vehicleId, mileage, description, initialDiagnosis, 
                estimatedTime, laborCost, status, startDate, endDate, warrantyUntil, technicianIds, partsInService);
        clearFields();
    }

    private void updateService() {
        int selectedRow = tableServices.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select a service to update.");
            return;
        }

        int serviceId = (int) tableServices.getValueAt(selectedRow, 0);
        String maintenanceType = (String) inputMaintenanceType.getSelectedItem();
        int vehicleId = getSelectedVehicleId();
        Integer mileage = inputMileage.getText().isEmpty() ? null : Integer.parseInt(inputMileage.getText());
        String description = inputDescription.getText().trim();
        String initialDiagnosis = inputInitialDiagnosis.getText().trim();
        String finalObservations = inputFinalObservations.getText().trim();
        Double estimatedTime = inputEstimatedTime.getText().isEmpty() ? null : Double.parseDouble(inputEstimatedTime.getText());
        Double laborCost = inputLaborCost.getText().isEmpty() ? null : Double.parseDouble(inputLaborCost.getText());
        String status = (String) inputStatus.getSelectedItem();
        Date startDate = inputStartDate.getDate();
        Date endDate = inputEndDate.getDate();
        Date warrantyUntil = inputWarrantyUntil.getDate();
        List<Integer> technicianIds = inputTechnicianIds.getSelectedValuesList();
        List<PartsInService> partsInService = inputPartsInService.getSelectedValuesList();

        controller.updateService(serviceId, maintenanceType, vehicleId, mileage, description, initialDiagnosis, 
                finalObservations, estimatedTime, laborCost, status, startDate, endDate, warrantyUntil, technicianIds, partsInService);
        clearFields();
    }

    private void deleteService() {
        int selectedRow = tableServices.getSelectedRow();
        if (selectedRow < 0) {
            showAlert("Please select a service to remove.");
            return;
        }

        int serviceId = (int) tableServices.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Are you sure you want to remove this service?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteService(serviceId);
            clearFields();
        }
    }

    private void searchService() {
        String searchTerm = inputSearch.getText().trim();
        List<Service> services;
        if (searchTerm.isEmpty()) {
            services = controller.getAllServices();
        } else {
            try {
                int id = Integer.parseInt(searchTerm);
                Service service = controller.getServiceById(id);
                services = service != null ? List.of(service) : new ArrayList<>();
            } catch (NumberFormatException e) {
                services = new ArrayList<>();
            }
        }
        updateTable(services);
    }

    private void loadServiceForEdit(int row) {
        int serviceId = (int) tableServices.getValueAt(row, 0);
        Service service = controller.getServiceById(serviceId);
        if (service != null) {
            inputMaintenanceType.setSelectedItem(service.getMaintenanceType());
            setVehicleSelection(service.getVehicleId());
            inputMileage.setText(service.getMileage() != null ? service.getMileage().toString() : "");
            inputDescription.setText(service.getDescription() != null ? service.getDescription() : "");
            inputInitialDiagnosis.setText(service.getInitialDiagnosis() != null ? service.getInitialDiagnosis() : "");
            inputFinalObservations.setText(service.getFinalObservations() != null ? service.getFinalObservations() : "");
            inputEstimatedTime.setText(service.getEstimatedTime() != null ? service.getEstimatedTime().toString() : "");
            inputLaborCost.setText(service.getLaborCost() != null ? service.getLaborCost().toString() : "");
            inputStatus.setSelectedItem(service.getStatus());
            inputStartDate.setDate(service.getStartDate());
            inputEndDate.setDate(service.getEndDate());
            inputWarrantyUntil.setDate(service.getWarrantyUntil());

            DefaultListModel<Integer> technicianModel = new DefaultListModel<>();
            technicianModel.addAll(service.getTechnicianIds());
            inputTechnicianIds.setModel(technicianModel);

            DefaultListModel<PartsInService> partsModel = new DefaultListModel<>();
            partsModel.addAll(service.getPartsInService());
            inputPartsInService.setModel(partsModel);
        }
    }

    private int getSelectedVehicleId() {
        String selected = (String) inputVehicle.getSelectedItem();
        if (selected == null || selected.equals("Select a vehicle")) return 0;
        try {
            return Integer.parseInt(selected.split(" - ")[0]);
        } catch (NumberFormatException e) {
            showAlert("Error parsing vehicle ID: " + e.getMessage());
            return 0;
        }
    }

    private void setVehicleSelection(int vehicleId) {
        for (int i = 0; i < inputVehicle.getItemCount(); i++) {
            String item = inputVehicle.getItemAt(i);
            if (item.startsWith(vehicleId + " - ")) {
                inputVehicle.setSelectedItem(item);
                return;
            }
        }
        inputVehicle.setSelectedItem("Select a vehicle");
    }

    public void refreshTable() {
        List<Service> services = controller.getAllServices();
        System.out.println("Refreshing table with" + services.size() + " services.");
        updateTable(services);
        if (services.isEmpty()) {
            showAlert("No services were found in the database.");
        }
    }

    private void updateTable(List<Service> services) {
        String[] columns = {"ID", "Type", "Vehicle", "Mileage", "Status", "Start Date", "End Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Service service : services) {
            Vehicle vehicle = controller.getAllVehicles().stream()
                    .filter(v -> v.getVehicleId() == service.getVehicleId())
                    .findFirst().orElse(null);
            String vehicleInfo = vehicle != null ? vehicle.getLicensePlate() : "unknown";
            model.addRow(new Object[]{
                service.getServiceId(),
                service.getMaintenanceType(),
                vehicleInfo,
                service.getMileage() != null ? service.getMileage() : "",
                service.getStatus(),
                service.getStartDate() != null ? dateFormat.format(service.getStartDate()) : "",
                service.getEndDate() != null ? dateFormat.format(service.getEndDate()) : ""
            });
        }
        tableServices.setModel(model);
        System.out.println("Table updated with " + model.getRowCount() + " rows.");
    }

    private void clearFields() {
        inputMaintenanceType.setSelectedIndex(0);
        inputVehicle.setSelectedIndex(0);
        inputMileage.setText("");
        inputDescription.setText("");
        inputInitialDiagnosis.setText("");
        inputFinalObservations.setText("");
        inputEstimatedTime.setText("");
        inputLaborCost.setText("");
        inputStatus.setSelectedIndex(0);
        inputStartDate.setDate(null);
        inputEndDate.setDate(null);
        inputWarrantyUntil.setDate(null);
        ((DefaultListModel<Integer>) inputTechnicianIds.getModel()).clear();
        ((DefaultListModel<PartsInService>) inputPartsInService.getModel()).clear();
        inputSearch.setText("");
        tableServices.clearSelection();
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}