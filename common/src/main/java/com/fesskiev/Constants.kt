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
    const val NOTE_TEXT = "note_text"
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
