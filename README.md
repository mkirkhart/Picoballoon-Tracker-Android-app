# Picoballoon Tracker app - Android

This app, along with the Picballoon Tracker Interface Device (TID), can be used for monitoring transmissions from a Picoballoon Tracker.

## What is a Picoballon Tracker?

About one year ago, I started designing a small tracking device meant to log simple atmospheric data, such as barometric pressure and temperature. Unlike a typical radiosonde, this device is light enough (25 g total mass, including the battery) to be flown using a large (30 inch or larger) foil party balloon obtainable for $10 to $20 from a party supply store. One of the goals of this project was to use this device as a scientific experiment for school children to answer the question of what happens to air temperature and pressure as altitude increases. The device, which I call a “Picoballoon Tracker”, is equipped with a GPS receiver, barometric pressure sensor, temperature sensor, and an RFM22B ISM band radio configured to operate in the 70 cm (433 MHz) amateur radio band. Once powered, it periodically transmits its 3D position as well as atmospheric pressure and temperature using the RFM22B. A groundstation receives these transmissions, and can display them as well as log them.

This app is based on the Nordic Semiconductor nRF UART Android app (version 2.0.1), and uses the Nordic UART Service to transfer Picoballoon Tracker data from the TID to the Android device.

This app was developed as an enhancement to the original Arduino UNO/SparkFun RFM22 shield based groundstation.  Moreover, it was developed to satisfy the requirements of the University of Michigan - Dearborn course CIS535 - Programmable Mobile/Wireless Technologies and Pervasive Computing, which was taken during the Winter 2018 semester.

This source code can be compiled with Android Studio version 3.1.2 and Gradle.

### Note
- Android 4.3 or later is required (API level 18)
- Android Studio supported (version 3.1.2 - recently updated to version 3.4.1)
