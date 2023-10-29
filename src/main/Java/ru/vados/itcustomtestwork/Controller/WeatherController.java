package ru.vados.itcustomtestwork.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vados.itcustomtestwork.Dto.WeatherItem;
import ru.vados.itcustomtestwork.Dto.WeatherReq;
import ru.vados.itcustomtestwork.Service.WeatherService;

@RestController
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("getWeather")
    public WeatherItem getWeather(@RequestBody WeatherReq req){
        if(req.date == null){
           return weatherService.getWeatherByCity(req.getCity());
        }else {
            return weatherService.getWeatherByCityAndDate(req.getCity(), req.getDate().toInstant());
        }
    }


}
