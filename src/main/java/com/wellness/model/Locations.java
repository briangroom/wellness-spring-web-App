package com.wellness.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "locations")
public class Locations implements Serializable{

	private static final long serialVersionUID = -666664499L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotEmpty(message = "Address required")
	@Pattern(regexp = "([a-zA-Z0-9]+([ ][a-zA-Z0-9])*)+", message = "Please enter Address")
	private String address;
	@NotEmpty(message = "City required")
	@Pattern(regexp = "([a-zA-Z0-9]+([ ][a-zA-Z0-9])*)+", message = "Please enter City")
	private String city;
	@NotEmpty(message = "State required")
	@Pattern(regexp = "([a-zA-Z0-9]+([ ][a-zA-Z0-9])*)+", message = "Please enter State")
	private String state;
	@NotEmpty(message = "Zip required")
	//@Pattern(regexp = "([0-9]+([ ][0-9])*)+", message = "Please enter Zip")
	private long zip;
	@NotEmpty(message = "Telephone required")
	//@Pattern(regexp = "([0-9]+([ ][0-9])*)+", message = "Please enter Telephone")
	private long tel;
	@NotEmpty(message = "Business Name required")
	@Pattern(regexp = "([a-zA-Z0-9]+([ ][a-zA-Z0-9])*)+", message = "Please enter Business Name")
	private String bname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getZip() {
		return zip;
	}

	public void setZip(long zip) {
		this.zip = zip;
	}

	public long getTel() {
		return tel;
	}

	public void setTel(long tel) {
		this.tel = tel;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}


}
