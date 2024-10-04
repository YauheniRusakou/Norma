package by.rusakou.norma;

import java.util.ArrayList;

/**
 * Класс для расчёта математических операций.
 */
public final class MathOp {

    /**
     * Нельзя создавать объекты этого класса
     */
    private MathOp() {}

    /**
     * Метод расчитывает увеличинный размер длины, ширины или радиуса лотка до усадки (по ножу)
     *
     * @param size - размер лотка, мм
     * @param shrinkage - усадка
     * @return - размер лотка до усадки (по ножу), мм
     */
    public static double runSizeKnife(double size, double shrinkage) {
        size = size / shrinkage; // Увеличиваем длину на усадку
        return size;
    }

    /**
     * Метод расчитывает площадь ножа до усадки изделия
     *
     * @param length - длина по ножу, мм
     * @param width - ширина по ножу, мм
     * @param radius - радиус по ножу, мм
     * @return - Площадь ножа до усадки изделия, см2
     */
    public static double runAreaKnife(double length, double width, double radius) {
        if ((radius <= (0.5 * length)) | (radius <= (0.5 * width))) {
            length = length / 10; // Переводим из мм в см
            width = width / 10; // Переводим из мм в см
            radius = radius / 10; // Переводим из мм в см
            return (length * width - Math.pow(radius, 2) * (4 - Math.PI)); // По формуле S = a*b - R^2(4 - PI)
        }
        return 0;
    }

    /**
     * Метод расчитывает площадь круглого ножа до усадки изделия
     *
     * @param diameter - диаметр по ножу, мм
     * @return - Площадь круглого ножа до усадки изделия, см2
     */
    public static double runAreaKnifeCircle(double diameter) {
        diameter  = diameter  / 10; // Переводим из мм в см
        return ((Math.PI  * Math.pow(diameter, 2)) / 4); // По формуле S = (PI*d^2) / 4
    }

    /**
     * Метод расчитывает длину одного ножа
     *
     * @param length - длина, мм
     * @param width - ширина, мм
     * @param radius - радиус, мм
     * @return - Длина ножей формы, мм
     */
    public static double runPerimeterOneKnife(double length, double width, double radius) {
        return ( 2 * Math.PI * radius + 2 * (length - 2 * radius) + 2 * (width - 2 * radius) ); // Расчёт периметра Р = 2PiR + 2(a-2R) + 2(b-2R)
    }

    /**
     * Метод расчитывает длину ножей формы
     *
     * @param length - длина, мм
     * @param width - ширина, мм
     * @param radius - радиус, мм
     * @param numProdFilm - количество мест формы
     * @return - Длина ножей формы, мм
     */
    public static double runPerimeterKnifeForm(double length, double width, double radius, double numProdFilm) {

        double perimeter = 2 * Math.PI * radius + 2 * (length - 2 * radius) + 2 * (width - 2 * radius); // Расчёт периметра Р = 2PiR + 2(a-2R) + 2(b-2R)
        perimeter *= numProdFilm; // Умножаем на колличество мест формы
        return perimeter;
    }

    /**
     * Метод расчитывает чистый вес изделия
     *
     * @param areaKnife - площадь ножа до усадки изделия, см2
     * @param thickness - толщина плёнки, мкм
     * @param density - плотность, г/см3
     * @return - чистый вес изделия, г
     */
    public static double runMass(double areaKnife, double thickness, double density) {
        thickness = thickness / 10000; // 10000 - перевод из мкм в см
        return (areaKnife * thickness * density);
    }

    /**
     * Метод расчитывает допуск массы
     *
     * @param mass - масса лотка, г
     * @param thickness - толщина плёнки, мкм
     * @return - допуск массы, г
     */
    public static double runMassLimit(double mass, double thickness) {
        if (thickness <= 300) { //если толщина лотка меньше 300 мкм, то допуск массы 5%
            return (mass * 0.05);
        }
        else //если толщина лотка больше 300 мкм, то допуск массы 10%
            return (mass * 0.1);
    }

