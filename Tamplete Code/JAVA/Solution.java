package test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;

import org.apache.commons.collections.list.TreeList;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

public class Solution {
    // Complete the findDigits function below.
    static int findDigits(int n) {
    	int result = 0;
    	String s = String.valueOf(n);
    	
    	for(char i : s.toCharArray())
    	{
    		
    		System.out.println(n+","+i+","+(n%Character.getNumericValue(i)));
    		if(n%Character.getNumericValue(i)==0) result++;
    		if(n%(Character.getNumericValue(i))==0) result++;
    	}
   
    	return result;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            int n = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            int result = findDigits(n);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }

	public int[] Solution(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
}

class Solution2 {

    // Complete the extraLongFactorials function below.
    static void extraLongFactorials(int n) {
    	BigDecimal result = BigDecimal.valueOf(n);
        while(n>1) {
            n--;
            result.multiply(BigDecimal.valueOf(n));
        }
        System.out.println(result);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	
        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        extraLongFactorials(n);

        scanner.close();
    }
}



class Solution4 {

    // Complete the miniMaxSum function below.
    static void miniMaxSum(int[] arr) {
    	long max = 0, min = 0;
    	
    	ArrayList<Integer> al = new ArrayList<Integer>();
    	for(int i :arr) al.add(i);
    	
    	Collections.sort(al);
    	
    	for(int i=0; i<al.size(); i++)
    	{
    		if(i>=1) max += al.get(i);
    		if(i<al.size()-1) min += al.get(i);
    	}
    	
    	System.out.println(max+" "+min);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int[] arr = new int[5];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < 5; i++) {
            int arrItem = Integer.parseInt(arrItems[i]);
            arr[i] = arrItem;
        }

        miniMaxSum(arr);

        scanner.close();
    }
}

class Solution5 {

    // Complete the miniMaxSum function below.
    static void miniMaxSum(int[] arr) {
    	long sum = 0;
    	int max = arr[0], min = arr[0];
    	
    	for(int i : arr) {
    		sum += i;
    		if(max<i) max = i;
    		if(min>i) min = i;
    	}
    	System.out.println(String.format("%d %d", sum-max, sum-min));
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int[] arr = new int[5];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < 5; i++) {
            int arrItem = Integer.parseInt(arrItems[i]);
            arr[i] = arrItem;
        }

        miniMaxSum(arr);

        scanner.close();
    }
}

class Solution6 {

    // Complete the gameOfThrones function below.
    static String gameOfThrones(String s) {
    	
    	int[] i = new int[26];
    	for(char ch : s.toCharArray()) {
    		i[ch-'a']++;
    	}
    	
    	Boolean odd = s.length() % 2 ==1;
    	for(int j : i) {
    		if(j%2==1) {
    			if(odd) odd = false;
    			else return "NO";
    		}
    	}
    	return "YES";
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = scanner.nextLine();

        String result = gameOfThrones(s);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}

class Solution7 {

    // Complete the twoStrings function below.
    static String twoStrings(String s1, String s2) {
    	
//    	for(char ch1 : s1.toCharArray()) {
//    		for(char ch2 : s2.toCharArray()) {
//    			if(ch1 == ch2) return "YES";
//    			s2.replaceAll(String.valueOf(ch2), "");
//    		}
//    		s1.replaceAll(String.valueOf(ch1), "");
//    	}
    	
//    	while(s1.length() > 0) {
//            for(char ch2 : s2.toCharArray()) {
//                if(s1.toCharArray()[0]==ch2) return "YES";
//                s1 = s1.replaceAll(String.valueOf(s1.toCharArray()[0]), "");
//            }
//        }
        
    	if(s2.indexOf(s1.toCharArray()[0]) > -1) return "YES";
    	s1 = s1.replaceAll(String.valueOf(s1.toCharArray()[0]), "");
    	if(s1.length()>0) return twoStrings(s1, s2);
    	return "NO";
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s1 = scanner.nextLine();

            String s2 = scanner.nextLine();

            String result = twoStrings(s1, s2);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}

class Solution8 {

    // Complete the stringConstruction function below.
    static int stringConstruction(String s) {
    	
    	int result = 0;
        
		while(s.length() > 0) {
			s = s.replaceAll(String.valueOf(s.toCharArray()[0]), "");
			result++;
		}
        return result;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s = scanner.nextLine();

            int result = stringConstruction(s);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}

class Solution9 {

    static int jumpingOnClouds(int[] c, int k) {
    	
    	int result = 100;
    	int i=0;
    	do {
    		System.out.println(i);
    		result -= c[i]*2+1;
    		i=(i+k)%c.length;
    	}while(i!=0);
    	
    	return result;
    }
    
    static String appendAndDelete(String s, String t, int k) {
//    	String maxs, mins;
//    	if(Math.max(s.length(), t.length()) == s.length()) {
//    		maxs=s; 
//    		mins=t;
//    	} else {
//    		maxs=t; 
//    		mins=s;
//    	}
//    	k -= (maxs.length() - mins.length());
//    	
//    	for(int i=0; i<mins.length(); i++) {
//    		if(maxs.toCharArray()[i]!=mins.toCharArray()[i]) k-=2;
//    	}
//    	if(k<0) return "NO";
//    	return "YES";
    	
    	int n = Math.min(s.length(), t.length());
    	int idx = n;
    	for(int i=0; i<n; i++) {
    		if(s.toCharArray()[i]==t.toCharArray()[i]) {
    			idx-=i;
    			break;
    		}
    	}
    	k -= Math.abs(s.length()-t.length());
    	if(k-(idx*2)<0) return "Yes";
    	return "No";
    }
    
    static int jumpingOnClouds(int[] c) {
    	int result = 0, i = 1;
    	
    	while(i < c.length) {
    		
    		result++;
    		
    		if(i==c.length-1) break;
    			
    		if(c[i]==1 || (c[i]==0 && c[i+1]==0)) i+=2;
    		else i++;
    	}
    	
    	return result;
    }
    
    static int equalizeArray(int[] arr) {
    	
    	int size = arr.length;
    	int[] cnt = new int[101] ;
    	for(int i : arr) cnt[i]++;
    	
    	Arrays.sort(cnt);
    	
    	return size - cnt[cnt.length-1];
    }
    
