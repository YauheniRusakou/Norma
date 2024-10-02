package by.rusakou.norma.draw;

import android.graphics.Path;

public abstract class DrawCadre extends Path{
    /**
     * Нельзя создавать объекты этого класса
     */
    private DrawCadre() {}

    /**
     * Класс для рисования контура кадра с координатами float
     */
    public static class Path2 extends Path {
        //
        static float beforeCurveY = 35; // Отступ от формы до кривой
        static float beforeCurveYBezier = 30; // Отклонение кривизны по оси Y для Кривой Безье

        /**
         * Конструктор для рисования контура кадра с координатами float
         *
         * @param x - начальная координата рисования формы по х
         * @param y - начальная координата рисования формы по у
         * @param width - ширина формы
         * @param length - длина формы (по шагу)
         * @param forChain - растояние на цепи
         */
        public Path2(float x, float y, float width, float length, float forChain, float forStep) {

            reset();
            moveTo((x - forChain/2), (y - forStep));
            lineTo((x - forChain/2), (y - forStep - beforeCurveY));
            quadToLine((x - forChain/2), (y - forStep - beforeCurveY), (x + width + forChain/2));
            lineTo((x + width + forChain/2), (y + beforeCurveY + length));
            quadToLine((x - forChain/2), (y + length + beforeCurveY), (x + width + forChain/2));
            moveTo((x - forChain/2), (y + length + beforeCurveY));
            lineTo((x - forChain/2), (y - forStep));
            dottedLine((x - forChain/2), (y  - forStep),  (x + width + forChain/2));
            dottedLine((x - forChain/2), (y + length), x);
        }

        /**
         * Вспомогательный метод, предназначенный для рисования пунктирной линии.
         *
         * @param x1 - начало пунктирной линии по х
         * @param y1 - начало пунктирной линии по у
         * @param line - конец пунктирной линии по х
         */
        public void dottedLine(float x1, float y1, float line) { // Рисуем пунктирную линию

            float stroke = 10; // штрих
            float dotted = 5; // пунктир
            moveTo(x1, y1);
            while ((line - x1 - stroke - dotted) > 0) {
                lineTo((x1 + stroke), y1);
                x1 = x1 + stroke;
                moveTo((x1 + dotted), y1);
                x1 = x1 + dotted;
            } lineTo(line, y1);
        }

        /**
         * Вспомогательный метод, предназначенный для рисования линии разрыва (кривой).
         *
         * @param x1 - начало линии разрыва по х
         * @param y1 - начало линии разрыва по у
         * @param line - конец линии разрыва по х
         */
        public void quadToLine(float x1, float y1, float line) { // Рисуем линию разрыва

            float xHalf  = (x1 + line)/2; //Координаты посередине между началом х1 и концом линии line

            moveTo(x1, y1);
            quadTo(((x1  + xHalf)/2), (y1 - beforeCurveYBezier), xHalf, y1); //Соединяет текущую точку с конечной (х2, y2), с помощью квадратичной кривой Безье с контрольной точкой (х1, у1)
            quadTo((line - (line - xHalf)/2), (y1 + beforeCurveYBezier), line, y1);
          }

        /**
         * Возвращает значение отступа от формы до кривой
         *
         * @return отступ от формы до кривой
         */
        public static float getBeforeCurveY() {
            return beforeCurveY;
        }
    }
}

