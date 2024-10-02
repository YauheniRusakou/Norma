package by.rusakou.norma.parser;

import androidx.annotation.NonNull;

/**
 * Класс для создания объекта Материал, с необходимыми переменными.
 */
public class Material {
    private String name;
    private double shrinkage;
    private double density;

    public String getName() {   return name;    }

    public double getShrinkage() {  return shrinkage;   }

    public double getDensity() {    return density; }

    public void setName(String name) {
        this.name = name;
    }

    public void setShrinkage(String shrinkage) {
        this.shrinkage = Double.parseDouble(shrinkage);
    }

    public void setDensity(String density) {
        this.density = Double.parseDouble(density);
    }

    @NonNull
    public String toString(){
        return  name + " " + shrinkage + " " + density;
    }
}