    static int chocolateFeast(int n, int c, int m) {
    	
    	int cnt = (int) Math.floor(n/c);
    	int w = cnt;
    	while(w >= m) {
    		int fc = (int) Math.floor(w/m);
    		cnt += fc;
    		w = (w%m)+fc;
    	}
    	
    	return cnt;
    }
    
    
}

class Solution10 {

    // Complete the serviceLane function below.
	static int[] serviceLane(int n, int[][] cases) {
    	int[] maxWidth = new int[n];
    	
    	
    	return maxWidth;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nt = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nt[0]);

        int t = Integer.parseInt(nt[1]);

        int[] width = new int[n];

        String[] widthItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int widthItem = Integer.parseInt(widthItems[i]);
            width[i] = widthItem;
        }

        int[][] cases = new int[t][2];

        for (int i = 0; i < t; i++) {
            String[] casesRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int j = 0; j < 2; j++) {
                int casesItem = Integer.parseInt(casesRowItems[j]);
                cases[i][j] = casesItem;
            }
        }

        int[] result = serviceLane(n, cases);

        for (int i = 0; i < result.length; i++) {
            bufferedWriter.write(String.valueOf(result[i]));

            if (i != result.length - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
    
    static int workbook(int n, int k, int[] arr) {
    	int result = 0;
    	int page = 1;
    	
    	for(int a : arr) {
    		for(int i = 1; i<=a; i++) {
    			if(page == i) result++;
    			if(i%k==0 && i!=a) page++;
    		}
    		page++;
    	}
    	
    	return result;
    }
    
    static int flatlandSpaceStations(int n, int[] c) {

    	int[] max = new int[n];
    	
    	for(int i =0; i<n; i++) {
    		max[i] = n;
    		for(int j : c) {
    			max[i] = Math.min(max[i], Math.abs(j-i));
    		}
    	}
    	Arrays.sort(max);
    	
    	return max[max.length-1];
    }
    

}

class Solution11 {

    // Complete the flatlandSpaceStations function below.
    static int flatlandSpaceStations(int n, String[] c) {

        int[] min = new int[n];
        int max = 0;
        
        for(int i =0; i<n; i++) {
            min[i] = n;
            for(String j : c) {
                min[i] = Math.min(min[i], Math.abs(Integer.valueOf(j)-i));
            }
            max = Math.max(max,min[i]);
        }
        
        
        return max;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nm = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nm[0]);

        int m = Integer.parseInt(nm[1]);

        int[] c = new int[m];

        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        // for (int i = 0; i < m; i++) {
        //     int cItem = Integer.parseInt(cItems[i]);
        //     c[i] = cItem;
        // }

        int result = flatlandSpaceStations(n, cItems);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
    
    static int[] permutationEquation(int[] p) {
    	int[] result = new int[p.length];
    	
    	for(int i=1; i<=p.length; i++) {
    		for(int j=1; j<=p.length; j++) {
    			System.out.println(i+","+j+","+p[p[j-1]-1]);
    			if(i==p[p[j-1]-1]) {
    				result[i-1] = j;
    				break;
    			}
    		}
    	}
    	return result;
    	
    }
    
    static int minimumDistances(int[] a) {
    	int result = a.length;
    	
    	for(int i=0; i<a.length; i++) {
    		for(int j=i+1; j< a.length; j++) {
    			if(a[i]==a[j]) {
    				result = Math.min(result, j-i);
    				break;
    			}
    		}
    	}
    	
    	return result==a.length?-1:result;
    }
    
    static int howManyGames(int p, int d, int m, int s) {
    	int result = 0;
    	while(s >= p) {
    		s-=p;
    		result++;
    		p-=d;
    		if(p<m) p=m;
    	}
    	return result;
    }
    
    static int divisibleSumPairs(int n, int k, int[] ar) {
    	int cnt = 0;
    	for(int i=0;i<n-1;i++) {
    		for(int j=i+1; j<n; j++) {
    			if((ar[i]+ar[j])%k==0) cnt++;
    		}
    	}
    	return cnt;
    }
    
    static int introTutorial(int V, int[] arr) {
    	
    	for(int i=0; i<arr.length; i++) {
    		if(V==arr[i]) return i;
    	}
    	return 0;
    }
    
    static int runningTime(int[] arr) {
    	int cnt = 0;
    	int i=0;
    	while(i<arr.length-1) {
    		if(arr[i]>arr[i+1]) {
    			int temp = arr[i];
    			arr[i]=arr[i+1];
    			arr[i+1]=temp;
    			cnt++;
    			if(i>0) i--;
    		}else i++;
    	}
    	for(int a : arr) System.out.println(a);
    	
    	return cnt;
    }
    
    static int[] quickSort(int[] arr) {
    	int[] result = new int[arr.length];
    	ArrayList<Integer> lst = new ArrayList<>();
    	lst.add(arr[0]);
    	
    	for(int i=1; i<arr.length; i++) {
    		if(arr[0]>arr[i]) lst.add(0, arr[i]);
    		else lst.add(arr[i]);
    	}
    	for(int i=0; i<lst.size(); i++) {
    		result[i] = lst.get(i);
    	}
    	
    	return result;
    }
    
    static int[] quickSort2(int[] arr) {
    	
    	Arrays.sort(arr);
    	
    	return arr;
    }
    
    static int[] countingSort(int[] arr) {
    	
    	int[] result = new int[100];
    	
    	for(int i : arr) result[i]++;
    	
    	return result;
    }
    
    static long marcsCakewalk(int[] calorie) {
    	
    	long result = 0;
    	
    	Arrays.sort(calorie);
    	
    	for(int i=0,j=calorie.length-1; i<calorie.length; i++,j-- ) {
    		result += Math.pow(2, i) * calorie[j];
    	}
    	
    	return result;
    }
    
    static String gridChallenge(String[] grid) {
    	
    	for(int i=0; i<grid.length;i++) {
    		char[] ch = grid[i].toCharArray();
    		Arrays.sort(ch);
    		grid[i]=String.valueOf(ch);
    	}
    	
    	for(int j=0; j<grid[0].length(); j++) {
    		for(int i=0; i<grid.length-1; i++) {
    			System.out.println(grid[i].charAt(j));
    			if(grid[i].charAt(j)>grid[i+1].charAt(j)) return "NO";
    		}
    	}
    	return "YES";
    }
    
    static int toys(int[] w) {
    	
    	Arrays.sort(w);
    	int result = 1, min = w[0];
    	
    	for(int i : w) {
    		if(i-min > 3) {
    			result++;
    			min = i;
    		}
    	}
    	return result;
    }
    
    public static String stringsXOR(String s, String t) {
    	
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == t.charAt(i))
                res.append("0");
            else
                res.append("1");
        }
        return res.toString();
    }
    
