package com.epam.weather.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface WeatherService {

    Optional<Float> getTemperature(String province, String city, String county) throws Exception;
}
