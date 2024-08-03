package vn.attendance.util;

/**
 * API Constants
 */
public class APIConstants {
    /**
     * Common
     */
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 400;

    public static final int ERROR_NOT_PRODUCT_GACHA = 304;
    public static final int ERROR_NOT_GACHA_YOU_CAN_SHOOT = 305;
    public static final String ERROR_NOT_AIRDROP_NOT_PRODUCT = "309";
    public static final int ERROR_GACHA_SERVER = 500;
    public static final int ERROR_CODE_TRANSACTION = 306;
    public static final int ERROR_CODE_TRANSACTION_NOT_PRODUCT_GACHA = 307;
    public static final int ERROR_CODE_TRANSACTION_NOT_DEPOSIT = 308;
    public static final int ERROR_BAD_REQUEST_CODE = 400;
    public static final String REPLACE_CHAR = "%s";
    public static final String SUCCESS = "SUCCESS";

    public static final String ERROR_UNKNOWN_MSG = "Unknown error";
    public static final String USER_INACTIVE_MSG = "User has not been activated";
    public static final String TOKEN_INVALID_MSG = "Invalid token";
    public static final String ERROR_PRODUCT_MSG = "Product";
    public static final String ERROR_CATEGORY_MSG = "Category";
    public static final String ERROR_CREATOR_MSG = "Creator";
    public static final String ERROR_PRODUCT_DETAIL = "Product detail";
    public static final String ERROR_NOT_USER_REGISTER_AIRDROP = "不正な要求を受信するユーザーがいないため、エラー送信 NFT が失敗しました。";
    public static final String ERROR_NOT_PRODUCT_AIRDROP= " NFTの数が不足しました。";
    public static final String ERROR_PRODUCT_ATTRIBUTES_MSG = "Product attributes";
    public static final String ERROR_PRIVATE_KEY_MSG = "Private key";
    public static final String ERROR_MASTER_DATA_MSG = "Master data";
    public static final String ERROR_POINT_PURCHASE_PRODUCT_MSG = "Master data";
    public static final String ERROR_IDENTITY_MSG = "Identity data";
    public static final String ERROR_INVALID_DATE_MSG = "Invalid date";
    public static final String ERROR_MAINTENANCE_MSG = "Maintenance name";
    public static final String ERROR_USER_INFO_REQ_FLG = "Information required flag code";
    public static final String ERROR_ARTIST_MSG = "Artist";
    public static final String ERROR_ARTIST_GROUP_MSG = "Artist group";
    public static final String ERROR_SUPPORTER_MSG = "Supporter";
    public static final String ERROR_MPA_MSG = "Master Product Attribute";

    public static final String BANNER = "Banner";
    public static final String REFRESH_TOKEN_IS_INVALID = "The refresh token has invalid";
    public static final String NOT_FOUND_MESSAGE = "%s not found";
    public static final String NOT_FOUND_AT_MESSAGE = "%s not found: ";
    public static final String USER = "User";
    public static final String SUB_ADMIN = "Sub admin";
    public static final String SPONSOR = "Sponsor";
    public static final String LICENSE = "License";
    public static final String USER_ID_INPUT_IS_INVALID = "The input user id has invalid";
    public static final String PASSWORD_INPUT_IS_INVALID = "Invalid password”";
    public static final String INVALID_FORMAT_MESSAGE = "%s is invalid format";
    public static final String ERROR_INVALID_EXPIRED_MESSAGE = "The token has invalid";
    public static final String ERROR_FROM_TO_DATE_MESSAGE = "The fromDate or toDate not null";
    public static final String EMAIL = "Email";
    public static final String CAN_NOT_DELETE_MESSAGE = "%s can't delete";
    public static final String S3_OBJECT = " S3's object";
    public static final String S3_BUCKET = " S3's bucket";
    public static final String GET_TRANSACTION_ERROR_MESSAGE = "Get transaction error with message: %s";
    public static final String TRANSACTION = "Transaction";
    public static final String GET_FAIL_STATUS_MESSAGE = "Get %s with status fail";
    public static final String GET_FAIL_MESSAGE = "Get %s fail";
    public static final String TOKEN_ID = "Token id";
    public static final String HISTORY_ORDER = "History order";
    public static final String PRODUCT_ID = "Product id";
    public static final String TRANSACTION_VALUE_IS_NOT_EQUAL_PRODUCT_PRICE_MESSAGE = "Transaction value is not equal Product price";
    public static final String PRODUCT_PRICE = "Product price";
    public static final String TRANSACTION_VALUE = "Transaction value";
    public static final String ERROR_PRIVATE_KEY_NOT_FOUND_MESSAGE = "Private key not found";
    public static final String ERROR_NEW_PASSWORD_NOT_MATCH_CONFIRM_PASSWORD = "New password is not same as confirm password";
    public static final String ERROR_NAME_AND_VALUE_MASTER_DATA_NOT_NULL = "Name and Value is not null";
    public static final String ERROR_INVALID_FORMAT_FILE = "Invalid format file";
    public static final String ERROR_EXISTED_MESSAGE = "%s is existed";
    public static final String ERROR_MUST_BE_DECIMAL = "%s must be decimal format";
    public static final String DATA_INPUT = "Data input";
    public static final String MASTER_PUBLIC_KEY = "Master public key";
    public static final String SET_APPROVE_FOR_ALL = "Set approve for all";
    public static final String ERROR_USER_BUY_WHEN_PRODUCT_SOLD = "User call transaction when product is sold";
    public static final String ERROR_SYSTEM_NOT_HAVE_AVAILABLE_USER_PUBLIC_ADDRESS = "System hadn't available user public address for order";
    public static final String ERROR_USER_VERIFY_SIGNATURE_FAIL = "User verify signature fail";
    public static final String TARGET_GAME = "Target game";
    public static final String ERROR_NOT_PLAYER_TYPE = "User is not player";
    public static final String ERROR_ALREADY_FRIEND = "Already friend";
    public static final String ERROR_ALREADY_SENT_FRIEND_REQUEST = "Already sent friend request";
    public static final String ERROR_SEND_YOURSELF_FRIEND_REQUEST = "Can not send request to yourself";
    public static final String ERROR_SPONSOR_MSG = "Sponsor";
    public static final String ERROR_CSV_TO_POINT_PURCHASE_PRODUCT = "Invalid format at row: %s";
    public static final String ERROR_USER_JOINED_ANOTHER_TEAM = "%s joined another team";
    public static final String ERROR_PASSWORD_LENGTH_VIOLATION = "Password has at least %s characters";
    public static final String USER_CANCEL_TRANSACTION = "User cancel transaction";
    public static final String ERROR_PLAYER_CREATE_TEAM_IS_NOT_LEADER = "Player creating the team must be the leader";
    public static final String ERROR_USER_CAN_ONLY_UPDATE_HIS_PROFILE = "User can only update his profile";
    public static final String ERROR_USER_WITHDRAW = "Bank_code or Branch_code exist";

