# 📜 Interactive News Map App – India Edition 🇮🇳

An Android application that offers a unique and engaging way to explore state-wise **news** and **weather updates** across India. Simply tap on any state in the interactive map to dive into localized information — all packed in a sleek, intuitive UI.

## ✨ Features

- 🔐 **Login & Authentication**
  - Google Sign-In via Firebase Authentication
  - Simple user ID login option
  - Firebase Analytics integration to track user engagement

- 🗺️ **Interactive Indian Map (SVG-based)**
  - Clickable Indian map built using scalable vector graphics (SVG)
  - Each state is tappable and triggers content specific to that region

- 📰 **State-wise News Feed**
  - Real-time news fetched using **web scraping (Jsoup)**
  - Displays state-specific headlines and summaries upon tap

- 🌦️ **Live Weather Updates**
  - Integrated **weather API** using Retrofit
  - Displays current weather for selected state

## 👨‍💼 How to Run Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/arkos121/interactive-news-map.git
   ```

2. **Open in Android Studio**

3. **Add Firebase Config**
   - Download `google-services.json` from your Firebase project
   - Place it in the `/app` directory

4. **Add Weather API Key**
   - Insert your API key in `local.properties` or constants file as:
     ```
     WEATHER_API_KEY=your_api_key_here
     ```

5. **Build & Run**

## 🌐 Tech Stack

| Component            | Tech Used                |
|----------------------|--------------------------|
| Language             | Kotlin                   |
| Architecture         | MVVM                     |
| UI Layout            | ConstraintLayout, SVG    |
| Network Calls        | Retrofit                 |
| Auth & Analytics     | Firebase Auth & Analytics|
| Web Scraping         | Jsoup                    |
| JSON Parsing         | Gson                     |

## 📸 Screenshots (To Add)


## 🧠 Why I Built This

> I wanted to build a user-friendly app that gives people quick access to local news and weather — just by tapping on a state. It combines real-time data, clean visuals, and interactive UI using SVG maps.
>  It also helped me strengthen my skills in **Kotlin**, **Firebase**, and **Retrofit**, while having fun working with **web scraping** and **custom layouts**.

## 🔮 Future Enhancements

- Push notifications for breaking news
- Offline caching of recent headlines
- Improved map design using Jetpack Compose Canvas
- Multi-language support (Hindi, Tamil, etc.)
- Favorite state selection for quick access

## 📬 Contact

Made with ❤️ by **[Akash Ranjan](https://github.com/arkos121)**  
📧 akashrj1857@gmail.com
🔗 [GitHub](https://github.com/arkos121) • [LinkedIn](https://linkedin.com/in/your-profile)

