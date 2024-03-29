package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Nicole Mottier
 * Date: September 23, 2019
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = true;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 3;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //TODO (Step 3): Add a loop here that calls createMarker (see below) 
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)
	    
	    Object mag2;
	    float mag3;
	    for(int counter = 0; counter < earthquakes.size(); counter++) {
	    	PointFeature pf = earthquakes.get(counter);
	    	mag2 = pf.getProperty("magnitude");
	    	mag3 = Float.parseFloat(mag2.toString());
	    	if(mag3 > THRESHOLD_LIGHT) {
	    	markers.add(createMarker(pf));
	    	}
	    	else {
	    	}
	    	}
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
		
	private void SimplePointMarker(List<Marker> markers) {
		// TODO Auto-generated method stub
		
	}

	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is.  Call it from a loop in the 
	 * setp method.  
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	public SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		System.out.println(feature.getProperties());
		
		
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int purple = color(156, 51, 255);
	    int pink = color(255, 51, 227);
	    int teal = color(51, 255, 168);
	    int grey = color(128, 128, 128);
	    float radius1 = 5;
	    float radius2 = 10;
	    float radius3 = 15;
	    
		
		// TODO (Step 4): Add code below to style the marker's size and color 
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    if (mag < THRESHOLD_LIGHT) {
	    	marker.setColor(purple);
	    	marker.setRadius(radius1);
	    }
	    else if (mag >= THRESHOLD_LIGHT && mag <= THRESHOLD_MODERATE) {
	    	marker.setColor(grey);
	    	marker.setRadius(radius2);
	    }
	    else if (mag > THRESHOLD_MODERATE) {
	    	marker.setColor(pink);
	    	marker.setRadius(radius3);
	    	
	   }
	   
	    
	    // Finally return the marker
	    return marker;
	    
	    
	}
	
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255, 204, 204);
		rect(25, 50, 150, 300);
	
		textSize(16);
		fill(0, 0, 0);
		text("Earthquake", 55, 80);
		
		textSize(16);
		fill(0, 0, 0);
		text("Magnitude Key", 45, 100);
		
		textSize(10);
		fill(0, 0, 0);
		text("Below 3.0", 65, 140);
		
		textSize(10);
		fill(0, 0, 0);
		text("Between 3.0 and 5.0", 65, 180);
		
		textSize(10);
		fill(0, 0, 0);
		text("Above 5.0", 65, 220);
		
		fill(156, 51, 255);
		ellipse(45, 135, 5, 5);
		
		fill(128, 128, 128);
		ellipse(45, 175, 10, 10);
	
		fill(255, 51, 227);
		ellipse(45, 215, 15, 15);
	}
}
