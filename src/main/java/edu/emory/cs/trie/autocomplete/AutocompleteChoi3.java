package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;

import java.util.*;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteChoi3 extends Autocomplete<List<String>> {
    List<String> result ;
    Queue<String> stringQueue ;
    PriorityQueue<TrieNode<List<String>>> nodeQueue;
    TrieNode<List<String>> node;

    public AutocompleteChoi3(String dict_file, int max) {
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
        //if node already contains a list as its value, return its value

        if(node == null){
            result = new ArrayList<>();
            return result;
        }
        Comparator<TrieNode<List<String>>> characterComparator = new Comparator<>() {
            @Override
            public int compare(TrieNode<List<String>> n1, TrieNode<List<String>> n2) {
                if(n1.getKey()>n2.getKey())
                    return 1;
                else if(n1.getKey()<n2.getKey())
                    return -1;
                else
                    return 0;
            }
        };
        if(node.getValue()!=null)
            return node.getValue();
        else {
            result = new ArrayList<>();
            stringQueue = new LinkedList<>();
            nodeQueue = new PriorityQueue<TrieNode<List<String>>>(characterComparator);
            //Edge Case when prefix is already a matching word
            if (node.isEndState())
                result.add(prefix);
            //starting enque
            stringQueue.add(prefix);
            nodeQueue.add(node);
            int i = 0;

            while (result.size() < getMax()) {
                TrieNode<List<String>> temp = nodeQueue.remove();
                String accStr = stringQueue.remove()+temp.getKey();
                System.out.println(accStr);
                stringQueue.add(accStr);

                if(temp.isEndState()){
                    result.add((accStr));
                }
                if(result.size()>=getMax()) break;
                for (Character key : temp.getChildrenMap().keySet()) {
                    nodeQueue.add(temp.getChild(key));
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
/* List Checker
    System.out.println(result.size());
    for(int j=0;j<result.size();j++){
        System.out.println("result string: "+result.get(j)+"\t");
    }
 */

    @Override
    public void pickCandidate(String prefix, String candidate) {
        System.out.println("call for "+candidate);
        node = find(prefix);
        List<String> pickList = node.getValue();
        List<String> resultList = new LinkedList<>();
        if(pickList!=null){
            for(int i=0;i<pickList.size();i++){
                String k = pickList.get(i);
                if(k.equals(candidate)){
                    resultList.add(candidate);
                    pickList.remove(candidate);
                    resultList.addAll(pickList);
                }
            }
            node.setValue(resultList);
        }
        else {
            getCandidates(prefix);
            pickCandidate(prefix,candidate);
        }

        for(int j=0;j<resultList.size();j++){
            System.out.println("resultList string: "+resultList.get(j)+"\t");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteChoi3 tmp = new AutocompleteChoi3(dict_path,10);
        //tmp.getCandidates("jskdjalskdjlasjdklajsdk");
        //tmp.getCandidates("ec");
        tmp.getCandidates("we");
        //tmp.pickCandidate("","");
        //tmp.pickCandidate("","k");
        //tmp.pickCandidate("ship","shipboy");
        //tmp.pickCandidate("ship","ships");
        //tmp.pickCandidate("ship","shipt");
        //tmp.pickCandidate("ec","eche");
        //tmp.pickCandidate("ship","shipboy");
    }








}