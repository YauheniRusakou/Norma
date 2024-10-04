package by.rusakou.norma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import by.rusakou.norma.dialog.*;
import by.rusakou.norma.fragment.*;
import by.rusakou.norma.file.*;
import by.rusakou.norma.parser.*;

/**
 @author Русаков Евгений Михайлович.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityTAG";
    private EditText editTextLengthProduct, editTextWidthProduct, editTextRadiusProduct, editTextThickness;
    private RadioGroup radioGroupMaterials;
    private RadioButton radioButtonMaterial;
    private ChipGroup chipGroupModelMachines, chipGroupMachines, chipGroupLocation;
    private Chip chipModelMachine, chipMachine, chipLocationOne, chipLocationTwo, chipLocationSquare, chipLocationCircle;
    private MaterialButtonToggleGroup toggleButtonBFSPunching;
    private Button buttonBFS, buttonPunching;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragmentRectangle, fragmentSquare, fragmentCircle;
    private List<Material> listMaterials = Collections.synchronizedList(new ArrayList<>());
    private List<Machine> listMachines = Collections.synchronizedList(new ArrayList<>());
    private List<Coefficient> listCoefficients = Collections.synchronizedList(new ArrayList<>());
    private List<Property> listProperties = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadLocal.onCreateLocale(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //отключаем темную тему
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);

        editTextThickness = findViewById(R.id.editTextThickness);
        radioGroupMaterials = findViewById(R.id.radioGroupMaterials);
        chipGroupLocation = findViewById(R.id.chipGroupLocation);
        chipLocationOne = findViewById(R.id.chipLocationOne);
        chipLocationTwo = findViewById(R.id.chipLocationTwo);
        chipLocationSquare = findViewById(R.id.chipLocationSquare);
        chipLocationCircle = findViewById(R.id.chipLocationCircle);
        toggleButtonBFSPunching = findViewById(R.id.toggleButtonBFSPunching);
        buttonBFS = findViewById(R.id.buttonBFS);
        buttonPunching = findViewById(R.id.buttonPunching);

        //Добавляем фрагмент с размерами прямоугольного лотка для стартового экрана
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().setReorderingAllowed(true);  // начинаем транзакцию
        fragmentRectangle = new SizeProdRectangleFragment(); // Создаем и добавляем фрагмент
        fragmentTransaction.add(R.id.layoutSizeProduct, fragmentRectangle, SizeProdRectangleFragment.TAG); //Добавляем фрагмент с его тэгом, потом по этому тегу будем его отслеживать
        fragmentTransaction.commit(); // Подтверждаем операцию

        Thread threadURL = new Thread(new ReaderThread());
        threadURL.start();

        try {
            threadURL.join();
            for (int i = 0; i < listMaterials.size(); i++) { //Создаём радиокнопки выбора материала
                radioButtonMaterial = new RadioButton(this);
                radioButtonMaterial.setText(listMaterials.get(i).getName());
                radioButtonMaterial.setId(i);
                if (radioButtonMaterial.getText().equals(listProperties.get(0).getStartMaterial())){//equals("PP")) //getId() == 3)
                    radioButtonMaterial.setChecked(true);
                }
                radioGroupMaterials.addView(radioButtonMaterial);
            }

            Set<String> hashSetModelMachines = new LinkedHashSet<>(); //Создаем список моделей машин. LinkedHashSet позволяет создать список без повторов
            for (int j = 0; j < listMachines.size(); j++) {
                hashSetModelMachines.add(listMachines.get(j).getModelMachine());
            }
            List<String> listModelMachines = new ArrayList<>(hashSetModelMachines); //Преобразуем в ArrayList, чтобы получать по индексу

            chipGroupModelMachines = findViewById(R.id.chipGroupModelMachines); //chipGroup для вида моделей кнопок
            for (int x = 0; x < listModelMachines.size(); x++) { //Создаём кнопки с моделями машин
                View view = LayoutInflater.from(this).inflate(R.layout.item_choice_chip, chipGroupModelMachines, false);
                chipModelMachine = view.findViewById(R.id.chips_item_choice);
                chipModelMachine.setText(listModelMachines.get(x));
                chipModelMachine.setClickable(true);
                chipModelMachine.setCheckable(true);
                chipModelMachine.setId(x);
                //chipModelMachine.setOnClickListener(myListener);
                if (chipModelMachine.getText().equals(listProperties.get(0).getStartMachine())){ //equals("LX Plastic")) //equals(listModelMachines.get(4))
                    chipModelMachine.setChecked(true);
                }
                chipGroupModelMachines.addView(chipModelMachine);
            }

            chipGroupMachines = findViewById(R.id.chipGroupMachines); //chipGroup для кнопок машин
            createChipMachine(listModelMachines); //создаём первоначальные chip кнопки с машинами

            //обновляем chip кнопки с машинами, если юзер выбрал другой вид машин
            chipGroupModelMachines.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                    chipGroupMachines.removeAllViews();
                    createChipMachine(listModelMachines);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Переключаем фрагменты размеров лотков: Прямоугольник, квадрат или круг
        chipGroupLocation.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                fragmentTransaction = fragmentManager.beginTransaction().setReorderingAllowed(true);

                if (chipLocationOne.isPressed() | chipLocationTwo.isPressed()) {
                    //fragment = new SizeProdRectangleFragment(); // Создаем и добавляем фрагмент
                    fragmentTransaction.replace(R.id.layoutSizeProduct, fragmentRectangle, SizeProdRectangleFragment.TAG).commit();
                }
                if (chipLocationSquare.isPressed()) {
                    fragmentSquare = new SizeProdSquareFragment();
                    fragmentTransaction.replace(R.id.layoutSizeProduct, fragmentSquare, SizeProdSquareFragment.TAG).commit();
                }
                if (chipLocationCircle.isPressed()) {
                    fragmentCircle = new SizeProdCircleFragment();
                    fragmentTransaction.replace(R.id.layoutSizeProduct, fragmentCircle, SizeProdCircleFragment.TAG).commit();
                }
            }
        });

        //  Включаем кнопку совмещенная или раздельная вырубка
        chipGroupMachines.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                int checkedIdMachine = group.getCheckedChipId();
                Machine machine = listMachines.get(checkedIdMachine);
                postBFSPunchingButton(buttonBFS, machine.isMachineBFS(), buttonPunching, machine.isMachinePunching());
            }
        });

        //свайп для обновления данных из интернета
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_update);
        // указываем слушатель свайпов пользователя
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateActivity();
                swipeRefreshLayout.setRefreshing(false);// убираем стандартную анимацию
            }
        });

	// Инициализируем Google Mobile Ads SDK в фоновом потоке.
        new Thread(
                () -> {
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();
    }

    /**
     * Внутренний класс для чтения информации из интернета.
     * Создаёт синхронизированные коллекции объектов материалов, машин, коэффициентов и свойств.
     */
    class ReaderThread implements Runnable {
        public void run() {
            try {
                {
                    String contentMaterials = DowloadURL.download("http://rusakou.by/norma/xml/materials.xml");
                    synchronized (listMaterials) {
                        MaterialParser parser = new MaterialParser();
                        parser.parse(contentMaterials);
                        listMaterials = parser.getMaterials();
                    } //Log.d(TAG, "ReaderThread listMaterials finished");
                }
                {
                    String contentMachines = DowloadURL.download("http://rusakou.by/norma/xml/machines.xml");
                    synchronized (listMachines) {
                        MachineParser parser = new MachineParser();
                        parser.parse(contentMachines);
                        listMachines = parser.getMachines();
                    }
                }
                {
                    String contentCoefficients = DowloadURL.download("http://rusakou.by/norma/xml/coefficients.xml");
                    synchronized (listCoefficients) {
                        CoefficientParser parser = new CoefficientParser();
                        parser.parse(contentCoefficients);
                        listCoefficients = parser.getCoefficients();
                    }
                }
                {
                    String contentProperties = DowloadURL.download("http://rusakou.by/norma/xml/properties.xml");
                    synchronized (listProperties) {
                        PropertyParser parser = new PropertyParser();
                        parser.parse(contentProperties);
                        listProperties = parser.getProperties();
                    } //Log.d(TAG, " " + listProperties);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод создаёт кнопки Chip с названием машин и присваевает им Id.
     * Для каждой модели оборудования обображаем только те машины, которые к ним относятся.
     *
     * @param listModelMachines - список моделей машин
     */
    public void createChipMachine(List<String> listModelMachines) {
        int checkedIdNameMachine = chipGroupModelMachines.getCheckedChipId();
        for (int i = 0; i < listMachines.size(); i++) {
            String modelMachine = listModelMachines.get(checkedIdNameMachine); //название вида модели машины в кнопке выбора
            String modelMachineTag = listMachines.get(i).getModelMachine(); //тэг названия модели машины в самой машине
            if (modelMachine.equals(modelMachineTag)) { //если тэг модели машины есть, то создаём вьюху кнопок
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_choice_chip, chipGroupMachines, false);
                chipMachine = view.findViewById(R.id.chips_item_choice);
                chipMachine.setText(listMachines.get(i).getName());
                chipMachine.setClickable(true);
                chipMachine.setCheckable(true);
                chipMachine.setId(i);
                //chip.setOnClickListener(myListener);
                if ((listModelMachines.get(checkedIdNameMachine).equals(listMachines.get(i).getModelMachine()))) { //последняя кнопка выделена
                    chipMachine.setChecked(true);
                    Machine machine = listMachines.get(i);   // Включаем кнопку совмещенная или раздельная вырубка
                    postBFSPunchingButton(buttonBFS, machine.isMachineBFS(), buttonPunching, machine.isMachinePunching());
                }
                chipGroupMachines.addView(chipMachine);
            }
        }
    }

    /**
     * Метод позволяет обновлять активити с кнопками совмещенная или раздельная вырубка.
     *
     * @param viewBFS      - кнопка с названием совмещённая вырубка
     * @param boolBFS      - машина имеет совмещенную вырубку
     * @param viewPunching - кнопка с названием раздельная вырубка
     * @param boolPunching - машина имеет раздельную вырубку
     */
    public void postBFSPunchingButton(View viewBFS, boolean boolBFS, View viewPunching, boolean boolPunching) {
        if (!boolBFS & boolPunching) { // Если машина не BFS, а только Раздельная
            viewBFS.post(new Runnable() {
                public void run() {
                    viewBFS.setVisibility(View.GONE);
                    viewPunching.setVisibility(View.VISIBLE);
                    toggleButtonBFSPunching.check(R.id.buttonPunching);
                }
            });
        }
        if (boolBFS & !boolPunching) { // Если машина только BFS
            viewPunching.post(new Runnable() {
                public void run() {
                    viewBFS.setVisibility(View.VISIBLE);
                    viewPunching.setVisibility(View.GONE);
                    toggleButtonBFSPunching.check(R.id.buttonBFS);
                }
            });
        }
        if (boolBFS & boolPunching) { // Если машина и BFS, и Раздельная
            viewPunching.post(new Runnable() {
                public void run() {
                    viewBFS.setVisibility(View.VISIBLE);
                    viewPunching.setVisibility(View.VISIBLE);
                    toggleButtonBFSPunching.check(R.id.buttonPunching);
                }
            });
        }
    }

    /**
     * Запуск расчета формы.
     *
     * @param view - вызывается при нажатии на кнопку
     */
    public void onClickCalc(View view) {
        if (isHostAvailable(listMaterials, listMachines, listCoefficients, listProperties)) { //проверяем загружены ли данные
            // Устанавливаем значения размеров лотка в зависимости от выбранного фрагмента: Прямоугольник, квадрат или круг
            if (fragmentManager.findFragmentByTag(SizeProdRectangleFragment.TAG) != null) {
                editTextLengthProduct = findViewById(R.id.editTextLengthProduct);
                editTextWidthProduct = findViewById(R.id.editTextWidthProduct);
                editTextRadiusProduct = findViewById(R.id.editTextRadiusProduct);
            }
            if (fragmentManager.findFragmentByTag(SizeProdSquareFragment.TAG) != null) {
                editTextLengthProduct = findViewById(R.id.editTextLengthProduct);
                editTextWidthProduct = editTextLengthProduct;
                editTextRadiusProduct = findViewById(R.id.editTextRadiusProduct);
            }
            if (fragmentManager.findFragmentByTag(SizeProdCircleFragment.TAG) != null) {
                editTextLengthProduct = findViewById(R.id.editTextLengthProduct);
                editTextWidthProduct = editTextLengthProduct;
                editTextRadiusProduct = editTextLengthProduct;
            }

            int checkedIdMaterial = radioGroupMaterials.getCheckedRadioButtonId();
            Material material = listMaterials.get(checkedIdMaterial);
            int checkedIdMachine = chipGroupMachines.getCheckedChipId();
            Machine machine = listMachines.get(checkedIdMachine);
            Log.d(TAG, "Machines " + machine + "\n");
            Coefficient coefficient = (machine.isTypeMachineK()) ? listCoefficients.get(1) : listCoefficients.get(0); //Тернарный оператор
            int checkedIdLocation = chipGroupLocation.getCheckedChipId();
            int checkedIdBFSPunching = toggleButtonBFSPunching.getCheckedButtonId();
            boolean bfs = (checkedIdBFSPunching == buttonBFS.getId()); //если вырубка совмещенная, то true, если раздельная то false
            double forEdgeFormExam = (bfs) ? machine.getForEdgeFormBFS() : machine.getForEdgeFormPunching(); //Тернарный оператор
            Intent intent = new Intent(this, PaintActivity.class);

            Thread thread = new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //Проверяем чтобы машина работала с РР
                            if (material.getName().equals("PP") & !machine.isUsedPP()) {
                                Toast.makeText(MainActivity.this, R.string.toast_machine_not_pp, Toast.LENGTH_LONG).show();
                                //Проверяем введенные значения, если число больше 2х - знаков запускаем расчёт
                            } else if (editTextLengthProduct.getText().length() > 1 & editTextWidthProduct.getText().length() > 1 & editTextThickness.getText().length() > 1 & editTextRadiusProduct.getText().length() >= 0) {
                                double sizeRadius = (editTextRadiusProduct.getText().length() == 0) ? 0 : Double.parseDouble(editTextRadiusProduct.getText().toString().replace(",", ".")); //Тернарный оператор
                                double thickness = Double.parseDouble(editTextThickness.getText().toString().replace(",", "."));
                                double sizeOne = 0, sizeTwo = 0;
                                if (checkedIdLocation == chipLocationOne.getId()) {
                                    sizeOne = Double.parseDouble(editTextLengthProduct.getText().toString().replace(",", "."));
                                    sizeTwo = Double.parseDouble(editTextWidthProduct.getText().toString().replace(",", "."));
                                }
                                if (checkedIdLocation == chipLocationTwo.getId()) {
                                    sizeTwo = Double.parseDouble(editTextLengthProduct.getText().toString().replace(",", "."));
                                    sizeOne = Double.parseDouble(editTextWidthProduct.getText().toString().replace(",", "."));
                                }
                                if (checkedIdLocation == chipLocationSquare.getId()) {
                                    sizeOne = sizeTwo = Double.parseDouble(editTextLengthProduct.getText().toString().replace(",", "."));
                                }
                                if (checkedIdLocation == chipLocationCircle.getId()) {
                                    sizeOne = sizeTwo = Double.parseDouble(editTextLengthProduct.getText().toString().replace(",", "."));
                                    sizeRadius = (sizeOne - 0.01) / 2;
                                }
                                if (sizeOne > 0 & sizeTwo > 0) { //проверка, чтобы размеры лотка не равнялись нулю
                                    if (((MathOp.runSizeKnife(sizeOne, material.getShrinkage()) + 2 * forEdgeFormExam) <= machine.getMaxFormWidth())
                                            & ((MathOp.runSizeKnife(sizeTwo, material.getShrinkage()) + 2 * forEdgeFormExam) <= machine.getMaxFormLength())) { //Проверка размеров лотка, чтобы не превышали размеры формы
                                        if ((sizeRadius < (0.5 * sizeOne)) & (sizeRadius < (0.5 * sizeTwo))) { //Проверка радиусов лотка, чтобы не превышали полдлины размера лотка

                                            String nameProduct = getNameProduct(sizeOne, sizeTwo);
                                            Calculation calc = new Calculation(sizeOne, sizeTwo, sizeRadius, thickness, nameProduct, material, machine, bfs, coefficient);
                                            intent.putExtra(Calculation.class.getSimpleName(), calc);
                                            startActivity(intent);

                                        } else
                                            Toast.makeText(MainActivity.this, R.string.toast_big_radius, Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(MainActivity.this, R.string.toast_big_product, Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(MainActivity.this, R.string.toast_size_not_zero, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.toast_size_not_data, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            };
            thread.start();
        } else  {
            NoInternetDialogFragment dialog = new NoInternetDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");
        }
    }

    /**
     * Вспомогательный метод, позволяющий выводить наименование лотка в виде String.
     * Метод определяет задан прямоугольный лоток или круглый, для коректного вывода результата.
     *
     * @return название лотка
     */
    public String getNameProduct(double sizeOne, double sizeTwo) {

        NumberFormat nf = new DecimalFormat("#.##");
        String message = null;

        if (fragmentManager.findFragmentByTag(SizeProdRectangleFragment.TAG) != null
                | fragmentManager.findFragmentByTag(SizeProdSquareFragment.TAG) != null) {
            double resProdLength; // Длина лотка
            double resProdWidth; // Ширина лотка
            if ((sizeOne > sizeTwo) | (sizeOne == sizeTwo)) { // выводим сторону более длинную первую в названии
                resProdLength = sizeOne;
                resProdWidth = sizeTwo;
            } else {
                resProdLength = sizeTwo;
                resProdWidth = sizeOne;
            }
            message = String.format(getResources().getString(R.string.result_name_product), nf.format(resProdLength), nf.format(resProdWidth));
        }
        if (fragmentManager.findFragmentByTag(SizeProdCircleFragment.TAG) != null) {
            message = String.format(getResources().getString(R.string.result_name_product_circle), nf.format(sizeOne));
        }
        return message;
    }

    //Добавляем Меню на панель  приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Для реагирования нашей активити на щелчки в панели приложения
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.english_language) {
            LoadLocal.saveFile(new Locale("en"/*, "US"*/), this);
            restartApp();
            return true;
        }
        if (item.getItemId() == R.id.belarusian_language) {
            LoadLocal.saveFile(new Locale("be", "BY"), this);
            restartApp();
            return true;
        }
        if (item.getItemId() == R.id.russian_language) {
            LoadLocal.saveFile(new Locale("ru", "RU"), this);
            restartApp();
            return true;
        }
        if (item.getItemId() == R.id.action_info) {
            InfoDialogFragment dialog = new InfoDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Вспомогательный метод, который перезагружает приложение.
     */
    public void restartApp() {
        Intent intent = new Intent(this, this.getClass()); //Перезапускаем активити
        finish();
        this.startActivity(intent);
    }

    /**
     * Вспомогательный метод, который перезагружает активити без анимации.
     */
    public void updateActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //Убираем анимацию
        this.startActivity(intent);
    }

    /**
     * Метод проверяет загружены ли данные из интернета. Если данные загружены, то возвращаем true, если нет - false.
     *
     * @param listMaterials    - список материалов
     * @param listMachines     - список машин
     * @param listCoefficients - список коэффициентов
     * @param listProperties - список свойств
     * @return true - данные есть, false - данные отсуствуют.
     */
    public static boolean isHostAvailable(List<Material> listMaterials, List<Machine> listMachines,
                                          List<Coefficient> listCoefficients, List<Property> listProperties) {
        if ( !listMaterials.isEmpty() & !listMachines.isEmpty() & !listCoefficients.isEmpty() & !listProperties.isEmpty()) {
            return true;
        } else
            return false;
    }

}