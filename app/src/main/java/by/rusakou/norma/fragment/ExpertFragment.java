package by.rusakou.norma.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.rusakou.norma.MathOp;
import by.rusakou.norma.R;


public class ExpertFragment extends Fragment {

    public static final String TAG = "expertFragmentTAG";
    private Bundle bundleToExpert;
    private EditText editTextBetweenProduct, editTextForEdgeForm, editTextForChain,
            editTextShrinkagePS, editTextShrinkagePET, editTextShrinkagePVC, editTextShrinkagePP;
    private TextView textViewBetweenProduct, textViewForEdgeForm, textViewForChain,
            textViewShrinkagePS, textViewShrinkagePET, textViewShrinkagePVC, textViewShrinkagePP;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert, container, false);
        editTextBetweenProduct = view.findViewById(R.id.editTextBetweenProduct);
        editTextForEdgeForm = view.findViewById(R.id.editTextForEdgeForm);
        editTextForChain = view.findViewById(R.id.editTextForChain);
        editTextShrinkagePS = view.findViewById(R.id.editTextShrinkagePS);
        editTextShrinkagePET = view.findViewById(R.id.editTextShrinkagePET);
        editTextShrinkagePVC = view.findViewById(R.id.editTextShrinkagePVC);
        editTextShrinkagePP = view.findViewById(R.id.editTextShrinkagePP);
        textViewBetweenProduct = view.findViewById(R.id.textViewBetweenProduct);
        textViewForEdgeForm = view.findViewById(R.id.textViewForEdgeForm);
        textViewForChain = view.findViewById(R.id.textViewForChain);
        textViewShrinkagePS = view.findViewById(R.id.textViewShrinkagePS);
        textViewShrinkagePET = view.findViewById(R.id.textViewShrinkagePET);
        textViewShrinkagePVC = view.findViewById(R.id.textViewShrinkagePVC);
        textViewShrinkagePP = view.findViewById(R.id.textViewShrinkagePP);

        bundleToExpert = getArguments(); //получаем знaчения
//        Log.d(TAG, bundleToExpert.toString());

        postEditText(editTextBetweenProduct, bundleToExpert, 0); //Обновляем editText между лотками
        postEditText(editTextForEdgeForm, bundleToExpert, 1); //Обновляем editText на край формы
        postEditText(editTextForChain, bundleToExpert, 2); //Обновляем editText на цепи
        postEditText(editTextShrinkagePS, bundleToExpert, 3); //Обновляем editText усадку в процентах PS
        postEditText(editTextShrinkagePET, bundleToExpert, 4); //Обновляем editText усадку в процентах PЕТ
        postEditText(editTextShrinkagePVC, bundleToExpert, 5); //Обновляем editText усадку в процентах PVC
        postEditText(editTextShrinkagePP, bundleToExpert, 6); //Обновляем editText усадку в процентах PP

        double maxBetweenProduct = getValue(bundleToExpert, 7); //максимум между лотками
        double maxForEdgeForm = getValue(bundleToExpert, 8); //максимум на край формы
        double maxForChain = getValue(bundleToExpert, 9); //максимум на цепи
        double maxShrinkagePercentPS = getValue(bundleToExpert, 10); //максимум усадка в процентах PS
        double maxShrinkagePercentPET = getValue(bundleToExpert, 11); //максимум усадка в процентах PET
        double maxShrinkagePercentPVC = getValue(bundleToExpert, 12); //максимум усадка в процентах PVC
        double maxShrinkagePercentPP = getValue(bundleToExpert, 13); //максимум усадка в процентах PP
