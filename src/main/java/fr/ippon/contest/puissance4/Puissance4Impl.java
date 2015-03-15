package fr.ippon.contest.puissance4;

/**
 * Implémentation de l'interface {@link Puissance4}
 * @author Bertrand
 */
public class Puissance4Impl implements Puissance4 {
    private char[][] grille;
    private char tour;
    private EtatJeu etatJeu;

    private final int NOMBRE_DE_LIGNES = 6;
    private final int NOMBRE_DE_COLONNES = 7;

    /**
     * Notes d'implémentation.
     * <p>
     *     La fonction principale de cette classe est de détecter la fin du jeu en recherchant
     * une combinaison gagnante. Cette recherche est faite soit quand une grille est chargeé (fonction {@link Puissance4Impl#chargerJeu}),
     * soit après chaque coup joué (fonction {@link Puissance4Impl#jouer}).
     * Pour cela, la grille est représentée par un tableau à 2 dimensions de même structure que celui qui
     * est fourni à la fonction {@link Puissance4Impl#chargerJeu}.
     * </p>
     * <p>
     *     C'est la fonction {@link Puissance4Impl#vérifieSérieGagnante} qui fait cette vérification. Elle s'attend à recevoir
     * une série de cellules à analyser. Cette série représente soit une colonne de la grille, soit une ligne,
     * soit une diagonale. Selon le contexte d'appel, la longueur de la série varie.
     * Au chargement du jeu, elle reçoit à analyser, toutes les colones, toutes les lignes et toutes les diagonales
     * pouvant contenir une combinaison gagnante.
     * Après chaque coup joué, elle reçoit à analyser la colonne, la ligne et les deux diagonales contenant le
     * nouveau jeton introduit. La recherche se limite aux séries de cellules dont le jeton cré potentiellement une combinaison gagnante.
     * Par exemple, pour une ligne donnée, si le jeton est introduit dans la première cellule, la recherche ne s'effectuera
     * que sur les 4 premières cellules. Tandis que si le jeton est introduit dans la quatrième cellule, elle s'effectuera
     * sur les 7 cellules de la ligne. Un raisonnement identique est appliqué aux colonne et aux diagonales. De plus,
     * pour les colonnes, on tient compte du fait que le jeton est nécessairement introduit dans la première cellule
     * vide de la colonne. L'analyse d'une combinaison gagnante se limitera donc au maximum aux 3 cellules sous ce jeton.
     * </p>
     */

     /**
     * <p>Initialisation du jeu en tirant au sort le joueur qui commence</p>
     */
    public Puissance4Impl() {
        this.nouveauJeu();
    }

    /**
     * Recherche d'une combinaison gagnante dans une série de cellules de la grille.
     * Une série peut être une colonne, une ligne ou une diagonale.
     * La recherche s'arrête à la première combinaison trouvée.
     *
     * <p>La variable d'instance {@link Puissance4Impl#etatJeu} est mise à jour en cas de succès.</p>
     *
     * @param débutLigne ligne à laquelle commence la série
     * @param débutColonne colonne à laquelle commence la série
     * @param incrémentLigne incrément de progression de la ligne
     * @param incrémentColonne incrément de progression de la colonne
     * @param longueurSérie la longueur de la série considérée
      *
     * @return true si une combinaison gagnante est trouvée
     */
    private boolean vérifieSérieGagnante(int débutLigne, int débutColonne, int incrémentLigne, int incrémentColonne, int longueurSérie) {
        char couleurCourante = '-';

        // dés que 4 jetons successifs sont rencontrés, la série est considérée gagnante
        int nombreDeJetonsGagnants = 0;

        // si la série considérée fait partie d'une colonne, on sait que les cellules vides sont toutes au haut de la colonne,
        // donc on peut arrêter la recherche dès la première cellule vide rencontrée
        boolean passerCellulesVides = incrémentColonne != 0;

        // on parcours la série jusqu'à ce que :
        // - soit 4 jetons successifs de la même couleur sont rencontrés
        // - soit, en tenant compte du nombre de jetons identiques rencontrés jusque-là, il ne reste plus suffisamment de jetons dans la série pour qu'il puisse y avoir une combinaison gagnante
        // - soit une cellule vide qui marque la fin des cellules pleines a été rencontrée
        for (; longueurSérie + nombreDeJetonsGagnants >= 4 && (grille[débutLigne][débutColonne] != '-' || passerCellulesVides); --longueurSérie, débutLigne += incrémentLigne, débutColonne += incrémentColonne) {
            if (grille[débutLigne][débutColonne] != couleurCourante) {
                couleurCourante = grille[débutLigne][débutColonne];
                nombreDeJetonsGagnants = 1;
            } else if (grille[débutLigne][débutColonne] != '-') {
                if (nombreDeJetonsGagnants == 3) {
                    this.etatJeu = couleurCourante == 'R' ? EtatJeu.ROUGE_GAGNE : EtatJeu.JAUNE_GAGNE;
                    return true;
                }

                ++nombreDeJetonsGagnants;
            }
        }
        return false;
    }

