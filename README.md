# 📘 TP N°2 — Spring MVC : Spring Data JPA, Hibernate & Spring Security

**Filière :** BDCC II (Big Data & Cloud Computing)
**Module :** J2EE & Middlewares
**Réalisé par :** Siham TAYEBI
**Encadré par :** Prof Mohamed YOUSSFI
**Date :** Mai 2026
**Institution :** ENSET Mohammedia, Université Hassan II

---

## Partie 1 : Spring MVC — Spring Data JPA & Hibernate

### Authentification et connexion à la base de données

Page d'authentification de l'application :

![img_auth.png](images/img_auth.png)

Configuration de la connexion à la base de données :

![img_con_bd.png](images/img_con_bd.png)

Structure de la base de données générée par Hibernate :

![bd.png](images/bd.png)

Injection des dépendances soit avec `@Autowired`, soit avec un constructeur sans arguments.

---

### Affichage de la liste des produits

Liste des produits avant l'ajout de Bootstrap :

![lst_avant_bootstrap.png](images/lst_avant_bootstrap.png)

Liste des produits après l'ajout de Bootstrap (mise en forme améliorée) :

![img_apres_bootstrap.png](images/img_apres_bootstrap.png)

---

### Fonctionnalité de suppression

Ajout du bouton "Supprimer" dans la liste des produits :

![img_btn_delete.png](images/img_btn_delete.png)

Test de la suppression d'un produit :

![test_delete.png](images/test_delete.png)

Ajout d'une boîte de confirmation avant la suppression pour éviter les suppressions accidentelles :

![delete_confirmation.png](images/delete_confirmation.png)

Liste des produits après la suppression :

![apres_delete.png](images/apres_delete.png)

---

### Gestion des layouts avec Thymeleaf Layout

Ajout de la dépendance Thymeleaf Layout pour gérer les layouts.

Après, on décore les pages produits avec le layout.

Page produits après application du layout :

![apres_layout.png](images/apres_layout.png)

Modification de la navbar pour y intégrer les liens de navigation :

![modif_navbar.png](images/modif_navbar.png)

On associe des `href` à chaque lien :

- `/` → Home
- `/products` → Liste des produits

---

### Ajout des fonctionnalités Produits

- Création de la page New Product
- Ajout de la route `saveProduct`

Liste des produits après le premier ajout :

![liste_apres_ajout.png](images/liste_apres_ajout.png)

Test d'ajout de produit ✔️

Formulaire de test d'ajout d'un nouveau produit :

![test2.png](images/test2.png)

Liste mise à jour après le deuxième ajout :

![liste_apres_ajout2.png](images/liste_apres_ajout2.png)

---

## Partie 2 : Spring Security

On ajoute la dépendance Spring Boot Starter Security.

Pour utiliser Spring Security avec Thymeleaf, on peut désactiver temporairement la sécurité pour tester.

Quand on relance l'application, Spring demande un username et un password, car l'authentification est activée par défaut.

Spring Security activé : la page de login par défaut s'affiche au démarrage :

![activation_spring_security.png](images/activation_spring_security.png)

Connexion réussie avec les identifiants par défaut :

![success.png](images/success.png)

---

### Configuration de la sécurité

On crée une classe de configuration (`SecurityConfig`) :

#### Activation avec `@EnableWebSecurity`

Configuration des accès :
- Si l'utilisateur n'est pas authentifié → redirection vers login
- Toutes les requêtes nécessitent une authentification

Ajout d'un encodeur de mot de passe (`BCryptPasswordEncoder`).

Test de la configuration de sécurité — la page de login s'affiche bien :

![test_config_sec.png](images/test_config_sec.png)

---

### Concepts importants

| Concept | Description |
|---|---|
| **Hashing** | Transformation irréversible (ex : BCrypt) |
| **Encoding** | Réversible (ex : Base64) |
| **Cryptage symétrique** | Même clé pour chiffrer/déchiffrer |
| **Cryptage asymétrique** | Clé publique / privée (ex : RSA) |

> Ici, on utilise le **hashing (BCrypt)**.

Deuxième test de la configuration de sécurité avec un utilisateur en base de données :

![test2_configSec.png](images/test2_configSec.png)

---

### Authentification

Les mots de passe sont stockés hachés dans la base de données.

Après authentification :
- Un utilisateur avec le rôle `USER` ne peut **pas** supprimer un produit ❌
- Erreur **403 (Forbidden)** = accès non autorisé

Authentification réussie avec un utilisateur de rôle USER :

![authentif_succes.png](images/authentif_succes.png)

Tentative de suppression par un USER → accès interdit (403) :

![interdiction.png](images/interdiction.png)

---

### Gestion des rôles

| Règle | Comportement |
|---|---|
| `hasRole("ADMIN")` | Accès admin uniquement |
| `permitAll()` | Accès public sans authentification |

Ajout de routes protégées :
- `/admin/delete`
- `/admin/save`

Déconnexion (logout) de l'utilisateur courant :

