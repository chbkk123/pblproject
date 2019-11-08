package nicarng.selcar;

public class Spinner {

    String DbCarName;
    String DbCarType;

    public String getDbCarName() {
        return DbCarName;
    }
    public void setDbCarName(String DbCarName) {
        this.DbCarName = DbCarName;
    }

    public String getDbCarType(){return DbCarType;}
    public void setDbCarType(String DbCarType){ this.DbCarType = DbCarType; }


    public Spinner(String DbCarName,String DbCarType) {
        this.DbCarName = DbCarName;
        this.DbCarType = DbCarType;
        }
}
