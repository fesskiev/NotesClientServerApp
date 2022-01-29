# NotesClientServerApp

Main idea of this project is use the same modern technologies for development both client and server side. 
The app contains three modules: client, server and common. Client module is an Android app that consists of registration,
login and main screen where user can perform typical CRUD operations. Android app developed with 
a declarative UI and unidirectional architecture. The Server module is a backend app that supports CRUD operations too via REST API,
also supports JWT Authentication. The common module is shared code that uses both client and server apps.

### Tech
* [Kotlin] - main programming language
* [Ktor] -  Ktor is an asynchronous framework for creating microservices, web applications, and more
* [Coroutines] - solution for asynchronous programming
* [Jetpack Compose] - declarative UI toolkit
* [Jetpack Compose Navigation] - navigation between composables
* [Jetpack Compose Paging] - Compose pagination library
* [Koin] - DI framework
* [Exposed] - ORM framework
* [H2] - database


### Run Server
 Android Studio\
open 'Edit Run/Debug configuration' dialog\
click 'Kotlin'\
click the "+" button\
In 'Name' enter any server name\
'Use classpath of module' choose 'NotesClientServerApp.server'\
'Main class' choose 'ApplicationKt'\

add 'STORAGE_DIR_PATH' to 'Environment field'\

click 'Apply'



### License
MIT

[Kotlin]: <https://kotlinlang.org/>
[Ktor]: <https://ktor.io/>
[Coroutines]: <https://kotlinlang.org/docs/reference/coroutines-overview.html>
[Jetpack Compose]: <https://developer.android.com/jetpack/compose>
[Jetpack Compose Navigation]: <https://developer.android.com/jetpack/compose/navigatione>
[Jetpack Compose Paging]: <https://developer.android.com/topic/libraries/architecture/paging/v3-overview>
[Koin]: <https://insert-koin.io/>
[Exposed]: <https://github.com/JetBrains/Exposed>
[H2]: <https://www.h2database.com/html/main.html>

