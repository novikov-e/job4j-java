package servlet.cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.logging.Logger;
/**
 * Класс JDBCTransactionSavePointDemo - пример транзакции с приминением JDBC.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1$
 * @since 0.1
 */
public class JDBCTransactionSavePointDemo {

    // JDBC Driver Name & Database URL
    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final static String JDBC_DB_URL = "jdbc:mysql://localhost:3306/tutorialDb";

    // JDBC Database Credentials
    private final static String JDBC_USER = "root";
    private final static String JDBC_PASS = "";

    private static Connection connObj;
    public final static Logger LOG = Logger.getLogger("JDBCTransactionsDemo.class");

    public static void connectDb() {
        try {
            Class.forName(JDBC_DRIVER);
            connObj = DriverManager.getConnection(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
            LOG.info("\n=======DATABASE CONNECTION OPEN=======\n");
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void disconnectDb() {
        try {
            connObj.close();
            LOG.info("\n=======DATABASE CONNECTION CLOSED=======\n");
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void showTableRecords(String tableName) throws SQLException {
        ResultSet rsObj = null;
        Statement stmtObj = connObj.createStatement();
        rsObj = stmtObj.executeQuery("select user_id, user_name, created_date from " + tableName + ";");
        if (!rsObj.next()) {
            LOG.info("No Records In The Table\n");
        } else {
            LOG.info("Id: " + rsObj.getInt("user_id") + ", Name: " + rsObj.getString("user_name") + ", Joining Date: " + rsObj.getInt("created_date") + "\n");
        }
    }

    public static void saveUserDetails(int userId, String userName, String sysName) {

        PreparedStatement insertStatement = null,
                updateStatement = null;

        Savepoint saveObj = null;

        try {
            connObj.setAutoCommit(false);

            LOG.info("\n=======Inserting Data In The Table=======\n");
            String insertTableSQL = "insert into user_table (user_id, user_name, created_by, created_date) VALUES (?, ?, ?, ?);";

            insertStatement = connObj.prepareStatement(insertTableSQL);
            insertStatement.setInt(1, userId);
            insertStatement.setString(2, userName);
            insertStatement.setString(3, sysName);
            insertStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
            insertStatement.executeUpdate();        // Record Is Not Committed In Database At This Moment

            saveObj = connObj.setSavepoint();    // Savepoint Will Allow To RollBack Only Till This Checkpoint Incase An Exception Occurs.

            LOG.info("\n=======Updating Value In The Table=======\n");
            String updateTableSQL = "update user_table set user_name =? where user_id = ?";

            updateStatement = connObj.prepareStatement(updateTableSQL);

            // Line No. 84 - This line Will Result In An Exception & The Data Will Rolled-Back
            updateStatement.setString(1, "A Very Very Long String Resulting In A Database Error");

            // updateStatement.setString(1, "Lucifer Star");
            updateStatement.setInt(2, userId);
            updateStatement.executeUpdate();

            connObj.commit();
            showTableRecords("user_table");
        } catch (Exception sqlException) {
            try {
                connObj.rollback(saveObj);                    // Here, The Rollback Command Will Execute But The 'Insert' Will Still Be Committed To The Database As We Have Introduced A Savepoint at Line No. 76
                LOG.info("\n=======!Db Exception! Rolling Back The Update Data But Not Insert=======\n");
                showTableRecords("user_table");
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        } finally {
            try {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
                connObj.setAutoCommit(true);
            } catch (Exception sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        connectDb();
        saveUserDetails(101, "Harry Potter", "sys_admin");
        disconnectDb();
    }
}
