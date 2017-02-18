package backend;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Player {

	private String myName;
	
	private int myArmorRating;
	
	private int myHealth;
	
	private int myAttack;
	
	public Player () {
		this ("Player", 10, 10, 20);
	}
	
	public Player (final String theName, final int theArmorRating, final int theHealth, final int theAttack) {
		myName = theName;
		myArmorRating = theArmorRating;
		myHealth = theHealth;
		myAttack = theAttack;
	}
	
	public void receiveDamage (final int theDamage) {
		System.out.println("The damage: " + theDamage);
		System.out.println("The armor rating: " + myArmorRating);
		System.out.println();
		if (theDamage > myArmorRating) {
			System.out.println("myHealth: " + (theDamage - myArmorRating));
			myHealth -= (theDamage - myArmorRating);
		}
	}
	
	public int showDamageTaken (final int theDamage) {
		int damageTaken = 0;
		if (theDamage - myArmorRating > 0) {
			damageTaken = theDamage - myArmorRating;
		}
		
		return damageTaken;
	}
	
	public int attack () {
		int attackDamage;
		attackDamage = ThreadLocalRandom.current().nextInt(1, myAttack * 2);
		return attackDamage;
	}
	
	public int getArmorRating () {
		return myArmorRating;
	}

	public int getAttackDamage () {
		return myAttack;
	}
	
	public String getName () {
		return myName;
	}
	
	public int getHealthRemaining () {
		return myHealth;
	}

}