    /**
     * Метод расчитывает параметры формы
     *
     * @param sizeOne        - размер лотка, расположеный поперёк направления плёнки, мм
     * @param sizeTwo        - размер лотка, расположеный вдоль направления плёнки, мм
     * @param radius 		 - радиус, мм
     * @param maxFormWidth   - максимальная ширина формы (поперёк направления плёнки), мм
     * @param maxFormLength  - максимальная длина формы (вдоль направления плёнки), мм
     * @param betweenProduct - растояние между лотками, мм
     * @param forEdgeForm    - растояние от лотка до края формы, мм
     * @param forChain       - растояние для заправки плёнки в цепь, мм
     * @return ArrayList<Double> элементов, где аргумент:
     * 		   0й - ширина формы, мм,
     *         1й - ширина плёнки, мм,
     *         2й - кол-во лотков по ширине плёнки,
     *         3й - длина формы, мм,
     *         4й - шаг формы, мм,
     *         5й - кол-во лотков по шагу формы,
     *         6й - кол-во мест формы,
     *         7й - длина ножей формы, мм.
     */

    public static ArrayList<Double> runCalcForm(double sizeOne, double sizeTwo, double radius, double maxFormWidth,
                                                double maxFormLength, double betweenProduct, double forEdgeForm, double forChain) {

        double calcFormWidth = sizeOne + (2 * forEdgeForm); // forEdgeForm - двойное расстояние от края формы до лотка
        double n = 1;
        double calcFormLength = sizeTwo + (2 * forEdgeForm);
        double m = 1;

        while (calcFormWidth < (maxFormWidth - (sizeOne + betweenProduct))) { //// было calcFormWidth < (maxFormWidth - sizeOne)
            calcFormWidth = calcFormWidth + betweenProduct + sizeOne; // betweenProduct - расстояние между лотками
            n++;
        }

        while (calcFormLength < (maxFormLength - (sizeTwo + betweenProduct))) {
            calcFormLength = calcFormLength + betweenProduct + sizeTwo; // betweenProduct - расстояние между лотками
            m++;
        }

        double perimeterKnife = runPerimeterKnifeForm(sizeOne, sizeTwo, radius, (m * n));

        ArrayList<Double> ar = new ArrayList<>();
        ar.add(calcFormWidth);
        ar.add(calcFormWidth + forChain);
        ar.add(n);
        ar.add(calcFormLength);
        ar.add(calcFormLength + 10); // тут 10 - шаг формы
        ar.add(m);
        ar.add(n * m);
        ar.add(perimeterKnife);

        return ar;
    }

    /**
     * Метод расчитывает параметры формы, откоррестироваемой пользователем. При расчётной длине ножей большей,
     * чем максимальная длина ножей, пользователь может убрать ряд по ширине или шагу форму.
     *
     * @param sizeOne        - размер лотка, расположеный поперёк направления плёнки, мм
     * @param sizeTwo        - размер лотка, расположеный вдоль направления плёнки, мм
     * @param radius 		 - радиус, мм
     * @param betweenProduct - растояние между лотками, мм
     * @param forEdgeForm    - растояние от лотка до края формы, мм
     * @param forChain       - растояние для заправки плёнки в цепь, мм
     * @param userNumProdFormWidth - количесво рядов изделий по ширине флормы
     * @param userNumProdFormLenght - количесво рядов изделий по шагу формы
     *
     * @return ArrayList<Double> элементов, где аргумент:
     * 		   0й - ширина формы, мм,
     *         1й - ширина плёнки, мм,
     *         2й - кол-во лотков по ширине плёнки,
     *         3й - длина формы, мм,
     *         4й - шаг формы, мм,
     *         5й - кол-во лотков по шагу формы,
     *         6й - кол-во мест формы,
     *         7й - длина ножей формы, мм.
     */
    public static ArrayList<Double> runCalcFormUser(double sizeOne, double sizeTwo, double radius,
                                                    double betweenProduct, double forEdgeForm, double forChain,
                                                    double userNumProdFormWidth, double userNumProdFormLenght) {

        double calcFormWidth = sizeOne + (2 * forEdgeForm); // forEdgeForm - двойное расстояние от края формы до лотка
        double n = 1;
        double calcFormLength = sizeTwo + (2 * forEdgeForm);
        double m = 1;

        while (n < userNumProdFormWidth) {
            calcFormWidth = calcFormWidth + betweenProduct + sizeOne; // betweenProduct - расстояние между лотками
            n++;
        }
        while (m < userNumProdFormLenght) {
            calcFormLength = calcFormLength + betweenProduct + sizeTwo; // betweenProduct - расстояние между лотками
            m++;
        }

        double perimeterKnife = runPerimeterKnifeForm(sizeOne, sizeTwo, radius, (m * n));

        ArrayList<Double> arUser = new ArrayList<>();
        arUser.add(calcFormWidth);
        arUser.add(calcFormWidth + forChain);
        arUser.add(n);
        arUser.add(calcFormLength);
        arUser.add(calcFormLength + 10); // тут 10 - шаг формы
        arUser.add(m);
        arUser.add(n * m);
        arUser.add(perimeterKnife);

        return arUser;
    }

