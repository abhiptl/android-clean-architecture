package ams.com.eattendance.domain.model;

public class Geofence {

	private String geoLocationName;
	private double geoLocationLatitude;
	private double geoLocationLongitude;
	private double geoLocationRadius;

	public Geofence(String geoLocationName, double geoLocationLatitude, double geoLocationLongitude, double geoLocationRadius) {
		this.geoLocationName = geoLocationName;
		this.geoLocationLatitude = geoLocationLatitude;
		this.geoLocationLongitude = geoLocationLongitude;
		this.geoLocationRadius = geoLocationRadius;
	}

	public String getGeoLocationName() {
		return geoLocationName;
	}

	public void setGeoLocationName(String geoLocationName) {
		this.geoLocationName = geoLocationName;
	}

	public double getGeoLocationLatitude() {
		return geoLocationLatitude;
	}

	public void setGeoLocationLatitude(double geoLocationLatitude) {
		this.geoLocationLatitude = geoLocationLatitude;
	}

	public double getGeoLocationLongitude() {
		return geoLocationLongitude;
	}

	public void setGeoLocationLongitude(double geoLocationLongitude) {
		this.geoLocationLongitude = geoLocationLongitude;
	}

	public double getGeoLocationRadius() {
		return geoLocationRadius;
	}

	public void setGeoLocationRadius(double geoLocationRadius) {
		this.geoLocationRadius = geoLocationRadius;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Geofence))
			return false;

		Geofence geofence = (Geofence) o;

		if (Double.compare(geofence.geoLocationLatitude, geoLocationLatitude) != 0)
			return false;
		if (Double.compare(geofence.geoLocationLongitude, geoLocationLongitude) != 0)
			return false;
		return Double.compare(geofence.geoLocationRadius, geoLocationRadius) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(geoLocationLatitude);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(geoLocationLongitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(geoLocationRadius);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Geofence{" + "geoLocationName='" + geoLocationName + '\'' + ", geoLocationLatitude=" + geoLocationLatitude
				+ ", geoLocationLongitude=" + geoLocationLongitude + ", geoLocationRadius=" + geoLocationRadius + '}';
	}
}
