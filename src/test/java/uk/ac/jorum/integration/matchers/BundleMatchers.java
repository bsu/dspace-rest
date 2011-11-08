package uk.ac.jorum.integration.matchers;

import static uk.ac.jorum.integration.matchers.EntityMatchers.hasId;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.allOf;
import static uk.ac.jorum.integration.matchers.EntityMatchers.*;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.json.simple.JSONObject;

public class BundleMatchers {


	  @Factory
	  public static Matcher<JSONObject> isBundleWithId(int id) {
	    return hasId(id);
	  }
	  
	  @Factory
	  public static Matcher<JSONObject> isBundle(int id, String name, ArrayList<Matcher<JSONObject>> itemIdMatchers, ArrayList<Matcher<JSONObject>> bitstreamIdMatchers) {
	    return allOf(
	    		hasId(id),
	    		hasType(1),
	    		hasName(name),
	    		hasArray("items", itemIdMatchers),
	    		hasArray("bitstreams", bitstreamIdMatchers)
	    		);
	  }
}