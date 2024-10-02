package by.rusakou.norma;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import by.rusakou.norma.parser.*;

/**
 * Класс для расчета параметров формы.
 */
public class Calculation implements Parcelable {

    public static final String TAG = "CalculationTAG";
    private double sizeOne; // первый размер лотка с усадкой
    private double sizeTwo; // второй размер лотка с усадкой
    private double sizeRadius; // радиус лотка
    private double thickness; // толщина плёнки
    private String nameProduct; //название лотка
    private String nameMaterial; //название материала
    private double shrinkage; //усадка
    private double density; //плотность
    private String nameMachine; //название оборудования
    private double maxFormWidth; //Максимальная ширина формы
    private double maxFormLength; //Максимальная длина формы по шагу
    private int bfs; // вид вырубки bfs или совмещенная
    private double betweenProduct; //Между лотками
    private double forEdgeForm; //На край формы
    private double forChain; //На цепи
    private double maxLengthKnife; //Максімальная лина ножей
    private double coefficientForNorm;
    private double formWidth; //ширина формы
    private double filmWidth; //ширина плёнки
    private double numProdFormWidth; //кол-во лотков по ширине плёнки
    private double formLength; //длина формы
    private double filmStep; //шаг формы
    private double numProdFormLenght; //кол-во лотков по шагу формы
    private double formPlace; //кол-во мест формы
    private double perimeterKnife; //длина ножей формы
    private double roundFilmWidth; //округлённый размер ширины плёнки
    private double roundFilmStep; //округлённый размер шага плёнки
    private double valueMass;  // Масса лотка
    private double valueMassLimit; // Допуск массы
    private double valueNorm; //Норма расхода
    private double valueScrap; // Отходность

