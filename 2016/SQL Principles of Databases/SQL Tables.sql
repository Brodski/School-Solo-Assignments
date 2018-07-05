-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema newSchoolProj
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema newSchoolProj
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `newSchoolProj` DEFAULT CHARACTER SET utf8 ;
USE `newSchoolProj` ;

-- -----------------------------------------------------
-- Table `newSchoolProj`.`Customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Customers` (
  `Cust_ID` INT NOT NULL AUTO_INCREMENT,
  `Plan_type` VARCHAR(20) NULL,
  `Cust_FName` VARCHAR(20) NOT NULL,
  `Cust_LName` VARCHAR(20) NOT NULL,
  `Cust_State` VARCHAR(2) NOT NULL,
  `Cust_Zip` VARCHAR(10) NOT NULL,
  `Cust_Address` VARCHAR(100) NOT NULL,
  `Cust_payment` VARCHAR(20) NULL,
  PRIMARY KEY (`Cust_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Rental_History`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Rental_History` (
  `DVD_ID` INT NOT NULL,
  `Cust_ID` INT NOT NULL,
  `DVD_Rented_Date` DATE NULL,
  `DVD_Return_Date` DATE NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`DVD_Specific_Info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`DVD_Specific_Info` (
  `DVD_ID` INT NOT NULL AUTO_INCREMENT,
  `Movie_or_Season_ID` INT NOT NULL,
  `is_it_movie` TINYINT(1) NOT NULL,
  `DVD_Status` VARCHAR(20) NOT NULL,
  `DVD_New_as_of_x` DATE NOT NULL,
  `Specific_DVD_Infocol` VARCHAR(45) NULL,
  PRIMARY KEY (`DVD_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Movies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Movies` (
  `Movie_ID` INT NOT NULL AUTO_INCREMENT,
  `Movie_Title` VARCHAR(200) NOT NULL,
  `Movie_YearRelease` INT NULL,
  `Movie_Description` VARCHAR(300) NULL,
  PRIMARY KEY (`Movie_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Genre` (
  `Movie_or_Series_ID` INT NOT NULL,
  `Genre_type` VARCHAR(20) NULL,
  `is_it_movie` TINYINT(1) NOT NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`DVD_Series_Split`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`DVD_Series_Split` (
  `DVD_Series_Split_ID` INT NOT NULL AUTO_INCREMENT,
  `Season_ID` INT NOT NULL,
  `Disc_Num` INT NOT NULL,
  `Description` VARCHAR(45) NULL,
  PRIMARY KEY (`DVD_Series_Split_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Cust_Online_Viewing_History`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Cust_Online_Viewing_History` (
  `Cust_ID` INT NOT NULL,
  `Movie_or_Ep_ID` INT NOT NULL,
  `is_it_movie` TINYINT(1) NOT NULL,
  `Cust_Online_Date_watched` DATE NOT NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Season`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Season` (
  `Season_ID` INT NOT NULL AUTO_INCREMENT,
  `Series_ID` INT NOT NULL,
  `Season_Num` INT NOT NULL,
  `Description` VARCHAR(255) NULL,
  PRIMARY KEY (`Season_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Plan` (
  `Plan_type` VARCHAR(20) NOT NULL,
  `Plan_Online_Quality` VARCHAR(20) NOT NULL,
  `Plan_Num_DVDs_at_once` INT NOT NULL,
  `Plan_Price` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`Plan_type`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Cust_Ratings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Cust_Ratings` (
  `Cust_ID` INT NOT NULL,
  `Movie_or_Ep_ID` INT NOT NULL,
  `is_it_movie` TINYINT(1) NOT NULL,
  `Cust_Rating` INT NULL,
  `Cust_Comment` VARCHAR(255) NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Episode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Episode` (
  `Ep_ID` INT NOT NULL AUTO_INCREMENT,
  `Season_ID` INT NOT NULL,
  `Ep_Num` INT NOT NULL,
  `Ep_Title` VARCHAR(100) NULL,
  `Ep_Description` VARCHAR(255) NULL,
  PRIMARY KEY (`Ep_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Payment_History`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Payment_History` (
  `Cust_ID` INT NOT NULL,
  `Plan_type` VARCHAR(20) NOT NULL,
  `Payment_Date` DATE NULL,
  `Payment_Amount` DECIMAL(5,2) NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Series` (
  `Series_ID` INT NOT NULL AUTO_INCREMENT,
  `Series_Title` VARCHAR(100) NOT NULL,
  `Series_Description` VARCHAR(300) NULL,
  PRIMARY KEY (`Series_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Person` (
  `Person_ID` INT NOT NULL,
  `Movie_or_Season_ID` INT NOT NULL,
  `is_it_movie` TINYINT(1) NOT NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newSchoolProj`.`Person_Info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `newSchoolProj`.`Person_Info` (
  `Person_ID` INT NOT NULL AUTO_INCREMENT,
  `Person_FName` VARCHAR(20) NOT NULL,
  `Person_LName` VARCHAR(20) NOT NULL,
  `Person_Occupation` VARCHAR(20) NULL,
  `Person_Description` VARCHAR(300) NULL,
  PRIMARY KEY (`Person_ID`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
