import java.util.Iterator;


/**
 * The Volunteer class maintains the information relevant to a SafeWalk volunteer. This information includes the name of
 * the volunteer, and the changing state of the volunteer (including current score and movement information).
 * 
 * @author dhelia
 * @author jtk
 * @version 2014
 */

public class Volunteer {
	private Model model;
	private String name;
	private int score;
	private Location location; // location of this volunteer; only meaningful if not currently moving

	// Parameters used to track movement of this volunteer...
	String requester = null;
	private double[] start;
	private double[] destination;
	private Location destinationLocation;
	private Location startLocation;
	private boolean inProcess = false;

	/**
	 * Constructor that creates a volunteer with the given name and registers it with the model. Sets current score and
	 * current (non-null) location, which implies the volunteer is not moving.
	 * 
	 * @param model
	 *            the Model in which this volunteer is located
	 * @param name
	 *            the String name of the volunteer
	 */
	public Volunteer(Model model, String name, int score, Location location) {
		this.model = model;
		this.name = name;
		this.score = score;
		this.location = location;

		location.addVolunteer(this);
		model.addVolunteer(this);
	}

	/**
	 * Gets the current location of the volunteer. (Only meaningful if the volunteer is not currently moving.)
	 * 
	 * @return the Location where the volunteer is currently located.
	 */
	public Location getCurrentLocation() {
		return location;
	}

	public void setCurrentLocation(Location location) {
		this.location = location;
	}

	/**
	 * Gets the current (x, y) coordinates of this volunteer. The position is computed dynamically (each time this
	 * method is called). The current position is a linear interpolation between the start and destination positions
	 * based on the current elapsed time and the expected transit time.
	 * 
	 * If the elapsed time is greater than the required transit time for the movement, then this method should return
	 * the destination coordinates. (That is, don't overshoot the destination.)
	 * 
	 * @return coordinates (x, y) double of the current position of this volunteer
	 */
	public double[] getCurrentPosition() {
		if (this.getCurrentLocation() == null) {
			return null;
		}
		//    	double xLength = destination[0] - start[0];
		//    	double yLength = destination[1] - start[1];
		//
		//    	double[] position = new double[2];
		//
		//    	long time = System.currentTimeMillis() - timeStarted;
		//    	if (time >= timeRequired) {
		//    		position[0] = destination[0];
		//    		position[1] = destination[1];
		//    	} else {
		//    		position[0] = xLength * time / timeRequired + start[0];
		//    		position[1] = yLength * time / timeRequired + start[1];
		//    	}
		//    	return position;
		//    	}
		else
			return this.getCurrentLocation().getXY();

	}

	/**
	 * Gets the name of this volunteer.
	 * 
	 * @return the String name of this volunteer
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the current score of this volunteer.
	 * 
	 * @return the int value of this volunteer's current score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Gets the name of the requester with whom this volunteer is walking.
	 * 
	 * @return the String name of the requester being walked
	 */
	public String getRequester() {
		return requester;
	}

	/**
	 * Gets the starting coordinates of this volunteer's current movement.
	 * 
	 * @return a two-dimensional double array with the (x, y) coordinates of the start location
	 */
	public double[] getStart() {
		return start;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	/**
	 * Gets the destination coordinates of this volunteer's current movement.
	 * 
	 * @return a two-dimensional double array with the (x, y) coordinates of the destination location
	 */
	public double[] getDestination() {
		return destination;
	}

	public Location getDestinationLocation() {
		return destinationLocation;
	}

	/**
	 * Starts this volunteer on the move. Keeps track of starting and ending coordinates, starting and (projected)
	 * ending times. Subsequent calls to getCurrentPosition return the (x, y) coordinates of this volunteer, giving the
	 * amount of time elapsed since the movement started.
	 * 
	 * @param destination the Location of the final destination
	 * @param timeRequired the long number of milliseconds required to make the trip
	 */
	public void startMoving(Location destination) {
		startMovement(location.getXY(), destination.getXY(), null);
		// update currentLocation
		destinationLocation = destination;
	}

	/**
	 * Starts this volunteer on a safewalk. Keeps track of starting and ending coordinates, starting and (projected)
	 * ending times, and the name of the requester. Subsequent calls to getCurrentPosition return the (x, y) coordinates
	 * of this volunteer, giving the amount of time elapsed since the movement started.
	 * 
	 * @param request the Request that contains the safewalk information (start, destination, requester)
	 * @param timeRequired the long number of milliseconds required to make the trip
	 */
	public void startWalking(Request request) {
		model.removeRequest(request);
		startMovement(request.getStart().getXY(), request.getDestination().getXY(), request.getName());
		// update currentLocation
		destinationLocation = request.getDestination();
	}

	/**
	 * Records that this volunteer is now on the move, including the current time in milliseconds as the start time for
	 * this move.
	 * 
	 * @param start
	 *            coordinates (x, y) double of start of move
	 * @param destination
	 *            coordinates (x, y) double of destination of move
	 * @param timeRequired
	 *            number of milliseconds required to complete the trip
	 * @param requester
	 *            the String name of the requester (null if moving only)
	 */
	private void startMovement(double[] start, double[] destination,
			String requester) {
		if (location != null)
			location.removeVolunteer(this);
		location = null;

		this.start = start;
		this.destination = destination;
		this.requester = requester;

		System.currentTimeMillis();
	}

	public boolean getInProcessSituation() {
		return inProcess;
	}

	public void setInProcessSituation(boolean inProcess) {
		this.inProcess = inProcess;
	}

	public void makeADecision (Model model) {
		// find the highest value
		Iterator<Request> requests = model.getRequests().iterator();
		while (requests.hasNext()) {
			Request request = requests.next();
			int compare = 0;
			Request decision = null;
			if (request.getValue() > compare) {
				compare = request.getValue();
				decision = request;
			}
			requester = decision.getName();
			startLocation = decision.getStart();
			start = startLocation.getXY();
			destinationLocation = decision.getDestination();
			destination = destinationLocation.getXY();
			
		}
		
		/*// find the closest location with requests
				Iterator<Request> requests = model.getRequests().iterator();
				double compare = 100000000.0;
				String locationName = null;
				double[] current = this.getCurrentLocation().getXY();
				while (requests.hasNext()) {
					Request request = requests.next();
					double[] start = request.getStart().getXY();
					double distance = Math.sqrt(Math.pow(current[0] - start[0],
							2) + Math.pow(current[1] - start[1], 2));
					if (distance < compare) {
						compare = distance;
						locationName = request.getStart().getName();
					}
				}
				startLocation = model.getLocationByName(locationName);
				start = startLocation.getXY();

				// find the highest score in this location
				Iterator<Request> insideRequests = model.getLocationByName(locationName).getRequests().iterator();
				int compareInt = 0;
				Request decision = null;
				while (insideRequests.hasNext()) {
					Request request = insideRequests.next();
					if (request.getValue() > compareInt) {
						compareInt = request.getValue();
						decision = request;
					}
				}
				requester = decision.getName();
				destination = decision.getStart().getXY();
				destinationLocation = decision.getDestination();*/
	}
}

