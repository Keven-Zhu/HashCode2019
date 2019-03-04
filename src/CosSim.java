import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class CosSim {

    static int break_point = 10000;

    public static void main(String args[]){
        String file = "d_pet_pictures.txt";
        String [] stuff = readFile(file);

        System.out.println(file);

        ArrayList<String> arr = new ArrayList<String>(Arrays.asList(stuff));
        String [] arr1 = combineV(arr).toArray(new String[arr.size()]);

//        for(int i=0; i<stuff.length; i++){
//            if(stuff[i].split(" ")[0].equals("H")){
//                String start = "";
//                int num = Integer.parseInt(stuff[i].split(" ")[1]);
//                int j = 0;
//                while(j < num){
//                    start += stuff[i].split(" ")[2 + j] + " ";
//                    j++;
//                }
//                arr.add(start);
//            }
//        }

        //String a [] = arr.toArray(new String[arr.size()]);
        System.out.println(cosineSimilarity(arr1));

    }

    public static int cosineSimilarity(String vals[]){
        ArrayList<String> permutations = new ArrayList <String>();
        ArrayList<Integer> alreadyUsed = new ArrayList <Integer>();

        ArrayList<String> indexes = new ArrayList<String>();

        for(int i=0; i<vals.length && i<break_point; i++){
            if(!alreadyUsed.contains(i)){
                int max_1 = 0;
                int [] iv = new int[2];
                for(int v = i+1; v<vals.length && v < break_point; v++) {
                    if (!alreadyUsed.contains(v) && vals[i] != null && vals[v] != null) {
                        int points = twoPoints(vals[i].split(" "), vals[v].split(" "));
                        if (points > max_1) {
                            max_1 = points;
                            iv[0] = i;
                            iv[1] = v;
                        }
                    }
                }
                if(max_1 > 0){
                    permutations.add(vals[iv[0]]);
                    permutations.add(vals[iv[1]]);
                    alreadyUsed.add(iv[0]);
                    alreadyUsed.add(iv[1]);
                    indexes.add(integers.get(iv[0]));
                    indexes.add(integers.get(iv[1]));
                }
            }
        }
        int points = calcPoints(permutations);


        try{
            FileWriter fw = new FileWriter("src/submission_6.txt");
            fw.write(indexes.size() + "\n");
            for(int i=0; i<indexes.size(); i++){
                fw.write(indexes.get(i) + " \n");
            }
            fw.close();
        } catch (Exception e){

        }


        return points;
    }

    static ArrayList<String> integers = new ArrayList<String>();

    public static ArrayList<String> combineV(ArrayList<String> input){
        ArrayList<String> smashed = new ArrayList<String>();
        ArrayList<String> toBeSmashed = new ArrayList<String>();
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();


        for (int i = 0; i < input.size(); i++){
            String temp = input.get(i);
            if ((temp).charAt(0) == 'H'){
                smashed.add(temp.split(" ", 3)[2]);
                integers.add(Integer.toString(i));
            }
            else{
                toBeSmashed.add(temp.split(" ", 3)[2]);
                hmap.put(temp.split(" ", 3)[2], i);
            }
        }

        if ((toBeSmashed.size()%2) == 1){
            toBeSmashed.remove(toBeSmashed.get(toBeSmashed.size() - 1));
        }

        String[] arr = new String[toBeSmashed.size()];
        arr = toBeSmashed.toArray(arr);

        Comparator<String> lengthComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        };
        Arrays.sort(arr, lengthComparator);

        toBeSmashed = new ArrayList<String>(Arrays.asList(arr));

        ArrayList <String> alreadyAdded = new ArrayList<String>();

        while (toBeSmashed.size() > 0){
            if(!alreadyAdded.contains(hmap.get(toBeSmashed.get(0))) && !alreadyAdded.contains(toBeSmashed.get(toBeSmashed.size() - 1))){
                smashed.add(toBeSmashed.get(0) + toBeSmashed.get(toBeSmashed.size() - 1));
                integers.add(hmap.get(toBeSmashed.get(0)) + " " + hmap.get(toBeSmashed.get(toBeSmashed.size() - 1)));

                alreadyAdded.add(Integer.toString(hmap.get(toBeSmashed.get(0))));
                alreadyAdded.add(Integer.toString(hmap.get(toBeSmashed.get(toBeSmashed.size() - 1))));

                toBeSmashed.remove(0);
                toBeSmashed.remove((toBeSmashed.size() - 1));

            }
        }

        return smashed;
    }

    public static int twoPoints(String[] s11, String[] s22){
        ArrayList<String> common = new ArrayList<>();
        ArrayList<String> s1diff = new ArrayList<>();
        ArrayList<String> s2diff = new ArrayList<>();

        ArrayList <String> s1 = new ArrayList<String>(Arrays.asList(s11));
        ArrayList <String> s2 = new ArrayList<String>(Arrays.asList(s22));

        int score = 0;

        for (String str: s1){
            if (s2.contains(str)){
                common.add(str);
            }
            else{
                s1diff.add(str);
            }
        }
        for(String str: s2){
            if(!s1.contains(str)){
                s2diff.add(str);
            }
        }
        score = Math.min(Math.min(common.size(), s1diff.size()), s2diff.size());

        return score;
    }


    public static int calcPoints(ArrayList<String> slideshow){

        int score = 0;

        for(int s = 0; s<slideshow.size() - 1; s++){
            ArrayList<String> s1 = new ArrayList<String>(Arrays.asList(slideshow.get(s).split(" ")));
            ArrayList<String> s2 = new ArrayList<String>(Arrays.asList(slideshow.get(s + 1).split(" ")));

            ArrayList<String> common = new ArrayList<>();
            ArrayList<String> s1diff = new ArrayList<>();
            ArrayList<String> s2diff = new ArrayList<>();

            for (String str: s1){
                if (s2.contains(str)){
                    common.add(str);
                }
                else{
                    s1diff.add(str);
                }
            }
            for(String str: s2){
                if(!s1.contains(str)){
                    s2diff.add(str);
                }
            }
            score += Math.min(Math.min(common.size(), s1diff.size()), s2diff.size());
        }

        return score;
    }

    public static String[] readFile(String fileLocation) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/" + fileLocation));

            String line;
            int count = Integer.parseInt(br.readLine());

            String [] photos = new String[count];
            int index = 0;

            while((line = br.readLine()) != null){
                photos[index] = line;
                index ++;
            }
            return photos;

        } catch (Exception e){
        }

        return new String[4];
    }
}
