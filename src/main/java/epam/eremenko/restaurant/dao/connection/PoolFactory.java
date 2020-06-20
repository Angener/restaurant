package epam.eremenko.restaurant.dao.connection;

import epam.eremenko.restaurant.dao.connection.impl.c3po.C3p0;
import epam.eremenko.restaurant.dao.connection.impl.custom.CustomPool;

public final class PoolFactory {
    private static PoolFactory instance;
    private final C3p0 c3po = new C3p0();
    private final CustomPool custom = new CustomPool();

    private PoolFactory() {
    }

    public static PoolFactory getInstance() {
        if (instance == null) {
            synchronized (PoolFactory.class) {
                if (instance == null) {
                    instance = new PoolFactory();
                }
            }
        }
        return instance;
    }

    public ConnectionPool getC3po() {
        return c3po;
    }

    public ConnectionPool getCustom() {
        return custom;
    }
}