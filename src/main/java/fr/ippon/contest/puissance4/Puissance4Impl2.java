package fr.ippon.contest.puissance4;

/**
 * Created by Bertrand on 08/03/2015.
 */
public class Puissance4Impl2 implements Puissance4 {
    private char[][] grille;
    private char tour;
    private EtatJeu etatJeu;

    int NUMBER_OF_ROWS = 6; // FIXME: vérifier la déclaration de constantes
    int NUMBER_OF_COLUMNS = 7;

    private class Combinaison {
        char joueur = '-';
        int nombreDeJetons = 0;

        void jouer(char joueur) {
            if (this.joueur == joueur) {
                ++this.nombreDeJetons;
            }
            else {
                this.joueur = joueur;
                this.nombreDeJetons = 1;
            }
        }

        boolean estGagnante() {
            return this.nombreDeJetons >= 4;
        }
    }

    private class ColonneInfo {
        Combinaison combinaison = new Combinaison();
        int casesLibres = NUMBER_OF_ROWS;

        public void jouer(char joueur) {
            this.combinaison.jouer(joueur);
            --this.casesLibres;
        }

        public boolean estGagante() {
            return this.combinaison.estGagnante();
        }
    }

    private ColonneInfo[] columns = new ColonneInfo[NUMBER_OF_COLUMNS];

    private boolean checkEnd(int ligne, int colonne) {
        char tour = this.grille[ligne][colonne];

        // check column winner
        {
//            int i;
//            for (i = ligne + 1; i < NOMBRE_DE_LIGNES && grille[i][colonne] == tour; ++i)
//                ;
//
//            if (i - ligne >= 4) {
            if (this.columns[colonne].estGagante()) {
                this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                return true;
            }
        }

        // check line winner
        {
            int i, j;

            for (i = colonne - 1; i >= 0 && grille[ligne][i] == tour; --i)
                ;

            for (j = colonne + 1; j < NUMBER_OF_COLUMNS && grille[ligne][j] == tour; ++j)
                ;

            if (j - i - 1 >= 4) {
                this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                return true;
            }
        }

        // check left-to-right diagonal winner
        {
            int i, j, k, l;

            for (i = colonne - 1, k = ligne - 1; i >= 0 && k >= 0 && grille[k][i] == tour; --i, --k)
                ;

            for (j = colonne + 1, l = ligne + 1; j < NUMBER_OF_COLUMNS && l < NUMBER_OF_ROWS && grille[l][j] == tour; ++j, ++l)
                ;

            if (j - i - 1 >= 4) {
                this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                return true;
            }
        }

        // check right-to-left diagonal winner
        {
            int i, j, k, l;

            for (i = colonne - 1, k = ligne + 1; i >= 0 && k < NUMBER_OF_ROWS && grille[k][i] == tour; --i, ++k)
                ;

            for (j = colonne + 1, l = ligne - 1; j < NUMBER_OF_COLUMNS && l >= 0 && grille[l][j] == tour; ++j, --l)
                ;

            if (j - i - 1 >= 4) {
                this.etatJeu = tour == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                return true;
            }
        }

        // check null
        {
            int i;

            for (i = 0; i < NUMBER_OF_COLUMNS && grille[0][i] != '-'; i++)
                ;

            if (i >= NUMBER_OF_COLUMNS) {
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
        this.grille = new char[][]{{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'}};

//        System.arraycopy(this.modelColumns, 0, columns, 0, modelColumns.length);

        columns = new ColonneInfo[] {
                new ColonneInfo(),
                new ColonneInfo(),
                new ColonneInfo(),
                new ColonneInfo(),
                new ColonneInfo(),
                new ColonneInfo(),
                new ColonneInfo()
        };

        for (int ligne = NUMBER_OF_ROWS - 1; ligne >= 0; --ligne) {
            for (int colonne = 0; colonne < NUMBER_OF_COLUMNS; ++colonne) {
                if (grille[ligne][colonne] != 'R' && grille[ligne][colonne] != 'J' && grille[ligne][colonne] != '-') {
                    throw new IllegalArgumentException("La grille contient des caractères invalides");
                }

                if (grille[ligne][colonne] != '-') {
                    this.grille[ligne][colonne] = grille[ligne][colonne];
                    this.columns[colonne].jouer(grille[ligne][colonne]);
                    if (this.checkEnd(ligne, colonne)) {
                        return;
                    }
                }
            }
        }
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
        return this.grille[ligne][colonne];
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

        for (ligne = 0; ligne < NUMBER_OF_ROWS && grille[ligne][colonne] == '-'; ++ligne)
            ;

        if (ligne == 0) {
            throw new IllegalStateException("La colonne est pleine");
        }

        --ligne;

        grille[ligne][colonne] = this.tour;
        this.columns[colonne].jouer(this.tour);

        checkEnd(ligne, colonne);

        this.tour = this.tour == 'R' ? 'J' : 'R';
    }
}
