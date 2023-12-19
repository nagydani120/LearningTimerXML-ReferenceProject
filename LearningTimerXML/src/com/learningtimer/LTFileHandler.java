package com.learningtimer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.learningtimer.dataStoreObjects.OneDay;
import com.learningtimer.dataStoreObjects.Project;
import com.learningtimer.dataStoreObjects.TimeSession;
import com.learningtimer.windows.MainWindow;

//@ formatter:off
/*
 * The LT File Handler handles all of the manipulation with the file or loads the data from the file
 * The basic of the data saving is .xml file.
 * Construction -> 
 * 
 *		(store the settings and root element)		---root--- -> Attr: "isProgressBarVisible" / "progressTimeSet"
 *		(the dates and time sum)					 --dates-- -> Attr: "TimeSum" / "ProgressPercent" 
 *		(sessions and data)							  -sess-   -> Attr: "Archived" / "Project" / "TimePassed" / "TimeWhenStarted" 	
 *
 * Its a date based data storing, every date has time sessions, every time session has a connection to project
 * (The project is just an attribute, the method what is responsible for reading the project list just reads
 * all the sessions and if it founds a project name it stores in HashSet (to avoid duplicate to get project name many times))
 * 
 * (((The timeSessions under 00:00:01 is not showing in table, that function is used to store the empty projects in Project list,
 * the project gets an time session with 00:00:00, but it is just for avoid that problem if a project is created but has no 
 * time session yet and the user exits the program.Alias it has to be stored right after creating the project)))
 * */
//@ formatter:on
public class LTFileHandler extends MainWindow {
	private static Document doc = null;
	private static File file;
	public static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss"); // pattern used in the .xml
																							// to save times

	// the LearningTimer creates the ltdata.xml in the same directory where the jar
	// file(after exporting) is located
	static {
		String jarPath = LTFileHandler.class.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.getPath();
		File fileDirectory = new File(jarPath).getParentFile();
		file = new File(fileDirectory, "ltdata.xml");
	
		
	}

