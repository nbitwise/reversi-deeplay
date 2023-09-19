package client;

public enum ViewAndCommand {

    REGISTRATION("REGISTRATION"),
    AUTHORIZATION("AUTHORIZATION"),
    VIEWROOMS("VIEWROOMS"),
    CREATEROOM("CREATEROOM"),

    CONNECTTOROOM("CONNECTTOROOM"),

    WHEREICANGORESPONSE("WHEREICANGORESPONSE"),

    LEAVEROOM("LEAVEROOM"),

    GAMEOVER("GAMEOVER"),
    MAKEMOVE("MAKEMOVE"),

    STARTGAME("STARTGAME"),

    EXIT("EXIT"),

    SURRENDER("SURRENDER"),

    GUI("GUI");
    public final String commandName;

    ViewAndCommand(String nameCommand) {
        this.commandName = nameCommand;
    }
}
