package nicarng.selcar;

import java.io.Serializable;

public class Dbcarlist implements Serializable {
    String car_image;
    String car_name;
    String car_km;
    String car_year;
    String car_location;
    String car_available;
    String add_id;

    public String getCar_image(){return car_image;}
    public void setCar_image(String car_image){this.car_image=car_image;}

    public String getCar_name() {
        return car_name;
    }
    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_km() {
        return car_km;
    }
    public void setCar_km(String car_km) {
        this.car_km = car_km;
    }

    public String getCar_year() {
        return car_year;
    }
    public void setCar_year(String car_year) {
        this.car_year = car_year;
    }

    public String getCar_location(){return car_location;}
    private void setCar_location(String car_location){this.car_location = car_location;}

    public String getCar_available(){return car_available;}
    public void setCar_available(String car_available){this.car_available = car_available;}

    public String getAdd_id(){return add_id;}
    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public Dbcarlist(String car_image, String car_name, String car_km, String car_year,String car_location, String car_available,String add_id){
        this.car_image = car_image;
        this.car_name = car_name;
        this.car_km = car_km;
        this.car_year = car_year;
        this.car_location = car_location;
        this.car_available = car_available;
        this.add_id = add_id;
    }


}
