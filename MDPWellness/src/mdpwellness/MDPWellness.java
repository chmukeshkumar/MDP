/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.QValue;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.statehashing.DiscretizingStateHashFactory;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;

/**
 *
 * @author munna
 */

public class MDPWellness {
    
    private boolean divideActionSet = false;
    
    MDPWellness()
    {
        if(divideActionSet) {
            
            for(int i=0;i<IntensityLevel.values().length;i++) {
                for(int j=0;j<IntensityLevel.values().length;j++) {
                    WellnessDomain wellnessDomain = new WellnessDomain();
                    Domain domain = wellnessDomain.generateDomain();
                    StateSpace stateSpace = new StateSpace();
                    stateSpace.generateStateSpace((int)BodyParams.targetWeight-10,(int) BodyParams.initialWeight+10, domain);
                    ActionSet      actionSet = new ActionSet(domain,stateSpace);
                    actionSet.createActionSet(i,j);
                    run(domain,i,j);
                }
            }
        }
        else
        {
            WellnessDomain wellnessDomain = new WellnessDomain();
            Domain domain = wellnessDomain.generateDomain();
            StateSpace stateSpace = new StateSpace();
            stateSpace.generateStateSpace((int)BodyParams.targetWeight,(int) BodyParams.initialWeight, domain);
            ActionSet      actionSet = new ActionSet(domain,stateSpace);
            actionSet.createActionSet(new double[]{ActionSet.minCalories, ActionSet.maxCalories},
                                             new double[]{ActionSet.minPA, ActionSet.maxPA});
            run(domain,-1,-1);
        }
//        System.out.println("No. of actions " + domain.getActions().size());
       
    }
    
    
    private void run(Domain domain,int nutritionIntensityLevel, int exerciseIntensityLevel) {
        
         WellnessRewardFunction rf = new WellnessRewardFunction();
        WellnessTerminalFunction tf = new WellnessTerminalFunction();
        DiscretizingStateHashFactory hashingFactory = new DiscretizingStateHashFactory(1.0);
//        
        State initialState = getInitialState(domain);
//        
        ValueIteration vi = new ValueIteration(domain,rf,tf,0.1,hashingFactory,0.001,100);
        vi.planFromState(initialState);
        
//        PolicyIteration pi = new PolicyIteration(domain, rf, tf, 0.1, hashingFactory, 0.1, 100, 100);
//        pi.planFromState(initialState);
        
        HashMap<Integer,String> valueIterationWellnessPolicy = new HashMap();
//        HashMap<Integer,String> policyIterationWellnessPolicy = new HashMap();
        
//        PolicyVisualizer viz = new PolicyVisualizer("Wellness Policy");
//        viz.addData(wellnessPolicy);
        
        List<State> allStates = vi.getAllStates();
        GreedyQPolicy valueIterationPolicy = new GreedyQPolicy(vi);
//        GreedyQPolicy policyIterationPolicy = new GreedyQPolicy(pi);
        
        for(State s : allStates)
        {   
            AbstractGroundedAction valueIterationAction = valueIterationPolicy.getAction(s);
//            AbstractGroundedAction policyIterationAction = policyIterationPolicy.getAction(s);
            
            String valueIterationActionName = valueIterationAction.actionName();
//            String policyIterationActionName = policyIterationAction.actionName();
            
//            System.out.println("For State " + weight + " Greedy Policy Action name " + actionName);
            int weight = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
            valueIterationWellnessPolicy.put(weight,valueIterationActionName);
//            policyIterationWellnessPolicy.put(weight, policyIterationActionName);
            
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////            System.out.println(" Weight " + weight + " ACtion " + actionName);
//            for(QValue q : vi.getQs(s))
//            {
//                State state = q.s;
//                int stateWeight = state.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
//                System.out.println( stateWeight + " " + q.a.actionName() + "-" + q.q);
//            }
//           System.out.println("###############################################");
        }
        
        String policyName = "Nut " + nutritionIntensityLevel + " Ex: " + exerciseIntensityLevel;
//        PolicyVisualizer valueIterationPolicyVisualizer = new PolicyVisualizer(policyName);
//        valueIterationPolicyVisualizer.addData(valueIterationWellnessPolicy);
        printPolicy(policyName, valueIterationWellnessPolicy);
//        PolicyVisualizer policyIterationPolicyVisualizer = new PolicyVisualizer("Policy Iteration Wellness Policy");
//        policyIterationPolicyVisualizer.addData(policyIterationWellnessPolicy);
//        
//        WellnessPolicyImplementer policyImplementer  = new WellnessPolicyImplementer();
//        policyImplementer.implementPolicy(valueIterationWellnessPolicy);
    }
    
    private void printPolicy(String name, Map<Integer,String> policy) {
        System.out.println("======"+name+"======");
        for(int weight = (int)BodyParams.initialWeight; weight>=BodyParams.targetWeight;weight--) {
            String actionName = policy.get(weight);
            if(actionName == null) {
                continue;
            }
            double nutritionCalories = Double.valueOf(actionName.split("-")[0]);
            double pal = Double.valueOf(actionName.split("-")[1]);
            
            int nutritionIndex = ((int)nutritionCalories - (int)ActionSet.minCalories ) / 250;
            int palIndex       = (int) ((pal - ActionSet.minPA) / 0.1);
            
            System.out.println(weight+ " " +"n"+nutritionIndex+",l"+palIndex);
        }
    }
    
    
    private State getInitialState(Domain domain)
    {
        State s = new State();
        ObjectInstance subject = new ObjectInstance(domain.getObjectClass(SUBJECT),SUBJECT);
        subject.setValue(WEIGHT, (int) BodyParams.initialWeight);
        s.addObject(subject);
        
        return s;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MDPWellness();
    }
}
