package by.rusakou.norma.parser;

import androidx.annotation.NonNull;

/**
 * Класс для создания объекта Коэффициент, с необходимыми переменными.
 */
public class Coefficient {
    private String name;
    private double coefficientForNorm;

    public String getName() {   return name;    }
    public double getCoefficientForNorm() {  return coefficientForNorm;   }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoefficientForNorm(String coefficientForNorm) {
        this.coefficientForNorm = Double.parseDouble(coefficientForNorm);
    }

    @NonNull
    public String toString(){
        return  name + " " + coefficientForNorm;
    }
}