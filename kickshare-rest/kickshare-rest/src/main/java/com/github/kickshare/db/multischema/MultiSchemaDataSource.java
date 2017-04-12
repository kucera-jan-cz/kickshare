package com.github.kickshare.db.multischema;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 11.4.2017
 */
@AllArgsConstructor
public class MultiSchemaDataSource implements DataSource {
    private DataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        setSchema(connection);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = dataSource.getConnection(username, password);
        setSchema(connection);
        return connection;
    }

    private void setSchema(Connection connection) throws SQLException {
        connection.createStatement().execute("SET SCHEMA " + SchemaContextHolder.getSchema());
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }
}
