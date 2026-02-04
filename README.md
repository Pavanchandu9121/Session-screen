# 🔐Passwordless Authentication App (Email + OTP)

Tech Stack:
Kotlin · Jetpack Compose · MVVM · Coroutines · StateFlow · Firebase Analytics

Timebox:
6–7 Hours (as specified in the assignment PDF)


## 📌Project Overview

This Android application demonstrates a passwordless authentication flow using
Email + One-Time Password (OTP), followed by a session tracking screen.

All authentication logic is implemented locally, without any backend, strictly
following the assignment requirements.

The app focuses on:
- Correct OTP rules and edge cases
- Clean state management
- Realistic user experience
- Clear separation of UI and business logic


## 🎯Functional Requirements (As per PDF)

✅ Email + OTP Login
- User enters an email address
- User taps Send OTP
- A 6-digit OTP is generated locally
- User enters OTP to log in

✅ OTP Rules
- OTP length: 6 digits
- OTP expiry: 60 seconds
- Maximum attempts: 3
- OTP stored per email
- Generating a new OTP:
  - Invalidates the old OTP
  - Resets attempt count

✅Session Screen
After successful login:
- Displays session start time
- Displays live session duration (mm:ss)
- Provides Logout button

Timer behavior:
- Survives recompositions
- Stops correctly on logout


## 🔐 OTP Logic and Expiry Handling

OTP Generation
- OTPs are generated using SecureRandom
- Each OTP is a 6-digit numeric value
- This provides better unpredictability than basic random generation

OTP Expiry Handling
- Each OTP stores its creation timestamp
- Expiry is checked using system time
- OTP expires after 60 seconds

Attempt Handling
- Maximum attempts per OTP: 3
- Attempts are decreased only on incorrect OTP entry
- OTP expiry does not reduce attempts
- Attempts are reset only when a new OTP is generated (resend)

Resend OTP Behavior
- Resend OTP is allowed at any time, even during countdown
- Resending:
  - Invalidates the previous OTP
  - Resets attempts to 3
  - Restarts the expiry timer

This behavior is realistic and fully compliant with the assignment PDF.


## 🧱 Data Structures Used and Why

OTP Storage Structure
- MutableMap<String, OtpData>

Key:
- Email address

Value:
- OtpData (OTP value, creation time, remaining attempts)

Why Map?
- Ensures OTP is stored per email
- Prevents multiple valid OTPs for the same email
- Allows efficient overwrite when resending OTP
- Simple and easy to reason about

OtpData Model
- otp: String
- createdAt: Long
- attemptsLeft: Int


## 📊 External SDK Used

Chosen SDK:
✅ Firebase Analytics

Why Firebase Analytics?
- Explicitly allowed in the assignment
- No backend dependency
- Suitable for logging authentication events
- Commonly used in real Android applications

Events Logged:
- OTP generated
- OTP validation success
- OTP validation failure
- OTP resend
- Logout

🧠 Architecture Decision:
- Firebase calls are wrapped inside an AnalyticsLogger class
- ViewModel interacts only with this abstraction
- No direct Firebase calls in UI or ViewModel logic


## 🎨 Architecture and State Management

Architecture:
- MVVM (Model–View–ViewModel)
- One-way data flow
- Clear separation of UI and business logic

State Handling:
- StateFlow for UI state
- SharedFlow for one-time UI events (Toast messages)
- Timers are handled inside the ViewModel
- No UI logic inside ViewModel


## UI Design Justification (6–7 Hour Timebox)

- Built using Jetpack Compose with Material 3
- Card-based layouts for clarity
- Reusable CommonScreen scaffold
- Clear visual hierarchy
- No custom themes or heavy animations

The UI is intentionally simple and clean, focusing on correctness,
readability, and realistic user experience within the given timebox.


## 🖼️Screenshots and Demo

Screenshots (to be added):
- Login Screen
- OTP Verification Screen
- Session Screen

Example placeholders:
<p align="center">
    
<img src="https://github.com/user-attachments/assets/408ebf32-a9c7-4f89-8aa9-020cd8ac2f3f" alt="Login Screen" width="300"/>
<img src="https://github.com/user-attachments/assets/7e1ed4ca-580e-49a9-901d-e2d4aecf2870" alt="Login Screen" width="300"/>
<img src="https://github.com/user-attachments/assets/80c95166-009a-41f3-8880-1370b0645661" alt="Login Screen" width="300"/>

</p>
Demo Video (to be added):
[Watch Demo Video](videos/demo.mp4)


## 🤖 GPT Usage Disclosure

How GPT Was Used
- Used as a reference for best practices
- Helped validate architectural decisions
- Assisted in identifying edge cases
- Helped structure documentatio
- While implementing some UI components
- While preparing README File in Githib

🚫 What I Fully Understood and Implemented Myself
- SecureRandom-based OTP generation
- OTP expiry and attempt tracking logic
- Resend OTP behavior
- Session timer implementation
- MVVM architecture
- StateFlow and SharedFlow usage
- Jetpack Compose UI implementation
- Firebase Analytics integration
- UI layout and design decisions

All logic was understood, reviewed, and implemented manually.


## What This App Does NOT Do

- No backend usage
- No Firebase Authentication
- No global mutable state
- No blocking calls on main thread
- No UI logic inside ViewModel


## Conclusion

This project demonstrates:
- Secure local OTP handling
- Correct expiry and attempt tracking
- Clean MVVM architecture
- Modern Jetpack Compose UI
- Realistic user experience
- Responsible and transparent use of AI assistance

All requirements from the assignment PDF have been fully implemented
within the given 6–7 hour time constraint.


##📎Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Add google-services.json if Firebase Analytics is enabled
4. Build and run the app
