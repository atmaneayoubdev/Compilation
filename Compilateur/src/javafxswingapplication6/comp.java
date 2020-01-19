package javafxswingapplication6;

import static java.awt.PageAttributes.MediaType.A;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class comp {
    public static BufferedReader lec = null;
    public static BufferedWriter eG = null;
    public static BufferedWriter eD = null;
    public static BufferedWriter eE = null;
    public static int lex;
    public static char c;
    private static String mot;
    public static String [][] tmotrev ={{"Start_Program"," Debut de programme"},{"End_Program","Fin de programme"},
        {"Give"," Affectation  une valeur "},{"ShowMes","Afichage du message"},
        {"Affect","affectation entre variables"},{"to","de affect"},{"ShowVal","Affichage d'une valeure"},{"Int_Number","Declaration d'entier" },
        {"Real_Number","Declaration de reel"},{"","indication de source"},
        {"Start"," Debut de bloc"},{"If","Instruction conditiunnelle"},{"Else","Sinon"},
        {"Finish","Fin Bloc"},{"--","Debut/Fin condition"},
        {";;","Fin instruction"},{"//.","Debut commentaire"}};
    public static int nbmorev =17;
    static  final int mrev=0, mident=1, ent=2, ree=3, oper=4, chainec=5,virg=13, finins=6,deuxpoint=15,guim=14, 
            par=7,errfinins=8, debfin=9,errlex=10,errmot=11,nl=12,nbmrev=16, mess=13,errcond=20;
    public static int indtlex;
    public static int [] [] tlex;  
    public static String [] tmotid;
    public static String [] tmes;
    public  static int indtmes;
    public  static int indmid; 
    public static double val2;
    public static int val1;
    public static int [] entier; 
    public static Double [] reel;   
    public static int indrel; 
    public static int inde;  
    private static int nbligne=0;
    public static int l;
    private static int k;
    private static int nbn;
    private static int nbe;
    private static int nbr;
 public comp(String F, String G, String D,String E) throws IOException/*F pointeur de fichier source,G pointeur de fichier resultat lexical , D syntaxic,E semantiquie*/
{
    this.tmotid = new String[50];
    this.tlex= new int[100][2];
    this.entier = new int [50];
    this.reel= new Double [20];
    this.tmes=new String[200];
    this.inde=-1;
    this.indrel=-1;
    this.indmid=-1;
    this.indmid=-1;
    this.indtlex=-1;
    this.indtmes=-1;
    lec = new BufferedReader(new FileReader(F));    System.out.println("valeur bufferd aaaa"+lec);
    eG = new BufferedWriter (new FileWriter(G));
    eD = new BufferedWriter(new FileWriter(D));
    eE = new BufferedWriter(new FileWriter(E));
    System.out.println("objet comp");
    while (lec.ready())
    {   System.out.println(" 22222 ");
        lex=unilex();
    }
    synt();
    lec.close();eG.close();
    eD.close();eE.close();
}
 public static int unilex() throws IOException
 {
     System.out.println("ok");
         c=(char) lec.read();
      System.out.println(c);
     while(lec!=null&&(c==' '||c=='\n'||c=='\r')){c=(char)lec.read();}
     mot="";
     if ((c>='a'&&c<='z')||(c>='A'&&c<='Z'))
     {
         mot=obtmot();k=0;
         k=typemot(mot);
         if (c!=':'&&c!=','&&c!=';'&&c!='<'&&c!='>'&&c!='=')
         {
             return k;
         }
     }
     else if(c>='0'&&c<='9')
     {
         mot=cons();k=0;
        k=typecons(mot);
        if (c!=':'&&c!=','&&c!=';'&&c!='<'&&c!='>'&&c!='=')
         {
             return k;
         }
     }
     
      switch(c)
         {
             case';':
             {
                 if(lec.ready())
                 {
                   c=(char)lec.read();
                 if(c==';')
                 {
                     mot=";;";
                     eG.write(mot+"     :mot reserve de fin d'instruction \n");eG.newLine();
                     indtlex++;
                     tlex[indtlex][0]=finins;
                     tlex[indtlex][1]=finins;
                     return finins;
                 }
                 else 
                 {
                     eG.write("     Erreur ; manquant");eG.newLine();
                     avancer();return errfinins;
                 }
                 }
             }
             case':':
             {
                 mot=":";
                 eG.write(mot+"     :carctere reserve");eG.newLine();
                 indtlex++;
                 tlex[indtlex][0]=deuxpoint;
                 tlex[indtlex][1]=deuxpoint;
                 return deuxpoint;
             }
             case',':
             {
                 mot=",";
                 eG.write(mot+"     :carctere reserve");eG.newLine();
                 indtlex++;
                 tlex[indtlex][0]=virg;
                 tlex[indtlex][1]=virg;
                 return virg;
             }
             case'-':
             {
                  if(lec.ready())
                 {
                   c=(char)lec.read();
                 if(c=='-')
                 {
                     mot="--";
                     eG.write(mot+"     :mot reserve debut fin de conditions \n");eG.newLine();
                     indtlex++;
                     tlex[indtlex][0]=debfin;
                     tlex[indtlex][1]=debfin;
                     return debfin;
                 }
                 else 
                 {
                     eG.write("     Erreur - manquant ");eG.newLine();
                     return errcond;
                 }
                 }
             }
             case'/':
                 {   c=(char) lec.read();
                           if( c=='/'){c=(char) lec.read();
                                        if(c=='.')
                                               { 
                                               avancer();nbligne++; mot="//.";
                                               eG.write(mot +"      :Debut commentaire ");
                                               eG.newLine();
                                                 // on ignore toute la ligne
                                               return chainec;
                                               }
                  }
                           else {eG.write("     Erreur symbole incomplet");return errlex;}
                 }
             case '"' :
             { 
                 indtlex++;
             tlex[indtlex][0]=guim;
             tlex[indtlex][1]=guim;
                 message();
                 return mess;
             }
             case '<':
             case'>':
             case'=':
                 mot=mot+c;                  
             eG.write(mot +"        :Operateur de comparaison \n");eG.newLine();
             indtlex++;
             tlex[indtlex][0]=oper;
             tlex[indtlex][1]=oper;
              return oper;
            default:{mot=mot+c;
                            eG.write(mot +"     :Symbole incorrect ligne ignoree \n");eG.newLine();
                            avancer(); 
                            return errlex;
                            }
             }
          
 }
      private static String obtmot()throws IOException {
      
      while (lec!=null&&((c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='_')||(c>='0'&&c<='9')) 
      {mot=mot+c;
       c=(char)lec.read();
      }
      return mot;
    }
      
   public static int typemot(String mot) throws IOException
   { //System.out.println("******"+c+"$$$$$$$");
      if(c==' '||c == '<'||c=='>'||c=='='||c==';'||c==','||c==':'||c=='-'||c=='\n'||c=='\r')
             { int j=ismorev(mot);if( j !=-1)
                                       {indtlex++;
                                        tlex[indtlex][0]=mrev;
                                        tlex[indtlex][1]=j;
                                        // fichier pour anal syn
                                        eG.write(mot +"     :mot reservé  "+ tmotrev[j][1] );eG.newLine();
                                         return mrev;
                                       }
                                       else{ j=ismoid(mot);
                                            indtlex++;
                                            tlex[indtlex][0]=mident;
                                            tlex[indtlex][1]=j;
                                         // fichier pour anal syn
                                         eG.write(mot +"        :Identificateur ");eG.newLine();
                                            return mident;
                                           }  
              }
      else { mot=mot+c; eG.write(mot +"     :Identificateur  ou mot reservé incorect ");eG.newLine(); return errmot;}
   } 
   private static String cons()throws IOException {
      int l=0; val1=0;val2=0;
      while (lec.ready()&& (c>='0'&&c<='9')) 
      {mot=mot+c;l++; val1 =val1*10 + (c -'0');
       c=(char)lec.read();
      }
      System.out.println("spkjbnfmfjkn   "+val1);
      if (c=='.'){mot=mot+c;l++;c=(char)lec.read(); l++; int j=1;
                      while (lec.ready()&& (c>='0'&&c<='9')) 
                                          {mot=mot+c;l++; val2 =val2*10 + (c -'0'); j=j*10;
                                            c=(char)lec.read();
                                          }
                      val2=val2/j;
                 }
      System.out.println("spkjbnfmfjkn   "+val2);
     return mot;
    } 
 public static int typecons(String mot) throws IOException
    { if(val2!=0){int i=0; val2=val1+val2;
                  while(i<=indrel && reel[i]!= val2 )i++;
                   if (i>indrel){ indrel++; reel[i]=val2;}
                   indtlex++;
                   tlex[indtlex][0]=ree;
                   tlex[indtlex][1]=i; 
                   eG.write(mot + "     :Valeur reel ");eG.newLine();
                   return ree;  
                 }
    else {int i=0; 
                  while(i<=inde && entier[i]!= val1 )i++;
                   if (i>inde){ inde++; entier[i]=val1;}
                   indtlex++;
                   tlex[indtlex][0]=ent;
                   tlex[indtlex][1]=i;
                   eG.write(mot + "     :valeur entiere ");eG.newLine();
                   return ent;  
         }
        
    }
    public static int ismorev( String mot) throws IOException
    {
        int i=0;
        while(i<nbmrev && tmotrev[i][0].equals(mot)==false)i++;
        if (i==nbmrev) return  -1;
        else return i;
    }
    
        
     
    public static int ismoid( String mot) throws IOException
    {
       int i=0;
        while(i<=indmid && tmotid[i].equals(mot)==false)i++;
       
        if (i>indmid){ indmid++; tmotid[indmid]=mot;}
       
        return i;  
    } 
    public static void avancer() throws IOException
     {
         while (lec.ready()&&(c!='\n')) 
              {c=(char)lec.read();
              }
     }
    public static void message()throws IOException
     { mot=mot+c; c=(char)lec.read();mot=mot+c;
         while (lec.ready()&&(c!='"')) 
              {c=(char)lec.read();mot=mot+c;
              }
              indtmes++;
              tmes[indtmes]=mot;
              indtlex++;
              tlex[indtlex][0]=mess;
              tlex[indtlex][1]=indtmes;
              c=(char)lec.read();
         eG.write(mot +"        :Message ");eG.newLine();
     }
    
    private void synt() throws IOException
    {
        l=0;
        if(l<=indtlex)
        {
            if(tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Start_Program"))
            {
                eD.write("Start_Program            Debut du programme correct");
                eE.write("Début du programme");eE.newLine();
                eD.newLine();
                l++;lS();
                if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("End_Program"))
                {
                    eD.write("End_Progrem            Fin du programme correct");eD.newLine();
                    eE.write("Fin du programme");eE.newLine();
                }
                else 
                {
                     eD.write("End_Program   Manquant     Fin du programme incorrect");eD.newLine();
                }
            }
            else
            {
                eD.write("Start_Program   Manquant     Debut du programme incorrect");eD.newLine();
                lS(); 
            }    
        }
    }
     public static void lS() throws IOException
    {   
        S();    
    }
    public static void S() throws IOException
    {   if(l<=indtlex-1)
         {LD();
          LI(); }   
    }
    public static void LD() throws IOException
    {
        if(l<=indtlex && tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Int_Number"))
            {
                eD.write("Int_Number ");
                eE.write("Int_Number ");
                l++;nbe=0;nbr=-1;
                if(l<=indtlex &&tlex[l][0]==deuxpoint)
                {
                    l++;eD.write(":");eE.write(":");
                    Lid();
                }
                else{ eD.newLine();eD.write("Errer syntaxique le symbole':' manquand \n");}
            }
                else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Real_Number"))
                {
                    eD.write("Real_Number ");
                    eE.write("Real_Number ");
                    l++;nbr=0;nbe=-1;
                   if(l<=indtlex &&tlex[l][0]==deuxpoint)
                     {
                        l++;eD.write(":");eE.write(":");
                        Lid();
                     }
                         else eD.write("Errer syntaxique le symbole':' manquand\n");
                }
       
    }
    public static void Lid() throws IOException
    {
     if(l<=indtlex &&tlex[l][0]==mident)
     {   
         if(nbr==-1)nbe++;
         else if(nbe==-1)nbr++;
         eD.write(tmotid[tlex[l][1]] );
         eE.write(tmotid[tlex[l][1]] );
         l++;Lid2();
     }
     else eD.write("Errer syntaxique Identifiant manquand \n");
    }
    public static void Lid2() throws IOException
    {
     if(l<=indtlex &&tlex[l][0]==virg)
     {   eD.write(" , ");
         eE.write(" , ");
         l++;Lid();
     }
     else if(l<=indtlex &&tlex[l][0]==finins)
     {   eD.write(" ;;      Déclaration correct \n");
         if(nbr==-1)
         eE.write(" ;;       Declaration de "+nbe+" varible(s) entier \n");
         else eE.write(" ;;   Declaration de "+nbr+" varible(s) reel \n");
         l++;
     }
     else{ eD.write("      Errer syntaxique le symbole ',' ou ';;' manquand \n");eD.newLine();}
    }
    public static void LI() throws IOException
    {
        I();
    }
    public static void I() throws IOException
    {    
        if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Give"))
        {   
            eD.write("Give  ");
            eE.write("Give  ");
            AffGv();S();
        }
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Affect"))
        {   eD.write("Affect ");
            eE.write("Affect ");
            Affect();S();
        }
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("If"))
        {    eD.write("If");
             eE.write("If");
            l++;ICOND();
            if (l<=indtlex && tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Else"))elseIns(); 
         
            S();
        }
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("ShowVal"))
        {    eD.write("ShowVal");
             eE.write("ShowVal");
            l++; ShowV();S();
        }
        
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("ShowMes"))
        {
            eD.write("ShowMes");
            eE.write("ShowMes");
            l++;ShowM();S();
        }
    }
    
    public static void ShowV() throws IOException
    {
        if(l<=indtlex &&tlex[l][0]==deuxpoint)
        {
            eD.write(":");
            eE.write(":");
            l++;
            if(l<=indtlex &&tlex[l][0]==mident)
            {
                eD.write(tmotid[tlex[l][1]]);
                eE.write(tmotid[tlex[l][1]]);
                l++;
                if(l<=indtlex &&tlex[l][0]==finins)
                {
                 eD.write(";;     instruction d'affichage d'un identificateur");eD.newLine();
                 eE.write(";;     instruction d'affichage de la valeur de "+tmotid[tlex[l-1][1]]);eE.newLine();
                 l++;
                }
                else {eD.write("Erreur sytaxique ';;' manquand");eD.newLine();}
            }
            else {eD.write("Erreur syntaxique identifiant manquand");eD.newLine();}
        }
        else {eD.write("Erreur syntaxique ':' manquan");eD.newLine();}
    }
    public static void ShowM() throws IOException
    {    
       if(l<=indtlex &&tlex[l][0]==deuxpoint)
       {
            eD.write(":");
            eE.write(":");
            l++;
            if(l<=indtlex && tlex[l][0]==guim)
            {
               l++;
                if (l<=indtlex && tlex[l][0]==mess)
                {
                    eD.write(tmes[tlex[l][1]]+"affichage de message");eD.newLine();
                    eE.write(tmes[tlex[l][1]]+"affichage de message");eE.newLine();
                    l++;
                    if(tlex[l][0]==finins)
                {
                 eD.write(";;        instruction d'affichage d'un identificateur");eD.newLine();
                 eE.write(";;         instruction d'affichage de la valeur de ");eE.newLine();
                 l++;
                }
                }
            }
            else eD.write("\nErreur syntaxique les guimes sont manquantes\n");
       }
       else eD.write("\nErreur syntaxique  : manquand\n");
    }
    public static void AffGv() throws IOException
    {
            l++;
            if(l<=indtlex &&tlex[l][0]==mident)
            {  
                eD.write(tmotid[tlex[l][1]]);
                eE.write(tmotid[tlex[l][1]]);
                l++;
            if(l<=indtlex &&tlex[l][0]==deuxpoint)
            {   
                eD.write(" : ");
                eE.write(" : ");                            
                l++;
            if(l<=indtlex &&tlex[l][0]==ent)
            {    
                String z=null;
                z=new Integer(entier[tlex[l][1]]).toString();
                System.out.println("bbbbbbbbbbbbbb "+entier[tlex[l][1]]);
                eD.write(" "+z);
                eE.write(" "+z);
                l++;
            if(l<=indtlex &&tlex[l][0]==finins)
            {
                l++;eD.write(" ;;         Instruction affectation de valeur a identifiant \n");
                eE.write("  ;;            Affectation d'une valeur a  "+tmotid[tlex[l-2][1]]+"\n");
            }
            else {eD.write("  Erreur syntaxique fin d'instructions incorect");eD.newLine();}
            }
            else if(l<=indtlex &&tlex[l][0]==ree)
            {   String z=String.valueOf(reel[tlex[l][1]]);
                System.out.println(" "+reel[tlex[l][1]]);
                eD.write(" "+z);
                eE.write(" "+z);
                l++;
            if(tlex[l][0]==finins)
            {
                l++;eD.write(";;"+ "        instruction affectation de valeur a identifiant \n");eD.newLine();
                eE.write("Affectation d'une valeur a"+tmotid[tlex[l-2][1]]);eE.newLine();
            }
            else {eD.write("  Erreur syntaxique fin d'instructions incorect");eD.newLine();}
            } 
             else{eD.write("  Erreur syntaxique la valeur est manquante\n");}
            }
             else {eD.write("  Erreur syntaxique le symbole ':' manquand");eD.newLine();}
            }
            else {eD.write("   Erreur syntaxique identifiant manquand");eD.newLine();}            
    }
    public static void Affect() throws IOException
    {
        l++;
            if(l<=indtlex && tlex[l][0]==mident)
            {
                eD.write(tmotid[tlex[l][1]]);
                eE.write(tmotid[tlex[l][1]]);
                l++;
                 if(l<=indtlex && tlex[l][0]==mrev&& tmotrev[tlex[l][1]][0].equals("to"))
                 {
                     eD.write("to");
                     eE.write("to");
                     l++;
                     if(l<=indtlex && tlex[l][0]==mident)
                     {
                         eD.write(tmotid[tlex[l][1]]);
                         eE.write(tmotid[tlex[l][1]]);
                         l++;
                     if(l<=indtlex && tlex[l][0]==finins)
                     {
                         l++;
                         eD.write(";;"+"   instruction affectation de la valeur d'un identificateur a un autre\n ");eD.newLine();
                         eE.write("affectation de valeur entre variable");eE.newLine();
                     }
                     else {eD.write("erreur syntaxique fin d'instructions incorect");eD.newLine();}
                     }
                     else {eD.write("erreur syntaxique identifiant manquand ");eD.newLine();}
                 }
                 else {eD.write("erreur syntaxique le mot reserve 'to' manquand");eD.newLine();}
            }
            else {eD.write("Erreur syntaxique identifiant manquand");eD.newLine();} 
    }
    
    public static void ICOND() throws IOException
    {
      if (l<=indtlex &&tlex[l][0]==debfin)
      {
          eD.write("--");
          eE.write("--");
          l++;
          if(l<=indtlex && tlex[l][0]==mident)
          {
                eD.write(tmotid[tlex[l][1]]);
                eE.write(tmotid[tlex[l][1]]);
                l++;
                if(l<=indtlex &&tlex[l][0]==oper)
                {
                    eD.write("operateur");
                    eE.write("op");
                    l++;
                    if(l<=indtlex &&tlex[l][0]==mident)
                    {
                     eD.write(tmotid[tlex[l][1]]);
                     eE.write(tmotid[tlex[l][1]]);
                     l++; if(l<=indtlex &&tlex[l][0]==debfin)
                           {
                              eD.write(" --");
                              eE.write(" --");
                              l++;BIns();eE.write("       Condition Alors Action\n");
                              
                           }
                    }
                    else if(l<=indtlex &&tlex[l][0]==ent)
                    {
                        String k= String.valueOf(entier[tlex[l][1]]);
                      eD.write(" "+k );
                      eE.write(" "+k );
                      l++; if(l<=indtlex &&tlex[l][0]==debfin)
                           {
                              eD.write(" -- ");
                              eE.write(" -- ");l++;BIns();eE.write("     Condition Alors Action\n");
                              
                           }
                    }
                    else if(l<=indtlex &&tlex[l][0]==ree)
                    {
                        String z=String.valueOf(reel[tlex[l][1]]);
                        eD.write(z);
                        eE.write(z);
                        l++; if(tlex[l][0]==debfin)
                           {
                              eD.write(" --");
                              eE.write(" --");l++;
                              BIns();
                              eE.write("Condition Alors Action\n");
                              
                           } 
                    }
                    else{eD.write("Erreur syntaxique identifian ou valeur manquand");eD.newLine();}
                }
                else{eD.write("Erreur syntaxique operateur manquand");eD.newLine();}
          }
                    else{eD.write("Erreur syntaxique identifian manquand");eD.newLine();}
      }
                    else{eD.write("Erreur syntaxique '--' manquand");eD.newLine();}
    }
    public static void BIns() throws IOException
    {    
        if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Start"))
        {
            eD.write("Start\n");
            eE.write("Start   debut d'un bloc\n");
            l++;
            I();
             
            if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Finish"))
            {
                eD.write("Finish       bloc instruction\n");
                eE.write("Finish       Fin d'un bloc\n");l++;
            }
            else{eD.write("Erreur syntaxique Finish manquand");eD.newLine();}
        } else Ii();
    }
    public static void elseIns() throws IOException
    { 
        if(l<=indtlex && tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Else"))
        {
           eD.write("Else \n");
           eE.write("Else       Sinon\n");
           l++;BIns();
        }
    }
   //********************
    public static void Ii() throws IOException
    {
        if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Give"))
        {   
            eD.write("Give  ");
            eE.write("Give  ");
            AffGv();
        }
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("Affect"))
        {   eD.write("Affect ");
            eE.write("Affect ");
            Affect();
        }
        else if(l<=indtlex &&tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("If"))
        {    eD.write("If ");
             eE.write("If ");
            l++;ICOND();
        }
        else if(l<=indtlex && tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("ShowVal"))
        {    eD.write("ShowVal ");
             eE.write("ShowVal ");
            l++; ShowV();
        }
        
        else if(l<=indtlex && tlex[l][0]==mrev && tmotrev[tlex[l][1]][0].equals("ShowMes"))
        {
            eD.write("ShowMes ");
            eE.write("ShowMes ");
            l++;ShowM();
        }  
    } 
}