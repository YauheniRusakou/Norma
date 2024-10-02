package by.rusakou.norma.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

import by.rusakou.norma.R;

/**
 * Для создания своих диалоговых окон используется компонент AlertDialog в связке с классом фрагмента DialogFragment.
 */
public class NoInternetDialogFragment extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        return builder
                .setTitle(R.string.no_internet_dialog_fragment_title)
                .setIcon(R.drawable.img_no_internet)
                .setView(R.layout.no_internet_dialog_fragment) //для центрирования текста используем кастомный layout
                .setPositiveButton(R.string.info_dialog_fragment_close, null)
                .create();
    }
}

