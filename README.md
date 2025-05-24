# 🚨 HelAlert App

**HelAlert App** is an Android-based emergency alert system designed to work with a smart helmet. When a collision is detected, the app automatically sends alerts and live location to the user's emergency contacts. It also includes features for storing important documents and setting bike servicing reminders.

---

## 📱 Features

### ✅ Welcome Page
- Prompts user to:
  - Enable Bluetooth
  - Grant permissions for location, SMS, and calls
- Displays the app logo and a "Get Started" button

---

### 🆘 Emergency Contacts
- Allows the user to save up to 5 emergency contacts in priority order
- Contacts are stored locally and used in case of emergencies

---

### 🚨 Collision Detection
- Receives a signal from the smart helmet (via Bluetooth)
- Displays a notification:
  - “Hey, are you safe?”
  - Option:
     “Yes, I am safe
- If no response in 1 minute:
  - Sends an emergency SMS with live location to all contacts

---

### 📁 Document Storage
- Allows users to store links to important documents in the app
  - Driving License (DL)
  - Registration Certificate (RC)
  - Bike Insurance Policy
  - Pollution Under Control (PUC) Certificate
  - Medical Certificate
- Easy access to all documents from the app

---

### 🔧 Bike Servicing Reminder
- User can enter a time interval for bike servicing (e.g., every 2 months)
- The app sets a reminder notification when the servicing time is due

---

## 📡 Integration with Smart Helmet
- Designed to receive collision alerts via Bluetooth from a smart helmet using Arduino-based sensors

---

## ⚙️ Technologies Used
- Android Studio (Java)
- Android SDK
- Bluetooth communication
- Location services (GPS)
- SMS and call permissions

---

## 🔒 Permissions Required
- `ACCESS_FINE_LOCATION`
- `SEND_SMS`
- `BLUETOOTH_CONNECT`
- `BLUETOOTH_ADMIN`

---

## 📷 Screenshots


---

## 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/PranavRaut062006/HelAlertApp.git

