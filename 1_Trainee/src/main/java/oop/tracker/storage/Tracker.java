package oop.tracker.storage;

import oop.tracker.objects.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
/**
 * Class Tracker keeps applications, and has methods for editing.
 * @author Egor Novikov (enovikovdev@gmail.com)
 * @version 1.0
 * @since 14/06/2018
 */
public class Tracker implements AutoCloseable {

    private final Connection connection;
    private static final Logger LOG = LoggerFactory.getLogger(Tracker.class);

    public Tracker() throws SQLException, IOException, URISyntaxException {
        this.connection = getConnection();
    }

    public Tracker(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            props.load(stream);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Method adds the application in the repository.
     * @param item - item;
     */
    public void add(Item item) {
        String sql = "INSERT INTO items(name, description, create_date) VALUES(?, ?, ?);";
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDesc());
            ps.setString(3, item.getDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
    }
    /**
     * Method modifies the application in the repository.
     * @param item - item.
     */
    public void replace(Item item) {
        String sql = "UPDATE items SET name = ?, description = ?, create_date = ? where id = ?;";
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDesc());
            ps.setString(3, item.getDate());
            ps.setInt(4, item.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
    }
    /**
     * Method that realizes the removal of an application from the repository.
     * @param id - ID.
     */
    public void delete(int id) {
        String sql = "DELETE FROM items WHERE id = ?;";
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
    }
    /**
     * The method searches for an application by ID.
     * @param id - ID.
     * @return - new unique key.
     */
    public Item findById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?;";
        Item item = null;
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                item = new Item(result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("create_date"));
            }
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
        return item;
    }
    /**
     * Method returns a list of orders by name.
     * @param name - name of application.
     * @return - list of applications.
     */
    public ArrayList<Item> findByName(String name) {
        String sql = "SELECT * FROM items WHERE name = ?";
        ArrayList<Item> items = new ArrayList<>();
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                items.add(new Item(result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("create_date")));
            }
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
        return items;
    }
    /**
     * Method receives a list of all applications.
     * @return lost of application.
     */
    public ArrayList<Item> getAll() {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                items.add(new Item(result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("create_date")));
            }
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
        return items;
    }
    /**
     * The method is responsible for the availability of applications.
     * @return - true, if there are applications, else false.
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS items("
                + "id serial primary key, "
                + "name character varying (200), "
                + "description character varying (2000), "
                + "create_date character varying (100));";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
    }
    /**
     * The method check db is empty.
     * @return - true - is empty or false.
     */
    public boolean isEmpty() {
        boolean result = true;
        int count = 0;
        String sql = "SELECT count(*) FROM items as count";
        try (PreparedStatement ps  = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            if (count > 0) {
                result = false;
            }
        } catch (SQLException e) {
            LOG.error("Connection error!");
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}