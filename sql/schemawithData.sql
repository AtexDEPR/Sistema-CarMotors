-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: carmotors
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `campaign_appointments`
--

DROP TABLE IF EXISTS `campaign_appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign_appointments` (
  `campaign_appointment_id` int NOT NULL AUTO_INCREMENT,
  `campaign_id` int NOT NULL,
  `vehicle_id` int NOT NULL,
  `scheduled_date` date NOT NULL,
  `status` enum('Pending','Confirmed','Completed','Cancelled') NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`campaign_appointment_id`),
  KEY `fk_campaign_appointments_campaigns1_idx` (`campaign_id`),
  KEY `fk_campaign_appointments_vehicles1_idx` (`vehicle_id`),
  CONSTRAINT `fk_campaign_appointments_campaigns1` FOREIGN KEY (`campaign_id`) REFERENCES `campaigns` (`campaign_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_campaign_appointments_vehicles1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign_appointments`
--

LOCK TABLES `campaign_appointments` WRITE;
/*!40000 ALTER TABLE `campaign_appointments` DISABLE KEYS */;
INSERT INTO `campaign_appointments` VALUES (1,1,1,'2025-06-15','Pending'),(2,2,2,'2025-11-10','Confirmed'),(3,3,3,'2025-03-20','Pending'),(4,4,4,'2025-09-15','Confirmed'),(5,5,5,'2025-12-05','Pending'),(6,6,6,'2025-01-10','Confirmed'),(7,7,7,'2025-08-20','Pending'),(8,8,8,'2025-04-15','Confirmed'),(9,9,9,'2025-07-10','Pending'),(10,10,10,'2025-02-15','Confirmed');
/*!40000 ALTER TABLE `campaign_appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaigns`
--

DROP TABLE IF EXISTS `campaigns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaigns` (
  `campaign_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `discount_percentage` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`campaign_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaigns`
--

LOCK TABLES `campaigns` WRITE;
/*!40000 ALTER TABLE `campaigns` DISABLE KEYS */;
INSERT INTO `campaigns` VALUES (1,'Summer Tune-Up','Complete vehicle check','2025-06-01','2025-08-31',10.00),(2,'Winter Prep','Winter readiness package','2025-11-01','2025-12-31',15.00),(3,'Spring Refresh','Spring maintenance deal','2025-03-01','2025-05-31',12.00),(4,'Fall Service','Fall vehicle check','2025-09-01','2025-11-30',10.00),(5,'Holiday Special','Holiday maintenance offer','2025-12-01','2025-12-31',20.00),(6,'New Year Promo','Start the year right','2025-01-01','2025-01-31',15.00),(7,'Back to School','Student vehicle check','2025-08-01','2025-09-30',10.00),(8,'Eco-Friendly Deal','Hybrid vehicle service','2025-04-01','2025-06-30',12.00),(9,'Safety First','Brake and tire check','2025-07-01','2025-09-30',10.00),(10,'Loyalty Bonus','For loyal customers','2025-02-01','2025-04-30',15.00);
/*!40000 ALTER TABLE `campaigns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `identification_number` varchar(50) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Juan Pérez','CC12345678','3001234567','juan.perez@example.com','Calle 10 #5-23, Bucaramanga','2025-05-06 11:16:27','2025-05-06 11:16:27'),(2,'Laura Gómez','CC87654321','3017654321','laura.gomez@example.com','Carrera 15 #45-12, Floridablanca','2025-05-06 11:16:27','2025-05-06 11:16:27'),(3,'Carlos Ruiz','CC11223344','3023344556','carlos.ruiz@example.com','Av. Libertadores #78-90, Bucaramanga','2025-05-06 11:16:27','2025-05-06 11:16:27'),(4,'Ana Torres','CC99887766','3039876543','ana.torres@example.com','Calle 100 #25-36, Girón','2025-05-06 11:16:27','2025-05-06 11:16:27'),(5,'Miguel Castro','CC44556677','3041122334','miguel.castro@example.com','Carrera 7 #14-89, Piedecuesta','2025-05-06 11:16:27','2025-05-06 11:16:27'),(6,'Juan Pérez','CC12345678','3001234567','juan.perez@example.com','Calle 10 #5-23, Bucaramanga','2025-05-06 11:24:19','2025-05-06 11:24:19'),(7,'Laura Gómez','CC87654321','3017654321','laura.gomez@example.com','Carrera 15 #45-12, Floridablanca','2025-05-06 11:24:19','2025-05-06 11:24:19'),(8,'Carlos Ruiz','CC11223344','3023344556','carlos.ruiz@example.com','Av. Libertadores #78-90, Bucaramanga','2025-05-06 11:24:19','2025-05-06 11:24:19'),(9,'Ana Torres','CC99887766','3039876543','ana.torres@example.com','Calle 100 #25-36, Girón','2025-05-06 11:24:19','2025-05-06 11:24:19'),(10,'Miguel Castro','CC44556677','3041122334','miguel.castro@example.com','Carrera 7 #14-89, Piedecuesta','2025-05-06 11:24:19','2025-05-06 11:24:19'),(11,'John Doe','ID123456','555-0101','john.doe@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(12,'Jane Smith','ID789012','555-0102','jane.smith@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(13,'Michael Brown','ID345678','555-0103','michael.brown@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(14,'Emily Davis','ID901234','555-0104','emily.davis@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(15,'William Johnson','ID567890','555-0105','william.johnson@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(16,'Sarah Wilson','ID234567','555-0106','sarah.wilson@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(17,'David Lee','ID890123','555-0107','david.lee@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(18,'Lisa Garcia','ID456789','555-0108','lisa.garcia@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(19,'James Martinez','ID012345','555-0109','james.martinez@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(20,'Anna Thompson','ID678901','555-0110','anna.thompson@email.com',NULL,'2025-05-06 11:25:11','2025-05-06 11:25:11'),(21,'John Doe','ID123456','555-0101','john.doe@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(22,'Jane Smith','ID789012','555-0102','jane.smith@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(23,'Michael Brown','ID345678','555-0103','michael.brown@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(24,'Emily Davis','ID901234','555-0104','emily.davis@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(25,'William Johnson','ID567890','555-0105','william.johnson@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(26,'Sarah Wilson','ID234567','555-0106','sarah.wilson@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(27,'David Lee','ID890123','555-0107','david.lee@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(28,'Lisa Garcia','ID456789','555-0108','lisa.garcia@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(29,'James Martinez','ID012345','555-0109','james.martinez@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30'),(30,'Anna Thompson','ID678901','555-0110','anna.thompson@email.com',NULL,'2025-05-06 11:26:30','2025-05-06 11:26:30');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_orders`
--

DROP TABLE IF EXISTS `delivery_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_orders` (
  `delivery_order_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `delivery_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_observations` text,
  `customer_satisfaction` enum('Very satisfied','Satisfied','Neutral','Dissatisfied','Very dissatisfied') DEFAULT NULL,
  `customer_signature` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`delivery_order_id`),
  KEY `fk_delivery_orders_services1_idx` (`service_id`),
  CONSTRAINT `fk_delivery_orders_services1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_orders`
--

LOCK TABLES `delivery_orders` WRITE;
/*!40000 ALTER TABLE `delivery_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivery_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspections`
--

DROP TABLE IF EXISTS `inspections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspections` (
  `inspection_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_id` int NOT NULL,
  `inspection_type` varchar(100) NOT NULL,
  `inspection_date` date NOT NULL,
  `result` enum('Approved','Repairs_needed','Rejected') NOT NULL,
  `observations` text,
  `next_inspection_date` date DEFAULT NULL,
  PRIMARY KEY (`inspection_id`),
  KEY `fk_inspections_vehicles1_idx` (`vehicle_id`),
  CONSTRAINT `fk_inspections_vehicles1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspections`
--

LOCK TABLES `inspections` WRITE;
/*!40000 ALTER TABLE `inspections` DISABLE KEYS */;
INSERT INTO `inspections` VALUES (1,1,'Annual','2025-01-15','Approved','All systems normal','2026-01-15'),(2,2,'Safety','2025-01-16','Repairs_needed','Brake pads worn','2025-07-16'),(3,3,'Emissions','2025-01-17','Approved','Passed emissions test','2026-01-17'),(4,4,'Annual','2025-01-18','Approved','No issues found','2026-01-18'),(5,5,'Safety','2025-01-19','Approved','All safety features intact','2026-01-19'),(6,6,'Annual','2025-01-20','Repairs_needed','Radiator leak detected','2025-07-20'),(7,7,'Emissions','2025-01-21','Approved','Passed emissions test','2026-01-21'),(8,8,'Safety','2025-01-22','Approved','Tires in good condition','2026-01-22'),(9,9,'Annual','2025-01-23','Repairs_needed','Muffler noise detected','2025-07-23'),(10,10,'Safety','2025-01-24','Approved','Battery in good condition','2026-01-24');
/*!40000 ALTER TABLE `inspections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `invoice_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `issue_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `invoice_number` varchar(20) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL DEFAULT '0.00',
  `taxes` decimal(10,2) NOT NULL DEFAULT '0.00',
  `total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `electronic_invoice_id` varchar(200) DEFAULT NULL,
  `qr_code` text,
  PRIMARY KEY (`invoice_id`),
  UNIQUE KEY `invoice_number` (`invoice_number`),
  KEY `fk_invoices_services1_idx` (`service_id`),
  CONSTRAINT `fk_invoices_services1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,1,'2025-02-01 17:00:00','INV001',115.00,17.25,132.25,'EI001','QR001'),(2,2,'2025-02-02 17:00:00','INV002',250.00,37.50,287.50,'EI002','QR002'),(3,3,'2025-02-03 17:00:00','INV003',200.00,30.00,230.00,'EI003','QR003'),(4,4,'2025-02-03 19:00:00','INV004',450.00,67.50,517.50,'EI004','QR004'),(5,5,'2025-02-04 17:00:00','INV005',110.00,16.50,126.50,'EI005','QR005'),(6,6,'2025-02-05 17:00:00','INV006',700.00,105.00,805.00,'EI006','QR006'),(7,7,'2025-02-06 17:00:00','INV007',260.00,39.00,299.00,'EI007','QR007'),(8,8,'2025-02-07 18:00:00','INV008',730.00,109.50,839.50,'EI008','QR008'),(9,9,'2025-02-08 17:00:00','INV009',300.00,45.00,345.00,'EI009','QR009'),(10,10,'2025-02-09 17:00:00','INV010',250.00,37.50,287.50,'EI010','QR010');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loyalty_program`
--

DROP TABLE IF EXISTS `loyalty_program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loyalty_program` (
  `program_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `accumulated_points` int NOT NULL DEFAULT '0',
  `customer_level` enum('Bronze','Silver','Gold','Platinum') NOT NULL DEFAULT 'Bronze',
  `program_entry_date` date NOT NULL,
  `last_update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`program_id`),
  KEY `fk_loyalty_program_customers1_idx` (`customer_id`),
  CONSTRAINT `fk_loyalty_program_customers1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loyalty_program`
--

LOCK TABLES `loyalty_program` WRITE;
/*!40000 ALTER TABLE `loyalty_program` DISABLE KEYS */;
INSERT INTO `loyalty_program` VALUES (1,1,100,'Bronze','2024-01-01','2025-05-06 16:27:21'),(2,2,250,'Silver','2024-02-01','2025-05-06 16:27:21'),(3,3,150,'Bronze','2024-03-01','2025-05-06 16:27:21'),(4,4,300,'Silver','2024-04-01','2025-05-06 16:27:21'),(5,5,200,'Bronze','2024-05-01','2025-05-06 16:27:21'),(6,6,400,'Gold','2024-06-01','2025-05-06 16:27:21'),(7,7,120,'Bronze','2024-07-01','2025-05-06 16:27:21'),(8,8,350,'Silver','2024-08-01','2025-05-06 16:27:21'),(9,9,180,'Bronze','2024-09-01','2025-05-06 16:27:21'),(10,10,500,'Gold','2024-10-01','2025-05-06 16:27:21');
/*!40000 ALTER TABLE `loyalty_program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parts`
--

DROP TABLE IF EXISTS `parts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parts` (
  `part_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` enum('Mechanical','Electrical','Body','Consumable') NOT NULL,
  `compatible_make_model` varchar(255) DEFAULT NULL,
  `supplier_id` int DEFAULT NULL,
  `quantity_in_stock` int NOT NULL DEFAULT '0',
  `minimum_stock` int NOT NULL DEFAULT '1',
  `entry_date` date NOT NULL,
  `estimated_lifespan` date DEFAULT NULL,
  `status` enum('Available','Reserved','Out_of_service') NOT NULL DEFAULT 'Available',
  `batch_id` varchar(100) DEFAULT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`part_id`),
  KEY `fk_parts_suppliers1_idx` (`supplier_id`),
  CONSTRAINT `fk_parts_suppliers1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parts`
--

LOCK TABLES `parts` WRITE;
/*!40000 ALTER TABLE `parts` DISABLE KEYS */;
INSERT INTO `parts` VALUES (1,'Clutch Kit','Mechanical','Volkswagen Golf 2018',1,18,4,'2025-05-01','2027-05-01','Available','CK202505','2025-05-06 11:24:39','2025-05-06 11:24:39'),(2,'Fuel Pump','Electrical','Hyundai Elantra 2020',2,12,3,'2025-05-02','2028-05-02','Available','FP202505','2025-05-06 11:24:39','2025-05-06 11:24:39'),(3,'Side Mirror','Body','Toyota Corolla 2021',3,30,6,'2025-05-03',NULL,'Available','SM202505','2025-05-06 11:24:39','2025-05-06 11:24:39'),(4,'Timing Belt','Mechanical','Mazda 6 2017',1,22,5,'2025-05-04','2026-11-04','Available','TB202505','2025-05-06 11:24:39','2025-05-06 11:24:39'),(5,'AC Compressor','Electrical','Kia Sportage 2019',2,10,2,'2025-05-05','2029-05-05','Available','AC202505','2025-05-06 11:24:39','2025-05-06 11:24:39'),(6,'Clutch Kit','Mechanical','Volkswagen Golf 2018',1,18,4,'2025-05-01','2027-05-01','Available','CK202505','2025-05-06 11:26:25','2025-05-06 11:26:25'),(7,'Fuel Pump','Electrical','Hyundai Elantra 2020',2,12,3,'2025-05-02','2028-05-02','Available','FP202505','2025-05-06 11:26:25','2025-05-06 11:26:25'),(8,'Side Mirror','Body','Toyota Corolla 2021',3,30,6,'2025-05-03',NULL,'Available','SM202505','2025-05-06 11:26:25','2025-05-06 11:26:25'),(9,'Timing Belt','Mechanical','Mazda 6 2017',1,22,5,'2025-05-04','2026-11-04','Available','TB202505','2025-05-06 11:26:25','2025-05-06 11:26:25'),(10,'AC Compressor','Electrical','Kia Sportage 2019',2,10,2,'2025-05-05','2029-05-05','Available','AC202505','2025-05-06 11:26:25','2025-05-06 11:26:25');
/*!40000 ALTER TABLE `parts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parts_in_service`
--

DROP TABLE IF EXISTS `parts_in_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parts_in_service` (
  `parts_in_service_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `part_id` int NOT NULL,
  `quantity_used` int NOT NULL DEFAULT '1',
  `unit_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`parts_in_service_id`),
  KEY `fk_parts_in_service_services1_idx` (`service_id`),
  KEY `fk_parts_in_service_parts1_idx` (`part_id`),
  CONSTRAINT `fk_parts_in_service_parts1` FOREIGN KEY (`part_id`) REFERENCES `parts` (`part_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_parts_in_service_services1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parts_in_service`
--

LOCK TABLES `parts_in_service` WRITE;
/*!40000 ALTER TABLE `parts_in_service` DISABLE KEYS */;
INSERT INTO `parts_in_service` VALUES (1,1,4,1,15.00),(2,2,1,2,50.00),(3,3,3,1,200.00),(4,4,2,1,150.00),(5,5,4,2,15.00),(6,6,6,1,300.00),(7,7,7,2,100.00),(8,8,8,4,120.00),(9,9,9,1,180.00),(10,10,10,1,100.00);
/*!40000 ALTER TABLE `parts_in_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_details`
--

DROP TABLE IF EXISTS `purchase_order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_details` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `part_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`detail_id`),
  KEY `order_id` (`order_id`),
  KEY `part_id` (`part_id`),
  CONSTRAINT `purchase_order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `purchase_orders` (`order_id`),
  CONSTRAINT `purchase_order_details_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `parts` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_details`
--

LOCK TABLES `purchase_order_details` WRITE;
/*!40000 ALTER TABLE `purchase_order_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_date` date NOT NULL,
  `status` varchar(50) NOT NULL,
  `supplier_id` int DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `creation_date` datetime NOT NULL,
  `last_update_date` datetime NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `purchase_orders_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `services` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `maintenance_type` enum('Preventive','Corrective') NOT NULL,
  `vehicle_id` int NOT NULL,
  `mileage` int DEFAULT NULL,
  `description` text,
  `initial_diagnosis` text,
  `final_observations` text,
  `estimated_time` decimal(5,2) DEFAULT NULL,
  `labor_cost` decimal(10,2) DEFAULT NULL,
  `status` enum('Pending','In_progress','Completed','Delivered') NOT NULL DEFAULT 'Pending',
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `warranty_until` date DEFAULT NULL,
  PRIMARY KEY (`service_id`),
  KEY `fk_services_vehicles1_idx` (`vehicle_id`),
  CONSTRAINT `fk_services_vehicles1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (1,'Preventive',1,15000,'Oil change and tire rotation','Routine maintenance','Completed successfully',2.50,100.00,'Completed','2025-02-01 14:00:00','2025-02-01 16:30:00','2025-08-01'),(2,'Corrective',2,30000,'Brake pad replacement','Worn brake pads','Brakes restored',3.00,150.00,'In_progress','2025-02-02 15:00:00',NULL,NULL),(3,'Preventive',3,20000,'Full inspection','Routine check','All systems normal',4.00,200.00,'Pending',NULL,NULL,NULL),(4,'Corrective',4,25000,'Alternator repair','Charging issue','Alternator replaced',5.00,300.00,'Completed','2025-02-03 13:00:00','2025-02-03 18:00:00','2025-08-03'),(5,'Preventive',5,18000,'Filter replacement','Clogged filters','Filters replaced',2.00,80.00,'Completed','2025-02-04 14:00:00','2025-02-04 16:00:00','2025-08-04'),(6,'Corrective',6,35000,'Radiator replacement','Leaking radiator','New radiator installed',6.00,400.00,'In_progress','2025-02-05 13:00:00',NULL,NULL),(7,'Preventive',7,22000,'Headlight check','Dim headlights','Headlights adjusted',1.50,60.00,'Completed','2025-02-06 15:00:00','2025-02-06 16:30:00','2025-08-06'),(8,'Corrective',8,28000,'Tire replacement','Worn tires','New tires installed',3.50,250.00,'Completed','2025-02-07 14:00:00','2025-02-07 17:30:00','2025-08-07'),(9,'Preventive',9,19000,'Muffler inspection','Noise issue','Muffler repaired',2.50,120.00,'In_progress','2025-02-08 13:00:00',NULL,NULL),(10,'Corrective',10,27000,'Battery replacement','Dead battery','New battery installed',2.00,150.00,'Completed','2025-02-09 14:00:00','2025-02-09 16:00:00','2025-08-09');
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_evaluation`
--

DROP TABLE IF EXISTS `supplier_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_evaluation` (
  `evaluation_id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int NOT NULL,
  `evaluation_date` date NOT NULL,
  `punctuality_rating` int NOT NULL,
  `quality_rating` int NOT NULL,
  `price_rating` int NOT NULL,
  `total_rating` decimal(3,1) GENERATED ALWAYS AS ((((`punctuality_rating` + `quality_rating`) + `price_rating`) / 3)) STORED,
  `observations` text,
  `evaluator` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`evaluation_id`),
  KEY `fk_supplier_evaluation_suppliers1_idx` (`supplier_id`),
  CONSTRAINT `fk_supplier_evaluation_suppliers1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_evaluation`
--

LOCK TABLES `supplier_evaluation` WRITE;
/*!40000 ALTER TABLE `supplier_evaluation` DISABLE KEYS */;
INSERT INTO `supplier_evaluation` (`evaluation_id`, `supplier_id`, `evaluation_date`, `punctuality_rating`, `quality_rating`, `price_rating`, `observations`, `evaluator`) VALUES (1,1,'2025-01-15',8,9,7,'Reliable delivery','Manager A'),(2,2,'2025-01-16',7,8,8,'Good quality parts','Manager B'),(3,3,'2025-01-17',9,9,6,'Competitive pricing needed','Manager C'),(4,4,'2025-01-18',8,7,8,'Consistent supply','Manager D'),(5,5,'2025-01-19',7,8,9,'Excellent pricing','Manager E'),(6,6,'2025-01-20',9,8,7,'Timely delivery','Manager F'),(7,7,'2025-01-21',8,9,8,'High-quality components','Manager G'),(8,8,'2025-01-22',7,7,8,'Average performance','Manager H'),(9,9,'2025-01-23',8,8,7,'Good overall service','Manager I'),(10,10,'2025-01-24',9,9,9,'Outstanding supplier','Manager J');
/*!40000 ALTER TABLE `supplier_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `tax_id` varchar(20) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `visit_frequency` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'AutoParts Inc.','TAX123456','contact@autoparts.com','Weekly'),(2,'Motor Supplies Ltd.','TAX789012','sales@motorsupplies.com','Biweekly'),(3,'Quality Auto Parts','TAX345678','info@qualityparts.com','Monthly'),(4,'Speedy Spares','TAX901234','support@speedyspares.com','Weekly'),(5,'Reliable Components','TAX567890','orders@reliablecomp.com','Biweekly'),(6,'Prime Auto Solutions','TAX234567','prime@autosolutions.com','Monthly'),(7,'Tech Parts Co.','TAX890123','tech@partsco.com','Weekly'),(8,'Global Auto Supply','TAX456789','global@autosupply.com','Biweekly'),(9,'Precision Parts','TAX012345','precision@parts.com','Monthly'),(10,'Elite Auto Distributors','TAX678901','elite@autodist.com','Weekly'),(11,'AutoParts Inc.','TAX123456','contact@autoparts.com','Weekly'),(12,'Motor Supplies Ltd.','TAX789012','sales@motorsupplies.com','Biweekly'),(13,'Quality Auto Parts','TAX345678','info@qualityparts.com','Monthly'),(14,'Speedy Spares','TAX901234','support@speedyspares.com','Weekly'),(15,'Reliable Components','TAX567890','orders@reliablecomp.com','Biweekly'),(16,'Prime Auto Solutions','TAX234567','prime@autosolutions.com','Monthly'),(17,'Tech Parts Co.','TAX890123','tech@partsco.com','Weekly'),(18,'Global Auto Supply','TAX456789','global@autosupply.com','Biweekly'),(19,'Precision Parts','TAX012345','precision@parts.com','Monthly'),(20,'Elite Auto Distributors','TAX678901','elite@autodist.com','Weekly');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `technicians`
--

DROP TABLE IF EXISTS `technicians`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `technicians` (
  `technician_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `identification` varchar(20) NOT NULL,
  `specialty` enum('Mechanical','Electrical','Body','General') NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `hire_date` date NOT NULL,
  `status` enum('Active','Inactive','Vacation') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`technician_id`),
  UNIQUE KEY `identification` (`identification`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `technicians`
--

LOCK TABLES `technicians` WRITE;
/*!40000 ALTER TABLE `technicians` DISABLE KEYS */;
INSERT INTO `technicians` VALUES (1,'Robert Taylor','TECH123','Mechanical','555-0201','robert.taylor@shop.com','2024-01-01','Active'),(2,'Laura White','TECH456','Electrical','555-0202','laura.white@shop.com','2024-02-01','Active'),(3,'Mark Green','TECH789','Body','555-0203','mark.green@shop.com','2024-03-01','Vacation'),(4,'Sophie Clark','TECH012','General','555-0204','sophie.clark@shop.com','2024-04-01','Active'),(5,'Daniel Adams','TECH345','Mechanical','555-0205','daniel.adams@shop.com','2024-05-01','Active'),(6,'Emma Harris','TECH678','Electrical','555-0206','emma.harris@shop.com','2024-06-01','Active'),(7,'Thomas Lewis','TECH901','Body','555-0207','thomas.lewis@shop.com','2024-07-01','Active'),(8,'Olivia Walker','TECH234','General','555-0208','olivia.walker@shop.com','2024-08-01','Vacation'),(9,'Jack Young','TECH567','Mechanical','555-0209','jack.young@shop.com','2024-09-01','Active'),(10,'Chloe King','TECH890','Electrical','555-0210','chloe.king@shop.com','2024-10-01','Active');
/*!40000 ALTER TABLE `technicians` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `technicians_service`
--

DROP TABLE IF EXISTS `technicians_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `technicians_service` (
  `technician_service_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `technician_id` int NOT NULL,
  `assignment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `hours_worked` decimal(5,2) DEFAULT NULL,
  `observations` text,
  PRIMARY KEY (`technician_service_id`),
  KEY `fk_technicians_service_services1_idx` (`service_id`),
  KEY `fk_technicians_service_technicians1_idx` (`technician_id`),
  CONSTRAINT `fk_technicians_service_services1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_technicians_service_technicians1` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`technician_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `technicians_service`
--

LOCK TABLES `technicians_service` WRITE;
/*!40000 ALTER TABLE `technicians_service` DISABLE KEYS */;
INSERT INTO `technicians_service` VALUES (1,1,1,'2025-02-01 14:00:00',2.50,'Oil change completed'),(2,2,2,'2025-02-02 15:00:00',3.00,'Brake pad replacement in progress'),(3,3,3,'2025-02-03 13:00:00',NULL,'Inspection pending'),(4,4,4,'2025-02-03 13:00:00',5.00,'Alternator replaced'),(5,5,5,'2025-02-04 14:00:00',2.00,'Filters replaced'),(6,6,6,'2025-02-05 13:00:00',6.00,'Radiator replacement in progress'),(7,7,7,'2025-02-06 15:00:00',1.50,'Headlights adjusted'),(8,8,8,'2025-02-07 14:00:00',3.50,'Tires replaced'),(9,9,9,'2025-02-08 13:00:00',2.50,'Muffler repair in progress'),(10,10,10,'2025-02-09 14:00:00',2.00,'Battery replaced');
/*!40000 ALTER TABLE `technicians_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`vehicle_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,1,'ABC123','Toyota','Corolla',2020,'2025-05-06 11:16:32','2025-05-06 11:16:32'),(2,2,'XYZ789','Chevrolet','Spark GT',2018,'2025-05-06 11:16:32','2025-05-06 11:16:32'),(3,3,'DEF456','Mazda','CX-5',2022,'2025-05-06 11:16:32','2025-05-06 11:16:32'),(4,4,'GHI321','Renault','Duster',2019,'2025-05-06 11:16:32','2025-05-06 11:16:32'),(5,1,'ABC123','Toyota','Corolla',2020,'2025-05-06 11:24:26','2025-05-06 11:24:26'),(6,2,'XYZ789','Chevrolet','Spark GT',2018,'2025-05-06 11:24:26','2025-05-06 11:24:26'),(7,3,'DEF456','Mazda','CX-5',2022,'2025-05-06 11:24:26','2025-05-06 11:24:26'),(8,4,'GHI321','Renault','Duster',2019,'2025-05-06 11:24:26','2025-05-06 11:24:26'),(9,1,'ABC123','Toyota','Corolla',2020,'2025-05-06 11:26:01','2025-05-06 11:26:01'),(10,2,'XYZ789','Chevrolet','Spark GT',2018,'2025-05-06 11:26:01','2025-05-06 11:26:01'),(11,3,'DEF456','Mazda','CX-5',2022,'2025-05-06 11:26:01','2025-05-06 11:26:01'),(12,4,'GHI321','Renault','Duster',2019,'2025-05-06 11:26:01','2025-05-06 11:26:01');
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'carmotors'
--

--
-- Dumping routines for database 'carmotors'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-06 11:27:46
