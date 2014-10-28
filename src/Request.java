
/**
 * The Request class maintains the information relevant to a single request. This information includes the name of the
 * person requesting the walk, the start location and destination location, and the value to be awarded when the walk
 * completes.
 * 
 * @author dhelia
 * @author jtk
 * @version 2014
 */
public class Request {
    private String name;
    private Location start;
    private Location destination;
    private int value;

    /**
     * Constructor that creates a request with the given parameters. Adds this request to the start location and to the
     * model.
     * 
     * @param model
     *            the Model where the request is stored
     * @param name
     *            the String name of the person making the request
     * @param start
     *            the Location of the start of this request
     * @param destination
     *            the Location of the destination of this request
     * @param value
     *            the int value awarded when this request completes.
     */
    public Request(Model model, String name, Location start, Location destination, int value) {
	this.name = name;
	this.start = start;
	this.destination = destination;
	this.value = value;

	start.addRequest(this);
	model.addRequest(this);
    }

    /**
     * Gets the destination location of this request.
     * 
     * @return the Location of the destination of this request
     */
    public Location getDestination() {
	return destination;
    }

    /**
     * Gets the name of the person making this request.
     * 
     * @return the String name of the person making this request
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the start location of this request.
     * 
     * @return the Location of the start of this request
     */
    public Location getStart() {
	return start;
    }

    /**
     * Gets the value scored when this request completes.
     * 
     * @return the int value scored when this request completes.
     */
    public int getValue() {
	return value;
    }
}
