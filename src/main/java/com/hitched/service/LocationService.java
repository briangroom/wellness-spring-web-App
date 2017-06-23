package com.hitched.service;

import java.util.List;

import com.hitched.model.Addresses;
import com.hitched.model.Locations;

public interface LocationService {

	public long createAddress(Addresses addresses);
	public Addresses updateAddress(Addresses addresses);
	public List<Addresses> getAllAddresses();

}
