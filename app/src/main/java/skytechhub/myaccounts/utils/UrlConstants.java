package skytechhub.myaccounts.utils;

/**
 * Created by Nirav Patel on 11/25/2015.
 */
public class UrlConstants {
    public static String DOMAIN_NAME = "http://dev.geekyworks.com/healthcare/";

    public static String REGISTER = DOMAIN_NAME + "oauth/userRegister";
    public static String VERIFICATION = DOMAIN_NAME + "oauth/userVerification";
    public static String LOGIN = DOMAIN_NAME + "oauth/login";
    public static String FORGOT_PASSWORD = DOMAIN_NAME + "oauth/forgotPassword";
    public static String CHANGE_PASSWORD = DOMAIN_NAME + "users/changePassword";
    public static String EDIT_PROFILE = DOMAIN_NAME + "users/editProfile";
    public static String GET_EDIT_PROFILE = DOMAIN_NAME + "users/getEditProfile";

    public static String GET_MH_BASIC_INFO = DOMAIN_NAME + "users/getBasicDetails";
    public static String SET_MH_BASIC_INFO = DOMAIN_NAME + "users/updateBasicDetails";

    public static String GET_MH_COMPLAINT_DETAILS = DOMAIN_NAME + "users/getComplainDetails";
    public static String SET_MH_COMPLAINT_DETAILS = DOMAIN_NAME + "users/updateComplainDetails";

    public static String GET_MH_REGULAR_DETAILS = DOMAIN_NAME + "users/getRegularDetails";
    public static String SET_MH_REGULAR_DETAILS = DOMAIN_NAME + "users/updateRegularDetails";

    public static String GET_QUERY_LIST = DOMAIN_NAME + "users/getSecondQueryListForUsers";
    public static String ADD_QUERY = DOMAIN_NAME + "users/addSecondQuery";

    /*dev.geekyworks.com/healthcare/users/getDoctorList/44*/
    public static String GET_DOCTOR_LIST_BY_SPECIALITY = DOMAIN_NAME + "users/getDoctorList";
    public static String GET_SPECIALITY = DOMAIN_NAME + "users/getSpecilalist";
    public static String GET_PHSYCOLOGIST = DOMAIN_NAME + "our_panel/getDoctorsPanel/1";
//    dev.geekyworks.com/healthcare/our_panel/getDoctorsPanel/1

    public static String GET_SESSION_LIST = DOMAIN_NAME + "users/getBookedListUsers";
    public static String BOOK_SESSION = DOMAIN_NAME + "users/bookYourSession";

    public static String GET_PROFILE_DATA = DOMAIN_NAME + "users/getEditProfile/";
    /*http://dev.geekyworks.com/healthcare/users/getEditProfile/8*/
    public static String GET_DOCUMENT_LIST = DOMAIN_NAME + "users/getDocumentList";

    /*users/getMyCondition/8*/
    public static String GET_MY_CONDITION_LIST = DOMAIN_NAME + "users/getMyCondition/";
    public static String ADD_CONDITION = DOMAIN_NAME + "users/addMyCondition";

    /*dev.geekyworks.com/healthcare/users/getNicknameList/4*/
    public static String GET_USERS_LIST = DOMAIN_NAME + "users/getNicknameList/";

    /*http://dev.geekyworks.com/healthcare/users/uploadDocument*/
    public static String UPLOAD_FILE = DOMAIN_NAME + "users/uploadDocument";
}
