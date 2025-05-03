package lld.observerpattern;

import lld.observerpattern.observable.WeatherObservable;

public interface WeatherObserver {
    void addObserver(WeatherObservable observer);

    void removeObserver(WeatherObservable observer);

    void changeWeather(double temperature, double pressure, double humidity);

    void notifyObserver();

}