    /**
     * Расчёт параметров формы.
     *
     * @param sizeOneProduct - первый размер изделия
     * @param sizeTwoProduct - первый размер изделия
     * @param sizeRadiusProduct - радиус изделия
     * @param thickness - первый размер изделия
     * @param nameProduct - название изделия
     * @param material - материал
     * @param machine - оборудование
     * @param bfs - вырубка совмещенная (true), вырубка раздельная (false)
     * @param coefficient - коэффициент для расчёта нормы расхода
     */
    public Calculation(double sizeOneProduct, double sizeTwoProduct, double sizeRadiusProduct, double thickness,
                       String nameProduct, Material material, Machine machine, boolean bfs, Coefficient coefficient) {

        this.thickness = thickness;
        this.nameProduct = nameProduct;
        this.nameMaterial = material.getName();
        this.shrinkage = material.getShrinkage();
        this.density = material.getDensity();
        this.nameMachine = machine.getName();
        this.maxFormWidth = machine.getMaxFormWidth();
        this.maxFormLength= machine.getMaxFormLength();
        this.bfs = (bfs) ? 1 : 0; //передаём в Parcelable int, т.к. boolean он не передаёт. 1 = true, 0 = false (это для названия вырубки в результате)
        if (bfs) { //если вырубка совмещенная
            this.betweenProduct = machine.getBetweenProductBFS();
            this.forEdgeForm = machine.getForEdgeFormBFS();
        }
        if (!bfs) { //если вырубка раздельная
            this.betweenProduct = machine.getBetweenProductPunching();
            this.forEdgeForm = machine.getForEdgeFormPunching();
        }
        this.forChain = machine.getForChain();
        boolean arrayMaxKnifeThickness = machine.isArrayMaxKnifeThickness();
        if(!arrayMaxKnifeThickness){ //если не задан массив максимальных длин ножей от толщины плёнки, а задана одна длина ножей
            if (material.getName().equals("PS")) this.maxLengthKnife = machine.getMaxLengthKnifePS();
            if (material.getName().equals("PET")) this.maxLengthKnife = machine.getMaxLengthKnifePET();
            if (material.getName().equals("PVC")) this.maxLengthKnife = machine.getMaxLengthKnifePVC();
            if (material.getName().equals("PP")) this.maxLengthKnife = machine.getMaxLengthKnifePP();
        }
        if(arrayMaxKnifeThickness){ //если задан массив максимальных длин ножей от толщины плёнки
            if (material.getName().equals("PS")) this.maxLengthKnife = MathOp.runMaxLengthKnifeThickness(thickness, machine.getArrayMaxLengthKnifePS());
            if (material.getName().equals("PET")) this.maxLengthKnife = MathOp.runMaxLengthKnifeThickness(thickness, machine.getArrayMaxLengthKnifePET());
            if (material.getName().equals("PVC")) this.maxLengthKnife = MathOp.runMaxLengthKnifeThickness(thickness, machine.getArrayMaxLengthKnifePVC());
            if (material.getName().equals("PP")) this.maxLengthKnife = MathOp.runMaxLengthKnifeThickness(thickness, machine.getArrayMaxLengthKnifePP());
        }
        //Если задан коэффициент вырубки для BFS, то применяем его
        if (bfs & machine.getCoefficientKnifeBFS() > 0) this.maxLengthKnife = maxLengthKnife * machine.getCoefficientKnifeBFS();

        if (machine.isTypeMachineK()) this.coefficientForNorm = coefficient.getCoefficientForNorm();
        if (!machine.isTypeMachineK()) this.coefficientForNorm = coefficient.getCoefficientForNorm();

        sizeOne = MathOp.runSizeKnife(sizeOneProduct, shrinkage); // Расчитываем размер лотка до усадки (по ножу)
        sizeTwo = MathOp.runSizeKnife(sizeTwoProduct, shrinkage);
        sizeRadius = MathOp.runSizeKnife(sizeRadiusProduct, shrinkage); // Расчитываем размер радиуса до усадки (по ножу)
        double valueArea = MathOp.runAreaKnife(sizeOne, sizeTwo, sizeRadius); //Площадь по ножу до усадки
        valueMass = MathOp.runMass(valueArea, thickness,	density); // Масса лотка
        valueMassLimit = MathOp.runMassLimit(valueMass, thickness); // Допуск массы

        ArrayList<Double> calc =  MathOp.runCalcForm(sizeOne, sizeTwo, sizeRadius, maxFormWidth, maxFormLength, betweenProduct,
                forEdgeForm, forChain); // Расчитываем параметры формы
        formWidth  = calc.get(0);
        filmWidth = calc.get(1);
        numProdFormWidth = calc.get(2);
        formLength = calc.get(3);
        filmStep = calc.get(4);
        numProdFormLenght = calc.get(5);
        formPlace = calc.get(6);
        perimeterKnife = calc.get(7);

        ArrayList<Double> round = MathOp.runRoundCadre(calc.get(1), calc.get(4)); // Расчитываем округленный кадр формы
        roundFilmWidth = round.get(0); //округлённый размер ширины плёнки
        roundFilmStep = round.get(1); //округлённый размер шага плёнки

        valueNorm = MathOp.runNorm(roundFilmWidth, roundFilmStep,thickness,
                density, coefficientForNorm, calc.get(6)); // Расчитываем норму расхода
        valueScrap =  MathOp.runScrapPercent(valueNorm, valueMass); // Расчитываем отходность в %
    }

    /**
     * Расчёт параметров формы, откорректированной пользователем.
     * Расчитываются новые параметры формы, на основе прежних данных, но с другим количеством изделий.
     *
     * @param calc - данные существующей формы, которые собираемся пересчитывать
     * @param newNumProdFormWidth - новое количество изделий по ширине формы
     * @param newNumProdFormLenght - новое количество изделий по шагу формы
     */
    public Calculation(Calculation calc, double newNumProdFormWidth, double newNumProdFormLenght) {
        
         sizeOne = calc.getSizeOne();
         sizeTwo = calc.getSizeTwo();
         sizeRadius = calc.getSizeRadius();
         thickness = calc.getThickness();
         nameProduct = calc.getNameProduct();
         nameMaterial = calc.getNameMaterial();
         shrinkage = calc.getShrinkage();
         density = calc.getDensity();
         nameMachine = calc.getNameMachine();
         maxFormWidth = calc.getMaxFormWidth();
         maxFormLength = calc.getMaxFormLength();
         betweenProduct = calc.getBetweenProduct();
         forEdgeForm = calc.getForEdgeForm();
         forChain = calc.getForChain();
         maxLengthKnife = calc.getMaxLengthKnife();
         coefficientForNorm = calc.getCoefficientForNorm();
         valueMass = calc.getValueMass();
         valueMassLimit = calc.valueMassLimit;

        ArrayList<Double> calc2 =  MathOp.runCalcFormUser(sizeOne, sizeTwo, sizeRadius,  betweenProduct,
                                              forEdgeForm, forChain, newNumProdFormWidth, newNumProdFormLenght); // Расчитываем параметры формы
        formWidth  = calc2.get(0);
        filmWidth = calc2.get(1);
        numProdFormWidth = calc2.get(2);
        formLength = calc2.get(3);
        filmStep = calc2.get(4);
        numProdFormLenght = calc2.get(5);
        formPlace = calc2.get(6);
        perimeterKnife = calc2.get(7);

        ArrayList<Double> round = MathOp.runRoundCadre(calc2.get(1), calc2.get(4)); // Расчитываем округленный кадр формы
        roundFilmWidth = round.get(0); //округлённый размер ширины плёнки
        roundFilmStep = round.get(1); //округлённый размер шага плёнки
        valueNorm = MathOp.runNorm(roundFilmWidth, roundFilmStep, thickness,
                density, coefficientForNorm, calc2.get(6)); // Расчитываем норму расхода
        valueScrap =  MathOp.runScrapPercent(valueNorm, valueMass); // Расчитываем отходность в %
  }

