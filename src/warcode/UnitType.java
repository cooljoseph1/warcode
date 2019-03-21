package warcode;

public enum UnitType {
	CASTLE(100, 250, 0, 500, 0, 0, 0, 100, 0), PEASANT(15, 0, 100, 25, 0, 0, 5, 100, 0),
	ARCHER(20, 50, 0, 50, 10, 150, 4, 180, 0), MAGE(30, 100, 0, 80, 25, 100, 2, 100, 2),
	KNIGHT(25, 100, 0, 150, 30, 4, 10, 150, 0);
	public final int CONSTRUCTION_GOLD, CONSTRUCTION_WOOD, RESOURCE_CAPACITY, INITIAL_HEALTH, ATTACK_DAMAGE,
			ATTACK_RADIUS, MOVEMENT_SPEED, VISION_RADIUS, SPLASH_RADIUS;

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

	public static UnitType fromString(String string) {
		switch (string) {
		case "C":
			return CASTLE;
		case "P":
			return PEASANT;
		case "A":
			return ARCHER;
		case "M":
			return MAGE;
		case "K":
			return KNIGHT;
		default:
			throw new RuntimeException("Unit type's string must be one of \"C\", \"P\", \"A\", \"M\", \"K\"");
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case CASTLE:
			return "C";
		case PEASANT:
			return "P";
		case ARCHER:
			return "A";
		case MAGE:
			return "M";
		case KNIGHT:
			return "K";
		default:
			return "X";
		}

	}
}
