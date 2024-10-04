package by.rusakou.norma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import by.rusakou.norma.draw.*;
import by.rusakou.norma.file.*;
import by.rusakou.norma.dialog.*;
import by.rusakou.norma.fragment.*;

public class PaintActivity extends AppCompatActivity {

    public static final String TAG = "PaintActivityTAG";
    private float sizeOne; // первый размер лотка
    private float sizeTwo; // второй размер лотка
    private float sizeRadius; // радиус лотка
    private float thickness; //толщина плёнки
    private String nameProduct; // название лотка
    private String nameMaterial; // название материала
    private String nameMachine; // название машины
    private int bfs;  // вид вырубки bfs или совмещенная
    private float betweenProduct; //Между лотками
    private float forEdgeForm; //На край формы
    private float forChain; //На цепи
    private float maxLengthKnife; //Максімальная лина ножей
    private float formWidth; //ширина формы
    private float filmWidth; //ширина плёнки
    private float numProdFormWidth; //кол-во лотков по ширине плёнки
    private float formLength; //длина формы
    private float filmStep; //шаг формы
    private float numProdFormLenght; //кол-во лотков по шагу формы
    private float formPlace; //кол-во мест формы
    private float perimeterKnife; //длина ножей формы
    private float roundFilmWidth; //округлённый размер ширины плёнки
    private float roundFilmStep; //округлённый размер шага плёнки
    private float valueMass;  // Масса лотка
    private float valueMassLimit; // Допуск массы
    private float valueNorm; //Норма расхода
    private float valueScrap; // Отходность
    private final float startX = 120; //Начало расположения рисования кадра по х
    private final float startY = 120; //Начало расположения рисования кадра по у
    private float widthCadreView; //Ширина View с кадром
    private float heightCadreView; //Высота View с кадром
    private static int widthDisplay; //ширина экрана приложения
    private static float scalePxToDp; //Маштаб для View для растягивания на весь экран
    private Calculation calculation; //Результаты расчёта
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private LinearLayout linearLayout;
    static int counter; // Счетчик объектов создаваемых в этом классе
    private static final int PERMISSION_REQUEST_CODE = 123;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadLocal.onCreateLocale(this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle arguments = intent.getExtras();
        if (arguments != null) { //получаем данные из серлизованного объекта
            calculation = arguments.getParcelable(Calculation.class.getSimpleName());
            assert calculation != null;
            sizeOne = (float) calculation.getSizeOne();
            sizeTwo = (float) calculation.getSizeTwo();
            sizeRadius = (float) calculation.getSizeRadius();
            thickness = (float) calculation.getThickness();
            nameProduct = calculation.getNameProduct();
            nameMaterial = calculation.getNameMaterial();
            nameMachine = calculation.getNameMachine();
            bfs = calculation.getBFS();
            betweenProduct = (float) calculation.getBetweenProduct();
            forEdgeForm = (float) calculation.getForEdgeForm();
            forChain = (float) calculation.getForChain();
            maxLengthKnife = (float) calculation.getMaxLengthKnife();
            formWidth = (float) calculation.getFormWidth();
            filmWidth = (float) calculation.getFilmWidth();
            numProdFormWidth = (float) calculation.getNumProdFormWidth();
            formLength = (float) calculation.getFormLength();
            filmStep = (float) calculation.getFilmStep();
            numProdFormLenght = (float) calculation.getNumProdFormLenght();
            formPlace = (float) calculation.getFormPlace();
            perimeterKnife = (float) calculation.getPerimeterKnife();
            roundFilmWidth = (float) calculation.getRoundFilmWidth();
            roundFilmStep = (float) calculation.getRoundFilmStep();
            valueMass = (float) calculation.getValueMass();
            valueMassLimit = (float) calculation.getValueMassLimit();
            valueNorm = (float) calculation.getValueNorm();
            valueScrap = (float) calculation.getValueScrap();
        }
        setContentView(R.layout.activity_paint);

        Toolbar toolbar = findViewById(R.id.toolBarPaint);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar(); //кнопка "вверх"
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        //Распологаем кадр на activity
        linearLayout = findViewById(R.id.linearLayout);
        View dr = new DrawView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER; // Кадр по центру относительно макета
        dr.setLayoutParams(params);
        linearLayout.addView(dr, 0); // число - порядок располжения в LinearLayout

        TextView textView = findViewById(R.id.textViewResult);
        textView.setText(getResultText());

        //Добавляем фрагмент, если длина ножей больше максимальной длины ножей машины
        if (perimeterKnife >= maxLengthKnife) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();  // начинаем транзакцию
            fragment = new NumProductFragment(); // Создаем и добавляем фрагмент
            fragmentTransaction.add(R.id.fragmentLayout, fragment, NumProductFragment.TAG); //Добавляем фрагмент с его тэгом, потом по этому тегу будем его удалять
            fragmentTransaction.commit(); // Подтверждаем операцию
        }
        //Добавляем фрагмент, о том что нет данных о максимальной длине ножей
        if (maxLengthKnife == 1234567) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();  // начинаем транзакцию
            fragment = new InfoKnifeFragment(); // Создаем и добавляем фрагмент
            fragmentTransaction.add(R.id.fragmentLayout, fragment, InfoKnifeFragment.TAG); //Добавляем фрагмент с его тэгом, потом по этому тегу будем его удалять
            fragmentTransaction.commit(); // Подтверждаем операцию
        }
        counter++; // увеличиваем счётчик
        //if(counter % 5 == 0) { //проверка на деление без остатка
        //Log.d(TAG, "counter = " + counter  + " тут будет межстраничная реклама :) ");
        //}
        bannerAdView = findViewById(R.id.banner_ad_view); //Баннер рекламы AdMob
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
        eventsAd(); //отслеживание рекламных событий баннера
    }

    //Класс DrawView является наследником View и переопределяет его метод onDraw. А этот метод дает нам доступ к объекту Canvas.
    class DrawView extends View {
        Paint p;
        RectF rectProd;
        DrawCadre.Path2 cadre;
        DrawArrow.Path2 arrowSizeOne, arrowFormWidth, arrowFilmWidth, arrowSizeTwo, arrowFormLenght, arrowFilmStep, pathArrowSizeTwo, pathArrowFormLenght, pathArrowFilmStep;
        float[] textArrowSizeOne, textArrowFormWidth, textArrowFilmWidth, textArrowSizeTwo, textArrowFormLenght, textArrowFilmStep; //Координаты точки для текста размера
        int fontSize = 24;

        public DrawView(Context context) {
            super(context);
            p = new Paint();

            rectProd = new RectF(0, 0, sizeOne, sizeTwo); //Размеры лотка

            float forStep = 10; // Шаг формы
            cadre = new DrawCadre.Path2(startX, startY, formWidth, formLength, forChain, forStep); //Рисуем кадр
            float distanceToProd = 55; // Растояние между эскизом и размерной линией для лотка
            float distanceToForm = distanceToProd + 35; // Растояние между эскизом и размерной линией для формы
            float distanceToCadre = distanceToForm + 35; // Растояние между эскизом и размерной линией для кадра
            // Размер лотка и координаты текста горизонтальные
            arrowSizeOne = new DrawArrow.Path2((startX + forEdgeForm), (startY + formLength - forEdgeForm - sizeRadius), (startX + forEdgeForm + sizeOne),
                    (startY + formLength - forEdgeForm - sizeRadius), (distanceToProd + forEdgeForm + sizeRadius)); // Размер лотка горизонтальный
            textArrowSizeOne = new float[]{(arrowSizeOne.getCenterText()[0] + startX + forEdgeForm), (arrowSizeOne.getCenterText()[1] + startY + formLength - forEdgeForm - sizeRadius)}; //Координаты точки для текста размера
            // Размер формы и координаты текста горизонтальные
            arrowFormWidth = new DrawArrow.Path2(startX, (startY + formLength), (startX + formWidth), (startY + formLength), distanceToForm);// Размер формы горизонтальный
            textArrowFormWidth = new float[]{(arrowFormWidth.getCenterText()[0] + startX), (arrowFormWidth.getCenterText()[1] + startY + formLength)};
            // Размер кадра и координаты текста горизонтальные
            arrowFilmWidth = new DrawArrow.Path2((startX - forChain / 2), (startY + formLength + DrawCadre.Path2.getBeforeCurveY()), (startX + formWidth + forChain / 2),
                    (startY + formLength + DrawCadre.Path2.getBeforeCurveY()), (distanceToCadre - DrawCadre.Path2.getBeforeCurveY())); // Размер кадра горизонтальный
            textArrowFilmWidth = new float[]{(arrowFilmWidth.getCenterText()[0] + startX - forChain / 2), (arrowFilmWidth.getCenterText()[1] + startY + formLength + DrawCadre.Path2.getBeforeCurveY())};
            // Размер лотка и координаты текста вертикальные
            arrowSizeTwo = new DrawArrow.Path2((startX + formWidth - forEdgeForm - sizeRadius), (startY + forEdgeForm), (startX + formWidth - forEdgeForm - sizeRadius),
                    (startY + forEdgeForm + sizeTwo), (distanceToProd + forEdgeForm + sizeRadius)); // Размер лотка вертикальный
            textArrowSizeTwo = new float[]{(arrowSizeTwo.getCenterText()[0] + startX + formWidth - forEdgeForm - sizeRadius), (arrowSizeTwo.getCenterText()[1] + startY + forEdgeForm)};
            pathArrowSizeTwo = new DrawArrow.Path2(textArrowSizeTwo[0], textArrowSizeTwo[1]);
            // Размер формы и координаты текста вертикальные
            arrowFormLenght = new DrawArrow.Path2((startX + formWidth), startY, (startX + formWidth), (startY + formLength), distanceToForm); // Размер формы вертикальный
            textArrowFormLenght = new float[]{(arrowFormLenght.getCenterText()[0] + startX + formWidth), (arrowFormLenght.getCenterText()[1] + startY)};
            pathArrowFormLenght = new DrawArrow.Path2(textArrowFormLenght[0], textArrowFormLenght[1]);
            // Размер кадра и координаты текста вертикальные
            arrowFilmStep = new DrawArrow.Path2((startX + formWidth + forChain / 2), (startY - forStep), (startX + formWidth + forChain / 2),
                    (startY + formLength), (distanceToCadre - forChain / 2)); // Размер кадра вертикальный
            textArrowFilmStep = new float[]{(arrowFilmStep.getCenterText()[0] + startX + formWidth + forChain / 2), (arrowFilmStep.getCenterText()[1] + startY - forStep)};
            pathArrowFilmStep = new DrawArrow.Path2(textArrowFilmStep[0], textArrowFilmStep[1]);

            widthCadreView = 2*startX + formWidth + distanceToCadre - forChain/2; //Задаём размеры View
            heightCadreView = startY + filmStep + distanceToCadre;
            scalePxToDp = scalePixelsToDp(widthCadreView, this.getContext()); //маштаб для View
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //   canvas.translate(100, 100); //Перемещение канвы
            canvas.scale(scalePxToDp, scalePxToDp); //Маштабирование канвы по ширине экрана

            // canvas.drawColor(Color.WHITE);
            p.setColor(Color.BLACK);
            p.setStrokeWidth(2);
            p.setStyle(Paint.Style.STROKE); //Обводка линий снаружи, без заполнения

            canvas.drawRect(startX, startY, formWidth + startX, formLength + startY, p);  //Рисуем габариты формы

            float x = startX + forEdgeForm;
            float y = startY + forEdgeForm;

            for (int j = 0; j < numProdFormLenght; j++) { //Рисуем лотки в форме
                for (int i = 0; i < numProdFormWidth; i++) {
                    p.setColor(Color.rgb(32, 178, 170)); //LightSeaGreen цвет
                    p.setStrokeJoin(Paint.Join.ROUND);//Закругление углов
                    rectProd.offsetTo(x, y);
                    canvas.drawRoundRect(rectProd, sizeRadius, sizeRadius, p);
                    x = startX + forEdgeForm + sizeOne * (i + 1) + betweenProduct * (i + 1);
                }
                x = startX + forEdgeForm;
                y = startY + forEdgeForm + betweenProduct * (j + 1) + sizeTwo * (j + 1);
            }

            p.setColor(Color.rgb(255, 165, 0)); //Тёмно-оранжевый
            canvas.drawPath(cadre, p); //Добавляем кадр на канву

            p.setColor(Color.BLUE);
            //Добавляем линии размеров на канву
            canvas.drawPath(arrowSizeOne, p);
            canvas.drawPath(arrowFormWidth, p);
            canvas.drawPath(arrowFilmWidth, p);
            canvas.drawPath(arrowSizeTwo, p);
            canvas.drawPath(arrowFormLenght, p);
            canvas.drawPath(arrowFilmStep, p);

            //Добавляем текст размеров на канву
            p.setTextSize(fontSize);
            p.setStrokeWidth(1);
            p.setTextAlign(Paint.Align.CENTER);
            p.setStyle(Paint.Style.FILL);
            p.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));
            canvas.drawText(decimalTextArrow(sizeOne), textArrowSizeOne[0], textArrowSizeOne[1], p);
            canvas.drawText(decimalTextArrow(formWidth), textArrowFormWidth[0], textArrowFormWidth[1], p);
            canvas.drawText(decimalTextArrow(roundFilmWidth), textArrowFilmWidth[0], textArrowFilmWidth[1], p);
            canvas.drawTextOnPath(decimalTextArrow(sizeTwo), pathArrowSizeTwo, 0, 0, p);
            canvas.drawTextOnPath(decimalTextArrow(formLength), pathArrowFormLenght, 0, 0, p);
            canvas.drawTextOnPath(decimalTextArrow(roundFilmStep), pathArrowFilmStep, 0, 0, p);
        }

        // Для установки размера Custom View нужно переопределить метод onMeasure в котором вызвать метод setMeasuredDimension.
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) (widthCadreView * scalePxToDp), (int) (heightCadreView * scalePxToDp));
        }
    }

    /**
     * Вспомогательный метод, который высчитывает маштаб для увеличения или уменьшения View всего кадра по ширине экрана.
     *
     * @param widthView - размер View всего кадра по ширине в px
     * @param context - глобальная информация о среде приложения
     * @return маштаб перевода View из px в dp экрана приложения
     */
    public static float scalePixelsToDp(float widthView, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        widthDisplay = metrics.widthPixels;
        return widthDisplay / widthView;
    }

    /**
     * Вспомогательный метод, который возвращает ширину экрана приложения.
     *
     * @return - ширина экрана приложения в px
     */
    public static int getWidthDisplay() {
        return widthDisplay;
    }

    /**
     * Вспомогательный метод, который возвращает текст в виде "123,45", округляя десятичные.
     *
     * @param textArrow - число до округления
     * @return число в виде String после округления
     */
    public static String decimalTextArrow(float textArrow) {
        return new DecimalFormat("#.##").format(textArrow);
    }

    /**
     * Вспомогательный метод, позволяющий выводить результат расчётов в виде String
     *
     * @return текст результата в виде String
     */
    public String getResultText() {

        NumberFormat nf = new DecimalFormat("#.##");
        String strMaxLengthKnife = nf.format(maxLengthKnife) + getResources().getString(R.string.text_view_result_mm);
        if (maxLengthKnife == 1234567) { // "1234567" - нет данных по ножам
            strMaxLengthKnife = getResources().getString(R.string.text_view_result_no_data);
        }
        String punching = null; //название вырубки совмещённая или нет
        if (bfs == 1) punching = getResources().getString(R.string.text_view_bfs).toLowerCase();
        if (bfs == 0) punching = getResources().getString(R.string.text_view_punching).toLowerCase();

//                        " %s, %s, машина %s\n" +
//                        " Форма %sх%s мм, %s мест, %s вырубка\n" +
//                        " Длина ножей %s мм (при максимальной - %s)\n" +
//                        " Предварительный размер кадра %sх%s мм\n"  +
//                        " Теоретическая масса изделия %s±%s г, при толщине плёнки %s мкм\n" +
//                        " Норма расхода %s г, отходность %s%%"

        return String.format(getResources().getString(R.string.text_view_result),
                nameProduct, nameMaterial, nameMachine,
                nf.format(formWidth), nf.format(formLength), nf.format(formPlace), punching,
                nf.format(perimeterKnife), strMaxLengthKnife,
                nf.format(roundFilmWidth), nf.format(roundFilmStep),
                nf.format(valueMass), nf.format(valueMassLimit), nf.format(thickness),
                nf.format(valueNorm), nf.format(valueScrap));
    }

    /**
     * При превышении длины ножей, метод перезапускает расчёт и активити с новыми параметрами формы.
     *
     * @param userNumProdFormWidth  - количество изделий по ширине формы, заданная пользователем
     * @param userNumProdFormLenght - количество изделий по шагу формы, заданная пользователем
     */
    private void userNumProduct(float userNumProdFormWidth, float userNumProdFormLenght) {
        Calculation calcUser = new Calculation(calculation, userNumProdFormWidth, userNumProdFormLenght);
        Intent intent = new Intent(this, this.getClass()); //Перезапускаем активити
        intent.putExtra(Calculation.class.getSimpleName(), calcUser);
        finish();
        this.startActivity(intent);
    }

    /**
     * При нажатии кнопки, метод убирает один ряд изделий по ширине формы.
     *
     * @param view - вызывается при нажании на кнопку
     */
    public void onClickMinusNumWidth(View view) {
        userNumProduct(numProdFormWidth, (numProdFormLenght - 1));
    }

    /**
     * При нажатии кнопки, метод убирает один ряд изделий по шагу формы.
     *
     * @param view - вызывается при нажании на кнопку
     */
    public void onClickMinusNumLenght(View view) {
        userNumProduct((numProdFormWidth - 1), numProdFormLenght);

    }

    /**
     * Метод, который убирает фрагмент с экрана
     *
     * @param view - вызывается при нажании на кнопку
     */
    public void onClickClose(View view) {
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    //Добавляем Меню на панель  приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_paint, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Для реагирования нашей активити на щелчки в панели приложения
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_save) {

            if (hasPermissions()){ // у нашего приложения есть разрешения
                makeFile();
            }
            else { //наше приложение не имеет разрешений, поэтому запрашиваем разрешения
              requestPermissionWithRationale();
            }
        }

        if (item.getItemId() == R.id.action_send) {
            PdfDocument document = SaveFile.generatePdf(linearLayout);
            File path = this.getCacheDir(); //папка кеша приложения
            File filePath = new File(path, nameProduct + " - " + SaveFile.getDate(this) + " send" + ".pdf");
            SaveFile.savePDF(document, this, filePath, false);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            Uri uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", filePath);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chosenIntent = Intent.createChooser(intent, null); //без выбора "по умолчанию"
            startActivity(chosenIntent);
        }

        if (item.getItemId() == R.id.action_info) {
            InfoDialogFragment dialog = new InfoDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Метод позволяет отслеживать рекламные события AdMod
     */
    private void eventsAd() {
        if (bannerAdView != null) {
            bannerAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    Log.d(TAG, "onAdClicked: >>Пользователь нажал на объявление.");
                }
                @Override
                public void onAdClosed() {
                    Log.d(TAG, "onAdClosed: >>Возврат в приложение после перехода по ссылке.");
                }
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    Log.d(TAG, "onAdFailedToLoad: >>Ошибка \n " + adError);
                }
                @Override
                public void onAdImpression() {
                    Log.d(TAG, "onAdImpression: >>Pегистрация показа объявления.");
                }
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: >>Завершение загрузки рекламы.");
                }
                @Override
                public void onAdOpened() {
                    Log.d(TAG, "onAdOpened: >>Пользователь нажал на ссылку рекламы.");
                }
            });
        }
    }

    // Ниже по коду методы необходимые для добавления разрешения на запись в файловую систему телефона.
    // Создаём файл и сохраняем его в папке downloads.
    private void makeFile(){
        PdfDocument document = SaveFile.generatePdf(linearLayout);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //папка downloads
        File filePath = new File(path, nameProduct + " - " + SaveFile.getDate(this) + ".pdf");
        SaveFile.savePDF(document, this, filePath, true);

    }

    // Метод помощью метода checkCallingOrSelfPermission в цикле проверяет предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
    private boolean hasPermissions(){
        int res;
        //Массив string разрешений (permissions)
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    //В методе requestPerms создаем массив permissions , который содержит ссылки на константы класса Manifest
    //с кодами разрешений. После вызова этого метода пользователю отображается диалоговое окно с запросом разрешения.
    @SuppressLint("ObsoleteSdkInt")
    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}; //проверка версии устройства, больше ли API 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    // После того, как пользователь ответит на диалоговое окно системных разрешений, ответ пользователя приходит в метод onRequestPermissionsResult()
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allowed = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults) {
                    // если пользователь предоставил все разрешения.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // если пользователю не предоставлены разрешения.
                allowed = false;
                break;
        }

        if (allowed) {
            //пользователю предоставлены все разрешения, которые мы можем выполнить.
            makeFile();
        } else {
            // мы предупредим пользователя о том, что он не предоставил разрешения.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.storage_permissions_denied, Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }
    }

    // Метод создаёт Snackbar с кнопкой действия, ведущей в настройки разрешений приложения, и генерирующей тост для пользователя с инструкцией.
    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(PaintActivity.this.findViewById(R.id.activityPaintView), R.string.storage_permission_is_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.settings_upper_case, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(), R.string.open_permissions_and_grant_the_storage_permission, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    //Метод открывает настройки, где мы создаем и отправляем соответствующий интент.
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    //Метод определяет, что пользователь вернулся из activity настроек обратно в приложение и пробует выполнить действие.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            makeFile();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Если вы ранее уже запрашивали разрешение, но пользователь отказался предоставить его, необходимо объяснить ему причину запроса.
    //Здесь также проверяем ответ метода shouldShowRequestPermissionRationale и если он true, создаем снекбар с сообщением для пользователя и кнопкой, по нажатию на которую будем вызывать метод получения разрешения.
    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final String message = getString(R.string.storage_permission_is_required_to_save_the_file);
            Snackbar.make(PaintActivity.this.findViewById(R.id.activityPaintView), message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.grant_upper_case, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPerms();
                        }
                    })
                    .show();
        } else {
            requestPerms();
        }
    }
}