    // Ниже по коду создаём объекты Parcelable для передачи между двумя activity.
    // Используем сериализованый объект Calculation в дальнейшем в PaintActivity.
    public double getSizeOne() {        return sizeOne;    }
    public double getSizeTwo() {        return sizeTwo;    }
    public double getSizeRadius() {        return sizeRadius;    }
    public double getThickness() {        return thickness;    }
    public String getNameProduct() {        return nameProduct;    }
    public String getNameMaterial() {        return nameMaterial;    }
    public double getShrinkage() {        return shrinkage;    }
    public double getDensity() {        return density;    }
    public String getNameMachine() {        return nameMachine;    }
    public double getMaxFormWidth() {        return maxFormWidth;    }
    public double getMaxFormLength() {        return maxFormLength;    }
    public int getBFS() {        return bfs;    }
    public double getBetweenProduct() {        return betweenProduct;    }
    public double getForEdgeForm() {        return forEdgeForm;    }
    public double getForChain() {        return forChain;    }
    public double getMaxLengthKnife() {        return maxLengthKnife;    }
    public double getCoefficientForNorm() {        return coefficientForNorm;    }
    public double getFormWidth() {        return formWidth;    }
    public double getFilmWidth() {        return filmWidth;    }
    public double getNumProdFormWidth() {        return numProdFormWidth;    }
    public double getFormLength() {        return formLength;    }
    public double getFilmStep() {        return filmStep;    }
    public double getNumProdFormLenght() {        return numProdFormLenght;    }
    public double getFormPlace() {        return formPlace;    }
    public double getPerimeterKnife() {        return perimeterKnife;    }
    public double getRoundFilmWidth() {        return roundFilmWidth;    }
    public double getRoundFilmStep() {        return roundFilmStep;    }
    public double getValueMass() {        return valueMass;    }
    public double getValueMassLimit() {        return valueMassLimit;    }
    public double getValueNorm() {        return valueNorm;    }
    public double getValueScrap() {        return valueScrap;    }

