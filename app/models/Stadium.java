package models;

public class Stadium {

    private String name;
    private Location location;

    public Stadium(){
        
    }

    public Stadium(String name, Location location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Stadium [location=" + location + ", name=" + name + "]";
    }

}
