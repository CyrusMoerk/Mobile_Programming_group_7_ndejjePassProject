# NdejjePass Android App

NdejjePass is a Kotlin/Jetpack Compose Android prototype for student tuition clearance at Ndejje University. It includes role-aware login, payment submission, admin payment approval, and clearance progression toward permit eligibility.

## Current Functional Scope

- Student login and dashboard
- Tuition payment submission (`mtn`, `airtel`, `bank`)
- Admin pending-payment review (approve/reject)
- Clearance percentage recalculation on approval
- Profile editing for allowed fields and password updates

## Demo Credentials (Seeded On First Run)

Password for all demo accounts: `password123`

- Student: `student@stud.ndejjeuniversity.ac.ug`
- Admin: `admin@ndejjeuniversity.ac.ug`
- Security: `security@ndejjeuniversity.ac.ug`

## Tech Stack

- Kotlin + Jetpack Compose
- AndroidX Navigation Compose
- Room (local persistence)
- MVVM with repository pattern

## Run Locally

1. Open the project in Android Studio.
2. Sync Gradle and let dependencies download.
3. Run the `app` module on an emulator or device.

## Notes

- This project stores data locally using Room for prototype/demo purposes.
- Demo records are seeded only if they do not already exist.
