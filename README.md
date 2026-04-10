# 📚 Gestionnaire de Bibliothèque Android

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Bun](https://img.shields.io/badge/Bun-000000?style=for-the-badge&logo=bun&logoColor=white)
![Prisma](https://img.shields.io/badge/Prisma-3982CE?style=for-the-badge&logo=Prisma&logoColor=white)

Une application Android native et fluide permettant de gérer intégralement une bibliothèque personnelle (livres, auteurs, tags, et commentaires). Développée en Java avec une architecture MVVM stricte et connectée à une API REST performante.

> 🎓 **Projet académique** développé par Lasfar Omar Farouk et Alexis Hellich.

---

## ✨ Fonctionnalités Principales & Optimisations

Nous avons poussé le développement au-delà du simple CRUD pour offrir une expérience utilisateur (UX) optimale et des performances solides :

* 🖼️ **Couvertures 100% Hors-Ligne :** Sélection et sauvegarde persistante de couvertures locales via les `SharedPreferences` (zéro temps de chargement).
* 👤 **Avatars Dynamiques :** Génération automatique d'avatars basés sur les initiales des auteurs via l'API `ui-avatars.com` (géré asynchronement par Glide).
* 📑 **Pagination Explicite :** Chargement optimisé des données par pages pour préserver la RAM du téléphone et la bande passante.
* 🏷️ **Système de Tags & Filtres :** Catégorisation des livres avec des `ChipGroup` dynamiques et barres de recherche en temps réel.
* 💬 **Gestion UX des États Vides :** Affichage de messages alternatifs propres (ex: *"Il n'y a pas encore de commentaire"*) lorsque les listes `RecyclerView` sont vides.
* ✏️ **Gestion Complète (CRUD) :** Création, lecture, modification et suppression des livres et des auteurs en temps réel.

## 🏗️ Architecture Technique

L'application respecte les standards modernes de développement Android :
* **MVVM & LiveData :** Séparation stricte de l'interface et de la logique métier. L'interface réagit automatiquement aux changements de la base de données.
* **Singleton `DataRepository` :** Une instance unique pour gérer les appels réseau (Retrofit), évitant la saturation de l'API et garantissant une navigation fluide.
* **Jetpack Navigation :** Transitions entre les écrans (`Fragments`) et gestion native de la pile de retour (`onSupportNavigateUp`).
* **ViewHolder Pattern :** Méthodes `bind()` encapsulées pour un rendu des listes ultra-fluide et un code propre.

---

## 🚀 Démarrage du Projet

Le projet est divisé en deux parties : l'application Android et l'API (Backend).

### 1. Lancer l'API (Bun + Prisma)
Naviguez dans le dossier de l'API et exécutez ces commandes :

```bash
# 1. Installer les dépendances
bun install

# 2. Réinitialiser la DB et injecter les fausses données (Faker)
bun prisma migrate reset

# 3. Lancer le serveur en mode développement
bun --watch src/index.ts
