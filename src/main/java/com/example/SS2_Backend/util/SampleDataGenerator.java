package com.example.SS2_Backend.util;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.example.SS2_Backend.model.StableMatching.Matches;
import com.example.SS2_Backend.model.StableMatching.StableMatchingProblem;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import java.util.ArrayList;
import java.util.Random;

public class SampleDataGenerator {

    public static void main(String[] args) {
        ArrayList<Individual> individuals = generateSampleIndividuals(12);

        // Create a StableMatchingProblem object with the generated data
        StableMatchingProblem problem = new StableMatchingProblem(individuals, "compositeWeightFunction", "fitnessFunction");
        if(problem.isPreferenceEmpty()){
            System.out.println("Preference failed to generate");
        }else{
            System.out.println("success");
            System.out.println(problem.printPreferenceLists());
        }
        System.out.println(problem.getNumberOfIndividual()); //success
        System.out.println(problem.printPreferenceLists()); //failed
        System.out.println(problem.isPreferenceEmpty()); // true -- Preference initializing failed

        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(1000)
                .withProperty("populationSize", 100)
                .distributeOnAllCores()
                .run();
        //create Datastructure to fetch the result
        for (Solution solution : result) {
            System.out.print(-solution.getObjective(0) + "\t"); // Negate to show maximized objective
            System.out.println(solution.getVariable(0).toString());
            Matches matches = problem.stableMatching(solution.getVariable(0));
            System.out.println(matches);
        }
    }

    public static ArrayList<Individual> generateSampleIndividuals(int numIndividuals) {
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 1; i <= numIndividuals; i++) {
            String individualName = "Individual" + i;
            int individualSet;
            if(i <= numIndividuals/2){
                individualSet = 0;
            }else{
                individualSet = 1;
            }
            Individual individual = new Individual(individualName, individualSet);

            // Add some sample properties (you can customize this part)
            for (int j = 0; j < 5; j++) { // Adding 5 sample properties for each individual
                String propertyName = "Property" + j;
                int propertyValue = new Random().nextInt(20) + 1;
                int propertyWeight = new Random().nextInt(10) + 1; // Random weight between 1 and 10
                individual.setProperty(propertyName, propertyValue, propertyWeight);
            }

            individuals.add(individual);
        }

        return individuals;
    }
}
