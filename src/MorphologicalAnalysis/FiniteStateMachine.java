package MorphologicalAnalysis;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by olcay on 12/08/2018.
 */
public class FiniteStateMachine {
    private ArrayList<State> states;

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
     * @param fileName the file to read the finite state machine.
     */
    public FiniteStateMachine(String fileName) {
        int i;
        boolean startState, endState;
        NodeList stateList;
        Node stateNode, rootNode, transitionNode, withNode;
        State state, toState;
        String stateName, withName, originalPos, rootToPos, toPos;
        NamedNodeMap attributes;
        DOMParser parser = new DOMParser();
        Document doc;
        try {
            parser.parse(fileName);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        doc = parser.getDocument();
        stateList = doc.getElementsByTagName("state");
        states = new ArrayList<>();
        for (i = 0; i < stateList.getLength(); i++) {
            stateNode = stateList.item(i);
            attributes = stateNode.getAttributes();
            stateName = attributes.getNamedItem("name").getNodeValue();
            startState = attributes.getNamedItem("start").getNodeValue().equalsIgnoreCase("yes");
            endState = attributes.getNamedItem("end").getNodeValue().equalsIgnoreCase("yes");
            if (startState) {
                originalPos = attributes.getNamedItem("originalpos").getNodeValue();
                states.add(new State(stateName, true, endState, originalPos));
            } else {
                states.add(new State(stateName, false, endState));
            }
        }
        rootNode = doc.getFirstChild();
        stateNode = rootNode.getFirstChild();
        while (stateNode != null) {
            if (stateNode.hasAttributes()) {
                attributes = stateNode.getAttributes();
                stateName = attributes.getNamedItem("name").getNodeValue();
                state = getState(stateName);
                transitionNode = stateNode.getFirstChild();
                while (transitionNode != null) {
                    if (transitionNode.hasAttributes()) {
                        attributes = transitionNode.getAttributes();
                        stateName = attributes.getNamedItem("name").getNodeValue();
                        if (attributes.getNamedItem("transitionname") != null) {
                            withName = attributes.getNamedItem("transitionname").getNodeValue();
                        } else {
                            withName = null;
                        }
                        if (attributes.getNamedItem("topos") != null) {
                            rootToPos = attributes.getNamedItem("topos").getNodeValue();
                        } else {
                            rootToPos = null;
                        }
                        toState = getState(stateName);
                        if (toState != null) {
                            withNode = transitionNode.getFirstChild();
                            while (withNode != null) {
                                if (withNode.getFirstChild() != null) {
                                    if (withNode.hasAttributes()) {
                                        attributes = withNode.getAttributes();
                                        withName = attributes.getNamedItem("name").getNodeValue();
                                        if (attributes.getNamedItem("toPos") != null) {
                                            toPos = attributes.getNamedItem("topos").getNodeValue();
                                        } else {
                                            toPos = null;
                                        }
                                    } else {
                                        toPos = null;
                                    }
                                    if (toPos == null) {
                                        if (rootToPos == null) {
                                            state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName);
                                        } else {
                                            state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName, rootToPos);
                                        }
                                    } else {
                                        state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName, toPos);
                                    }
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
        for (State state : states) {
            for (int i = 0; i < state.transitionCount(); i++) {
                if (state.getTransition(i).toString() != null && state.getTransition(i).toString().equals(transition)) {
                    return true;
                }
            }
        }
        return false;
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

}
