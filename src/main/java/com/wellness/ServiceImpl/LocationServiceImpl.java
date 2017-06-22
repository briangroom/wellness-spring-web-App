package com.wellness.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wellness.Dao.LocationDao;
import com.wellness.Dao.WellnessDao;
import com.wellness.model.Addresses;
import com.wellness.model.Locations;
import com.wellness.model.UserLogin;
import com.wellness.service.LocationService;

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
