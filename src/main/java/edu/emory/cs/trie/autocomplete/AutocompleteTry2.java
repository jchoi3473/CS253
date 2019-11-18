package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;

import java.util.*;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteTry2 extends Autocomplete<List<String>> {
    List<String> result;
    Queue<String> stringQueue ;
    Queue<TrieNode<List<String>>> nodeQueue;
    TrieNode<List<String>> node;
    public AutocompleteTry2(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        node = find(prefix);
        if(node.getValue()!=null)
            return node.getValue();
        else {
            result = new ArrayList<>();
            stringQueue = new LinkedList<>();
            nodeQueue = new LinkedList<>();
            //get a node up to the end of the prefix
            //Edge Case when prefix is already a matching word
            if (node.isEndState())
                result.add(prefix);
            //starting enque
            stringQueue.add(prefix);
            nodeQueue.add(node);
            //System.out.println("getMax: "+getMax()); // pre-iteration checker
            //System.out.println("result: "+result.size()); //pre-iteration checker
            int i = 0;
            while (result.size() < getMax()) {
                TrieNode<List<String>> temp = nodeQueue.remove();
                //System.out.println("node key: "+temp.getKey());//node key checker
                String accStr = stringQueue.remove();
                //System.out.println("accStr: "+accStr);//accStr checker
                for (Character key : temp.getChildrenMap().keySet()) {
                    //System.out.println("Iteration: "+i+"\t"+temp.getChild(key).getKey()); //iteration counter
                    nodeQueue.add(temp.getChild(key));

                    if (temp.getChild(key).isEndState()) {
                        result.add(accStr + temp.getChild(key).getKey());
                    }

                    stringQueue.add(accStr + temp.getChild(key).getKey());
                    //i++; //iteration counter
                }
                // i=0; //iteration counter
            }

            for(int j=0;j<result.size();j++){
                System.out.println("result string: "+result.get(j)+"\t");
            }

            node.setValue(result);
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
        // TODO: must be filled
        node = find(prefix);
        /*
        for(int i=0;i<prefix.length();i++){
            node = node.getChild(prefix.charAt(i));
        }
         */
        List<String> pickList = node.getValue();
        List<String> resultList = new LinkedList<>();
        if(pickList!=null){
            //for(String k : pickList){
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
        System.out.println(resultList.size());
        for(int j=0;j<resultList.size();j++){
            System.out.println("resultList string: "+resultList.get(j)+"\t");
        }



    }


    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteTry2 tmp = new AutocompleteTry2(dict_path,10);
        tmp.getCandidates("ship");
        tmp.getCandidates("ec");
        //tmp.getCandidates("we");
        tmp.pickCandidate("ship","shipboy");
        tmp.pickCandidate("ship","ship");
        tmp.pickCandidate("ship","shipboy");
        tmp.pickCandidate("ship","ships");
        tmp.pickCandidate("ship","shipt");
        tmp.pickCandidate("ec","eche");
        tmp.pickCandidate("ship","shipboy");






    }
}