package uk.ac.jorum.integration.matchers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HasStreamContent extends TypeSafeMatcher<String> {

	private String streamContent = "";
	
	private HasStreamContent(String filePath) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line=reader.readLine())!=null) {
			streamContent += line;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Stream content should be: " + streamContent);
	}

	@Override
	public boolean matchesSafely(String bitstreamContent) {
		return streamContent.trim().equals(bitstreamContent.trim());
	}
	
	@Factory
	public static Matcher<String> hasStreamContent(String filePath) throws FileNotFoundException, IOException {
		return new HasStreamContent(filePath);
	}	
}
