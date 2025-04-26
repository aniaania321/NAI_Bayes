import java.util.Map;

public class Observation {
    public Map<String, String> attributes; // attribute name -> attribute value
    public String label; // class label ("Yes" / "No" / etc.)

    public Observation(Map<String, String> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Observation {" + "attributes=" + attributes + " | label=" + label + '}';
    }
}
