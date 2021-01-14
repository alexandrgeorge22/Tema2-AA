package Graph;

public class Vertex {
    private Integer label;

    public Vertex(Integer label) {
        this.label = label;
    }

    public Integer getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "label=" + label +
                '}';
    }
}
