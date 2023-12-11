DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `age` INT,
  `prenom` varchar(255) NOT NULL,
  `profession` varchar(255) NOT NULL,
  `salaire` INT);