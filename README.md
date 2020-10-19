# P8 Tourguide
TourGuide is a Spring Boot application and a key part of the TripMaster application portfolio. It allows users to see which tourist attractions are nearby and get discounts on hotel stays and tickets to various shows.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

This project need GpsApi & RewardCentralApi
https://github.com/obaocr/P8_GpsApi.git
https://github.com/obaocr/P8_RewardCentralApi.git

Pleas Install and run the two project below

For Tourguide (main project)
- Java 1.8
- Gradle 5.6.2
- SpringBoot 2.2.5


### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

3.Spring

https://spring.io

### Documentation : Architecture applicative TourGuide

![UML diagram](/TourGuide/img/Archi_appli_tourguide.JPG)

### Documentation : Architecture globale

![UML diagram](/TourGuide/img/Archi_appli_globale.JPG)

### Testing

The app has unit tests and performances tests written.

To run the tests from gradle launch the gradle test task or the build, il will generate the test report with jacoco


### Other consideration
JAVADOC has been initialized and needs to be completed.
