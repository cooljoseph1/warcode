package warcode;

public final class SPECS {
	public static final UnitType Castle = 	new UnitType(100, 	250, 	0, 		500, 	0, 		0, 		0, 		100, 	0);
	public static final UnitType Peasant = 	new UnitType(10, 	25, 	100, 	25, 	0, 		0, 		5, 		100, 	0);
	public static final UnitType Archer = 	new UnitType(15, 	50, 	0, 		50, 	10, 	150, 	4, 		180, 	0);
	public static final UnitType Mage = 	new UnitType(30, 	100, 	0, 		80, 	25, 	100, 	2, 		100, 	2);
	public static final UnitType Knight = 	new UnitType(25, 	100, 	0, 		150, 	30, 	4, 		10, 	150, 	0);

	public static final int MAX_RESOURCES = 100;
	public static final int MINE_AMOUNT = 10;
	public static final int WOOD_AMOUNT = 10;
	
	public static final int MAX_MAP_SIZE = 100;
	public static final int MIN_MAP_SIZE = 30;
}

final class UnitType {
	final int CONSTRUCTION_GOLD, CONSTRUCTION_WOOD, RESOURCE_CAPACITY, INITIAL_HEALTH, ATTACK_DAMAGE, ATTACK_RADIUS,
			MOVEMENT_SPEED, VISION_RADIUS, SPLASH_RADIUS;

	UnitType(int constructionGold, int constructionWood, int resourceCapacity, int initialHealth, int attackDamage,
			int attackRadius, int movementSpeed, int visionRadius, int splashRadius) {
			CONSTRUCTION_GOLD = constructionGold;
			CONSTRUCTION_WOOD = constructionWood;
			RESOURCE_CAPACITY = resourceCapacity;
			INITIAL_HEALTH = initialHealth;
			ATTACK_DAMAGE = attackDamage;
			ATTACK_RADIUS = attackRadius;
			MOVEMENT_SPEED = movementSpeed;
			VISION_RADIUS = visionRadius;
			SPLASH_RADIUS = splashRadius;
	}
}
