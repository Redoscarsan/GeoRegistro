The following repository has all the necessary tools and verification that an Android app might need.
The repository and the source code was mainly done with the use of A.I, more on that on DECLARACION_IA.md.
The app is able to properly store the fields typed by the user in the Firebase Database, to see the coordinates of the phone as well as google maps (google maps was not able to be implemented fully due to technical problems),
and test sensors.

App info:
        - Android Version: 37
        - Minimum SDK: 27
        - Target SDK: 37
        - Language: Java/Kotlin
        - Java version: 11
        - Implementations:
          Firebase,
          SplashScreen,
          google-services-location
          google-services-maps
Firebase info:
        - Firestore rules:
            rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
        - Database Rules:
          {
  "rules": {
    ".read": false,
    ".write": false
  }
}


Instructions:
  After opening the app, the main screen will be shown, which has the form, a save button, a button for the map layout and a button for the sensor layout.
  <img width="353" height="635" alt="image" src="https://github.com/user-attachments/assets/d062d918-8a9e-4b3a-8831-38694ff74dd6" />

  The map layout (note that google maps implementation does not work due to a problem when adding the API key to google cloud services. But the implementation is fully functional).
  <img width="330" height="738" alt="image" src="https://github.com/user-attachments/assets/63dcfd14-7808-492d-8380-342f1b6b581e" />

  The sensor layout.

  <img width="318" height="743" alt="image" src="https://github.com/user-attachments/assets/c94c89a2-0148-4327-a0b7-83e4ba17375b" />

