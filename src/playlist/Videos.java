package playlist;

/**
 *
 * @author corpix
 */
public class Videos<Path, Name, Format, Duration> {
    Path path = null;
    Name name = null;
    Format format = null;
    Duration duration = null;

    Videos(Path path, Name name, Format format, Duration duration) {
        this.path = path;
        this.name = name;
        this.format = format;
        this.duration = duration;
    }

    // Path
    public Path getPath() {
        return path;
    }

    public Videos setPath(Path path) {
        this.path = path;
        
        return this;
    }

    // Name
    public Name getName() {
        return name;
    }

    public Videos setName(Name name) {
        this.name = name;
        
        return this;
    }

    // Format
    public Format getFormat() {
        return format;
    }

    public Videos setFormat(Format format) {
        this.format = format;
        
        return this;
    }

    // Duration
    public Duration getDuration() {
        return duration;
    }

    public Videos setDuration(Duration duration) {
        this.duration = duration;
        
        return this;
    }

}