/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.Domain;

/**
 *
 * @author munna
 */
class ActionSet {

    Domain domain;
    StateSpace stateSpace;
    
    static double minCalories = 500;
    static double maxCalories = 5000;
    
    double nutritionIntensity[][] = {   {3500, maxCalories},
                                        {2000, 3500},
                                        {minCalories,2000}
                                    };
    static double minPA = 1.0;
    static double maxPA = 3.0;
    
    double exerciseIntensity[][] = {    {minPA , 1.8 },
                                        {1.8 , 2.3 },
                                        {2.3, maxPA}
                                    };
    
    ActionSet(Domain domain,StateSpace stateSpace) {
        this.domain = domain;
        this.stateSpace = stateSpace;
    }
    
    public void createActionSet(double nutritionCalorieLimits[], double palLimits[])
    {
        for(double calories = nutritionCalorieLimits[0] ; calories < nutritionCalorieLimits[1] ; calories += 250)
        {
            for(double pa = palLimits[0] ; pa < palLimits[1] ; pa += 0.1 )
            {
                new WellnessAction(calories+"-"+pa,calories, pa,this.domain,this.stateSpace);
            }
        }
//        new WellnessAction(minCalories+"-"+minPA,minCalories,minPA,this.domain);
//        new WellnessAction(minCalories+"-"+maxPA,minCalories,maxPA,this.domain);
//        
    }

    void createActionSet(int nutritionIntensityLevel, int exerciseIntensityLevel) {
        double nutritionLimits[] = new double[2];
        double exPALimits[] = new double[2];
        
        nutritionLimits[0] = nutritionIntensity[nutritionIntensityLevel][0];
        nutritionLimits[1] = nutritionIntensity[nutritionIntensityLevel][1];
        
        exPALimits[0] = exerciseIntensity[exerciseIntensityLevel][0];
        exPALimits[1] = exerciseIntensity[exerciseIntensityLevel][1];
        
        createActionSet(nutritionLimits, exPALimits);
    }
}
