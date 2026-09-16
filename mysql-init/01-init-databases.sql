CREATE DATABASE IF NOT EXISTS sige_identidad CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_identidad_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_identidad.* TO 'sige_identidad_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_estudiantes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_estudiantes_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_estudiantes.* TO 'sige_estudiantes_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_academico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_academico_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_academico.* TO 'sige_academico_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_evaluaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_evaluaciones_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_evaluaciones.* TO 'sige_evaluaciones_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_anotaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_anotaciones_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_anotaciones.* TO 'sige_anotaciones_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_mensajeria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_mensajeria_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_mensajeria.* TO 'sige_mensajeria_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_comunicaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_comunicaciones_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_comunicaciones.* TO 'sige_comunicaciones_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_calendario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_calendario_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_calendario.* TO 'sige_calendario_user'@'%';

CREATE DATABASE IF NOT EXISTS sige_auditoria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sige_auditoria_user'@'%' IDENTIFIED BY 'CAMBIAR-password-local-dev';
GRANT ALL PRIVILEGES ON sige_auditoria.* TO 'sige_auditoria_user'@'%';

FLUSH PRIVILEGES;