import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {
    public static boolean applySmoothingAll;
    public static List<Observation> trainDataset=new ArrayList<>();
    public static Map<String, Map<String, Map<String, Integer>>> attributeCounts=new HashMap<>();
    public static int yes_count;
    public static int no_count;

    public static Map<String, Map<String, Double>> yes_probabilities_attribute = new HashMap<>();//We are trying to store the probabilities of each attribute-value pair given the label ("yes" or "no").
    public static Map<String, Map<String, Double>> no_probabilites_attribute = new HashMap<>();//here attribute is the outermost key

    public NaiveBayesClassifier(List<Observation> trainDataset, boolean applySmoothingAll) {
        this.applySmoothingAll = applySmoothingAll;
        this.trainDataset = trainDataset;
        train(); // fill the counts when object is created
    }
    public static void train() {
        //A priori
        for (Observation observation : trainDataset) {
            if (observation.label.equals("yes")) {
                yes_count += 1;
            } else if (observation.label.equals("no")) {
                no_count += 1;
            }
            //build attribute counts array (IDK!)
            for (Map.Entry<String, String> entry : observation.attributes.entrySet()) {
                String attribute = entry.getKey();
                String value = entry.getValue();

                attributeCounts.putIfAbsent(attribute, new HashMap<>());
                Map<String, Map<String, Integer>> valueMap = attributeCounts.get(attribute);
                valueMap.putIfAbsent(value, new HashMap<>());
                Map<String, Integer> labelMap = valueMap.get(value);
                labelMap.put(observation.label, labelMap.getOrDefault(observation.label, 0) + 1);
            }
        }
        //A posteriori
        for (String attribute : attributeCounts.keySet()) {
            Map<String, Map<String, Integer>> valueMap = attributeCounts.get(attribute);
            Map<String, Double> yes_probabilities_values = new HashMap<>();//here the attribute value is the key, this is yhe inner we will put in the main map
            Map<String, Double> no_probabilites_values = new HashMap<>();
            for (String value : valueMap.keySet()) {
                Map<String, Integer> labelMap = valueMap.get(value);
                for (String label : labelMap.keySet()) {
                    int count = labelMap.get(label);
                    double probability;
                    if (label.equals("yes")) {
                        if(applySmoothingAll||count==0) {
                            probability=simpleSmoothing((double)count,yes_count,2);
                        }
                        else{
                            probability = (double) count / yes_count;
                        }
                        yes_probabilities_values.put(value, probability);
                    } else {
                        if(applySmoothingAll||count==0) {
                            probability=simpleSmoothing((double)count,no_count,2);
                        }else{
                            probability = (double) count / no_count;
                        }
                        no_probabilites_values.put(value, probability);
                    }
                }
            }
            yes_probabilities_attribute.put(attribute, yes_probabilities_values);
            no_probabilites_attribute.put(attribute, no_probabilites_values);
        }
    }
    public String predict(Map<String, String> attributes) {
        double probability_yes=1;
        double probability_no =1;

        for(String attribute : attributes.keySet()) {
            Map<String, Double> probabilities_for_attribute=yes_probabilities_attribute.get(attribute);
            if(probabilities_for_attribute.containsKey(attribute)) {
                double probability_yes_for_attribute=probabilities_for_attribute.get(attributes.get(attribute));
                probability_yes*=probability_yes_for_attribute;
            }else{
                probability_yes *= simpleSmoothing(0, no_count, 2);//"Gdyby nie było w ogóle w train secie np. rainy, a w test secie byłby, to wtedy w predict byłoby null i potrzebujemy smoothingu.
                //A jeśli było rainy tylko przy jednym labelu (no), to dla drugiego labelu (yes) prawdopodobieństwo normalnie wysmoothujemy."
            }
        }
        for(String attribute : attributes.keySet()) {
            Map<String, Double> probabilities_for_attribute=no_probabilites_attribute.get(attribute);
            if(probabilities_for_attribute.containsKey(attribute)) {
                double probability_no_for_attribute=probabilities_for_attribute.get(attributes.get(attribute));
                probability_no*=probability_no_for_attribute;
            } else{
                probability_no *= simpleSmoothing(0, no_count, 2);

            }
        }
        probability_yes *= ((double) yes_count / (yes_count + no_count));
        probability_no *= ((double) no_count / (yes_count + no_count));

        if(probability_yes>probability_no) {
            return "yes";
        } else if (probability_no>probability_yes) {
            return "no";
        }else{
            return "probabilites are equal idk what now";
        }
    }

    public static double simpleSmoothing(double numerator, int denominator, int classes) {
        return (double) (numerator + 1) / (denominator + classes);
    }
}