    /**
     * Vérifie si le match est nul
     * @return true si le match est nul
     */
    private boolean vérifieSiMatchNul() {
        int iColonne = 0;

        for (; iColonne < NOMBRE_DE_COLONNES && grille[0][iColonne] != '-'; iColonne++)
            ;

        if (iColonne >= NOMBRE_DE_COLONNES) {
            this.etatJeu = EtatJeu.MATCH_NUL;
            return true;
        }

        return false;
    }

    /**
     * Vérifie si le jeu est terminé
     * @param ligne la ligne contenant le jeton qui vient d'être ajouté
     * @param colonne la colonne contenant le jeton qui vient d'être ajouté
     * @return true si le jeu est terminé
     */
    private boolean vérifierSiJeuTerminé(int ligne, int colonne) {
        char tour = this.grille[ligne][colonne];
        int nombreDeJetonsLigneInférieur = Math.min(3, NOMBRE_DE_LIGNES - 1 - ligne);
        int nombreDeJetonsLigneSupérieur = Math.min(3, ligne);
        int nombreDeJetonsColonneGauche = Math.min(3, colonne);
        int nombreDeJetonsColonneDroite = Math.min(3, NOMBRE_DE_COLONNES - 1 - colonne);

        // Cette fonction teste les différentes combinaisons qui sont susceptibles d'être gagnantes suite à l'ajout du dernier jeton.
        // La fonction se base sur la fonction vérifieSérieGagnante pour passer en revue les différentes possibilités, l'une après l'autre.
        //
        // Notons que cette recherche pourrait être optimisée en ne cherchant que la combinaison gagnante de la même couleur que le joureur courant,
        // et en partant du dernier jeton introduit. Mais comme cela ne permet pas de gain substanciel, il est préférable de réutiliser la fonction
        // vérifieSérieGagnante et ainsi de n'avoir qu'un seul code de recherche de combinaison gagnante.


        // vérifier les différentes combinaisons potentiellement gagnantes
        return
                // vérifier si la colonne est gagnante
                vérifieSérieGagnante(ligne + nombreDeJetonsLigneInférieur, colonne, -1, 0, nombreDeJetonsLigneInférieur + 1)

             // vérifier si la ligne est gagnante
             || vérifieSérieGagnante(ligne, colonne - nombreDeJetonsColonneGauche, 0, 1, nombreDeJetonsColonneGauche + 1 + nombreDeJetonsColonneDroite)

             // vérifier si la diagonale de gauche à droite de bas en haut est gagnante
             || vérifieSérieGagnante(ligne + Math.min(nombreDeJetonsColonneGauche, nombreDeJetonsLigneInférieur), colonne - Math.min(nombreDeJetonsColonneGauche, nombreDeJetonsLigneInférieur), -1, 1, Math.min(nombreDeJetonsLigneInférieur, nombreDeJetonsColonneGauche) + 1 + Math.min(nombreDeJetonsLigneSupérieur, nombreDeJetonsColonneDroite))

             // vérifier si la dialoginale de droite à gauche de bas en haut est gagnante
             || vérifieSérieGagnante(ligne - Math.min(nombreDeJetonsColonneGauche, nombreDeJetonsLigneSupérieur), colonne - Math.min(nombreDeJetonsColonneGauche, nombreDeJetonsLigneInférieur), 1, 1, Math.min(nombreDeJetonsLigneSupérieur, nombreDeJetonsColonneGauche) + 1 + Math.min(nombreDeJetonsLigneInférieur, nombreDeJetonsColonneDroite))

             // vérifier si le match est nul
             || vérifieSiMatchNul();
    }

