package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteChoi9 extends Autocomplete<List<String>> {
    List<String> result ;
    Queue<String> stringQueue ;
    Queue<TrieNode<List<String>>> nodeQueue;
    TrieNode<List<String>> node;

    public AutocompleteChoi9(String dict_file, int max) {
        super(dict_file, max);
    }
    /*
    Traverse tries using BFS. Used 2 queues to store node and accumulating string in order to achieve BFS.
    1st while - Iterate until list of complete strings hits getMax()
    2nd forloop - Iterate number of node's Children. While this loop, determine if any words are complete(isEndstate)
    if complete, add to the list. In between two loops, sort nodes by alphabetical order and put them in a queue.
    This will guarantee alphabetically sorted list when comparing elements with same length.
    2 Queues remove values at the same time(they store/remove same number of elements)
      */
    @Override
    public List<String> getCandidates(String prefix) {
        prefix = prefix.replaceAll("\\s+","");//trim all white spaces
        //get a node up to the end of the prefix
        node = find(prefix);
        if(node == null){ //adding prefix not in a dictionary but is not a word(End State = False)
            put(prefix,null);
            node = find(prefix);
            node.setEndState(false);
            return new ArrayList<>();
        }
        if(node.getValue()!=null) {//if node already contains a list as its value, return its value
            List<String> temp = node.getValue();
            int num = temp.size();
            List<String> newList = creatList(prefix);

            for(int i=0;i<num;i++){//need to add newly added elements in tries to the list
                if(!newList.contains(temp.get(0))){
                    newList.add(i,temp.get(0));
                    temp.remove(0);
                }
                else if(!newList.get(i).equals(temp.get(0))){
                    if(!temp.contains(newList.get(i))){//newly introduced
                        temp.remove(0);
                    }
                    else {
                        newList.remove(temp.get(0));
                        newList.add(i, temp.get(0));
                        temp.remove(0);
                    }
                }
                else {
                    temp.remove(0);
                }
            }
            node.setValue(newList);
            return newList;
        }
        else {
            result=creatList(prefix);
            node.setValue(result);//set the result list as node's value
            return result;
        }
    }
    //Create a list that gets an original form of list from trie using BFS, main trie >> List orient algorithm
    public List<String> creatList(String prefix){
        node = find(prefix);
        result = new ArrayList<>();
        stringQueue = new LinkedList<>();
        nodeQueue = new LinkedList<>();;
        //Edge Case when prefix is already a matching word
        if (node.isEndState())
            result.add(prefix);
        //starting enque
        stringQueue.add(prefix);
        nodeQueue.add(node);
        while (result.size() < getMax()) {
            if(nodeQueue.isEmpty()) break; //edge case, when not enough number of words left in trie than max
            TrieNode<List<String>> temp = nodeQueue.remove();
            String accStr = stringQueue.remove();
            char[] c = new char[temp.getChildrenMap().size()];
            int i =0;
            for (Character key : temp.getChildrenMap().keySet()) {//store keys into an array
                c[i] = key;
                i++;
            }
            Arrays.sort(c);//sort nodes in an alphabetical order
            for (Character newChar : c) { //add alphabetically sorted nodes to the Queue
                if(result.size()>=getMax()) break;
                nodeQueue.add(temp.getChild(newChar));
                if (temp.getChild(newChar).isEndState()) {
                    result.add(accStr + temp.getChild(newChar).getKey());
                }
                stringQueue.add(accStr + temp.getChild(newChar).getKey());
            }
        }
        return result;
    }
    /*
    When prefix contains a value(List of strings), use the string
    */
    @Override
    public void pickCandidate(String prefix, String candidate) {
        prefix = prefix.replaceAll("\\s+","");//trim all white spaces
        candidate = candidate.replaceAll("\\s+","");//trim all white spaces
        node = find(prefix);
        if(node!=null) {//Edge case when prefix of pickCandidate Doesn't exist.
            List<String> pickList = node.getValue();
            List<String> resultList = getCandidates(prefix);
            if (pickList != null) {
                if(find(candidate)==null||!find(candidate).isEndState()){//when candidate doesn't exist
                    put(candidate,null);
                    resultList.add(0,candidate);
                    resultList.remove(resultList.size()-1);
                }
                else if (resultList.remove(candidate)) {//when candidate is in the list
                    resultList.add(0, candidate);
                }
                else {//not official word existence
                    resultList.add(0,candidate);
                }
                node.setValue(resultList);
            } else {//When prefix was never called before, create new list using getCandidate, use recursion to find candidate
                getCandidates(prefix);
                pickCandidate(prefix, candidate);
            }
        }
        else{//When prefix is not a word.
            getCandidates(prefix);
            List<String> resultList = new ArrayList<>();
            resultList.add(candidate);
            node.setValue(resultList);
            if (!contains(candidate)){
                if(find(candidate)==null) {
                    put(candidate, null);
                }
                else {
                    put(candidate,find(candidate).getValue());
                }
            }
        }
    }
}