    public static boolean isSmartNumber(int num) {
        int val = (int) Math.sqrt(num);    
        if(val*val == num)
            return true;
        return false;
    }
    
    static long repeatedString(String s, long n) {
    	if(s.length()==1) return n;
    	long cnt = 0;
    	char ch = s.charAt((int)(n%s.length())-1);
    	for(int i=0,j=0; i<n;i++,j++) {
    		if(j==s.length()) j=0;
    		if(s.charAt(j)==ch) cnt++;
    	}
    	return cnt;
    }
    
    static int[] missingNumbers(int[] arr, int[] brr) {
    	int[] missingNo = new int[brr.length-arr.length];
    	Arrays.sort(arr);
    	Arrays.sort(brr);
    	int a=0;
    	int j=0;
    	for(int i=0; i<arr.length; i++) {
    		
    		while(j<brr.length) {
    			if(arr[i]==brr[j]) {
    				j++;
    				break;
    			}
    			else {
    				missingNo[a++] = brr[j];
    				j++;
    			}
    		}
    	}
    	return missingNo;
    }
}

 class Solution12 {

    static int[] missingNumbers(int[] arr, int[] brr) {
    	List<Integer> li = new ArrayList<Integer>();
        Arrays.sort(arr);
        Arrays.sort(brr);
        int j=0;
        for(int i=0; i<arr.length; i++) {
            while(j<brr.length) {
                if(arr[i]==brr[j]) {
                    j++;
                    break;
                }
                else {
                    if(li.indexOf(brr[j]) < 0) li.add(brr[j]);
                    j++;
                }
            }
        }
        int[] missNo = new int[li.size()];
        for(int i=0;i<li.size();i++) missNo[i] = li.get(i);
        
        return missNo;

    }
    
    static int[] missingNumbers2(int[] arr, int[] brr) {
    	int min = arr[0];
    	for(int b : brr) if(min>b) min = b;
    	int[] diff = new int[101];
    	
    	for(int cur : brr) {
            diff[cur - min]++;
        }
        
        for(int cur : arr) {
            diff[cur - min]--;
        }
        
        int j = 0;
        for(int i = 0; i < 101; i++) {
            if(diff[i] > 0) j++;
        }
        
        int[] result = new int[j];
        j=0;
        for(int i = 0; i < 101; i++) {
            if(diff[i] > 0) result[j++] = min + i;
        }
        return result;
    }


    
    static int pairs(int k, int[] arr) {
    	int cnt = 0;
    	
//    	for(int i=0; i<arr.length-1; i++) {
//    		for(int j=i+1; j<arr.length; j++) {
//    			if(Math.abs(arr[i]-arr[j]) == k) cnt++;
//    		}
//    	}
    	Arrays.sort(arr);
    	for(int i:arr) if(Arrays.binarySearch(arr, i+k)>=0) cnt++;
    	
    	return cnt;
    }
    
    
    public static String marathon(String[] participant, String[] completion) {
        
        //1
//        for(String s : completion) {
//        	System.out.println(s +" : "+Arrays.binarySearch(participant, s));
//        	if(Arrays.binarySearch(participant, s) < 0) answer = s;
//        }
        
        //2
//        List<String> l = new ArrayList<>();
//        List<String> l2 = new ArrayList<>();
//        Collections.addAll(l, participant);
//        Collections.addAll(l2, completion);
//        l.removeAll(l2);
//        answer= l.get(0);
        Arrays.sort(participant);
        Arrays.sort(completion);

        for(int i=0; i<completion.length; i++) {
        	if(!participant[i].equals(completion[i])) return participant[i];
        }

        return participant[participant.length-1];
    }
    
    public static boolean tel(String[] phone_book) {
        boolean answer = true;
        Arrays.sort(phone_book);
        for(int i=0;i<phone_book.length-1;i++) {
        	for(int j=i+1;j<phone_book.length;j++) {
        		if(phone_book[j].startsWith(phone_book[i])) return false;
        	}
        }

        return answer;
    }
    
    public static int camouflage(String[][] clothes) {
    	
        int answer = 1;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        
        for(String s1[] : clothes) {
        	if(map.containsKey(s1[1])) map.put(s1[1], map.get(s1[1])+1);
        	else map.put(s1[1], 1);
        }
        
        for(String k : map.keySet()) {
        	System.out.println(k+":"+map.get(k));
        	answer *= map.get(k) + 1;
        }
        
        return answer-1;
    }
    
    public static int[] bestAlbum(String[] genres, int[] plays) {
    	
    	ArrayList<HashMap<String, Object>> lm = new ArrayList<HashMap<String,Object>>();
    	Map<String, Integer> genSum = new TreeMap<String, Integer>((i1, i2)->i2.compareTo(i1));
    	ArrayList<Integer> result = new ArrayList<Integer>();
    	
    	for(int i=0; i<genres.length; i++) {
    		HashMap<String, Object> m = new HashMap<>();
    		m.put("genres", genres[i]);
    		m.put("plays", plays[i]);
    		m.put("no", i);
    		lm.add(m);
    		
    		if(genSum.containsKey(genres[i])) genSum.put(genres[i], genSum.get(genres[i]) + plays[i]);
    		else genSum.put(genres[i], plays[i]);
    	}
    	
    	Collections.sort(lm,new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> m1, HashMap<String, Object> m2) {
            	int i =  genSum.get(m2.get("genres")).compareTo(genSum.get(m1.get("genres")));
            	int j =  ((Integer) m2.get("plays")).compareTo((Integer) m1.get("plays"));
            	int k = ((Integer) m1.get("no")).compareTo((Integer) m2.get("no"));
            	return i==0? (j ==0? k: j) : i;
            }
    	});

