/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.rest.diagnose;

import java.sql.SQLException;

/**
 * Provides standard adaption for SQL exceptions caught by dspace
 * and rethrown to the entity bus framework as EntityException.
 */
public class SQLFailureEntityException extends DSpaceEntityException {

    private static final long serialVersionUID = 134589230572423312L;
    private final SQLException causalSQLException;
    
    private SQLFailureEntityException(SQLException cause, ErrorCategory category,
            ErrorDetail details, HTTPStatusCode code, Operation failedOperation) {
        super(cause, category, details, code, failedOperation);
        this.causalSQLException = cause;
    }

    /**
     * Convenience constructor builds a 500 Internal Server Error.
     * @param operation describes the operation which failed, for logging, not null
     * @param cause not null
     */
    public SQLFailureEntityException(final Operation operation, final SQLException cause) {
        this(cause, ErrorCategory.INTERNAL, ErrorDetail.SQL, HTTPStatusCode.INTERNAL_SERVER_ERROR, operation);
    }

    public SQLException getCausalSQLException() {
        return causalSQLException;
    }   
}
