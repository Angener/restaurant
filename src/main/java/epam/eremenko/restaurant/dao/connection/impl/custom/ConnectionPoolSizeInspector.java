package epam.eremenko.restaurant.dao.connection.impl.custom;

import java.sql.SQLException;

public class ConnectionPoolSizeInspector extends Thread {

    @Override
    public void run() {
        try {
            if (CustomConnectionManager.getInstance().isActiveThreadsQuantityHasFelt()) {
                CustomConnectionManager.getInstance().terminateConnection();
            } else if (CustomConnectionManager.getInstance().isActiveThreadsCountsReachedThreshold()) {
                CustomConnectionManager.getInstance().changeConnectionPoolParameters();
            }
        } catch (SQLException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
