# Stock Exchange - Android client

## About
This application emulates a small stock exchange where only two currencies are traded.
Active orders to buy or sell an abstract currency are displayed in the form of an order book, based on the data of which it is possible to create your own order. All orders are relevant until the end of the day, after which they become inactive.

Since it is a demo application, it does not have a user base, so orders are generated automatically to emulate trading. Generation takes place once every 15 minutes.

Trades are made by the server not instantly, but about once every 15 seconds, taking into account the time of receipt of orders.

## 🌐 Backend
This is the client part of the whole project, which is implemented as an Android application. Backend part [can be found here](https://github.com/CRaFT4ik/dc-stock-exchange-backend).

## 📽️ Preview
[![Watch the video](https://user-images.githubusercontent.com/1649292/159302965-31a425ef-e5eb-40aa-99e8-94c278e2da50.png)](https://youtu.be/R3nkqxwXhls)

## 📦 APK download
You can download the APK [on the releases page](https://github.com/CRaFT4ik/dc-stock-exchange-android/releases/).

## 📊 Project features
- Only 2 currencies are available: balance (represented in $) and trading subject
- Trading in minus is allowed, user is not limited in funds
- No limits on size or amount of trades

## 🚀 Features of Android implementation
- A lot of attention is paid exactly to the architecture. The application is built with pure architecture principles in mind. Each layer is placed in a separate module (app, domain, data) to be more expressive. Dependencies are strictly defined.
- Jetpack Compose is used to build the UI (for the order book Custom View with its own data model is implemented).
- Each UI component is inherited from Material-components and redesigned to fit the application's needs
- MVVM pattern is used to separate business logic from user view. There is no direct dependency on the ViewModel on each screen, allowing the individual UI components to be tested independently
- The user is able to choose the application theme
- Data on the user screen is updated automatically
- An animated splash-screen is displayed on Android 12+

## 📚 Application stack
- Kotlin with coroutines
- Clean architecture
- Koin
- Jetpack Compose with Compose Navigation
- Custom designed UI-components
- MVVM
- OkHTTP & Retrofit 2 & Moshi
- Room
- Datastore preferences
- JUnit

## ⚙️ ToDo list
- To implement saving the query cache. For this we already have everything
- Organize error collection and displaying in the most common way
- Switch to webhooks
- Provide test coverage
