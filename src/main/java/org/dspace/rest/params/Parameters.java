package org.dspace.rest.params;

import java.util.List;

import org.dspace.core.Context;
import org.dspace.rest.entities.DetailDepth;
import org.dspace.rest.entities.ItemBuilder;
import org.sakaiproject.entitybus.entityprovider.extension.RequestStorage;

public class Parameters {

    private final RequestStorage requestStore;
    
    public Parameters(RequestStorage requestStore) {
        super();
        this.requestStore = requestStore;
    }

    public EntityBuildParameters getEntityBuild() {
        return EntityBuildParameters.build(requestStore);
    }

    public DetailDepthParameters getDetailDepth() {
        return DetailDepthParameters.build(requestStore);
    }

    public PaginationParameters getPagination() {
        return new PaginationParameters(requestStore);
    }

    public SortParameters getSort() {
        return new SortParameters(requestStore);
    }

    public StatusParameters getStatus() {
        return new StatusParameters(requestStore);
    }
    
    public DurationParameters getDuration() {
        return new DurationParameters(requestStore);
    }
    
    public ItemBuilder itemBuilder() {
        return itemBuilder(getDetailDepth().getDepth());
    }

    public ItemBuilder itemBuilder(final DetailDepth depth) {
        return ItemBuilder.builder(getEntityBuild().isIdOnly(), depth);
    }
    
    /**
     * Remove items from list in order to display only requested items
     * (according to _start, _limit etc.)
     * @param entities
     */
    public void removeTrailing(List<?> entities) {
        getPagination().removeTrailing(entities);
    }
    
    public void sort(final List<?> entities) {
        /**
         * if the full info are requested and there are sorting requirements
         * process entities through sorting filter first
         */
        if (!getEntityBuild().isIdOnly()) {
            getSort().sort(entities);
        }
    }

    public ScopeParameters getScope(Context context) {
        return ScopeParameters.build(requestStore, context);
    }
}
