/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.rest.entities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.core.Context;
import org.dspace.rest.providers.BitstreamProvider;
import org.sakaiproject.entitybus.entityprovider.annotations.EntityFieldRequired;
import org.sakaiproject.entitybus.entityprovider.annotations.EntityId;

/**
 * Entity describing Bitstreams
 * @see BitstreamEntityId
 * @see BitstreamProvider
 * @see Bitstream
 * @author Bojan Suzic, bojan.suzic@gmail.com
 */
public class BitstreamEntity extends BitstreamEntityId {

    @EntityId
    private int id;
    @EntityFieldRequired
    private String name;
    private String handle;
    private int type, storeNumber;
    private long sequenceId, size;
    private String checkSumAlgorithm, description, checkSum, formatDescription, source, userFormatDescription, mimeType;
    List<Object> bundles = new ArrayList<Object>();

    public BitstreamEntity(String uid, Context context, int level, final DetailDepth depth) throws SQLException {
        this(Bitstream.find(context, Integer.parseInt(uid)), level, depth);
    }

    public BitstreamEntity(Bitstream bitstream, int level, final DetailDepth depth) throws SQLException {
        // Only include full when above maximum depth
        final boolean includeFullNextLevel = depth.includeFullDetails(++level);
        
        this.handle = bitstream.getHandle();
        this.name = bitstream.getName();
        this.type = bitstream.getType();
        this.id = bitstream.getID();
        this.checkSum = bitstream.getChecksum();
        this.checkSumAlgorithm = bitstream.getChecksumAlgorithm();
        this.description = bitstream.getDescription();
        this.formatDescription = bitstream.getFormatDescription();
        this.sequenceId = bitstream.getSequenceID();
        this.size = bitstream.getSize();
        this.source = bitstream.getSource();
        this.storeNumber = bitstream.getStoreNumber();
        this.userFormatDescription = bitstream.getUserFormatDescription();
        Bundle[] bnd = bitstream.getBundles();
        for (Bundle b : bnd) {
            this.bundles.add(includeFullNextLevel ? new BundleEntity(b, level, depth) : new BundleEntityId(b));
        }
        this.mimeType = bitstream.getFormat().getMIMEType();
    }

    public BitstreamEntity() {
        // check calling package/class in order to prevent chaining
        boolean includeFull = false;

        this.handle = null;
        this.name = "The name of the file";
        this.type = 0;
        this.id = 4;
        this.checkSum = "b74c368660979349459fe30c93e2d9c7";
        this.checkSumAlgorithm = "MD5";
        this.description = "Sample description";
        this.formatDescription = "Adobe PDF";
        this.sequenceId = 1;
        this.size = 123456789;
        this.source = "/dspace/upload/file.pdf";
        this.storeNumber = 0;
        this.userFormatDescription = null;
        this.bundles.add(includeFull ? new BundleEntity() : new BundleEntityId());
        this.mimeType = "application/pdf";
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public List<?> getBundles() {
        return this.bundles;
    }

    public String getCheckSum() {
        return this.checkSum;
    }

    public String getCheckSumAlgorithm() {
        return this.checkSumAlgorithm;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFormatDescription() {
        return this.formatDescription;
    }

    public long getSequenceId() {
        return this.sequenceId;
    }

    public long getSize() {
        return this.size;
    }

    public String getSource() {
        return this.source;
    }

    public int getStoreNumber() {
        return this.storeNumber;
    }

    public String getUserFormatDescription() {
        return this.userFormatDescription;
    }

    public String getName() {
        return this.name;
    }

    public String getHandle() {
        return this.handle;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "id:" + this.id + ", stuff.....";
    }
}
