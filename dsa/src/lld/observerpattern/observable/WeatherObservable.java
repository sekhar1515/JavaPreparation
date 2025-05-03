package lld.observerpattern.observable;

public interface WeatherObservable {
    void notifyObservers(double humidity, double temperature, double pressure);
}
