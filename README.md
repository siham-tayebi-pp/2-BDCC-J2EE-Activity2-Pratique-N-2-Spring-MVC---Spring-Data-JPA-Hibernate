# 📘 TP N°2 - Spring MVC - Spring Data JPA, Hibernate

**Filière :** BDCC II (Big Data & Cloud Computing)  
**Module :** J2EE & Middlewares  
**Réalisé par :** Siham TAYEBI 
**Encadré par :** Prof Mohamed YOUSSFI
**Date :** Mai 2026  
**Institution :** ENSET Mohammedia, Université Hassan II

---
# Activité Pratique N°2 : Spring MVC - Spring Data JPA, Hibernate

![img_auth.png](images/img_auth.png)
![img_con_bd.png](images/img_con_bd.png)
![bd.png](images/bd.png)

Injection des dépendances soit avec `@Autowired`, soit avec un constructeur sans arguments.

![lst_avant_bootstrap.png](images/lst_avant_bootstrap.png)
![img_apres_bootstrap.png](images/img_apres_bootstrap.png)
![img_btn_delete.png](images/img_btn_delete.png)
![test_delete.png](images/test_delete.png)
![delete_confirmation.png](images/delete_confirmation.png)
![apres_delete.png](images/apres_delete.png)

Ajout de la dépendance Thymeleaf Layout pour gérer les layouts.

Après, on décore les pages produits avec le layout.

![apres_layout.png](images/apres_layout.png)
![modif_navbar.png](images/modif_navbar.png)

On associe des `href` à chaque lien :

- `/` → Home
- `/products` → Liste des produits

---

## Ajout des fonctionnalités Produits

- Création de la page New Product
- Ajout de la route `saveProduct`

![liste_apres_ajout.png](images/liste_apres_ajout.png)

Test d'ajout de produit ✔️

![test2.png](images/test2.png)
![liste_apres_ajout2.png](images/liste_apres_ajout2.png)

---

## Partie 2 : Spring Security

On ajoute la dépendance Spring Boot Starter Security.

Pour utiliser Spring Security avec Thymeleaf, on peut désactiver temporairement la sécurité pour tester.

Quand on relance l'application, Spring demande un username et un password, car l'authentification est activée par défaut.

![activation_spring_security.png](images/activation_spring_security.png)

Et ça marche :

![success.png](images/success.png)

---

### Configuration de la sécurité

On crée une classe de configuration (`SecurityConfig`) :

#### Activation avec `@EnableWebSecurity`

Configuration des accès :
- Si l'utilisateur n'est pas authentifié → redirection vers login
- Toutes les requêtes nécessitent une authentification

Ajout d'un encodeur de mot de passe (`BCryptPasswordEncoder`).

![test_config_sec.png](images/test_config_sec.png)

### Concepts importants

| Concept | Description |
|---|---|
| **Hashing** | Transformation irréversible (ex : BCrypt) |
| **Encoding** | Réversible (ex : Base64) |
| **Cryptage symétrique** | Même clé pour chiffrer/déchiffrer |
| **Cryptage asymétrique** | Clé publique / privée (ex : RSA) |

> Ici, on utilise le **hashing (BCrypt)**.

![test2_configSec.png](images/test2_configSec.png)

---

### Authentification

Les mots de passe sont stockés hachés dans la base de données.

Après authentification :
- Un utilisateur avec le rôle `USER` ne peut **pas** supprimer un produit ❌
- Erreur **403 (Forbidden)** = accès non autorisé

![authentif_succes.png](images/authentif_succes.png)
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

![logout.png](images/logout.png)

Connexion en tant qu'admin :

![con_admin.png](images/con_admin.png)
![echec_save.png](images/echec_save.png)

> Car cela nécessite une authentification avec le rôle `ADMIN`.

Test de suppression et d'ajout d'un produit → ✔️

- Produit ajouté : ![prod_ajoute.png](images/prod_ajoute.png)
- Produit supprimé : ![prod_supr.png](images/prod_supr.png)

Tout nécessite une authentification avec le bon rôle. Seul `/public` est accessible sans authentification grâce à `permitAll()`.

On modifie le contrôleur avec les routes `/admin/delete`, `/admin/save`, etc.

![modif_route.png](images/modif_route.png)

On ajoute le namespace `sec` de Thymeleaf Extras pour afficher le nom de l'utilisateur authentifié.

![nom_user.png](images/nom_user.png)

---

### Gestion des erreurs

Il vaut mieux ne pas afficher la page d'erreur 403 brute, mais rediriger vers une page personnalisée (exception handling / "Not Authorized").

![notAuthorized.png](images/notAuthorized.png)

On cache les boutons "Supprimer" et "Ajouter" pour les non-admins, via :

```html
sec:authorize="hasRole('ADMIN')"
```

![cacher_boutton.png](images/cacher_boutton.png)

Tentative de suppression via l'URL directement → accès refusé ✔️

![essai_supression.png](images/essai_supression.png)
![notAuthorized.png](images/notAuthorized.png)

---

### Session et Cookies

Spring Security utilise une authentification **stateful** :

1. Création d'une session
2. Stockage dans un cookie `JSESSIONID`
3. `HttpOnly` → non accessible via JavaScript

À chaque requête, le navigateur envoie le `JSESSIONID`. Le serveur vérifie si la session est ouverte et si l'utilisateur a le droit d'effectuer l'opération.

---

### Attaque CSRF

**Problème :**

Le navigateur envoie automatiquement les cookies à chaque requête. Si un lien malveillant déclenche une requête vers votre serveur, le navigateur envoie les cookies automatiquement → action non voulue exécutée.

**Solution — Token CSRF :**

À chaque formulaire, le serveur ajoute un champ caché contenant un token CSRF. À chaque soumission, le serveur vérifie ce token : s'il correspond à la session, la requête est acceptée ; sinon, elle est refusée.

> Spring Security active cette protection **par défaut**.

![csrf.png](images/csrf.png)

Pour la désactiver (ex : mode stateless) :

```java
.csrf().disable()
```

> ⚠️ Sans cette protection, on est exposé à des suppressions ou modifications non voulues déclenchées par un simple lien.

On ne doit **jamais utiliser GET** pour des opérations de sauvegarde ou de suppression. On utilise un formulaire avec la méthode `POST` (la méthode `DELETE` n'est pas supportée nativement dans les formulaires HTML).

![notauthorized.png](img_1.png)

---

### Page de login personnalisée

```java
.formLogin(fl -> fl.loginPage("/login"))
```

On crée la page de login et on l'autorise avec `permitAll()`. Il faut également autoriser les ressources WebJars (Bootstrap) avec `permitAll()`.

On ajoute le logout pour invalider la session :

```java
.logout(logout -> logout.invalidateHttpSession(true))
```

![login.png](images/login.png)
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
