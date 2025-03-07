package edu.csupomona.cs480.controller;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.chrono.BuddhistChronology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.csupomona.cs480.App;
import edu.csupomona.cs480.data.User;
import edu.csupomona.cs480.data.provider.UserManager;

import com.google.common.base.Joiner;

import org.apache.commons.math3.random;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.RandomSource;
/**
 * This is the controller used by Spring framework.
 * <p>
 * The basic function of this controller is to map
 * each HTTP API Path to the correspondent method.
 *
 */

@RestController
public class WebController {

	/**
	 * When the class instance is annotated with
	 * {@link Autowired}, it will be looking for the actual
	 * instance from the defined beans.
	 * <p>
	 * In our project, all the beans are defined in
	 * the {@link App} class.
	 */
	@Autowired
	private UserManager userManager;

	/**
	 * This is a simple example of how the HTTP API works.
	 * It returns a String "OK" in the HTTP response.
	 * To try it, run the web application locally,
	 * in your web browser, type the link:
	 * 	http://localhost:8080/cs480/ping
	 */
	@RequestMapping(value = "/cs480/ping", method = RequestMethod.GET)
	String healthCheck() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return "OK";
	}

	/**
	 * This is a simple example of how to use a data manager
	 * to retrieve the data and return it as an HTTP response.
	 * <p>
	 * Note, when it returns from the Spring, it will be
	 * automatically converted to JSON format.
	 * <p>
	 * Try it in your web browser:
	 * 	http://localhost:8080/cs480/user/user101
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.GET)
	User getUser(@PathVariable("userId") String userId) {
		User user = userManager.getUser(userId);
		return user;
	}

	/**
	 * This is an example of sending an HTTP POST request to
	 * update a user's information (or create the user if not
	 * exists before).
	 *
	 * You can test this with a HTTP client by sending
	 *  http://localhost:8080/cs480/user/user101
	 *  	name=John major=CS
	 *
	 * Note, the URL will not work directly in browser, because
	 * it is not a GET request. You need to use a tool such as
	 * curl.
	 *
	 * @param id
	 * @param name
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.POST)
	User updateUser(
			@PathVariable("userId") String id,
			@RequestParam("name") String name,
			@RequestParam(value = "major", required = false) String major) {
		User user = new User();
		user.setId(id);
		user.setMajor(major);
		user.setName(name);
		userManager.updateUser(user);
		return user;
	}

	/**
	 * This API deletes the user. It uses HTTP DELETE method.
	 *
	 * @param userId
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.DELETE)
	void deleteUser(
			@PathVariable("userId") String userId) {
		userManager.deleteUser(userId);
	}

	/**
	 * This API lists all the users in the current database.
	 *
	 * @return
	 */
	@RequestMapping(value = "/cs480/users/list", method = RequestMethod.GET)
	List<User> listAllUsers() {
		return userManager.listAllUsers();
	}

	/*********** Web UI Test Utility **********/
	/**
	 * This method provide a simple web UI for you to test the different
	 * functionalities used in this web service.
	 */
	@RequestMapping(value = "/cs480/home", method = RequestMethod.GET)
	ModelAndView getUserHomepage() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("users", listAllUsers());
		return modelAndView;
	}
	/*
	 * New mapping made for Assignment #3
	 */
	@RequestMapping(value = "/cs480/adam", method = RequestMethod.GET)
	String adamsPage() {
		return "This page was made by Adam";
	}
	@RequestMapping(value = "/cs480/nick", method = RequestMethod.GET)
	String nicksPage() {
		return "Nick Soultanian";
	}
	@RequestMapping(value = "/cs480/annie", method = RequestMethod.GET)
	public ModelAndView anniesPage() {
		String anniesFlickr = "https://www.flickr.com/people/anniewuphotos/";
	    return new ModelAndView("redirect:" + anniesFlickr);
	}
	@RequestMapping(value = "/cs480/theresa", method = RequestMethod.GET)
	String theresasPage() {
		return "Theresa's page \n";
	}
	@RequestMapping(value = "/cs480/diane", method = RequestMethod.GET)
	String dianesPage() {
	    return "Diane's testing page";
	}
	
	/*
	 * New Mappings for Assignment #4
	 */
	@RequestMapping(value = "/cs480/youtubeLinks", method = RequestMethod.GET)
	String youtubeLinks() {
		Document doc = null;
		Elements links = null;
		String title = null;
        try {

            // need http protocol
            doc = Jsoup.connect("http://youtube.com").get();

            // get page title
            title = doc.title();

            // get all links
            links = doc.select(".yt-ui-ellipsis");
            

        } catch (IOException e) {
            e.printStackTrace();
        }
       
        return (title + links.toString());
	}
	
	@RequestMapping(value = "/cs480/guavaTest", method = RequestMethod.GET)
	String guavaJoiner() {
		String[] members = {"Nick", "Annie", "Diane", "Adam", "Theresa"};
		String result = Joiner.on(" | ").join(members);
		return result;
	}
	
	@RequestMapping(value = "/cs480/jodaTimeTest", method = RequestMethod.GET)
	String jodaTime() {
		// get current date in default time zone
	    DateTime date = new DateTime();
	    // Buddhist chronology
	    DateTime dateBuddhist = date.withChronology(BuddhistChronology.getInstance());
	    return "Current Year: " + date.getYear() + "\nCurrent Year in Buddhist Chronology: " + dateBuddhist.getYear();
	}
	@RequestMapping(value = "/cs480/commonsMath", method = RequestMethod.GET)
	String mathTime(){
		long seed = 17399225432L; // Fixed seed means same results every time 
		UniformRandomProvider rg = RandomSource.create(RandomSource.MT, seed);
		
		return "Here is your random number: " + seed
	}
	
}
