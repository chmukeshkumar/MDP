/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.YIntervalSeries;

/**
 *
 * @author munna
 */
public class WellnessPolicyImplementer {
    
    Random r = new Random();
    
    BodyWeightVisualizer viz = new BodyWeightVisualizer("Body Weight Trajectories");
    
    WellnessPolicyImplementer()
    {
        
    }
    
    private double addUncertainity(double weight)
    {
        double rand = r.nextDouble();
        double sum = 0;
        int i =0;
        for(;i<WellnessAction.uncertainty.length;i++)
        {
            sum = sum + WellnessAction.uncertainty[i];
            if(rand < sum)
            {
                break;
            }
        }

        if(i == 1)
        {
            return weight - 2;
        }
        else if( i == 2)
        {
            return weight + 2;
        }
        
        return weight;
    }
    
    public void implementPolicy(HashMap<Integer,String> policy)
    {
        int totalTrails = 50;
        int totalTime = 300;
        double data[][] = new double[totalTime/WellnessAction.timeStep + 2][totalTrails+1];
        
        for(int trial =1;trial<=totalTrails;trial++)
        {
            System.out.println("-------------------Implementing Policy--------------- ");
            double weight = BodyParams.initialWeight;
            int time = 0;

            data[0][trial] = weight;
            data[0][0] = 0;
            int count = 1;
            do 
            {
                String action = policy.get((int)weight);
                System.out.println(""+(int)weight + " " + action);
                if(action == null)
                {
                    continue;
                }
                double actionCalories = Double.valueOf(action.split("-")[0]);
                double actionPA = Double.valueOf(action.split("-")[1]);       

                KevinHallModel khm = new KevinHallModel(weight,
                                                        BodyParams.height,
                                                        BodyParams.age,
                                                        BodyParams.gender,
                                                        BodyParams.initialPA);

                ArrayList<Double> setCalories = new ArrayList();
                ArrayList<Double> setPA       = new ArrayList();

                int timeStep = WellnessAction.timeStep ;

                for(int i=0;i<timeStep;i++)
                {
                    setCalories.add(actionCalories);
                    setPA.add(actionPA);
                }
                khm.setCurrentParameters(setCalories, setPA);
                double[] y = new double[]{0.5,khm.getInitialECF(),khm.getInitialFatMass(),khm.getInitialLeanMass(),0};
                FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1);
                integrator.integrate(khm, 0, y, (timeStep-1), y);
                weight = addUncertainity(y[0]+y[1]+y[2]+y[3]);
                time = time + timeStep;
//                System.out.println("time " + time + " weight " + weight);
                
                data[count][0] = time;
                data[count][trial] = weight;
                count++;
//            }while (weight > BodyParams.targetWeight );
            } while(time <= totalTime );
            
        }
        
//        System.out.println("Time Weight Error+ Error-");
        YIntervalSeries intervalSeries = new YIntervalSeries("Weight Loss Trajectory");
        for(int tt = 0;tt<data.length;tt++)
        {
            double time = data[tt][0];
            double sum = data[tt][1];
            double min = data[tt][1];
            double max = data[tt][1];
            for(int i=2;i<=totalTrails;i++)
            {
                sum = sum+data[tt][i];
                max = Double.max(data[tt][i], max);
                min = Double.min(data[tt][i],min);
            }
            double avg = sum/totalTrails;
            
            System.out.println(""+data[tt][0] +" " + avg + " " + (max-avg) + " " + (avg-min));
            intervalSeries.add(time, avg, min, max);
        }
        viz.addSeries(intervalSeries);
    }
}
