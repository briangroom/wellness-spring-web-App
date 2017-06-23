package com.hitched.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hitched.model.Addresses;
import com.hitched.model.Locations;
import com.hitched.model.UserLogin;
import com.hitched.service.LocationService;
import com.hitchedUtils.States;

@Controller
public class LocationController {
	
	@Autowired
	private LocationService locationService;

    @Autowired
	    DataSource dataSource;
    

	@ModelAttribute("states")
	public List<States> populateStates() {
		return Arrays.asList(States.values());
	}

	@RequestMapping(value = "/testpage", method = RequestMethod.GET)
	public String testpage(Model model) {
		model.addAttribute("msg", "This is a TEST!");
		model.addAttribute("msg2", "This is the second message!");
		model.addAttribute("addresses", locationService.getAllAddresses());
		

		return "testpage";
	}
	
	@RequestMapping(value = "/testpost", method = RequestMethod.POST)
	public String test(@ModelAttribute("testpost") UserLogin user, ModelMap model) throws ParseException {

		model.addAttribute("msg2", "Post Success! You entered email: " +user.getEmailAddress()+ " and first name is " + user.getFname());
		model.addAttribute("addresses", locationService.getAllAddresses());

		return "testpage";
	}	

	@RequestMapping(value = "/lookcity", method = RequestMethod.POST)
	public String lookcity(@ModelAttribute("lookcity") String lookcity, ModelMap model) throws ParseException {

		model.addAttribute("message", lookcity + " Lookup");
		model.addAttribute("users", lookupCity(lookcity));

		return "services";
	}	
	
	@SuppressWarnings("rawtypes")
	public List<UserLogin> lookupCity(String cityOrZip) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); 
		
       	final String sql  = "SELECT m.* FROM hitched.users m WHERE m.city like '%"+ cityOrZip +"%'"
       			+ " UNION "
       			+ "SELECT m.* FROM hitched.users m WHERE m.zip like '%"+ cityOrZip +"%'";
       	@SuppressWarnings("unchecked")
   		List<UserLogin> memberList = jdbcTemplate.query(sql, new BeanPropertyRowMapper( UserLogin.class ));						
   		return memberList;
   		 
	}

	
	
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	public String addresses(Model model) {
		model.addAttribute("addresses", new Addresses());

		return "addresses";
	}
	
	@RequestMapping(value = "/addresses", method = RequestMethod.POST)
	public String addresses(@Valid @ModelAttribute("addresses") Addresses addresses, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "addresses";
		}
		try {
			 locationService.createAddress(addresses);
			//model.addAttribute("addresses", locationService.getAllAddresses());
			model.addAttribute("success", "Address added for " + addresses.getBname() + " Location");

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return "addresses";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Address Not Added for " + addresses.getBname() + " Location");

			e.printStackTrace();
			return "addresses";
		}

		return "about";
	}	


}
