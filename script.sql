DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `teams`;


CREATE TABLE IF NOT EXISTS `teams` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `students` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(20) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `gender` enum('M','F','A') NOT NULL,
  `last_place` varchar(50) NOT NULL,
  `last_formation` varchar(50) NOT NULL,
  `team_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY `team_id` (`team_id`)
);