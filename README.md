# DevOps - Lab \#1: Mutators

### Équipe \#5:
* Antoine Aubé
* Maxime Carlier
* Huang Xiaohan
* Thibaut Terris

## Comment exécuter le projet ?

Maven est nécessaire.

``./execution.sh <nom_dossier>``



Utilisation : execution ``<folder>``


``<folder>`` doit être le chemin du projet Maven auquel vous voulez appliquer toutes les mutations.


``[--windows]`` Si vous êtes sur Windows ajouter cet argument après le folder.

## Comment ajouter un mutateur ?

Les mutateurs se trouvent dans le dossier ``spoon-mutators/src/main/java/devops/five/mutators``

C'est à cet endroit là que vous pouvez en ajouter un.


Si vous voulez pouvoir changer son pourcentage d'affectation ne pas oublier de mettre un constructeur par défaut qui appellera
le constructeur mère *super("nomMutateur")*.

## Comment changer le pourcentage de mutation ?

Rendez-vous dans le dossier ``spoon-mutators/src/main/resources`` vous y trouverez un fichier ``percentages.properties``.


Pour ajouter un pourcentage de mutation, associez le nomMutateur (définit dans le constructeur du mutateur) avec sa valeur comprise entre 0.0 et 1.0.
