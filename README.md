## NotesClientServerApp
Main idea of this project is use the same modern technologies for development both client and server side. 
The app contains three modules: client, server and common. Client module is an Android app that consists of registration,
login and main screen where user can perform typical CRUD operations. Android app is developed with 
a declarative UI and unidirectional architecture. Server module is a backend app that supports CRUD operations too via REST API,
also supports JWT Authentication. Common module is shared code that uses both client and server apps.

### Tech
* [Kotlin] - main programming language
* [Ktor] -  Ktor is an asynchronous framework for creating microservices, web applications, and more
* [Coroutines] - solution for asynchronous programming
* [Jetpack Compose] - declarative UI toolkit
* [Koin] - DI framework
* [Exposed] - ORM framework
* [H2] - database

### License
MIT

[Kotlin]: <https://kotlinlang.org/>
[Ktor]: <https://ktor.io/>
[Coroutines]: <https://kotlinlang.org/docs/reference/coroutines-overview.html>
[Jetpack Compose]: <https://developer.android.com/jetpack/compose>
[Koin]: <https://insert-koin.io/>
[Exposed]: <https://github.com/JetBrains/Exposed>
[H2]: <https://www.h2database.com/html/main.html>