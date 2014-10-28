
import java.util.HashSet;

/**
 * The Location class maintains information relevant to a single location. The information includes the location name
 * and (x, y) coordinates. Each location object also contains two sets: the set of volunteers and the set of requests
 * that are currently at this location.
 * 
 * @author dhelia
 * @author jtk
 * @version 2014
 */
public class Location {
    private String name;
    private double x;
    private double y;

    // Volunteers and Requests at the current location
    private HashSet<Volunteer> volunteers = new HashSet<Volunteer>();
    private HashSet<Request> requests = new HashSet<Request>();

    /**
     * Constructor that creates a Location with the given name at coordinates (x, y) in the given model.
     * 
     * @param model
     *            the Model that this Location is a part of
     * @param name
     *            the String name of the location (for example, "LWSN")
     * @param x
     *            the double that is the x coordinate of this location
     * @param y
     *            the double that is the y coordinate of this location
     */
    public Location(Model model, String name, double x, double y) {
	this.name = name;
	this.x = x;
	this.y = y;

	model.addLocation(this);
    }

    /**
     * Adds a request to this location.
     * 
     * @param request
     *            the Request to be added to this location
     */
    public void addRequest(Request request) {
	requests.add(request);
    }

    /**
     * Adds a volunteer to this location.
     * 
     * @param volunteer
     *            the Volunteer to be added to this location
     */
    public void addVolunteer(Volunteer volunteer) {
	volunteers.add(volunteer);
    }

    /**
     * Gets the name of this location.
     * 
     * @return the String name of this location
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the set of requests at this location.
     * 
     * @return a HashSet<Request> of pending requests at this location
     */
    public HashSet<Request> getRequests() {
	return requests;
    }

    /**
     * Gets the set of volunteers at this location.
     * 
     * @return a HashSet<Volunteer> of available volunteers at this location
     */
    public HashSet<Volunteer> getVolunteers() {
	return volunteers;
    }

    /**
     * Gets the pair of values that give the (x, y) coordinates of this location.
     * 
     * @return a two-dimensional double array with the (x, y) coordinates of this location
     */
    public double[] getXY() {
	return new double[] { x, y };
    }

    /**
     * Removes a request from the set of requests at this location.
     * 
     * @param request
     *            the Request to be removed
     */
    public void removeRequest(Request request) {
	requests.remove(request);
    }

    /**
     * Removes a volunteer from the set of volunteers at this location.
     * 
     * @param volunteer
     *            the Volunteer to be removed
     */
    public void removeVolunteer(Volunteer volunteer) {
	volunteers.remove(volunteer);
    }
    
    public double calculateDistance(double[] xy1) {
    	double[] xy2 = this.getXY();
    	double result = Math.sqrt(Math.pow(xy1[0] - xy2[0], 2) + Math.pow(xy1[1] - xy2[1], 2));
    	return result;
    }
}
