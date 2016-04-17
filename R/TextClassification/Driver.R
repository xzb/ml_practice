#install.packages("RTextTools")
library("RTextTools")

TrainingSets <- list(
  "data/20news-bydate/20news-bydate-train/alt.atheism",
  "data/20news-bydate/20news-bydate-train/comp.graphics",
  "data/20news-bydate/20news-bydate-train/misc.forsale",
  "data/20news-bydate/20news-bydate-train/rec.autos",
  "data/20news-bydate/20news-bydate-train/sci.crypt"
  )
TrainingLabels <- list(
  "data/20news-bydate/20news-bydate-train/alt_atheism_label.csv",
  "data/20news-bydate/20news-bydate-train/comp_graphics_label.csv",
  "data/20news-bydate/20news-bydate-train/misc_forsale_label.csv",
  "data/20news-bydate/20news-bydate-train/rec_autos_label.csv",
  "data/20news-bydate/20news-bydate-train/sci_crypt_label.csv"
  )
TestingSets <- list(
  "data/20news-bydate/20news-bydate-test/alt.atheism",
  "data/20news-bydate/20news-bydate-test/comp.graphics",
  "data/20news-bydate/20news-bydate-test/misc.forsale",
  "data/20news-bydate/20news-bydate-test/rec.autos",
  "data/20news-bydate/20news-bydate-test/sci.crypt"
  )
TestingLabels <- list(
  "data/20news-bydate/20news-bydate-test/alt_atheism_label.csv",
  "data/20news-bydate/20news-bydate-test/comp_graphics_label.csv",
  "data/20news-bydate/20news-bydate-test/misc_forsale_label.csv",
  "data/20news-bydate/20news-bydate-test/rec_autos_label.csv",
  "data/20news-bydate/20news-bydate-test/sci_crypt_label.csv"
)

# ====== load training data =======
rm(dataFrame)
for (index in 1 : length(TrainingSets))
{
  dataDir <- TrainingSets[[index]]
  labelPath <- TrainingLabels[[index]]
  if(!exists("dataFrame"))
  {
    dataFrame <- read_data(dataDir,type = "folder",index = labelPath, warn=F)
  }
  else
  {
    dataFrame <- rbind(dataFrame, read_data(dataDir,type = "folder",index = labelPath, warn=F))
  }
}
trainingSize <- length(dataFrame$Text.Data)

# ====== load testing data ======
for (index in 1 : length(TestingSets))
{
  dataDir <- TestingSets[[index]]
  labelPath <- TestingLabels[[index]]
  dataFrame <- rbind(dataFrame, read_data(dataDir,type = "folder",index = labelPath, warn=F))
}
wholeSize <- length(dataFrame$Text.Data)

doc_matrix <- create_matrix(dataFrame$Text.Data, language="english", removeNumbers=TRUE, stemWords=TRUE, removeSparseTerms=.998)
container <- create_container(doc_matrix, dataFrame$Labels, trainSize=1:trainingSize, testSize=(trainingSize + 1) : wholeSize, virgin=FALSE)


# ====== create model ======
SVM <- train_model(container,"SVM")
GLMNET <- train_model(container,"GLMNET")
MAXENT <- train_model(container,"MAXENT")
BOOSTING <- train_model(container,"BOOSTING")
#BAGGING <- train_model(container,"BAGGING")
#RF <- train_model(container,"RF")
NNET <- train_model(container,"NNET")
TREE <- train_model(container,"TREE")

# ====== test model ======
SVM_CLASSIFY <- classify_model(container, SVM)
GLMNET_CLASSIFY <- classify_model(container, GLMNET)
MAXENT_CLASSIFY <- classify_model(container, MAXENT)
BOOSTING_CLASSIFY <- classify_model(container, BOOSTING)
#BAGGING_CLASSIFY <- classify_model(container, BAGGING)
#RF_CLASSIFY <- classify_model(container, RF)
NNET_CLASSIFY <- classify_model(container, NNET)
TREE_CLASSIFY <- classify_model(container, TREE)

# ====== summary ======
analytics <- create_analytics(container, cbind(SVM_CLASSIFY, GLMNET_CLASSIFY, MAXENT_CLASSIFY, BOOSTING_CLASSIFY, NNET_CLASSIFY, TREE_CLASSIFY))
summary(analytics)

write.csv(analytics@algorithm_summary, "AlgorithmSummary.csv")


# ====== calculate accuracy ======
predictResult <- list(
  analytics@document_summary$SVM_LABEL,
  analytics@document_summary$LOGITBOOST_LABEL,
  analytics@document_summary$GLMNET_LABEL,
  analytics@document_summary$TREE_LABEL,
  analytics@document_summary$NNETWORK_LABEL,
  analytics@document_summary$MAXENTROPY_LABEL
  )
testingLabel <- dataFrame$Labels[(trainingSize + 1) : wholeSize]

comp <- predictResult[[1]] == testingLabel
accuracy <- sum(comp == TRUE) / length(comp)
for (index in 2 : length(predictResult))
{
  comp <- predictResult[[index]] == testingLabel
  accuracy <- cbind(accuracy, sum(comp == TRUE) / length(comp))
}
colnames(accuracy) <- c("SVM", "LOGITBOOST", "GLMNET", "TREE", "NNETWORK", "MAXENTROPY")

write.csv(accuracy, "Accuracy.csv")
