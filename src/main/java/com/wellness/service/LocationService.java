package com.wellness.service;

import java.util.List;

import com.wellness.model.Addresses;
import com.wellness.model.Locations;

public interface LocationService {

	public long createAddress(Addresses addresses);
	public Addresses updateAddress(Addresses addresses);
	public List<Addresses> getAllAddresses();

}
