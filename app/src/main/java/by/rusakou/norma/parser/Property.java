package by.rusakou.norma.parser;

import androidx.annotation.NonNull;

/**
 * Класс для создания объекта Свойства, с необходимыми переменными.
 * Объект размещает в себе стартовые свойства.
 */

public class Property {
        private String startMaterial;
        private String startMachine;

        public String getStartMaterial() {   return startMaterial;    }
        public String getStartMachine() {  return startMachine;   }

        public void setStartMaterial(String startMaterial) {
            this.startMaterial = startMaterial;
        }

        public void setStartMachine(String startMachine) {
            this.startMachine = startMachine;
        }

        @NonNull
        public String toString(){
            return  startMaterial + " " + startMachine;
        }
    }