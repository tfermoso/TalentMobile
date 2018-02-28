package spinoffpyme.com.talentmobile.Model;

/**
 * Created by tomas on 28/02/2018.
 */

public class StationWeather {
    private double latitude;
    private double longitude;
    private double temperature;
    private String name;

    public StationWeather() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
