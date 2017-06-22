package com.wellness.DaoImpl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wellness.Dao.LocationDao;
import com.wellness.Dao.WellnessDao;
import com.wellness.Repository.HibernateUtil;
import com.wellness.model.Addresses;
import com.wellness.model.Locations;

@Repository
public class LocationDaoImpl implements LocationDao{

		//@Autowired
		//DataSource dataSource;
		
		@Autowired
	    private HibernateUtil hibernateUtil;

		/*@Override
		public long createAddress(Locations locations) {
			return (Long) hibernateUtil.create(locations);
		}*/

		@Override
		public long createAddress(Addresses addresses) {
			// TODO Auto-generated method stub
			return (Long) hibernateUtil.create(addresses);
		}

		@Override
		public Addresses updateAddress(Addresses addresses) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Addresses> getAllAddresses() {
			// TODO Auto-generated method stub
			return hibernateUtil.fetchAll(Addresses.class);
		}

}
