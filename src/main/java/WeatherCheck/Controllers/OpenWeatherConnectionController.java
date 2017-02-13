package WeatherCheck.controllers;

/**
 * Created by Kayvon on 12/18/2016.
 */

import WeatherCheck.Application;
import WeatherCheck.tools.Converter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.json.*;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;

@RestController
public class OpenWeatherConnectionController {

    static Logger log = Logger.getLogger(Application.class.getName());
    static Converter converter = new Converter();


    public class WeatherObject {
        private JSONObject JSONObject_coord;
        private Double result_lon;
        private Double result_lat;

        //"sys"
        private JSONObject JSONObject_sys;
        private String result_country;
        private int result_sunrise;
        private int result_sunset;

        //"weather"
        private String result_weather;
        private JSONArray JSONArray_weather;
        JSONObject JSONObject_weather;

        private String result_main;
        private String result_description;
        private String result_icon;

        //"base"
        private String result_base;
        //"main"

        private JSONObject JSONObject_main;
        private Double result_temp;
        private Double result_pressure;
        private Double result_humidity;
        private Double result_temp_min;
        private Double result_temp_max;

        //"wind"
        private JSONObject JSONObject_wind;
        private Double result_speed;
        private Double result_deg;
        private String result_wind;

        //"clouds"
        private JSONObject JSONObject_clouds;
        private int result_all;

        //"dt"
        private int result_dt;

        //"id"
        private int result_id;

        //"name"
        private String result_name;

        //"cod"
        private int result_cod;

        public WeatherObject(JSONObject jsonObject) throws Exception{
            //"coord"
            JSONObject_coord = jsonObject.getJSONObject("coord");
            result_lon = JSONObject_coord.getDouble("lon");
            result_lat = JSONObject_coord.getDouble("lat");

            //"sys"
            JSONObject_sys = jsonObject.getJSONObject("sys");
            result_country = JSONObject_sys.getString("country");
            result_sunrise = JSONObject_sys.getInt("sunrise");
            result_sunset = JSONObject_sys.getInt("sunset");

            //"weather"
            JSONArray_weather = jsonObject.getJSONArray("weather");
            if (JSONArray_weather.length() > 0) {
                JSONObject_weather = JSONArray_weather.getJSONObject(0);
                result_id = JSONObject_weather.getInt("id");
                result_main = JSONObject_weather.getString("main");
                result_description = JSONObject_weather.getString("description");
                result_icon = JSONObject_weather.getString("icon");

                result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: " + result_description + "\ticon: " + result_icon;
            } else {
                result_weather = "weather empty!";
            }

            //"base"
            result_base = jsonObject.getString("base");

            //"main"
            JSONObject_main = jsonObject.getJSONObject("main");
            result_temp = converter.kelvinToFahrenheit(JSONObject_main.getDouble("temp"));
            result_pressure = JSONObject_main.getDouble("pressure");
            result_humidity = JSONObject_main.getDouble("humidity");
            result_temp_min = converter.kelvinToFahrenheit(JSONObject_main.getDouble("temp_min"));
            result_temp_max = converter.kelvinToFahrenheit(JSONObject_main.getDouble("temp_max"));

            //"wind"
            JSONObject_wind = jsonObject.getJSONObject("wind");
            result_speed = JSONObject_wind.getDouble("speed");
            result_deg = JSONObject_wind.getDouble("deg");
            result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

            //"clouds"
            JSONObject_clouds = jsonObject.getJSONObject("clouds");
            result_all = JSONObject_clouds.getInt("all");

            //"dt"
            result_dt = jsonObject.getInt("dt");

            //"id"
            result_id = jsonObject.getInt("id");

            //"name"
            result_name = jsonObject.getString("name");

            //"cod"
            result_cod = jsonObject.getInt("cod");
        }

        @Override
        public String toString() {
            String result ="coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n" +
                    "sys\tcountry: " + result_country + "\tsunrise: " + result_sunrise + "\tsunset: " + result_sunset + "\n" +
                    result_weather + "\n" +
                    "base: " + result_base + "\n" +
                    "main\ttemp: " + result_temp + "\thumidity: " + result_humidity + "\tpressure: " + result_pressure + "\ttemp_min: " + result_temp_min + "F \ttemp_max: " + result_temp_min + "F \n" +
                    result_wind + "\n" +
                    "clouds\tall: " + result_all + "\n" +
                    "dt: " + result_dt + "\n" +
                    "id: " + result_id + "\n" +
                    "name: " + result_name + "\n" +
                    "cod: " + result_cod + "\n" +
                    "\n";
            return result;
        }

    }


    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private static String getForcastByCityCountry(String city, String country) throws Exception {
        String weatherAPI = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&appid=8286d438ec068a984763e6668772ac80";
        String json = readUrl(weatherAPI);
        log.info(weatherAPI);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    private static String getForcastByZipCountry(int zip, String country) throws Exception {
        String weatherAPI = "http://api.openweathermap.org/data/2.5/forecast?zip=" + zip + "," + country + "&appid=8286d438ec068a984763e6668772ac80";
        String json = readUrl(weatherAPI);
        log.info(weatherAPI);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }


    private WeatherObject getWeatherByCityCountry(String city, String country) throws Exception {
        String weatherAPI = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=8286d438ec068a984763e6668772ac80";
        String json = readUrl(weatherAPI);
        log.info(weatherAPI);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        JSONObject jsonObject = new JSONObject(prettyJsonString);
        WeatherObject weather = new WeatherObject(jsonObject);

        return weather;
    }

    private WeatherObject getWeatherByZipCountry(int zip, String country) throws Exception {
        String weatherAPI = "http://api.openweathermap.org/data/2.5/weather?zip=" + zip + "," + country + "&appid=8286d438ec068a984763e6668772ac80";
        String json = readUrl(weatherAPI);
        log.info(weatherAPI);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        JSONObject jsonObject = new JSONObject(prettyJsonString);
        WeatherObject weather = new WeatherObject(jsonObject);

        return weather;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Welcome to Weather Checker";
    }

    @RequestMapping(path = "/api/getForcast/{city}/{country}", method = RequestMethod.GET)
    public String ForcastDataByCityCountry(@PathVariable String city, @PathVariable String country) {
        if (country == null) {
            country = "US";
        }

        try {
            return getForcastByCityCountry(city,country);
        } catch (Exception e) {
            return "Location Not Found, Please be specific with location";
        }
    }


    @RequestMapping(path = "/api/getForcast/{zip}", method = RequestMethod.GET)
    public String ForcastDataByZipCountry(@PathVariable int zip) {
        try {
            return getForcastByZipCountry(zip, "us");
        } catch (Exception e) {
            return "Location not Found. Please be specific with location";
        }
    }


    @RequestMapping(path = "/api/getWeather/{city}/{country}", method = RequestMethod.GET)
    public String WeatherDataByCityCountry(@PathVariable String city, @PathVariable String country) {
        if (country == null) {
            country = "US";
        }

        try {
            WeatherObject weather = getWeatherByCityCountry(city, country);
            return weather.toString();
        } catch (Exception e) {
            return "Location Not Found, Please be specific with location";
        }
    }


    @RequestMapping(path = "/api/getWeather/{zip}", method = RequestMethod.GET)
    public String WeatherDataByZipCountry(@PathVariable int zip) {
        try {
            WeatherObject weather = getWeatherByZipCountry(zip, "us");
            return weather.toString();
        } catch (Exception e) {
            return "Location not Found. Please be specific with location";
        }
    }


}
