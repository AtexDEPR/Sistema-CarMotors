package com.carmotorsproject.campaigns.model;

import com.carmotorsproject.customers.model.Customer;
import com.carmotorsproject.customers.model.CustomerDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of CampaignAppointmentDAOInterface
 */
public class CampaignAppointmentDAO implements CampaignAppointmentDAOInterface {
    
    // In-memory storage for appointments (would be replaced with database in production)
    private static final Map<String, CampaignAppointment> appointments = new HashMap<>();
    
    // Reference to customer DAO
    private CustomerDAO customerDAO;
    
    public CampaignAppointmentDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
    
    @Override
    public boolean save(CampaignAppointment appointment) {
        try {
            appointments.put(appointment.getId(), appointment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(CampaignAppointment appointment) {
        try {
            if (!appointments.containsKey(appointment.getId())) {
                return false;
            }
            
            appointment.setLastModifiedDate(new Date());
            appointments.put(appointment.getId(), appointment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(String appointmentId) {
        try {
            if (!appointments.containsKey(appointmentId)) {
                return false;
            }
            
            appointments.remove(appointmentId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CampaignAppointment getById(String appointmentId) {
        return appointments.get(appointmentId);
    }
    
    @Override
    public List<CampaignAppointment> getAll() {
        return new ArrayList<>(appointments.values());
    }
    
    @Override
    public List<CampaignAppointment> getByCampaign(String campaignId) {
        return appointments.values().stream()
                .filter(a -> a.getCampaignId().equals(campaignId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CampaignAppointment> getByCustomer(String customerId) {
        return appointments.values().stream()
                .filter(a -> a.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CampaignAppointment> getByStatus(String status) {
        return appointments.values().stream()
                .filter(a -> a.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CampaignAppointment> getByDateRange(Date startDate, Date endDate) {
        return appointments.values().stream()
                .filter(a -> (a.getAppointmentDate().after(startDate) || a.getAppointmentDate().equals(startDate)) && 
                       (a.getAppointmentDate().before(endDate) || a.getAppointmentDate().equals(endDate)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllStatuses() {
        return Arrays.asList("Programada", "En proceso", "Completada", "Cancelada", "Reprogramada");
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Total appointments
        statistics.put("totalAppointments", appointments.size());
        
        // Appointments by status
        Map<String, Long> appointmentsByStatus = appointments.values().stream()
                .collect(Collectors.groupingBy(CampaignAppointment::getStatus, Collectors.counting()));
        statistics.put("appointmentsByStatus", appointmentsByStatus);
        
        // Appointments by campaign
        Map<String, Long> appointmentsByCampaign = appointments.values().stream()
                .collect(Collectors.groupingBy(CampaignAppointment::getCampaignId, Collectors.counting()));
        statistics.put("appointmentsByCampaign", appointmentsByCampaign);
        
        // Upcoming appointments
        Date now = new Date();
        long upcomingAppointments = appointments.values().stream()
                .filter(a -> a.getAppointmentDate().after(now) && !a.getStatus().equals("Cancelada"))
                .count();
        statistics.put("upcomingAppointments", upcomingAppointments);
        
        // Overdue appointments
        long overdueAppointments = appointments.values().stream()
                .filter(a -> a.getAppointmentDate().before(now) && 
                       !a.getStatus().equals("Completada") && 
                       !a.getStatus().equals("Cancelada"))
                .count();
        statistics.put("overdueAppointments", overdueAppointments);
        
        return statistics;
    }
    
    @Override
    public List<Customer> getCustomersForCampaign(String campaignId) {
        List<String> customerIds = appointments.values().stream()
                .filter(a -> a.getCampaignId().equals(campaignId))
                .map(CampaignAppointment::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        
        List<Customer> customers = new ArrayList<>();
        for (String customerId : customerIds) {
            Customer customer = customerDAO.getById(customerId);
            if (customer != null) {
                customers.add(customer);
            }
        }
        
        return customers;
    }
    
    @Override
    public List<Object[]> getUsageStatistics() {
        List<Object[]> statistics = new ArrayList<>();
        
        // Group by campaign and count
        Map<String, Long> campaignCounts = appointments.values().stream()
                .collect(Collectors.groupingBy(CampaignAppointment::getCampaignId, Collectors.counting()));
        
        for (Map.Entry<String, Long> entry : campaignCounts.entrySet()) {
            Object[] row = new Object[2];
            row[0] = entry.getKey();
            row[1] = entry.getValue();
            statistics.add(row);
        }
        
        return statistics;
    }
}