        /**
         * Метод расчитывает округлённый кадр.
         * Ширина плёнки округляется кратно 5 в большую сторону.
         * Шаг формы округляется до целого в большую сторону.
         *
         * @param calcFilmWidth - Расчётная ширина плёнки, мм
         * @param calcFilmStep - Шаг формы (длина формы + 10 мм), мм
         * @return ArrayList<Double> элементов, где аргумент:
         *		   0й - округлённая ширина плёнки, мм,
         *         1й - округлённый шаг формы, мм.
         */
    public static ArrayList<Double> runRoundCadre(double calcFilmWidth, double calcFilmStep) {
        double roundFilmWidth = 5*(Math.ceil(calcFilmWidth/5)); // Округляем до 5 в большую сторону
        double roundFilmStep = Math.ceil(calcFilmStep); // // Округляем до целого в большую сторону

        ArrayList<Double> ar = new ArrayList<Double>();
        ar.add(roundFilmWidth);
        ar.add(roundFilmStep);
        return (ar);
    }

    /**
     * Метод расчитывает норму расхода
     *
     * @param filmWidth - ширина плёнки, мм
     * @param filmStep - шаг формы, мм
     * @param thickness - толщина плёнки, мкм
     * @param density - плотность, г/см3
     * @param coefficient - коэффициент
     * @param quanPlaces - кол-во мест формы
     * @return норму расхода, г
     */
    public static double runNorm(double filmWidth, double filmStep, double thickness, double density, double coefficient, double quanPlaces) {
        filmWidth = filmWidth / 10; // 10 - перевод из мм в см
        filmStep = filmStep / 10; // 10 - перевод из мм в см
        thickness = thickness / 10000; // 10000 - перевод из мкм в см
        return (filmWidth * filmStep * thickness * density * coefficient) / quanPlaces;
    }

    /**
     * Метод расчитывает отходность
     *
     * @param norm - норма, г
     * @param mass - масса, г
     * @return - отходность, %
     */
    public static double runScrapPercent(double norm, double mass) {
        return ((norm - mass)/norm) * 100;
    }

    /**
     * Метод возвращает максимальную длину ножей в зависимости от толщины плёнки
     *
     * @param thickness - толщина плёнки
     * @param arrayMaxLengthKnife - массив максимальных длин ножей, в зависимости от толщины пленки
     * @return - максимальная длина ножей для данной толщины плёнки
     */
    static double runMaxLengthKnifeThickness(double thickness, double[] arrayMaxLengthKnife){
        double maxLengthKnife = 0;
        double start = -50; // начало диапазона толщин
        double end = 50; // конец диапазона толщин
        for (int i = 0; i < arrayMaxLengthKnife.length; i++) {
            if (thickness >(start + i*100) & thickness <=(end + i*100))  maxLengthKnife = arrayMaxLengthKnife[i]; // например, при толщине плйнки от 450 до 550 мкм, назначаем maxLengthKnife=5
            if (thickness >(end + (arrayMaxLengthKnife.length-1)*100)) maxLengthKnife = arrayMaxLengthKnife[arrayMaxLengthKnife.length-1]; // последний элемен массива
        }
        return maxLengthKnife;
    }

    /**
     * Метод возвращает значение усадки из вида коэффициента в виде процента
     *
     * @param shrinkageCoefficient - усадка в виде коэффициента
     * @return - усадка в виде процента
     */
    public static double runShrinkagePercent(double shrinkageCoefficient) {
        double scale = 100; // округление до второго знака после запятой
        return Math.round((100 * (1 - shrinkageCoefficient)) * scale) / scale;
    }

    /**
     * Метод возвращает значение усадки из вида процента в виде коэффициента
     *
     * @param shrinkagePercent - усадка в виде процента
     * @return - усадка в виде коэффициента
     */
    public static double runShrinkageCoefficient(double shrinkagePercent) {
        double scale = 100000; // округление до пятого знака после запятой
        return Math.round((1 - shrinkagePercent/100) * scale) / scale;
    }
}