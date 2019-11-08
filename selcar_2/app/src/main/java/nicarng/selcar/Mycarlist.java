package nicarng.selcar;

public class Mycarlist {
    String carimage;
    String carname;
    String carnumber;
    String startdate;
    String enddate;
    String starttime;
    String endtime;
    String location;

    public String getCarimage(){return carimage;}

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getCarname() {
        return carname;
    }
    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCarnumber() {
        return carnumber;
    }
    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getStartdate() {
        return startdate;
    }
    public void setStartdate(String startdate){
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }
    public void setEnddate(String enddate){
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLocation(){return location;}
    public void setLocation(String location){this.location = location;}
                                        // db에 현재 위치(좌표 및 주차장 위치)
    public Mycarlist(String carimage,String carname, String carnumber, String startdate, String enddate, String starttime, String endtime,String location) {
        this.carimage = carimage;
        this.carname = carname;
        this.carnumber = carnumber;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
    }


}
