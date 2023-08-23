package com.java.springboot.test.SpringBootProjectTest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

@RestController
public class WeatherMap {
	
	private boolean Terminate = false;
	
	@Value("${Url}")
	private String Url;
	
	@GetMapping("/getWeatherData")
	public String getWeatherData(@RequestParam String date,@RequestParam int choice) 
	{
		if (Terminate) {
            return "Program terminated.";
        }
		
		 try {
	            if (choice == 0) {
	                Terminate = true;
	                return "Oops!..Terminating the program...";
	            }
		URL url = new URL(Url);
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("GET");
		
		int response_CODE = connect.getResponseCode();
		if (response_CODE == HttpURLConnection.HTTP_OK) {
			BufferedReader Reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			
			while ((line = Reader.readLine()) != null) {
                response.append(line);
            }
			
			Reader.close();
			JSONObject jsonResponse = new JSONObject(response.toString());
			
			JSONArray predict = jsonResponse.getJSONArray("list");
			for (int i = 0; i < predict.length(); i++) {
				JSONObject forecast = predict.getJSONObject(i);
                String predictdateTime = forecast.getString("dt_txt");

                if (predictdateTime.equals(date)) {
                    JSONObject main = forecast.getJSONObject("main");
                    JSONObject wind = forecast.getJSONObject("wind");
                    double temp = main.getDouble("temp");
                    double w_Speed = wind.getDouble("speed");
                    double Pressure = main.getDouble("pressure");

                    switch (choice) {
                    
                        case 1:
                            return "Temperature: " + temp + " K";
                        case 2:
                            return "Wind Speed: " + w_Speed + " m/s";
                        case 3:
                            return "Pressure: " + Pressure + " hPa";
                        default:
                            return "Invalid choice selected. Please!, select 1 for Temperature, select 2 for Wind-Speed, select 3 for Pressure, select 0 to Terminate";
                    }
                }
			}
			return "No data available for the entered date and time. Please select other ";
		} else {
            return "Failed to fetch data from API. Response code: " + response_CODE;
        }
		} catch(IOException e) {
			return "Error in fetching data from API: " + e.getMessage();
		}
	}
		 
		 @GetMapping("/terminate")
		public String exit() {
			Terminate = true;
			return "Program terminated...You selected 0";
		}
	}


