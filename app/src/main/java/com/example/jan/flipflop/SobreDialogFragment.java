package com.example.jan.flipflop;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by Jan on 28/11/2015.
 */
public class SobreDialogFragment extends DialogFragment {
    public static final String DIALOG_TAG = "dialogSobre";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_frag_sobre, null);

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }).create();
        return alert;
    }
    public void abrir(FragmentManager fm){
        Fragment fragment = fm.findFragmentByTag(DIALOG_TAG);
        show(fm,DIALOG_TAG);

    }
}
