package com.epam.weather;

import com.epam.weather.exception.InvalidLocationException;
import com.epam.weather.service.WeatherService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class WeatherApplicationTests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeatherService weatherService;


    @Test
    public void getTemperatureNormal() throws Exception {

        try {
            Optional<Float> result = weatherService.getTemperature("10119", "04", "07");
            if (result.isPresent()) {
                logger.info("the temp is : " + result.get().toString());
            } else {
                logger.info("the temp is empty! ");
            }

        } catch (InvalidLocationException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void invalidLocation() throws Exception {

        try {
            Optional<Float> result = weatherService.getTemperature("10119", "04", "070");
            if (result.isPresent()) {
                logger.info("the temp is : " + result.get().toString());
            } else {
                logger.info("the temp is empty! ");
            }

        } catch (InvalidLocationException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Before
    public void init() {
        System.out.println("test begin !-----------------");
    }

    @After
    public void after() {
        System.out.println("test end !-----------------");
    }
}
