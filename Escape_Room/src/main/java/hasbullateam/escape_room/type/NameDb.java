package hasbullateam.escape_room.type;

public enum NameDb {
    PINGPONG("statsPingPong"),TRIS("statsTris"),MORRACINESE("statsMorraCinese"),USER("User");
    private String name;

    NameDb(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
