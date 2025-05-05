/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.services.model;

import com.carmotorsproject.services.model.Service;
import com.carmotorsproject.services.model.ServiceStatus;
import com.carmotorsproject.services.model.PartInService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for service data access operations.
 * Defines methods for CRUD operations on services and related entities.
 */
public interface ServiceDAOInterface {

    /**
     * Saves a new service to the database.
     *
     * @param service The service to save
     * @return The saved service with its generated ID
     * @throws SQLException If a database access error occurs
     */
    Service save(Service service) throws SQLException;

    /**
     * Updates an existing service in the database.
     *
     * @param service The service to update
     * @return The updated service
     * @throws SQLException If a database access error occurs
     */
    Service update(Service service) throws SQLException;

    /**
     * Deletes a service from the database.
     *
     * @param id The ID of the service to delete
     * @return true if the service was deleted, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean delete(int id) throws SQLException;

    /**
     * Finds a service by its ID.
     *
     * @param id The ID of the service to find
     * @return The service, or null if not found
     * @throws SQLException If a database access error occurs
     */
    Service findById(int id) throws SQLException;

    /**
     * Finds all services in the database.
     *
     * @return A list of all services
     * @throws SQLException If a database access error occurs
     */
    List<Service> findAll() throws SQLException;

    /**
     * Finds services for a specific vehicle.
     *
     * @param vehicleId The ID of the vehicle
     * @return A list of services for the vehicle
     * @throws SQLException If a database access error occurs
     */
    List<Service> findByVehicle(int vehicleId) throws SQLException;

    /**
     * Finds services assigned to a specific technician.
     *
     * @param technicianId The ID of the technician
     * @return A list of services assigned to the technician
     * @throws SQLException If a database access error occurs
     */
    List<Service> findByTechnician(int technicianId) throws SQLException;

    /**
     * Finds services with a specific status.
     *
     * @param status The status to search for
     * @return A list of services with the specified status
     * @throws SQLException If a database access error occurs
     */
    List<Service> findByStatus(ServiceStatus status) throws SQLException;

    /**
     * Finds services created within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of services created within the date range
     * @throws SQLException If a database access error occurs
     */
    List<Service> findByDateRange(Date startDate, Date endDate) throws SQLException;

    /**
     * Adds a part to a service.
     *
     * @param partInService The part in service record to add
     * @return The added part in service record with its generated ID
     * @throws SQLException If a database access error occurs
     */
    PartInService addPartToService(PartInService partInService) throws SQLException;

    /**
     * Updates a part in a service.
     *
     * @param partInService The part in service record to update
     * @return The updated part in service record
     * @throws SQLException If a database access error occurs
     */
    PartInService updatePartInService(PartInService partInService) throws SQLException;

    /**
     * Removes a part from a service.
     *
     * @param partInServiceId The ID of the part in service record to remove
     * @return true if the part was removed, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean removePartFromService(int partInServiceId) throws SQLException;

    /**
     * Finds all parts used in a specific service.
     *
     * @param serviceId The ID of the service
     * @return A list of parts used in the service
     * @throws SQLException If a database access error occurs
     */
    List<PartInService> findPartsByService(int serviceId) throws SQLException;

    /**
     * Updates the status of a service.
     *
     * @param serviceId The ID of the service
     * @param status The new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException If a database access error occurs
     */
    boolean updateServiceStatus(int serviceId, ServiceStatus status) throws SQLException;

    /**
     * Calculates the total cost of a service (labor + parts).
     *
     * @param serviceId The ID of the service
     * @return The total cost of the service
     * @throws SQLException If a database access error occurs
     */
    double calculateServiceCost(int serviceId) throws SQLException;
}