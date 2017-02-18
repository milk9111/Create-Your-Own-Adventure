package backend;

public class Enemy extends Player {

	public Enemy () {
		this ("Enemy", 15, 5, 11);
	}
	
	public Enemy (final String theName, final int theArmorRating, final int theHealth, final int theDamage) {
		super (theName, theArmorRating, theHealth, theDamage);
	}

}
