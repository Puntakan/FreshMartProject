-- MySQL Workbench Forward Engineering

drop schema projectstore;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema projectstore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema projectstore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `projectstore` DEFAULT CHARACTER SET utf8 ;
USE `projectstore` ;

-- -----------------------------------------------------
-- Table `projectstore`.`history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projectstore`.`history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numList` JSON,
  `datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customerType` ENUM('Member', 'Guest') NOT NULL DEFAULT 'Guest',
  `discount` DECIMAL(10,2) NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `projectstore`.`history` (`numList`, `customerType`, `discount`, `total`) 
VALUES ('[10000, 30000]', 'Guest', 0.00, 40000.00);