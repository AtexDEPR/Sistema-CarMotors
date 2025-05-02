package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.services.model.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of InspectionDAOInterface
 */
public class InspectionDAO implements InspectionDAOInterface {
    
    // In-memory storage for inspections (would be replaced with database in production)
    private static final Map<String, Inspection> inspections = new HashMap<>();
    
    // In-memory storage for inspection items
    private static final Map<String, List<InspectionResult>> inspectionItems = new HashMap<>();
    
    @Override
    public boolean save(Inspection inspection, List<InspectionResult> items) {
        try {
            inspections.put(inspection.getId(), inspection);
            inspectionItems.put(inspection.getId(), items);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Inspection inspection) {
        try {
            if (!inspections.containsKey(inspection.getId())) {
                return false;
            }
            
            inspection.setLastModifiedDate(new Date());
            inspections.put(inspection.getId(), inspection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(String inspectionId) {
        try {
            if (!inspections.containsKey(inspectionId)) {
                return false;
            }
            
            inspections.remove(inspectionId);
            inspectionItems.remove(inspectionId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Inspection getById(String inspectionId) {
        return inspections.get(inspectionId);
    }
    
    @Override
    public List<Inspection> getAll() {
        return new ArrayList<>(inspections.values());
    }
    
    @Override
    public List<Inspection> getByVehicle(String vehicleId) {
        return inspections.values().stream()
                .filter(i -> i.getVehicleId().equals(vehicleId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Inspection> getByCustomer(String customerId) {
        return inspections.values().stream()
                .filter(i -> i.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Inspection> getByDateRange(Date startDate, Date endDate) {
        return inspections.values().stream()
                .filter(i -> (i.getInspectionDate().after(startDate) || i.getInspectionDate().equals(startDate)) && 
                       (i.getInspectionDate().before(endDate) || i.getInspectionDate().equals(endDate)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Inspection> getByCondition(String condition) {
        return inspections.values().stream()
                .filter(i -> i.getOverallCondition().equals(condition))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<InspectionResult> getInspectionItems(String inspectionId) {
        return inspectionItems.getOrDefault(inspectionId, new ArrayList<>());
    }
    
    @Override
    public List<String> getAllConditions() {
        return Arrays.asList("Excelente", "Bueno", "Regular", "Deficiente");
    }
    
    @Override
    public List<String> getAllItemCategories() {
        return Arrays.asList("Motor", "Transmisión", "Frenos", "Suspensión", "Carrocería", "Interior", "Eléctrico");
    }
    
    @Override
    public List<InspectionResult> getDefaultItems(String vehicleType) {
        List<InspectionResult> defaultItems = new ArrayList<>();
        
        // Common items for all vehicle types
        defaultItems.add(new InspectionResult(null, "Aceite de motor", "Motor", 0, ""));
        defaultItems.add(new InspectionResult(null, "Filtro de aire", "Motor", 0, ""));
        defaultItems.add(new InspectionResult(null, "Líquido de frenos", "Frenos", 0, ""));
        defaultItems.add(new InspectionResult(null, "Pastillas de freno", "Frenos", 0, ""));
        defaultItems.add(new InspectionResult(null, "Discos de freno", "Frenos", 0, ""));
        defaultItems.add(new InspectionResult(null, "Amortiguadores", "Suspensión", 0, ""));
        defaultItems.add(new InspectionResult(null, "Batería", "Eléctrico", 0, ""));
        defaultItems.add(new InspectionResult(null, "Luces", "Eléctrico", 0, ""));
        
        // Add specific items based on vehicle type
        if ("Sedan".equals(vehicleType) || "Hatchback".equals(vehicleType)) {
            defaultItems.add(new InspectionResult(null, "Dirección asistida", "Suspensión", 0, ""));
        } else if ("SUV".equals(vehicleType) || "Pickup".equals(vehicleType)) {
            defaultItems.add(new InspectionResult(null, "Tracción 4x4", "Transmisión", 0, ""));
            defaultItems.add(new InspectionResult(null, "Suspensión elevada", "Suspensión", 0, ""));
        }
        
        return defaultItems;
    }
    
    @Override
    public boolean generateReport(Inspection inspection, List<InspectionResult> items, 
                                 Vehicle vehicle, Customer customer) {
        // In a real implementation, this would generate a PDF report
        // For this example, we'll just return true
        return true;
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Total inspections
        statistics.put("totalInspections", inspections.size());
        
        // Inspections by condition
        Map<String, Long> inspectionsByCondition = inspections.values().stream()
                .collect(Collectors.groupingBy(Inspection::getOverallCondition, Collectors.counting()));
        statistics.put("inspectionsByCondition", inspectionsByCondition);
        
        // Recent inspections (last 30 days)
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
        long recentInspections = inspections.values().stream()
                .filter(i -> i.getInspectionDate().after(thirtyDaysAgo))
                .count();
        statistics.put("recentInspections", recentInspections);
        
        // Items requiring attention
        int itemsRequiringAttention = 0;
        for (List<InspectionResult> items : inspectionItems.values()) {
            itemsRequiringAttention += items.stream()
                    .filter(InspectionResult::isRequiresAttention)
                    .count();
        }
        statistics.put("itemsRequiringAttention", itemsRequiringAttention);
        
        return statistics;
    }
}