    	for(String s : genSum.keySet()) {
    		int i=0;
    		for(HashMap<String, Object> m : lm) {
    			if(i>1) break;
    			if(m.get("genres").equals(s)) {
    				result.add((Integer) m.get("no"));
    				i++;
    			}
    		}
    	}
    	
//    	for(HashMap m : lm) {
//    		System.out.println(m.get("genres")+", "+m.get("plays")+", "+m.get("no"));
//    	}
    	
    	return result.stream().mapToInt(i -> i).toArray();
    }
    
    public static int[] bestAlbum2(String[] genres, int[] plays) {
    	
    	List<Map<String,Object>> lmGenres = new ArrayList<Map<String,Object>>();
    	List<Integer> result = new ArrayList<>();
    	
    	for(int i=0; i<genres.length; i++) {
    		int chkIdx = -1;
    		for(int j=0; j<lmGenres.size(); j++) {
    			if(lmGenres.get(j).containsValue(genres[i])) {
    				chkIdx = j;
    				break;
    			}
    		}
    		if(chkIdx > -1) {
    			lmGenres.get(chkIdx).put("plays", (int)lmGenres.get(chkIdx).get("plays")+plays[i]);
    		}else {
    			Map<String, Object> tempM = new HashMap<>();
    			tempM.put("genres", genres[i]);
        		tempM.put("plays", plays[i]);
    			lmGenres.add(tempM);
    		}
    	}
    	
		Collections.sort(lmGenres,new playCompare());
		
		for(Map<String,Object> m :lmGenres) {
			List<Map<String,Object>> lmMusic = new ArrayList<Map<String,Object>>();
			for(int i=0; i<genres.length; i++) {
				Map<String, Object> tempM = new HashMap<>();
				if(m.get("genres").equals(genres[i])) {
					tempM.put("plays", plays[i]);
					tempM.put("no", i);
					lmMusic.add(tempM);
				}
			}
			Collections.sort(lmMusic,new playCompare());
			
			m.put("music", lmMusic);
		}
    	
		for(Map<String,Object> m :lmGenres) {
			List<Map<String,Object>> lmMusic = (List<Map<String, Object>>) m.get("music");
    		for(int i=0; i<lmMusic.size(); i++) {
    			if(i>1) break;
    			result.add((Integer) lmMusic.get(i).get("no"));
    		}
		}
		
		
//    	for(Map m :lmGenres) {
//    		System.out.println(m.get("genres")+", "+m.get("plays"));
//    		List<Map<String,Object>> lmMusic = (List<Map<String, Object>>) m.get("music");
//    		for(Map m2 : lmMusic) {
//    			System.out.println("=> "+m2.get("no")+" : "+m2.get("plays"));
//    		}
//    	}
    	
    	int[] answer = new int[result.size()];
    	for(int i=0; i<result.size();i++) answer[i]=result.get(i);
    	
        return answer;
    }
    
    public static class playCompare implements Comparator<Map<String, Object>>{
    	public int compare(Map<String, Object> m1, Map<String, Object> m2) {
			return ((Integer) m2.get("plays")).compareTo((Integer) m1.get("plays"));
		}
    }
    
    public static void main(String[] args) throws IOException {
    	
    	
    	
    	String[] a = {"classic", "pop", "hippop","classic", "classic", "pop","hippop","hippop"};
    	int[] b =    {500, 		  600, 	   1000,	 150, 		800, 	2500,  1000,	1000};
    	
    	ArrayList<String> l = new ArrayList<>(Arrays.asList(a));
    	l.set(2, "pop2");
    	System.out.println(l);
    	for(String s :l) System.out.println(s);
    	
    	
    	int[] result = bestAlbum2(a,b);
    	
    	for(int i : result) System.out.println(i);
    }
}

 class Solution13 {
	 
	 public static int[] kNumber(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];
        
        for(int i=0; i<commands.length; i++) {
        	int[] arr = Arrays.copyOfRange(array, commands[i][0]-1, commands[i][1]);
        	Arrays.sort(arr);
        	answer[i] = arr[commands[i][2]-1];
        }
        
        return answer;
    }

	 public static void main(String[] args) throws IOException {
	    	
	 	int[] a = {1, 5, 2, 6, 3, 7, 4};
    	int[][] b = {{2, 5, 3}, {4, 4, 1}, {1, 7, 3}};
    	
    	
    	int[] result = kNumber(a,b);
    	
    	for(int i : result) System.out.println(i);
	 }
 }

class Solution14 {

	public static String bigNo(int[] numbers) {
		
		StringBuffer result = new StringBuffer();
		String[] arrS = Arrays.stream(numbers).mapToObj(i->String.valueOf(i)).toArray(String[]::new); 
		
		Arrays.sort(arrS, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return (s2+s1).compareTo(s1+s2);
//				for(int i=s1.length()-1,j=0;i<3;i++,j++) {
//					s1+=s1.toCharArray()[j];
//				}
//				for(int i=s2.length()-1,j=0;i<3;i++,j++) {
//					s2+=s2.toCharArray()[j];
//				}
//				return s2.compareTo(s1);
			}
		});
		
		for(String s : arrS) {
//			System.out.print(s+", ");
//			for(int i=s.length()-1,j=0;i<3;i++,j++) {
//				s+=s.toCharArray()[j];
//			}
			System.out.println(s);
		}
		for(String s : arrS) result.append(s);
		
//		BigDecimal a = new BigDecimal(result.toString());
		return String.valueOf(new BigDecimal(result.toString()));
		//실행한 결괏값 95343[03]이(가) 기댓값 95343[30]와(과) 다릅니다.
	}
	
	public static String bigNo2(int[] numbers) {
		
		Integer[] num = Arrays.stream(numbers).boxed().toArray(Integer[]::new); 
		StringBuffer result = new StringBuffer();
		
		Arrays.sort(num, new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				if(i1==0) return 1;
	    		if(i2==0) return -1;
	    		
				double b1 = Math.log10(i1);
	    		double b2 = Math.log10(i2);
	    		b1 = Math.floor((b1-Math.floor(b1))*Math.pow(10, 14))/Math.pow(10, 14);
	    		b2 = Math.floor((b2-Math.floor(b2))*Math.pow(10, 14))/Math.pow(10, 14);
	    		
	    		if(b1 > b2) return -1;
	    		else if(b1 < b2) return 1;
	    		else return i1.compareTo(i2);
			}
		});
		
		for(Integer i : num) {
			double d = Math.log10(i);
			System.out.println(i +" , "+d +", " + Math.floor((d-Math.floor(d))*Math.pow(10, 14))/Math.pow(10, 14));
		}
		
		for(Integer i : num) result.append(i);
	
		return result.toString();
		//실행한 결괏값 95343[03]이(가) 기댓값 95343[30]와(과) 다릅니다.
	}
	
	
	public static void main(String[] args) throws IOException {
    	
		int[] a = {3, 6,30,303, 34,10, 5, 9,0,1004,91,1};
//		int[] a = {0,0,0,0,0};
		int[][] b = {{2, 5, 3}, {4, 4, 1}, {1, 7, 3}};
		
		String result = bigNo(a);
			
		System.out.println(result);
	}
}

