public class EvaluationMetrics {

    public static double measureAccuracy(int[][] confusionMatrix) {
        int correct = 0;
        int total = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                total += confusionMatrix[i][j];
                if (i == j) {
                    correct += confusionMatrix[i][j];
                }
            }
        }
        return (double) correct / total;
    }

    public static double[] measureRecall(int[][] confusionMatrix) {
        int n = confusionMatrix.length;
        double[] recall = new double[n];

        for (int i = 0; i < n; i++) {
            int tp = confusionMatrix[i][i];
            int fn = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    fn += confusionMatrix[i][j];
                }
            }
            recall[i] = tp + fn == 0 ? 0.0 : (double) tp / (tp + fn);
        }
        return recall;
    }

    public static double[] measurePrecision(int[][] confusionMatrix) {
        int n = confusionMatrix.length;
        double[] precision = new double[n];

        for (int i = 0; i < n; i++) {
            int tp = confusionMatrix[i][i];
            int fp = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    fp += confusionMatrix[j][i];
                }
            }
            precision[i] = tp + fp == 0 ? 0.0 : (double) tp / (tp + fp);
        }
        return precision;
    }

    public static double[] measureFMeasure(int[][] confusionMatrix) {
        double[] recall = measureRecall(confusionMatrix);
        double[] precision = measurePrecision(confusionMatrix);
        int n = confusionMatrix.length;
        double[] fMeasure = new double[n];

        for (int i = 0; i < n; i++) {
            if (precision[i] + recall[i] == 0) {
                fMeasure[i] = 0.0;
            } else {
                fMeasure[i] = 2 * precision[i] * recall[i] / (precision[i] + recall[i]);
            }
        }
        return fMeasure;
    }
}
