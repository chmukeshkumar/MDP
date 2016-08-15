/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rewardFunctionPlots;

import mdpwellness.BodyParams;

/**
 *
 * @author munna
 */
public class LinearReward {
    
    public static void main(String[] args)
    {
        double initialWeight = 120;
        double targetWeight   = 80;
        System.out.println("TargetWeight InitialWeight CurrentWeight Reward MaxReward");
        for(double weight = initialWeight ; weight >= targetWeight; weight -- )
        {
            double reward = 10 + (90/(targetWeight - initialWeight))*(weight -initialWeight);
            System.out.println(targetWeight + " " + initialWeight + " " + weight + " " + reward + " 100" );
        }
    }
    
}
