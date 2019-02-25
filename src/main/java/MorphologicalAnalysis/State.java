package MorphologicalAnalysis;

public class State {

    private boolean startState;
    private boolean endState;
    private String name;
    private String pos;


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
     * Overridedn toString method which  returns the name.
     *
     * @return String name.
     */
    public String toString() {
        return name;
    }

}
