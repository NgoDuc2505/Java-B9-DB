package QLSV_DB;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private static List<String> listMajor = new ArrayList<>();
    private static Statement statement;
    private static ResultSet result;

    public static List<String> getListDepartment(ResultSet result, Statement statement) throws SQLException {
        final String query = "SELECT * FROM Department;";
        result = statement.executeQuery(query);
        while (result.next()){
            System.out.println(result.getString(1) +"|"+result.getString(2));
            listMajor.add(result.getString(1) +"|"+result.getString(2));
        }

        return  listMajor;
    }
}