    public static final Creator<Calculation> CREATOR = new Creator<Calculation>() {
        @Override
        public Calculation createFromParcel(Parcel source) {
            double sizeOne = source.readDouble();
            double sizeTwo = source.readDouble();
            double sizeRadius = source.readDouble();
            double thickness = source.readDouble();
            String nameProduct = source.readString();
            String nameMaterial = source.readString();
            double shrinkage = source.readDouble();
            double density = source.readDouble();
            String nameMachine = source.readString();
            double maxFormWidth = source.readDouble();
            double maxFormLength = source.readDouble();
            int bfs = source.readInt();
            double betweenProduct = source.readDouble();
            double forEdgeForm = source.readDouble();
            double forChain = source.readDouble();
            double maxLengthKnife = source.readDouble();
            double coefficientForNorm = source.readDouble();
            double formWidth = source.readDouble();
            double filmWidth = source.readDouble();
            double numProdFormWidth = source.readDouble();
            double formLength = source.readDouble();
            double filmStep = source.readDouble();
            double numProdFormLenght = source.readDouble();
            double formPlace = source.readDouble();
            double perimeterKnife = source.readDouble();
            double roundFilmWidth = source.readDouble();
            double roundFilmStep = source.readDouble();
            double valueMass = source.readDouble();
            double valueMassLimit = source.readDouble();
            double valueNorm = source.readDouble();
            double valueScrap = source.readDouble();

            return new Calculation(sizeOne, sizeTwo, sizeRadius, thickness,
                    nameProduct, nameMaterial, shrinkage, density,
                    nameMachine, maxFormWidth, maxFormLength, bfs, betweenProduct,
                    forEdgeForm, forChain, maxLengthKnife, coefficientForNorm,
                    formWidth, filmWidth, numProdFormWidth,
                    formLength, filmStep, numProdFormLenght, formPlace, perimeterKnife,
                    roundFilmWidth, roundFilmStep, valueMass, valueMassLimit,
                    valueNorm, valueScrap);
        }

        @Override
        public Calculation[] newArray(int size) {
            return new Calculation[size];
        }
    };
    private Calculation(double sizeOne, double sizeTwo, double sizeRadius, double thickness,
                        String nameProduct, String nameMaterial, double shrinkage, double density,
                        String nameMachine, double maxFormWidth, double maxFormLength, int bfs, double betweenProduct,
                        double forEdgeForm, double forChain, double maxLengthKnife, double coefficientForNorm,
                        double formWidth, double filmWidth, double numProdFormWidth,
                        double formLength, double filmStep, double numProdFormLenght, double formPlace, double perimeterKnife,
                        double roundFilmWidth, double roundFilmStep, double valueMass, double valueMassLimit,
                        double valueNorm, double valueScrap) {
        this.sizeOne = sizeOne;
        this.sizeTwo = sizeTwo;
        this.sizeRadius = sizeRadius;
        this.thickness = thickness;
        this.nameProduct = nameProduct;
        this.nameMaterial = nameMaterial;
        this.shrinkage = shrinkage;
        this.density = density;
        this.nameMachine = nameMachine;
        this.maxFormWidth = maxFormWidth;
        this.maxFormLength = maxFormLength;
        this.bfs = bfs;
        this.betweenProduct = betweenProduct;
        this.forEdgeForm = forEdgeForm;
        this.forChain = forChain;
        this.maxLengthKnife = maxLengthKnife;
        this.coefficientForNorm = coefficientForNorm;
        this.formWidth = formWidth;
        this.filmWidth = filmWidth;
        this.numProdFormWidth = numProdFormWidth;
        this.formLength = formLength;
        this.filmStep = filmStep;
        this.numProdFormLenght = numProdFormLenght;
        this.formPlace = formPlace;
        this.perimeterKnife = perimeterKnife;
        this.roundFilmWidth = roundFilmWidth;
        this.roundFilmStep = roundFilmStep;
        this.valueMass = valueMass;
        this.valueMassLimit = valueMassLimit;
        this.valueNorm = valueNorm;
        this.valueScrap = valueScrap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(sizeOne);
        dest.writeDouble(sizeTwo);
        dest.writeDouble(sizeRadius);
        dest.writeDouble(thickness);
        dest.writeString(nameProduct);
        dest.writeString(nameMaterial);
        dest.writeDouble(shrinkage);
        dest.writeDouble(density);
        dest.writeString(nameMachine);
        dest.writeDouble(maxFormWidth);
        dest.writeDouble(maxFormLength);
        dest.writeInt(bfs);
        dest.writeDouble(betweenProduct);
        dest.writeDouble(forEdgeForm);
        dest.writeDouble(forChain);
        dest.writeDouble(maxLengthKnife);
        dest.writeDouble(coefficientForNorm);
        dest.writeDouble(formWidth);
        dest.writeDouble(filmWidth);
        dest.writeDouble(numProdFormWidth);
        dest.writeDouble(formLength);
        dest.writeDouble(filmStep);
        dest.writeDouble(numProdFormLenght);
        dest.writeDouble(formPlace);
        dest.writeDouble(perimeterKnife);
        dest.writeDouble(roundFilmWidth);
        dest.writeDouble(roundFilmStep);
        dest.writeDouble(valueMass);
        dest.writeDouble(valueMassLimit);
        dest.writeDouble(valueNorm);
        dest.writeDouble(valueScrap);
    }
}
