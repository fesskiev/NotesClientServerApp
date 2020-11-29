package com.fesskiev

object Routes {
    const val REGISTRATION = "/users/registration"
    const val LOGIN = "/users/login"
    const val LOGOUT = "/users/logout"

    const val GET_NOTES = "/notes/get"
    const val ADD_NOTE = "/notes/add"
    const val EDIT_NOTE = "/notes/edit"
    const val DELETE_NOTE = "/notes/delete"
}

object HTTPParameters {
    const val NOTE_TITLE = "note_title"
    const val NOTE_DESCRIPTION = "note_description"
    const val NOTE_PICTURE_URL = "note_picture_url"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val DISPLAY_NAME = "display_name"
}

object Headers {
    const val SESSION = "Session"
    const val CONTENT_TYPE = "Content-Type"
    const val AUTHORIZATION = "Authorization"
}

object Database {
    const val CLIENT_URL = "jdbc:h2:/data/data/com.fesskiev.compose/data/hello;FILE_LOCK=FS;PAGE_SIZE=1024;CACHE_SIZE=8192"
    const val SERVER_URL = "jdbc:h2:./server/notes"
}

object ServerErrorCodes {
    const val EMAIL_EMPTY = 0
    const val DISPLAY_NAME_EMPTY = 1
    const val PASSWORD_EMPTY = 2
    const val EMAIL_ALREADY_USE = 3
    const val USER_NOT_FOUND = 4
    const val PASSWORD_INCORRECT = 5
    const val USER_NOT_CREATED = 6
    const val SESSION_NOT_FOUND = 7
    const val NOTE_NOT_ADDED = 8
    const val NOTE_EMPTY = 9
    const val NOTE_TITLE_EMPTY = 10
    const val NOTE_DESCRIPTION_EMPTY = 11
    const val INTERNAL_SERVER_ERROR = 12
}
