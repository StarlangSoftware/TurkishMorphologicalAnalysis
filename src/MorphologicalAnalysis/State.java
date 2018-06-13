package MorphologicalAnalysis;

import java.util.ArrayList;

public class State {

    private boolean startState;
    private boolean endState;
    private String name;
    private String pos;
    private ArrayList<Transition> transitions;


    /**
     * First constructor of the {@link State} class which takes 3 parameters String name, boolean startState,
     * and boolean endState as input and initializes the private variables of the class also leaves pos as null.
     *
     * @param name       String input.
     * @param startState boolean input.
     * @param endState   boolean input.
     */
    public State(String name, boolean startState, boolean endState) {
        this.name = name;
        this.startState = startState;
        this.endState = endState;
        this.pos = null;
        transitions = new ArrayList<Transition>();
    }

    /**
     * Second constructor of the {@link State} class which takes 4 parameters as input; String name, boolean startState,
     * boolean endState, and String pos and initializes the private variables of the class.
     *
     * @param name       String input.
     * @param startState boolean input.
     * @param endState   boolean input.
     * @param pos        String input.
     */
    public State(String name, boolean startState, boolean endState, String pos) {
        this.name = name;
        this.startState = startState;
        this.endState = endState;
        this.pos = pos;
        transitions = new ArrayList<Transition>();
    }

    /**
     * The addTransition method creates a new {@link Transition} with given input parameters and adds the transition to
     * transitions {@link ArrayList}.
     *
     * @param toState  State type input indicating the next state.
     * @param with     String input indicating with what the transition will be made.
     * @param withName String input.
     */
    public void addTransition(State toState, String with, String withName) {
        Transition newTransition = new Transition(toState, with, withName);
        transitions.add(newTransition);
    }

    /**
     * Another addTransition method which takes additional argument; toPos and. It creates a new {@link Transition}
     * with given input parameters and adds the transition to transitions {@link ArrayList}.
     *
     * @param toState  State type input indicating the next state.
     * @param with     String input indicating with what the transition will be made.
     * @param withName String input.
     * @param toPos    String input.
     */
    public void addTransition(State toState, String with, String withName, String toPos) {
        Transition newTransition = new Transition(toState, with, withName, toPos);
        transitions.add(newTransition);
    }

    /**
     * Getter for the name.
     *
     * @return String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the pos.
     *
     * @return String pos.
     */
    public String getPos() {
        return pos;
    }

    /**
     * The isEndState method returns endState's value.
     *
     * @return boolean endState.
     */
    public boolean isEndState() {
        return endState;
    }

    /**
     * The transitionCount method returns the size of the transitions {@link ArrayList}.
     *
     * @return the size of the transitions {@link ArrayList}.
     */
    public int transitionCount() {
        return transitions.size();
    }

    /**
     * The getTransition method returns the transition at the given index.
     *
     * @param index int input.
     * @return transition at given index.
     */
    public Transition getTransition(int index) {
        return transitions.get(index);
    }

    /**
     * Overridedn toString method which  returns the name.
     *
     * @return String name.
     */
    public String toString() {
        return name;
    }

}
