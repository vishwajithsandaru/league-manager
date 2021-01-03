package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable{

	private ArrayList<String> lanes;

	private String district;

	private String state;

	private String postalCode;

	private double latitude;

	private double longitude;

	public Location(ArrayList<String> lanes, String district, String state) {
		this.lanes = lanes;
		this.district = district;
		this.state = state;
	}

	public ArrayList<String> getLanes() {
		return lanes;
	}

	public void setLanes(ArrayList<String> lanes) {
		this.lanes = lanes;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (lanes == null) {
			if (other.lanes != null)
				return false;
		} else if (!lanes.equals(other.lanes))
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Location [district=" + district + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", postalCode=" + postalCode + ", state=" + state + "]";
	}	

}