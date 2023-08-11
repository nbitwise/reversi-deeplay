package io.deeplay.Aplication;

public class ChooseColorRequest implements Request {
    String command = "chooseColor";
    String color;

    ChooseColorRequest(String color) {
        this.color = color;
    }
}
