package MorphologicalAnalysis;

import DataStructure.CounterHashMap;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FiniteStateMachineTest {
    FiniteStateMachine fsm;
    ArrayList<State> stateList;

    @Before
    public void setUp() {
        fsm = new FiniteStateMachine("turkish_finite_state_machine.xml");
        stateList = fsm.getStates();
    }

    @org.junit.Test
    public void testStateCount() {
        assertEquals(141, stateList.size());
    }

    @org.junit.Test
    public void testStartEndStates() {
        int endStateCount = 0;
        for (State state : stateList){
            if (state.isEndState()){
                endStateCount++;
            }
        }
        assertEquals(37, endStateCount);
        CounterHashMap<String> posCounts = new CounterHashMap<>();
        for (State state : stateList){
            posCounts.put(state.getPos());
        }
        assertEquals(1, (int) posCounts.get("HEAD"));
        assertEquals(6, (int) posCounts.get("PRON"));
        assertEquals(1, (int) posCounts.get("PROP"));
        assertEquals(8, (int) posCounts.get("NUM"));
        assertEquals(7, (int) posCounts.get("ADJ"));
        assertEquals(1, (int) posCounts.get("INTERJ"));
        assertEquals(1, (int) posCounts.get("DET"));
        assertEquals(1, (int) posCounts.get("ADVERB"));
        assertEquals(1, (int) posCounts.get("QUES"));
        assertEquals(1, (int) posCounts.get("CONJ"));
        assertEquals(26, (int) posCounts.get("VERB"));
        assertEquals(1, (int) posCounts.get("POSTP"));
        assertEquals(1, (int) posCounts.get("DUP"));
        assertEquals(11, (int) posCounts.get("NOUN"));
    }

    @org.junit.Test
    public void testTransitionCount() {
        int transitionCount = 0;
        for (State state : stateList){
            transitionCount += fsm.getTransitions(state).size();
        }
        assertEquals(783, transitionCount);
    }

    @org.junit.Test
    public void testTransitionWith() {
        CounterHashMap<String> transitionCounts = new CounterHashMap<>();
        for (State state : stateList){
            ArrayList<Transition> transitions = fsm.getTransitions(state);
            for (Transition transition : transitions){
                transitionCounts.put(transition.toString());
            }
        }
        List<Map.Entry<String, Integer>> topList = transitionCounts.topN(5);
        assertEquals("0", topList.get(0).getKey());
        assertEquals(111, (int) topList.get(0).getValue());
        assertEquals("lAr", topList.get(1).getKey());
        assertEquals(37, (int) topList.get(1).getValue());
        assertEquals("DHr", topList.get(2).getKey());
        assertEquals(28, (int) topList.get(2).getValue());
        assertEquals("Hn", topList.get(3).getKey());
        assertEquals(24, (int) topList.get(3).getValue());
        assertEquals("lArH", topList.get(4).getKey());
        assertEquals(23, (int) topList.get(4).getValue());
    }

    @org.junit.Test
    public void testTransitionWithName() {
        CounterHashMap<String> transitionCounts = new CounterHashMap<>();
        for (State state : stateList){
            ArrayList<Transition> transitions = fsm.getTransitions(state);
            for (Transition transition : transitions){
                transitionCounts.put(transition.with());
            }
        }
        List<Map.Entry<String, Integer>> topList = transitionCounts.topN(5);
        assertEquals(null, topList.get(0).getKey());
        assertEquals(52, (int) topList.get(0).getValue());
        assertEquals("^DB+VERB+CAUS", topList.get(1).getKey());
        assertEquals(33, (int) topList.get(1).getValue());
        assertEquals("^DB+VERB+PASS", topList.get(2).getKey());
        assertEquals(31, (int) topList.get(2).getValue());
        assertEquals("A3PL", topList.get(3).getKey());
        assertEquals(28, (int) topList.get(3).getValue());
        assertEquals("LOC", topList.get(4).getKey());
        assertEquals(24, (int) topList.get(4).getValue());
    }

}