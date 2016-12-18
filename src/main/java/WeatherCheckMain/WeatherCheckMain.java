package WeatherCheckMain;

/**
 * Created by Kayvon on 6/14/2016.
 */

public class WeatherCheckMain {

    public static void main(String [] args) throws Exception{

        OpenWeatherConnection test = new OpenWeatherConnection();
        System.out.println(test.parseJson("New_York_City", "US"));

    }
}
