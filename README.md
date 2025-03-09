# RateContent 🎬🎮📚

Android-приложение для поиска фильмов, игр и книг с отображением средних оценок.  
Возможность добавления в избранное с персональной оценкой.

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Clean_Architecture-6DB33F?style=for-the-badge)](https://developer.android.com/topic/architecture)

## 🌟 Основные функции
- Поиск по трем категориям: фильмы, игры, книги
- Отображение средней оценки из открытых источников
- Персональное оценивание и добавление в избранное
- Локальное хранение избранных позиций

## 🛠 Технологический стек
- **Язык**: Kotlin
- **UI**: Jetpack Compose
- **DI**: Hilt
- **Локальная БД**: Room
- **Сетевое взаимодействие**: 3xRest API, Retrofit + Moshi
- **Асинхронность**: Coroutines, Flow, StateFlow
- **Архитектура**: MVVM, Clean Architecture


## 🏗 Архитектура приложения
```mermaid
graph LR
    UI[Экран поиска] -->|Запрос| VM[ViewModel]
    VM -->|SearchUseCase| API[(Внешние API)]
    API -->|Данные| VM
    VM -->|Обновление UI| UI

   
    VM -->|Добавление в избранное| DB[(Room Database)]
    DB -->|Кэш избранного| VM
    VM -->|Обновление списка из ДБ| FAV[Экран избранного]
```

MIT License © Дмитрий Тимонин.
