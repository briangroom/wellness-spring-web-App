package com.wellness.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wellness.model.Addresses;
import com.wellness.model.Locations;
import com.wellness.service.LocationService;
import com.wellness.wellnessUtils.States;

@Controller
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	@ModelAttribute("states")
	public List<States> populateStates() {
		return Arrays.asList(States.values());
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
			//model.addAttribute("users", locationService.getAllAddresses());
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
