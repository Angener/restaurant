package epam.eremenko.restaurant.service.util;

public class UtilFactory {
    private static volatile UtilFactory instance;
    private final Encoder encoder = new Encoder();

    private UtilFactory() {
    }

    public static UtilFactory getInstance() {
        if (instance == null) {
            synchronized (UtilFactory.class) {
                if (instance == null) {
                    instance = new UtilFactory();
                }
            }
        }
        return instance;
    }

    public Encoder getENCODER() {
        return encoder;
    }

    public MenuValidator getMenuValidator() {
        return new MenuValidator();
    }

    public UserValidator userValidator() {
        return new UserValidator();
    }
}