class Solution15 {
	
	public static int hIndex(int[] citations) {
        int answer = 0;
        
        Arrays.sort(citations);
        
        for(int i=citations.length; i>=1; i--) {
        	int cnt = 0;
        	for(int j=citations.length-1; j>=0; j--) {
        		if(i<=citations[j]) cnt++;
        	}
        	if(cnt >= i) {
        		answer = i;
        		break;
        	}
        }

        return answer;
    }
	
	
	public static void main(String[] args) throws IOException {
    	
		int[] a = {5, 5, 5, 5};
//		int[] a = {0, 1, 3, 5, 6, 6};
		int result = hIndex(a);
			
		System.out.println("result : "+result);
	}
}

class Solution16 {
	
	public static int bridgeTruck(int bridge_length, int weight, int[] truck_weights) {
		int answer = 0, i=0;
		
        Queue<Integer> qu = new ArrayDeque<>();
        for(int a=0;a<bridge_length;a++) qu.add(0);
        
        do{
        	answer++;
        	if(i>=truck_weights.length) {
        		weight+=qu.poll();
//        		System.out.println(qu);
        		continue;
        	}
        	
        	if(qu.size() >= bridge_length) weight+=qu.poll();
        	if(weight >= truck_weights[i]) {
        		qu.add(truck_weights[i]);
        		weight-=truck_weights[i];
        		i++;
        	} else {
        		qu.add(0);
        	}
        	
//        	System.out.println(qu);
        	
        } while(qu.size()>0);
        
//        if(bridge_length>truck_weights.length) 
//        	answer+=bridge_length-truck_weights.length;
        
        return answer;
    }
	
	public static void main(String[] args) throws IOException {
		int a =100;	
		int b=100;	
		int[] c ={7,4,5,6};
//		int[] c ={10};
		int result = bridgeTruck(a,b,c);
		System.out.println("result : "+result);
	}
}

class solution17 {
	
	public static int[] top(int[] heights) {
        
        
        Stack<Integer> result = new Stack<>();
        
        for(int i=heights.length-1;i>0;i--) {
        	for(int j=i-1;j>=0;j--) {
        		if(heights[i]<heights[j]) {
        			result.push(j+1);
        			break;
        		}
        		if(j==0) result.push(0);
        	}
        }
        result.push(0);
        
        int[] answer = new int[result.size()];
        int i=0;
        while(!result.empty()) {
        	answer[i++] = result.pop();
        }
        
        return answer;
    }
	
	public static void main(String[] args) throws IOException {

		int[] a = {6,9,5,7,4};
		int result[] = top(a);
		for(int i : result) System.out.println("result : "+i);
	}
}

class solution18 {
	public static int[] funcDev(int[] progresses, int[] speeds) {
		
		int day=0;
		int[] days = new int[speeds.length];
		List<Integer> cnt = new ArrayList<Integer>();
		
		for(int i=0; i<progresses.length; i++) {
			days[i] = (int)Math.ceil(((double)(100-progresses[i])/speeds[i]));
//			System.out.println(days[i]);
		}
		
		for(int i : days) {
			if(day < i) {
				day = i;
				cnt.add(1);
			} else {
				int lIdx = cnt.size()-1;
				cnt.set(lIdx, cnt.get(lIdx)+1);
			}
		}

//		for(int a:cnt) System.out.println("r : "+a);
		int [] answer = cnt.stream().mapToInt(Integer::intValue).toArray();
        
        return answer;
    }
	
	public static void main(String[] args) throws IOException {

		int[] a = {93,30,55};
		int[] b = {1,30,5};
		int result[] = funcDev(a,b);
		for(int i : result) System.out.println("result : "+i);
	}
}

class solution19 {
	 public static int printDoc(int[] priorities, int location) {
	        
        Queue<Integer[]> qu = new ArrayDeque<>();
        Integer iDoc[] = Arrays.stream(priorities).boxed().toArray(Integer[]::new); 
        
        for(int i=0; i<priorities.length; i++) {
        	qu.add(new Integer[]{priorities[i], location==i?1:0});
        }
        Arrays.sort(iDoc, Collections.reverseOrder());
        
//	        for(Integer[] i :qu) System.out.println(i[0]+","+i[1]);
//	        for(Integer i :iDoc) System.out.println(i);
        
        int idx = 0;
        while(!qu.isEmpty()) {
        	if(qu.peek()[0]<iDoc[idx]) {
        		qu.add(qu.poll());
        	}else if(qu.peek()[0]==iDoc[idx]) {
        		if(qu.peek()[1]==1) break;
        		qu.poll();
        		idx++;
        	}
        }

        return idx+1;
    }
	 
	 public static int printDoc2(int[] priorities, int location) {
	        
	        Queue<Integer[]> qu = new ArrayDeque<>();

	        for(int i=0; i<priorities.length; i++) {
	        	qu.add(new Integer[]{priorities[i], location==i?1:0});
	        }
	        Arrays.sort(priorities);
	        
	        for(Integer[] i :qu) System.out.println(i[0]+","+i[1]);
	        for(Integer i :priorities) System.out.println(i);
	        
	        int idx = priorities.length-1;
	        while(!qu.isEmpty()) {
	        	if(qu.peek()[0]<priorities[idx]) {
	        		qu.add(qu.poll());
	        	}else if(qu.peek()[0]==priorities[idx]) {
	        		if(qu.peek()[1]==1) break;
	        		qu.poll();
	        		idx--;
	        	}
	        }

	        return idx+1;
	    }
	
	 
	public static void main(String[] args) throws IOException {

//		int[] a = {2,1,3,2};
		int[] a = {1, 1, 9, 1, 1, 1};
//		int b = 2;
		int b = 0;
		int result = printDoc2(a,b);
		System.out.println("result : "+result);
	}
}

