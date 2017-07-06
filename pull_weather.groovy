import groovy.json.JsonSlurper

String API_KEY = 'YOUR API KEY FROM OPEN WEATHER MAP'

class City{
  String country
  String city
}

def getWeather(String apiKey, City city) {
  String units = 'metric'

  def apiUrlString = "http://api.openweathermap.org/data/2.5/weather?q=${city.city},${city.country}&units=${units}&appid=${apiKey}"

  def apiUrl = new URL(apiUrlString)
  return new JsonSlurper().parseText(apiUrl.text)
}

City vilnius = new City(country: 'LT', city: 'Vilnius')

println getWeather(API_KEY, vilnius)
