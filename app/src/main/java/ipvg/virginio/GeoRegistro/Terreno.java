package ipvg.virginio.GeoRegistro;

public class Terreno {
    private String title;
    private String description;
    private String category;
    private String date;
    private String latitude;
    private String longitude;

    public Terreno(String title, String description, String category, String date, String latitude, String longitude){
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
    public String getDate() { return date; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
