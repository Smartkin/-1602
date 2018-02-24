//Main class
public class Palindrome {
	//Program's entry point
	public static void main(String[] args)
	{
		//Loop for grabing all console parameters
		for(int i=0;i<args.length;i++)
		{
			String s = args[i];
			//Output depending on whether the string is a palondrome
			if (isPalindrome(s))
			{
				System.out.println(s+" is a palindrome");
			}
			else
			{
				System.out.println(s+" is not a palindrome");
			}
		}
	}
	//String reversal function
	public static String reverseString(String s)
	{
		String temp = "";
		for(int i=s.length()-1;i>=0;i--)
		{
			temp += s.charAt(i);
		}
		return temp;
	}
	//Checking a string by comparing it to its reverse equivalent
	public static boolean isPalindrome(String s)
	{
		return s.equals(reverseString(s));
	}
}