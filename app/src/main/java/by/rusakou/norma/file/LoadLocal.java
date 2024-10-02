package by.rusakou.norma.file;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

/*
  @author Русаков Евгений Михайлович.
 */
/**
 * В данном классе сохраняется, востанавливается и очищается значение из памяти.
 */
public class LoadLocal {

    public static final String TAG = "LoadLocalTAG";
    public static String path = File.separator + "loadLocal" + File.separator + "loadLocal.ser";
    /**
     * Нельзя создавать объекты этого класса
     */
    private LoadLocal() {}

    /**
     * Метод, который позволяет сохранить локаль в память.
     *
     * @param local значение для сохранения локали в памяти.
     * @param context - контекст текущего состояния приложения
     */
    public static void saveFile(Locale local, Context context) {
        try {
            if (! new File(context.getFilesDir().getPath()+File.separator +"loadLocal").mkdir()){
                String outFileName = context.getFilesDir().getPath() + path;
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(outFileName));
                os.writeObject(local);
            }
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "! Error writing memory: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Метод, который позволяет востановить локаль из памяти.
     *
     * @param context - контекст текущего состояния приложения
     * @return ранее сохраненное значение локали из памяти.
     */
    public static Locale restoreFile(Context context) {
        Locale local = null;
        try {
            String fileName = context.getFilesDir().getPath() + path;
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            local = (Locale) is.readObject();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "! Error while restoring memory: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return local;
    }

    /**
     * Метод, который проверяет проверяет наличие файла локали.
     * Если файл с локалью есть, то обновляет конфигурацию перед запуском активити.
     * Если его нет, то создает его с локалью по умолчанию.
     *
     * @param context - контекст текущего состояния приложения
     */
    public static void onCreateLocale(Context context) {
        File file = new File(context.getFilesDir().getPath() + path);
        if (file.exists()) { //если файл существует, то читаем локаль из него и обновляем конфигурацию
            Configuration configuration = new Configuration();
            configuration.setLocale(LoadLocal.restoreFile(context));
            context.getResources().updateConfiguration(configuration, null);
        } else { //если файла нет, то создаем его с сонфигурацией по умолчанию
            LoadLocal.saveFile(Locale.getDefault(), context);
        }
    }

}