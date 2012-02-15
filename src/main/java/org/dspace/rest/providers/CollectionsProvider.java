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
import org.dspace.rest.data.DSpace;
import org.dspace.rest.diagnose.EntityNotFoundException;
import org.dspace.rest.diagnose.Operation;
import org.dspace.rest.diagnose.SQLFailureEntityException;
import org.dspace.rest.entities.CollectionEntity;
import org.dspace.rest.entities.CollectionEntityId;
import org.dspace.rest.entities.DetailDepth;
import org.dspace.rest.params.DetailDepthParameters;
import org.dspace.rest.params.EntityBuildParameters;
import org.dspace.rest.params.Parameters;
import org.dspace.rest.params.Route;
import org.dspace.rest.providers.CollectionsProvider.CollectionBinder;
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
public class CollectionsProvider extends AbstractBaseProvider implements CoreEntityProvider {

    public static class CollectionBinder extends Binder{
    
        @Override
        protected Object value(String id, Parameters parameters,
                Context context, String attributeSegment) throws SQLException {
            if("id".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getId();
            } else if("id".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getId();
            } else if("name".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getName();
            } else if("licence".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getLicence();
            } else if("items".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getItems();
            } else if("handle".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getHandle();
            } else if("canedit".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getCanEdit();
            } else if("communities".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getCommunities();
            } else if("countItems".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getCountItems();
            } else if("type".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getType();
            } else if("shortDescription".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getShortDescription();
            } else if("introText".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getIntroText();
            } else if("copyrightText".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getCopyrightText();
            } else if("sidebarText".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getSidebarText();
            } else if("provenance".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getProvenance();
            } else if("logo".equals(attributeSegment)) {
                return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth()).getLogo();
            } else {
                return null;
            }
        }
    
        @Override
        protected Operation operation() {
            return Operation.GET_COLLECTIONS;
        }
        
    }

    private static Logger log = Logger.getLogger(CollectionsProvider.class);
    private final Binder binder;

    public CollectionsProvider(EntityProviderManager entityProviderManager) throws SQLException, NoSuchMethodException {
        super(entityProviderManager);
        this.binder = new CollectionsProvider.CollectionBinder();
    }

    public String getEntityPrefix() {
        return "collections";
    }

    public boolean entityExists(String id) {
        // sample entity
        if (id.equals(":ID:")) {
            return true;
        }

        final Context context = DSpace.context();
        try {
            return Collection.find(context, Integer.parseInt(id)) != null;
        } catch (SQLException ex) {
            log.debug("Failed to find collections. Assuming that this means it doesn't exist.", ex);
            return false;
        } finally {
            DSpace.complete(context);
        }
    }

    /**
     * Returns information about particular entity
     * @param reference
     * @return
     */
    @Override
    public Object getEntity(EntityReference reference) {
        final String id = reference.getId();
        // sample entity
        if (id == null || ":ID:".equals(id)) {
            return getSampleEntity();
        }
        return entity(id);
    }

    private Object entity(final String id) {
        final Operation operation = Operation.GET_COLLECTIONS;
        final Context context = DSpace.context();
        try {
            final Parameters parameters = new Parameters(requestStore);
            final Route route = new Route(requestStore);

            if (route.isAttribute()) {
                log.debug("Using generic entity binding");
                return binder.resolve(id, route, parameters, context);
            } else {
                if (entityExists(id)) {
                    // return basic entity or full info
                    if (parameters.getEntityBuild().isIdOnly()) {
                        return new CollectionEntityId(id, context);
                    } else {
                        return new CollectionEntity(id, context, 1, parameters.getDetailDepth().getDepth());
                    }

                } else {
                    if (log.isDebugEnabled()) log.debug("Cannot find entity " + id);
                    throw new EntityNotFoundException(operation);
                }
            }
        } catch (SQLException cause) {
            if (log.isDebugEnabled()) log.debug("Cannot find entity " + id);
            throw new SQLFailureEntityException(operation, cause);

        } finally {
            DSpace.complete(context);
        }
    }

    /**
     * List all collection in the system, sort and format if requested
     * @param ref
     * @param search
     * @return
     */
    public List<?> getEntities(EntityReference ref, Search search) {
        return getAllCollections();
    }

    private List<?> getAllCollections() {
        Operation operation = Operation.GET_COLLECTIONS;
        final Parameters parameters = new Parameters(requestStore);
        final Context context = DSpace.context();
        try {

            final List<Object> entities = new ArrayList<Object>();
            final Collection[] collections = Collection.findAll(context);
            final boolean idOnly = parameters.getEntityBuild().isIdOnly();
            for (Collection c : collections) {
                entities.add(idOnly ? new CollectionEntityId(c) : new CollectionEntity(c, 1, DetailDepth.FOR_ALL_INDEX));
            }

            parameters.sort(entities);
            parameters.removeTrailing(entities);

            return entities;

        } catch (SQLException cause) {
            throw new SQLFailureEntityException(operation, cause);
        } finally {
            DSpace.complete(context);
        }
    }
}
