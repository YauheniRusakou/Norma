package by.rusakou.norma.file;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

import by.rusakou.norma.MainActivity;
import by.rusakou.norma.R;

public class LangSetting {

    static int counter; // Счетчик объектов создаваемых в этом классе
    int x;
    int y;
    int z;
    String string;

    public LangSetting(String string){
        counter++;
        this.x = counter;
        this.y = counter;
        this.z = counter;
        this.string = string;
    };

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getString() {
        return string;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString(){
        return  String.format("%d %d %d %s ",
                x, y, z, string);
    }

}
