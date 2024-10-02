package by.rusakou.norma.parser;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.Arrays;

/**
 * Класс для создания объекта Машина, с необходимыми переменными.
 */
public class Machine {
    private String name;
    private String modelMachine; //Модель машины
    private double maxFormWidth; //Максимальная ширина формы
    private double maxFormLength; //Максимальная длина формы по шагу
    private boolean machineBFS; //Машина с BFS
    private double betweenProductBFS; //Между лотками для машин с BFS
    private double forEdgeFormBFS; //На край формы для машин с BFS
    private boolean machinePunching; //Машина с раздельный вырубкой
    private double betweenProductPunching; //Между лотками для машин с раздельный вырубкой
    private double forEdgeFormPunching; //На край формы для машин с раздельный вырубкой
    private double forChain; //На цепи
    private boolean usedPP; //Использование РР
    private double maxLengthKnifePS; //Максімальная длина ножей РS
    private double maxLengthKnifePET; //Максімальная длина ножей РЕТ
    private double maxLengthKnifePVC; //Максімальная длина ножей РVC
    private double maxLengthKnifePP; //Максімальная длина ножей РР
    private double coefficientKnifeBFS; //Коэффициент уменьшения длины ножей при BFS
    private boolean arrayMaxKnifeThickness; // Массив максимальных длин ножей в зависимости от толщины плёнки
    private double[] arrayMaxLengthKnifePS; //Массив максимальных длин ножей РS в зависимости от толщины плёнки
    private double[] arrayMaxLengthKnifePET; //Массив максимальных длин ножей РET в зависимости от толщины плёнки
    private double[] arrayMaxLengthKnifePVC; //Массив максимальных длин ножей РVC в зависимости от толщины плёнки
    private double[] arrayMaxLengthKnifePP; //Массив максимальных длин ножей РP в зависимости от толщины плёнки
    private boolean typeMachineK; //Машина типа Kiefel, Illig и т.д.


    public String getName() {   return name;    }
    public String getModelMachine() {   return modelMachine;    }
    public double getMaxFormWidth() {    return maxFormWidth;   }
    public double getMaxFormLength() {    return maxFormLength;    }
    public boolean isMachineBFS() {    return machineBFS;    }
    public double getForEdgeFormBFS() {   return forEdgeFormBFS;    }
    public double getBetweenProductBFS() {   return betweenProductBFS;    }
    public boolean isMachinePunching() {    return machinePunching;    }
    public double getBetweenProductPunching() {   return betweenProductPunching;    }
    public double getForEdgeFormPunching() {    return forEdgeFormPunching;    }
    public double getForChain() {    return forChain;    }
    public boolean isUsedPP() {    return usedPP;    }
    public double getMaxLengthKnifePS() {  return maxLengthKnifePS;  }
    public double getMaxLengthKnifePET() {  return maxLengthKnifePET;  }
    public double getMaxLengthKnifePVC() {  return maxLengthKnifePVC;  }
    public double getMaxLengthKnifePP() {  return maxLengthKnifePP;  }
    public double getCoefficientKnifeBFS() {  return coefficientKnifeBFS;  }
    public boolean isArrayMaxKnifeThickness() {        return arrayMaxKnifeThickness;    }
    public double[] getArrayMaxLengthKnifePS() {        return arrayMaxLengthKnifePS;    }
    public double[] getArrayMaxLengthKnifePET() {        return arrayMaxLengthKnifePET;    }
    public double[] getArrayMaxLengthKnifePVC() {        return arrayMaxLengthKnifePVC;    }
    public double[] getArrayMaxLengthKnifePP() {        return arrayMaxLengthKnifePP;    }
    public boolean isTypeMachineK() {    return typeMachineK;    }

