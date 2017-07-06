@GrabConfig(systemClassLoader=true)
@Grab(group='org.mariadb.jdbc', module='mariadb-java-client', version='2.0.3')

import groovy.json.JsonSlurper
import groovy.transform.ToString
import groovy.sql.Sql
import org.mariadb.jdbc.Driver

final String API_KEY = System.getenv("OPEN_WEATHER_API")
final String DB_JDBC_URL = System.getenv("WEATHER_DB_JDBC_URL")
final String DB_USERNAME = System.getenv("WEATHER_DB_username")
final String DB_PASSWORD = System.getenv("WEATHER_DB_password")

class DBInfo {
  String jdbcUrl
  String username
  String password
}

class City {
  Long id
  String country
  String city
}

@ToString(includeNames=true)
class WeatherInfo {
  City city

  String weatherCondition
  String weatherDescription

  Integer temperature
  Integer pressure
  Integer humidity
  Integer windSpeed
  Integer visibility

  WeatherInfo(Object json) {
    weatherCondition = json.weather.main
    weatherDescription = json.weather.description
    temperature = json.main.temp
    pressure = json.main.pressure
    humidity = json.main.humidity
    windSpeed = json.wind.speed
    visibility = json.visibility
  }
}

WeatherInfo getWeather(String apiKey, City city) {
   String units = 'metric'

   def apiUrlString = "http://api.openweathermap.org/data/2.5/weather?q=${city.city},${city.country}&units=${units}&appid=${apiKey}"

   def apiUrl = new URL(apiUrlString)
   def weatherInfo = new WeatherInfo(new JsonSlurper().parseText(apiUrl.text))
   weatherInfo.city = city
   return weatherInfo
}

def persistWeatherInfo(DBInfo dbInfo, WeatherInfo weather) {
  println weather
  def sql = Sql.newInstance(dbInfo.jdbcUrl, dbInfo.username, dbInfo.password)
  sql.execute('INSERT INTO weather (created, city_id, weather, description, temperature, pressure, humidity, wind_speed, visibility) VALUES (now(), ?, ?, ?, ?, ?, ?, ?, ?)',
    [weather.city.id, weather.weatherCondition, weather.weatherDescription, weather.temperature, weather.pressure, weather.humidity, weather.windSpeed, weather.visibility]
  )
  sql.close()
}

List<City> getCities(DBInfo dbInfo) {
  def sql = Sql.newInstance(dbInfo.jdbcUrl, dbInfo.username, dbInfo.password)
  List<City> cities = []
  sql.eachRow('SELECT * FROM city') { row ->
       cities << new City(id: row.id, country: row.country, city: row.city)
   }
  sql.close()

  return cities
}

DBInfo dbInfo = new DBInfo(jdbcUrl: DB_JDBC_URL, username: DB_USERNAME, password: DB_PASSWORD)

getCities(dbInfo).each {
  persistWeatherInfo(dbInfo, getWeather(API_KEY, it))
}
