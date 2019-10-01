package mastermind;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Startup {
    private static char[] colors = {'r','b','y','g','o','p'};
    private static Map<Character,Boolean> colorMap = new HashMap<>();
    private String randomstr;
    public Startup(){
        super();
        for(char c:colors){
            colorMap.put(c,true);
        }
        setcolors();
    }
    private  void setcolors(){
        Random random = new Random();
        int num = colors.length;
        Set<Integer> set = new LinkedHashSet<Integer>();
        while (set.size() < 4){
            set.add(random.nextInt(num));
        }
        Iterator<Integer> iterator = set.iterator();
        int[] indexarr = new int[4];
        int point = 0;
        while (iterator.hasNext()){
            indexarr[point++] = iterator.next();
        }
        randomstr = new String( new char[]{
            colors[indexarr[0]],
            colors[indexarr[1]],
            colors[indexarr[2]],
            colors[indexarr[3]]
        });
    }
	private int[] calculate(String guess) throws Exception{
        checkIn(guess);
		int[] res = new int[2];
		Map<Character, AtomicInteger> map = new HashMap<Character, AtomicInteger>();
		for(int i = 0; i < 4; i++) {
			if(randomstr.charAt(i) == guess.charAt(i)) {
				res[0]++;
			}else {
				if(map.get(guess.charAt(i)) == null){
					map.put(guess.charAt(i),new AtomicInteger(1));
				}else{
					map.get(guess.charAt(i)).addAndGet(1);
				}
			}
		}
		for(int i = 0; i < 4; i++){
			if(randomstr.charAt(i) != guess.charAt(i) && map.get(randomstr.charAt(i)) != null && map.get(randomstr.charAt(i)).get() > 0){
				res[1]++;
				map.get(randomstr.charAt(i)).decrementAndGet();
			}
		}
		return res;
	}
	private void checkIn(String guess) throws Exception{
        if(guess == null || guess.length() != 4){
            throw new Exception("Wrong proposed combination length");
        }
        checkInColors(guess);
        checkInRepeated(guess);
    }
    private void checkInColors(String guess) throws Exception{
        for(int i =0; i < guess.length();i++){
            if(colorMap.get(guess.charAt(i)) == null){
                throw new Exception("Wrong colors, they must be: rbygop");
            }
        }
    }
    private  void checkInRepeated(String str) throws Exception{
        Set<Character> set = new HashSet<>();
        char[] chars = str.toCharArray();
        for(char c:chars) {
            set.add(c);
        }
        if(set.size() < str.length()){
            throw new Exception("Repeated colors");
        }
    }
    private void printRecords(){
        System.out.println(guessRecords.size()+" attempt(s):");
        System.out.println("****");
        for (String s : guessRecords){
            System.out.println(s);
        }
    }
    private boolean checkGameOver(int[] res){
        if(res[0] == 4 ){
            System.out.println("You've won!!! ;-)");
            return true;
        }
        if(guessRecords.size() >= 10){
            System.out.println("You've lost!!! :-(");
            return true;
        }
        return false;
    }
    private void clear(){
        guessRecords.clear();
        setcolors();
    }
    private static List<String> guessRecords = new ArrayList<>();
	public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] res = new int[0];
        Scanner sc = new Scanner(System.in);
        Startup st = new Startup();
        System.out.println("----- MASTERMIND -----");
        while (true){
            System.out.print("Propose a combination:");
            String guess = sc.next();
            try {
                res = st.calculate(guess);
                guessRecords.add(guess+" --> "+res[0]+" blacks and "+res[1]+" whites");
                st.printRecords();
                if(st.checkGameOver(res)){
                    while (true){
                        System.out.print("Do you want to continue? (s/n): ");
                        String s = sc.next();
                        if(s.equals("s")){
                            System.out.println("----- MASTERMIND -----");
                            st.clear();
                            break;
                        }else if(s.equals("n")){
                            System.exit(0);
                        }else {
                            System.out.println("Invalid input");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
	}

}
class Exception extends java.lang.Exception{
    private String message;
    public Exception(String str){
        super(str);
        message = str;
    }
    public String getMessage(){
        return this.message;
    }
}