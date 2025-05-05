/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.model;


import com.carmotorsproject.config.DatabaseConnection;
import com.carmotorsproject.parts.model.SupplierEvaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of SupplierEvaluationDAOInterface that provides data access operations
 * for supplier evaluations in the database.
 */
public class SupplierEvaluationDAO implements SupplierEvaluationDAOInterface {

    private static final Logger LOGGER = Logger.getLogger(SupplierEvaluationDAO.class.getName());
    private final DatabaseConnection dbConnection;

    // SQL queries
    private static final String INSERT_EVALUATION =
            "INSERT INTO supplier_evaluation (supplier_id, evaluation_date, delivery_rating, " +
                    "quality_rating, price_rating, communication_rating, total_rating, comments) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_EVALUATION =
            "UPDATE supplier_evaluation SET supplier_id = ?, evaluation_date = ?, " +
                    "delivery_rating = ?, quality_rating = ?, price_rating = ?, " +
                    "communication_rating = ?, total_rating = ?, comments = ? " +
                    "WHERE evaluation_id = ?";

    private static final String DELETE_EVALUATION =
            "DELETE FROM supplier_evaluation WHERE evaluation_id = ?";

    private static final String FIND_BY_ID =
            "SELECT * FROM supplier_evaluation WHERE evaluation_id = ?";

    private static final String FIND_ALL =
            "SELECT * FROM supplier_evaluation ORDER BY evaluation_date DESC";

    private static final String FIND_BY_SUPPLIER =
            "SELECT * FROM supplier_evaluation WHERE supplier_id = ? ORDER BY evaluation_date DESC";

    private static final String FIND_BY_SCORE_RANGE =
            "SELECT * FROM supplier_evaluation WHERE total_rating BETWEEN ? AND ? " +
                    "ORDER BY total_rating DESC";

    private static final String FIND_BY_DATE_RANGE =
            "SELECT * FROM supplier_evaluation WHERE evaluation_date BETWEEN ? AND ? " +
                    "ORDER BY evaluation_date DESC";

    /**
     * Constructor that initializes the database connection.
     */
    public SupplierEvaluationDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Saves a new supplier evaluation to the database.
     *
     * @param evaluation The supplier evaluation to save
     * @return The saved supplier evaluation with generated ID
     * @throws SQLException If a database access error occurs
     */
    @Override
    public SupplierEvaluation save(SupplierEvaluation evaluation) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            // Validate ratings
            validateRatings(evaluation);

            // Compute total rating
            evaluation.computeTotalRating();

            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(INSERT_EVALUATION, Statement.RETURN_GENERATED_KEYS);

