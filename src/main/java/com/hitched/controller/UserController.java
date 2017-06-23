package com.hitched.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hitched.model.Addresses;
import com.hitched.model.UserLogin;
import com.hitched.service.LocationService;
import com.hitched.service.MailService;
import com.hitched.service.SendMail;
import com.hitchedUtils.States;
import com.hitchedUtils.States.Days;
import com.hitched.service.HitchedService;
import com.hitched.model.Hitched;

@Controller
@SessionAttributes("emailAddress")
public class UserController {

	final static Logger logger = Logger.getLogger(UserController.class);
	// static final Logger logger = (Logger)
	// LoggerFactory.getLogger(UserController.class);

	public UserController() {
		logger.info("in UserController servelet");
	}

	@Autowired
	private LocationService locationService;

	
	@Autowired
	private MailService mailServices;

	@Autowired
	DataSource dataSource;

	SendMail sendMail = new SendMail();

	SendMail mailService = new SendMail();

	@Autowired
	private HitchedService hitchedService;

	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public String service(Model model) {
		model.addAttribute("message", "Welcome to the Gettin' Hitched website ");

		return "services";
	}

	@RequestMapping(value = "/signups", method = RequestMethod.GET)
	public String signups(Model model) {
		model.addAttribute("signupMember", new UserLogin());
		logger.info("#######################signup out : ");

		return "signups";
	}

	@RequestMapping(value = "/addresses3", method = RequestMethod.GET)
	public String addresses(Model model) {
		model.addAttribute("addresses", new Addresses());

		return "addresses";
	}
	
	@RequestMapping(value = "/addresses4", method = RequestMethod.POST)
	public String addresses(@Valid @ModelAttribute("addresses") Addresses addresses, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "addresses";
		}
		try {
			// locationService.createAddress(address);
			// model.addAttribute("users", locationService.getAllAddresses());
			model.addAttribute("success", "Address added for " + addresses.getAddress() + " Address");

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return "addresses";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Address Not Added for " + addresses.getAddress() + " Address");

			e.printStackTrace();
			return "addresses";
		}

