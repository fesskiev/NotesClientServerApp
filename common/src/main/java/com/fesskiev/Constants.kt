package com.fesskiev

val IMAGES_DIR_PATH: String = System.getenv("STORAGE_DIR_PATH")
val DATABASE_DIR_PATH: String = "jdbc:h2:" + System.getenv("STORAGE_DIR_PATH") + "database"

object Routes {
    const val REGISTRATION = "/users/registration"
    const val LOGIN = "/users/login"
    const val LOGOUT = "/users/logout"

    const val GET_NOTES = "/notes/get"
    const val ADD_NOTE = "/notes/add"
    const val ADD_NOTE_IMAGE = "/notes/image/add"
    const val EDIT_NOTE = "/notes/edit"
    const val DELETE_NOTE = "/notes/delete"
}

object HTTPParameters {
    const val NOTE_TITLE = "note_title"
    const val NOTE_DESCRIPTION = "note_description"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val DISPLAY_NAME = "display_name"
    const val PAGE = "page"
    const val NOTE_UID = "note_uid"
}

object Headers {
    const val SESSION = "Session"
    const val CONTENT_TYPE = "Content-Type"
    const val AUTHORIZATION = "Authorization"
}

object ServerErrorCodes {
    const val UNKNOWN_SERVER_ERROR = -1
    const val INTERNAL_SERVER_ERROR = 0
    const val EMAIL_EMPTY = 1
    const val DISPLAY_NAME_EMPTY = 2
    const val PASSWORD_EMPTY = 3
    const val EMAIL_ALREADY_USE = 4
    const val USER_NOT_FOUND = 5
    const val NOTE_ID_NOT_FOUND = 6
    const val NOTE_NOT_FOUND = 7
    const val PASSWORD_INCORRECT = 8
    const val USER_NOT_CREATED = 9
    const val SESSION_NOT_FOUND = 10
    const val NOTE_NOT_ADDED = 11
    const val IMAGE_NOT_ADDED = 12
    const val NOTE_EMPTY = 13
    const val NOTE_TITLE_EMPTY = 14
    const val NOTE_DESCRIPTION_EMPTY = 15
    const val EMAIL_INVALID = 16
    const val PASSWORD_INVALID = 17
}
