package lld.observerpattern.observersImpl;

import lld.observerpattern.WeatherObserver;
import lld.observerpattern.observable.WeatherObservable;

import java.util.ArrayList;
import java.util.List;

public class WeatherStationImpl implements WeatherObserver {
    private List<WeatherObservable> weatherObservers = new ArrayList<>();
    private double temperature;
    private double pressure;
    private double humidity;

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    @Override
    public void addObserver(WeatherObservable observer) {
        weatherObservers.add(observer);
    }

    @Override
    public void removeObserver(WeatherObservable observer) {
        weatherObservers.remove(observer);
    }

    @Override
    public void changeWeather(double temperature, double pressure, double humidity) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        notifyObserver();
    }

    @Override
    public void notifyObserver() {
        for (WeatherObservable observer : weatherObservers) {
            observer.notifyObservers(this.temperature, this.pressure, this.humidity);
        }
    }
}
