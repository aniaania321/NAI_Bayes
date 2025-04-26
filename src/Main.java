import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 1. Define the observations (manual ones for now)
        List<Observation> observations = new ArrayList<>(List.of(
                new Observation(Map.of("outlook", "sunny", "temperature", "hot", "humidity", "high", "windy", "false"), "no"),
                new Observation(Map.of("outlook", "sunny", "temperature", "hot", "humidity", "high", "windy", "true"), "no"),
                new Observation(Map.of("outlook", "overcast", "temperature", "hot", "humidity", "high", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "rainy", "temperature", "mild", "humidity", "high", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "rainy", "temperature", "cool", "humidity", "normal", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "rainy", "temperature", "cool", "humidity", "normal", "windy", "true"), "no"),
                new Observation(Map.of("outlook", "overcast", "temperature", "cool", "humidity", "normal", "windy", "true"), "yes"),
                new Observation(Map.of("outlook", "sunny", "temperature", "mild", "humidity", "high", "windy", "false"), "no"),
                new Observation(Map.of("outlook", "sunny", "temperature", "cool", "humidity", "normal", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "rainy", "temperature", "mild", "humidity", "normal", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "sunny", "temperature", "mild", "humidity", "normal", "windy", "true"), "yes"),
                new Observation(Map.of("outlook", "overcast", "temperature", "mild", "humidity", "high", "windy", "true"), "yes"),
                new Observation(Map.of("outlook", "overcast", "temperature", "hot", "humidity", "normal", "windy", "false"), "yes"),
                new Observation(Map.of("outlook", "rainy", "temperature", "mild", "humidity", "high", "windy", "true"), "no")
        ));

        // 2. Shuffle the list to get random selection
        Collections.shuffle(observations);

        // 3. Randomly select 2 samples as test
        List<Observation> testSet = new ArrayList<>(observations.subList(0, 2)); // First 2 as test

        // 4. The rest will be used for training
        List<Observation> trainingSet = new ArrayList<>(observations.subList(2, observations.size())); // All others as training

        // 5. Print the results to verify
        System.out.println("Training Set:");
        for (Observation obs : trainingSet) {
            System.out.println(obs);
        }

        System.out.println("\nTest Set:");
        for (Observation obs : testSet) {
            System.out.println(obs);
        }

        NaiveBayesClassifier classifier=new NaiveBayesClassifier(trainingSet,false);
        for(Observation obs: testSet) {
            System.out.println(classifier.predict(obs.attributes));

        }
        //Evaluation metrics

// 1. Your labels
        List<String> realClasses = new ArrayList<>();
        List<String> predictedClasses = new ArrayList<>();

        for (Observation obs : testSet) {
            realClasses.add(obs.label);
            String predicted = classifier.predict(obs.attributes);
            predictedClasses.add(predicted);
        }

// 2. Map classes to indices (example: "yes" -> 0, "no" -> 1)
        Map<String, Integer> labelToIndex = new HashMap<>();
        labelToIndex.put("yes", 0);
        labelToIndex.put("no", 1);

// 3. Build confusion matrix
        int nClasses = 2; // because "yes" and "no"
        int[][] confusionMatrix = new int[nClasses][nClasses];

        for (int i = 0; i < realClasses.size(); i++) {
            int realIndex = labelToIndex.get(realClasses.get(i));
            int predictedIndex = labelToIndex.get(predictedClasses.get(i));
            confusionMatrix[realIndex][predictedIndex]++;
        }

// 4. Measure metrics
        double accuracy = EvaluationMetrics.measureAccuracy(confusionMatrix);
        double[] recall = EvaluationMetrics.measureRecall(confusionMatrix);
        double[] precision = EvaluationMetrics.measurePrecision(confusionMatrix);
        double[] fMeasure = EvaluationMetrics.measureFMeasure(confusionMatrix);

// 5. Print results
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Recall: " + Arrays.toString(recall));
        System.out.println("Precision: " + Arrays.toString(precision));
        System.out.println("F-Measure: " + Arrays.toString(fMeasure));

    }
}
