#install.packages("e1071")
library("e1071")

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  trainingData[[col]] <- factor(trainingData[[col]])    # factorize the class column
  
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- naiveBayes(formula, data = trainingData)
  #model <- naiveBayes(trainingData[-col], trainingData[col])
}


findAccuracy <- function (model, testData)
{
  col = ncol(testData)
  testData[[col]] <- factor(testData[[col]])    # factorize the class column
  
  predictResult <- predict(model, testData[, 1 : col - 1])    # factor array
  
  # make sure two array have same factors
  diffFactor1 <- setdiff(levels(predictResult), levels(testData[[col]]))
  diffFactor2 <- setdiff(levels(testData[[col]]), levels(predictResult))
  levels(predictResult) <- c(levels(predictResult), diffFactor1, diffFactor2)
  levels(testData[[col]]) <- c(levels(testData[[col]]), diffFactor1, diffFactor2)
  
  comp <- predictResult == testData[[col]]
  accuracy <- sum(comp == TRUE) / nrow(testData)
}
