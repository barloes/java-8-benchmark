-- Create the initial table
-- Person with name and isHuman

CREATE TABLE IF NOT EXISTS `person` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `sysDelete` TINYINT(1) NOT NULL DEFAULT '0',
  `isHuman` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- table personHst
CREATE TABLE IF NOT EXISTS `personHistory` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `sysDelete` TINYINT(1) NOT NULL DEFAULT '0',
  `isHuman` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;