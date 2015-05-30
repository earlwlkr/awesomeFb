package awesomefb;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

import java.io.*;

/**
 * Created by earl on 5/30/2015.
 */
public class SentimentClassifier {
    File mPolarityDir;
    String[] mCategories;
    DynamicLMClassifier<NGramProcessLM> mClassifier;

    public SentimentClassifier() {
        mPolarityDir = new File("train_datasets");
        mCategories = mPolarityDir.list();
        int nGram = 8;
        mClassifier = DynamicLMClassifier.createNGramProcess(mCategories, nGram);
    }

    void train() {
        for (int i = 0; i < mCategories.length; ++i) {
            String category = mCategories[i];
            Classification classification = new Classification(category);
            File dir = new File(mPolarityDir, mCategories[i]);
            File[] trainFiles = dir.listFiles();
            for (int j = 0; j < trainFiles.length; ++j) {
                File trainFile = trainFiles[j];
                if (isTrainingFile(trainFile)) {
                    try {
                        String review = Files.readFromFile(trainFile, "ISO-8859-1");
                        Classified<CharSequence> classified = new Classified<CharSequence>(review, classification);
                        mClassifier.handle(classified);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String classify(String txt) throws IOException {
        Classification classification = mClassifier.classify(txt);
        String resultCategory = classification.bestCategory();
        return resultCategory;
    }

    boolean isTrainingFile(File file) {
        return file.getName().charAt(2) != '9';  // test on fold 9
    }
}
