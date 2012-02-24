package org.dspace.rest.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.core.Context;
import org.dspace.rest.entities.CollectionEntity;
import org.dspace.rest.entities.CollectionEntityId;
import org.dspace.rest.entities.CommunityEntity;
import org.dspace.rest.entities.CommunityEntityId;
import org.dspace.rest.entities.DetailDepth;
import org.dspace.rest.entities.Entity;
import org.dspace.rest.params.Parameters;

public class Collections {

    private static CollectionEntity build(final String uid, final Context context, final DetailDepth depth) throws SQLException {
        return new CollectionEntity(fetch(uid, context), 1, depth);
    }

    private static Collection fetch(final String uid, final Context context)
            throws SQLException {
        final int id = Integer.parseInt(uid);
        return Collection.find(context, id);
    }

    private static Entity build(final String uid, final Context context) throws SQLException {
        return Collections.build(fetch(uid, context));
    }

    public static Entity build(final Collection collection) {
        return new CollectionEntityId(collection.getID());
    }

    public static Entity build(final Context context, final DetailDepth depth,
            final String uid, final boolean idOnly) throws SQLException {
        return idOnly ? build(uid, context) : build(uid, context, depth);
    }

    public static Object items(String id, Parameters parameters,
            Context context) throws SQLException {
        switch (parameters.getEntityBuild().getFetchGroup()) {
            case LIGHT:
                return lightCollectionWithItems(id, context);
            default:
                return build(id, context, parameters.getDetailDepth().getDepth()).getItems();
        }
    }

    private static Entity lightCollectionWithItems(String id, Context context) {
        return null;
    }

    public static CollectionEntity collection(String id, Parameters parameters,
            Context context) throws SQLException {
        return build(id, context, parameters.getDetailDepth().getDepth());
    }

    public static Entity build(final String id, final Context context,
            final Parameters parameters) throws SQLException {
        if (parameters.getEntityBuild().isIdOnly()) {
            return build(id, context);
        } else {
            return build(id, context, parameters.getDetailDepth().getDepth());
        }
    }

    public static List<Object> build(final Parameters parameters,
            final Collection[] collections) throws SQLException {
        final List<Object> entities = new ArrayList<Object>();
        final boolean idOnly = parameters.getEntityBuild().isIdOnly();
        for (Collection c : collections) {
            entities.add(idOnly ? build(c) : new CollectionEntity(c, 1, DetailDepth.FOR_ALL_INDEX));
        }
        return entities;
    }

    public static List<Object> communities(final Collection collection, final int level, final DetailDepth depth)
            throws SQLException {
        final boolean includeFullNextLevel = depth.includeFullDetails(level);
        final List<Object> communities = new ArrayList<Object>();
        for (Community community : collection.getCommunities()) {
            communities.add(includeFullNextLevel ? new CommunityEntity(community, level, depth) : new CommunityEntityId(community));
        }
        return communities;
    }
    
}