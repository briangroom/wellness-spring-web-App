package com.hitched.Dao;

import java.util.List;

import com.hitched.model.Addresses;
import com.hitched.model.Locations;
import com.hitched.model.UserLogin;

public interface LocationDao {

	public long createAddress(Addresses addresses);
	public List<Addresses> getAllAddresses();
	
	public Addresses updateAddress(Addresses addresses);
	//public void deleteAddressById(long id);
	

}
