package by.rusakou.norma.file;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import by.rusakou.norma.PaintActivity;
import by.rusakou.norma.R;

public class SaveFile {

    public static final String TAG = "SaveFileTAG";

    /**
     * Нельзя создавать объекты этого класса
     */
    private SaveFile() {}

    /**
     * Метод создаёт файл pdf из результатов расчётов.
     *
     * @return pdf файл
     */
    public static PdfDocument generatePdf(ViewGroup layout){
        int height = layout.getHeight() + 100;// высота Layout с результатами + поля
        int width = PaintActivity.getWidthDisplay() + 150; // ширина экрана + поля

        PdfDocument  document = new PdfDocument(); // создаем документ
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(width, height, 1).create(); // определяем размер страницы
        PdfDocument.Page page = document.startPage(pageInfo); // получаем страницу, на котором будем генерировать контент

        Canvas ca = page.getCanvas();
        int start = (width - layout.getWidth())/2;
        ca.translate(start, 0); //Перемещение канвы по центру ширины
//        linearLayout.draw(page.getCanvas());
        layout.draw(ca);  // рисуем view на страницу

        document.finishPage(page);
       // document.close(); // закрываем документ //закрываем в других методах (обязательно)
        return document;
    }

    /** Метод сохраняет файл pdf из результатов расчётов.
     *
     * @param document - PdfDocument результата расчёта
     * @param context - Context активити
     * @param filePath - путь, куда сохраняем
     * @param toast - вывод сообщения о сохранении, если true - выводим, false - нет
     */
    public static void savePDF(PdfDocument document, Context context, File filePath, boolean toast){
        // сохраняем записанный контент
        try {
            document.writeTo(new FileOutputStream(filePath));
            if (toast) Toast.makeText(context, R.string.file_saved_in_download, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.file_failed_to_save, Toast.LENGTH_LONG).show();
        }
        document.close(); // закрываем документ
    }

    /**
     * Метод позволяющий выводить дату и время в формате "28-02-2024 15-52".
     * При локали США дата "02-28-2024".
     * При локали стран США, Канады, Австралии, Новой Зеландии и Филлипин время в формате "03-52 PM".
     *
     * @return дата и время в формате String
     */
    public static String getDate(Context context) {

        Locale locale = LoadLocal.restoreFile(context);
        //Если страна не сохранена в настройках, то берём из настроек телефона
        String strLoc = (locale.getCountry().isEmpty()) ?  Locale.getDefault().getCountry() : LoadLocal.restoreFile(context).getCountry(); //Тернарный оператор
        Date nowDate = new Date();
        String dateFormat;

        if(strLoc.equals("US")){
            dateFormat = String.format(Locale.US,"%1$tm-%1$td-%1$tY", nowDate); //для США ММ-ДД-ГГГГ
        } else dateFormat = String.format(locale,"%1$td-%1$tm-%1$tY", nowDate); //для остальных ДД-ММ-ГГГГ

        if (strLoc.equals("US") | strLoc.equals("CA") | strLoc.equals("AU") | strLoc.equals("NZ") | strLoc.equals("PH")) {
            dateFormat = String.format("%1$s %2$tI-%2$tM %2$Tp",dateFormat, nowDate);
        } else { dateFormat = String.format("%1$s %2$tH-%2$tM",dateFormat, nowDate);
        }
        Log.d(TAG, "date " + dateFormat);
        return dateFormat;
    }

}
