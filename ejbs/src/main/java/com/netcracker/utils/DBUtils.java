package com.netcracker.utils;

import java.sql.Connection;
import java.sql.*;
import java.util.*;

public class DBUtils {

    /**
     *
     * @param connection
     * @param sqlQuery
     * @param params
     * @param retColNums
     * @return
     * @throws SQLException
     */
    public static Map<String, List> executeSelect(Connection connection, String sqlQuery, Object[] params, int[] retColNums)
            throws SQLException {

        if (connection == null || sqlQuery == null || params == null || retColNums == null) {
            throw new IllegalArgumentException("All parameters must be not null values.");
        }

        Statement stm = null;
        ResultSet rs = null;
        try {
            if (params.length == 0) {
                stm = connection.createStatement();
                rs = stm.executeQuery(sqlQuery);
                return processResult(rs, retColNums);
            } else {
                PreparedStatement prStm = substituteParams(connection.prepareStatement(sqlQuery), params);
                rs = prStm.executeQuery();
                return processResult(rs, retColNums);
            }
        } finally {
            cleanup(rs, stm, connection);
        }
    }

    private static Map<String, List> processResult(ResultSet rs, int[] retColNums) throws SQLException {

        // Find maximum value in retColNums array
        int max = -1;
        if (retColNums.length != 0) {
            Arrays.sort(retColNums);
            max = retColNums[retColNums.length - 1];
        }

        ResultSetMetaData metaData = rs.getMetaData();
        if (max > metaData.getColumnCount()) {
            throw new IllegalArgumentException("Specified expected column numbers (in retColNums parameter) of result set" +
                                               "don't corresponds to the actual result (retrieved from SQL execution). Check" +
                                               "retColNums parameter");
        }

        // Create blank map (which will be contain result) with appropriate keys (keys automatically sorted by TreeMap)
        Map<String, List> result = new TreeMap<String, List>();
        if (retColNums.length != 0) {   // In this case result will contain only those columns (from result set), that specified in array
            for (int colNum : retColNums) {
                result.put(String.valueOf(colNum), new LinkedList());
            }
        } else {    // In this case result will contain all columns from result set
            for (int colNum = 1; colNum <= metaData.getColumnCount(); colNum++) {
                result.put(String.valueOf(colNum), new LinkedList());
            }
        }

        // Filling values to the result
        while (rs.next()) {
            for (int colNum = 1; colNum <= metaData.getColumnCount(); colNum++) {
                if (retColNums.length != 0) {   // Fill specified columns
                    if (isInArray(colNum, retColNums)) {
                        result.get(String.valueOf(colNum)).add(rs.getObject(colNum));
                    }
                } else {    // Fill all impossible columns
                    result.get(String.valueOf(colNum)).add(rs.getObject(colNum));
                }
            }
        }

        return result;
    }

    private static boolean isInArray(int number, int[] sortedArray) {
        return Arrays.binarySearch(sortedArray, number) >= 0;
    }


    /**
     *
     * @param connection
     * @param sqlQuery
     * @param params
     * @return
     * @throws SQLException
     */
    public static Object executeSelectSingle(Connection connection, String sqlQuery, Object[] params) throws SQLException {
        if (connection == null || sqlQuery == null || params == null) {
            throw new IllegalArgumentException("All parameters must be not null values.");
        }

        Statement stm = null;
        ResultSet rs = null;
        Object result = null;
        try {
            if (params.length == 0) {
                stm = connection.createStatement();

                rs = stm.executeQuery(sqlQuery);
                if (rs.next()) {
                    return rs.getObject(1);
                } else {
                    return result;
                }
            } else {
                PreparedStatement prStm = substituteParams(connection.prepareStatement(sqlQuery), params);
                stm = prStm;

                rs = prStm.executeQuery();
                if (rs.next()) {
                    return rs.getObject(1);
                } else {
                    return result;
                }
            }
        } finally {
            cleanup(rs, stm, connection);
        }
    }

    /**
     * Execute given query "sqlQuery" and from the result set returns column with "colNum" number.
     *
     * @param connection    connection to the database
     * @param sqlQuery      executed SQL query
     * @param colNum        returned column from the result set
     *
     * @return              returns column from the result set represented as Java collection
     *
     * @throws SQLException when any errors during the work with database are occurs
     * @throws IllegalArgumentException     in case when colNum parameter is negative
     */
    public static Collection selectSingleColumn(Connection connection, String sqlQuery, int colNum) throws SQLException {
        if (colNum < 0) {
            throw new IllegalArgumentException("colNum parameter must be positive number");
        }

        return null;

    }

    /**
     *
     * @param connection
     * @param sqlQuery
     * @param params
     * @return amount of affected rows
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, String sqlQuery, Object[] params) throws SQLException {
        if (connection == null || sqlQuery == null || params == null) {
            throw new IllegalArgumentException("All parameters must be not null values.");
        }

        Statement stm = null;
        ResultSet rs = null;
        try {
            if (params.length == 0) {
                stm = connection.createStatement();
                return stm.executeUpdate(sqlQuery);
            } else {
                PreparedStatement prStm = substituteParams(connection.prepareStatement(sqlQuery), params);
                stm = prStm;
                return prStm.executeUpdate();
            }
        } finally {
            cleanup(rs, stm, connection);
        }
    }

    /**
     * This method creates entity record in the database and returns its id.
     *
     * @param connection
     * @param sqlQuery      insert query
     * @param params        query params
     * @return              id of newly created entity
     *
     * @throws SQLException
     */
    public static int executeInsert(Connection connection, String sqlQuery, Object[] params, boolean genKey)
            throws SQLException {
        if (connection == null || sqlQuery == null || params == null || params.length == 0) {
            throw new IllegalArgumentException("All parameters must be not empty.");
        }

        PreparedStatement prStm = null;
        ResultSet rs = null;
        try {
            if (genKey) {
                prStm = substituteParams(connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS), params);
                prStm.executeUpdate();
                rs = prStm.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                prStm = substituteParams(connection.prepareStatement(sqlQuery), params);
                return prStm.executeUpdate();
            }
        } finally {
            cleanup(rs, prStm, connection);
        }
    }


    // ------------------------------------- Helper methods -----------------------------------------------------
    private static void cleanup(ResultSet rs, Statement stm, Connection connection) throws SQLException {
        if(rs != null) {
            rs.close();
        }
        if (stm != null) {
            stm.close();
        }
        if(connection != null) {
            connection.close();
        }

    }

    private static PreparedStatement substituteParams(PreparedStatement prStm, Object[] params) throws SQLException{
        int i = 1;
        // Set parameters for SQL query
        for (Object param : params) {
            if (param instanceof Integer) {     // fake applied for balink table
                prStm.setInt(i, (Integer)param);
            } else {
                prStm.setObject(i, param);
            }
            i++;
        }
        return prStm;
    }
    // ----------------------------------------------------------------------------------------------------------

}