    public void setName(String name) {
        this.name = name;
    }
    public void setModelMachine(String modelMachine) {
        this.modelMachine = modelMachine;
    }
    public void setMaxFormWidth(String maxFormWidth) {
        this.maxFormWidth =  Double.parseDouble(maxFormWidth);
    }
    public void setMaxFormLength(String maxFormLength) {
        this.maxFormLength =  Double.parseDouble(maxFormLength);
    }
    public void setMachineBFS(String machineBFS) {
        this.machineBFS =  Boolean.parseBoolean(machineBFS);
    }
    public void setBetweenProductBFS(String betweenProductBFS) {
        this.betweenProductBFS = Double.parseDouble(betweenProductBFS);
    }
    public void setForEdgeFormBFS(String forEdgeFormBFS) {
        this.forEdgeFormBFS = Double.parseDouble(forEdgeFormBFS);
    }
    public void setMachinePunching(String machinePunching) {
        this.machinePunching =  Boolean.parseBoolean(machinePunching);
    }
    public void setBetweenProductPunching(String betweenProductPunching) {
        this.betweenProductPunching = Double.parseDouble(betweenProductPunching);
    }
    public void setForEdgeFormPunching(String forEdgeFormPunching) {
        this.forEdgeFormPunching = Double.parseDouble(forEdgeFormPunching);
    }
    public void setForChain(String forChain) {
        this.forChain = Double.parseDouble(forChain);
    }
    public void setUsedPP(String usedPP) {
        this.usedPP =  Boolean.parseBoolean(usedPP);
    }
    public void setMaxLengthKnifePS(String maxLengthKnifePS) {
        this.maxLengthKnifePS = Double.parseDouble(maxLengthKnifePS);
    }
    public void setMaxLengthKnifePET(String maxLengthKnifePET) {
        this.maxLengthKnifePET = Double.parseDouble(maxLengthKnifePET);
    }
    public void setMaxLengthKnifePVC(String maxLengthKnifePVC) {
        this.maxLengthKnifePVC = Double.parseDouble(maxLengthKnifePVC);
    }
    public void setMaxLengthKnifePP(String maxLengthKnifePP) {
        this.maxLengthKnifePP = Double.parseDouble(maxLengthKnifePP);
    }
    public void setCoefficientKnifeBFS(String coefficientKnifeBFS) {
        this.coefficientKnifeBFS = Double.parseDouble(coefficientKnifeBFS);
    }
    public void setArrayMaxKnifeThickness(String arrayMaxKnifeThickness) {
        this.arrayMaxKnifeThickness =  Boolean.parseBoolean(arrayMaxKnifeThickness);
    }
    public void setArrayMaxLengthKnifePS(String arrayMaxLengthKnifePS) {
        String[] arrStr = arrayMaxLengthKnifePS.split(" "); //создаём массив String
        double[] arrDouble = new double[arrStr.length];// Создаем массив чисел с размером, как у массива строк
        for (int i = 0; i < arrStr.length; i++) { // Проходимся в цикле по исходному массиву. Сохраняем приведенные элементы в новый массив
            arrDouble[i] = Double.parseDouble(arrStr[i]);
        }
        this.arrayMaxLengthKnifePS = arrDouble;
    }
    public void setArrayMaxLengthKnifePET(String arrayMaxLengthKnifePET) {
        String[] arrStr = arrayMaxLengthKnifePET.split(" ");
        double[] arrDouble = new double[arrStr.length];
        for (int i = 0; i < arrStr.length; i++) {
            arrDouble[i] = Double.parseDouble(arrStr[i]);
        }
        this.arrayMaxLengthKnifePET = arrDouble;
    }
    public void setArrayMaxLengthKnifePVC(String arrayMaxLengthKnifePVC) {
        String[] arrStr = arrayMaxLengthKnifePVC.split(" ");
        double[] arrDouble = new double[arrStr.length];
        for (int i = 0; i < arrStr.length; i++) {
            arrDouble[i] = Double.parseDouble(arrStr[i]);
        }
        this.arrayMaxLengthKnifePVC = arrDouble;
    }
    public void setArrayMaxLengthKnifePP(String arrayMaxLengthKnifePP) {
        String[] arrStr = arrayMaxLengthKnifePP.split(" ");
        double[] arrDouble = new double[arrStr.length];
        for (int i = 0; i < arrStr.length; i++) {
            arrDouble[i] = Double.parseDouble(arrStr[i]);
        }
        this.arrayMaxLengthKnifePP = arrDouble;
    }
    public void setTypeMachineK(String typeMachineK) {
        this.typeMachineK =  Boolean.parseBoolean(typeMachineK);
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString(){
        return  String.format("%s [%s] %fx%f [%b %f %f %b %f %f] %f [%b PS - %s PET - %s PVC - %s PP - %s] " +
                        "[%f %b PS - %s PET - %s PVC - %s PP - %s] %b ",
                name, modelMachine, maxFormWidth, maxFormLength,
                machineBFS, betweenProductBFS, forEdgeFormBFS, machinePunching, betweenProductPunching, forEdgeFormPunching,
                forChain, usedPP, maxLengthKnifePS, maxLengthKnifePET, maxLengthKnifePVC, maxLengthKnifePP,
                coefficientKnifeBFS, arrayMaxKnifeThickness, Arrays.toString(arrayMaxLengthKnifePS),
                Arrays.toString(arrayMaxLengthKnifePET), Arrays.toString(arrayMaxLengthKnifePVC),
                Arrays.toString(arrayMaxLengthKnifePP), typeMachineK);
    }
}
