package MorphologicalAnalysis;

import Util.FileUtils;
import Xml.XmlDocument;
import Xml.XmlElement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by olcay on 12/08/2018.
 */
public class FiniteStateMachine {
    private ArrayList<State> states;
    private HashMap<State, ArrayList<Transition>> transitions;

    /**
     * Constructor reads the finite state machine in the given input file. It has a NodeList which holds the states
     * of the nodes and there are 4 different type of nodes; stateNode, root Node, transitionNode and withNode.
     * Also there are two states; state that a node currently in and state that a node will be in.
     * <p>
     * DOMParser is used to parse the given file. Firstly it gets the document to parse, then gets its elements by the
     * tag names. For instance, it gets states by the tag name 'state' and puts them into an ArrayList called stateList.
     * Secondly, it traverses this stateList and gets each Node's attributes. There are three attributes; name, start,
     * and end which will be named as states. If a node is in a startState it is tagged as 'yes', otherwise 'no'.
     * Also, if a node is in a startState, additional attribute will be fetched; originalPos that represents its original
     * part of speech.
     * <p>
     * At the last step, by starting rootNode's first child, it gets all the transitionNodes and next states called toState,
     * then continue with the nextSiblings. Also, if there is no possible toState, it prints this case and the causative states.
     *
     * @param fileName the resource file to read the finite state machine. Only files in resources folder are supported.
     */
    public FiniteStateMachine(String fileName) {
        boolean startState, endState;
        XmlElement stateNode, rootNode, transitionNode, withNode;
        State state, toState;
        String stateName, withName, originalPos, rootToPos, toPos;
        XmlDocument doc;
        doc = new XmlDocument(FileUtils.getInputStream(fileName));
        doc.parse();
        transitions = new HashMap<>();
        rootNode = doc.getFirstChild();
        stateNode = rootNode.getFirstChild();
        states = new ArrayList<>();
        while (stateNode != null){
            stateName = stateNode.getAttributeValue("name");
            startState = stateNode.getAttributeValue("start").equalsIgnoreCase("yes");
            endState = stateNode.getAttributeValue("end").equalsIgnoreCase("yes");
            if (startState) {
                originalPos = stateNode.getAttributeValue("originalpos");
                states.add(new State(stateName, true, endState, originalPos));
            } else {
                states.add(new State(stateName, false, endState));
            }
            stateNode = stateNode.getNextSibling();
        }
        rootNode = doc.getFirstChild();
        stateNode = rootNode.getFirstChild();
        while (stateNode != null) {
            if (stateNode.hasAttributes()) {
                stateName = stateNode.getAttributeValue("name");
                state = getState(stateName);
                transitionNode = stateNode.getFirstChild();
                while (transitionNode != null) {
                    if (transitionNode.hasAttributes()) {
                        stateName = transitionNode.getAttributeValue("name");
                        if (!transitionNode.getAttributeValue("transitionname").isEmpty()) {
                            withName = transitionNode.getAttributeValue("transitionname");
                        } else {
                            withName = null;
                        }
                        if (!transitionNode.getAttributeValue("topos").isEmpty()) {
                            rootToPos = transitionNode.getAttributeValue("topos");
                        } else {
                            rootToPos = null;
                        }
                        toState = getState(stateName);
                        if (toState != null) {
                            withNode = transitionNode.getFirstChild();
                            while (withNode != null) {
                                if (withNode.hasAttributes()) {
                                    withName = withNode.getAttributeValue("name");
                                    if (!withNode.getAttributeValue("topos").isEmpty()) {
                                        toPos = withNode.getAttributeValue("topos");
                                    } else {
                                        toPos = null;
                                    }
                                } else {
                                    toPos = null;
                                }
                                if (toPos == null) {
                                    if (rootToPos == null) {
                                        addTransition(state, toState, withNode.getPcData(), withName);
                                    } else {
                                        addTransition(state, toState, withNode.getPcData(), withName, rootToPos);
                                    }
                                } else {
                                    addTransition(state, toState, withNode.getPcData(), withName, toPos);
                                }
                                withNode = withNode.getNextSibling();
                            }
                        } else {
                            System.out.println("From state " + state.getName() + " to state " + stateName + " does not exist");
                        }
                    }
                    transitionNode = transitionNode.getNextSibling();
                }
            }
            stateNode = stateNode.getNextSibling();
        }
    }

    /**
     * The isValidTransition loops through states ArrayList and checks transitions between states. If the actual transition
     * equals to the given transition input, method returns true otherwise returns false.
     *
     * @param transition is used to compare with the actual transition of a state.
     * @return true when the actual transition equals to the transition input, false otherwise.
     */
    public boolean isValidTransition(String transition) {
        for (State state : transitions.keySet()) {
            for (Transition transition1 : transitions.get(state)) {
                if (transition1.toString() != null && transition1.toString().equals(transition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * the getStates method returns the states in the FiniteStateMachine.
     * @return StateList.
     */
    public ArrayList<State> getStates(){
        return states;
    }

    /**
     * The getState method is used to loop through the states {@link ArrayList} and return the state whose name equal
     * to the given input name.
     *
     * @param name is used to compare with the state's actual name.
     * @return state if found any, null otherwise.
     */
    public State getState(String name) {
        for (State state : states) {
            if (state.getName().equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }

    /**
     * The addTransition method creates a new {@link Transition} with given input parameters and adds the transition to
     * transitions {@link ArrayList}.
     *
     * @param fromState  State type input indicating the from state.
     * @param toState  State type input indicating the next state.
     * @param with     String input indicating with what the transition will be made.
     * @param withName String input.
     */
    public void addTransition(State fromState, State toState, String with, String withName) {
        ArrayList<Transition> transitionList;
        Transition newTransition = new Transition(toState, with, withName);
        if (transitions.containsKey(fromState)){
            transitionList = transitions.get(fromState);
        } else {
            transitionList = new ArrayList<>();
        }
        transitionList.add(newTransition);
        transitions.put(fromState, transitionList);
    }

    /**
     * Another addTransition method which takes additional argument; toPos and. It creates a new {@link Transition}
     * with given input parameters and adds the transition to transitions {@link ArrayList}.
     *
     * @param fromState  State type input indicating the from state.
     * @param toState  State type input indicating the next state.
     * @param with     String input indicating with what the transition will be made.
     * @param withName String input.
     * @param toPos    String input.
     */
    public void addTransition(State fromState, State toState, String with, String withName, String toPos) {
        ArrayList<Transition> transitionList;
        Transition newTransition = new Transition(toState, with, withName, toPos);
        if (transitions.containsKey(fromState)){
            transitionList = transitions.get(fromState);
        } else {
            transitionList = new ArrayList<>();
        }
        transitionList.add(newTransition);
        transitions.put(fromState, transitionList);
    }

    /**
     * The getTransitions method returns the transitions at the given state.
     *
     * @param state State input.
     * @return transitions at given state.
     */
    public ArrayList<Transition> getTransitions(State state) {
        if (transitions.containsKey(state)){
            return transitions.get(state);
        } else {
            return new ArrayList<>();
        }
    }

}
