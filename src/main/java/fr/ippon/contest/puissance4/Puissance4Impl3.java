package fr.ippon.contest.puissance4;

/**
 * Created by Bertrand on 08/03/2015.
 */
public class Puissance4Impl3 implements Puissance4 {
    private char[][] grille;
    private char tour;
    private EtatJeu etatJeu;

    int NUMBER_OF_ROWS = 6; // FIXME: vérifier la déclaration de constantes
    int NUMBER_OF_COLUMNS = 7;

//    public Puissance4Impl3() {
//        this.etatJeu = EtatJeu.EN_COURS;
//        this.tour = 'R';
//        this.grille = new char[][]{
//                {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
//                {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'}
//        };
//    }

    /**
     * Recherche d'une combinaison gagnante d'une colonne, d'une ligne ou d'une diagonale
     * déterminée en fonction des arguments.
     * Arrête la recherche à la première combinaison trouvée.
     * Retourne
     *
     * @param ligne
     * @param colonne
     * @param stepLigne
     * @param stepColonne
     * @param stopOnEmptyCell parcours toute la ligne au lieu de s'arrêter à la première cellule vide
     * @return true si une combinaison gagnante est trouvée
     */
    private boolean checkCombinaisonGagnanteInitiale(int ligne, int colonne, int stepLigne, int stepColonne, boolean stopOnEmptyCell, int serieLength) {
        char couleurCourante = '-';
        int nombreDeJetonsGagnant = 0;
        // ==> on s'arrête dès qu'un autre caractère que 'R' ou 'J' est rencontré
        // ==> notons que si on avait pas autre chose que le caractère '-' dans les lignes, on pourrait ne faire qu'un seul test, mais il faudrait tester les bornes en plus, sauf si le caractère '-' est aussi un caractère de la borne
        // nbParsedCoins permet de limiter la recherche aux combinaisons possibles
        for (; serieLength + nombreDeJetonsGagnant >= 4 && (grille[ligne][colonne] == 'R' || grille[ligne][colonne] == 'J' || !stopOnEmptyCell); --serieLength, ligne += stepLigne, colonne += stepColonne) {
            if (grille[ligne][colonne] != couleurCourante) {
                couleurCourante = grille[ligne][colonne];
                nombreDeJetonsGagnant = 1;
            } else if (grille[ligne][colonne] != '-') {
                if (nombreDeJetonsGagnant == 3) {
                    this.etatJeu = couleurCourante == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                    return true;
                }

                ++nombreDeJetonsGagnant;
            }
        }
        return false;
    }

    private boolean checkCombinaisonGagnante(int ligne, int colonne, int stepLigne) {
        char tour = this.grille[ligne][colonne];
        int iStartLigne = ligne - stepLigne;
        int iEndLigne = ligne + stepLigne;
        int iStartColonne = colonne - 1;
        int iEndColonne = colonne + 1;

        // recherche du début de l'intervalle gagnant
        for (; grille[iStartLigne][iStartColonne] == tour; iStartLigne -= stepLigne, --iStartColonne)
            ;

        if (iEndColonne - iStartColonne - 1 < 4) {
            // recherche de la fin de l'intervalle gagnant
            for (; grille[iEndLigne][iEndColonne] == tour; iEndLigne += stepLigne, ++iEndColonne)
                ;
        }
        return iEndColonne - iStartColonne - 1 == 4;
    }

    private boolean checkGrilleNulle() {
        int iColonne = 0;

        for (; iColonne < NUMBER_OF_COLUMNS && grille[1][iColonne + 1] != '-'; iColonne++)
            ;

        if (iColonne >= NUMBER_OF_COLUMNS) {
            this.etatJeu = EtatJeu.MATCH_NUL;
            return true;
        }

        return false;
    }

