package ams.com.eattendance.presentation.ui.activities.wifi;

/**
 * Created by WeConnect on 4/3/2017.
 */
public class WifiConnectionInfo {

	private String ssid;
	private String macid;
	private Boolean isConnected;

	public WifiConnectionInfo(String ssid, String macid, Boolean isConnected) {
		this.ssid = ssid;
		this.macid = macid;
		this.isConnected = isConnected;
	}

	public String getSsid() {
		return ssid;
	}

	public String getMacid() {
		return macid;
	}

	public Boolean getConnected() {
		return isConnected;
	}

	@Override
	public String toString() {
		return "WifiConnectionInfo{" + "ssid='" + ssid + '\'' + ", macid='" + macid + '\'' + ", isConnected=" + isConnected + '}';
	}
}
