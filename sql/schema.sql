-- -----------------------------------------------------
-- Schema carmotors
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `carmotors` ;

-- -----------------------------------------------------
-- Schema carmotors
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `carmotors` DEFAULT CHARACTER SET utf8 ;
USE `carmotors` ;

-- -----------------------------------------------------
-- Table `carmotors`.`suppliers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`suppliers` (
  `supplier_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `tax_id` VARCHAR(20) NULL,
  `contact` VARCHAR(255) NULL,
  `visit_frequency` VARCHAR(100) NULL,
  PRIMARY KEY (`supplier_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`parts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`parts` (
  `part_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `type` ENUM('Mechanical', 'Electrical', 'Body', 'Consumable') NOT NULL,
  `compatible_make_model` VARCHAR(255) NULL,
  `supplier_id` INT NULL,
  `quantity_in_stock` INT NOT NULL DEFAULT 0,
  `minimum_stock` INT NOT NULL DEFAULT 1,
  `entry_date` DATE NOT NULL,
  `estimated_lifespan` DATE NULL,
  `status` ENUM('Available', 'Reserved', 'Out_of_service') NOT NULL DEFAULT 'Available',
  `batch_id` VARCHAR(100) NULL,
  PRIMARY KEY (`part_id`),
  INDEX `fk_parts_suppliers1_idx` (`supplier_id` ASC) VISIBLE,
  CONSTRAINT `fk_parts_suppliers1`
    FOREIGN KEY (`supplier_id`)
    REFERENCES `carmotors`.`suppliers` (`supplier_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`customers` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `identification` VARCHAR(20) NOT NULL UNIQUE,
  `phone` VARCHAR(20) NULL,
  `email` VARCHAR(255) NULL,
  PRIMARY KEY (`customer_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`vehicles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`vehicles` (
  `vehicle_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `make` VARCHAR(100) NOT NULL,
  `model` VARCHAR(100) NOT NULL,
  `license_plate` VARCHAR(10) NOT NULL UNIQUE,
  `type` VARCHAR(50) NULL,
  PRIMARY KEY (`vehicle_id`),
  INDEX `fk_vehicles_customers1_idx` (`customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_vehicles_customers1`
    FOREIGN KEY (`customer_id`)
    REFERENCES `carmotors`.`customers` (`customer_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`services` (
  `service_id` INT NOT NULL AUTO_INCREMENT,
  `maintenance_type` ENUM('Preventive', 'Corrective') NOT NULL,
  `vehicle_id` INT NOT NULL,
  `mileage` INT NULL,
  `description` TEXT NULL,
  `initial_diagnosis` TEXT NULL,
  `final_observations` TEXT NULL,
  `estimated_time` DECIMAL(5,2) NULL,
  `labor_cost` DECIMAL(10,2) NULL,
  `status` ENUM('Pending', 'In_progress', 'Completed', 'Delivered') NOT NULL DEFAULT 'Pending',
  `start_date` TIMESTAMP NULL,
  `end_date` TIMESTAMP NULL,
  `warranty_until` DATE NULL,
  PRIMARY KEY (`service_id`),
  INDEX `fk_services_vehicles1_idx` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_services_vehicles1`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `carmotors`.`vehicles` (`vehicle_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`parts_in_service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`parts_in_service` (
  `parts_in_service_id` INT NOT NULL AUTO_INCREMENT,
  `service_id` INT NOT NULL,
  `part_id` INT NOT NULL,
  `quantity_used` INT NOT NULL DEFAULT 1,
  `unit_price` DECIMAL(10,2) NULL,
  PRIMARY KEY (`parts_in_service_id`),
  INDEX `fk_parts_in_service_services1_idx` (`service_id` ASC) VISIBLE,
  INDEX `fk_parts_in_service_parts1_idx` (`part_id` ASC) VISIBLE,
  CONSTRAINT `fk_parts_in_service_services1`
    FOREIGN KEY (`service_id`)
    REFERENCES `carmotors`.`services` (`service_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_parts_in_service_parts1`
    FOREIGN KEY (`part_id`)
    REFERENCES `carmotors`.`parts` (`part_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`invoices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`invoices` (
  `invoice_id` INT NOT NULL AUTO_INCREMENT,
  `service_id` INT NOT NULL,
  `issue_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `invoice_number` VARCHAR(20) NOT NULL UNIQUE,
  `subtotal` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `taxes` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `total` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `electronic_invoice_id` VARCHAR(200) NULL,
  `qr_code` TEXT NULL,
  PRIMARY KEY (`invoice_id`),
  INDEX `fk_invoices_services1_idx` (`service_id` ASC) VISIBLE,
  CONSTRAINT `fk_invoices_services1`
    FOREIGN KEY (`service_id`)
    REFERENCES `carmotors`.`services` (`service_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`purchase_orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`purchase_orders` (
  `purchase_order_id` INT NOT NULL AUTO_INCREMENT,
  `supplier_id` INT NOT NULL,
  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expected_delivery_date` DATE NULL,
  `status` ENUM('Pending', 'Sent', 'Received', 'Cancelled') NOT NULL DEFAULT 'Pending',
  `observations` TEXT NULL,
  PRIMARY KEY (`purchase_order_id`),
  INDEX `fk_purchase_orders_suppliers1_idx` (`supplier_id` ASC) VISIBLE,
  CONSTRAINT `fk_purchase_orders_suppliers1`
    FOREIGN KEY (`supplier_id`)
    REFERENCES `carmotors`.`suppliers` (`supplier_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`purchase_order_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`purchase_order_detail` (
  `order_detail_id` INT NOT NULL AUTO_INCREMENT,
  `purchase_order_id` INT NOT NULL,
  `part_id` INT NOT NULL,
  `quantity_ordered` INT NOT NULL,
  `estimated_unit_price` DECIMAL(10,2) NULL,
  PRIMARY KEY (`order_detail_id`),
  INDEX `fk_purchase_order_detail_purchase_orders1_idx` (`purchase_order_id` ASC) VISIBLE,
  INDEX `fk_purchase_order_detail_parts1_idx` (`part_id` ASC) VISIBLE,
  CONSTRAINT `fk_purchase_order_detail_purchase_orders1`
    FOREIGN KEY (`purchase_order_id`)
    REFERENCES `carmotors`.`purchase_orders` (`purchase_order_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_purchase_order_detail_parts1`
    FOREIGN KEY (`part_id`)
    REFERENCES `carmotors`.`parts` (`part_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`campaigns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`campaigns` (
  `campaign_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `discount_percentage` DECIMAL(5,2) NULL,
  PRIMARY KEY (`campaign_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`campaign_appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`campaign_appointments` (
  `campaign_appointment_id` INT NOT NULL AUTO_INCREMENT,
  `campaign_id` INT NOT NULL,
  `vehicle_id` INT NOT NULL,
  `scheduled_date` DATE NOT NULL,
  `status` ENUM('Pending', 'Confirmed', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`campaign_appointment_id`),
  INDEX `fk_campaign_appointments_campaigns1_idx` (`campaign_id` ASC) VISIBLE,
  INDEX `fk_campaign_appointments_vehicles1_idx` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_campaign_appointments_campaigns1`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `carmotors`.`campaigns` (`campaign_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_campaign_appointments_vehicles1`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `carmotors`.`vehicles` (`vehicle_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `carmotors`.`inspections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`inspections` (
  `inspection_id` INT NOT NULL AUTO_INCREMENT,
  `vehicle_id` INT NOT NULL,
  `inspection_type` VARCHAR(100) NOT NULL,
  `inspection_date` DATE NOT NULL,
  `result` ENUM('Approved', 'Repairs_needed', 'Rejected') NOT NULL,
  `observations` TEXT NULL,
  `next_inspection_date` DATE NULL,
  PRIMARY KEY (`inspection_id`),
  INDEX `fk_inspections_vehicles1_idx` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_inspections_vehicles1`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `carmotors`.`vehicles` (`vehicle_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table for technicians/employees
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`technicians` (
  `technician_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `identification` VARCHAR(20) NOT NULL UNIQUE,
  `specialty` ENUM('Mechanical', 'Electrical', 'Body', 'General') NOT NULL,
  `phone` VARCHAR(20) NULL,
  `email` VARCHAR(255) NULL,
  `hire_date` DATE NOT NULL,
  `status` ENUM('Active', 'Inactive', 'Vacation') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`technician_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table for assigning technicians to services
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`technicians_service` (
  `technician_service_id` INT NOT NULL AUTO_INCREMENT,
  `service_id` INT NOT NULL,
  `technician_id` INT NOT NULL,
  `assignment_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `hours_worked` DECIMAL(5,2) NULL,
  `observations` TEXT NULL,
  PRIMARY KEY (`technician_service_id`),
  INDEX `fk_technicians_service_services1_idx` (`service_id` ASC) VISIBLE,
  INDEX `fk_technicians_service_technicians1_idx` (`technician_id` ASC) VISIBLE,
  CONSTRAINT `fk_technicians_service_services1`
    FOREIGN KEY (`service_id`)
    REFERENCES `carmotors`.`services` (`service_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_technicians_service_technicians1`
    FOREIGN KEY (`technician_id`)
    REFERENCES `carmotors`.`technicians` (`technician_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table for customer loyalty program
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`loyalty_program` (
  `program_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `accumulated_points` INT NOT NULL DEFAULT 0,
  `customer_level` ENUM('Bronze', 'Silver', 'Gold', 'Platinum') NOT NULL DEFAULT 'Bronze',
  `program_entry_date` DATE NOT NULL,
  `last_update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`program_id`),
  INDEX `fk_loyalty_program_customers1_idx` (`customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_loyalty_program_customers1`
    FOREIGN KEY (`customer_id`)
    REFERENCES `carmotors`.`customers` (`customer_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table for supplier evaluation
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`supplier_evaluation` (
  `evaluation_id` INT NOT NULL AUTO_INCREMENT,
  `supplier_id` INT NOT NULL,
  `evaluation_date` DATE NOT NULL,
  `punctuality_rating` INT NOT NULL,
  `quality_rating` INT NOT NULL,
  `price_rating` INT NOT NULL,
  `total_rating` DECIMAL(3,1) GENERATED ALWAYS AS ((punctuality_rating + quality_rating + price_rating) / 3) STORED,
  `observations` TEXT NULL,
  `evaluator` VARCHAR(100) NULL,
  PRIMARY KEY (`evaluation_id`),
  INDEX `fk_supplier_evaluation_suppliers1_idx` (`supplier_id` ASC) VISIBLE,
  CONSTRAINT `fk_supplier_evaluation_suppliers1`
    FOREIGN KEY (`supplier_id`)
    REFERENCES `carmotors`.`suppliers` (`supplier_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table to record vehicle delivery orders
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `carmotors`.`delivery_orders` (
  `delivery_order_id` INT NOT NULL AUTO_INCREMENT,
  `service_id` INT NOT NULL,
  `delivery_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_observations` TEXT NULL,
  `customer_satisfaction` ENUM('Very satisfied', 'Satisfied', 'Neutral', 'Dissatisfied', 'Very dissatisfied') NULL,
  `customer_signature` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`delivery_order_id`),
  INDEX `fk_delivery_orders_services1_idx` (`service_id` ASC) VISIBLE,
  CONSTRAINT `fk_delivery_orders_services1`
    FOREIGN KEY (`service_id`)
    REFERENCES `carmotors`.`services` (`service_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Views for Common Queries
-- -----------------------------------------------------

-- View to get the list of parts with supplier information
CREATE VIEW view_parts_with_supplier AS
SELECT
    p.part_id,
    p.name AS part_name,
    p.type,
    p.compatible_make_model,
    s.name AS supplier_name,
    s.contact AS supplier_contact,
    p.quantity_in_stock,
    p.minimum_stock,
    p.entry_date,
    p.estimated_lifespan,
    p.status,
    p.batch_id
FROM parts p
LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id;

-- View to get services with vehicle and customer information
CREATE VIEW view_services_with_vehicle_customer AS
SELECT
    s.service_id,
    s.maintenance_type,
    v.license_plate AS vehicle_license_plate,
    v.make AS vehicle_make,
    v.model AS vehicle_model,
    c.name AS customer_name,
    c.identification AS customer_identification,
    s.description AS service_detail,
    s.initial_diagnosis,
    s.mileage,
    s.estimated_time,
    s.labor_cost,
    s.status AS service_status,
    s.start_date,
    s.end_date,
    s.warranty_until
FROM services s
JOIN vehicles v ON s.vehicle_id = v.vehicle_id
JOIN customers c ON v.customer_id = c.customer_id;

-- View to get the details of parts used in a service
CREATE VIEW view_service_parts_detail AS
SELECT
    s.service_id,
    v.license_plate AS vehicle_license_plate,
    p.name AS part_name,
    ps.quantity_used,
    ps.unit_price,
    (ps.quantity_used * ps.unit_price) AS part_subtotal
FROM services s
JOIN vehicles v ON s.vehicle_id = v.vehicle_id
JOIN parts_in_service ps ON s.service_id = ps.service_id
JOIN parts p ON ps.part_id = p.part_id;

-- View to get invoices with service and customer information
CREATE VIEW view_invoices_with_service_customer AS
SELECT
    i.invoice_id,
    i.invoice_number,
    i.issue_date,
    vs.vehicle_license_plate,
    vs.vehicle_make,
    vs.vehicle_model,
    vs.customer_name,
    i.subtotal AS invoice_subtotal,
    i.taxes AS invoice_taxes,
    i.total AS invoice_total,
    i.electronic_invoice_id,
    i.qr_code
FROM invoices i
JOIN view_services_with_vehicle_customer vs ON i.service_id = vs.service_id;

-- View to get purchase orders with supplier information
CREATE VIEW view_purchase_orders_with_supplier AS
SELECT
    po.purchase_order_id,
    s.name AS supplier_name,
    s.contact AS supplier_contact,
    po.creation_date,
    po.expected_delivery_date,
    po.status AS purchase_order_status,
    po.observations
FROM purchase_orders po
JOIN suppliers s ON po.supplier_id = s.supplier_id;

-- View to get the details of parts in a purchase order
CREATE VIEW view_purchase_order_parts_detail AS
SELECT
    po.purchase_order_id,
    p.name AS part_name,
    pod.quantity_ordered,
    pod.estimated_unit_price,
    (pod.quantity_ordered * pod.estimated_unit_price) AS detail_subtotal
FROM purchase_orders po
JOIN purchase_order_detail pod ON po.purchase_order_id = pod.purchase_order_id
JOIN parts p ON pod.part_id = p.part_id;

-- View for complete maintenance history by vehicle
CREATE VIEW view_vehicle_maintenance_history AS
SELECT
    v.vehicle_id,
    v.license_plate,
    v.make,
    v.model,
    c.name AS customer_name,
    s.service_id,
    s.maintenance_type,
    s.description,
    s.mileage,
    s.initial_diagnosis,
    s.final_observations,
    s.status AS service_status,
    s.start_date,
    s.end_date,
    s.warranty_until,
    i.inspection_date,
    i.inspection_type,
    i.result AS inspection_result
FROM vehicles v
LEFT JOIN customers c ON v.customer_id = c.customer_id
LEFT JOIN services s ON v.vehicle_id = s.vehicle_id
LEFT JOIN inspections i ON v.vehicle_id = i.vehicle_id
ORDER BY v.vehicle_id, s.start_date DESC, i.inspection_date DESC;

-- View for technicians productivity
CREATE VIEW view_technician_productivity AS
SELECT
    t.technician_id,
    t.name AS technician_name,
    t.specialty,
    COUNT(ts.service_id) AS total_assigned_services,
    SUM(ts.hours_worked) AS total_hours_worked,
    AVG(ts.hours_worked) AS average_hours_per_service,
    COUNT(CASE WHEN s.status = 'Completed' THEN 1 END) AS completed_services,
    COUNT(CASE WHEN s.status = 'In_progress' THEN 1 END) AS services_in_progress
FROM technicians t
LEFT JOIN technicians_service ts ON t.technician_id = ts.technician_id
LEFT JOIN services s ON ts.service_id = s.service_id
GROUP BY t.technician_id, t.name, t.specialty;

-- View for loyalty program and benefits
CREATE VIEW view_customer_loyalty AS
SELECT
    c.customer_id,
    c.name AS customer_name,
    c.identification,
    lp.accumulated_points,
    lp.customer_level,
    COUNT(v.vehicle_id) AS registered_vehicles_count,
    COUNT(DISTINCT s.service_id) AS total_services_performed,
    SUM(i.total) AS total_invoiced
FROM customers c
LEFT JOIN loyalty_program lp ON c.customer_id = lp.customer_id
LEFT JOIN vehicles v ON c.customer_id = v.customer_id
LEFT JOIN services s ON v.vehicle_id = s.vehicle_id
LEFT JOIN invoices i ON s.service_id = i.service_id
GROUP BY c.customer_id, c.name, c.identification, lp.accumulated_points, lp.customer_level;

-- View for supplier evaluation with history
CREATE VIEW view_supplier_evaluation AS
SELECT
    s.supplier_id,
    s.name AS supplier_name,
    s.tax_id,
    s.contact,
    AVG(se.total_rating) AS average_rating,
    MAX(se.evaluation_date) AS last_evaluation,
    COUNT(se.evaluation_id) AS total_evaluations
FROM suppliers s
LEFT JOIN supplier_evaluation se ON s.supplier_id = se.supplier_id
GROUP BY s.supplier_id, s.name, s.tax_id, s.contact;

-- View for delivery order tracking
CREATE VIEW view_delivery_tracking AS
SELECT
    do.delivery_order_id,
    s.service_id,
    v.license_plate AS vehicle_license_plate,
    c.name AS customer_name,
    s.maintenance_type,
    s.description,
    s.end_date AS service_completion_date,
    do.delivery_date,
    do.customer_satisfaction,
    do.customer_signature,
    DATEDIFF(do.delivery_date, s.end_date) AS days_between_completion_delivery
FROM delivery_orders do
JOIN services s ON do.service_id = s.service_id
JOIN vehicles v ON s.vehicle_id = v.vehicle_id
JOIN customers c ON v.customer_id = c.customer_id;
