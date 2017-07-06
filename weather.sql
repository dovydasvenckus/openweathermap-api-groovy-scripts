CREATE TABLE city(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country VARCHAR(10),
  city VARCHAR(256)
);

CREATE TABLE weather(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  created TIMESTAMP,
  city_id BIGINT,
  weather VARCHAR(128),
  description VARCHAR(256),
  temperature TINYINT,
  pressure     SMALLINT,
  humidity    TINYINT,
  wind_speed  SMALLINT,
  visibility  SMALLINT,

  FOREIGN KEY (city_id) REFERENCES city(id)

);