	public static void dataWriter(OneDay day, TimeSession timeSessionObject) {

		try {

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			if (!file.isFile()) { // create or read the file.
				doc = initializeFile();
			} else {
				doc = db.parse(file);
			}

			if ((Element) doc.getFirstChild() == null || doc.getElementsByTagName("D" + LocalDate.now())
					.getLength() == 0) { // if its first time run or the date not exists yet
				doc = appendToDocument(day, timeSessionObject);
			} else {
				/*
				 * This is used if the day exists, it appends to date the timeSession as child
				 * and adds the time passed to total sum attribute of the day,the progress
				 * percent... By default all project is unarchived.
				 **/
				Element dateThatExists = (Element) doc.getElementsByTagName("D" + LocalDate.now())
						.item(0);

				// SETTING THE TIME SUM
				Attr timeSum = dateThatExists.getAttributeNode("TimeSum"); // GETS THE TIME SUM
				LocalTime timeSumResult = LocalTime.parse(timeSum.getNodeValue());
				Duration timeSumBeforeInDuration = Duration.ZERO;
				day.getTimeSessions()
						.stream()
						.map(e -> e.getTimePassed())
						.map(t -> timeSumBeforeInDuration.plusHours(t.getHour())
								.plusMinutes(t.getMinute())
								.plusSeconds(t.getSecond()));

				timeSumResult = timeSumResult.plus(timeSumBeforeInDuration);
				timeSumResult = timeSumResult.plusSeconds(timeSessionObject.getTimePassed()
						.toSecondOfDay());
				timeSum.setValue(timeSumResult.format(timeFormat));

				// Setting the progress percent (progress before from the .xml + the actual
				// sessions time
				// -> this method can just adjust and calculate in real time
				Attr progressPercent = dateThatExists.getAttributeNode("ProgressPercent");
				progressPercent.setValue(Double.parseDouble(progressPercent.getValue())
						+ timeSessionObject.getTimeSessionPercent() + "");

				// Setting the remaining
				Element timeSession = doc.createElement("Sess");
				timeSession.setAttribute("TimePassed", timeSessionObject.getTimePassed()
						.format(timeFormat));
				timeSession.setAttribute("TimeWhenTimerStarted", timeSessionObject.getTimeWhenStarted()
						.toString());
				timeSession.setAttribute("Project", timeSessionObject.getProject()
						.getProjectName());
				timeSession.setAttribute("Archived", "" + timeSessionObject.isArchived());
				dateThatExists.appendChild(timeSession);
			}
			// write all the changed data to the file
			writeOutDoc(doc);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private static Document initializeFile() // if its first run,or after "clear" function adds the root element to .xml
			throws Exception {
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.newDocument();
		Element root = doc.createElement("root");

		root.setAttribute("progressBarVisible", "true");
		root.setAttribute("progressTimeSet", "" + 18000);
		doc.appendChild(root);

		writeOutDoc(doc);
		return doc;
	}

	/*
	 * This method is used when the day(actual day) not exists yet in logfile
	 */
	private static Document appendToDocument(OneDay day, TimeSession timeSessionObject) {
		Node root = doc.getDocumentElement();
		Element date = doc.createElement("D" + day.getDate()
				.toString());
		date.setAttribute("TimeSum", timeSessionObject.getTimePassed()
				.format(timeFormat));
		date.setAttribute("ProgressPercent", "" + day.getDailyProgressPercent());
		Element timeSession = doc.createElement("Sess");
		timeSession.setAttribute("Project", timeSessionObject.getProject()
				.getProjectName());
		timeSession.setAttribute("TimePassed", timeSessionObject.getTimePassed()
				.format(timeFormat));
		timeSession.setAttribute("TimeWhenTimerStarted", timeSessionObject.getTimeWhenStarted()
				.toString());
		timeSession.setAttribute("Archived", "" + timeSessionObject.isArchived());

		date.appendChild(timeSession);
		root.appendChild(date);
		return doc;
	}

	public static Map<LocalDate, OneDay> loadData() {
		// This method reads all of the values what is saved before except the settings
		// ALL the values of the data from xml is stored in MAP object!!

		// just for safety purpose if the file not existing because of something
		// this if statetement gets true and creates the file
		Map<LocalDate, OneDay> allDays = new LinkedHashMap<>();
		if (!file.isFile()) {
			try {
				initializeFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return allDays; // ???? i changed the return null value
		}
		try {

			Document loadedData = readDoc();
			Element root;
			OneDay thisDay;
			// getting the root element
			if ((root = (Element) loadedData.getFirstChild()) != null) {
				// getting all child nodes
				NodeList childNodes = root.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					if (childNodes.item(i)
							.getNodeType() == 1) { // just if its the date obj

						Element dateElement = (Element) childNodes.item(i);

						LocalDate date = LocalDate.parse(dateElement.getNodeName()
								.substring(1)); // the first letter is "D",thats why substring is needed
						thisDay = new OneDay(date); // creating the date obj

						if (dateElement.hasChildNodes()) {
							// gets the progress percent from attribute
							double progressPercentOfThisDay = Double
									.parseDouble(dateElement.getAttribute("ProgressPercent"));
							LocalTime timeSum = LocalTime.parse(dateElement.getAttribute("TimeSum"));

							thisDay.setTimeSum(timeSum);
							thisDay.setDailyProgressPercent(progressPercentOfThisDay);

							// getting the nodeList what is named "Sess" (all session attributes)
							NodeList sessions = dateElement.getElementsByTagName("Sess");

							for (int j = 0; j < sessions.getLength(); j++) {

								Element timeSession = (Element) sessions.item(j);
								Project project = new Project(timeSession.getAttribute("Project"));
								LocalTime timePassed = LocalTime.parse(timeSession.getAttribute("TimePassed"));
								LocalDateTime timeWhenStarted = LocalDateTime
										.parse(timeSession.getAttribute("TimeWhenTimerStarted"));
								Boolean archived = Boolean.parseBoolean(timeSession.getAttribute("Archived"));

								// getting all info what is needed the time session and adding to time session
								// array list
								thisDay.getTimeSessions()
										.add(new TimeSession(timeWhenStarted, timePassed, project, archived));

							}

						}
						allDays.put(date, thisDay); // -> put the red info to the map object.
					}
				}

			}

		} catch (Exception e) {
			System.err.println("Load data error. (LTF File Handler)");
			e.printStackTrace();

		}
		return allDays;
	}

	public static void removeProject(Project p) {
		try {
			Document doc = readDoc();

			Node root = doc.getFirstChild();
			Node node = root.getFirstChild();

			do {
				if (node.getNodeType() == 1) {
					Element date = (Element) node;
					NodeList sessions = date.getElementsByTagName("Sess");
					for (int i = 0; i < sessions.getLength(); i++) {
						Attr att = ((Element) sessions.item(i)).getAttributeNode("Project");
						if (att.getValue()
								.equals(p.getProjectName())) {
							date.removeChild(sessions.item(i));
							i--;
						}
					}
				}
			} while ((node = node.getNextSibling()) != null);

			writeOutDoc(doc);
			LTTableHandler.fillMainTableWithData(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setArchived(Project p, boolean archive) {
		Document doc = readDoc();

		Node root = doc.getFirstChild();
		Node node = root.getFirstChild();

		do {
			if (node.getNodeType() == 1) {
				Element date = (Element) node;
				NodeList sessions = date.getElementsByTagName("Sess");
				for (int i = 0; i < sessions.getLength(); i++) {
					Attr att = ((Element) sessions.item(i)).getAttributeNode("Project");
					Attr archived = ((Element) sessions.item(i)).getAttributeNode("Archived");
					if (att.getValue()
							.equals(p.getProjectName())) {
						archived.setValue(Boolean.toString(archive)); // Setting the archive value in xml(true or
																		// false)
					}
				}
			}
		} while ((node = node.getNextSibling()) != null);

		writeOutDoc(doc);
		LTTableHandler.fillMainTableWithData(model);
	}

	public static void saveSettings(boolean isPiped, int progressInSeconds) {
		Document doc = readDoc();

		Element root = (Element) doc.getFirstChild();
		root.setAttribute("progressBarVisible", Boolean.toString(isPiped));
		root.setAttribute("progressTimeSet", "" + progressInSeconds);

		writeOutDoc(doc);
		LTTableHandler.fillMainTableWithData(model);

	}

	public static Object[] loadSettings() {

		Document doc = readDoc();

		Element root = (Element) doc.getFirstChild();

		String progressBarVisible = root.getAttribute("progressBarVisible");
		String progressTimeSet = root.getAttribute("progressTimeSet");

		boolean isPiped = Boolean.parseBoolean(progressBarVisible);
		int progressTimeInSeconds = Integer.parseInt(progressTimeSet);

		return new Object[] { progressTimeInSeconds, isPiped };
	}

	private static Document readDoc() {
		Document doc = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = db.parse(file);
		} catch (Exception e) {
			System.err.println("Error parsing the .xml file.");
			e.printStackTrace();
		}
		return doc;
	}

	private static void writeOutDoc(Document doc) {
		try {
			Transformer tf = TransformerFactory.newInstance()
					.newTransformer();
			DOMSource ds = new DOMSource(doc);
			StreamResult sr = new StreamResult(file);
			tf.transform(ds, sr);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void clearFile() {
		File file = new File("ltdata.xml");
		if (file.isFile()) {
			file.delete();
		}
		try {
			initializeFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}