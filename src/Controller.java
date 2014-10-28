import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
	private static Model model;
	private Connector connector;
	private static final String KEY = "k958930";
	private static final String NICKNAME = "Darrell";
	private static final String HOST = "pc.cs.purdue.edu";
	private static final int PORT = 1337;

	Controller(Model model) {
		Controller.model = model;
		connector = new Connector(HOST, PORT, String.format("connect %s", KEY),
				this);
	}

	public void volunteerStartAction() {
		Volunteer me = model.getVolunteerByName(NICKNAME);
		// if the volunteer is not in process, which means he just started the
		// game
		// or he moved to a place but the requester was already taken away

		// if the volunteer is in process which means he is going to walk the
		// requester
		if (!me.getInProcessSituation())
			me.makeADecision(model);

		// if the volunteer is at the location where he should start walking
		if (me.getCurrentLocation().equals(me.getStartLocation())) {
			connector.writeLine("walk " + me.getRequester());
			me.setInProcessSituation(false);
		}
		// move to the location of the request
		else {
			connector.writeLine("move " + model.getRequestByName(me.getRequester()).getStart().getName());
			me.setInProcessSituation(true);
		}
	}

	private void handleLocationMessage(String[] fields) {
		String buildingName = (String) fields[1];
		double x = Double.valueOf(fields[2]);
		double y = Double.valueOf(fields[3]);
		new Location(model, buildingName, x, y);
	}

	private void handleRequestMessage(String[] fields) {
		String name = fields[1];
		String fromBuildingName = fields[2];
		String toBuildingName = fields[3];
		int value = Integer.valueOf(fields[4]);
		new Request(model, name, model.getLocationByName(fromBuildingName),
				model.getLocationByName(toBuildingName), value);
	}

	private void handleVolunteerMessage(String[] fields) {
		String name = fields[1];
		String locationName = fields[2];
		int score = Integer.valueOf(fields[3]);
		double[] xy = model.getLocationByName(locationName).getXY();

		if (model.getVolunteers().contains(model.getVolunteerByName(name)))
			model.getVolunteerByName(name).setCurrentLocation(
					model.getLocationByName(locationName));
		else
			new Volunteer(model, name, score, new Location(model, locationName,
					xy[0], xy[1]));

		if (name.equals(NICKNAME))
			volunteerStartAction();
	}

	private void handleDeleteMessage(String[] fields) {
		try {
			model.removeVolunteer(model.getVolunteerByName(fields[1]));
		} catch (Exception e) { // "Volunteer " + messages[1] +
			// " is not in the list."
		}
	}

	private void handleResetMessage(String[] fields) {
		System.out.println("The system has been reset. All clients should reset internal information about the state of the system.");
	}

	private void handleWarningMessage (String[] fields) {
		String text = "";
		for (int i = 1; i < fields.length; i++) {
			text = text + fields[i];
		}
		System.out.println("WARNING TEXT: " + text);
	}

	private void handleErrorMessage (String[] fields) {
		String text = "";
		for (int i = 1; i < fields.length; i++) {
			text = text + fields[i];
		}
		System.out.println("ERROR TEXT:" + text);
	}


	@Override
	public void update(Observable o, Object arg) {
		synchronized (model.lock) {
			String message = (String) arg;
			String[] fields = message.split(" ");

			if (fields[0].equals("location"))
				handleLocationMessage(fields);
			else if (fields[0].equals("request"))
				handleRequestMessage(fields);
			else if (fields[0].equals("volunteer"))
				handleVolunteerMessage(fields);
			else if (fields[0].equals("delete"))
				handleDeleteMessage(fields);
			else if (fields[0].equals("error"))
				handleErrorMessage(fields);
			else if (fields[0].equals("reset"))
				handleResetMessage(fields);
			else if (fields[0].equals("warning"))
				handleWarningMessage(fields);
		}
	}

}