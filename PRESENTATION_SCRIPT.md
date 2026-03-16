# Script de présentation — Application Android "COOKING EXPRESS"
> **Destiné à Gamma AI — 10 cartes maximum**
> Copiez ce script tel quel dans Gamma AI pour générer votre présentation.

---

## Carte 1 — Titre

**Titre :** COOKING EXPRESS

**Sous-titre :** Consultez vos recettes en ligne et hors ligne

**Logo :** ![Logo Cooking Express](https://github.com/user-attachments/assets/75d75114-b143-49d7-882f-0658c964e12f)

**Visuel suggéré :** Logo COOKING EXPRESS sur fond blanc (wordmark "cook" avec flamme verte et poêle, sous-titre vert "COOKING EXPRESS")

---

## Carte 2 — Présentation du projet

**Titre :** Qu'est-ce que COOKING EXPRESS ?

**Contenu :**
- Application Android développée en **Kotlin**
- Permet de consulter des recettes de cuisine depuis **TheMealDB API**
- Fonctionne **en ligne ET hors ligne** grâce au cache local
- Interface moderne et intuitive en **Material Design 3**

**Visuel suggéré :** Logo COOKING EXPRESS + capture d'écran de l'écran principal (liste de recettes)

---

## Carte 3 — Fonctionnalités principales

**Titre :** Fonctionnalités clés

**Contenu :**
- 🔍 **Recherche** de recettes par nom (en temps réel)
- 🏷️ **Filtrage par catégorie** (chips horizontaux)
- 📄 **Détail complet** : ingrédients, mesures, instructions
- 📜 **Pagination** : chargement automatique au scroll
- 💾 **Mode hors ligne** : données disponibles sans connexion

**Visuel suggéré :** Icônes représentant chaque fonctionnalité

---

## Carte 4 — Architecture technique

**Titre :** Architecture du projet

**Contenu (schéma en 3 couches) :**

| Couche | Rôle |
|--------|------|
| **UI (Présentation)** | Compose, ViewModel, StateFlow |
| **Domain (Métier)** | Modèles, interfaces Repository |
| **Data (Données)** | API Retrofit, Room Database |

**Architecture :** Clean Architecture (séparation des responsabilités)

**Visuel suggéré :** Diagramme en couches (UI → Domain → Data)

---

## Carte 5 — Technologies utilisées

**Titre :** Stack technologique

**Contenu :**
- 🖼️ **Jetpack Compose** + Material 3 — Interface déclarative moderne
- 🌐 **Retrofit 2** + OkHttp — Appels API REST
- 🗄️ **Room Database** — Stockage local (SQLite)
- ⚡ **Coroutines Kotlin** — Programmation asynchrone
- 🖼️ **Coil** — Chargement d'images optimisé
- 🧭 **Navigation Compose** — Navigation entre écrans

**Visuel suggéré :** Logos des technologies (Kotlin, Jetpack, Retrofit…)

---

## Carte 6 — Écrans de l'application

**Titre :** Parcours utilisateur

**Contenu :**
1. **Splash Screen** — Écran d'accueil animé avec logo COOKING EXPRESS (2 secondes)
2. **Écran Principal** — Liste des recettes + barre de recherche + filtres
3. **Écran Détail** — Photo, ingrédients et instructions de la recette

**Navigation :** `Splash → Liste → Détail`

**Visuel suggéré :** Maquette montrant les 3 écrans côte à côte

---

## Carte 7 — Gestion du cache et mode hors ligne

**Titre :** Fonctionnement hors ligne

**Contenu :**
- Les recettes sont **sauvegardées localement** via Room Database
- Durée de cache : **1 heure** (rafraîchissement automatique)
- En cas d'absence de connexion → **affichage des données en cache**
- Aucune perte de données pour l'utilisateur

**Visuel suggéré :** Icône Wi-Fi barré + base de données locale (flèche vers l'appareil)

---

## Carte 8 — Recherche intelligente

**Titre :** Recherche et filtrage

**Contenu :**
- Recherche **en temps réel** avec délai de 400 ms (debounce)
- Évite les appels API excessifs lors de la frappe
- Filtrage par **catégorie** (ex : Beef, Seafood, Dessert…)
- Résultats mis en cache localement après chaque recherche

**Visuel suggéré :** GIF ou image d'une barre de recherche avec des chips de catégories

---

## Carte 9 — Points forts du projet

**Titre :** Ce qui rend COOKING EXPRESS remarquable

**Contenu :**
- ✅ **Clean Architecture** — Code maintenable et testable
- ✅ **100 % Kotlin** — Langage moderne et concis
- ✅ **Offline First** — Expérience utilisateur sans coupure
- ✅ **UI moderne** — Jetpack Compose + Material Design 3
- ✅ **Performance** — Pagination et debounce pour éviter les surcharges

**Visuel suggéré :** Badge "✅" sur fond vert pour chaque point

---

## Carte 10 — Conclusion et perspectives

**Titre :** Bilan & améliorations futures

**Contenu — Ce qui a été réalisé :**
- Application COOKING EXPRESS fonctionnelle avec recherche, filtres et mode hors ligne
- Architecture propre et scalable

**Contenu — Améliorations envisagées :**
- ⭐ Ajout de recettes en **favoris**
- 🌍 Support **multilingue**
- 🔔 **Notifications** de nouvelles recettes
- 📊 Historique de navigation

**Visuel suggéré :** Logo COOKING EXPRESS + icône de fusée ou d'étoile pour les perspectives

---

*Script généré pour Gamma AI — 10 cartes — Application Android COOKING EXPRESS*
