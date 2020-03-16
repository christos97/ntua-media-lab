package Model.Entities;

public class Data {
    String categ;
    String city;
    int type;
    int price;
    String suf;
    String[] services ;
    Double[] pricing ;
    double total_fee;

    public Data (){
        total_fee =0.0;
    }
    public Data(String category,String _city, int flightType,int _price ,String suffix, String[] serv, Double[] pric){
        city=_city;
        categ = category;
        type = flightType;
        suf = suffix;
        services = serv;
        pricing = pric;
        price = _price;
       total_fee=0.0;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public Double[] getPricing() {
        return pricing;
    }

    public void setPricing(Double[] pricing) {
        this.pricing = pricing;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSuf() {
        return suf;
    }

    public void setSuf(String suf) {
        this.suf = suf;
    }
    @Override
    public String toString() {
        return getCateg()+","+getCity()+","+getType()+","+getSuf()+","+getPricing()+","+getServices()+","+getPrice()+","+getTotal_fee()+"\n";
    }
}
