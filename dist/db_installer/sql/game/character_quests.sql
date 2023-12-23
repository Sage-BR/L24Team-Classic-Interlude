DROP TABLE IF EXISTS `character_quests`;
CREATE TABLE IF NOT EXISTS `character_quests` (
  `charId` INT UNSIGNED NOT NULL DEFAULT 0,
  `name` VARCHAR(60) NOT NULL DEFAULT '',
  `var`  VARCHAR(20) NOT NULL DEFAULT '',
  `value` VARCHAR(255) ,
  PRIMARY KEY (`charId`,`name`,`var`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;