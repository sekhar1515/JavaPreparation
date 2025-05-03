package lld.observerpattern.observable.impl;

import lld.observerpattern.WeatherObserver;
import lld.observerpattern.observable.WeatherObservable;

public class MobilePhone implements WeatherObservable {

    private double humidity;
    private double temperature;
    private double pressure;

    public MobilePhone(WeatherObserver weatherObserver) {
        weatherObserver.addObserver(this);
    }

    @Override
    public void notifyObservers(double humidity, double temperature, double pressure) {
        System.out.println("weather now is : " + humidity + "," + temperature + "," + pressure);
    }
}
