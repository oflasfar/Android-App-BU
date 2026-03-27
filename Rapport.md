1. Répartition des Tâches et Fusion
Le développement de l'application a été divisé en deux modules distincts avant d'être fusionné pour créer l'application finale complète :

Partie Auteurs (développée par Lasfar Omar Farouk) : Prise en charge de toute la logique liée aux auteurs. Cela inclut la création, la gestion de la liste des auteurs, et la génération dynamique de leurs profils visuels (avatars avec initiales).

Partie Livres (développée par Alexis Hellich) : Prise en charge de la logique des livres. Cela comprend le formulaire d'ajout complet, l'affichage détaillé, le système de sélection de couvertures locales (22 images embarquées), et la gestion des tags.

Fusion : Les deux parties ont ensuite été fusionnées pour lier les entités entre elles (ex: un livre créé est dynamiquement lié à un auteur existant via un menu déroulant).

2. Architecture des Données et Backend Backend
Le DataRepository Unique : Pour gérer la communication avec la base de données et l'API, nous avons pris la décision technique d'utiliser un seul et unique DataRepository partagé par toute l'application (modèle Singleton). Ce choix garantit qu'il n'y ait qu'une seule connexion ouverte avec l'API, ce qui empêche la saturation du serveur, réduit la consommation de mémoire du téléphone, et assure une navigation parfaitement fluide entre les écrans.

L'API du Professeur Kraemer : Nous avons fait le choix d'utiliser l'API fournie par le professeur Kraemer comme backend. Cette API couvre la quasi-totalité des scénarios d'utilisation dont nous avions besoin pour la gestion complète de la bibliothèque. De plus, elle constitue un atout technique majeur lors du développement : elle renvoie des messages d'erreur précis et explicites, ce qui a grandement facilité et accéléré notre processus de débogage à chaque fois que nous rencontrions un problème.

3. Liste des Fonctionnalités Principales
Module Auteurs :

Gestion des auteurs : Création de nouveaux auteurs et affichage sous forme de liste.

Génération visuelle : Création automatique d'un visuel de profil pour chaque auteur en utilisant ses initiales.

Module Livres :

Création de livres : Formulaire d'ajout incluant le titre, l'année de publication, et la sélection de l'auteur associé via un AutoCompleteTextView.

Couvertures 100% hors-ligne : Système interactif permettant de faire défiler et de sélectionner une couverture parmi 22 images locales embarquées dans l'application (drawables), avec sauvegarde persistante du choix via SharedPreferences.

Catégorisation par Tags : Ajout de multiples étiquettes (tags) à un livre grâce à un composant ChipGroup dynamique.

Consultation détaillée : Écran regroupant toutes les informations du livre, affichant la note moyenne (RatingBar) et une liste de commentaires (RecyclerView), avec gestion des états vides (affichage d'un texte alternatif si aucun commentaire n'existe).

Gestion (CRUD) : Possibilité de modifier ou de supprimer un livre existant, avec mise à jour immédiate de l'interface grâce à l'observation des données (LiveData).