    // Validate user's input info
    public static final String ERROR_USER_INPUT_DATA_INVALID = "Username and email are required";
    public static final String ERROR_FIELD_REQUIRED_VALUE = "The field [%s] is required";
    public static final String SPONSOR_SHIP = "Sponsor Ship";
    public static final String ERROR_TRANSACTION_306 = "There is currently a transaction going on, please come back later";
    public static final String ERROR_TRANSACTION_NOT_PRODUCT_GACHA_307 = "Error: There are currently no gacha products";
    public static final String ERROR_TRANSACTION_NOT_DEPOSIT_308 = "Error: You have not deposited money for the spin";
    public static final String ERROR_TRANSACTION_RECHARGED_400 = "ERROR_TRANSACTION_RECHARGED_400";
    public static final String ERROR_NOT_PRODUCT_GACHA_304 = "Error: There are currently no gacha products 304";
    public static final String ERROR_NOT_PRODUCT_GACHA_305 = "You do not spin them any items";
    public static final String ERROR_NOT_SERVER_GACHA_500 = "Gacha error nodejs gacha server 500";
    public static final String ERROR_NOT_SERVER_AIRDROP_500 = "Airdrop nodejs error server 500";

    //validate user data
    public static final String ERROR_USER_NOT_FOUND = "User not found";

    //validate role data
    public static final String ERROR_ROLE_ALREADY_EXISTED = "Role already existed";
    public static final String ERROR_ROLE_USER_NOT_FOUND = "Role user not found";
    public static final String ERROR_ROLE_USER_ALREADY_EXISTED = "Role user already existed";
    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";

    //validate job title data
    public static final String ERROR_JOB_TITLE_NOT_FOUND = "Job title not found";

    //validate level data
    public static final String ERROR_LEVEL_NOT_FOUND = "Level not found";

    //validate Scenario Group Skill
    public static final String ERROR_SCENARIO_GROUP_SKILL_DUPLICATE = "Duplicate group skill in this scenario";
    public static final String ERROR_ACTION_MODIFY_GROUP_SKILL_EMPTY = "Action can't be null or empty";

    //validate Scenario
    public static final String ERROR_SCENARIO_ID_NOT_EXISTS = "ScenarioId not exists";

    //validate Customer
    public static final String ERROR_CUSTOMER_NOT_EXISTS = "Customer not exists";

    //validate Language
    public static final String ERROR_LANGUAGE_NOT_EXISTS = "Language not exists";

    //validate question
    public static final String ERROR_QUESTION_NOT_FOUND = "Question not found";

    //user time interview
    public static final String ERROR_USER_TIME_INTERVIEW_NOT_FOUND = "User time interview not found";

    //question high, medium, low
    public static final String ERROR_QUESTION_NUMBER_EXCEED = "%s number is exceed %s";
    public static final String ERROR_TOTAL_QUESTION_IS_ZERO = "total question (high + medium + low)/ group skill > 0";
    public static final String ERROR_TOTAL_QUESTION_IN_RANGE = "%s <= total question scenario <= %s";

}