package lld.observerpattern;

import lld.observerpattern.observable.WeatherObservable;
import lld.observerpattern.observable.impl.MobilePhone;
import lld.observerpattern.observersImpl.WeatherStationImpl;

public class ObserverMainClass {
    public static void main(String[] args) {
        WeatherObserver weatherObserver = new WeatherStationImpl();
        WeatherObservable weatherObservable = new MobilePhone(weatherObserver);
        weatherObserver.changeWeather(12.3, 40.1, 23.6);
        weatherObserver.changeWeather(11, 41, 23);

    }
}
