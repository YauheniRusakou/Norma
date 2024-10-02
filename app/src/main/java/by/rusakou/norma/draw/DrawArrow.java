package by.rusakou.norma.draw;

import android.graphics.Path;

public abstract class DrawArrow extends Path {

        /**
         * Нельзя создавать объекты этого класса
         */
        private DrawArrow() {}

        /**
         * Класс для рисования размерной стрелки с координатами float
         */
        public static class Path2 extends Path {
            private float x1;
            private float y1;
            private float x2;
            private float y2;
            static final float arrowLength = 20; //Длина стрелки
            static float arrowHalfWidth = 4; // Половина ширины стрелки
            static float arrowSlopeWidth = 4; // Наклон хвостов стрелки
            static float distanceAfter = 6; // Растояние выносной линии за конец стрелки
            static float indentText = 4; // отступ текста от размерной линии
            float[] centerText = new float[2]; //координата центра текста размера

            /**
             * Конструктор для рисования размерной стрелки с координатами float
             *
             * @param x1 - начало выносной линии по x
             * @param y1 - начало выносной линии по y
             * @param x2 - конец выносной линии по x
             * @param y2 - конец выносной линии по y
             * @param distanceTo - растояние между эскизом и размерной линией
             */
            public Path2(float x1, float y1, float x2, float y2, float distanceTo) {

                reset();
                if ( y1 == y2) { //горизонтальный размер

                    lineToDistanceVert(x1, y1, x2, y2, distanceTo);
                    y1 = this.y1;
                    y2 = this.y2;

                    moveTo(x1, y1);
                    lineTo((x1 + arrowLength), (y1 + arrowHalfWidth));
                    lineTo((x1 + arrowLength - arrowSlopeWidth), y1);
                    lineTo((x1 + arrowLength), (y1 - arrowHalfWidth));
                    close(); // Закрывает текущий подпуть, рисуя прямую линию обратно к координатам последнего moveTo.

                    lineTo(x2, y2);
                    lineTo((x2 - arrowLength), (y2 + arrowHalfWidth));
                    lineTo((x2 - arrowLength + arrowSlopeWidth), y2);
                    lineTo((x2 - arrowLength), (y2 - arrowHalfWidth));
                    lineTo(x2, y2);

                    centerText[0] = (x2 -x1)/2;
                    centerText[1] = distanceTo - indentText;
                    }

                if ( x1 == x2) { //вертикальный размер

                    lineToDistanceHoriz(x1, y1, x2, y2, distanceTo);
                    x1 = this.x1;
                    x2 = this.x2;

                    moveTo(x1, y1);
                    lineTo((x1 + arrowHalfWidth), (y1 + arrowLength));
                    lineTo(x1, (y1 + arrowLength - arrowSlopeWidth));
                    lineTo((x1 - arrowHalfWidth), (y1 + arrowLength));
                    close();

                    lineTo(x2, y2);
                    lineTo((x2 + arrowHalfWidth), (y2 - arrowLength));
                    lineTo(x2, (y2 - arrowLength + arrowSlopeWidth));
                    lineTo((x2 - arrowHalfWidth), (y2 - arrowLength));
                    lineTo(x2, y2);

                    centerText[0] = distanceTo - indentText;
                    centerText[1] = (y2 - y1)/2;
                }
            }

            /**
             * Конструктор для создания вспомогательного пути Path, чтобы рисовать вертикальный тект на линиях вертикального размера
             *
             * @param xCenter - начало текста вертикального размера по х
             * @param yCenter - начало текста вертикального размера по у
             */
            public Path2(float xCenter, float yCenter) {

                reset();
                moveTo(xCenter, (yCenter + 100));
                lineTo(xCenter, (yCenter - 100));
            }

            /**
             * Вспомогательный метод, предназначенный для рисования вертикальной выносной линии.
             * В методе рисуются выносные линии и устанавливается новые коодинаты x1,y1 и x2,y2 для рисования размерной линии со стрелкой.
             *
             * @param startX1 - начало выносной линии по x
             * @param startY1 - начало выносной линии по y
             * @param startX2 - конец выносной линии по x
             * @param startY2 - конец выносной линии по y
             * @param distanceTo - растояние между эскизом и размерной линией
             */
            public void lineToDistanceVert(float startX1, float startY1, float startX2, float startY2, float distanceTo) {

                moveTo(startX1, startY1);
                lineTo(startX1, (startY1 + distanceTo + distanceAfter));
                this.y1 = startY1 + distanceTo; //Переводим точку y1 на растояние выносной линии, чтобы дальше рисовать стрелочку

                moveTo(startX2, startY2);
                lineTo(startX2, (startY2 + distanceTo + distanceAfter));
                this.y2 = startY2 + distanceTo; //Переводим точку y2 на растояние выносной линии, чтобы дальше рисовать стрелочку
            }

            /**
             * Вспомогательный метод, предназначенный для рисования горизонтальной выносной линии.
             * В методе рисуются выносные линии и устанавливается новые коодинаты x1,y1 и x2,y2 для рисования размерной линии со стрелкой.
             *
             * @param startX1 - начало выносной линии по x
             * @param startY1 - начало выносной линии по y
             * @param startX2 - конец выносной линии по x
             * @param startY2 - конец выносной линии по y
             * @param distanceTo - растояние между эскизом и размерной линией
             */
            public void lineToDistanceHoriz(float startX1, float startY1, float startX2, float startY2, float distanceTo) {

                moveTo(startX1, startY1);
                lineTo((startX1 + distanceTo + distanceAfter), startY1);
                this.x1 = startX1 + distanceTo; //Переводим точку y1 на растояние выносной линии, чтобы дальше рисовать стрелочку

                moveTo(startX2, startY2);
                lineTo((startX2 + distanceTo + distanceAfter), startY2);
                this.x2 = startX2 + distanceTo; //Переводим точку y2 на растояние выносной линии, чтобы дальше рисовать стрелочку
            }

            /**
             * Метод возвращает координату центра текста размера
             *
             * @return - массив float координат центра текста размера
             */
            public float[] getCenterText() {
                return centerText;
            }

        }
    }

