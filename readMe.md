📱 Passwordless Authentication App (Email + OTP)
Overview

This Android application implements a passwordless authentication flow using Email + OTP, followed by a session tracking screen that displays login duration.
All logic is implemented locally, with no backend dependency, as per the assignment requirements.

The app is built using:

Kotlin

Jetpack Compose (Material 3)

MVVM architecture

Coroutines & StateFlow

Firebase Analytics for event logging

1. OTP Logic and Expiry Handling
OTP Generation

OTPs are generated locally using SecureRandom.

Each OTP is a 6-digit numeric value.

val otp = (secureRandom.nextInt(900000) + 100000).toString()

Why SecureRandom?

Provides better unpredictability than basic pseudo-random generation.

Lightweight and suitable for a local-only implementation.

Improves security reasoning without overengineering.

Expiry Handling

OTP expiry duration: 60 seconds

Each OTP stores its creation timestamp.

Expiry is checked by comparing the current system time with the stored timestamp.

currentTime - createdAt > 60_000L

Attempt Handling

Maximum attempts per OTP: 3

Attempts are decremented only on incorrect OTP validation

Attempts are reset only when a new OTP is generated (resend)

OTP expiry does not reset attempts automatically

Resend OTP Behavior

Resend OTP is allowed at any time, even during countdown.

Resending:

Invalidates the previous OTP

Resets attempts to 3

Restarts the expiry timer

This behavior is aligned with realistic authentication flows and complies fully with the assignment requirements.

2. Data Structures Used and Why
OTP Storage
MutableMap<String, OtpData>


Key: Email address

Value: OtpData (OTP value, creation time, remaining attempts)

Why Map?

Ensures OTPs are stored per email, as required.

Allows efficient lookup and overwrite when resending OTP.

Prevents multiple valid OTPs for the same email.

OtpData Model
data class OtpData(
    val otp: String,
    val createdAt: Long,
    var attemptsLeft: Int
)


This structure keeps all OTP-related state grouped and easy to reason about.

3. External SDK Used and Why
Chosen SDK: Firebase Analytics
Why Firebase Analytics?

Explicitly allowed in the assignment.

Requires no backend logic.

Ideal for logging authentication events.

Widely used in production Android apps.

Logged Events

OTP generated

OTP validation success

OTP validation failure

OTP resend

Logout

Architecture Decision

Firebase calls are wrapped inside a dedicated AnalyticsLogger class.

ViewModel interacts only with the abstraction, not the SDK directly.

This ensures:

Clean separation of concerns

Easier testing

No UI or SDK coupling

4. GPT Usage vs My Own Understanding & Implementation
How GPT Was Used

GPT was used as:

A reference and brainstorming assistant

For validating architectural decisions

For checking best practices in Compose, MVVM, and StateFlow

GPT was not used for blind copy-paste development.

What I Fully Understood and Implemented Myself
Core Business Logic

OTP generation using SecureRandom

OTP expiry calculation

Attempt tracking and reset rules

Resend OTP invalidation logic

Architecture

MVVM with unidirectional data flow

StateFlow for UI state

SharedFlow for one-time UI events (Toasts)

ViewModel lifecycle handling

Timer survival across recompositions

UI (Jetpack Compose)

Screen-based Compose architecture

Reusable CommonScreen scaffold

Card-based layouts for clarity

Disabled button states for validation

Real-time OTP countdown display

Session duration timer (mm:ss)

Display of session start time

All UI decisions were intentionally kept simple and clean, respecting the 6–7 hour time constraint, while still demonstrating Compose fundamentals and good UX practices.

What GPT Helped With (Guidance Level)

Suggesting improvements like SecureRandom

Identifying common Compose pitfalls

Reviewing flow edge cases

Helping structure the README clearly

Every suggested change was understood, evaluated, and then implemented manually.

UI Design Justification (Timebox Aware)

Material 3 components only

No custom themes or animations

Reusable layout wrapper (CommonScreen)

Focus on clarity over decoration

This approach balances:

Professional appearance

Readability

Realistic expectations for a 6–7 hour assignment

Conclusion

This project demonstrates:

Clear understanding of OTP-based authentication

Strong state management using modern Android tools

Clean, testable architecture

Practical UI decisions under time constraints

Responsible and transparent use of AI assistance