//        return inflater.inflate(R.layout.fragment_expert, container, false);
        textViewBetweenProduct.setText(String.format(getResources().getString(R.string.text_view_expert_between_product), maxBetweenProduct));
        textViewForEdgeForm.setText(String.format(getResources().getString(R.string.text_view_expert_for_edge_form), maxForEdgeForm));
        textViewForChain.setText(String.format(getResources().getString(R.string.text_view_expert_for_chain), maxForChain));
        textViewShrinkagePS.setText(String.format(getResources().getString(R.string.text_view_expert_shrinkage_ps), maxShrinkagePercentPS));
        textViewShrinkagePET.setText(String.format(getResources().getString(R.string.text_view_expert_shrinkage_pet), maxShrinkagePercentPET));
        textViewShrinkagePVC.setText(String.format(getResources().getString(R.string.text_view_expert_shrinkage_pvc), maxShrinkagePercentPVC));
        textViewShrinkagePP.setText(String.format(getResources().getString(R.string.text_view_expert_shrinkage_pp), maxShrinkagePercentPP));


        Button buttonSet = view.findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleFromExpert = new Bundle();
                // проверка введённых данных
                if (getEditTextBetweenProduct() <= maxBetweenProduct & getEditTextForEdgeForm() <= maxForEdgeForm
                        & getEditTextForChain() <= maxForChain & getEditTextShrinkagePS() <= maxShrinkagePercentPS
                        & getEditTextShrinkagePET() <= maxShrinkagePercentPET & getEditTextShrinkagePVC() <= maxShrinkagePercentPVC
                        & getEditTextShrinkagePP() <= maxShrinkagePercentPP) {
                    Toast.makeText(getActivity(), R.string.toast_expert_values_changed, Toast.LENGTH_LONG).show();
                    //передаём значения в активити
                    double[] valueFromExpert = {getEditTextBetweenProduct(), getEditTextForEdgeForm(), getEditTextForChain(),
                            MathOp.runShrinkageCoefficient(getEditTextShrinkagePS()), MathOp.runShrinkageCoefficient(getEditTextShrinkagePET()),
                            MathOp.runShrinkageCoefficient(getEditTextShrinkagePVC()), MathOp.runShrinkageCoefficient(getEditTextShrinkagePP())};
                    bundleFromExpert.putDoubleArray("bundleKeyFromExpert", valueFromExpert);
                    getParentFragmentManager().setFragmentResult("requestKey", bundleFromExpert);
                } else Toast.makeText(getActivity(), R.string.toast_size_not_data, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    /**
     * Метод позволяет обновлять EditText с значениями по умолчанию.
     *
     * @param editText - текстовое поле
     * @param bundle - передаваемый bundle из активити
     * @param number - номер необходимого значения в массиве double
     */
    public void postEditText(EditText editText, Bundle bundle, int number){
        editText.post(new Runnable() {
            public void run() {
                    double[] result = bundle.getDoubleArray("bundleKeyToExpert");
                    if (result != null) {
                        editText.setText(String.valueOf(result[number]));
                        //editText.setHint(String.valueOf(result[number]));
                    }
            }
        });
    }

    /**
     * Метод позволяет получить значение по умолчанию
     *
     * @param bundle - передаваемый bundle из активити
     * @param number - номер необходимого значения в массиве double
     * @return - значение из массива активити
     */
    public double getValue(Bundle bundle, int number){
        double value = 0;
        double[] result = bundle.getDoubleArray("bundleKeyToExpert");
        if (result != null) {
            value = result[number];
        }
        return value;
    }

    public double getEditTextBetweenProduct() {
        return (editTextBetweenProduct.getText().toString().isEmpty()) ?
                0 : Double.parseDouble(editTextBetweenProduct.getText().toString().replace(",", "."));
    }

    public double getEditTextForEdgeForm() {
        return (editTextForEdgeForm.getText().toString().isEmpty()) ?
                0 : Double.parseDouble(editTextForEdgeForm.getText().toString().replace(",", "."));
    }

    public double getEditTextForChain() {
        return (editTextForChain.getText().toString().isEmpty()) ?
                0 : Double.parseDouble(editTextForChain.getText().toString().replace(",", "."));
    }

    public double getEditTextShrinkagePS() {
        return (editTextShrinkagePS.getText().toString().isEmpty()) ?
                getValue(bundleToExpert, 3) : Double.parseDouble(editTextShrinkagePS.getText().toString().replace(",", "."));
    }

    public double getEditTextShrinkagePET() {
        return (editTextShrinkagePET.getText().toString().isEmpty()) ?
                getValue(bundleToExpert, 4) : Double.parseDouble(editTextShrinkagePET.getText().toString().replace(",", "."));
    }

    public double getEditTextShrinkagePVC() {
        return (editTextShrinkagePVC.getText().toString().isEmpty()) ?
                getValue(bundleToExpert, 5) : Double.parseDouble(editTextShrinkagePVC.getText().toString().replace(",", "."));
    }

    public double getEditTextShrinkagePP() {
        return (editTextShrinkagePP.getText().toString().isEmpty()) ?
                getValue(bundleToExpert, 6) : Double.parseDouble(editTextShrinkagePP.getText().toString().replace(",", "."));
    }
}