		return "about";
	}	

	@RequestMapping("signupMember")
	public String signupMember(@ModelAttribute("signupMember") UserLogin userLogin, BindingResult bindingResult,
			ModelMap model) throws ParseException {
		String msg = "Welcome to Gettin' Hitched. Please Login now <a href='http://localhost:8080/hitched/login'>Login </a>";
		try {
			if (hitchedService.findUser(userLogin.getEmailAddress()) == false) {
				if (userLogin.getPassword().equals(userLogin.getPasswordConfirm())) {
					hitchedService.createUser(userLogin);
					model.addAttribute("message",
							"Welcome " + userLogin.getFname() + " Login now <a href='login'>Login </a>");

					try {
						sendMail.sendMails(userLogin.getEmailAddress(), "Welcome to Gettin' Hitched. ", msg);
						model.addAttribute("success",
								"Confirmation email has been sent to " + userLogin.getEmailAddress() + " thanks");

					} catch (Exception e) {
						model.addAttribute("error",
								"Error sending confirmation email to " + userLogin.getEmailAddress());
					}

					return "login";
				} else {
					model.addAttribute("error", "Password not matching ");
				}

			} else {
				model.addAttribute("error", "A user exists with the supplied email " + userLogin.getEmailAddress()
						+ " <a href='forgotpassword'> Forgot Password click here </a>");

			}

		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			logger.error("Sorry, Unexpected error occured!", e);
			e.printStackTrace();

		}

		return "signups";

	}

	@RequestMapping("signup")
	public String signup(@ModelAttribute("signup") UserLogin userLogin, BindingResult bindingResult, ModelMap model)
			throws ParseException {
		String msg = "Welcome to the Gettin' Hitched website. Please Login now <a href='http://localhost:8080/hitched/login'>Login </a>";

		model.addAttribute("message", "Hello " + userLogin.getFname() + " Login now <a href='login'>Login </a>");

		return "login";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		logger.info("someone logg in out : ");
		if (logger.isDebugEnabled()) {
			logger.debug("logout out : ");
		}

		model.addAttribute("msg", "You have been logged out!");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {

		logger.info("called  log in : ");

		if (logger.isDebugEnabled()) {
			logger.debug("This is debug : ");
		}

		logger.debug("someone logg in out : ");

		model.addAttribute("loginMember", new UserLogin());
		model.addAttribute("msg", "Login to explore the complete features!");
		return "login";
	}

	@RequestMapping(value = "/loginUser", method = RequestMethod.GET)
	public String loginuser(Model model) {

		model.addAttribute("newlogin", new UserLogin());
		model.addAttribute("msg", "New login for test!");
		return "loginUser";
	}

	// @RequestMapping(value = "/newlogin", method = RequestMethod.POST)
		// since 'POST' is in the jsp, I don't need it here
	@RequestMapping("newlogin")
	public String newlogin(@ModelAttribute("newlogin") UserLogin user, ModelMap model) throws ParseException {

		logger.info("user logged in : " + user.getEmailAddress());

		try {

			UserLogin userdetail = hitchedService.getUserByEmailId(user.getEmailAddress());

			if (validateLogin(user.getEmailAddress(), user.getPassword())) {
				model.addAttribute("success", "Welcome " + userdetail.getFname());
				model.addAttribute("attribs", userdetail);
				model.addAttribute("user", userdetail.getFname());
				model.addAttribute("role", userdetail.getRoles());

				return "profile";

			} else {
				model.addAttribute("error", "Invalid credentials try again ");
			}
		} 
		/* catch (EmptyResultDataAccessException e) {
			model.addAttribute("error",
					"User " + user.getEmailAddress() + " does not exists <a href='signups'>Sign up </a>");
		} */ 
		catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}

		return "login";
	}

	public boolean validateLogin(String uname, String pswd) {
		UserLogin dbCredentials = hitchedService.getUserByEmailId(uname);
		if (!(uname.equals(dbCredentials.getEmailAddress()) && pswd.equals(dbCredentials.getPassword()))) {
			return false;
		} else
			return true;
	}







	@RequestMapping(value = "/loginMember", method = RequestMethod.POST)
	public String login(@ModelAttribute("loginMember") UserLogin user, ModelMap model) throws ParseException {

		logger.info("user logged in : " + user.getEmailAddress());

		try {
			logger.info("This is info : " + user.getEmailAddress());
			UserLogin userdetail = hitchedService.getUserByEmailId(user.getEmailAddress());
			if (hitchedService.findUser(user.getEmailAddress()) == true) {
				if (userdetail.getEmailAddress().equals(user.getEmailAddress())
						&& userdetail.getPassword().equals(user.getPassword())) {
					{
						model.addAttribute("success", "Welcome " + userdetail.getFname() + ". This is a secure zone! ");
						model.addAttribute("attribs", userdetail);
						model.addAttribute("user", userdetail.getFname());
						model.addAttribute("role", userdetail.getRoles());

						return "profile";
					}
				} else {
					model.addAttribute("error", "Invalid credentials try again ");
				}
			}
		} catch (EmptyResultDataAccessException e) {
			model.addAttribute("error",
					"User " + user.getEmailAddress() + " does not exists <a href='signups'>Sign up </a>");
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}

		return "login";
	}

	@RequestMapping("deleteUser")
	public String searchStudents(@RequestParam long id, @RequestParam long loggedin, ModelMap model) {

		try {
			UserLogin userdetail = hitchedService.getUserById(id);
			if (userdetail.getRoles().equalsIgnoreCase("super")) {
				model.addAttribute("error", "cannot delete user with Super role");
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			} else {
				hitchedService.deleteUserById(id);
				model.addAttribute("success", "User deleted successfully");
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			}
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			model.addAttribute("users", hitchedService.getAllusers());
			model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
			model.addAttribute("id", loggedin);
			model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			e.printStackTrace();

		}

		return "admin";
	}

	// Example used in class
	@RequestMapping("deleteExample")
	public String searchStudents2(@RequestParam long id, @RequestParam long loggedin, ModelMap model) {

		try {
			UserLogin userdetail = hitchedService.getUserById(id);
			if (userdetail.getRoles().equalsIgnoreCase("super")) {
				model.addAttribute("error", "cannot delete user with Super role");
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			} else {
				hitchedService.deleteUserById(id);
				model.addAttribute("success", "User deleted successfully");
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			}
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			model.addAttribute("users", hitchedService.getAllusers());
			model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
			model.addAttribute("id", loggedin);
			model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			e.printStackTrace();

		}

		return "admin";
	}

	@RequestMapping("editUser")
	public String editUser(@RequestParam long loggedin, @ModelAttribute UserLogin userLogin, ModelMap model) {

		try {
			if (hitchedService.getUserById(userLogin.getId()).getRoles().equalsIgnoreCase("super")) {
				model.addAttribute("error", "cannot edit a user with super role");
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
				return "admin";
			} else {
				model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			}
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));
			model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
			model.addAttribute("id", loggedin);
			model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());

			e.printStackTrace();

		}

		return "dataEdit";
	}

	@RequestMapping("userEdits")
	public String userEdits(@RequestParam long id, @ModelAttribute UserLogin userLogin, ModelMap model) {

		try {
			model.addAttribute("attribs", hitchedService.getUserById(id));
			model.addAttribute("user", hitchedService.getUserById(id).getFname());
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}

		return "UserUpdateForm";
	}

	@RequestMapping("userEdit")
	public String userEdit(@ModelAttribute("userEdit") UserLogin userLogin, BindingResult bindingResult, ModelMap model)
			throws ParseException {
		try {
			hitchedService.updateUserById(userLogin, userLogin.getId());
			model.addAttribute("message", "Update Success");
			model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));
			model.addAttribute("user", hitchedService.getUserById(userLogin.getId()).getFname());
			model.addAttribute("role", hitchedService.getUserById(userLogin.getId()).getRoles());

			return "profile";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}
		return "UserUpdateForm";
	}

	@RequestMapping("saveUser")
	public String adminEdit(@ModelAttribute("saveUser") UserLogin userLogin, @RequestParam long loggedin,
			BindingResult bindingResult, ModelMap model) throws ParseException {
		try {
			hitchedService.updateUserById(userLogin, userLogin.getId());
			model.addAttribute("message", "User " + userLogin.getFname() + " " + userLogin.getLname() + " Updated");
			model.addAttribute("users", hitchedService.getAllusers());

			model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
			model.addAttribute("id", loggedin);
			model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
			return "admin";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}
		return "dataEdit";
	}

	@RequestMapping("admin")
	public String admin(@RequestParam long id, @ModelAttribute UserLogin user, Model model) {

		try {
			UserLogin userdetail = hitchedService.getUserById(id);

			model.addAttribute("users", hitchedService.getAllusers());
			model.addAttribute("role", hitchedService.getUserById(id).getRoles());
			model.addAttribute("user", userdetail.getLname());
			model.addAttribute("id", id);

		} catch (EmptyResultDataAccessException e) {
			model.addAttribute("error", " Access denied ");
			return "login";
		} catch (Exception e) {
			model.addAttribute("user", user.getLname());
			model.addAttribute("id", id);
			model.addAttribute("error", " Access denied ");
			e.printStackTrace();
			return "login";
		}

		return "admin";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String profile(@RequestParam long id, @ModelAttribute UserLogin user, Model model) {
		UserLogin userdetail = hitchedService.getUserById(id);

		model.addAttribute("success", "Welcome " + userdetail.getFname() + ". This is a secure zone! ");
		model.addAttribute("attribs", userdetail);
		model.addAttribute("user", userdetail.getFname());
		model.addAttribute("role", userdetail.getRoles());

		return "profile";
	}

	@RequestMapping("assignRole")
	public String assignrole(@ModelAttribute("assignRole") UserLogin userLogin, @RequestParam long loggedin,
			BindingResult bindingResult, ModelMap model) throws ParseException {
		try {
			String role = hitchedService.getUserById(userLogin.getId()).getRoles();
			if (role.equalsIgnoreCase("super")) {
				model.addAttribute("error", "cannot re-assign new roles to a user with a super role");
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
			} else {
				hitchedService.updaterole(userLogin, userLogin.getRoles(), userLogin.getId());
				model.addAttribute("users", hitchedService.getAllusers());
				model.addAttribute("role", hitchedService.getUserById(loggedin).getRoles());
				model.addAttribute("user", hitchedService.getUserById(loggedin).getLname());
				model.addAttribute("id", loggedin);
				model.addAttribute("success", "User " + hitchedService.getUserById(userLogin.getId()).getFname()
						+ " assigned role " + userLogin.getRoles());
			}
		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}
		return "admin";
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public String forgotpass(Model model) {
		return "forgotpassword";
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
	public String resetpass(Model model) {
		model.addAttribute("message", "Reset your password");

		return "resetpassword";
	}

	@RequestMapping(value = "/forgotpass", method = RequestMethod.POST)
	public String forgotpass(@ModelAttribute("forgotpass") Hitched hitched, BindingResult bindingResult,
			Model model) {

		String validemails = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		model.addAttribute("em", hitched.getEmailAddress());

		if (!hitched.getEmailAddress().matches(validemails)) {
			model.addAttribute("emailrror",
					"Invalid email " + hitched.getEmailAddress() + " use format johnsmith@scglobal.com ");
		} else

			try {
				if (hitchedService.findUser(hitched.getEmailAddress()) == true) {
					try {
						mailServices.sendpasswordemail(hitched.getEmailAddress());
						model.addAttribute("success",
								"Password reset instuction has been sent to " + hitched.getEmailAddress());
						model.addAttribute("emailrror", "");
						model.addAttribute("em", "");

					} catch (Exception e) {
						model.addAttribute("error", "Error sending email to " + hitched.getEmailAddress());
					}

				} else {
					model.addAttribute("success", "user not found " + hitched.getEmailAddress());
				}
			} catch (Exception e) {
				model.addAttribute("sendmailfail", "Error sending email  ");
				e.printStackTrace();
			}

		return "login";
	}

	@RequestMapping(value = "/getnewpass", method = RequestMethod.POST)
	public String setpass(@ModelAttribute("getnewpass") UserLogin userLogin,
			@RequestParam(value = "id", required = false) long id, BindingResult bindingResult, ModelMap model)
			throws ParseException {
		try {
			UserLogin userdetail = hitchedService.getUserByEmailId(userLogin.getEmailAddress());
			if (hitchedService.findUser(userLogin.getEmailAddress()) == true) {
				if (userLogin.getPassword().equals(userLogin.getPasswordConfirm())) {

					hitchedService.passReset(userLogin, userLogin.getPassword(), userLogin.getEmailAddress());
					model.addAttribute("success", "password reset success, please keep your password safely.");
					model.addAttribute("attribs", userdetail);
					model.addAttribute("user", userdetail.getFname());
					model.addAttribute("role", userdetail.getRoles());

					try {
						mailService.sendMails(userLogin.getEmailAddress(), "Hitched Password",
								"Your password was reset contact admin if you did not do it");

					} catch (Exception e) {
						if (id < 0) {
							model.addAttribute("error", "Error sending email to " + userdetail.getEmailAddress()
									+ " Please check your network");
							return "resetpassword";
						}
						model.addAttribute("error", "Error sending email to " + userdetail.getEmailAddress()
								+ " Please check your network");
					}
				} else {

					if (id < 0) {
						model.addAttribute("error", "Password not matching ");
						return "resetpassword";
					} else

						model.addAttribute("attribs", userdetail);
					model.addAttribute("user", userdetail.getFname());
					model.addAttribute("role", userdetail.getRoles());
					model.addAttribute("error", "Password not matching ");
				}
			} else {
				model.addAttribute("error", "No user found, <a href='signups'>Sign up </a>");
				return "login";
			}

		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}
		return "profile";
	}

	@RequestMapping("changeSubscriptionType")
	public String changeMembershipType(@ModelAttribute("changeSubscriptionType") UserLogin userLogin,
			BindingResult bindingResult, ModelMap model) throws ParseException {
		try {
			// long
			// creditcard=hitchedService.getUserById(userLogin.getId()).getCard();

			hitchedService.updateSubscription(userLogin, userLogin.getSubscription(), userLogin.getId());
			model.addAttribute("success", "Hi " + hitchedService.getUserById(userLogin.getId()).getFname()
					+ " Your subscription changed, your credit card on file was proccessed");
			model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));

		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}
		return "profile";
	}

	@RequestMapping("searchMembers")
	public String searchMembers(ModelMap model, @RequestParam("searchNames") String names,
			@RequestParam("id") long id) {

		try {
			// List<UserLogin> memberList = hitchedService.lookupMembers(names);
			if (hitchedService.lookupMembers(names).isEmpty()) {
				model.addAttribute("error", "There are no matches for this Look-up");
				model.addAttribute("role", hitchedService.getUserById(id).getRoles());
				model.addAttribute("user", hitchedService.getUserById(id).getLname());
				model.addAttribute("id", id);
			} else
				model.addAttribute("users", hitchedService.lookupMembers(names));
			model.addAttribute("role", hitchedService.getUserById(id).getRoles());
			model.addAttribute("user", hitchedService.getUserById(id).getLname());
			model.addAttribute("id", id);

		} catch (RuntimeException e) {
			model.addAttribute("error", "Unexpected error occured");
			e.printStackTrace();

		}

		return "admin";
	}

	@ModelAttribute("states")
	public List<States> populateStates() {
		return Arrays.asList(States.values());
	}

	@ModelAttribute("days")
	public Days[] day() {
		return Days.values();
	}
	
	@ModelAttribute("addresses")
	public List<Addresses> populateStates(Model model){
		return locationService.getAllAddresses();
	}
	


}
