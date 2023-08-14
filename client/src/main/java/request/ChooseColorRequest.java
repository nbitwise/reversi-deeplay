package request;

public class ChooseColorRequest implements Request {
    public final String command = "chooseColor";
    public String color;

    public ChooseColorRequest(String color) {
        this.color = color;
    }
}
