CREATE TABLE `image_index` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`image_name` TEXT NOT NULL COLLATE 'utf8mb4_bin',
	`added_date` DATETIME NOT NULL,
	`updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`image_keywords` TEXT NOT NULL COLLATE 'utf8mb4_bin',
	`image_type` CHAR(9) NOT NULL COLLATE 'utf8mb4_bin',
	`image_path` TEXT NOT NULL COLLATE 'utf8mb4_bin',
	`image_thump_path` TEXT NOT NULL COLLATE 'utf8mb4_bin',
	INDEX `id` (`id`)
)
COLLATE='utf8mb4_bin'
ENGINE=InnoDB
AUTO_INCREMENT=2
;
