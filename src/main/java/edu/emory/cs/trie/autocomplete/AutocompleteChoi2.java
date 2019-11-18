package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;

import java.util.*;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteChoi2 extends Autocomplete<List<String>> {
    List<String> result ;
    Queue<String> stringQueue ;
    Queue<TrieNode<List<String>>> nodeQueue;
    TrieNode<List<String>> node;

    public AutocompleteChoi2(String dict_file, int max) {
        super(dict_file, max);
    }



    /*
    Traverse tries using BFS. Used 2 queues to store node and accumulating string in order to achieve BFS.
    1st while - Iterate until list of complete strings hits getMax()
    2nd forloop - Iterate number of node's Children. While this loop, determine if any words are complete(isEndstate)
    if complete, add to the list

    2 Queues remove values at the same time(they store/remove same number of elements)
      */
    @Override
    public List<String> getCandidates(String prefix) {
        //get a node up to the end of the prefix
        node = find(prefix);
        //when input doesn't exist
        if(node == null){
           result = new ArrayList<>();
           return result;
        }
        //if node already contains a list as its value, return its value
        if(node.getValue()!=null) {

            System.out.println("size of a list: "+result.size());
            for(int j=0;j<result.size();j++){
                System.out.println("result string: "+result.get(j)+"\t");
            }
            return node.getValue();
        }
        else {
            result = new ArrayList<>();
            stringQueue = new LinkedList<>();
            nodeQueue = new LinkedList<>();
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
                    for (Character key : temp.getChildrenMap().keySet()) {
                        if(result.size()>=getMax()) break;
                        nodeQueue.add(temp.getChild(key));
                        if (temp.getChild(key).isEndState()) {
                            result.add(accStr + temp.getChild(key).getKey());
                        }
                        stringQueue.add(accStr + temp.getChild(key).getKey());
                    }
            }
            /*  To test elements in the list*/

            System.out.println("size of a list: "+result.size());
            for(int j=0;j<result.size();j++){
                System.out.println("result string: "+result.get(j)+"\t");
            }


            node.setValue(result);//set the result list as node's value
            return result;
        }
    }

    /*
    Take value(List<String>) from the node. Remove the candidate from the list, put it in front of the list and update the value.
     */
    @Override
    public void pickCandidate(String prefix, String candidate) {
        node = find(prefix);
        if(node!=null) {//Edge case when prefix of pickCandidate Doesn't exist.
            List<String> pickList = node.getValue();
            List<String> resultList = new LinkedList<>();
            System.out.println(pickList == null);
            if (pickList != null) {
                for (int i = 0; i < pickList.size(); i++) {
                    String k = pickList.get(i);
                    if (k.equals(candidate)) {
                        resultList.add(candidate);
                        pickList.remove(candidate);
                        resultList.addAll(pickList);
                    }
                }
                node.setValue(resultList);
            }
            else {//When prefix was never called before, create new list using getCandidate, use recursion to find candidate
                getCandidates(prefix);
                pickCandidate(prefix,candidate);
            }
        }
        else{//doesn't return anything when prefix doesn't exist.
            System.out.println("Prefix does not exist");
        }


    }


    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteChoi2 tmp = new AutocompleteChoi2(dict_path,30);
        //tmp.getCandidates("jskdjalskdjlasjdklajsdk");
        //tmp.getCandidates("ec");
       // tmp.getCandidates("aba");
        //tmp.pickCandidate("","abaq");
        //tmp.pickCandidate("","k");
        tmp.getCandidates("ab");
        tmp.getCandidates("aba");
        tmp.getCandidates("ab");


        //tmp.pickCandidate("ship","shipboy");
        //tmp.pickCandidate("ship","ships");
        //tmp.pickCandidate("ab","avasdjak");
        //tmp.getCandidates("bc");

        //tmp.pickCandidate("ec","eche");
        //tmp.pickCandidate("ship","shipboy");
        }
}