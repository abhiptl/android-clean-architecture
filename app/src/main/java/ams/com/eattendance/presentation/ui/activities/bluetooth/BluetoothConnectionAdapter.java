package ams.com.eattendance.presentation.ui.activities.bluetooth;

import java.util.ArrayList;

import ams.com.eattendance.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BluetoothConnectionAdapter extends RecyclerView.Adapter<BluetoothConnectionAdapter.BluetoothViewHolder> {

	private Context context;
	private ArrayList<BluetoothDevice> bluetoothConnectionInfoList = new ArrayList<>();

	public BluetoothConnectionAdapter(Context context, ArrayList<BluetoothDevice> bluetoothConnectionInfoList) {
		this.context = context;
		this.bluetoothConnectionInfoList = bluetoothConnectionInfoList;
	}

	@Override
	public BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bluetooth_connection, parent, false);
		BluetoothViewHolder bluetoothViewHolder = new BluetoothViewHolder(v); // pass the view to View Holder
		return bluetoothViewHolder;
	}

	@Override
	public void onBindViewHolder(BluetoothViewHolder holder, final int position) {
		holder.textViewName.setText(bluetoothConnectionInfoList.get(position).getName());
		holder.textViewAddress.setText(bluetoothConnectionInfoList.get(position).getAddress());
	}

	@Override
	public int getItemCount() {
		return bluetoothConnectionInfoList.size();
	}

	public class BluetoothViewHolder extends RecyclerView.ViewHolder {

		TextView textViewName;
		TextView textViewAddress;

		public BluetoothViewHolder(View itemView) {
			super(itemView);
			textViewName = (TextView) itemView.findViewById(R.id.text_bluetooth_name);
			textViewAddress = (TextView) itemView.findViewById(R.id.text_bluetooth_address);

		}
	}

}
