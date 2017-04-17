package ams.com.eattendance.presentation.ui.activities.wifi;

import java.util.ArrayList;

import ams.com.eattendance.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WifiConnectionAdapter extends RecyclerView.Adapter<WifiConnectionAdapter.WifiViewHolder> {

	private Context context;
	private ArrayList<WifiConnectionInfo> wifiConnectionInfoList;

	public WifiConnectionAdapter(Context context, ArrayList<WifiConnectionInfo> wifiConnectionInfoList) {
		this.context = context;
		this.wifiConnectionInfoList = wifiConnectionInfoList;
	}

	@Override
	public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_wifi_connection, parent, false);
		WifiViewHolder wifiViewHolder = new WifiViewHolder(v); // pass the view to View Holder
		return wifiViewHolder;
	}

	@Override
	public void onBindViewHolder(WifiViewHolder holder, int position) {
		holder.textViewSsid.setText(wifiConnectionInfoList.get(position).getSsid());
		holder.textViewMacId.setText(wifiConnectionInfoList.get(position).getMacid());

		Boolean connected = wifiConnectionInfoList.get(position).getConnected();

		if (connected) {
			holder.textConnectedStatus.setText(context.getString(R.string.activity_wifi_connection_connected_status));
		} else {
			holder.textConnectedStatus.setText("");
		}

	}

	@Override
	public int getItemCount() {
		return wifiConnectionInfoList.size();
	}

	public class WifiViewHolder extends RecyclerView.ViewHolder {

		TextView textViewSsid;
		TextView textViewMacId;
		TextView textConnectedStatus;

		public WifiViewHolder(View itemView) {
			super(itemView);
			textViewSsid = (TextView) itemView.findViewById(R.id.text_ssid);
			textViewMacId = (TextView) itemView.findViewById(R.id.text_macid);
			textConnectedStatus = (TextView) itemView.findViewById(R.id.text_connected_status);

		}
	}

}




