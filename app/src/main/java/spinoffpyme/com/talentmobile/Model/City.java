package spinoffpyme.com.talentmobile.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomas on 28/02/2018.
 */

public class City {
    private String name;
    private String region;
    private CityBox cityBox;
    private Double longitude;
    private Double latitude;
    private List<StationWeather> listStations;


    public City() {
        listStations=new ArrayList<>();
        cityBox=new CityBox();
    }

    public String getName() {
        return name;
    }

    public List<StationWeather> getListStations() {
        return listStations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public CityBox getCityBox() {
        return cityBox;
    }

    public void setCityBox(CityBox cityBox) {
        this.cityBox = cityBox;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public class CityBox{
        private String north;
        private String south;
        private String east;
        private String west;

        @Override
        public String toString() {
            return "Area: "+north+","+south+","+east+","+west;
        }

        public String getNorth() {
            return north;
        }

        public void setNorth(String north) {
            this.north = north;
        }

        public String getSouth() {
            return south;
        }

        public void setSouth(String south) {
            this.south = south;
        }

        public String getEast() {
            return east;
        }

        public void setEast(String east) {
            this.east = east;
        }

        public String getWest() {
            return west;
        }

        public void setWest(String west) {
            this.west = west;
        }
    }
}
