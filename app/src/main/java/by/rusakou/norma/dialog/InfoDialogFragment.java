package by.rusakou.norma.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

import by.rusakou.norma.R;

/**
 * Для создания своих диалоговых окон используется компонент AlertDialog в связке с классом фрагмента DialogFragment.
 */
public class InfoDialogFragment extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        return builder
                .setTitle(R.string.info_dialog_fragment_title)
                .setIcon(R.drawable.img_lotok)
                .setMessage(R.string.info_dialog_fragment_text)
//                .setView(R.layout.fragment_custom_dialog)
                .setNegativeButton(R.string.info_dialog_fragment_send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendEmail(new String[]{"ya.rusakou@gmail.com"}, getString(R.string.title_email_app_norma));
                    }})
                .setPositiveButton(R.string.info_dialog_fragment_close, null)
                .create();
    }

    /**
     * Метод помогает открыть почту ,чтобы написать письмо.
     *
     * @param email - почта
     * @param subject - заголовок
     */
    public void sendEmail(String[] email, String subject) {
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // только почта
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, email); // Кому
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); //Заголовок
        Intent chosenIntent = Intent.createChooser(intent, null); //без выбора "по умолчанию"
        InfoDialogFragment.this.startActivity(chosenIntent);
    }
}