    /**
     * Vide la grille de jeu, et tire au sort le joeur qui commence.
     */
    public void nouveauJeu() {
        this.etatJeu = EtatJeu.EN_COURS;
        this.tour = new java.util.Random().nextInt(2) == 0 ? 'R' : 'J';

        this.grille = new char[][]{
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'}
        };
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

        if (grille.length != NOMBRE_DE_LIGNES) {
            throw new IllegalArgumentException("La grille doit avoir exactement 6 lignes");
        }

        for (int i = 0; i < NOMBRE_DE_LIGNES; ++i) {
            if (grille[i].length != NOMBRE_DE_COLONNES) {
                throw new IllegalArgumentException("La grille doit avoir exactement 7 colonnes");
            }
        }

        if (tour != 'R' && tour != 'J') {
            throw new IllegalArgumentException("Le joueur doit être représenté par 'J' ou par 'R");
        }

        this.tour = tour;
        this.etatJeu = EtatJeu.EN_COURS;
        this.grille = new char[][]{
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'}
        };

        // - validation du contenu de la grille
        // - vérification qu'il n'y a pas de jeton "flottant"
        // initialisation de la grille interne
        for (int colonne = 0; colonne < NOMBRE_DE_COLONNES; ++colonne) {
            boolean fullCells = true;
            for (int ligne = NOMBRE_DE_LIGNES - 1; ligne >= 0; --ligne) {

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
                    this.grille[ligne][colonne] = grille[ligne][colonne];
                }
            }
        }

        // à partir de ce point la grille est valide
        // recherche d'une combinaison gagnante

        // recherche d'une combinaison gagnante dans les colonnes
        for (int colonne = 0; colonne < NOMBRE_DE_COLONNES; ++colonne) {
            if (vérifieSérieGagnante(NOMBRE_DE_LIGNES - 1, colonne, -1, 0, NOMBRE_DE_LIGNES))
                return;
        }

        // recherche d'une combinaison gagnante dans les lignes
        // on commence la recherche au bas de la grille
        for (int ligne = NOMBRE_DE_LIGNES - 1; ligne >= 0; --ligne) {
            if (vérifieSérieGagnante(ligne, 0, 0, 1, NOMBRE_DE_COLONNES))
                return;
        }

        // recherche d'une combaison gagnante dans les diagonales
        if (
            // d'abord les diagonales du bas vers le haut de gauche à droite
            // le peu de lignes permet d'exprimer les appels de la fonction vérifieSérieGagnante en extension
               vérifieSérieGagnante(3, 0, -1, +1, 4)
            || vérifieSérieGagnante(4, 0, -1, +1, 5)
            || vérifieSérieGagnante(5, 0, -1, +1, 6)
            || vérifieSérieGagnante(5, 1, -1, +1, 6)
            || vérifieSérieGagnante(5, 2, -1, +1, 5)
            || vérifieSérieGagnante(5, 3, -1, +1, 4)

            // ensuite les diagonales du bas vers le haut de droite à gauche
            || vérifieSérieGagnante(5, 3, -1, -1, 4)
            || vérifieSérieGagnante(5, 4, -1, -1, 5)
            || vérifieSérieGagnante(5, 5, -1, -1, 6)
            || vérifieSérieGagnante(5, 6, -1, -1, 6)
            || vérifieSérieGagnante(4, 6, -1, -1, 5)
            || vérifieSérieGagnante(3, 6, -1, -1, 4)
            )
            return;

        // vérifier si c'est un match nul
        vérifieSiMatchNul();
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
        if (ligne < 0 || ligne >= NOMBRE_DE_LIGNES) {
            throw new IllegalArgumentException("La ligne est invalide");
        }

        if (colonne < 0 || colonne >= NOMBRE_DE_COLONNES) {
            throw new IllegalArgumentException("La colonne est invalide");
        }

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

        if (colonne < 0 || colonne >= NOMBRE_DE_COLONNES) {
            throw new IllegalArgumentException("Mauvaise colonne");
        }

        for (ligne = 0; ligne < NOMBRE_DE_LIGNES && grille[ligne][colonne] == '-'; ++ligne)
            ;

        if (ligne == 0) {
            throw new IllegalStateException("La colonne est pleine");
        }

        --ligne;

        grille[ligne][colonne] = this.tour;

        vérifierSiJeuTerminé(ligne, colonne);

        this.tour = this.tour == 'R' ? 'J' : 'R';
    }
}
