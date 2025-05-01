-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: employees
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `empid` int NOT NULL,
  `street` varchar(100) DEFAULT NULL,
  `city_id` int DEFAULT NULL,
  `state_id` int DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `identified_race` varchar(50) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `mobile_phone` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`empid`),
  KEY `city_id` (`city_id`),
  KEY `state_id` (`state_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`) ON DELETE CASCADE,
  CONSTRAINT `address_ibfk_2` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`),
  CONSTRAINT `address_ibfk_3` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,'2004-05-23',NULL),(2,NULL,NULL,NULL,NULL,NULL,NULL,'1991-07-18',NULL),(3,NULL,NULL,NULL,NULL,NULL,NULL,'2003-03-17',NULL),(4,NULL,NULL,NULL,NULL,NULL,NULL,'2000-12-05',NULL),(5,NULL,NULL,NULL,NULL,NULL,NULL,'1976-08-07',NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`city_id`),
  UNIQUE KEY `city_name` (`city_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `division` (
  `id` int NOT NULL AUTO_INCREMENT,
  `division_name` varchar(100) DEFAULT NULL,
  `addressLine1` varchar(100) DEFAULT NULL,
  `addressLine2` varchar(100) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `postalCode` varchar(10) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `division_name` (`division_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_division`
--

DROP TABLE IF EXISTS `employee_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_division` (
  `empid` int NOT NULL,
  `div_id` int NOT NULL,
  PRIMARY KEY (`empid`,`div_id`),
  KEY `div_id` (`div_id`),
  CONSTRAINT `employee_division_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`) ON DELETE CASCADE,
  CONSTRAINT `employee_division_ibfk_2` FOREIGN KEY (`div_id`) REFERENCES `division` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_division`
--

LOCK TABLES `employee_division` WRITE;
/*!40000 ALTER TABLE `employee_division` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_job_titles`
--

DROP TABLE IF EXISTS `employee_job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_job_titles` (
  `empid` int NOT NULL,
  `job_title_id` int NOT NULL,
  PRIMARY KEY (`empid`),
  KEY `job_title_id` (`job_title_id`),
  CONSTRAINT `employee_job_titles_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`),
  CONSTRAINT `employee_job_titles_ibfk_2` FOREIGN KEY (`job_title_id`) REFERENCES `job_titles` (`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_job_titles`
--

LOCK TABLES `employee_job_titles` WRITE;
/*!40000 ALTER TABLE `employee_job_titles` DISABLE KEYS */;
INSERT INTO `employee_job_titles` VALUES (2,3),(3,3),(4,3),(1,1001);
/*!40000 ALTER TABLE `employee_job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `empid` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `ssn` varchar(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `HireDate` date DEFAULT NULL,
  `salary` decimal(10,2) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`empid`),
  UNIQUE KEY `ssn` (`ssn`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Trey','Palmer','111-11-1111',NULL,NULL,225000.00,'1234'),(2,'Jimmy','Butler','222-22-2222',NULL,NULL,NULL,'password456'),(3,'Reed','Sheppard','333-33-3333',NULL,NULL,NULL,'5678'),(4,'Jalen','Milroe','444-44-4444',NULL,NULL,NULL,'password123'),(5,'Samuel','Jackson','555-55-5555',NULL,NULL,103482.00,'defaultPassword');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_titles`
--

DROP TABLE IF EXISTS `job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_titles` (
  `job_title_id` int NOT NULL AUTO_INCREMENT,
  `job_title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`job_title_id`),
  UNIQUE KEY `job_title` (`job_title`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_titles`
--

LOCK TABLES `job_titles` WRITE;
/*!40000 ALTER TABLE `job_titles` DISABLE KEYS */;
INSERT INTO `job_titles` VALUES (3,'associate'),(1001,'manager');
/*!40000 ALTER TABLE `job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `empid` int NOT NULL,
  `pay_date` date NOT NULL,
  `earnings` decimal(10,2) DEFAULT NULL,
  `fed_tax` decimal(10,2) DEFAULT NULL,
  `fed_med` decimal(10,2) DEFAULT NULL,
  `fed_ss` decimal(10,2) DEFAULT NULL,
  `state_tax` decimal(10,2) DEFAULT NULL,
  `retire_401k` decimal(10,2) DEFAULT NULL,
  `health_care` decimal(10,2) DEFAULT NULL,
  KEY `empid` (`empid`),
  CONSTRAINT `payroll_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,'2025-04-28',31254.30,NULL,NULL,NULL,NULL,NULL,NULL),(3,'2025-04-28',45000.00,NULL,NULL,NULL,NULL,NULL,NULL),(1,'2025-04-30',35942.45,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `state` (
  `state_id` int NOT NULL AUTO_INCREMENT,
  `state_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`state_id`),
  UNIQUE KEY `state_name` (`state_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-01 10:29:08
