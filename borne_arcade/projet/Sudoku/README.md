# Sudoku 🎱

Jeu de Sudoku en Java développé pour intégrer la borne d'arcade

## Description 

Ce jeu est une reproduction du jeu de Sudoku classique avec une interface graphique pour borne d'arcade. Le joueur complète une grille 9×9 générée aléatoirement en respectant les règles du Sudoku.

## Règles 

Chaque chiffre ne doit apparaître qu'une seule fois :

- dans chaque ligne 🚥
- dans chaque colonne 🚦
- dans chaque bloc 3x3 🔲

## Contrôles

### Menu
- **Joystick Haut/Bas** : Sélectionner entre PLAY et EXIT
- **Bouton A (F)** : Valider la sélection

### Jeu
- **Joystick Haut/Bas/Gauche/Droite** : Naviguer dans la grille
- **Bouton A (F)** : Augmenter la valeur (+1)
- **Bouton B (G)** : Diminuer la valeur (-1)
- **Bouton C (H)** : Effacer la case
- **Bouton Z/Y** : Quitter et retour au menu

## Prérequis 

Afin de procéder au lancement de la borne et des jeux inclus vous devez disposer : 
- D'un environnement Linux ou MacOs 
- D'un terminal Bash permettant d'éxcuter les **script .sh** de la borne.

## Dépendances
- Java ( JDK installé avec java et javac)
- MG2D : Bibliothèque graphique de la borne (contient les classes géométrique, fenêtre et musique)

## Compilation

Pour faire la compilation de la borne et des jeux entrez cette commande dans un terminal bash :
- ```./lancerBorne.sh```

## Lancement du jeu
Une fois la borne lancée naviguez à travers le menu de celle-ci et selectionnez le jeu **Sudoku** avec la touche **F**.

## Structure du projet 
Sudoku/

── Main.java 

── Sudoku.java            # Logique du jeu

── Grille.java            # Génération et gestion de la grille

── ClavierBorneArcade.java          

── Sudoku.sh              # Script de lancement

── README.md      

## Auteures
CHATELAIN Lilou
LIGNIER Noémie

IUT du littoral Côte d'Opale

## Versions

Version 1.1 - Mars 2026