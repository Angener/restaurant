package epam.eremenko.restaurant.dao.connection.impl.custom;

import epam.eremenko.restaurant.dao.exception.ConnectionPoolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CustomConnectionManager {
    private static CustomConnectionManager instance;
    private String url;
    private String user;
    private String password;
    private String useUnicode;
    private String characterEncoding;
    private int minPoolSize;
    private int maxPoolSize;
    private int connectThresholdPerConnection;
    private int initialPoolSize;
    private int acquireIncrement;
    private int existsPoolSize;
    private int permissibleLoadPerConnection;
    private BlockingQueue<CustomConnection> pool;

    public Connection getConnection() {
        Connection connection = takeConnectionFromPool();
        new Thread(new ConnectionPoolSizeInspector()).start();
        return connection;
    }

    static CustomConnectionManager getInstance() {
        if (instance == null) {
            synchronized (CustomConnectionManager.class) {
                if (instance == null) {
                    instance = new CustomConnectionManager();
                }
            }
        }
        return instance;
    }

    void init(Properties properties) throws SQLException {
        setProperties(properties);
        calculatePermissionLoadPerConnection();
        setExistsPoolSize(initialPoolSize);
        setPool(new ArrayBlockingQueue<>(maxPoolSize));
        createConnections(initialPoolSize);
    }

    private void setProperties(Properties properties) {
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
        this.useUnicode = properties.getProperty("useUnicode");
        this.characterEncoding = properties.getProperty("characterEncoding");
        this.initialPoolSize = Integer.parseInt(properties.getProperty("initialPoolSize"));
        this.minPoolSize = Integer.parseInt(properties.getProperty("minPoolSize"));
        this.maxPoolSize = Integer.parseInt(properties.getProperty("maxPoolSize"));
        this.connectThresholdPerConnection = Integer.parseInt(properties.getProperty("threshold"));
        this.acquireIncrement = Integer.parseInt(properties.getProperty("acquireIncrement"));
    }

    private void calculatePermissionLoadPerConnection() {
        permissibleLoadPerConnection = connectThresholdPerConnection * initialPoolSize;
    }

    void releaseConnection(CustomConnection connection) {
        try {
            pool.put(connection);
            new Thread(new ConnectionPoolSizeInspector()).start();
        } catch (InterruptedException ex) {
            throw new ConnectionPoolException("Connection can't back to the pool", ex);
        }
    }

    private Connection takeConnectionFromPool() {
        Connection connection;
        try {
            connection = pool.take();
        } catch (InterruptedException ex) {
            throw new ConnectionPoolException("Connection taking failed.", ex);
        }
        return connection;
    }

    private void createConnections(int quantity) throws SQLException {
        for (int i = 0; i < quantity; i++) {
            Connection connection =
                    DriverManager.getConnection(String.join("", url,
                            "?user=", user,
                            "&password=", password,
                            "&useUnicode=", useUnicode,
                            "&characterEncoding=", characterEncoding));
            pool.offer(new CustomConnection(connection));
        }
    }

    boolean isActiveThreadsQuantityHasFelt() {
        if (isActiveThreadsCountIsLessThenConnectionLoadForThreshold()) {
            return isPoolSizeIsMoreThenMinimalPoolSize();
        }
        return false;
    }

    private boolean isActiveThreadsCountIsLessThenConnectionLoadForThreshold() {
        return ((permissibleLoadPerConnection) > Thread.activeCount());
    }

    private boolean isPoolSizeIsMoreThenMinimalPoolSize() {
        return (existsPoolSize - acquireIncrement) >= minPoolSize;
    }

    synchronized void terminateConnection() throws SQLException, InterruptedException {
        if (isPoolSizeIsMoreThenMinimalPoolSize()) {
            pool.take().terminate();
            decreaseActiveConnectionCounterAndThreshold();
        }
    }

    private void decreaseActiveConnectionCounterAndThreshold() {
        permissibleLoadPerConnection -= connectThresholdPerConnection;
        existsPoolSize--;
    }

    boolean isActiveThreadsCountsReachedThreshold() {
        return (permissibleLoadPerConnection) < Thread.activeCount();
    }

    synchronized void changeConnectionPoolParameters() throws SQLException {
        if (isActiveThreadsCountsReachedThreshold()) {
            if (isConnectionIncreasingWontExceedMaxPoolSize()) {
                createConnections(acquireIncrement);
                increaseActiveConnectionsCounterAndThreshold();
            }
        }
    }

    private boolean isConnectionIncreasingWontExceedMaxPoolSize() {
        return existsPoolSize + acquireIncrement <= maxPoolSize;
    }

    private void increaseActiveConnectionsCounterAndThreshold() {
        permissibleLoadPerConnection += connectThresholdPerConnection;
        existsPoolSize++;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUseUnicode() {
        return useUnicode;
    }

    public void setUseUnicode(String useUnicode) {
        this.useUnicode = useUnicode;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public BlockingQueue<CustomConnection> getPool() {
        return pool;
    }

    public void setPool(BlockingQueue<CustomConnection> pool) {
        this.pool = pool;
    }

    public int getExistsPoolSize() {
        return existsPoolSize;
    }

    public void setExistsPoolSize(int existsPoolSize) {
        this.existsPoolSize = existsPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getConnectThresholdPerConnection() {
        return connectThresholdPerConnection;
    }

    public void setConnectThresholdPerConnection(int connectThresholdPerConnection) {
        this.connectThresholdPerConnection = connectThresholdPerConnection;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public int getAcquireIncrement() {
        return acquireIncrement;
    }

    public void setAcquireIncrement(int acquireIncrement) {
        this.acquireIncrement = acquireIncrement;
    }

    public int getPermissibleLoadPerConnection() {
        return permissibleLoadPerConnection;
    }

    public void setPermissibleLoadPerConnection(int permissibleLoadPerConnection) {
        this.permissibleLoadPerConnection = permissibleLoadPerConnection;
    }
}
