/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */


package org.dspace.rest.providers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.rest.entities.CollectionEntity;
import org.dspace.rest.entities.CollectionEntityId;
import org.dspace.rest.params.DetailDepthParameters;
import org.dspace.rest.params.EntityBuildParameters;
import org.sakaiproject.entitybus.EntityReference;
import org.sakaiproject.entitybus.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.entityprovider.search.Search;

/**
 * Provides interface for access to collections entities
 * @see CollectionEntity
 * @see CollectionEntityId
 * @author Bojan Suzic, bojan.suzic@gmail.com
 */
public class CollectionsProvider extends AbstractBindingProvider implements CoreEntityProvider {

    private static Logger log = Logger.getLogger(CollectionsProvider.class);

    public CollectionsProvider(EntityProviderManager entityProviderManager) throws SQLException, NoSuchMethodException {
        super(entityProviderManager, Binder.forCollections());
    }

    public String getEntityPrefix() {
        return "collections";
    }

    public boolean entityExists(String id) {
        log.info(userInfo() + "entity_exists:" + id);

        // sample entity
        if (id.equals(":ID:")) {
            return true;
        }
        Context context = context();

        boolean result = false;
        try {
            Collection col = Collection.find(context, Integer.parseInt(id));
            if (col != null) {
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
        }

        // close connection to prevent connection problems
        complete(context);
        return result;
    }

    /**
     * Returns information about particular entity
     * @param reference
     * @return
     */
    @Override
    public Object getEntity(EntityReference reference) {
        log.info(userInfo() + "get_entity:" + reference.getId());
        String segments[] = {};

        if (requestStore.getStoredValue("pathInfo") != null) {
            segments = requestStore.getStoredValue("pathInfo").toString().split("/", 10);
        }

        // first check if there is sub-field requested
        // if so then invoke appropriate method inside of entity
        if (segments.length > 3) {
            return super.getEntity(reference);
        } else {

            Context context = context();

            try {
                // sample entity
                if (reference.getId().equals(":ID:")) {
                    return new CollectionEntity();
                }

                if (reference.getId() == null) {
                    return new CollectionEntity();
                }

                if (entityExists(reference.getId())) {
                    try {
                        // return basic entity or full info
                        if (EntityBuildParameters.build(requestStore).isIdOnly()) {
                            return new CollectionEntityId(reference.getId(), context);
                        } else {
                            return new CollectionEntity(reference.getId(), context, 1, DetailDepthParameters.build(requestStore).getDepth());
                        }
                    } catch (SQLException ex) {
                        throw new IllegalArgumentException("Invalid id:" + reference.getId());
                    }
                }

                throw new IllegalArgumentException("Invalid id:" + reference.getId());
            } finally {
                complete(context);
            }
        }
    }

    /**
     * List all collection in the system, sort and format if requested
     * @param ref
     * @param search
     * @return
     */
    public List<?> getEntities(EntityReference ref, Search search) {
        log.info(userInfo() + "list_entities");

        Context context = context();

        List<Object> entities = new ArrayList<Object>();

        try {
            Collection[] collections = null;
            collections = Collection.findAll(context);
            //            System.out.println(" number of collections " + Collection.getNumCollections(context));
            for (Collection c : collections) {
                entities.add(EntityBuildParameters.build(requestStore).isIdOnly() ? new CollectionEntityId(c) : new CollectionEntity(c, 1, DetailDepthParameters.build(requestStore).getDepth()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        complete(context);
        
        sort(entities);
        removeTrailing(entities);

        return entities;
    }

    /*
     * Here is sample collection entity defined
     */
    public Object getSampleEntity() {
        return new CollectionEntity();
    }
}
