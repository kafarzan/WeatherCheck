package WeatherCheck.tools;

import java.text.DecimalFormat;

public class Converter {
    public Converter() {

    }
    private static DecimalFormat df = new DecimalFormat("#.#");
    // Method to convert from degrees Celcius to degrees Fahrenheit
    public static double celciusToFahrenheit(double degCelcius) {
        double degFahrenheit;
        degFahrenheit = degCelcius * 9 / 5 + 32;
        return degFahrenheit;
    }

    // Method to convert from degrees Fahrenheit to degrees Celcius
    public static double fahrenheitToCelcius(double degFahrenheit) {
        double degCelcius;
        degCelcius = (degFahrenheit - 32) * 5 / 9;
        return degCelcius;
    }

    // Method to convert from degrees Celcius to degrees Kelvin
    public static double celciusToKelvin(double degCelcius) {
        double degKelvin;
        degKelvin = degCelcius + 273.15f;
        return degKelvin;
    }

    // Method to convert from degrees Kelvin to degrees Celcius
    public static double kelvinToCelcius(double degKelvin) {
        double degCelcius;
        degCelcius = degKelvin - 273.15f;
        return degCelcius;
    }

    public static double  kelvinToFahrenheit(double degKelvin) {
        double degCelcius = kelvinToCelcius(degKelvin);
        double degFahrenheit = celciusToFahrenheit(degCelcius);
        return Double.parseDouble(df.format(degFahrenheit));
    }

}