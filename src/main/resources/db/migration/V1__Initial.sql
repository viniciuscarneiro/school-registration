-- school_registration_db.course_entity definition

CREATE TABLE `course_entity` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `created_at` datetime DEFAULT NULL,
                                 `updated_at` datetime DEFAULT NULL,
                                 `description` varchar(255) DEFAULT NULL,
                                 `name` varchar(255) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- school_registration_db.student_entity definition

CREATE TABLE `student_entity` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `created_at` datetime DEFAULT NULL,
                                  `updated_at` datetime DEFAULT NULL,
                                  `email` varchar(255) DEFAULT NULL,
                                  `full_name` varchar(255) DEFAULT NULL,
                                  `identification_document` varchar(255) DEFAULT NULL,
                                  `phone_number` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `identification_document_unique_constraint` (`identification_document`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- school_registration_db.student_course_entity definition

CREATE TABLE `student_course_entity` (
                                         `fk_student` bigint NOT NULL,
                                         `fk_course` bigint NOT NULL,
                                         PRIMARY KEY (`fk_course`,`fk_student`),
                                         KEY `FKjtcykwqqr9xo74hr1m5w6p41t` (`fk_student`),
                                         CONSTRAINT `FKjtcykwqqr9xo74hr1m5w6p41t` FOREIGN KEY (`fk_student`) REFERENCES `student_entity` (`id`),
                                         CONSTRAINT `FKlksx07ckydy5697h9gstmtwl6` FOREIGN KEY (`fk_course`) REFERENCES `course_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;