package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class DB_StatementPool {
    // This class helps to reduce the very expensive process of creating and closing statements.
    // We want to reuse statements.

    private Queue<Statement> statementQueue = new LinkedList<>();
    private Connection connection;

    public DB_StatementPool(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get a statement for the db connection
     * @return the head of the queue or a new statement if the queue is empty.
     * @throws SQLException
     */
    public Statement getStatement() throws SQLException {
        if(connection == null) {
            throw new RuntimeException("not connected");
        }

        if(!statementQueue.isEmpty()) {
            return statementQueue.poll();
        }

        return connection.createStatement();
    }

    /**
     * Return a used statement back to the queue
     * @param statement
     */
    public void releaseStatement(Statement statement) {
        if(connection == null) {
            throw new RuntimeException("not connected");
        }
        statementQueue.offer(statement);
    }

    /**
     * Closes all statements. This method should be called at the end of the program execution.
     * @throws SQLException
     */
    public void closeStatements() throws SQLException {
        while(!statementQueue.isEmpty()) {
            statementQueue.poll().close();
        }
    }
}
