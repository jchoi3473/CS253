package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;
import java.util.*;

public class AutocompleteExtraTry2 extends Autocomplete<List<List<String>>> {
    List<String> result ;
    Queue<String> stringQueue ;
    Queue<TrieNode<List<List<String>>>> nodeQueue;
    TrieNode<List<List<String>>> node;

    public AutocompleteExtraTry2(String dict_file, int max) {
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
    /*
    Take List<List<String>> as a generic. Number of frequency will depend on the length of each list.
    Whenever a candidate is called, add string to a list. Depending on the length of the each list, frequency can be determined.
    First element of each list will represent the word all the time.
     */
    @Override
    public List<String> getCandidates(String prefix) {
        prefix = prefix.replaceAll("\\s+","");//trim all white spaces

        //get a node up to the end of the prefix
        node = find(prefix);
        if(node == null){ //when prefix doesn't exist, return an empty list
            result = new ArrayList<>();
            return result;
        }
        //if node already contains a list as its value, return its value
        if(node.getValue()!=null) {
            result = new ArrayList<>();
            for(List<String> tempList : node.getValue())
                result.add(tempList.get(0));
            return result;
        }

        else {
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
                TrieNode<List<List<String>>> temp = nodeQueue.remove();
                String accStr = stringQueue.remove();
                char[] c = new char[temp.getChildrenMap().size()];
                int i =0;
                for (Character key : temp.getChildrenMap().keySet()) {
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
            //  To test elements in the list
            List<List<String>> valueStore = new ArrayList<>();
            for(String tempStr:result){
                List<String> tempList = new ArrayList<>();
                tempList.add(tempStr);
                valueStore.add(tempList);
            }
            node.setValue(valueStore);//set the result list as node's value
            return result;
        }
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
            List<List<String>> pickList = node.getValue();
            List<String> resultList = getCandidates(prefix);
            List<List<String>> valueStore = new ArrayList<>();
            if (pickList != null) {
                int index = resultList.indexOf(candidate);
                int i = 0;
                for (List<String> tempList : pickList) {
                    if (i == index)
                        tempList.add(candidate);
                    valueStore.add(tempList);
                    i++;
                }
                if (index >= 0) {
                    List<String> temp = valueStore.remove(index);
                    int count = 0;
                    while (temp.size() < valueStore.get(count).size()) {
                        count++;
                    }
                    valueStore.add(count, temp);
                }
                node.setValue(valueStore);
            } else {
                getCandidates(prefix);
                pickCandidate(prefix, candidate);
            }
        }
        else{//doesn't return anything when prefix doesn't exist.
            System.out.println("Prefix does not exist");
        }
    }
    /*
    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteExtraTry2 tmp = new AutocompleteExtraTry2(dict_path,400000);
        //tmp.getCandidates("jskdjalskdjlasjdklajsdk");
//        //tmp.getCandidates("ec");
//        //tmp.getCandidates("");
//        tmp.getCandidates("ab");
//        tmp.pickCandidate("ab","abalk");
//        tmp.pickCandidate("ab","aba");
//        tmp.pickCandidate("ab","abakada");
//        tmp.pickCandidate("ab","abalk");
//        tmp.pickCandidate("ab","abao");
//        tmp.pickCandidate("ab","aba");
//        tmp.pickCandidate("ab","aba");
//        tmp.pickCandidate("ab","abao");
//        tmp.pickCandidate("ship","shipt");
//        tmp.pickCandidate("ec","eche");
//        tmp.pickCandidate("ship","shipboy");
    }
    */

}