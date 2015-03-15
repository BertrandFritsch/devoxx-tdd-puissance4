package fr.ippon.contest.puissance4;

import fr.ippon.contest.puissance4.Puissance4.EtatJeu;
import org.apache.log4j.Logger;

import java.util.Scanner;


public class AppProfiler {

    private static final Logger log = Logger.getLogger(AppProfiler.class);
    Puissance4 jeu;


	public static void main(String[] args) {

        Scanner scanner = new Scanner (System.in);
        scanner.next();

        AppProfiler app = new AppProfiler();
        app.run(app);
	}

    private static void run(AppProfiler app) {
        app.jeu = new Puissance4Impl();

        app.testsErreursChargerJeu();
        app.testChargerJeu();
        app.testJouer();
        app.testChargerLigneGagnante();
        app.testChargerColonneGagnante();
        app.testChargerDiagonaleGagnante();
        app.testjouerLigneGagnante();
        app.testJouerColonneGagnante();
        app.testJouerDiagonaleGagnante();
        app.testJouerNul();
        app.testChargerNul();
    }

    private void fail(String s) {}
    private void assertEquals(Object o1, Object o2) {}

    public void testsErreursChargerJeu() {
        char [][] grille = {{'-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-'},
                {'-', '-', 'R', 'J', 'R', '-'},
                {'-', 'J', 'R', 'R', 'J', '-'},
                {'R', 'J', 'J', 'J', 'R', 'R'}};
        try {
            jeu.chargerJeu(grille, 'J');
            fail("grille invalide - 6 colonnes seulement.");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        char [][] grille2 = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', 'J', 'R', '-', '-'},
                {'-', 'J', 'R', 'R', 'J', '-', '-'}};
        try {
            jeu.chargerJeu(grille2, 'R');
            fail("grille invalide - 5 lignes seulement.");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        char [][] grille3 = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', 'J', 'R', '-', '-'},
                {'-', 'J', 'R', 'R', 'J', '-', '-'},
                {'R', 'J', 'J', 'J', 'R', 'R', '-'}};
        try {
            jeu.chargerJeu(grille3, '-');
            fail("grille invalide - joueur manquant.");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    
    public void testChargerJeu() {
        char [][] grille = {{'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', 'J', 'R', '-', '-'},
                {'-', 'J', 'R', 'R', 'J', '-', '-'},
                {'R', 'J', 'J', 'J', 'R', 'R', '-'}};
        jeu.chargerJeu(grille, 'R');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        assertEquals('R', jeu.getTour());
        assertEquals('J', jeu.getOccupant(4, 4));
    }
    
    public void testJouer() {
        char [][] grille = {{'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', 'J', 'R', '-', '-'},
                {'-', 'J', 'R', 'R', 'J', '-', '-'},
                {'R', 'J', 'J', 'J', 'R', 'R', '-'}};
        jeu.chargerJeu(grille, 'R');
        try {
            jeu.jouer(2);
            fail("colonne pleine.");
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        }
        try {
            jeu.jouer(7);
            fail("colonne invalide.");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            jeu.jouer(-1);
            fail("colonne invalide.");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        jeu.jouer(3);
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        assertEquals('J', jeu.getTour());
        assertEquals('R', jeu.getOccupant(2, 3));
    }
    
    public void testChargerLigneGagnante() {
        char [][] grille = {{'-', '-', '-', '-', '-', 'J', 'R'},
                {'-', '-', 'J', 'R', '-', 'R', 'J'},
                {'-', 'R', 'R', 'R', '-', 'R', 'J'},
                {'-', 'J', 'J', 'J', 'J', 'J', 'J'},
                {'-', 'R', 'J', 'R', 'R', 'J', 'R'},
                {'J', 'R', 'J', 'J', 'R', 'J', 'R'}};
        jeu.chargerJeu(grille, 'R');
        assertEquals(EtatJeu.JAUNE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testChargerColonneGagnante() {
        char [][] grille = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'J', 'J', 'R', '-', '-'},
                {'-', '-', 'J', 'R', 'R', 'R', '-'},
                {'-', '-', 'J', 'R', 'R', 'J', '-'}};
        jeu.chargerJeu(grille, 'R');
        assertEquals(EtatJeu.JAUNE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testChargerDiagonaleGagnante() {
        char [][] grille = {{'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'R', '-', '-', '-', '-'},
                {'-', 'R', 'J', '-', '-', '-', '-'},
                {'-', 'J', 'R', 'J', 'R', '-', '-'},
                {'R', 'J', 'R', 'R', 'J', '-', '-'},
                {'R', 'J', 'J', 'J', 'R', 'R', '-'}};
        jeu.chargerJeu(grille, 'J');
        assertEquals(EtatJeu.ROUGE_GAGNE, jeu.getEtatJeu());

        char [][] grille2 = {{'-', '-', '-', '-', '-', '-', '-'},
                {'R', 'R', 'R', 'J', '-', '-', 'R'},
                {'J', 'J', 'J', 'R', 'J', 'R', 'J'},
                {'R', 'R', 'J', 'J', 'R', 'J', 'J'},
                {'R', 'J', 'R', 'R', 'R', 'J', 'R'},
                {'R', 'J', 'J', 'R', 'R', 'J', 'J'}};
        jeu.chargerJeu(grille2, 'J');
        assertEquals(EtatJeu.ROUGE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testjouerLigneGagnante() {
        char [][] grille = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'R', '-', '-', '-', '-'},
                {'-', '-', 'J', 'J', 'J', 'R', '-'},
                {'-', '-', 'J', 'R', 'R', 'R', '-'},
                {'-', '-', 'J', 'R', 'R', 'J', 'J'}};
        jeu.chargerJeu(grille, 'R');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        jeu.jouer(6);
        assertEquals(EtatJeu.ROUGE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testJouerColonneGagnante() {
        char [][] grille = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', '-', '-', '-', '-'},
                {'-', '-', 'J', 'J', 'R', '-', '-'},
                {'-', '-', 'J', 'R', 'R', '-', '-'},
                {'-', 'R', 'R', 'J', 'J', '-', '-'},
                {'R', 'J', 'R', 'R', 'J', '-', '-'}};
        jeu.chargerJeu(grille, 'J');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        jeu.jouer(2);
        assertEquals(EtatJeu.JAUNE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testJouerDiagonaleGagnante() {
        char [][] grille1 = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'R', '-', '-', '-', '-'},
                {'-', '-', 'J', 'J', '-', '-', '-'},
                {'-', 'R', 'J', 'R', '-', '-', '-'},
                {'J', 'R', 'R', 'J', 'J', '-', '-'},
                {'R', 'J', 'R', 'R', 'J', '-', '-'}};
        jeu.chargerJeu(grille1, 'J');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        jeu.jouer(1);
        assertEquals(EtatJeu.JAUNE_GAGNE, jeu.getEtatJeu());

        char [][] grille2 = {{'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'J', 'J', '-', '-', '-'},
                {'-', '-', 'R', 'R', 'R', 'J', '-'},
                {'-', '-', 'R', 'J', 'J', 'J', '-'},
                {'-', 'R', 'R', 'J', 'J', 'R', '-'}};
        jeu.chargerJeu(grille2, 'R');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        jeu.jouer(4);
        assertEquals(EtatJeu.ROUGE_GAGNE, jeu.getEtatJeu());
    }
    
    public void testJouerNul() {
        char [][] grille = {{'R', '-', 'J', 'R', 'J', 'R', 'J'},
                {'J', 'R', 'R', 'J', 'R', 'R', 'R'},
                {'R', 'J', 'J', 'J', 'R', 'J', 'J'},
                {'J', 'R', 'R', 'J', 'R', 'R', 'R'},
                {'J', 'J', 'R', 'R', 'J', 'J', 'J'},
                {'R', 'J', 'J', 'J', 'R', 'R', 'R'}};
        jeu.chargerJeu(grille, 'J');
        assertEquals(EtatJeu.EN_COURS, jeu.getEtatJeu());
        jeu.jouer(1);
        assertEquals(EtatJeu.MATCH_NUL, jeu.getEtatJeu());
    }
    
    public void testChargerNul() {
        char [][] grille = {{'R', 'J', 'J', 'R', 'J', 'R', 'J'},
                {'J', 'R', 'R', 'J', 'R', 'R', 'R'},
                {'R', 'J', 'J', 'J', 'R', 'J', 'J'},
                {'J', 'R', 'R', 'J', 'R', 'R', 'R'},
                {'J', 'J', 'R', 'R', 'J', 'J', 'J'},
                {'R', 'J', 'J', 'J', 'R', 'R', 'R'}};
        jeu.chargerJeu(grille, 'J');
        assertEquals(EtatJeu.MATCH_NUL, jeu.getEtatJeu());
    }

}
