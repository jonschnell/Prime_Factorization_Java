package edu.iastate.cs228.hw3;

/**
 *  
 * @author Jonathon Schnell
 * @version 1.0
 * @since 4-1-2020
 * COM S 228
 * homework3
 *
 */

public class PrimeTest {
	
	public static void main(String[] args) {
		/*
		PrimeFactorization a = new PrimeFactorization();
		a.add(5, 1);
		System.out.println(a.toString());
		System.out.println(a.value());
		 */
		long n = 25480;
		long o = 405;
		PrimeFactorization b = new PrimeFactorization(n);
		PrimeFactorization c = new PrimeFactorization(o);
		
		System.out.println("b = " + b.toString());
		System.out.println("c = " + c.toString());
		
		PrimeFactorization d = new PrimeFactorization();
		d.multiply(b, c);
		
		System.out.println("d = " + d.toString());
		System.out.println("d.val = " + d.value());
		

		System.out.println(b.remove(7, 1));
		//System.out.println(b.remove(5, 1));

		
		
		System.out.println("b.val = " + b.value());

		
		//remove is broken?
		System.out.println("b = " + b.toString());
		//PrimeFactor pf = new PrimeFactor(42, 42);
		
		b.add(5, 1);

		
		//b.add(pf);
		System.out.println("b = " + b.toString());
		
		System.out.println("b.contains 8 = " + b.containsPrimeFactor(8));
		System.out.println("b.contains 7 = " + b.containsPrimeFactor(7));


		System.out.println(b.value());
		System.out.println(b.size());
		b.clearList();
		System.out.println(b.size());
	}

}
