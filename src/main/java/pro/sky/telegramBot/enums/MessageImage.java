package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения пути к конкретному медиа-контенту
 */
@Getter
public enum MessageImage {
    FIRST_TIME_WELCOME_MSG_IMG("/photos/first_time_welcome_msg.jpg"),
    SORRY_WELCOME_MSG_IMG("/photos/sorry_welcome_msg.jpg"),
    BLOCKED_WELCOME_MSG_IMG("/photos/blocked_welcome_msg.jpg"),
    CAT_SHELTERS_MSG_IMG("/photos/cat_shelters_msg.jpg"),
    DOG_SHELTERS_MSG_IMG("/photos/dog_shelters_msg.jpg"),
    SHELTER_DEFAULT_PREVIEW_MSG_IMG("/photos/shelter_default_preview_msg.jpg"),
    TAKING_PET_MSG_IMG("/photos/taking_pet_msg.jpg"),
    CARE_DOG_REC_MSG_IMG("/photos/care_dog_rec_msg.jpg"),
    CARE_CAT_REC_MSG_IMG("/photos/care_cat_rec_msg.jpg"),
    TWO_OPTIONS_SEND_REPORT_MSG_IMG("/photos/two_options.jpg"),
    ONE_OPTION_SEND_REPORT_MSG_IMG("/photos/one_option.jpg"),
    SHELTER_INFORMATION_MSG_IMG("/photos/shelter_information.jpg"),
    REPORT_ACCEPTED_MSG_IMG("/photos/report_accepted.jpg"),
    REPORT_NOT_ACCEPTED_MSG_IMG("/photos/report_not_accepted.jpg"),
    CALL_VOLUNTEER_MSG_IMG("/photos/volunteer.jpg"),
    SAVING_USER_INFO_SUCCESS_MSG_IMG("/photos/saving_user_info_success_msg.jpg"),
    QUESTION_FOR_REPORT_IMG("/photos/question_ror_report.jpg"),
    HELLO_VOLUNTEER_IMG("/photos/hello_volunteer.jpg"),
    CHOOSE_SHELTER_IMG("/photos/choose_shelter.jpg"),
    SAVING_USER_PERSONAL_DOCS_SCREENS_MSG_IMG("/photos/saving_user_personal_docs_screens_msg.jpg"),
    NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG("/photos/saving_user_personal_docs_screens_msg.jpg"),
    REMINDER_FOR_ADOPTER_ABOUT_REPORTING_IMG("/photos/reminder_for_adopter_about_reporting.jpg"),
    MISSING_PET_IMG("/photos/missing_pet.jpg"),
    NOTIFICATION_PROBLEM_IMG("/photos/notification_problem.jpg"),
    NOTIFICATION_TRY_YOUR_BEST_IMG("/photos/notification_try_your_best.jpg"),
    NOTIFICATION_GOOD_JOB_IMG("/photos/notification_good_job.jpg"),
    NOTIFICATION_CALCULATION_ERROR_IMG("/photos/notification_calculation_error.jpg");



    private final String path;

    MessageImage(String path) {
        this.path = path;
    }
}