class solution20 {
	
	public static int ironPipe(String arrangement) {
		
        int answer = 0, fold =0;
        arrangement = arrangement.replaceAll("\\(\\)", "|");
        
        for(char ch : arrangement.toCharArray()) {
        	if(ch=='(') {
        		fold++;
        	} else if(ch==')') {
        		fold--;
        		answer++;
        	} else {
        		answer+=fold;
        	}
        }
        return answer;
        
    }
	public static void main(String[] args) throws IOException {

		String a = "()(((()())(())()))(())";
		int result = ironPipe(a);
		System.out.println("\nresult : "+result);
	}
}

class solution21 {
	public static int[] stocks(int[] prices) {

		int[] answer = new int[prices.length];
        
        for(int i=0; i<prices.length; i++) {
        	int sec=0;
        	for(int j=i+1; j<prices.length; j++) {
        		System.out.println(prices[i]+", "+prices[j]);
        		sec++;
        		if(prices[i]>prices[j]) break;
        	}
        	answer[i]=sec;
        }
        return answer;
    }
	
	public static void main(String[] args) throws IOException {

		int[] a = {1, 2, 3, 2, 3};
		int[] result = stocks(a); //4, 3, 1, 1, 0
		System.out.println();
		for(int i : result) System.out.print(" "+i);
		
	}
}

class solution22 {
	 public static int moreHot(int[] scoville, int K) {
        int answer = 0;
        ArrayList<Integer> l = new ArrayList<>();
        for(int i:scoville) l.add(i);
        for(int i :l) System.out.print(i+" ");
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        
        Collections.sort(l);
                
        while(l.size()>1) {
//        	 for(int i :l) System.out.print(i+" ");
//        	 System.out.println();
        	if(K>l.get(0)) {
        		int temp = l.get(0)+(l.get(1)*2);
        		l.remove(1);
        		l.remove(0);
        		
        		if(l.size()<1) l.add(temp);
        		else {
	        		for(int i=0;i<l.size();i++) {
	        			if(l.get(i)>=temp) {
	        				l.add(i, temp);
	        				break;
	        			}
	        		}
        		}
        		answer++;
        	} else break;
        }
       
        if(l.get(0) < K) answer=-1;
        
//        System.out.println();
//        for(int i :l) System.out.print(i+" ");
//        System.out.println();
        return answer;
    }
	
	 public static int moreHot2(int[] scoville, int K) {
        int answer = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        for(int i:scoville) pq.add(i);
        
        while(pq.size()>1) {
//	        	System.out.println();
//	        	for(Object i : pq.toArray()) System.out.print(i+" ");
        	
        	if(K>pq.peek()) {
        		int temp = pq.poll() + (pq.poll()*2);
        		pq.add(temp);
        		answer++;
        	} else break;
        }
       
        if(pq.peek() < K) answer=-1;
        
//	        System.out.println();
//	        for(int i :l) System.out.print(i+" ");
//	        System.out.println();
        return answer;
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
//		int[] scoville = {1, 3, 2, 9, 10, 12, 1, 3, 2, 9, 10, 12};
//		int[] scoville = {1,2,100};
		int[] scoville = {0,0,0,0,3};
		int k = 7;
		int result = moreHot2(scoville,k); //2
		 System.out.println("\n result : "+result);
		 long et = System.currentTimeMillis();
		 System.out.println((et - st)+" ms");
		
	}
}

class solution23 {
	public static int noodleFactory(int stock, int[] dates, int[] supplies, int k) {
        int cnt=0,day=1;
        
        HashMap<Integer, Integer> map = new HashMap<>();
        Queue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        
        for(int i=0;i<dates.length;i++) map.put(dates[i], supplies[i]);
        
        while(stock<=k-1) {
        	pq.clear();
        	for(;day<=stock;day++) {
        		if(map.get(day)!=null) pq.add(map.get(day));
        	}
        	
//        	for(int i:pq) System.out.println(day+" day = "+i);
        	
        	for(int i:pq) {
        		if(stock>k-1) break;
        		stock+=i;
        		cnt++;
        	}
        }
        
//        for(int i : map2.values()) System.out.println(i);
        
        return cnt;
    }
	
	public static int noodleFactory2(int stock, int[] dates, int[] supplies, int k) {
        int cnt=0,day=0;
        
        Queue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        
        for(int i=0; i<k; i++) {
        	
        	if(day<dates.length && dates[day]==i) {
        		pq.add(supplies[day++]);
        	}
        	
        	if(stock==0) {
        		stock+=pq.poll();
        		cnt++;
        	}
        	
        	stock--;
        }
        return cnt;
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
//		int stock = 4;
//		int[] dates = {4,10,15};
//		int[] supplies = {20,5,10};
//		int k = 30;
		
		int stock = 10;
		int[] dates = {5,10};
		int[] supplies = {10,100};
		int k = 100;
		
//		int stock = 4;
//		int[] dates = {1,2,3,4};
//		int[] supplies = {1,1,1,1};
//		int k = 6; //2
			
		int result = noodleFactory2(stock,dates,supplies,k);
		
		System.out.println("\n result : "+result);
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
		
	}
}

class solution24 {
	
	public static int disk(int[][] jobs) {
		
        int answer=0, idx=0, ms=0;
        Queue<int[]> pq = new PriorityQueue<int[]>((i1,i2)->i1[1]-i2[1]);
        Arrays.sort(jobs, (i1, i2) -> i1[0] - i2[0]);
        
        while(idx<jobs.length || !pq.isEmpty()) {
//        	System.out.println(ms+","+idx);
        	while(idx<jobs.length && ms>=jobs[idx][0]) {
        		pq.add(jobs[idx++]);
        	}
        	
        	if(pq.isEmpty()) {
        		ms = jobs[idx][0];
        	} else {
        		int[] job = pq.poll();
        		ms += job[1];
        		answer += ms - job[0];
        	}
        	
        	System.out.println(ms +", "+idx);
        }
        
        return answer/jobs.length;
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
//		int[][] jobs = {{0, 3}, {1, 9}, {2, 6}}; //9
		int[][] jobs = {{0, 3}, {1, 9}, {500, 6}}; //6
			
		int result = disk(jobs);
		
		System.out.println("\n result : "+result);
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
		
		
		Queue<HashMap<String,Object>> pq = new PriorityQueue<>((o1,o2)->((Integer) o1.get("test")).compareTo((Integer) o2.get("test")));
		
	}
}

