package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCoffeeSortsDatabase {

    private static final String COFFEE_SORTS_DB_DIR =
            System.getProperty("user.dir") + "/db/coffeeSortsDB";
    private static final String CONNECTION_URL =
            "jdbc:h2:file:" + COFFEE_SORTS_DB_DIR;
    private static final String USER = "";
    private static final String PASSWORD = "";


    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(
                CONNECTION_URL, USER, PASSWORD);
        System.out.println("Coffee Sorts db created");
        createTables(conn);
        conn.close();
    }

    private static void createTables(Connection connection)
            throws SQLException {

        String DDL1 = "DROP TABLE IF EXISTS COFFEE";
        String DDL2 = "CREATE TABLE COFFEE (" +
                "`id` LONG IDENTITY NOT NULL PRIMARY KEY, " +
                "`manufactureCountry` VARCHAR(3) NOT NULL," +
                "`acidity` FLOAT," +
                "`collectionDate` DATE NOT NULL," +
                "`roastDate` DATE NOT NULL," +
                "`deliveryCountry` VARCHAR(3) NOT NULL," +
                "`name` VARCHAR(12) NOT NULL, " +
                "`roastingDepth` INT)";

        String DDL3 = "INSERT INTO COFFEE (manufactureCountry, acidity, collectionDate, roastDate, deliveryCountry, `name`, roastingDepth) VALUES ('USA', '3.5', '1999-12-01', '2000-02-21', 'RUS', 'Perfecto', '7');";
        String DDL4 = "INSERT INTO COFFEE (manufactureCountry, acidity, collectionDate, roastDate, deliveryCountry, `name`, roastingDepth) VALUES ('KAZ', '5.5', '2000-12-1', '2020-10-21', 'USA', 'Mediumo44', '2');";
        String DDL5 = "INSERT INTO COFFEE (manufactureCountry, acidity, collectionDate, roastDate, deliveryCountry, `name`, roastingDepth) VALUES ('UKR', '4', '2012-12-12', '2021-2-12', 'AZB', 'JB34', '0');";

        try (Statement stmnt = connection.createStatement()) {

            stmnt.executeUpdate(DDL1);
            stmnt.executeUpdate(DDL2);
            stmnt.executeUpdate(DDL3);
            stmnt.executeUpdate(DDL4);
            stmnt.executeUpdate(DDL5);

            System.out.println("Coffee sorts table created");
        }
    }
}