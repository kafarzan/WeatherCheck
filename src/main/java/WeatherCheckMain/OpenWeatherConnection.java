package WeatherCheckMain;

/**
 * Created by Kayvon on 6/14/2016.
 */

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
import java.util.List;

public class OpenWeatherConnection {

    static class Lists {
        String dt;
        Main info;
        String description;
    }

    static class Main{
        String temp;
        String temp_min;
        String temp_max;
    }

    static class Rain{
        String threeHour;
    }

    static class Page {
        String title;
        String link;
        String description;
        String language;
    }


    public static String readUrl(String urlString) throws Exception{
        BufferedReader reader = null;
        try{
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if(reader != null)
                reader.close();
        }
    }

    public static String parseJson(String city, String country) throws Exception {
        String json = readUrl("http://api.openweathermap.org/data/2.5/forecast?q="+city+country+",us&appid=8286d438ec068a984763e6668772ac80");

        Gson gson = new Gson();

        Page page = gson.fromJson(json, Page.class);

        System.out.println();

        return json;
    }

    private String callJson() throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

// read script file
        engine.eval(Files.newBufferedReader(Paths.get("C:/Users/Kayvon/IdeaProjects/RainProof/resources/js/getWeatherJson.js"), StandardCharsets.UTF_8));

        Invocable inv = (Invocable) engine;
// call function from script file

        Object test = inv.invokeFunction("test");
        return test.toString();

    }



}