class solution25 {
	public static int[] DoublePriorityQueue(String[] operations) {
        int[] answer = new int[2];
        Queue<Integer> pqAsc = new PriorityQueue<>();
        Queue<Integer> pqDesc = new PriorityQueue<>((i1,i2)->i2.compareTo(i1));
        
        for(String s: operations) {
        	String[] as = s.split(" ");
        	
        	if(as[0].equals("I")) {
        		pqAsc.offer(Integer.valueOf(as[1]));
        		pqDesc.offer(Integer.valueOf(as[1]));
        	} else {
        		if(Integer.valueOf(as[1]) > 0) {
        			pqAsc.remove(pqDesc.poll());
        		}else {
        			pqDesc.remove(pqAsc.poll());
        		}
        	}
        }
        answer[0] = pqDesc.peek()==null ? 0 : pqDesc.peek();
        answer[1] = pqAsc.peek()==null ? 0 : pqAsc.peek();
        
        return answer;
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
//		String[] a = {"I 7","I 5","I -5","D -1"}; //7,5
//		String[] a = {"I 16","D 1"}; //0,0
		String[] a = {"I 7","I 7","I 5","I -5","D -1","D 1"}; //5,5
			
		int[] result = DoublePriorityQueue(a);
		
		System.out.println("\n result : "+result[0]+","+result[1]);
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution26 {
	public static int[] mockExam(int[] answers) {
        int[][] pattern = {{1, 2, 3, 4, 5}
        				  ,{2, 1, 2, 3, 2, 4, 2, 5}
        				  ,{3, 3, 1, 1, 2, 2, 4, 4, 5, 5}};
        int[] cnt = new int[3];
        
        List<Integer> l = new ArrayList<>();
        
        for(int i=0;i<answers.length;i++) {
        	if(answers[i]==pattern[0][i%5]) cnt[0]++;
        	if(answers[i]==pattern[1][i%8]) cnt[1]++;
        	if(answers[i]==pattern[2][i%10]) cnt[2]++;
        }
        
        if(cnt[0]>=cnt[1]&&cnt[0]>=cnt[2]) l.add(1);
        if(cnt[1]>=cnt[0]&&cnt[1]>=cnt[2]) l.add(2);
        if(cnt[2]>=cnt[0]&&cnt[2]>=cnt[1]) l.add(3);
        
        int size = l.size();
        int[] answer = new int[size];
        for(int i=0; i<size;i++) answer[i]=l.get(i);
        return answer;
        
//        return answer.stream().mapToInt(i -> i).toArray();
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
//		int[] a = {1,2,3,4,5}; //1
		int[] a = {1,3,2,4,2}; //1,2,3
			
		int[] result = mockExam(a);
		
		for(int i:result) System.out.println("result:"+i);
		
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution27 {
	
	static HashSet<Integer> set = new HashSet<>();
	static ArrayList<String> arr = new ArrayList<>();
	
	public static int decimal(String numbers) {
        int answer = 0;
        
        for(char ch: numbers.toCharArray()) {
        	arr.add(String.valueOf(ch));
        }
        
        int size = arr.size();
        
        for(int i=1; i<=size; i++) {
        	ArrayList<String> arrList = new ArrayList<>();
	        boolean visit[] = new boolean[size];
	        permutation(arrList, visit, size, i);
        }

        for(int i: set) {
        	System.out.println(i +","+check(i));
        	if(check(i)) answer++;
        }
        
        return answer;
    }
	
	//순열
	public static void permutation(ArrayList<String> arrList, boolean[] visit, int n, int r) {

        if(arrList.size() == r) {
        	StringBuffer sb = new StringBuffer();
            for(String s : arrList) sb.append(s);
            set.add(Integer.valueOf(sb.toString()));
        }else {
            for(int i=0; i<n; i++) {
                if(!visit[i]) {
                    visit[i] = true;
                    arrList.add(arr.get(i));
                    permutation(arrList, visit, n, r);
                    arrList.remove(arrList.size()-1);
                    visit[i] = false;
                }
            }
        }
    }
	
	public static int decimal2(String numbers) {
        int answer = 0;
        HashSet<Integer> set = new HashSet<>();
        
        permutation("",numbers,set);
        
        for(int i:set) System.out.println(i);
        for(int i:set) if(check(i)) answer++;
        
        return answer;
    }
	
	//순열2
	public static void permutation(String prefix, String str, HashSet<Integer> set) {
		int n = str.length();
		if(prefix.length()>0) set.add(Integer.valueOf(prefix));
		
		for(int i=0; i<n; i++) {
			permutation(prefix+str.charAt(i)
						,str.substring(0,i) + str.substring(i+1, n)
						,set);
		}
	}
	
	//소수 체크
	static Boolean check(int num) {
		if(num<2) return false;
		
		for(int i=2;i<num;i++) {
			if(num%i==0 && num!=i) return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
		String a = "17"; //3
//		String a = "011"; //2
			
		int result = decimal2(a);
		
		System.out.println("result:"+result);
		
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution28 {
	public static int baseBall(int[][] baseball) {

        HashSet<String> set = new HashSet<>();
        HashSet<String> setDel = new HashSet<>();
        
        for(int i=1; i<=9;i++) {
        	for(int j=1; j<=9;j++) {
        		for(int k=1;k<=9;k++) {
        			if(i!=j&&j!=k&&i!=k) set.add(""+i+j+k);
        		}
        	}
        }
        
        for(int[] bb :baseball) {
    		
    		String number = String.valueOf(bb[0]);
    		for(String s : set) {
	    		int sCnt=0, bCnt=0;
	    		for(int i=0;i<3;i++) {
	    			if(s.charAt(i)==number.charAt(i)) sCnt++;
	    			
	    			for(int j=0;j<3;j++) {
	    				if(i!=j && s.charAt(i)==number.charAt(j)) bCnt++;
	    			}
	    		}
	    		if(bb[1] != sCnt || bb[2] != bCnt ) setDel.add(s);
    		}
        }
        set.removeAll(setDel);
        
        for(String s : set) System.out.println(s);
        
        return set.size();
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
		int[][] a = {{123, 1, 1}, {356, 1, 0}, {327, 2, 0}, {489, 0, 1}};
			
		int result = baseBall(a);
		
		System.out.println("result:"+result);
		
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution29 {
	
	public static int[] carpet(int brown, int yellow) {
        int[] answer = new int[2];
        int tempB=brown/2-2;
        
        for(int i=1,j=tempB-1; i<=tempB/2; i++,j--) {
        	if(i*j==yellow) {
        		answer[0] = j+2;
        		answer[1] = i+2;
        		break;
        	}
        }
        return answer;
    }
	
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
		int brown = 20;
		int yellow = 16;
			
		int[] result = carpet(brown,yellow);
		
		for(int i : result) System.out.println("result:"+i);
		
		
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution30 {
	public static int GymSuit(int n, int[] lost, int[] reserve) {
	    int answer = 0;
	    int[] cnt = new int[n+1];
	    for(int i=1;i<cnt.length;i++) cnt[i]=1;
	    for(int i:reserve) cnt[i]++;
	    for(int i:lost) cnt[i]--;
	    
	    for(int i=1;i<cnt.length;i++) {
	    	if(cnt[i]>1) {
	    		if(i-1>0 && cnt[i-1]==0) {
	    			cnt[i-1]++;
	    			cnt[i]--;
	    		}else if(i+1<cnt.length && cnt[i+1]==0) {
	    			cnt[i+1]++;
	    			cnt[i]--;
	    		}
	    	}
	    }
	    
	    for(int i: cnt) System.out.println(i);
	    
	    for(int i: cnt) if(i>0) answer++;
	    
	    return answer;
	}
	public static void main(String[] args) throws IOException {
		long st = System.currentTimeMillis();
		
//		int n = 5;
//		int[] lost = {2,4};
//		int[] reserve = {1,3,5};
		
		int n = 5;
		int[] lost = {2,4};
		int[] reserve = {3};
			
		int result = GymSuit(n,lost,reserve);
		
		System.out.println("result:"+result);
		
		
		long et = System.currentTimeMillis();
		System.out.println((et - st)+" ms");
	}
}

class solution31 {
	public static int joyStick(String name) {
        int answer = 0;
        StringBuffer sb = new StringBuffer();
        
        for(char ch:name.toCharArray()) {
        	int i = ch-65;
        	if(i<=13) answer += i;
        	else answer += Math.abs(26-i);
        	sb.append("A");
        }
        
        if(!name.equals(sb.toString())) {
        	while(name.startsWith("A")) {
        		String temp = name+name.substring(0, 1);
        		name = temp.substring(1, name.length()+1);
        	}
        }
        
        
        
        while(sb.length()>0) {
    		if(name.indexOf(sb.toString())>-1) {
    			break;
    		} else sb.deleteCharAt(0);
    	}
        
        System.out.println(sb.toString());
        
        
        answer += name.length()-sb.length()-1;
        
//        abcdefghijklmn
//         opqrstuvwxyz
        
        return answer;
    }
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
        BigDecimal b1 = new BigDecimal(sc.next());
        BigDecimal b2 = new BigDecimal(sc.next());
        
        System.out.println(b1.add(b2));
        System.out.println(b1.multiply(b2));
	}
}

class solution32 {
	
	static int libraryFine(int d1, int m1, int y1, int d2, int m2, int y2) {
		
		int result = 0;
		
		if(y1>y2) result = 10000;
		else if(y1==y2) {
			if(m1>m2) result = (m1-m2)*500;
			else if(m1==m2){
				if(d1>d2) result = (d1-d2)*15;
			}
		}
		
		return result;
    }
	
	public static void main(String[] args) throws IOException {
		int d1 = 2, m1=7, y1=1014, d2=1, m2=1, y2=1015;
        
        int i = libraryFine(d1, m1, y1, d2, m2, y2);
        System.out.println(i);
        
	}
}

class solution33 {
	
	static void bonAppetit(List<Integer> bill, int k, int b) {
		
		int sum = 0;
		
		bill.remove(k);
		for(Integer i : bill) {
			sum += i;
		}
			
		int refund = b-(sum/2);
		
		if(refund==0) System.out.println("Bon Appetit");
		else System.out.println(refund);
		
    }
	
	public static void main(String[] args) throws IOException {
		List<Integer> li = new ArrayList<Integer>();
		li.add(3);li.add(10);li.add(2);li.add(9);
        
        bonAppetit(li,1,7);
        
        
	}
}

class solution34 {
	
	public static int pickingNumbers(List<Integer> a) {
		
		int[] cnt = new int[a.size()];
		
		for(int i=0; i<a.size(); i++) {
			for(int j=0; j<a.size(); j++) {
				if(i!=j) {
					if(Math.abs(a.get(i)-a.get(j)) < 1) cnt[i]++;
				}
			}
		}
		Arrays.sort(cnt);
		
		return cnt[cnt.length-1]+1; 
    }
	
	public static void main(String[] args) throws IOException {
		List<Integer> li = new ArrayList<Integer>();
		li.add(1);li.add(2);li.add(2);li.add(3);li.add(1);li.add(2);
        
        int i = pickingNumbers(li);
        System.out.println(i);
        
	}
}

class solution35 {
	
	public static int[] solution(int n, int m) {
       
      
        int gcd = gcd(n,m);
        
        System.out.println(gcd);
        System.out.println(n*m/gcd);
        
        int[] answer = {gcd,n*m/gcd};
        
        return answer;
    }
	
	public static int gcd(int n,int m) {
		 while(m!=0) {
	        	int i = n%m;
	        	n=m;
	        	m=i;
	        	System.out.println(n+","+m);
	        }
		 return n;
	}
	
	public static void main(String[] args) throws IOException {
		
		Solution sol = new Solution();
		int[] a = new int[2];
		a[0] = 1;
		a[1] = 2;
		
		for(int b : a) System.out.println(b);
		
//		System.out.println(3%12);
//		System.out.println(12%3);
        
//		int[] i = solution(42,12);
//        System.out.println(i[0]+","+i[1]);
        
	}
}