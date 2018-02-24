//Main class
public class Primes {
	//Program's entry point
	public static void main(String[] args)
	{
		//Loop for checking numbers from 2 to 100
		for(int i=2;i<100;i++)
		{
			//Function call to check a number
			if (isPrime(i))
			{
				//Output
				System.out.println("Number "+i+" is prime"+"\n");
			}
		}
	}
	   
	//Function to check a prime number
	public static boolean isPrime(int n)
	{
		for(int i=2;i<n;i++)
		{
			if (n%i == 0)
				return false;
		}
		return true;
	}
}