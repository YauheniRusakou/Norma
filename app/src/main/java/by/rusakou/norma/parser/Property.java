package by.rusakou.norma.parser;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

/**
 * Класс для создания объекта Свойства, с необходимыми переменными.
 * Объект размещает в себе стартовые свойства.
 */

public class Property {
        private String startMaterial;
        private String startMachine;
        private double countAdvertising;
        private double maxBetweenProduct;
        private double maxForEdgeForm;
        private double maxForChain;
        private double maxShrinkagePercentPS;
        private double maxShrinkagePercentPET;
        private double maxShrinkagePercentPVC;
        private double maxShrinkagePercentPP;

        public String getStartMaterial() { return startMaterial; }
        public String getStartMachine() { return startMachine; }
        public double getCountAdvertising() { return countAdvertising; }
        public double getMaxBetweenProduct() { return maxBetweenProduct; }
        public double getMaxForEdgeForm() { return maxForEdgeForm; }
        public double getMaxForChain() { return maxForChain; }
        public double getMaxShrinkagePercentPS() { return maxShrinkagePercentPS; }
        public double getMaxShrinkagePercentPET() { return maxShrinkagePercentPET; }
        public double getMaxShrinkagePercentPVC() { return maxShrinkagePercentPVC; }
        public double getMaxShrinkagePercentPP() { return maxShrinkagePercentPP; }
        public void setStartMaterial(String startMaterial) {
            this.startMaterial = startMaterial;
        }
        public void setStartMachine(String startMachine) {
            this.startMachine = startMachine;
        }

        public void setCountAdvertising(String countAdvertising) {
            this.countAdvertising =  Double.parseDouble(countAdvertising);
        }

        public void setMaxBetweenProduct(String maxBetweenProduct) {
            this.maxBetweenProduct =  Double.parseDouble(maxBetweenProduct);
        }

        public void setMaxForEdgeForm(String maxForEdgeForm) {
            this.maxForEdgeForm =  Double.parseDouble(maxForEdgeForm);
        }

        public void setMaxForChain(String maxForChain) {
            this.maxForChain =  Double.parseDouble(maxForChain);
        }

        public void setMaxShrinkagePercentPS(String maxShrinkagePercentPS) {
            this.maxShrinkagePercentPS =  Double.parseDouble(maxShrinkagePercentPS);
        }

        public void setMaxShrinkagePercentPET(String maxShrinkagePercentPET) {
            this.maxShrinkagePercentPET =  Double.parseDouble(maxShrinkagePercentPET);
        }

        public void setMaxShrinkagePercentPVC(String maxShrinkagePercentPVC) {
            this.maxShrinkagePercentPVC =  Double.parseDouble(maxShrinkagePercentPVC);
        }

        public void setMaxShrinkagePercentPP(String maxShrinkagePercentPP) {
            this.maxShrinkagePercentPP =  Double.parseDouble(maxShrinkagePercentPP);
        }

        @NonNull
        @SuppressLint("DefaultLocale")
        public String toString(){
            return  String.format("[%s %s] %f [%f %f %f] [%f %f %f %f]",
                startMaterial, startMachine, countAdvertising, maxBetweenProduct, maxForEdgeForm, maxForChain,
                maxShrinkagePercentPS, maxShrinkagePercentPET, maxShrinkagePercentPVC, maxShrinkagePercentPP);
            }
        }