package Scenes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import weather.Period;
import weather.WeatherAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodaysWeather {
    private BorderPane borderPane;
    // Declare the navbar objects
    private HBox navBar;
    private Button threeDayWeatherBtn;
    private Button todaysWeatherBtn;

    // Declare vbox object for vertical alignment of main content
    private VBox mainContent;

    // Declare hbox object for horizontal alignment of first row of main content
    // Title, Today's Weather image, switch units toggle button
    private HBox titleLine;
    private Label todaysWeatherContentTitle;
    private Image iconImage;
    private ImageView weatherIconImageView;
    private ToggleButton switchUnitsBtn;

    // Declare first row of content objects (Today's Temperature)
    private HBox temperatureLine;
    private Label temperatureText;
    private Label fahrenheitText;
    private Label celsiusText;

    // Declare the second row of content objects (Today's wind speed)
    private HBox windSpeedLine;
    private Label windSpeedText;
    private Label mphWindSpeedText;
    private Label kphWindSpeedText;

    // Declare third row of content objects (Today's Weather description)
    private Label weatherDescriptionText;

    // Declare fourth row of content object (Today's Weather precipitation probability)
    private Label precipitationProbabilityText;

    // Construct today's weather scene layout
    public TodaysWeather() {
        todaysWeatherBtn = new Button("Today's weather");
        threeDayWeatherBtn = new Button("Three Day weather");

        todaysWeatherContentTitle = new Label("Today's Weather");
        switchUnitsBtn = new ToggleButton();

        temperatureText = new Label();
        fahrenheitText = new Label();
        celsiusText = new Label();

        windSpeedText = new Label();
        mphWindSpeedText = new Label();
        kphWindSpeedText = new Label();

        weatherDescriptionText = new Label();

        precipitationProbabilityText = new Label();
    }

    // Retrieves the weather data from the WeatherAPI.
    // Calls the updateWeather function to update
    // the weather data objects.
    public void loadWeatherData() {
        ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
        if (forecast == null || forecast.isEmpty()) {
            throw new RuntimeException("Forecast did not load");
        }

        this.updateWeather(forecast.get(0).temperature, forecast.get(0).icon, forecast.get(0).detailedForecast, forecast.get(0).windSpeed, String.valueOf(forecast.get(0).probabilityOfPrecipitation.value));
    }

    // Converts the mph wind speed range to kph wind speed range
    // Takes in the mph wind speed range (e.g "20 to 30 mph")
    // Returns an ArrayList of the kph range as integers (e.g [20, 30]);
    public ArrayList<Integer> convertMphToKphRange(String mphWindSpeedRange) {
        // match one or more digits (0-9)
        Pattern pattern = Pattern.compile("\\d+");
        // scans mphWindSpeedRange string for occurrences of pattern
        Matcher matcher = pattern.matcher(mphWindSpeedRange);

        // Store the converted km/h in an array list
        ArrayList<Integer> speedsInKph = new ArrayList<>();

        // Moves to the next match in the string (if any exists)
        while (matcher.find()) {
            // Extracts numbers and parses them as ints
            int mph = Integer.parseInt(matcher.group());
            // Converts to km/h
            int kph = (int) Math.round(mph * 1.60934);
            speedsInKph.add(kph);
        }

        return speedsInKph;
    }

    // Updates the weather data objects which represent the main content on the today's weather scene.
    public void updateWeather(Integer tempFahrenheit, String icon, String detailedForecast, String mphWindSpeedRange, String precipitationProb) {
        // Dynamically create the icon Image
        iconImage = new Image(icon);
        weatherIconImageView = new ImageView(iconImage);
        // Create the toggle units button
        switchUnitsBtn = new ToggleButton("Toggle Units");

        // Set the temperature text
        temperatureText.setText("Temperature: ");

        // Initially disable showing the Celsius text
        Integer tempCelsius = (tempFahrenheit - 32) * 5 / 9;
        String tempCelsiusText = String.valueOf(tempCelsius);
        celsiusText.setText(tempCelsiusText + "°C");
        celsiusText.setDisable(true);

        // Initially show the Fahrenheit units
        String tempFahrenheitText = String.valueOf(tempFahrenheit);
        fahrenheitText.setText(tempFahrenheitText + "°F ");

        // Set the wind speed text
        windSpeedText.setText("Wind speed: ");

        // Since speed is passed in as range (e.g "20 to 30 mph")
        // we need to first parse the string in order to convert to kph
        ArrayList<Integer> convertedSpeedRange = convertMphToKphRange(mphWindSpeedRange);

        String kphWindSpeed = "";
        if (convertedSpeedRange.size() == 2) {
            kphWindSpeed = String.valueOf(convertedSpeedRange.get(0)) + " to " + String.valueOf(convertedSpeedRange.get(1));
        } else {
            kphWindSpeed = String.valueOf(convertedSpeedRange.get(0));
        }

        // Initially show the mph units
        mphWindSpeedText.setText(mphWindSpeedRange + " ");

        // Initially disable showing the km/h speed
        kphWindSpeedText.setText(kphWindSpeed + "km/h");
        kphWindSpeedText.setDisable(true);

        // Toggle the units when switch units button is clicked
        switchUnitsBtn.setOnAction(e -> {
            if (switchUnitsBtn.isSelected()) {
                // Disable fahrenheit
                fahrenheitText.setDisable(true);
                // Enable celsius
                celsiusText.setDisable(false);

                // Disable mph
                mphWindSpeedText.setDisable(true);
                // Enable kmp
                kphWindSpeedText.setDisable(false);
            } else {
                // Disable celsius
                celsiusText.setDisable(true);
                // Enable fahrenheit
                fahrenheitText.setDisable(false);

                // Disable kph
                kphWindSpeedText.setDisable(true);
                // Enable mph
                mphWindSpeedText.setDisable(false);
            }
        });

        // Set the description for today's weather
        weatherDescriptionText.setText("Description: " + detailedForecast);

        // Set the precipitation for today's weather
        precipitationProbabilityText.setText("Precipitation: " + precipitationProb + "%");
    }

    public BorderPane getLayout() {
        borderPane = new BorderPane();
        loadWeatherData();

        navBar = new HBox(todaysWeatherBtn, threeDayWeatherBtn, switchUnitsBtn);
        borderPane.setTop(navBar);

        titleLine = new HBox(todaysWeatherContentTitle, weatherIconImageView, switchUnitsBtn);
        temperatureLine = new HBox(temperatureText, fahrenheitText, celsiusText);
        windSpeedLine = new HBox(windSpeedText, mphWindSpeedText, kphWindSpeedText);

        mainContent = new VBox(titleLine, temperatureLine, windSpeedLine, weatherDescriptionText, precipitationProbabilityText);
        borderPane.setCenter(mainContent);

        return borderPane;
    }
}
