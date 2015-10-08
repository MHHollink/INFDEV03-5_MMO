package nl.meh.mmo.server.server.database;

/**
 * This is a class which holds all the table/column names
 * It is nothing more than a placeholder...
 */
public class DatabaseSettings {
    // TABLES
    public static final String CHARACTER_TABLE_NAME = "characters";
    public static final String CHARACTER_SKILLS_TABLE_NAME = "character_skills";
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_OWNS_CHARACTER_TABLE_NAME = "users_owns_character";
    public static final String SERVER_TABLE_NAME = "servers";
    public static final String SERVER_CONTAINS_CHARACTER_TABLE_NAME = "server_contains_character";

    // SERVER
    public static final String SERVER_COLUMN_ADDRESS = "address";
    public static final String SERVER_COLUMN_NAME = "name";
    public static final String SERVER_COLUMN_LOCAL = "location";
    public static final String SERVER_COLUMN_MAX_USERS = "max_users";
    public static final String SERVER_COLUMN_CUR_USERS = "current_users";
    public static final String SERVER_COLUMN_ACTIVE = "active";

    // USER
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_FIRST_NAME = "first_name";
    public static final String USER_COLUMN_LAST_NAME = "last_name";
    public static final String USER_COLUMN_SLOTS = "character_slots";
    public static final String USER_COLUMN_BALANCE = "balance";
    public static final String USER_COLUMN_LAST_PAYED = "last_payment";
    public static final String USER_COLUMN_DAYS_LEFT = "days_left";
    public static final String USER_COLUMN_IBAN = "iban";
    public static final String USER_COLUMN_PASSWORD = "password";

    // CHARACTER
    public static final String CHARACTER_COLUMN_NAME = "name";
    public static final String CHARACTER_COLUMN_GENDER = "gender";
    public static final String CHARACTER_COLUMN_BALANCE = "balance";
    public static final String CHARACTER_COLUMN_RACE = "race"; // NYI
    public static final String CHARACTER_COLUMN_PROFESSION = "profession"; // NYI

    // SKILLS
    public static final String CHARACTER_SKILLS_COLUMN_CHARACTER_NAME = "character_name";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_TOTAL = "total_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_COMBAT = "combat_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_SKILL = "skill_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_ATTACK = "attack_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_STRENGTH = "defence_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_DEFENCE = "strength_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_HIT_POINTS = "hitpoints_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_MINING = "mining_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_WOODCUTTING = "woodcutting_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_SMITHING = "smithing_level";
    public static final String CHARACTER_SKILLS_COLUMN_LEVEL_BARTERING = "bartering_level";

    // COUPLE TABLES
    public static final String UOC_USERNAME = "user_username";
    public static final String UOC_CHARACTER = "character_name";
    public static final String SCC_SERVER = "server_address";
    public static final String SCC_CHARACTER = "character_name";

    // no instances of this class!
    private DatabaseSettings(){};
}
