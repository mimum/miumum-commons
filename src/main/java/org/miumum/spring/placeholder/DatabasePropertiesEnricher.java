package org.miumum.spring.placeholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The Database loading implementation of {@link PropertiesEnricher}
 * <p/>
 * This implementation can load the properties from some table in database if property loading fails no exception is thrown
 *
 * @author Roman Valina
 */
public class DatabasePropertiesEnricher implements PropertiesEnricher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String driverClassKey;

    private String jdbcUrlKey;

    private String userKey;

    private String passwordKey;

    private String nodeIdKey;

    private String propertyTableName;

    private String keyColumnName;

    private String valueColumnName;

    private String nodeIdColumnName;

    private DataSource dataSource;

    /**
     * Add some new properties to the current properties in {@link EnrichingPropertyPlaceholderConfigurer}
     * If the property, that is going to be added, exist in current properties, the value of the property is not overridden (new value is ignored)
     *
     * @param properties current loaded properties
     */
    @Override
    public void enrichProperties(Properties properties) {
        Connection con = null;
        try {
            if (dataSource == null) {
                SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
                dataSource.setDriverClassName(getValueFromProperty(driverClassKey, properties));
                dataSource.setUrl(getValueFromProperty(jdbcUrlKey, properties));
                dataSource.setUsername(getValueFromProperty(userKey, properties));
                dataSource.setPassword(getValueFromProperty(passwordKey, properties));
                this.dataSource = dataSource;
            }
            Assert.notNull(propertyTableName, "The propertyTableName must not be null.");
            Assert.notNull(keyColumnName, "The keyColumnName must not be null.");
            Assert.notNull(valueColumnName, "The valueColumnName must not be null.");

            String baseSelect = "SELECT " + keyColumnName + "," + valueColumnName + " FROM " + propertyTableName;
            con = dataSource.getConnection();
            if (nodeIdColumnName != null && nodeIdColumnName.length() > 0) {
                String nodeId = properties.getProperty(nodeIdKey);
                String nodeIdWhereClause = " WHERE " + nodeIdColumnName + " = '" + nodeId + "'";
                String nullNodeIdWhereClause =  " WHERE " + nodeIdColumnName + " IS NULL";

                if (nodeId != null) {
                   logger.info("Enriching properties with node specific id for id '{}'", nodeId);
                   enrichPropertiesFromDatabase(properties, baseSelect + nodeIdWhereClause, con);
                }
                enrichPropertiesFromDatabase(properties, baseSelect + nullNodeIdWhereClause, con);
            } else {
                enrichPropertiesFromDatabase(properties, baseSelect, con);
            }

        } catch (Exception e) {
            throw new RuntimeException(DatabasePropertiesEnricher.class.getSimpleName() + " could not load the properties from database. [\n" + e.getMessage() + "\n]", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {// nothing to do
                }
            }
        }
    }

    /**
     * Enrich properties using the result set obtained from the database using given sql statement.
     *
     * @param properties
     * @param sql
     * @param con
     * @throws java.sql.SQLException
     */
    private void enrichPropertiesFromDatabase(Properties properties, String sql, Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String key = rs.getString(keyColumnName);
            String value = rs.getString(valueColumnName);
            if (key == null || value == null) {
                continue;
            }

            String oldValue = properties.getProperty(key);
            if (oldValue == null) {
                properties.setProperty(key, value);
            } else {
                logger.info("Property '" + key + "' exist in current properties and it is not overridden.");
            }
        }
    }

    private String getValueFromProperty(String key, Properties props) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("No value found for property key '" + key + "'");
        }
        return value;
    }

    public String getPropertyTableName() {
        return propertyTableName;
    }

    /**
     * Name of table to load the properties from
     *
     * @param propertyTableName name of table
     */
    public void setPropertyTableName(String propertyTableName) {
        this.propertyTableName = propertyTableName;
    }

    public String getKeyColumnName() {
        return keyColumnName;
    }

    /**
     * Name of column with property keys in property table
     *
     * @param keyColumnName
     */
    public void setKeyColumnName(String keyColumnName) {
        this.keyColumnName = keyColumnName;
    }

    public String getValueColumnName() {
        return valueColumnName;
    }

    /**
     * Name of column with property values in property table
     *
     * @param valueColumnName
     */
    public void setValueColumnName(String valueColumnName) {
        this.valueColumnName = valueColumnName;
    }

    public String getNodeIdColumnName() {
        return nodeIdColumnName;
    }

    /**
     * Name of column with node id in property table
     *
     * @param nodeIdColumnName
     */
    public void setNodeIdColumnName(String nodeIdColumnName) {
        this.nodeIdColumnName = nodeIdColumnName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the data source for database
     *
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    /**
     * The key in current properties with value of database password
     * <p>It is used when no data source is set see {@link #setDataSource(javax.sql.DataSource) setDatasource}</p>
     *
     * @param passwordKey
     */
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

    public String getUserKey() {
        return userKey;
    }

    /**
     * The key in current properties with value of database username
     * <p>It is used when no data source is set see {@link #setDataSource(javax.sql.DataSource) setDatasource}</p>
     *
     * @param userKey
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getJdbcUrlKey() {
        return jdbcUrlKey;
    }

    /**
     * The key in current properties with value of jdbc url
     * <p>It is used when no data source is set see {@link #setDataSource(javax.sql.DataSource) setDatasource}</p>
     *
     * @param jdbcUrlKey
     */
    public void setJdbcUrlKey(String jdbcUrlKey) {
        this.jdbcUrlKey = jdbcUrlKey;
    }

    public String getDriverClassKey() {
        return driverClassKey;
    }

    /**
     * The key in current properties with value of driver class name
     * <p>It is used when no data source is set see {@link #setDataSource(javax.sql.DataSource) setDatasource}</p>
     *
     * @param driverClassKey
     */
    public void setDriverClassKey(String driverClassKey) {
        this.driverClassKey = driverClassKey;
    }

    public String getNodeIdKey() {
        return nodeIdKey;
    }

    /**
     * The key in current properties with value of node id
     * <p>Make sense only if nodeIdColumnName is see {@link #setNodeIdColumnName(String)}</p>
     *
     * @param nodeIdKey
     */
    public void setNodeIdKey(String nodeIdKey) {
        this.nodeIdKey = nodeIdKey;
    }

}
