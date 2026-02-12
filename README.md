**Description**

Ce projet a été utilisé comme base pour la mise en place d’une pipeline DevOps complète intégrant les pratiques d’intégration continue, d’analyse qualité, de gestion des artefacts et de conteneurisation.

**Objectifs du projet**

Gérer les présences des étudiants

Centraliser les données d’assiduité

Fournir des endpoints REST pour la consultation et la gestion des présences

Mettre en place une pipeline CI/CD automatisée

Garantir la qualité et la maintenabilité du code

**Architecture technique**

Le projet est basé sur une architecture microservices et comprend :

Backend : Spring Boot

Build tool : Maven

Base de données : MongoDB Atlas

API REST pour les opérations CRUD liées aux présences

**Pipeline DevOps**

Une pipeline CI/CD complète a été mise en place autour de ce projet :

Gestion du code source avec GitHub

Intégration continue avec Jenkins

Build et tests automatiques avec Maven

Tests unitaires avec JUnit

Analyse de couverture de code avec JaCoCo

Analyse statique et contrôle qualité avec SonarQube

Publication des artefacts dans JFrog Artifactory

Conteneurisation avec Docker

Monitoring et visualisation des métriques via Grafana

La pipeline est déclenchée automatiquement à chaque modification du code.

**Tests et qualité**

Exécution automatique des tests unitaires

Génération de rapports de couverture de code

Analyse des bugs, vulnérabilités et code smells

Vérification via Quality Gate avant validation du build

=> L’objectif est d’assurer un code propre, maintenable et conforme aux bonnes pratiques.

**Conteneurisation**

L’ensemble des outils DevOps (Jenkins, SonarQube, Artifactory, Grafana) ainsi que l’application ont été déployés à l’aide de Docker, garantissant:

Portabilité

Isolation des environnements

Facilité de déploiement

Reproductibilité de l’environnement

**Compétences mises en œuvre**

DevOps et CI/CD

Automatisation des builds et des tests

Analyse qualité continue

Gestion des artefacts

Conteneurisation

Monitoring applicatif

Architecture microservices

**Auteur**

**Sahar Kaddour**
Étudiante en ingénierie informatique – ESPRIT
