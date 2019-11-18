/*
 * Copyright 2014, Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteTry1 extends Autocomplete<List<String>> {
    List<String> result;
    TrieNode<List<String>> node;
    public AutocompleteTry1(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        node = getRoot();
        for(int i=0;i<prefix.length();i++){
            node = node.getChild(prefix.charAt(i));
        }
        result = new ArrayList<>();

        //System.out.print(node.getKey());//return last key

        //Queue can be used too.
        /* recursive method to search through a trie using BFS
        while(list < getmax)
            if(prefix endState true)
                list.add(prefix)
            need a way to store a string as proceeding with trie
            for(# child using hashmap)
                getCandidates(prefix+a)
         */
/*
        if (prefix.equals("sh"))
            return List.of("she", "ship", "shell");
        return List.of("dummy", "candidate");
 */
        return  createCandidates(result,prefix,node);
    }

    public List<String> createCandidates(List<String> result, String prefix, TrieNode<List<String>> node){
        if(result.size()==getMax())
            return result;
        else {
            if(node.isEndState()) {
                //prefix += node.getKey();
                result.add(prefix);
                //System.out.print(node.isEndState());
            }
            else {
                // prefix +=  node.getKey();
            }
            if(node.hasChildren()) {
                for (Character key : node.getChildrenMap().keySet()) {
                    // System.out.println(key);
                    if(node.getChild(key)!=null)
                        node = node.getChild(key);
                    //System.out.print(node.getKey());
                    createCandidates(result,prefix+node.getKey(),node);
                }
            }
        }
        for(String sys : result)
            System.out.print(sys+"\t");
        return result;
    }


    @Override
    public void pickCandidate(String prefix, String candidate) {
        // TODO: must be filled
    }

    public static void main(String[] args) {
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteChoi2 tmp = new AutocompleteChoi2(dict_path,10);
        tmp.getCandidates("sh");
    }
}