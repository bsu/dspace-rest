package org.dspace.rest.data;

import java.sql.SQLException;
import java.util.List;

import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.rest.diagnose.Operation;
import org.dspace.rest.diagnose.SQLFailureEntityException;
import org.dspace.rest.entities.CollectionEntity;
import org.dspace.rest.entities.CollectionEntityId;
import org.dspace.rest.entities.DetailDepth;
import org.dspace.rest.entities.Entity;
import org.dspace.rest.params.Parameters;

public class DSpace {

    /**
     * Complete connection in order to lower load of sql server
     * this way it goes faster and prevents droppings with higher load
     * @param context
     */
    public static final void complete(Context context) {
        // close connection to prevent connection problems
        try {
            context.complete();
        } catch (SQLException ex) {
        }
    }

    public static final Context context() {
        final Context context;
        try {
            context = new Context();
        } catch (SQLException ex) {
            throw new SQLFailureEntityException(Operation.CREATE_CONTEXT, ex);
        }
        return context;
    }

    public static class Collections {

        public static CollectionEntity build(final String uid, final Context context, final DetailDepth depth) throws SQLException {
            return new CollectionEntity(fetch(uid, context), 1, depth);
        }

        private static Collection fetch(final String uid, final Context context)
                throws SQLException {
            final int id = Integer.parseInt(uid);
            return Collection.find(context, id);
        }

        public static Entity build(final String uid, final Context context) throws SQLException {
            return Collections.build(fetch(uid, context));
        }

        public static Entity build(final Collection collection) {
            return new CollectionEntityId(collection.getID());
        }

        public static Entity build(final Context context, final DetailDepth depth,
                final String uid, final boolean idOnly) throws SQLException {
            return idOnly ? build(uid, context) : build(uid, context, depth);
        }

        public static List<?> items(String id, Parameters parameters,
                Context context) throws SQLException {
            return build(id, context, parameters.getDetailDepth().getDepth()).getItems();
        }

        public static CollectionEntity collection(String id, Parameters parameters,
                Context context) throws SQLException {
            return build(id, context, parameters.getDetailDepth().getDepth());
        }
        
    }
    
}
