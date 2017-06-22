package com.wellness.Dao;

import java.util.List;

import com.wellness.model.Addresses;
import com.wellness.model.Locations;
import com.wellness.model.UserLogin;

public interface LocationDao {

	public long createAddress(Addresses addresses);
	public List<Addresses> getAllAddresses();
	
	public Addresses updateAddress(Addresses addresses);
	//public void deleteAddressById(long id);
	

}
