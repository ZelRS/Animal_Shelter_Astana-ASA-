package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения callBackData кнопок
 */
@Getter
public enum CallbackData {
    BUT_WANT_DOG("dogs_but"),
    BUT_WANT_CAT("cats_but"),

    BUT_SEND_REPORT("send_report_but"),

    BUT_CALL_VOLUNTEER("call_volunteer_but"),
    BUT_GET_FULL_INFO("get_full_button_info_but"),
    BUT_TAKING_PET("taking_pet_but"),
    BUT_CARE_PET_REC("care_pet_rec_but"),
    BUT_START_REGISTRATION("start_registration_but"),
    BUT_FILL_OUT_REPORT_ON("but_fill_out_report_on"),
    BUT_FILL_OUT_REPORT_OFF("but_fill_out_report_off"),
    BUT_SET_DATA_FROM_USER("but_set_data_from_user"),

    // навигационные кнопки для модуля информации о приюте
    BUT_MORE_INFORMATION("but_more_information"),
    BUT_GO_TO_MAIN("but_go_to_main"),
    BUT_GO_TO_SHELTER_SELECT("but_go_to_shelter_select");

    private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
