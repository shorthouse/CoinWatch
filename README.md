# CoinWatch
**CoinWatch** is a cryptocurrency app that displays data about current coin prices and market details.

The app is built using the latest [Android architecture components](https://developer.android.com/topic/architecture/recommendations) - resulting in a scalable, modularised and testable app.

# Built With 🛠 
- Kotlin
- Jetpack Compose
- Retrofit
- Coil
- OkHttp
- Coroutines
- Flow
- Hilt
- ViewModel
- Timber logging
- Room
- Material Design 3
- Static code analysis using Ktlint

# Architecture 🧱
The design of CoinWatch follows a few key principles:
1. Unidirectional Data Flow (UDF) - state flows in one direction, events that modify the data flow in the opposite direction
2. Single Source of Truth (SSOT) - the SSOT is the owner of data, and only the SSOT can modify or mutate it
3. Drive UI from immutable data models - app data is contained within persistent data models and drives UI components

A high-level overview of the app's architecture is displayed below.

<p align="center">
   <img src="https://github.com/shorthouse/CoinWatch/assets/73708076/d931301f-80da-4cb7-9824-bdf1d4cdfaa3" width="550">
</p>

# Testing 🧬 
To facilitate testing of the app, CoinWatch uses the following:
 - JUnit
 - Espresso
 - Truth
 - MockK
 - Compose testing library

# Contribution 🤝
Contributions to the project are welcome and encouraged! To get started, please check out the contributing guidelines.

# Contact 📥
Do you have ideas to improve the app or a query for the developer?

Please get in touch via email - shorthouse20@gmail.com

# Acknowledgements ⭐
 - [CoinRanking API](https://developers.coinranking.com/api) for providing coin data
 - [unDraw](https://undraw.co/illustrations) for providing empty state images

# License 🔖
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