![logout.png](images/logout.png)

Connexion en tant qu'admin :

![con_admin.png](images/con_admin.png)

Tentative d'enregistrement d'un produit sans le rôle ADMIN → échec :

![echec_save.png](images/echec_save.png)

Car cela nécessite une authentification avec le rôle `ADMIN`.

On essaie de supprimer et d'ajouter un produit → ça marche ✔️

Produit ajouté avec succès par l'admin :

![prod_ajoute.png](images/prod_ajoute.png)

Produit supprimé avec succès par l'admin :

![prod_supr.png](images/prod_supr.png)

Tout nécessite une authentification avec le bon rôle. Seul `/public` est accessible sans authentification grâce à `permitAll()`. `hasRole` nécessite une authentification avec un rôle spécifique.

On passe à la modification du contrôleur avec les routes `/admin/delete`, `/admin/save`, etc.

Modification des routes dans le contrôleur pour les sécuriser sous `/admin` :

![modif_route.png](images/modif_route.png)

On ajoute le namespace `sec` de Thymeleaf Extras pour afficher le nom de l'utilisateur authentifié.

Affichage du nom de l'utilisateur connecté dans la navbar :

![nom_user.png](images/nom_user.png)

---

### Gestion des erreurs

Il vaut mieux ne pas afficher la page d'erreur 403 brute, mais rediriger vers une page personnalisée (exception handling / "Not Authorized").

Page personnalisée affichée lors d'un accès non autorisé :

![notAuthorized.png](images/notAuthorized.png)

Maintenant on va cacher les boutons "Supprimer" et "Ajouter" pour les utilisateurs non admin, et les afficher uniquement si la sécurité l'autorise, via :

```html
sec:authorize="hasRole('ADMIN')"
```

Boutons cachés pour les utilisateurs sans le rôle ADMIN :

![cacher_boutton.png](images/cacher_boutton.png)

Tentative de suppression via l'URL directement pour vérifier que la protection est bien active :

![essai_supression.png](images/essai_supression.png)

Et voilà, accès refusé même en passant par l'URL :

![notAuthorized.png](images/notAuthorized.png)

---

### Session et Cookies

Spring Security utilise une authentification **stateful** :

1. Création d'une session
2. Stockage dans un cookie `JSESSIONID`
3. `HttpOnly` → non accessible via JavaScript

Lors de l'authentification, Spring Security vérifie le mot de passe, crée une session et renvoie la session sous forme de cookie. Le navigateur la stocke côté client. À chaque requête, le navigateur envoie le `JSESSIONID`, et le serveur vérifie si la session est ouverte et si l'utilisateur a le droit d'effectuer l'opération.

---

### Attaque CSRF

**Problème :**

Le navigateur envoie automatiquement les cookies à chaque requête. Si un lien malveillant déclenche une requête vers votre serveur, le navigateur envoie les cookies automatiquement → action non voulue exécutée.

**Solution — Token CSRF :**

À chaque formulaire, le serveur ajoute un champ caché contenant un token CSRF. À chaque soumission, le serveur vérifie ce token : s'il correspond à la session, la requête est acceptée ; sinon, elle est refusée, car considérée comme ne provenant pas d'un formulaire légitime fourni par le serveur.

> Spring Security active cette protection **par défaut**.

Inspection du formulaire côté navigateur : le token CSRF est bien présent dans un champ caché :

![csrf.png](images/csrf.png)

Pour la désactiver (ex : mode stateless) :

```java
.csrf().disable()
```

> ⚠️ Sans cette protection, on est exposé à des suppressions ou modifications non voulues déclenchées par un simple lien.

On ne doit **jamais utiliser GET** pour des opérations de sauvegarde ou de suppression. On utilise un formulaire avec la méthode `POST` (la méthode `DELETE` n'est pas supportée nativement dans les formulaires HTML).

Tentative de suppression via un lien GET → accès refusé par Spring Security :

![notauthorized.png](img_1.png)

---

### Page de login personnalisée

```java
.formLogin(fl -> fl.loginPage("/login"))
```

On crée la page de login et on l'autorise avec `permitAll()`, sinon elle nécessitera une authentification. Il faut également autoriser les ressources WebJars (Bootstrap) avec `permitAll()`.

On ajoute le logout pour invalider la session :

```java
.logout(logout -> logout.invalidateHttpSession(true))
```

Page de login personnalisée fonctionnelle :

![login.png](images/login.png)

Page de login après personnalisation du style avec Bootstrap :

![login_style.png](images/login_style.png)

---

### Comment protéger l'application ?

Il existe deux méthodes, combinables :

| Méthode | Description |
|---|---|
| `authorizeHttpRequests` | Configuration de sécurité dans une classe de config |
| Annotations (`@PreAuthorize`, `@Secured`) | Protection directement dans le contrôleur |

```java
// Prochaine étape : utiliser JSON Web Token (JWT) avec authentification stateless
// au lieu de la gestion des sessions avec cookies
```
