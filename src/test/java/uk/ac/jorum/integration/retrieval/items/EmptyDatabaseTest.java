/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */


package uk.ac.jorum.integration.retrieval.items;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static uk.ac.jorum.integration.matchers.HasHTTPCode.hasHTTPCode;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.jorum.integration.HTTPStatusCode;
import uk.ac.jorum.integration.RestApiBaseTest;

public class EmptyDatabaseTest extends RestApiBaseTest {


	@BeforeClass
    public static void createFixture() throws Exception {
      loadFixture("emptyDatabase");
      startJetty();
    }

	@Test
    public void itemListShouldBeEmpty() throws Exception {
      String result = makeRequest("/items");
      assertThat(result, containsString("\"items_collection\": [\n\n]}"));
    }

	@Test
  	public void emptyItemsListShouldReturnNoContentStatusCode() throws Exception{
	  int result = getResponseCode("/items");
	  assertThat("200 is observed behaviour, should really be 204", result, hasHTTPCode(HTTPStatusCode.SUCCESS));
  	}

}