package edu.emory.cs.trie;

import java.util.ArrayList;
import java.util.List;

public class TrieQuiz {
    class Entity {
        int begin_index;
        int end_index;
        int country_id;

        /**
         * @param begin_index the offset of the first character (inclusive).
         * @param end_index the offset of the last character (exclusive).
         * @param country_id the unique country ID.
         */
        public Entity(int begin_index, int end_index, int country_id) {
            this.begin_index = begin_index;
            this.end_index = end_index;
            this.country_id = country_id;
        }
    }

    /**
     * @param T the trie containing all country names as keys and their unique IDs as values
     *          (e.g., T.get("United States") -> 0, T.get("South Korea") -> 1).
     * @param input the input string in plain text
     *              (e.g., "I was born in South Korea and raised in the United States").
     * @return the list of entities (e.g., [Entity(14, 25, 1), Entity(44, 57, 0)]).
     */

    List<Entity> get_entities(Trie<Integer> T, String input) {
        List<Entity> entities = new ArrayList<>();
        // TODO: to be filled.
        /*First for loop to make i as a starting index for search*/
        for(int i=0;i<input.length();i++) {
            String word=""; //String variable to store words
            /*Second for loop to make j as an ending index for search*/
            for (int j = i; j < input.length(); j++) {
                word+=input.charAt(j);
                    if (T.contains(word)) { //is word is equal to one of elements in Trie, if runs.
                        int id = T.get(word); //get value of a word in Trie
                        Entity e = new Entity(i,j+1,id); //create a new Entity to return
                        entities.add(e); //add e to entities list
                        //add(new Entity(i,j,id));
                    }
            }
        }
        return entities;//return entities.
    }

    //Trial1
    /*
        int firstI = 0;
        int secI=0;
        int wordCount =0;
        int arrayCounter =0;
        String[] countryA = new String[10];
        int[] indexA = new int[10];
        for(int i=0;i<input.length()-1;i++){
            if(Character.isUpperCase(input.charAt(i))){
                System.out.println(i);
                countryA[arrayCounter] =""+ input.charAt(i);
                indexA[arrayCounter] = i;
                while(input.charAt(i+1)!=' '&&i<input.length()-1){
                       countryA[arrayCounter] += input.charAt(i+1);
                       i++;
                }
                arrayCounter++;
            }
        }
        for(int i=0;i<countryA.length;i++){
            System.out.println(countryA[i]);
            System.out.println(indexA[i]);
        }
        System.out.println(wordCount);


     */
    /*Testing Main Method*/
    public static void main(String[] args) {
        Trie<Integer> tr = new Trie<>();
        tr.put("United States",0);
        tr.put("South Korea",1);
        TrieQuiz tq = new TrieQuiz();
        tq.get_entities(tr,"I was born in South Korea and raised in the United States");
    }
}