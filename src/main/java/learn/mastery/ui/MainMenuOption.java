package learn.mastery.ui;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATIONS(1, "View Reservations for Host"),
    MAKE_RESERVATION(2, "Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_RESERVATION(4, "Cancel a Reservation");

    private final int value;
    private final String label;

    MainMenuOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.value == value) {
                return option;
            }
        }
        return null;
    }
}