    private boolean checkEnd(int ligne, int colonne) {
        char tour = this.grille[ligne][colonne];

        // check column winner
        if (grille[ligne + 1][colonne] == tour
                && grille[ligne + 2][colonne] == tour
                && grille[ligne + 3][colonne] == tour) {
            this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
            return true;
        }

        // check line winner
        if (checkCombinaisonGagnante(ligne, colonne, 0)
                // check diagonale montante de gauche à droite
                || checkCombinaisonGagnante(ligne, colonne, +1)
                // check diagonale descendante de gauche à droite
                || checkCombinaisonGagnante(ligne, colonne, -1)) {
            this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
            return true;
        }

        // check null
        {
            if (checkGrilleNulle()) {
                this.etatJeu = EtatJeu.MATCH_NUL;
                return true;
            }
        }

        return false;
    }

    /**
     * Vide la grille de jeu, et tire au sort le joeur qui commence.
     */
    public void nouveauJeu() {

    }

    /**
     * Charge une partie en cours.
     *
     * @param grille une grille de puissance 4 (6 lignes x 7 colonnes).
     *               Une case vide est représentée par le caractère '-',
     *               Une case occupée par un jeton rouge, par le caractère 'R'
     *               Une case occupée par un jeton jaune, par le caractère 'J'.
     * @param tour   le joueur dont c'est le tour (J ou R)
     * @throws IllegalArgumentException si la grille est invalide,
     *                                  ou si tour ne vaut ni J ni R.
     */
    public void chargerJeu(final char[][] grille, final char tour) {
        if (grille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être nulle");
        }

        if (grille.length != NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("La grille doit avoir exactement 6 lignes");
        }

        for (int i = 0; i < NUMBER_OF_ROWS; ++i) {
            if (grille[i].length != NUMBER_OF_COLUMNS) {
                throw new IllegalArgumentException("La grille doit avoir exactement 7 colonnes");
            }
        }

        if (tour != 'R' && tour != 'J') {
            throw new IllegalArgumentException("Le joueur doit être représenté par 'J' ou par 'R");
        }

        // FIXME: utiliser une meilleure méthode de clonage, ie. arrayCopy
        this.tour = tour;
        this.etatJeu = EtatJeu.EN_COURS;

        /*
         * initialisation de la grille en ajoutant une bordure toute autour pour éviter d'avoir à tester
         * les sorties de plateau pendant la recherche des combinaisons gagnantes
         */
        this.grille = new char[][]{
                {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', '-', '-', '-', '-', '-', '-', '-', 'x'},
                {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'}
        };

//        for (int ligne = NOMBRE_DE_LIGNES - 1; ligne >= 0; --ligne) {
//            for (int colonne = 0; colonne < NOMBRE_DE_COLONNES; ++colonne) {
//                if (grille[ligne][colonne] != 'R' && grille[ligne][colonne] != 'J' && grille[ligne][colonne] != '-') {
//                    throw new IllegalArgumentException("La grille contient des caractères invalides");
//                }
//
//                if (grille[ligne][colonne] != '-') {
//                    this.grille[ligne + 1][colonne + 1] = grille[ligne][colonne];
//                    if (this.checkEnd(ligne + 1, colonne + 1)) {
//                        return;
//                    }
//                }
//            }
//        }

        // - validation du contenu de la grille
        // - vérification qu'il n'y a pas de jeton "flottant"
        // initialisation de la grille interne
        for (int colonne = 0; colonne < NUMBER_OF_COLUMNS; ++colonne) {
            boolean fullCells = true;
            for (int ligne = NUMBER_OF_ROWS - 1; ligne >= 0; --ligne) {

                boolean cellIsFull = grille[ligne][colonne] == 'R' || grille[ligne][colonne] == 'J';
                if (!cellIsFull && grille[ligne][colonne] != '-') {
                    throw new IllegalArgumentException("La grille contient des caractères invalides");
                }

                // détection de la dernière cellule occupée
                if (cellIsFull) {
                    if (!fullCells) {
                        throw new IllegalArgumentException("La grille contient des jetons flottants");
                    }
                } else {
                    fullCells = false;
                }

                if (grille[ligne][colonne] != '-') {
                    this.grille[ligne + 1][colonne + 1] = grille[ligne][colonne];
                }
            }
        }

        // la grille est valide
        // recherche des combinaisons gagnantes

        // recherche d'une combinaison gagnante dans les colonnes
        // on commence la recherche au bas de la grille
        for (int colonne = NUMBER_OF_COLUMNS; colonne > 0; --colonne) {
            if (checkCombinaisonGagnanteInitiale(6, colonne, -1, 0, true, 6))
                return;
        }

        // recherche d'une combinaison gagnante dans les lignes
        // on commence la recherche au bas de la grille
        for (int ligne = NUMBER_OF_ROWS; ligne > 0; --ligne) {
            if (checkCombinaisonGagnanteInitiale(ligne, 1, 0, 1, false, 7))
                return;
        }

        // recherche d'une combaison gagnante dans les diagonales
        if (
            // d'abord les diagonales du bas vers le haut de gauche à droite
                           checkCombinaisonGagnanteInitiale(4, 1, -1, +1, true, 4)
                        || checkCombinaisonGagnanteInitiale(5, 1, -1, +1, true, 5)
                        || checkCombinaisonGagnanteInitiale(6, 1, -1, +1, true, 6)
                        || checkCombinaisonGagnanteInitiale(6, 2, -1, +1, true, 6)
                        || checkCombinaisonGagnanteInitiale(6, 3, -1, +1, true, 5)
                        || checkCombinaisonGagnanteInitiale(6, 4, -1, +1, true, 4)

                        // ensuite les diagonales du bas vers le haut de droite à gauche
                        || checkCombinaisonGagnanteInitiale(6, 4, -1, -1, true, 4)
                        || checkCombinaisonGagnanteInitiale(6, 5, -1, -1, true, 5)
                        || checkCombinaisonGagnanteInitiale(6, 6, -1, -1, true, 6)
                        || checkCombinaisonGagnanteInitiale(6, 7, -1, -1, true, 6)
                        || checkCombinaisonGagnanteInitiale(5, 7, -1, -1, true, 5)
                        || checkCombinaisonGagnanteInitiale(4, 7, -1, -1, true, 4)
                )
            return;

        if (checkGrilleNulle())
            return;
    }

    /**
     * @return l'état dans lequel est le jeu :
     * EN_COURS, ROUGE_GAGNE, JAUNE_GAGNE, MATCH_NUL
     */
    public EtatJeu getEtatJeu() {
        return this.etatJeu;
    }

    /**
     * @return le joueur dont c'est le tour : 'R' pour rouge, 'J' pour jaune.
     */
    public char getTour() {
        return this.tour;
    }

    /**
     * @param ligne   de 0 à 5
     * @param colonne de 0 à 6
     * @return la couleur - R(ouge) ou J(aune) - du jeton occupant la case
     * aux coordonnées saisies en paramètre (si vide, '-')
     * @throws IllegalArgumentException si les coordonnées sont invalides.
     */
    public char getOccupant(int ligne, int colonne) {
        return this.grille[ligne + 1][colonne + 1];
    }

    /**
     * Un "coup" de puissance 4.
     *
     * @param colonne numéro de colonne où le joueur courant fait glisser son jeton
     *                (compris entre 0 et 6)
     * @throws IllegalStateException    si le jeu est déjà fini, où si la colonne est pleine
     * @throws IllegalArgumentException si l'entier en paramètre est > 6 ou < 0.
     */
    public void jouer(int colonne) {
        int ligne;

        if (this.etatJeu != EtatJeu.EN_COURS) {
            throw new IllegalStateException("Le jeu est terminé");
        }

        if (colonne < 0 || colonne >= NUMBER_OF_COLUMNS) {
            throw new IllegalArgumentException("Mauvaise colonne");
        }

        ++colonne;
        ligne = 1;
        for (; ligne <= NUMBER_OF_ROWS && grille[ligne][colonne] == '-'; ++ligne)
            ;

        if (ligne == 1) {
            throw new IllegalStateException("La colonne est pleine");
        }

        --ligne;

        grille[ligne][colonne] = this.tour;

        checkEnd(ligne, colonne);

        this.tour = this.tour == 'R' ? 'J' : 'R';
    }
}
