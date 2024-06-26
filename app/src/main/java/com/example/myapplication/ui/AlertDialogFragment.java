package com.example.myapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class AlertDialogFragment extends DialogFragment {
	private final String title;
	private final String positive;
	private String negative;
	private Runnable runnable;
	private Runnable runnableCancelled;

	public AlertDialogFragment(String title, String positive, String negative){
		this.negative = negative;
		this.positive = positive;
		this.title = title;
	}
	public AlertDialogFragment(String title, String positive){
		this.positive = positive;
		this.title = title;
	}
	public void ifSucsessful(Runnable runnable) {
		this.runnable = runnable;
	}
	public void ifDismissed(Runnable runnable) {
		this.runnableCancelled = runnable;
	}
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		this.setCancelable(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false)
				.setTitle(title);
		builder.setPositiveButton(positive, (dialog, id) -> {
			if (!Objects.isNull(runnable)) this.runnable.run();
			dialog.dismiss();
		});
		builder.setNegativeButton(negative, (dialog, id) -> {
			 if (!Objects.isNull(runnableCancelled)) this.runnableCancelled.run();
			dialog.dismiss();
		});

		return builder.create();
	}



}
