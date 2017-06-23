package com.hitched.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitched.Dao.LocationDao;
import com.hitched.model.Addresses;
import com.hitched.model.Locations;
import com.hitched.model.UserLogin;
import com.hitched.service.LocationService;
import com.hitched.Dao.HitchedDao;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
	
	@Autowired
	LocationDao locationDao;

	@Override
	public long createAddress(Addresses addresses) {
		return locationDao.createAddress(addresses);
	}

	@Override
	public Addresses updateAddress(Addresses addresses) {
		return locationDao.updateAddress(addresses);
	}

	@Override
	public List<Addresses> getAllAddresses() {
		// TODO Auto-generated method stub
		return locationDao.getAllAddresses();
	}
	
	
	

}
