package edu.cmu.cs15618.finalproject.classifier;

public interface RequestNumPredictor {

	public void initClassifier(String trainingSetPath);

	public void updateClassifier(int day, int hour, int minute, int requestNum);

	public int predictRequestNum(int day, int hour, int minute);

	public int predictRequestNum(int minuteCount);
}
