/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rewardFunctionPlots;

/**
 *
 * @author munna
 */
public class safeWeightLossReward {
    
    public static void main(String[] args)
    {
        double initialWeight = 120;
        double targetWeight   = 80;
        System.out.println("PreviousWeight CurrentWeight Reward");
        for( double currentWeight = initialWeight; currentWeight >= targetWeight; currentWeight--)
        {
            for(double previousWeight = initialWeight; previousWeight >= targetWeight; previousWeight--)
            {
                double diff = previousWeight - currentWeight;
                double reward;        
                if(diff <= 0 )
                {
                     reward = 0;
                }
                else
                {
                    reward = 100 - Math.abs(diff - 2)*10;
                }
                System.out.println(previousWeight + " " + currentWeight + " " + reward);
            }
            
        }
    }
    
}
