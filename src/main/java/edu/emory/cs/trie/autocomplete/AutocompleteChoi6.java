package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.Trie;
import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteChoi6 extends Autocomplete<List<String>> {
    List<String> result ;
    Queue<String> stringQueue ;
    Queue<TrieNode<List<String>>> nodeQueue;
    TrieNode<List<String>> node;

    public AutocompleteChoi6(String dict_file, int max) {
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
        if(node == null){
            System.out.println("null detected");
            put(prefix,null);
            node = find(prefix);
            node.setEndState(false);
            result = new ArrayList<>();
            return result;
        }
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
            /*  To test elements in the list
            System.out.println("size of a list: "+result.size());
            for(int j=0;j<result.size();j++){
                System.out.println("result string: "+result.get(j)+"\t");
            }
            */
            node.setValue(result);//set the result list as node's value
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
                if(find(candidate)==null||!find(candidate).isEndState()) {
                    pickList.add(0,candidate);
                    put(candidate, null);
                }
                else if (pickList.remove(candidate)) {
                    pickList.add(0, candidate);
                }
                else {
                    pickList.remove(candidate);
                    pickList.add(0,candidate);
                }
                System.out.println("candidate  "+candidate+"  picklist "+pickList.get(0));
                int i=0,j=0,index=0;
                while(i < getMax() && pickList.size()>0 && resultList.get(i)!=null){
                    String getJ = pickList.get(0);
                    System.out.print("GET: "+getJ+"\t");
                    if(!resultList.get(i).equals(getJ)){
                        resultList.add(index,getJ);
                        pickList.remove(0);
                        j++;
                        i++;
                        index++;
                    }
                    else {
                        pickList.remove(0);
                        i++;j++;index++;
                    }
                }
                for(int k=0; k<resultList.size();k++){
                    System.out.print(resultList.get(k)+"\t");
                }
                node.setValue(resultList);
            } else {//When prefix was never called before, create new list using getCandidate, use recursion to find candidate
                getCandidates(prefix);
                pickCandidate(prefix, candidate);
            }
//            for(int j=0;j<resultList.size();j++){
//                System.out.println("resultList string: "+resultList.get(j)+"\t");
//            }
//            System.out.println();
        }
        else{
            getCandidates(prefix);
            pickCandidate(prefix,candidate);
            /*
            if (node.getValue() == null) {
                List<String> resultList = new ArrayList<>();
                resultList.add(candidate);
                if (!contains(candidate))
                    put(candidate, null);
                node.setValue(resultList);
            } else {
                List<String> temp = node.getValue();
                temp.add(0,candidate);
                if(temp.size()>getMax())
                    temp.remove(temp.size()-1);
                node.setValue(temp);
                if (!contains(candidate))
                    put(candidate, null);
            }

             */

            /*
            put(prefix, null);
            node = find(prefix);
            node.setEndState(false);//when prefix doesn't exist, return an empty list
                if (node.getValue() == null) {
                    List<String> resultList = new ArrayList<>();
                    resultList.add(candidate);
                    if (!contains(candidate))
                        put(candidate, null);
                    node.setValue(resultList);
                } else {
                    node.getValue().add(0, candidate);
                    if (!contains(candidate))
                        put(candidate, null);
                }

             */

            //doesn't return anything when prefix doesn't exist.
            //System.out.println("Prefix does not exist");
        }

        /* Return test*/


    }
    /* Testing main method*/
    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteChoi6 tmp = new AutocompleteChoi6(dict_path,380000);
        //tmp.getCandidates("jskdjalskdjlasjdklajsdk");
        //tmp.getCandidates("ec");
//        tmp.getCandidates("");
//
//        tmp.getCandidates("jinho");

//        //tmp.getCandidates("a   b   a");
//        //tmp.getCandidates("ab");
//        //tmp.pickCandidate("a  b  a","");
//        //tmp.getCandidates("ab");
//        tmp.pickCandidate("","aba");
//        tmp.pickCandidate("","abakada");
//        tmp.pickCandidate("","cd");
        //tmp.pickCandidate("ab","abakada");
        //tmp.pickCandidate("ship","shipt");
        //tmp.pickCandidate("pc","eche");
        //tmp.pickCandidate("ship","shipboy");
    }
}