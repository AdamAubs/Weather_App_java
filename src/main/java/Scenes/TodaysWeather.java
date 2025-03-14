package Scenes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import weather.Period;
import weather.WeatherAPI;

import java.util.ArrayList;

public class TodaysWeather {
    private HBox navBar;
    private BorderPane borderPane;
    private TextField temperature;
    private TextField weather;
    private Button threeDayWeatherBtn;
    private Button todaysWeatherBtn;

    public TodaysWeather() {
        todaysWeatherBtn = new Button("Today's weather");
        threeDayWeatherBtn = new Button("Three Day weather");
        temperature = new TextField();
        weather = new TextField();
    }

    public void loadWeatherData() {
        ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
        if (forecast == null || forecast.isEmpty()) {
            throw new RuntimeException("Forecast did not load");
        }

        this.updateWeather(String.valueOf(forecast.get(0).temperature), forecast.get(0).shortForecast);
    }

    public void updateWeather(String temp, String forecast) {
        temperature.setText("Today's temperature: " + temp);
        weather.setText(forecast);
    }

    public BorderPane getLayout() {
        borderPane = new BorderPane();

//        Label topLabel = new Label("Top Header");
//        borderPane.setTop(topLabel);

        navBar = new HBox(todaysWeatherBtn, threeDayWeatherBtn);

        borderPane.setTop(navBar);


        return borderPane;
    }
}
