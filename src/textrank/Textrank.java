package textrank;

import java.io.IOException;
<<<<<<< HEAD
import java.util.HashSet;
import java.util.Set;
=======
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
>>>>>>> 8fa5ad7eb7c31b92408353d46882581b07ddadfb
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preprocessing.Preprocessing;
import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.PunctuationsRemover;

class Score
{
	public int index;
	public double score;
	
	public Score(int index, double score)
	{
		this.index = index;
		this.score = score;
	}
}

class Sortbyscore implements Comparator<Score> {
	 
    // Method
    // Sorting in descending order of score number
    public int compare(Score a, Score b)
    {
 
        return Double.compare(b.score, a.score);
    }
}

class Sortbyindex implements Comparator<Score> {
	 
    // Method
    // Sorting in ascending order of index number
    public int compare(Score a, Score b)
    {
 
        return Double.compare(a.index, b.index);
    }
}

public class Textrank {
	private Preprocessing pre;
	private String summarizedText;
	AraNormalizer arn = new AraNormalizer();
	DiacriticsRemover dr = new DiacriticsRemover();
	LightStemmer10 ls10 = new LightStemmer10();

	
	public String getSummarizedText()
	{
		return summarizedText;
	}
<<<<<<< HEAD
	
	public Textrank(String text) throws ClassNotFoundException, IOException
	{
			pre=new Preprocessing(text);
			
			//Extract features
			
			summarizedText = "";
=======

	public Textrank(String text) throws ClassNotFoundException, IOException {
		pre = new Preprocessing(text);
		double ratio = 0.3;
		summarizedText = "";
		
		// Extract features
		//double[] keyPhrases = keyPhrases(lightText_sentences, topKeys, post);
		//double[] sentenceLocation = sentencelocation(original_paragraphs, originalText_sentences);
		//double[] titleSimilarity = similarityWithTitle(lightText_sentences, tokens, lightSentencesTokens, title, topKeys);
		//double[] senCentrality = sentenceCentrality(rootText_sentences, rootTextTokens, rootSentencesTokens);
		double[] senLength = sentenceLength(pre.getrootSentencesTokens());
		double[] cuePhrases = cueWords(pre.getNormalized_sentences());
		double[] strongWords = positiveKeyWords(pre.getNormalized_sentences());
		double[] numberScores = numberScore(pre.getRootText_sentences(), pre.getrootSentencesTokens());
		//double[] weakWords = occurrenceOfNonEssentialInformation(normalized_sentences);
		
		//Ranking
		ArrayList<Score> sentences_scores = new ArrayList<Score>();
		String[] originalsentences = pre.getOriginalText_sentences();
		
		for (int i = 0; i < originalsentences.length; i++) {
			sentences_scores.add(new Score(i, //keyPhrases[i] + sentenceLocation[i] + titleSimilarity[i] + senCentrality[i] + 
					senLength[i] + cuePhrases[i] + strongWords[i] + numberScores[i])); // + weakWords[i];))
		}
		
		Collections.sort(sentences_scores, new Sortbyscore());
		
		int Summarylength = (int) (ratio * originalsentences.length);
		
		ArrayList<Score> summary = new ArrayList<Score>();
		for(int i=0; i<Summarylength; i++)
			summary.add(sentences_scores.get(i));
		Collections.sort(summary, new Sortbyindex());
		
		for(Score score : summary)
			summarizedText += originalsentences[score.index];

	}

	// A short list of important terms that provide a condensed summary of the main
	// topics of a document
	@SuppressWarnings("unused")
	private int keyPhrases() {
		return 0;
>>>>>>> 8fa5ad7eb7c31b92408353d46882581b07ddadfb
	}
	
	//A short list of important terms that provide a condensed summary of the main topics of a document
	private int keyPhrases()
	{
		return 0;
	}
	
	//Relating to the position of a sentence to the paragraph and document
	private int sentencelocation()
	{
		return 0;
	}
	
	//Similarity or overlapping between a given sentence and the document title.
	public double[] cosineTitle(String[] titleTokens,String[] token,String[] sentences,String[][] sentenceTokens)
	{
		int new_len = token.length + titleTokens.length;
		String[] tokens =  new String [new_len];
		for(int i = 0; i < new_len ;i++)
		{
			if(i < token.length)
			{
				tokens[i] = token[i];
			}
			else
			{
				for(int ii = 0 ; ii<titleTokens.length; ii++)
				{
					tokens[i] = titleTokens[ii];
				}
			}
		}
		
		Set<String> words = new HashSet<String>();
		for(String string :tokens)
		{
			words.add(string);
		}
		String[] WORDS = words.toArray(new String[words.size()]);
		
		double[][] tf = new double[sentences.length+1][WORDS.length];
		double[] isf = new double[WORDS.length];
		double[] cosineScore = new double[sentences.length];
		int countTF = 0;
		int countISF = 0;
		
		for(int i = 0 ; i < WORDS.length ; i++)
		{
			countISF = 0;
			for(int j = 0 ; j < sentences.length + 1 ; j++)
			{ 
				boolean found = false;
				countTF = 0;
				if(j < sentences.length)
				{
					for(int k = 0 ; k < sentenceTokens[j].length ; k++)
					{
						if(sentenceTokens[j][k].matches(WORDS[i]))
						{
							countTF = countTF +1;
							found = true;
						}
					}
				}
				else if(j == sentences.length)
				{
					for(int k = 0 ; k < titleTokens.length ; k++)
					{
						if(titleTokens[k].matches(WORDS[i]))
						{
							countTF = countTF + 1;
							found = true;
						}
					}
				}
				if(found == true)
				{
					countISF = countISF +1;
				}
				if(countTF != 0)
				{
					tf[j][i] = 1 + Math.log10(Double.valueOf(countTF));
				}
				else if(countTF == 0)
				{
					tf[j][i] = 0;
				}
			}
			if(countISF != 0)
			{
				isf[i] = Math.log10(sentences.length +1 /countISF);
			}
			else if(countISF == 0)
			{
				isf[i] = 0;
			}
		}
		
		for(int i = 0 ; i < sentences.length ; i++)
		{
			double sum = 0 , sqrt1 = 0, sqrt2 = 0;
			for(int j = 0 ; j < WORDS.length ; j++)
			{
				sum += tf[i][j] * tf[sentences.length][j] * isf[j]*isf[j];
				sqrt1 += (tf[i][j] * isf[j]) * (tf[i][j] * isf[j]);
				sqrt2 += (tf[sentences.length][j] * isf[j]) * (tf[sentences.length][j] * isf[j]);
			}
			
			cosineScore[i] = sum / (Math.sqrt(sqrt1) * Math.sqrt(sqrt2));
		}
		return cosineScore;
	}
	public double[] similarityWithTitle(String[] sentences_light,String[] token,String[][] sentenceTokens,
			String fileName, String[] KeyPhrases) throws IOException , ClassNotFoundException
	{
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		String[] title = tokens[0].split(" ");
		
		for(int i = 0 ; i < title.length ; i++)
		{
			title[i] = arn.normalize(title[i]);
			title[i] = dr.removeDiacritics(title[i]);
			title[i] = ls10.findStem(title[i]);
		}
		
		double[] sentence_TitleCosineScores = cosineTitle(title, token, sentences_light, sentenceTokens);
		int[] CommonKeyPhrasewithTitle = new int[sentences_light.length];
		
		for(int i = 0 ; i < sentences_light.length ; i++)
		{
			for (int j = 0 ; j <title.length ; j++)
			{
				boolean exist = false;
				if (!title[j].equals(" "))
				{
					if(sentences_light[i].matches(".*\\b"+title[j]+"\\b.*"))
					{
						for(int k = 0 ; k < KeyPhrases.length ; k++)
						{
							if(KeyPhrases[k].matches(".*\\b"+title[j]+"\\b.*"))
							{
								exist = true;
								break;
							}
						}
						if(exist == true)
						{
							CommonKeyPhrasewithTitle[i]++;
						}
					}
				}
			}
		}
		double[] sentence_scores = new double[sentences_light.length];
		for(int i = 0; i < sentence_scores.length ; i++)
		{
			sentence_scores[i] = Math.sqrt(1 + CommonKeyPhrasewithTitle[i]);
		}
		
		double[] TitleSimilarityScores = new double[sentences_light.length];
		for(int i = 0 ; i < sentences_light.length ; )
		{
			TitleSimilarityScores[i] = sentence_TitleCosineScores[i] * sentence_scores[i];
		}
		
		return TitleSimilarityScores;
	}
	
	//The similarity or the overlapping between a sentence and other sentences in the document
	private double[] sentenceCentrality(String[] sentences,String[] tokens, String[][] senTokens)
	{
		Set<String> words = new HashSet<String>();
		for(String str : tokens)
		{
			words.add(str);
		}
		
		String[] WORDS = words.toArray(new String[words.size()]);
		double[][] tf = new double[sentences.length][WORDS.length];
		double[] isf = new double[WORDS.length];
		double[][] cosineScore = new double[sentences.length][sentences.length];
		int countTF = 0;
		int countISF = 0;
		
		for(int i = 0 ; i < WORDS.length ; i++)
		{
			countISF = 0;
			for(int j = 0; j <sentences.length ; j++)
			{
				boolean find = false;
				countTF = 0;
				for(int k = 0 ; k <senTokens[j].length ; k++)
				{
					if(senTokens[j][k].matches(WORDS[i]))
					{
						countTF = countTF + 1;
						 find = true;
					}
					
				}
				if(find == true)
				{
					countISF = countISF +1;
				}
				if(countTF != 0)
				{
					tf[j][i] = 1 + Math.log10(Double.valueOf(countTF));
				}
				else if(countTF == 0)
				{
					tf[j][i] = 0;
				}
			}
			if(countISF != 0)
			{
				isf[i] = Math.log10(sentences.length /countISF);
			}
			else if(countISF == 0)
			{
				isf[i] = 0;
			}	
		}
		for(int k = 0 ; k < sentences.length; k++)
		{
			for(int i = 0 ; i < sentences.length ; i++)
			{
				double sum = 0 , sqrt1 = 0, sqrt2 = 0;
				if(i != k)
				{
				   for(int j = 0 ; j < WORDS.length ; j++)
				   {
					 sum += tf[k][j] * tf[i][j] * isf[j]*isf[j];
					 sqrt1 += (tf[k][j] * isf[j]) * (tf[k][j] * isf[j]);
					 sqrt2 += (tf[i][j] * isf[j]) * (tf[i][j] * isf[j]);
				   }
				
				   cosineScore[k][i] = sum / (Math.sqrt(sqrt1) * Math.sqrt(sqrt2));
			   }
				else if( i == k)
				{
					cosineScore[k][i] = 0;
				}
			}
		}
		
		double[] senCentrality = new double[sentences.length];
		int[] count = new int[sentences.length];
		double max = 0;
		
		for(int k = 0 ; k < sentences.length ; k++)
		{
			count[k] = 0;
			
			for(int i = 0 ; i < sentences.length ; i++)
			{
				if(cosineScore[k][i] > 0.1)
				{
					count[k] ++;
				    if(count[k] > max)
				    {
				    	max = count[k];
				    }
				}	
			}
		}
		for(int i = 0 ; i < sentences.length ; i++)
		{
			if(max == 0 )
			{
				senCentrality[i] = 0 ;
			}
			else
			{
				senCentrality[i] = count[i] / max;
			}
		}
		
		return senCentrality;
	}
<<<<<<< HEAD
	
	//Counting the number of words in the sentence (can be used to classify sentence as too short or too long)
	private int sentenceLength()
	{
		return 0;
	}
	
	//Words in the sentence such as â€œtherefore, finally and thusâ€� can be a good indicators of significant content
	private int cueWords()
	{
		return 0;
	}
	
	//Words that are used to emphasize or focus on special idea such as â€�have outstanding, and support forâ€�
	private double [] positiveKeyWords(String [] sentance)
	{		
		String [] Postive_Words = {
				"ايد","تأييد ","اقر","اقرار","اثبت","اثبات","المثبت من","تحديد","تأكيد","دليل","بينة ","ايجاب","أدلة"
				,"المؤكد من","بالتأكيد","أكد","وثق","توكيد","برهان","شهادة","جزم","تقرير ","تحقيق" 
				,"تقرير", "جزم", "شهادة", "برهان", "توكيد", "من المصدق", "تصديق",		
				"صدق", "دلل", "حدد", "ح قق", "برهن", "شهد", "ذو معنى", "كل المعنى", "داللي"
		};
=======

	// Words that are used to emphasize or focus on special idea such as â€�have
	// outstanding, and support forâ€�
	public double[] positiveKeyWords(String[] sentance) {
		String[] Postive_Words = { "ايد", "تأييد", "اقر", "اقرار", "اثبت", "اثبات", "المثبت من", "تحديد", "تأكيد",
				"دليل", "بينة", "ايجاب", "أدلة", "المؤكد من", "بالتأكيد", "أكد", "وثق", "توكيد", "برهان", "شهادة",
				"جزم", "تقرير", "تحقيق", "تقرير", "جزم", "شهادة", "برهان", "توكيد", "من المصدق", "تصديق", "صدق", "دلل",
				"حدد", "حقق", "برهن", "شهد", "ذو معنى", "كل المعنى", "داللي" };
>>>>>>> 8fa5ad7eb7c31b92408353d46882581b07ddadfb
		for (String str : Postive_Words) {
			str = arn.normalize(str);
		}
		
		int Total = 0 , freq[] = new int [sentance.length];
		for (int i=0;i<sentance.length;i++) {
			for (int j=0;j<Postive_Words.length;j++) {
				Pattern P1 =  Pattern.compile(".*\\b"+Postive_Words[j]+"\\b.*");
				Matcher M = P1.matcher(sentance[i]);
				while(M.find()) {	
					Total++;
					freq[i]++;
				}
			}
		}	
			double Sentance_Score [] = new double[sentance.length];
			if (Total == 0)return Sentance_Score;
			for (int i=0;i<sentance.length;i++) {
				Sentance_Score[i] = ((double)(freq[i])/Total);
			}
			return Sentance_Score;
		}
	
	//Existence of numerical data in the sentence.
	private int sentenceInclusionOfNumericalData()
	{
		return 0;
		
	}
	
	//Words that serve as an explanation words such as â€œfor exampleâ€�
	private int occurrenceOfNonEssentialInformation()
	{
		return 0;
	}
	
	// Occurence of non-essential information Score based.
	public double[] WeakWords_Scoring(String[] Sentences) { 
		String[] WeakWords = {"بالاضافة","علي سبيل المثال","مثل","كمثال","علي اي حال","علاوة علي ذلك","أولا","ثانيا","ثم","زيادة علي ذلك","بصيغة أخري"};
		for(String ww : WeakWords) {
			ww = arn.normalize(ww);
		}
		int[] sentenceCount = new int[Sentences.length];
		int[] sentenceWW = new int[Sentences.length];
		
		for(int i=0;i<Sentences.length;i++) {
			
			String[] SentenceWords = Sentences[i].split(" ");
			sentenceCount[i] = SentenceWords.length;
			
			for(int j=0;j<Sentences.length;j++) {
				Pattern pattern = Pattern.compile(".*\\b" + WeakWords[j] + "\\b.*");
				Matcher matcher = pattern.matcher(Sentences[i]);
				while (matcher.find()) {
					sentenceWW[i]++;
				}	
			}
		}
		
		double[] sentenceScoreWW = new double[Sentences.length];
		int i=0;
		for(String Sentence : Sentences) {
			for(String WW : WeakWords) {
				if(Sentence.startsWith(WW))
					{sentenceScoreWW[i] = -2;break;}
				else {
					sentenceScoreWW[i] = ((double)sentenceWW[i])/((double)sentenceCount[i]);
				}
			}
			i++;
		}
		return sentenceScoreWW;
	}
	
	// Occurence of non-essential information Machine Learning based.
	public double[][] WeakWords_ScoringML(String[] Sentences) { 
		String[] WeakWords = {"بالاضافة","علي سبيل المثال","مثل","كمثال","علي اي حال","علاوة علي ذلك","أولا","ثانيا","ثم","زيادة علي ذلك","بصيغة أخري"};
		for(String ww : WeakWords) {
			ww = arn.normalize(ww);
		}
		int[] sentenceCount = new int[Sentences.length];
		int[] sentenceWW = new int[Sentences.length];
		
		for(int i=0;i<Sentences.length;i++) {
			
			String[] SentenceWords = Sentences[i].split(" ");
			sentenceCount[i] = SentenceWords.length;
			
			for(int j=0;j<Sentences.length;j++) {
				Pattern pattern = Pattern.compile(".*\\b" + WeakWords[j] + "\\b.*");
				Matcher matcher = pattern.matcher(Sentences[i]);
				while (matcher.find()) {
					sentenceWW[i]++;
				}	
			}
		}
		
		double[] sentenceScoreWW_Start = new double[Sentences.length];
		double[] sentenceScoreWW_OtherLocation = new double[Sentences.length];
		int i=0;
		for(String Sentence : Sentences) {
			for(String WW : WeakWords) {
				if(Sentence.startsWith(WW))
					{sentenceScoreWW_Start[i] = -2;break;}
				else {
					sentenceScoreWW_OtherLocation[i] = ((double)sentenceWW[i])/((double)sentenceCount[i]);
				}
			}
			i++;
		}
		double[][] sentenceScoreWW = new double[Sentences.length][2];
		for(int k=0;k<sentenceScoreWW.length;k++)
		{
			sentenceScoreWW [k][0] = sentenceScoreWW_Start[k];
			sentenceScoreWW[k][1] = sentenceScoreWW_OtherLocation[k];
		}
		return sentenceScoreWW;
	}
}
