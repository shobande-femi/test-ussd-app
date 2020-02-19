### Sample USSD App

Demonstrates how to build a USSD app using
[Menufactory](https://github.com/shobande-femi/menufactory)

This sample app also shows how easy it is to switch between gateways.

In `App.kt`, choose which gateway implementation you want by uncommenting one or
the other:
* `AfricasTalkingUssdApp.main()` to run your app using Africa's Talking gateway
implementation
* `HubTelUssdApp.main()` to run your app using Hubtel's gateway implementation


After selecting gateway implementation, run `./gradlew run` to start the app.

Procfile also included for deploying to heroku.
Deploy the app to Heroku, register its url in the portal of whatever Gateway
you choose and view the magic!