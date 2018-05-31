package MorphologicalAnalysis;

import java.util.ArrayList;

public class State {

    private boolean startState;
    private boolean endState;
    private String name;
    private String pos;
    private ArrayList<Transition> transitions;


    public State(String name, boolean startState, boolean endState){
        this.name = name;
        this.startState = startState;
        this.endState = endState;
        this.pos = null;
        transitions = new ArrayList<Transition>();
    }

    public State(String name, boolean startState, boolean endState, String pos){
        this.name = name;
        this.startState = startState;
        this.endState = endState;
        this.pos = pos;
        transitions = new ArrayList<Transition>();
    }

    public void addTransition(State toState, String with, String withName){
        Transition newTransition = new Transition(toState, with, withName);
        transitions.add(newTransition);
    }

    public void addTransition(State toState, String with, String withName, String toPos){
        Transition newTransition = new Transition(toState, with, withName, toPos);
        transitions.add(newTransition);
    }

    public String getName(){
        return name;
    }

    public String getPos(){
        return pos;
    }

    public boolean isEndState(){
        return endState;
    }

    public int transitionCount(){
        return transitions.size();
    }

    public Transition getTransition(int index){
        return transitions.get(index);
    }

    public String toString(){
        return name;
    }

}
