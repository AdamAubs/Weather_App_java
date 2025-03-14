import Scenes.TodaysWeather;
import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;

import java.util.ArrayList;

public class JavaFX extends Application {
	TextField temperature,weather;

	public static void main(String[] args) {

		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("I'm a professional Weather App!");

		TodaysWeather todaysWeather = new TodaysWeather();

		Scene scene = new Scene(todaysWeather.getLayout(), 700,700);

		primaryStage.setTitle("I'm a professional Weather App!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
