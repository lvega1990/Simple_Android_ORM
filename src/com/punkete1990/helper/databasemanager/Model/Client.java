package com.punkete1990.helper.databasemanager.Model;

public class Client {
	private int _Id;
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String country;
	private String state_province_country;
	private String zip_postcode;
	private String contact_name;
	private String phone;
	private String email_id;
	private String website;
	
	public Client(){
	}
	
	public int get_id() {
		return _Id;
	}

	public void set_id(int _id) {
		this._Id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String client_name) {
		this.name = client_name;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStateProvinceCountry() {
		return state_province_country;
	}
	public void setState_province_country(String state_province_country) {
		this.state_province_country = state_province_country;
	}
	public String getZipPostCode() {
		return zip_postcode;
	}
	public void setZip_postcode(String zip_postcode) {
		this.zip_postcode = zip_postcode;
	}

	public String getContactName() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmailId() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	@Override
	public String toString() {
		return get_id() + ", "+  getName() + ", " + getAddress1()+ ", " + getAddress2()+ ", " + getCity()+ ", " + getCountry()+ ", " + getStateProvinceCountry()
				+ ", " + getZipPostCode()+ ", " + getContactName()+ ", " + getPhone()+ ", " + getEmailId()+ ", " + getWebsite();
	}

	
}
