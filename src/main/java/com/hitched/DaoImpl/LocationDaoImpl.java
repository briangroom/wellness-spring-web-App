package com.hitched.DaoImpl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitched.Dao.LocationDao;
import com.hitched.Dao.HitchedDao;
import com.hitched.Repository.HibernateUtil;
import com.hitched.model.Addresses;
import com.hitched.model.Locations;

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
