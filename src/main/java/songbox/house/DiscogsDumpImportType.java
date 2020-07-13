package songbox.house;

public enum DiscogsDumpImportType {
    RELEASES, MASTERS, LABELS, ARTISTS;

    public String getType() {
        return name().toLowerCase();
    }
}
