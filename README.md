                          Projet de compilation fait par Taïr Amine et Ayoub Atmane du groupe 4.
                                                Pseudo Compilateur
Introduction :

Nous avons conçus un programme qui permet l'analyse lexicale, syntaxique et sémantique d'un fichier source représentant un programme d’un language inventé. Pour ce faire, nous avons utilisé le langage java. Nous allons donner dans se petit rapport un aperçus sur la réalisation de ce pseudo compilateur.
Pour la réalisation de ce projet nous avons adopté le plan suivant :

Interface du programme :

Tout d’abord nous avons construis l’interface de notre programme avec java Fx, où nous avons mit dans un panel un textarea pour afficher et saisir du texte qui constitue l’éditeur de notre programme. Donc nous avons la possibilité de charger éditer ou modifier un code source à compiler. Cette interface est gérée par la programmation de boutons .

Analyse lexicale :

L’analyse lexical est assurée par une lecture du ficher code source caractère par caractère. La table d’analyse des lexèmes qui simule l’automate d’analyse d’expression régulière est assurée par un grand Switch en utilisant des tables des différents types des lexèmes : tables des mots réservés, table des identifiants, table des entiers, table des réels, table des messages, table des opérateurs et une table particulière résultat de l’analyse lexicale appelée < tlex > représentant l’enchainement des lexèmes nécessaire pour l’analyse syntaxique. Comme exemple la forme et le contenue de notre table des mots réservés est :

public static String [][] tmotrev ={{"Start_Program"," Debut de programme"},{"End_Program","Fin de programme"}, {"Give"," Affectation une valeur "},{"ShowMes","Afichage du message "} , {"Affect","affectation entre variables"}, {"to","de affect"}, {"ShowVal","Affichage d'une valeure"}, {"Int_Number","Declaration d'entier" }, {"Real_Number","Declaration de reel"}, {"","indication de source"}, {"Start"," Debut de bloc"}, {"If","Instruction conditiunnelle"}, {"Else","Sinon"}, {"Finish","Fin Bloc"},{"--", "Debut/Fin condition"}, {";;", "Fin instruction"}, {"//.","Debut commentaire"}};

Analyse syntaxique :

L’analyse syntaxique est la phase qui vient après l’analyse lexicale. Donc notre pseudo compilateur se fait en deux passes. Quand la table des lexèmes est générée l’analyse syntaxique est réalisée par appel de fonctions récursives de l’analyse descendante. Les fonctions de cette analyse assurent la vérification des règles de la grammaire. La forme de notre grammaire est comme dans l’exemple qui suit, nous avons pris soin dans son élaboration de vérifier les propriétés de factorisation et de non récursive gauche :

synt()-> Start_Program S() End_Program S()->LD() LI() LD()->Int_Number : Lid() / Real_Number : Lid() / ξ Lid()->ID, Lid()/ ID ;; Donc durant l’analyse syntaxique notre programme affiche des messages d’erreurs correspondants selon l’endroit où l’erreur est détectée.

Fonction sémantique :

L’analyse sémantique est faite dans la même passe que l’analyse syntaxique en introduisant des fonctions spéciales dans le membre droit des règles de la grammaire assurant des calcules sémantiques et des vérifications des erreurs sémantiques. Nous avons particulièrement augmenté (en vert dans les règles suivantes) le membre droit des règles suivantes pour calculer le nombre d’identifiants d’une déclaration :

LD()->Int_Number {nbe=0,nbr=-1}: Lid() / Real_Number{nbe=-1,nbr=0} : Lid() / ξ Lid()->ID { if(nbr==-1)nbe++; else if(nbe==-1)nbr++;},Lid()/ ID {if(nbr==-1)nbe++; else if(nbe==-1)nbr++;};;
