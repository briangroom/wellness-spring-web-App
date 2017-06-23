package com.hitched.ServiceImpl;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitched.Dao.HitchedDao;
import com.hitched.model.UserLogin;
import com.hitched.service.HitchedService;


@Service
@Transactional
public class HitchedServiceImpl implements HitchedService {

	@Autowired
	HitchedDao hitchedDao;
	
	@Override
	public boolean findUser(String email) {
		return hitchedDao.findUser(email);
	}

	@Override
	public long createUser(UserLogin userLogin) {
		return hitchedDao.createUser(userLogin);
	}

	@Override
	public UserLogin updateUser(UserLogin userLogin) {
		return hitchedDao.updateUser(userLogin);
	}

	@Override
	public void deleteUserById(long id) {
		hitchedDao.deleteUserById(id);
		
	}

	@Override
	public List<UserLogin> getAllusers() {
		return hitchedDao.getAllusers();
	}

	@Override
	public UserLogin getUserByEmailId(String email) {
		return hitchedDao.getUserByEmailId(email);
	}

	@Override
	public UserLogin getUserById(long id) {
		return hitchedDao.getUserById(id);
	}

	@Override
	public void updaterole(UserLogin userLogin, String role, long id) {
		hitchedDao.updaterole(userLogin, role, id);
		
	}

	
	@Override
	public void passReset(UserLogin userLogin, String pass, String email) {
		hitchedDao.passReset(userLogin, pass, email);
		
	}

	@Override
	public void updateSubscription(UserLogin userLogin, String membership, long id) {
		hitchedDao.updateSubscription(userLogin, membership, id);
	}

	@Override
	public List<UserLogin> lookupMembers(String name) {		
		return hitchedDao.lookupMembers(name);
	}

	@Override
	public void updateUserById(UserLogin userLogin, long id) {
		hitchedDao.updateUserById(userLogin, id);
		
	}

	@Override
	public List<UserLogin> getUserByName(String user) {
		return hitchedDao.getUserByName(user);
	}

	@Override
	public List<UserLogin> lookupCity(String cityOrZip) {		
		return hitchedDao.lookupCity(cityOrZip);
	}

	@Override
	public JSONObject jdbcDbConnect(String email) {
		return hitchedDao.jdbcDbConnect(email);
	}

}
