SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema news
-- -----------------------------------------------------
DROP DATABASE IF EXISTS news;
CREATE SCHEMA IF NOT EXISTS `news` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `news` ;

-- -----------------------------------------------------
-- Table `news`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `news`.`role` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `news`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `news`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `login` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(60) NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_category1_idx` (`role_id` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  CONSTRAINT `fk_users_category1`
    FOREIGN KEY (`role_id`)
    REFERENCES `news`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `news`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `news`.`post` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128) NOT NULL,
  `creation_date` TIMESTAMP NULL,
  `last_update_date` TIMESTAMP NULL,
  `content` TEXT NULL,
  `ordering` INT NULL,
  `mark` TINYINT(1) NULL,
  `user_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_post_user1_idx` (`user_id` ASC),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC),
  CONSTRAINT `fk_post_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `news`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `news`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `news`.`tag` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `tag_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `tag_name_UNIQUE` (`tag_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `news`.`post_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `news`.`post_tag` (
  `post_id` INT UNSIGNED NOT NULL,
  `tag_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`post_id`, `tag_id`),
  INDEX `fk_articles_tags_articles1_idx` (`post_id` ASC),
  INDEX `fk_articles_tags_tags1_idx` (`tag_id` ASC),
  CONSTRAINT `fk_articles_tags_articles1`
    FOREIGN KEY (`post_id`)
    REFERENCES `news`.`post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_articles_tags_tags1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `news`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
