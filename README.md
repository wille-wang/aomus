# AOMUS

_**All-in-One for Melbourne University Students**_ (_**AOMUS**_, /ˈau̇-mər-s/) is an Android app designed to help new University of Melbourne students familiarize themselves with the campus. With AOMUS, users can:

- Explore campus locations with detailed introductions to places of interest
- Check in at specific locations by scanning QR codes
- Locate campus buildings using an interactive map
- Chat with a built-in chatbot for University-related queries
- Access the university library's online resources instantly
- Follow predefined routes for guided campus tours

![summary](./docs/img/summary.jpeg)

## Table of Contents

- [AOMUS](#aomus)
  - [Table of Contents](#table-of-contents)
  - [Architecture](#architecture)
    - [Database Management System (DBMS)](#database-management-system-dbms)
  - [Quick Links](#quick-links)
  - [Usage](#usage)
    - [For Developers](#for-developers)
    - [For End Users](#for-end-users)
      - [Permissions](#permissions)
      - [Physical Device](#physical-device)
      - [Virtual Device](#virtual-device)
  - [Development Conventions](#development-conventions)
    - [Committing](#committing)
    - [Branching](#branching)
  - [Acknowledgements](#acknowledgements)
  - [Contributors](#contributors)

## Architecture

The AOMUS architecture follows a client-server model, where the Android app interacts with the Firebase backend services for data storage, retrieval, and user authentication. The key components of the architecture include:

- **client (Android app)**:
  - provides a user interface for interactions such as viewing profiles, checking in at locations, and reading building information
  - features for QR code scanning and so forth
  - communicates with Firebase for all real-time data needs (e.g., check-ins, profiles)
- **server**:
  - **Firebase**
    - stores data in a JSON format, ensuring real-time updates across all connected clients
    - manages user profiles, building details, check-in data, and so forth
    - synchronizes data changes automatically in real time, allowing users to instantly see updates
  - **GitHub Models**
    - provides large language models for user queries, enabling intelligent, AI-powered responses
  - **Google Maps**
    - offers basic map-based and location-based services

![architecture](./docs/img/architecture.png)

### Database Management System (DBMS)

The following schema outlines the structure of the Firebase Realtime Database, a JSON-based NoSQL DBMS used by AOMUS. Data are stored in collections, with each node representing a distinct type of entity:

```
root/
├── buildings/
│   └── <buildingUUID>/
│       ├── code: `String`
│       ├── desc: `String`
│       ├── imgUrl: `String`
│       ├── latitude: `float`
│       ├── longitude: `float`
│       ├── name: `String`
│       └── year: `int`
├── routes/
│   └── <routeUUID>/
│       ├── buildings: `Array`
│       ├── length: `String`
│       ├── name: `String`
│       └── time: `String`
└── users/
    └── <userUUID>/
        ├── checkins/
        │   └── <buildingId>/
        │       ├── buildingCode: `String`
        │       ├── counts: `int`
        │       └── lastCheckIn: `long`
        ├── password: `String`
        ├── program: `String`
        ├── realName: `String`
        ├── schoolEmail: `String`
        └── username: `String`

```

- `buildings`: a collection of all buildings on the campus. Each building contains metadata like its name, location (latitude/longitude), description, and year of establishment.
- `routes`: a collection of predefined campus routes. Each route contains a list of buildings along the route, an estimated distance, a suggested completion time, and a descriptive route name.
- `users`: a collection of registered users. Each user has personal details such as username, email, and program information, with a checkins node to track the buildings they have visited, including the number of times checked in and the timestamp of the last visit.

## Quick Links

- 📅 [GitHub project](https://github.com/users/wille-wang/projects/12)
- 👨‍🎓 [Assignment specification](https://canvas.lms.unimelb.edu.au/courses/183251/assignments/494805)
- 👨‍💻 [Android developer guides](https://developer.android.com/guide)

## Usage

### For Developers

The recommended development environment includes (or compatible versions):

- [Android Studio Koala (2024.1.1)](https://developer.android.com/studio/)
- [JDK 17](https://www.oracle.com/java/technologies/downloads/)
- [Android SDK 26 (Android 8.0)](https://developer.android.com/tools/releases/platforms)

### For End Users

This app is optimized for:

- **Android version**: 8.0 (Oreo) or higher
- **Display size**: Medium, 1080 \* 2400 resolution
- **Screen density**: 420 dpi

#### Permissions

To enable all features, please grant the following permissions when prompted:

- **Internet**: for network-based functionalities
- **Camera**: for using the QR code scanner
- **Notifications**: for receiving timely alerts

#### Physical Device

<!-- TODO: Write the installation instructions on physical devices -->

#### Virtual Device

To set up a virtual device, clone this repository and open it in Android Studio:

```sh
git clone https://github.com/wille-wang/comp90018-a2.git
```

## Development Conventions

### Committing

[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-%23FE5196?logo=conventionalcommits&logoColor=white)](https://conventionalcommits.org)

This repository intends to use [Conventional Commits](https://www.conventionalcommits.org/en/), which was adapted from the [Angular Commit Style](https://github.com/angular/angular/blob/22b96b9/CONTRIBUTING.md#commit-message-format).

> [!IMPORTANT]
>
> Commits can only be submitted via [branching](#branching) and [pull requests (PRs)](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/getting-started/best-practices-for-pull-requests#best-practices-for-creating-pull-requests)!

### Branching

- **main branch** (`main`): stable deliverable
- **feature branch** (`feat-xxx`): implementation of one new feature
- **bugfix branch** (`fix-xxx`): bug fixes
- **documentation branch** (`docs-xxx`): documentation writing

> [!TIP]
> You can also use the type tags following the [commit convention](#committing).

## Acknowledgements

- Images provided by the [University of Melbourne: Image Gallery](https://www.unimelb.edu.au/filming-on-campus/gallery)
- Icons provided by [Icons8](https://icons8.com/)

## Contributors

| Contributor   | Username         | Email                               |
| ------------- | ---------------- | ----------------------------------- |
| Zeru Guo      | `ero-crypto`     | `zerug@student.unimelb.edu.au`      |
| Yicheng Huang | `EASONOSAE`      | `yichuang1@student.unimelb.edu.au`  |
| Wille Wang    | `wille-wang`     | `yifewang14@student.unimelb.edu.au` |
| Zeyue Xu      | `zeyuexu`        | `zeyue@student.unimelb.edu.au`      |
| Yu Zhou       | `Universe010110` | `zhouyz4@student.unimelb.edu.au`    |