            // Set parameters
            statement.setInt(1, evaluation.getSupplierId());
            statement.setDate(2, new Date(evaluation.getEvaluationDate().getTime()));
            statement.setInt(3, evaluation.getDeliveryRating());
            statement.setInt(4, evaluation.getQualityRating());
            statement.setInt(5, evaluation.getPriceRating());
            statement.setInt(6, evaluation.getCommunicationRating());
            statement.setDouble(7, evaluation.getTotalRating());
            statement.setString(8, evaluation.getComments());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating supplier evaluation failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                evaluation.setEvaluationId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating supplier evaluation failed, no ID obtained.");
            }

            LOGGER.log(Level.INFO, "Supplier evaluation saved successfully for supplier ID: {0}",
                    evaluation.getSupplierId());
            return evaluation;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving supplier evaluation for supplier ID: " +
                    evaluation.getSupplierId(), e);
            throw e;
        } finally {
            closeResources(connection, statement, generatedKeys);
        }
    }

    /**
     * Updates an existing supplier evaluation in the database.
     *
     * @param evaluation The supplier evaluation to update
     * @return The updated supplier evaluation
     * @throws SQLException If a database access error occurs
     */
    @Override
    public SupplierEvaluation update(SupplierEvaluation evaluation) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Validate ratings
            validateRatings(evaluation);

            // Compute total rating
            evaluation.computeTotalRating();

            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(UPDATE_EVALUATION);

            // Set parameters
            statement.setInt(1, evaluation.getSupplierId());
            statement.setDate(2, new Date(evaluation.getEvaluationDate().getTime()));
            statement.setInt(3, evaluation.getDeliveryRating());
            statement.setInt(4, evaluation.getQualityRating());
            statement.setInt(5, evaluation.getPriceRating());
            statement.setInt(6, evaluation.getCommunicationRating());
            statement.setDouble(7, evaluation.getTotalRating());
            statement.setString(8, evaluation.getComments());
            statement.setInt(9, evaluation.getEvaluationId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating supplier evaluation failed, no rows affected.");
            }

            LOGGER.log(Level.INFO, "Supplier evaluation updated successfully: ID {0}",
                    evaluation.getEvaluationId());
            return evaluation;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating supplier evaluation: ID " +
                    evaluation.getEvaluationId(), e);
            throw e;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Deletes a supplier evaluation from the database.
     *
     * @param id The ID of the supplier evaluation to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException If a database access error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(DELETE_EVALUATION);

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Supplier evaluation deleted successfully: ID {0}", id);
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting supplier evaluation: ID " + id, e);
            throw e;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    /**
     * Finds a supplier evaluation by its ID.
     *
     * @param id The ID of the supplier evaluation to find
     * @return The found supplier evaluation, or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public SupplierEvaluation findById(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(FIND_BY_ID);

            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractEvaluationFromResultSet(resultSet);
            } else {
                LOGGER.log(Level.INFO, "No supplier evaluation found with ID: {0}", id);
                return null;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding supplier evaluation by ID: " + id, e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all supplier evaluations in the database.
     *
     * @return A list of all supplier evaluations
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<SupplierEvaluation> findAll() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<SupplierEvaluation> evaluations = new ArrayList<>();

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(FIND_ALL);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                evaluations.add(extractEvaluationFromResultSet(resultSet));
            }

            LOGGER.log(Level.INFO, "Found {0} supplier evaluations", evaluations.size());
            return evaluations;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all supplier evaluations", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds all supplier evaluations for a specific supplier.
     *
     * @param supplierId The ID of the supplier
     * @return A list of supplier evaluations for the specified supplier
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<SupplierEvaluation> findBySupplier(int supplierId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<SupplierEvaluation> evaluations = new ArrayList<>();

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(FIND_BY_SUPPLIER);

            statement.setInt(1, supplierId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                evaluations.add(extractEvaluationFromResultSet(resultSet));
            }

            LOGGER.log(Level.INFO, "Found {0} evaluations for supplier ID {1}",
                    new Object[]{evaluations.size(), supplierId});
            return evaluations;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding evaluations for supplier ID: " + supplierId, e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds supplier evaluations by evaluation score range.
     *
     * @param minScore The minimum score
     * @param maxScore The maximum score
     * @return A list of supplier evaluations within the specified score range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<SupplierEvaluation> findByScoreRange(double minScore, double maxScore) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<SupplierEvaluation> evaluations = new ArrayList<>();

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(FIND_BY_SCORE_RANGE);

            statement.setDouble(1, minScore);
            statement.setDouble(2, maxScore);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                evaluations.add(extractEvaluationFromResultSet(resultSet));
            }

            LOGGER.log(Level.INFO, "Found {0} evaluations with score between {1} and {2}",
                    new Object[]{evaluations.size(), minScore, maxScore});
            return evaluations;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding evaluations by score range", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Finds supplier evaluations by evaluation date range.
     *
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return A list of supplier evaluations within the specified date range
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<SupplierEvaluation> findByDateRange(java.util.Date startDate, java.util.Date endDate)
            throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<SupplierEvaluation> evaluations = new ArrayList<>();

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(FIND_BY_DATE_RANGE);

            statement.setDate(1, new Date(startDate.getTime()));
            statement.setDate(2, new Date(endDate.getTime()));

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                evaluations.add(extractEvaluationFromResultSet(resultSet));
            }

            LOGGER.log(Level.INFO, "Found {0} evaluations between dates {1} and {2}",
                    new Object[]{evaluations.size(), startDate, endDate});
            return evaluations;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding evaluations by date range", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    /**
     * Validates that all ratings in the evaluation are between 1 and 5.
     *
     * @param evaluation The supplier evaluation to validate
     * @throws SQLException If any rating is invalid
     */
    private void validateRatings(SupplierEvaluation evaluation) throws SQLException {
        try {
            if (evaluation.getDeliveryRating() < 1 || evaluation.getDeliveryRating() > 5) {
                throw new SQLException("Delivery rating must be between 1 and 5");
            }
            if (evaluation.getQualityRating() < 1 || evaluation.getQualityRating() > 5) {
                throw new SQLException("Quality rating must be between 1 and 5");
            }
            if (evaluation.getPriceRating() < 1 || evaluation.getPriceRating() > 5) {
                throw new SQLException("Price rating must be between 1 and 5");
            }
            if (evaluation.getCommunicationRating() < 1 || evaluation.getCommunicationRating() > 5) {
                throw new SQLException("Communication rating must be between 1 and 5");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Rating validation failed", e);
            throw new SQLException("Rating validation failed: " + e.getMessage());
        }
    }

    /**
     * Extracts a SupplierEvaluation object from a ResultSet.
     *
     * @param rs The ResultSet containing supplier evaluation data
     * @return A SupplierEvaluation object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private SupplierEvaluation extractEvaluationFromResultSet(ResultSet rs) throws SQLException {
        SupplierEvaluation evaluation = new SupplierEvaluation();

        evaluation.setEvaluationId(rs.getInt("evaluation_id"));
        evaluation.setSupplierId(rs.getInt("supplier_id"));
        evaluation.setEvaluationDate(rs.getDate("evaluation_date"));
        evaluation.setDeliveryRating(rs.getInt("delivery_rating"));
        evaluation.setQualityRating(rs.getInt("quality_rating"));
        evaluation.setPriceRating(rs.getInt("price_rating"));
        evaluation.setCommunicationRating(rs.getInt("communication_rating"));
        // We don't need to set total_rating as it's computed automatically
        evaluation.setComments(rs.getString("comments"));

        return evaluation;
    }

    /**
     * Closes database resources.
     *
     * @param connection The database connection
     * @param statement The prepared statement
     * @param resultSet The result set
     */
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                dbConnection.closeConnection(connection);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database resources", e);
        }
    }
}