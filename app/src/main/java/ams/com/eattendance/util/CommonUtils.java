package ams.com.eattendance.util;

import ams.com.eattendance.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by WeConnect on 27/01/17.
 */

public final class CommonUtils {

	private static final String TAG = "CommonUtils";

	private CommonUtils() {
		// This utility class is not publicly instantiable
	}

	public static ProgressDialog showLoadingDialog(Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.show();
		if (progressDialog.getWindow() != null) {
			progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		progressDialog.setContentView(R.layout.progress_dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		return progressDialog;
	}

	public static void showSnackBar(Context context, String message, View view) {
		Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

		View sbView = snackbar.getView();
		TextView textView = (TextView) sbView
				.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(ContextCompat.getColor(context, R.color.white));
		snackbar.show();

	}
}
