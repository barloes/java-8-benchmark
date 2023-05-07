-- Create the initial table
-- Person with name and isHuman

CREATE TABLE IF NOT EXISTS `person` (
  `id` binary(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)),
  `name` VARCHAR(255) NOT NULL,
  `sysDelete` TINYINT(1) NOT NULL DEFAULT '0',
  `isHuman` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- table personHst
CREATE TABLE IF NOT EXISTS `personHistory` (
  `id` binary(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)),
  `name` VARCHAR(255) NOT NULL,
  `sysDelete` TINYINT(1) NOT NULL DEFAULT '0',
  `isHuman` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;