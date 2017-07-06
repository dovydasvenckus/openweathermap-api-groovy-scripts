CREATE TABLE weather(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country VARCHAR(10),
  city VARCHAR(256),
  weather VARCHAR(128),
  description VARCHAR(256),
  temperature TINYINT,
  presure     SMALLINT,
  humidity    TINYINT,
  wind_speed  SMALLINT,
  visibility  SMALLINT
);