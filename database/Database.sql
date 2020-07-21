SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema restaurant
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `restaurant` DEFAULT CHARACTER SET utf8 ;
USE `restaurant` ;

-- -----------------------------------------------------
-- Table `restaurant`.`menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `restaurant`.`menu` (
  `dish_id` INT(11) NOT NULL AUTO_INCREMENT,
  `dish_name` VARCHAR(100) NOT NULL,
  `category` VARCHAR(100) NULL DEFAULT NULL,
  `description` VARCHAR(500) NOT NULL,
  `price` DOUBLE NOT NULL,
  PRIMARY KEY (`dish_id`),
  UNIQUE INDEX `dishName_UNIQUE` (`dish_name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `restaurant`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `restaurant`.`image` (
  `image_id` INT(11) NOT NULL AUTO_INCREMENT,
  `image_name` VARCHAR(50) NOT NULL,
  `image_path` VARCHAR(200) NOT NULL,
  `dish_id` INT(11) NOT NULL,
  PRIMARY KEY (`image_id`),
  UNIQUE INDEX `name_UNIQUE` (`image_name` ASC) VISIBLE,
  UNIQUE INDEX `path_UNIQUE` (`image_path` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`image_id` ASC) VISIBLE,
  INDEX `fk_image_menu1_idx` (`dish_id` ASC) VISIBLE,
  CONSTRAINT `fk_image_menu1`
    FOREIGN KEY (`dish_id`)
    REFERENCES `restaurant`.`menu` (`dish_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `restaurant`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `restaurant`.`user` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `role` ENUM('customer', 'admin') NOT NULL DEFAULT 'customer',
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `mobile` VARCHAR(20) NOT NULL,
  `registered` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `userId_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `mobile_UNIQUE` (`mobile` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 23
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `restaurant`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `restaurant`.`order` (
  `order_id` INT(11) NOT NULL AUTO_INCREMENT,
  `total_amount` FLOAT NOT NULL,
  `is_approved` TINYINT(4) NOT NULL DEFAULT '0',
  `is_passed` TINYINT(4) NOT NULL DEFAULT '0',
  `is_cooked` TINYINT(4) NOT NULL DEFAULT '0',
  `is_billed` TINYINT(4) NOT NULL DEFAULT '0',
  `is_paid` TINYINT(4) NOT NULL DEFAULT '0',
  `ordered` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_'order'_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_'order'_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `restaurant`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `restaurant`.`order_has_menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `restaurant`.`order_has_menu` (
  `order_id` INT(11) NOT NULL,
  `dish_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `amount` DOUBLE NOT NULL,
  PRIMARY KEY (`order_id`, `dish_id`),
  INDEX `fk_order_has_menu_menu1_idx` (`dish_id` ASC) VISIBLE,
  INDEX `fk_order_has_menu_order1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_has_menu_menu1`
    FOREIGN KEY (`dish_id`)
    REFERENCES `restaurant`.`menu` (`dish_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_order_has_menu_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `restaurant`.`order` (`order_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
